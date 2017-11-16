package com.yidiankeyan.science.subscribe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.utils.AudioPlayManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器service
 */
public class TimerService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private int allTime;
    private int currentTime;
    private String type;

    public TimerService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            if (timer != null) {
                timer.cancel();
                timerTask.cancel();
                timer = null;
                timerTask = null;
                currentTime = 0;
                allTime = 0;
            }
            return super.onStartCommand(intent, flags, startId);
        }
        type = intent.getStringExtra("type");
        if (type != null) {
            allTime = intent.getIntExtra("time", 0);
            if (timer == null) {
                currentTime = 0;
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        currentTime++;
                        Log.e("currentTime", "=====" + currentTime);
                        Log.e("allTime", "=====" + allTime);
                        if (currentTime < allTime) {
                            Intent intent1 = new Intent(AudioAlbumActivity.ACTION_TIME_CHANGED);
                            intent1.putExtra("surplusTime", allTime - currentTime);
                            intent1.putExtra("type", type);
                            sendBroadcast(intent1);
                        } else {
                            AudioPlayManager.getInstance().release();
                            Intent intent1 = new Intent(AudioAlbumActivity.ACTION_TIME_CHANGED);
                            intent1.putExtra("surplusTime", allTime - currentTime);
                            intent1.putExtra("type", type);
                            sendBroadcast(intent1);
                            if (timer != null) {
                                timer.cancel();
                                timerTask.cancel();
                                timer = null;
                                timerTask = null;
                                currentTime = 0;
                                allTime = 0;
                            }
                        }
                    }
                };
                timer.scheduleAtFixedRate(timerTask, 0, 1000);
            } else {
                currentTime = 0;
            }
        } else {
            if (timer != null) {
                timer.cancel();
                timerTask.cancel();
                timer = null;
                timerTask = null;
                currentTime = 0;
                allTime = 0;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
