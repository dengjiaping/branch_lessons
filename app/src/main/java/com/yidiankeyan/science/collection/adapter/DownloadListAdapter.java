package com.yidiankeyan.science.collection.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadInfo;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.download.DownloadState;
import com.yidiankeyan.science.download.DownloadViewHolder;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.my.activity.MyDownloadActivity;
import com.yidiankeyan.science.my.adapter.DownloadFinishAdapter;
import com.yidiankeyan.science.utils.Util;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by nby on 2016/8/2.
 * 点藏-下载列表
 */
public class DownloadListAdapter extends BaseAdapter {

    private DownloadManager downloadManager;
    private LayoutInflater mInflater;
    private Context mContext;

    private DownloadListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DownloadListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public DownloadListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        downloadManager = DownloadManager.getInstance();
    }

    @Override
    public int getCount() {
        if (downloadManager == null) return 0;
        Log.e("size", "====" + downloadManager.getDownloadListCount());
        return downloadManager.getDownloadListCount();
    }

    @Override
    public Object getItem(int position) {
        return downloadManager.getDownloadInfo(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("position", "====" + position);
        DownloadItemViewHolder holder = null;
        final DownloadInfo downloadInfo = downloadManager.getDownloadInfo(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_download, null);
            holder = new DownloadItemViewHolder(convertView, downloadInfo);
            convertView.setTag(holder);
            holder.refresh();
        } else {
            holder = (DownloadItemViewHolder) convertView.getTag();
            holder.update(downloadInfo);
        }
        if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
            try {
                downloadManager.startDownload(
                        downloadInfo.getUrl(),
                        downloadInfo.getLabel(),
                        downloadInfo.getFileSavePath(),
                        downloadInfo.isAutoResume(),
                        downloadInfo.isAutoRename(),
                        holder,
                        downloadInfo.getContentId(),
                        downloadInfo.getFileType(),
                        downloadInfo.getContentNum());
            } catch (DbException ex) {
                Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
            }
        }

//        holder.mIvRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onItemClickListener != null){
//                    onItemClickListener.onItemClick(position);
//                }
//            }
//        });
//        if (downloadInfo.getState().value() == DownloadState.FINISHED.value())
        return convertView;
    }

    public class DownloadItemViewHolder extends DownloadViewHolder {

        @ViewInject(R.id.img_avatar)
        private ImageView imgAvatar;
        @ViewInject(R.id.tv_title)
        private TextView tvTitle;
        @ViewInject(R.id.tv_progress)
        private TextView tvProgress;
        @ViewInject(R.id.tv_status)
        private TextView tvStatus;
//        @ViewInject(R.id.iv_remove)
//        private ImageView mIvRemove;
//        @ViewInject(R.id.img_state)
//        private ImageView imgState;
//        @ViewInject(R.id.tv_state)
//        private TextView tvState;
//        @ViewInject(R.id.pb_download)
//        private ProgressBar pbDownload;

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.tv_status)
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this,
                                downloadInfo.getContentId(),
                                downloadInfo.getFileType(),
                                downloadInfo.getContentNum());
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                default:
                    break;
            }
        }

//        @Event(R.id.img_remove)
//        private void removeEvent(View view) {
//            try {
//                downloadManager.removeDownload(downloadInfo);
//                switch (downloadInfo.getFileType()) {
//                    case 1:
//                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(downloadInfo.getContentId());
//                        break;
//                    case 2:
//                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadIssues(downloadInfo.getContentId());
//                        break;
//                    case 3:
//                        DB.getInstance(DemoApplication.applicationContext).deleteDownloadBook(downloadInfo.getContentId());
//                        break;
//                }
//                notifyDataSetChanged();
//            } catch (DbException e) {
//                Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
//            }
//        }

        private void refresh() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) mContext).isDestroyed())
                    return;
            }
            tvTitle.setText(downloadInfo.getLabel());
            imgAvatar.setImageResource(R.drawable.icon_hotload_failed);
            if (downloadInfo.getFileType() == 1) {
                //音频专辑
                AlbumContent content = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(downloadInfo.getContentId());
                if (content != null) {
                    Glide.with(mContext).load(Util.getImgUrl(content.getCoverimgurl()))
                            .error(R.drawable.icon_hotload_failed)
                            .placeholder(R.drawable.icon_hotload_failed)
                            .into(imgAvatar);
                }
            } else if (downloadInfo.getFileType() == 2) {
                IssuesDetailBean issues = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(downloadInfo.getContentId());
                Glide.with(mContext).load(Util.getImgUrl(issues.getCoverimg()))
                        .error(R.drawable.icon_hotload_failed)
                        .placeholder(R.drawable.icon_hotload_failed)
                        .into(imgAvatar);
            } else if (downloadInfo.getFileType() == 3) {
                MozReadDetailsBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFile(downloadInfo.getContentId());
                if (albumContent != null) {
                    Glide.with(mContext).load(Util.getImgUrl(albumContent.getCoverimgurl()))
                            .error(R.drawable.icon_hotload_failed)
                            .placeholder(R.drawable.icon_hotload_failed)
                            .into(imgAvatar);
                }
            } else if (downloadInfo.getFileType() == 4) {
                MagazineExcerptBean magazine = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(downloadInfo.getContentId());
                if (magazine != null) {
                    Glide.with(mContext).load(Util.getImgUrl(magazine.getCoverimg()))
                            .error(R.drawable.icon_hotload_failed)
                            .placeholder(R.drawable.icon_hotload_failed)
                            .into(imgAvatar);
                }
            }
            DownloadState state = downloadInfo.getState();
            double totalSize = (downloadInfo.getFileLength() / 1024.0 / 1024.0);
//            tvTotalSize.setText(Util.round(totalSize, 1) + "M");
            double currentSize = (totalSize * downloadInfo.getProgress() / 100.0);

            if (totalSize == 0 || currentSize == 0) {
                tvProgress.setText("已下载  0%");
            } else {
                tvProgress.setText("已下载  " + (int) ((currentSize / totalSize) * 100) + "%");
            }

//            tvDownloadedSize.setText(Util.round(currentSize, 1) + "M");
            switch (state) {
                case WAITING:
                    tvStatus.setText("等待");
                    tvStatus.setTextColor(Color.WHITE);
                    tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_download_state_white));
                    break;
                case STARTED:
                    tvStatus.setText("暂停");
                    tvStatus.setTextColor(Color.parseColor("#111111"));
                    tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_download_state_white));
                    break;
                case ERROR:
                case STOPPED:
                    tvStatus.setText("继续");
                    tvStatus.setTextColor(Color.WHITE);
                    tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_download_state_black));
                    break;
                case FINISHED:
                    try {
                        downloadManager.removeDownload(downloadInfo);
                        notifyDataSetChanged();
                    } catch (DbException e) {
                        Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            refresh();
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if (DemoApplication.getInstance().activityExisted(MyDownloadActivity.class))
                refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();
        }
    }
}
