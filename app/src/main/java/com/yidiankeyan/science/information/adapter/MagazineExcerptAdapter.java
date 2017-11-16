package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.acitivity.MagazineDetailsActivity;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/14 0014.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤    月刊详情
 * //       █▓▓▓▓██◤            -节选列表Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MagazineExcerptAdapter extends RecyclerAdapter<MagazineExcerptBean> {

    private Context mContext;

    public MagazineExcerptAdapter(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    public BaseViewHolder<MagazineExcerptBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    class ViewHolder extends BaseViewHolder<MagazineExcerptBean> {
        TextView tvLecturer;
        TextView tvTitleName;
        TextView tvTime;
        TextView tvSize;
        ImageView imgContentAvatar;
        ImageView imgPlayer;
        AutoFrameLayout flAudio;
        AutoRelativeLayout rlDownload;
        ImageView imgDownload;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_excerpt_magazine);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgContentAvatar = findViewById(R.id.img_content_avatar);
            tvTitleName = findViewById(R.id.tv_title_name);
            tvLecturer = findViewById(R.id.tv_lecturer);
            tvTime = findViewById(R.id.tv_time);
            tvSize = findViewById(R.id.tv_size);
            imgPlayer = findViewById(R.id.img_media_state);
            flAudio = findViewById(R.id.fl_audio);
            rlDownload = findViewById(R.id.rl_download);
            imgDownload = findViewById(R.id.img_download);
        }

        @Override
        public void setData(final MagazineExcerptBean object) {
            super.setData(object);

            Glide.with(getContext()).load(Util.getImgUrl(MagazineDetailsActivity.getInstance().detailsBean.getMonthlyDB().getCoverimg()))
                    .error(R.drawable.icon_readload_failed)
                    .placeholder(R.drawable.icon_readload_failed)
                    .into(imgContentAvatar);
            tvTitleName.setText(object.getName());
            tvLecturer.setText(object.getauthor());
            tvTime.setText(TimeUtils.formatTime(object.getCreateTime()));
            tvSize.setText(object.getSpace() + "M");
            object.setCoverimg(MagazineDetailsActivity.getInstance().detailsBean.getMonthlyDB().getCoverimg());
            object.setMonthlyName(MagazineDetailsActivity.getInstance().detailsBean.getMonthlyDB().getName());
            if (object.getDownloadState() == 1) {
                imgDownload.setImageResource(R.drawable.icon_magazine_by_dowcomplete);
            } else {
                imgDownload.setImageResource(R.drawable.icon_magazine_by_download);
            }

            if ((object.getId()).equals(AudioPlayManager.getInstance().getCurrId()) && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                    || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
                //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
                imgPlayer.setImageResource(R.drawable.audio_click_stop);
            } else {
                imgPlayer.setImageResource(R.drawable.audio_click_play);
            }

            flAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(object.getAudiourl()) && !TextUtils.equals("null", object.getAudiourl())) {
                        if (TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrId()) || !(object.getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
                            //当前点击的条目不在播放状态
                            int position = 0;
                            for (int i = 0; i < getData().size(); i++) {
                                if (TextUtils.equals(object.getId(), getData().get(i).getId())) {
                                    position = i;
                                }
                            }
                            AudioPlayManager.getInstance().init(getData(), position, AudioPlayManager.PlayModel.ORDER);
                            AudioPlayManager.getInstance().ijkStart();
                            DemoApplication.isPlay = true;
                            DemoApplication.isBuy = false;
                            imgPlayer.setImageResource(R.drawable.audio_click_stop);
                        } else {
                            switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                                case SystemConstant.ON_PREPARE:
                                    AudioPlayManager.getInstance().release();
                                    imgPlayer.setImageResource(R.drawable.audio_click_play);
                                    break;
                                case SystemConstant.ON_PAUSE:
                                    AudioPlayManager.getInstance().ijkStart();
                                    DemoApplication.isPlay = true;
                                    DemoApplication.isBuy = false;
                                    imgPlayer.setImageResource(R.drawable.audio_click_stop);
                                    break;
                                case SystemConstant.ON_PLAYING:
                                    AudioPlayManager.getInstance().pause();
                                    DemoApplication.isPlay = false;
                                    DemoApplication.isBuy = false;
                                    imgPlayer.setImageResource(R.drawable.audio_click_play);
                                    break;
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        ToastMaker.showShortToast("请先购买");
                    }

                }
            });

            MagazineExcerptBean magazineExcerptBean = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(object.getId());
            if (magazineExcerptBean != null && Util.fileExisted(magazineExcerptBean.getFilePath())) {
                imgDownload.setImageResource(R.drawable.icon_audio_album_download_finish);
            } else {
                imgDownload.setImageResource(R.drawable.icon_audio_album_download);
            }

            rlDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Util.hintLogin((BaseActivity) mContext))
                        return;
                    if (!TextUtils.isEmpty(object.getAudiourl()) && !TextUtils.equals("null", object.getAudiourl())) {
                        if (TextUtils.isEmpty(object.getAudiourl()) || "null".equals(object.getAudiourl())) {
                            Toast.makeText(DemoApplication.applicationContext, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //查询该内容是否下载过
                        MagazineExcerptBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(object.getId());
                        if (albumContent == null) {

                        }
                        String suffixes = object.getAudiourl().substring(object.getAudiourl().lastIndexOf("."));
                        //未下载过,开始下载
                        if (albumContent == null) {
                            Toast.makeText(DemoApplication.applicationContext, "开始下载", Toast.LENGTH_SHORT).show();
                            try {
                                object.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + object.getName() + suffixes);
                                DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(object);
                                DownloadManager.getInstance().startDownload(
                                        SystemConstant.ACCESS_IMG_HOST + object.getAudiourl()
                                        , object.getName()
                                        , Util.getSDCardPath() + "/MOZDownloads/" + object.getName() + suffixes
                                        , true
                                        , false
                                        , null
                                        , object.getId(), 4, 0);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (albumContent.getDownloadState() == 0) {
                                Toast.makeText(DemoApplication.applicationContext, "下载中...", Toast.LENGTH_SHORT).show();
                                //代表该内容已下载完成
                            } else if (albumContent.getDownloadState() == 1) {
                                //判断本地中是否存在该文件
                                if (Util.fileExisted(albumContent.getFilePath())) {
                                    Toast.makeText(DemoApplication.applicationContext, "该文件已下载", Toast.LENGTH_SHORT).show();
                                    imgDownload.setImageResource(R.drawable.icon_audio_album_download_finish);
                                } else {
                                    object.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + object.getName() + suffixes);
                                    try {
                                        DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(object.getId(), 0, 4);
                                        DownloadManager.getInstance().startDownload(
                                                SystemConstant.ACCESS_IMG_HOST + object.getAudiourl()
                                                , object.getName()
                                                , Util.getSDCardPath() + "/MOZDownloads/" + object.getName() + suffixes, true, false, null, object.getId(), 4, 0);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        }
                    } else {
                        ToastMaker.showShortToast("请先购买");
                    }
                }
            });
        }
    }

    private String getTime(long millisecond) {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - millisecond;
        long day = duration / (24 * 60 * 60 * 1000);
        if (day > 0) {
            return new SimpleDateFormat("MM-dd").format(new Date(millisecond));
        }
        long hour = duration / (60 * 60 * 1000);
        if (hour > 0)
            return hour + "小时前";
        long minute = duration / (60 * 1000);
        if (minute > 0)
            return minute + "分钟前";
        long second = duration / 1000;
        if (second == 0)
            return "1秒前";
        else
            return second + "秒前";
    }
}
