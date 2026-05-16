package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;


//Studio 4.1对应的Gradle版本为6.5，
//更多的版本对应关系见https://developer.android.google.cn/studio/releases/gradle-plugin#updating-plugin
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_AGE = "extra_age";
    public static final String EXTRA_BUNDLE = "extra_bundle";
    public static final String EXTRA_USER_SERIALIZABLE = "extra_user_serializable";
    public static final String EXTRA_USER_PARCELABLE = "extra_user_parcelable";
    // 学习重点：
    // 1. 最常用：onCreate / onResume / onPause
    // 2. 常见但次要：onStart / onStop
    // 3. 较少直接用：onRestart / onDestroy
    // 4. 最容易误用：onPause 里做重活、把一次性初始化放到 onResume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 最常用：页面第一次创建时调用。
        // 适合做：初始化界面、绑定控件、恢复数据、设置监听器。
        // 类比 iOS：viewDidLoad
        Log.d(TAG, "onCreate -> 类比 iOS 的 viewDidLoad");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn02 = findViewById(R.id.button2);
        btn02.setOnClickListener(this::btn02Click);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 页面“即将可见”。
        // 适合做：开始准备一些轻量工作，比如注册广播、准备刷新数据。
        // 使用频率：中等，页面每次从不可见变成可见都会走。
        Log.d(TAG, "onStart -> 类比 iOS 的 viewWillAppear");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 最常用：页面已经在前台，用户可以直接操作。
        // 适合做：恢复动画、恢复相机/传感器、开始真正的交互逻辑。
        // 类比 iOS：viewDidAppear
        Log.d(TAG, "onResume -> 类比 iOS 的 viewDidAppear");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 最常用：页面要失去焦点，但还没完全看不见。
        // 适合做：暂停动画、暂停播放、保存少量临时状态。
        // 注意：这里要快，别做耗时操作。
        // 类比 iOS：viewWillDisappear
        Log.d(TAG, "onPause -> 类比 iOS 的 viewWillDisappear");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 页面已经完全不可见。
        // 适合做：释放较重资源、停止监听、取消刷新、断开不需要的连接。
        // 使用频率：中等，页面退到后台或被别的页面完全盖住时调用。
        // 类比 iOS：viewDidDisappear
        Log.d(TAG, "onStop -> 类比 iOS 的 viewDidDisappear");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 最后一次清理机会。
        // 适合做：释放对象、注销监听、关闭资源。
        // 注意：进程被系统杀掉时，不一定总能等到这里。
        // 类比 iOS：dealloc
        Log.d(TAG, "onDestroy -> 类比 iOS 的 dealloc");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 页面从“不可见”状态回来时调用，通常接着会走 onStart -> onResume。
        // 适合做：从后台回来后重新刷新数据。
        // 使用频率：较少，只有从 stopped 状态回到前台才会触发。
        Log.d(TAG, "onRestart -> 页面从不可见回到可见");
    }

    public void btnClick(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity1.class);

        // 方式1：直接 putExtra（最常用）
        intent.putExtra(EXTRA_NAME, "小明");
        intent.putExtra(EXTRA_AGE, 12);

        // 方式2：Bundle 打包后再传
        Bundle bundle = new Bundle();
        bundle.putString("bundle_name", "小明");
        bundle.putInt("bundle_age", 12);
        intent.putExtra(EXTRA_BUNDLE, bundle);

        // 方式3：Serializable 对象传递
        UserSerializable serializableUser = new UserSerializable("小明", 12);
        intent.putExtra(EXTRA_USER_SERIALIZABLE, serializableUser);

        // 方式4：Parcelable 对象传递（Android 推荐，性能更好）
        UserParcelable parcelableUser = new UserParcelable("小明", 12);
        intent.putExtra(EXTRA_USER_PARCELABLE, parcelableUser);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void btn02Click(View view) {

        //延迟3秒(3000毫秒)后启动任务
        new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(mGoNext, 3000);
    }


    private final Runnable mGoNext = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        }

    };


}
