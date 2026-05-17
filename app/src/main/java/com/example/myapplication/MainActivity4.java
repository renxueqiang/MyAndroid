package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4 extends AppCompatActivity {
    private static final String TAG = "MainActivity4";

    private TextView tvServiceLog;
    private DemoService demoService;
    private boolean isBound = false;
    private Intent serviceIntent;

    // bindService 的连接回调：连接成功后就能拿到 Service 实例
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DemoService.LocalBinder binder = (DemoService.LocalBinder) service;
            demoService = binder.getService();
            isBound = true;
            appendLog("onServiceConnected: 绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            demoService = null;
            appendLog("onServiceDisconnected: 服务异常断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serviceIntent = new Intent(this, DemoService.class);
        tvServiceLog = findViewById(R.id.tv_service_log);

        Button btnStart = findViewById(R.id.btn_start_service);
        Button btnStop = findViewById(R.id.btn_stop_service);
        Button btnBind = findViewById(R.id.btn_bind_service);
        Button btnUnbind = findViewById(R.id.btn_unbind_service);
        Button btnCheck = findViewById(R.id.btn_check_service);

        // 方式1：startService，适合“要持续跑任务”的场景
        btnStart.setOnClickListener(v -> {
            startService(serviceIntent);
            appendLog("点击 startService");
        });

        // 停止 startService 启动的服务
        btnStop.setOnClickListener(v -> {
            stopService(serviceIntent);
            appendLog("点击 stopService");
        });

        // 方式2：bindService，适合“页面要调用服务方法”的场景
        btnBind.setOnClickListener(v -> {
            boolean ok = bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
            appendLog("点击 bindService, result=" + ok);
        });

        btnUnbind.setOnClickListener(v -> unbindDemoService());

        // 读取 Service 内部状态，演示绑定后如何调用服务方法
        btnCheck.setOnClickListener(v -> {
            if (!isBound || demoService == null) {
                appendLog("请先 bindService，再读取状态");
                Toast.makeText(this, "请先 bindService", Toast.LENGTH_SHORT).show();
                return;
            }
            appendLog("Service状态: isWorking=" + demoService.isWorking()
                    + ", tick=" + demoService.getTick());
        });
    }

    private void unbindDemoService() {
        if (!isBound) {
            appendLog("当前未绑定，无需 unbind");
            return;
        }
        unbindService(serviceConnection);
        isBound = false;
        demoService = null;
        appendLog("点击 unbindService");
    }

    private void appendLog(String message) {
        Log.d(TAG, message);
        String oldText = tvServiceLog.getText().toString();
        tvServiceLog.setText(message + "\n" + oldText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止 Activity 销毁时还持有绑定
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
            demoService = null;
        }
    }
}
