package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DemoService extends Service {
    private static final String TAG = "DemoService";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final LocalBinder binder = new LocalBinder();

    private int tick = 0;
    private boolean isWorking = false;

    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (!isWorking) {
                return;
            }
            tick++;
            Log.d(TAG, "service tick=" + tick);
            handler.postDelayed(this, 1000);
        }
    };

    public class LocalBinder extends Binder {
        public DemoService getService() {
            return DemoService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand startId=" + startId);
        startWorkIfNeeded();
        // 被系统杀掉后，不自动重建，适合教学观察生命周期
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        startWorkIfNeeded();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopWork();
        Log.d(TAG, "onDestroy");
    }

    public int getTick() {
        return tick;
    }

    public boolean isWorking() {
        return isWorking;
    }

    private void startWorkIfNeeded() {
        if (isWorking) {
            return;
        }
        isWorking = true;
        handler.post(ticker);
    }

    private void stopWork() {
        isWorking = false;
        handler.removeCallbacks(ticker);
    }
}
