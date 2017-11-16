package com.yidiankeyan.science.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.utils.SpUtils;


public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long id = SpUtils.getLongSp(context, "downloadId");
            if (downloadApkId == id) {
                Toast.makeText(DemoApplication.applicationContext, "下载完成", Toast.LENGTH_SHORT).show();
                installApk(context, downloadApkId);
            }
        }
    }

    private static void installApk(Context context, long downloadApkId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
        if (downloadFileUri != null) {
            Log.d("DownloadManager", downloadFileUri.toString());
            try {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } catch (Exception e) {

            }
        } else {
            Log.e("DownloadManager", "下载失败");
        }
    }
}
