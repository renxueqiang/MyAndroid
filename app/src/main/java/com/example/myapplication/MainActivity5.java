package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity5 extends AppCompatActivity {
    private static final String TAG = "MainActivity5";
    private static final int MSG_USER_INFO = 1;
    private static final int MSG_PROGRESS = 2;

    private TextView tvLog;

    // 绑定主线程 Looper：用于把任务切回 UI 线程执行
    private final Handler mainHandler = new Handler(Looper.getMainLooper(), msg -> {
        if (msg.what == MSG_USER_INFO) {
            String name = (String) msg.obj;
            int age = msg.arg1;
            appendLog("handleMessage: what=MSG_USER_INFO, name=" + name + ", age=" + age);
            return true;
        }

        if (msg.what == MSG_PROGRESS) {
            int progress = msg.arg1;
            appendLog("handleMessage: what=MSG_PROGRESS, progress=" + progress + "%");
            return true;
        }

        return false;
    });

    private final Runnable delayedTask = () ->
            appendLog("postDelayed 执行了（2秒后）");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvLog = findViewById(R.id.tv_handler_log);

        Button btnPost = findViewById(R.id.btn_post);
        Button btnPostDelayed = findViewById(R.id.btn_post_delayed);
        Button btnSendMsg = findViewById(R.id.btn_send_msg);
        Button btnBgToUi = findViewById(R.id.btn_bg_to_ui);
        Button btnCancel = findViewById(R.id.btn_cancel);

        // 用法1：post(Runnable) -> 把任务放进主线程消息队列
        btnPost.setOnClickListener(v -> mainHandler.post(() ->
                appendLog("post 执行，当前线程=" + Thread.currentThread().getName())));

        // 用法2：postDelayed -> 延迟入队
        btnPostDelayed.setOnClickListener(v -> {
            appendLog("已调用 postDelayed，2秒后执行");
            mainHandler.postDelayed(delayedTask, 2000);
        });

        // 用法3：sendMessage -> 传递 what/arg/obj
        btnSendMsg.setOnClickListener(v -> {
            Message message = mainHandler.obtainMessage(MSG_USER_INFO);
            message.obj = "小明";
            message.arg1 = 12;
            mainHandler.sendMessage(message);
            appendLog("已 sendMessage(MSG_USER_INFO)");
        });

        // 用法4：子线程发消息给主线程（真实开发最常见）
        btnBgToUi.setOnClickListener(v -> runFakeTaskInBackground());

        // 用法5：取消队列中未执行任务/消息，避免页面退出后误更新 UI
        btnCancel.setOnClickListener(v -> {
            mainHandler.removeCallbacksAndMessages(null);
            appendLog("已 removeCallbacksAndMessages(null)");
        });
    }

    private void runFakeTaskInBackground() {
        appendLog("子线程任务开始...");
        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Message msg = mainHandler.obtainMessage(MSG_PROGRESS);
                msg.arg1 = i * 20;
                mainHandler.sendMessage(msg);
            }
        }, "worker-thread").start();
    }

    private void appendLog(String msg) {
        Log.d(TAG, msg);
        String old = tvLog.getText().toString();
        tvLog.setText(msg + "\n" + old);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止 Activity 销毁后还有消息回调导致内存泄漏或崩溃
        mainHandler.removeCallbacksAndMessages(null);
    }
}
