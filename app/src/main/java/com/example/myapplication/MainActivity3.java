package com.example.myapplication;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "MainActivity3";
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvResult = findViewById(R.id.tv_provider_result);
        Button btnRunDemo = findViewById(R.id.btn_run_provider_demo);
        btnRunDemo.setOnClickListener(v -> runContentProviderDemo());
    }

    private void runContentProviderDemo() {
        StringBuilder sb = new StringBuilder();
        Uri usersUri = DemoContentProvider.CONTENT_URI;

        // 先清空旧数据，保证每次演示结果可预测
        int deleted = getContentResolver().delete(usersUri, null, null);
        sb.append("0) 清空旧数据 delete count=").append(deleted).append('\n');

        // 1) INSERT
        ContentValues insertValues = new ContentValues();
        insertValues.put("name", "小明");
        insertValues.put("age", 12);
        Uri insertedUri = getContentResolver().insert(usersUri, insertValues);
        sb.append("1) insert -> ").append(insertedUri).append('\n');

        if (insertedUri == null) {
            sb.append("插入失败，终止演示");
            tvResult.setText(sb.toString());
            return;
        }

        long insertedId = ContentUris.parseId(insertedUri);

        // 2) QUERY
        sb.append("2) query after insert:\n");
        appendQueryResult(sb, usersUri);

        // 3) UPDATE（按 id 改 age）
        ContentValues updateValues = new ContentValues();
        updateValues.put("age", 13);
        Uri userItemUri = ContentUris.withAppendedId(usersUri, insertedId);
        int updateCount = getContentResolver().update(userItemUri, updateValues, null, null);
        sb.append("3) update count=").append(updateCount).append('\n');
        sb.append("4) query after update:\n");
        appendQueryResult(sb, usersUri);

        // 4) DELETE（按 id 删除）
        int deleteCount = getContentResolver().delete(userItemUri, null, null);
        sb.append("5) delete count=").append(deleteCount).append('\n');
        sb.append("6) query after delete:\n");
        appendQueryResult(sb, usersUri);

        String result = sb.toString();
        tvResult.setText(result);
        Log.d(TAG, result);
    }

    private void appendQueryResult(StringBuilder sb, Uri uri) {
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, "_id DESC")) {
            if (cursor == null) {
                sb.append("cursor is null\n");
                return;
            }
            if (cursor.getCount() == 0) {
                sb.append("no rows\n");
                return;
            }
            int idIndex = cursor.getColumnIndex("_id");
            int nameIndex = cursor.getColumnIndex("name");
            int ageIndex = cursor.getColumnIndex("age");
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                int age = cursor.getInt(ageIndex);
                sb.append("id=").append(id)
                        .append(", name=").append(name)
                        .append(", age=").append(age)
                        .append('\n');
            }
        }
    }
}
