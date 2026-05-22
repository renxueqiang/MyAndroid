package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

// Studio 4.1对应的Gradle版本为6.5，
// 更多的版本对应关系见https://developer.android.google.cn/studio/releases/gradle-plugin#updating-plugin
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ACTION_DEMO_BROADCAST = "com.example.myapplication.ACTION_DEMO_BROADCAST";
    public static final String EXTRA_BROADCAST_MESSAGE = "extra_broadcast_message";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_AGE = "extra_age";
    public static final String EXTRA_BUNDLE = "extra_bundle";
    public static final String EXTRA_USER_SERIALIZABLE = "extra_user_serializable";
    public static final String EXTRA_USER_PARCELABLE = "extra_user_parcelable";
    public static final String EXTRA_RESULT_MESSAGE = "extra_result_message";

    private static final int ITEM_INTENT = 1;
    private static final int ITEM_RESULT_AND_BROADCAST = 2;
    private static final int ITEM_CONTENT_PROVIDER = 3;
    private static final int ITEM_SERVICE = 4;
    private static final int ITEM_HANDLER = 5;

    private boolean isDemoReceiverRegistered = false;

    private final BroadcastReceiver demoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_DEMO_BROADCAST.equals(intent.getAction())) {
                String msg = intent.getStringExtra(EXTRA_BROADCAST_MESSAGE);
                Log.d(TAG, "收到广播: " + msg);
                Toast.makeText(MainActivity.this, "收到广播: " + msg, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final ActivityResultLauncher<Intent> main2Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String message = result.getData().getStringExtra(EXTRA_RESULT_MESSAGE);
                    Log.d(TAG, "收到 MainActivity2 回调: " + message);
                    Toast.makeText(MainActivity.this, "MainActivity2回调: " + message, Toast.LENGTH_SHORT).show();
                }
            });

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

        RecyclerView recyclerView = findViewById(R.id.rv_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MenuAdapter(buildMenuItems(), this::onMenuItemClicked));

        // Demo 场景：即使跳到 MainActivity2，也希望 MainActivity 还能收到广播
        registerDemoBroadcastReceiver();
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
        unregisterDemoBroadcastReceiver();
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

    private List<HomeMenuItem> buildMenuItems() {
        return Arrays.asList(
                new HomeMenuItem(ITEM_INTENT, "Intent 传参与接收", "演示 putExtra/Bundle/Serializable/Parcelable"),
                new HomeMenuItem(ITEM_RESULT_AND_BROADCAST, "Activity 回调 + 广播", "传 name/age 到 MainActivity2，并回传 + 发广播"),
                new HomeMenuItem(ITEM_CONTENT_PROVIDER, "ContentProvider 示例", "演示 insert/query/update/delete"),
                new HomeMenuItem(ITEM_SERVICE, "Service 示例", "演示 start/stop 与 bind/unbind"),
                new HomeMenuItem(ITEM_HANDLER, "Handler 消息机制", "演示 post/sendMessage/线程切换/取消消息")
        );
    }

    private void onMenuItemClicked(HomeMenuItem item) {
        switch (item.id) {
            case ITEM_INTENT:
                openIntentDemo();
                break;
            case ITEM_RESULT_AND_BROADCAST:
                openMain2WithResult();
                break;
            case ITEM_CONTENT_PROVIDER:
                startActivity(new Intent(this, MainActivity3.class));
                break;
            case ITEM_SERVICE:
                startActivity(new Intent(this, MainActivity4.class));
                break;
            case ITEM_HANDLER:
                startActivity(new Intent(this, MainActivity5.class));
                break;
            default:
                break;
        }
    }

    private void openIntentDemo() {
        // 方式1：直接传 name/age
        Intent intent = new Intent(this, MainActivity1.class);
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

    private void openMain2WithResult() {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra(EXTRA_NAME, "小明");
        intent.putExtra(EXTRA_AGE, 12);
        main2Launcher.launch(intent);
    }

    private void registerDemoBroadcastReceiver() {
        if (isDemoReceiverRegistered) {
            return;
        }
        IntentFilter filter = new IntentFilter(ACTION_DEMO_BROADCAST);
        registerReceiver(demoBroadcastReceiver, filter, RECEIVER_NOT_EXPORTED);
        isDemoReceiverRegistered = true;
    }

    private void unregisterDemoBroadcastReceiver() {
        if (!isDemoReceiverRegistered) {
            return;
        }
        unregisterReceiver(demoBroadcastReceiver);
        isDemoReceiverRegistered = false;
    }

    private interface OnMenuItemClickListener {
        void onClick(HomeMenuItem item);
    }

    private static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
        private final List<HomeMenuItem> items;
        private final OnMenuItemClickListener listener;

        MenuAdapter(List<HomeMenuItem> items, OnMenuItemClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_menu, parent, false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            HomeMenuItem item = items.get(position);
            holder.title.setText(item.title);
            holder.subtitle.setText(item.subtitle);
            holder.itemView.setOnClickListener(v -> listener.onClick(item));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class MenuViewHolder extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView subtitle;

            MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv_item_title);
                subtitle = itemView.findViewById(R.id.tv_item_subtitle);
            }
        }
    }

    private static class HomeMenuItem {
        final int id;
        final String title;
        final String subtitle;

        HomeMenuItem(int id, String title, String subtitle) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
