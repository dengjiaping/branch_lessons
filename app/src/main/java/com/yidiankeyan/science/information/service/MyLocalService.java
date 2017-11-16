package com.yidiankeyan.science.information.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.AudioPlayManager;

import java.util.ArrayList;

/**
 * Created by 刘超PC on 2017/10/30.
 */

public class MyLocalService extends Service {
    private int mPosition;
    private ArrayList<OneDayArticles> mDatas;
    private ArrayList<ColumnAudioBean> mData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mData = intent.getParcelableArrayListExtra("mData");
        mPosition = intent.getIntExtra("position", 0);
        mDatas = intent.getParcelableArrayListExtra("mDatas");//

        if (Build.VERSION.SDK_INT <= 23) {
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            //新建一个Notification管理器;
            //API level 11
            Notification.Builder builder = new Notification.Builder(this);//新建Notification.Builder对象
            PendingIntent intent2 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            //PendingIntent点击通知后所跳转的页面
            if(null != mData && mData.size()>0){
                builder.setContentTitle(mData.get(mPosition).getAudioName());
                builder.setContentText(mData.get(mPosition).getAudioName());
            }else {
                builder.setContentTitle(mDatas.get(mPosition).getTitle());
                builder.setContentText(mDatas.get(mPosition).getContent());
            }
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(intent2);//执行intent
            Notification notification = builder.getNotification();//将builder对象转换为普通的notification
            notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
            manager.notify(1, notification);//运行notification
        } else {
            //获取NotificationManager实例
            NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //获取PendingIntent
            Intent mainIntent = new Intent(this, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //创建 Notification.Builder 对象
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //点击通知后自动清除
                    .setAutoCancel(true)
                    .setContentText("")
                    .setContentIntent(mainPendingIntent);
            if(null != mData && mData.size()>0){
                builder.setContentTitle(mData.get(mPosition).getColumnName());
            }else {
                builder.setContentTitle(mDatas.get(mPosition).getTitle());
            }
            //发送通知
            notifyManager.notify(3, builder.build());
            startForeground(startId, builder.mNotification);
        }
        if(null != mData && mData.size()>0){
            AudioPlayManager.getInstance().init(mData, mPosition, AudioPlayManager.PlayModel.ORDER);
        }else AudioPlayManager.getInstance().init(mDatas, mPosition, AudioPlayManager.PlayModel.ORDER);
        AudioPlayManager.getInstance().ijkStart();
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayManager.getInstance().stop();
        AudioPlayManager.getInstance().release();
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
