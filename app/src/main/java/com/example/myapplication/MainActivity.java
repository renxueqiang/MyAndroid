package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "我看到你了12333333");
        Log.d("MainActivity", "我看到你了");
        Log.d("MainActivity", "我看到你了");
        Integer[] intArray = { 89, 3, 67, 12, 45 };
        Arrays.sort(intArray, (o1, o2) -> Integer.compare(o2, o1));
        Log.d("MainActivity", Arrays.toString(intArray));


        TextView textView = findViewById(R.id.textView);
        textView.setBackgroundColor(Color.GREEN);//系统颜色
        textView.setBackgroundResource(R.color.green);//资源配置颜色
        textView.setText("我是文字");




//        goNextPage();
    }

    // 跳到下个页面
    private void goNextPage() {
//        TextView tv_hello = findViewById(R.id.tv_hello);
//        tv_hello.setText("3秒后进入下个页面");
// 延迟3秒（3000毫秒）后启动任务mGoNext
        new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(mGoNext, 3000);
    }
    private final Runnable mGoNext = new Runnable() {
        @Override
        public void run() {
// 活动页面跳转，从MainActivity跳到Main2Activity
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        }
    };


}


