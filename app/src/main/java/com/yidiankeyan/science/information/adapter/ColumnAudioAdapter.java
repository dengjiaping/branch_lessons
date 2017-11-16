package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.service.MyLocalService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/14 0014.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤    专栏详情
 * //       █▓▓▓▓██◤            -音频列表Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class ColumnAudioAdapter extends RecyclerAdapter<ColumnAudioBean> {

    private Context mContext;
    private List<ColumnAudioBean> listData;

    public ColumnAudioAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<ColumnAudioBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    class ViewHolder extends BaseViewHolder<ColumnAudioBean> {
        TextView tvTitleName;
        TextView tvTime;
        TextView tvSize;
        //        ImageView imgContentAvatar;
        TextView tvState;
        View vLine;
        AutoFrameLayout flAudio;
        ImageView imgMediaState;
        TextView tvFree;
        ImageView imgDownload;
        AutoLinearLayout mAutoLinearLayout;
        AutoRelativeLayout mRlDownLoad;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_column_audio);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
//            imgContentAvatar = findViewById(R.id.img_content_avatar);
            tvTitleName = findViewById(R.id.tv_title_name);
            tvTime = findViewById(R.id.tv_time);
            tvSize = findViewById(R.id.tv_size);
            vLine = findViewById(R.id.v_line);
            mAutoLinearLayout = (AutoLinearLayout) findViewById(R.id.autoLinearLayout);
            flAudio = findViewById(R.id.fl_audio);
            tvFree = findViewById(R.id.tv_free);
            imgMediaState = findViewById(R.id.img_media_state);
            imgDownload = findViewById(R.id.img_download);
            mRlDownLoad = findViewById(R.id.rl_download);
        }

        @Override
        public void setData(final ColumnAudioBean object) {
            super.setData(object);
//            Glide.with(getContext()).load(Util.getImgUrl(object.getAudioImg()))
//                    .error(R.drawable.icon_readload_failed)
//                    .placeholder(R.drawable.icon_readload_failed)
//                    .into(imgContentAvatar);

            tvTitleName.setText(object.getAudioName());
            tvTime.setText("时长 " + TimeUtils.getInterviewTime(Integer.parseInt(object.getAudioLength())));
            if (!TextUtils.isEmpty(object.getCreateTime())) {
                tvSize.setText(object.getCreateTime());
            } else {
                tvSize.setText("");
            }

            if (TextUtils.equals("1", object.getHaveYouPurchased())) {
                tvFree.setVisibility(View.GONE);
            } else {
                if (TextUtils.equals("1", object.getFree())) {
                    tvFree.setVisibility(View.VISIBLE);
                } else {
                    tvFree.setVisibility(View.GONE);
                }
            }
            if (TextUtils.equals(object.getId(), AudioPlayManager.getInstance().getCurrId())
                    && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                    || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
                //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
                imgMediaState.setImageResource(R.drawable.icon_rec_recomm_stops);
                tvTitleName.setTextColor(getContext().getResources().getColor(R.color.defaultcolor));
            } else if (DemoApplication.mListAudioselect.contains(object.getId())) {
                tvTitleName.setTextColor(mContext.getResources().getColor(R.color.menu));//
                imgMediaState.setImageResource(R.drawable.icon_rec_recomm_plays);
            } else {
                imgMediaState.setImageResource(R.drawable.icon_rec_recomm_plays);
                tvTitleName.setTextColor(getContext().getResources().getColor(R.color.black_33));
            }
            IssuesDetailBean issues = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(object.getId());
            if (issues == null || !Util.fileExisted(issues.getFilePath())) {
                imgDownload.setImageResource(R.drawable.icon_audio_album_download);
                mRlDownLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        download(object);
                    }
                });
            } else {
                imgDownload.setImageResource(R.drawable.icon_audio_album_download_finish);
                mRlDownLoad.setOnClickListener(null);
            }
            mAutoLinearLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.equals("1", object.getHaveYouPurchased()) || TextUtils.equals("1", object.getFree())) {
                                if (TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrId()) || !(object.getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
                                    //当前点击的条目不在播放状态
                                     listData = new ArrayList<ColumnAudioBean>();
                                    for (int x = 0; x < getData().size(); x++) {
                                        String free = getData().get(x).getFree();
                                        String haveYouPurchased = getData().get(x).getHaveYouPurchased();
                                        if (free.equals("1") || "1".equals(haveYouPurchased)) {
                                            listData.add(getData().get(x));
                                        }
                                    }
                                    DemoApplication.mListAudioselect.add(object.getId());
//                                    AudioPlayManager.getInstance().init(listData, getLayoutPosition(), AudioPlayManager.PlayModel.ORDER);
//                                    AudioPlayManager.getInstance().ijkStart();
                                    Intent intent = new Intent(mContext, MyLocalService.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("mData", (ArrayList<ColumnAudioBean>) listData);
                                    bundle.putInt("position", getLayoutPosition());
                                    bundle.putParcelableArrayList("mDatas", null);
                                    intent.putExtras(bundle);
                                    mContext.startService(intent);
                                    DemoApplication.isPlay = true;
                                } else {
                                    switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                                        case SystemConstant.ON_PREPARE:
                                            AudioPlayManager.getInstance().release();
                                            imgMediaState.setImageResource(R.drawable.icon_rec_recomm_plays);
                                            tvTitleName.setTextColor(getContext().getResources().getColor(R.color.black_33));
                                            break;
                                        case SystemConstant.ON_PAUSE:
                                            AudioPlayManager.getInstance().ijkStart();
                                            DemoApplication.isPlay = true;
                                            imgMediaState.setImageResource(R.drawable.icon_rec_recomm_stops);
                                            tvTitleName.setTextColor(getContext().getResources().getColor(R.color.defaultcolor));
                                            break;
                                        case SystemConstant.ON_PLAYING:
                                            AudioPlayManager.getInstance().pause();
                                            DemoApplication.isPlay = false;
                                            imgMediaState.setImageResource(R.drawable.icon_rec_recomm_plays);
                                            tvTitleName.setTextColor(getContext().getResources().getColor(R.color.black_33));
                                            break;
                                    }
                                }
                                notifyDataSetChanged();
                            } else {
                                ToastMaker.showShortToast("请先购买");
                            }
                        }
                    }

            );

        }

        private void download(ColumnAudioBean data) {
            if (!Util.hintLogin((BaseActivity) getContext())) {
                return;
            }
            IssuesDetailBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(data.getId());
            if (data.getAudioUrl() == null || TextUtils.isEmpty(data.getAudioUrl()) || "null".equals(data.getAudioUrl())) {
                if (TextUtils.equals("1", data.getHaveYouPurchased()) || TextUtils.equals("0", data.getFree())) {
                    Toast.makeText(getContext(), "请先购买", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            String suffixes = data.getAudioUrl().substring(data.getAudioUrl().lastIndexOf("."));
            //未下载过,开始下载
            if (albumContent == null) {
                try {
                    data.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + data.getAudioName() + suffixes);
                    DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(data);
                    DownloadManager.getInstance().startDownload(
                            Util.getImgUrl(data.getAudioUrl())
                            , data.getAudioName()
                            , Util.getSDCardPath() + "/MOZDownloads/" + data.getAudioName() + suffixes
                            , true
                            , false
                            , null
                            , data.getId(), 2, 0);
                    Toast.makeText(DemoApplication.applicationContext, "下载中...", Toast.LENGTH_SHORT).show();
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
                        data.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + data.getAudioName() + suffixes);
                        try {
                            DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(data.getId(), 0, 4);
                            DownloadManager.getInstance().startDownload(
                                    Util.getImgUrl(data.getAudioUrl())
                                    , data.getAudioName()
                                    , Util.getSDCardPath() + "/MOZDownloads/" + data.getAudioName() + suffixes, true, false, null, data.getId(), 2, 0);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

}
