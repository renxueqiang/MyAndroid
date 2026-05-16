package com.example.myapplication;

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
    private static final String TAG = "MainActivity1";

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

        // 读取上一个页面传过来的参数示例
        readParamsFromIntent();

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

    private void readParamsFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(TAG, "没有收到任何参数");
            return;
        }

        // 方式1：直接 putExtra 对应的读取
        String nameFromExtra = getIntent().getStringExtra(MainActivity.EXTRA_NAME);
        int ageFromExtra = getIntent().getIntExtra(MainActivity.EXTRA_AGE, -1);
        Log.d(TAG, "方式1-putExtra: name=" + nameFromExtra + ", age=" + ageFromExtra);

        // 方式2：Bundle 对应的读取
        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle != null) {
            String nameFromBundle = bundle.getString("bundle_name");
            int ageFromBundle = bundle.getInt("bundle_age", -1);
            Log.d(TAG, "方式2-Bundle: name=" + nameFromBundle + ", age=" + ageFromBundle);
        }

        // 方式3：Serializable 对应的读取
        UserSerializable serializableUser;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            serializableUser = getIntent().getSerializableExtra(
                    MainActivity.EXTRA_USER_SERIALIZABLE, UserSerializable.class);
        } else {
            serializableUser = (UserSerializable) getIntent().getSerializableExtra(
                    MainActivity.EXTRA_USER_SERIALIZABLE);
        }
        if (serializableUser != null) {
            Log.d(TAG, "方式3-Serializable: name=" + serializableUser.getName()
                    + ", age=" + serializableUser.getAge());
        }

        // 方式4：Parcelable 对应的读取
        UserParcelable parcelableUser;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            parcelableUser = getIntent().getParcelableExtra(
                    MainActivity.EXTRA_USER_PARCELABLE, UserParcelable.class);
        } else {
            parcelableUser = getIntent().getParcelableExtra(MainActivity.EXTRA_USER_PARCELABLE);
        }
        if (parcelableUser != null) {
            Log.d(TAG, "方式4-Parcelable: name=" + parcelableUser.getName()
                    + ", age=" + parcelableUser.getAge());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
