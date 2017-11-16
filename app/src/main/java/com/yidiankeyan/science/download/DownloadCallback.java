package com.yidiankeyan.science.download;

import android.text.TextUtils;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.DownloadApkProgress;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.utils.SystemConstant;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by wyouflf on 15/11/10.
 */
/*package*/ class DownloadCallback implements
        Callback.CommonCallback<File>,
        Callback.ProgressCallback<File>,
        Callback.Cancelable {

    private DownloadInfo downloadInfo;
    private WeakReference<DownloadViewHolder> viewHolderRef;
    private DownloadManager downloadManager;
    private boolean cancelled = false;
    private Cancelable cancelable;

    public DownloadCallback(DownloadViewHolder viewHolder) {
        this.switchViewHolder(viewHolder);
    }

    public boolean switchViewHolder(DownloadViewHolder viewHolder) {
        if (viewHolder == null) return false;

        synchronized (DownloadCallback.class) {
            if (downloadInfo != null) {
                if (this.isStopped()) {
                    return false;
                }
            }
            this.downloadInfo = viewHolder.getDownloadInfo();
            this.viewHolderRef = new WeakReference<DownloadViewHolder>(viewHolder);
        }
        return true;
    }

    public void setDownloadManager(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    private DownloadViewHolder getViewHolder() {
        if (viewHolderRef == null) return null;
        DownloadViewHolder viewHolder = viewHolderRef.get();
        if (viewHolder != null) {
            DownloadInfo downloadInfo = viewHolder.getDownloadInfo();
            if (this.downloadInfo != null && this.downloadInfo.equals(downloadInfo)) {
                return viewHolder;
            }
        }
        return null;
    }

    @Override
    public void onWaiting() {
        try {
            downloadInfo.setState(DownloadState.WAITING);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        DownloadViewHolder viewHolder = this.getViewHolder();
        if (viewHolder != null) {
            viewHolder.onWaiting();
        }
    }

    @Override
    public void onStarted() {
        try {
            downloadInfo.setState(DownloadState.STARTED);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        DownloadViewHolder viewHolder = this.getViewHolder();
        if (viewHolder != null) {
            viewHolder.onStarted();
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (isDownloading) {
            if (!TextUtils.equals(downloadInfo.getContentId(), "-1")) {
                try {
                    downloadInfo.setState(DownloadState.STARTED);
                    downloadInfo.setFileLength(total);
                    downloadInfo.setProgress((int) (current * 100 / total));
                    downloadManager.updateDownloadInfo(downloadInfo);
                } catch (DbException ex) {
                    LogUtil.e(ex.getMessage(), ex);
                }
                DownloadViewHolder viewHolder = this.getViewHolder();
                if (viewHolder != null) {
                    viewHolder.onLoading(total, current);
                }
            } else {
                EventMsg msg = EventMsg.obtain(SystemConstant.DOWNLOAD_APK);
                DownloadApkProgress downloadApkProgress = new DownloadApkProgress(current, total);
                msg.setBody(downloadApkProgress);
                EventBus.getDefault().post(msg);
            }
        }
    }

    @Override
    public void onSuccess(File result) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(DownloadState.FINISHED);
                downloadManager.updateDownloadInfo(downloadInfo);
                DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(downloadInfo.getContentId(), 1, downloadInfo.getFileType());
                EventMsg msg = EventMsg.obtain(SystemConstant.DOWNLOAD_FINISH);
                msg.setBody(downloadInfo.getContentId());
                downloadManager.removeDownload(downloadInfo);
                EventBus.getDefault().post(msg);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onSuccess(result);
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        synchronized (DownloadCallback.class) {
            try {
                switch (downloadInfo.getFileType()) {
                    case 1:
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(downloadInfo.getContentId());
                        break;
                    case 2:
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadIssues(downloadInfo.getContentId());
                        break;
                    case 3:
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadBook(downloadInfo.getContentId());
                        break;
                    case 4:
                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadMagazine(downloadInfo.getContentId());
                        break;
                }
                downloadInfo.setState(DownloadState.ERROR);
                downloadManager.updateDownloadInfo(downloadInfo);
                downloadManager.removeDownload(downloadInfo);
            } catch (DbException e) {
                LogUtil.e(e.getMessage(), e);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onError(ex, isOnCallback);
            }
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(DownloadState.STOPPED);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onCancelled(cex);
            }
        }
    }

    @Override
    public void onFinished() {
        cancelled = false;
    }

    private boolean isStopped() {
        DownloadState state = downloadInfo.getState();
        return isCancelled() || state.value() > DownloadState.STARTED.value();
    }

    @Override
    public void cancel() {
        cancelled = true;
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
