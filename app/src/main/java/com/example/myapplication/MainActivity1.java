package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main1);
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


        TextView textView = findViewById(R.id.textView5);
        textView.setBackgroundColor(Color.GREEN);//系统颜色
        textView.setBackgroundResource(R.color.green);//资源配置颜色
//        textView.setBackground(R.drawable);

        // 获取tv_code的布局参数（含宽度和高度）
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        // 修改布局参数中的宽度数值，注意默认px单位，需要把dp数值转成px数值
        params.width = 500;
        params.height = 300;

        textView.setLayoutParams(params); // 设置tv_code的布局参数

        textView.setText("我是文字");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}