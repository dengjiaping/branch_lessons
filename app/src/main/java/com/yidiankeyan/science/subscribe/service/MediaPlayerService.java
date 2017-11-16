package com.yidiankeyan.science.subscribe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by nby on 2016/8/16.
 */
public class MediaPlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case 1:
//                AudioPlayManager.getInstance().release();
//                AudioPlayManager.getInstance().setUp(SystemConstant.ACCESS_IMG_HOST + intent.getStringExtra("url"), intent.getStringExtra("id"));
//                AudioPlayManager.getInstance().start();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
