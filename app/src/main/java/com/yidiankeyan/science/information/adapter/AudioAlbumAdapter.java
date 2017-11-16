package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadInfo;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2017/7/15.
 * 作用：
 */

public class AudioAlbumAdapter extends RecyclerAdapter<AlbumContent> {
    public AudioAlbumAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<AlbumContent> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends BaseViewHolder<AlbumContent> {
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvLength;
        private ImageView imgDownload;
        private ImageView imgPlayer;
        private TextView tvIsnew;
        private TextView tvProgress;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_audio_album);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvLength = (TextView) findViewById(R.id.tv_length);
            imgDownload = (ImageView) findViewById(R.id.img_download);
            imgPlayer = (ImageView) findViewById(R.id.img_player);
            tvIsnew = (TextView) findViewById(R.id.tv_isnew);
            tvProgress = (TextView) findViewById(R.id.tv_progress);
        }

        @Override
        public void setData(final AlbumContent data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getCoverimgurl())).into(imgAvatar);
            tvName.setText(data.getArticlename());
            tvTime.setText(new SimpleDateFormat("yyyy年MM月").format(new Date(data.getLastupdatetime())));
            int length = data.getLength();
            if (length == 0)
                tvLength.setText("0");
            else {
                tvLength.setText(TimeUtils.formatTime(data.getLength() * 1000));
            }
            if (data.getIsNew() == 1) {
                tvIsnew.setVisibility(View.VISIBLE);
            } else {
                tvIsnew.setVisibility(View.GONE);
            }
            boolean show = false;
            for (DownloadInfo info : DownloadManager.getInstance().getDownloadInfoList()) {
                if (TextUtils.equals(info.getContentId(), data.getArticleid())) {
                    info.setHolder(this);
                    show = true;
                    break;
                }
            }
            if (show) {
                tvProgress.setVisibility(View.VISIBLE);
            } else {
                tvProgress.setVisibility(View.GONE);
            }
            AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(data.getArticleid());
            if (albumContent == null || !Util.fileExisted(albumContent.getFilePath())) {
                imgDownload.setImageResource(R.drawable.icon_audio_album_download);
                imgDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        download(data);
                    }
                });
            } else {
                Log.e("aaaaaa", "setData");
                imgDownload.setImageResource(R.drawable.icon_audio_album_download_finish);
                imgDownload.setOnClickListener(null);
            }
            if (TextUtils.equals(data.getArticleid(), AudioPlayManager.getInstance().getCurrId()) && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                    || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
                //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
                imgPlayer.setImageResource(R.drawable.audio_click_stop);
                tvName.setTextColor(getContext().getResources().getColor(R.color.defaultcolor));
            } else {
                imgPlayer.setImageResource(R.drawable.audio_click_play);
                tvName.setTextColor(getContext().getResources().getColor(R.color.black_33));
            }
            imgPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrId()) || !(TextUtils.equals(data.getArticleid(), AudioPlayManager.getInstance().getCurrId()))) {
                        //当前点击的条目不在播放状态
                        AudioPlayManager.getInstance().init(getData(), getLayoutPosition(), AudioPlayManager.PlayModel.ORDER);
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                    } else {
                        switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                            case SystemConstant.ON_PREPARE:
                                AudioPlayManager.getInstance().release();
                                imgPlayer.setImageResource(R.drawable.audio_click_play);
                                tvName.setTextColor(getContext().getResources().getColor(R.color.black_33));
                                break;
                            case SystemConstant.ON_PAUSE:
                                AudioPlayManager.getInstance().ijkStart();
                                DemoApplication.isPlay = true;
                                imgPlayer.setImageResource(R.drawable.audio_click_stop);
                                tvName.setTextColor(getContext().getResources().getColor(R.color.defaultcolor));
                                break;
                            case SystemConstant.ON_PLAYING:
                                AudioPlayManager.getInstance().pause();
                                DemoApplication.isPlay = false;
                                imgPlayer.setImageResource(R.drawable.audio_click_play);
                                tvName.setTextColor(getContext().getResources().getColor(R.color.black_33));
                                break;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        private void download(AlbumContent data) {
            if (!Util.hintLogin((BaseActivity) getContext())) {
                return;
            }
            AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(data.getArticleid());
            if (data.getMediaurl() == null || TextUtils.isEmpty(data.getMediaurl()) || "null".equals(data.getMediaurl())) {
                Toast.makeText(getContext(), "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                return;
            }
            String suffixes = data.getMediaurl().substring(data.getMediaurl().lastIndexOf("."));
            //未下载过,开始下载
            if (albumContent == null) {
                try {
                    data.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + data.getArticlename() + suffixes);
                    DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(data);
                    DownloadInfo downloadInfo = DownloadManager.getInstance().startDownload(
                            SystemConstant.ACCESS_IMG_HOST + data.getMediaurl()
                            , data.getArticlename()
                            , Util.getSDCardPath() + "/MOZDownloads/" + data.getArticlename() + suffixes
                            , true
                            , false
                            , null
                            , data.getContentNum()
                            , data.getArticleid());
                    downloadInfo.setHolder(ViewHolder.this);
                    tvProgress.setVisibility(View.VISIBLE);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            } else {
                if (albumContent.getDownloadState() == 0) {
                    Toast.makeText(getContext(), "下载中...", Toast.LENGTH_SHORT).show();
                    //代表该内容已下载完成
                } else if (albumContent.getDownloadState() == 1) {
                    if (!Util.fileExisted(albumContent.getFilePath())) {
                        //本地不存在该内容，开始下载，同时将数据库的标识置为0，代表正在下载
                        data.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + data.getArticlename() + suffixes);
                        try {
                            DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(data.getArticleid(), 0, 1);
                            DownloadInfo downloadInfo = DownloadManager.getInstance().startDownload(
                                    SystemConstant.ACCESS_IMG_HOST + data.getMediaurl()
                                    , data.getArticlename()
                                    , Util.getSDCardPath() + "/MOZDownloads/" + data.getArticlename() + suffixes, true, false, null, data.getContentNum(), data.getArticleid());

                            downloadInfo.setHolder(ViewHolder.this);
                            tvProgress.setVisibility(View.VISIBLE);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        public void onItemViewClick(AlbumContent data) {
            super.onItemViewClick(data);
            int position = 0;
            for (int i = 0; i < getData().size(); i++) {
                if (TextUtils.equals(data.getArticleid(), getData().get(i).getArticleid())) {
                    position = i;
                    break;
                }
            }
            Intent intent = new Intent(getContext(), AudioAlbumActivity.class);
            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) getData());
            intent.putExtra("id", data.getArticleid());
            intent.putExtra("position", position);
            getContext().startActivity(intent);
        }

        public void setProgress(int progress) {
            tvProgress.setText(progress + "%");
        }

        public void onSuccess() {
            tvProgress.setVisibility(View.GONE);
            imgDownload.setImageResource(R.drawable.icon_audio_album_download_finish);
            imgDownload.setOnClickListener(null);
        }
    }
}
