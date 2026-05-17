package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvReceive = findViewById(R.id.tv_receive);
        Button btnBack = findViewById(R.id.btn_back_with_result);
        Button btnSendBroadcast = findViewById(R.id.btn_send_broadcast);

        // 接收上个页面传来的参数并展示
        String name = getIntent().getStringExtra(MainActivity.EXTRA_NAME);
        int age = getIntent().getIntExtra(MainActivity.EXTRA_AGE, -1);
        tvReceive.setText("收到参数: name=" + name + ", age=" + age);

        // 点击按钮返回并回传“我知道了”
        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MainActivity.EXTRA_RESULT_MESSAGE, "我知道了");
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // 点击按钮发送广播（同 App 内动态广播示例）
        btnSendBroadcast.setOnClickListener(v -> {
            Intent broadcastIntent = new Intent(MainActivity.ACTION_DEMO_BROADCAST);
            broadcastIntent.putExtra(MainActivity.EXTRA_BROADCAST_MESSAGE, "来自 MainActivity2 的广播消息");
            sendBroadcast(broadcastIntent);
        });
    }
}
