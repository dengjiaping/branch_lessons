package com.yidiankeyan.science.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.service.MyLocalService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nby on 2016/7/7.
 * 今日得到每一天查看的历史记录的adapter
 */
public class TodayHistoryListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<OneDayArticles> mDatas = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private LinearLayout llAll;
    /**
     * 代表控制器的位置
     */
    private int selectPosition = -1;
    private ListView listView;
    /**
     * 正在播放的位置
     */
//    private int playPosition = -1;
    private AutoLinearLayout llShareWx;
    private AutoLinearLayout llShareFriendCircle;
    private AutoLinearLayout llShareQq;
    private CheckBox imgReport;

    public TodayHistoryListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<OneDayArticles> mDatas) {
        this.mDatas.removeAll(this.mDatas);
        if (mDatas != null)
            this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

//    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            int position = (int) seekBar.getTag();
//            if (fromUser) {
//                //因为不止一个控制器，所以为控制器设置tag，代表当前改变的seekbar为正在播放的seekbar则改变播放进度
//                if (selectPosition == position) {
//                    AudioPlayManager.getInstance(mContext).mediaPlayer.seekTo(progress);
//                }
//            }
//        }

//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            //触摸时暂停播放
//            if (AudioPlayManager.getInstance(mContext).mediaPlayer != null)
//                AudioPlayManager.getInstance(mContext).mediaPlayer.pause();
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            //停止触摸时，判断当前触摸的seekbar为正在播放的控制器的seekbar则继续播放，不是的话停止正在播放的数据，播放现在触摸的数据
//            if (((int) seekBar.getTag()) == selectPosition) {
//                AudioPlayManager.getInstance(mContext).start();
//            } else {
//                View view = listView.getChildAt(((int) seekBar.getTag()) - listView.getFirstVisiblePosition());
//                final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
//                if (selectPosition >= 0) {
//                    //设置当前播放的数据为正常状态
//                    mDatas.get(selectPosition).setPlayState(0);
//                }
//                AudioPlayManager.getInstance(mContext).stop();
//                //播放选择的数据
//                AudioPlayManager.getInstance(mContext).mediaPlayer = MediaPlayer.create(mContext, mDatas.get((int) seekBar.getTag()).getVoiceRes());
//                seekBar.setMax(AudioPlayManager.getInstance(mContext).mediaPlayer.getDuration());
//                AudioPlayManager.getInstance(mContext).start();
//                mDatas.get((int) seekBar.getTag()).setPlayState(1);
//                selectPosition = (int) seekBar.getTag();
//                notifyDataSetChanged();
//            }
//            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
//        }
//    };

//    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            //如果当前播放与下一首日期相同则播放下一首
//            if (mDatas.get(playPosition).getDate().equals(mDatas.get(playPosition + 1).getDate())) {
//                //播放的位置+1
//                playPosition++;
//                AudioPlayManager.getInstance(mContext).setPlayPosition(playPosition);
//                AudioPlayManager.getInstance(mContext).stop();
//                AudioPlayManager.getInstance(mContext).mediaPlayer = MediaPlayer.create(mContext, mDatas.get(playPosition).getVoiceRes());
//                if (selectPosition >= listView.getFirstVisiblePosition() && selectPosition <= listView.getLastVisiblePosition()) {
//                    View view = listView.getChildAt(selectPosition - listView.getFirstVisiblePosition());
//                    SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
//                    TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
//                    seekBar.setMax(AudioPlayManager.getInstance(mContext).mediaPlayer.getDuration());
//                    textView.setText(Util.getTimeFromInt(AudioPlayManager.getInstance(mContext).mediaPlayer.getDuration()));
//                }
//                AudioPlayManager.getInstance(mContext).mediaPlayer.start();
//                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
//                AudioPlayManager.getInstance(mContext).mediaPlayer.setOnCompletionListener(this);
//                Toast.makeText(mContext, playPosition + "", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    @Override
    public OneDayArticles getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    /**
     * 更新seekbar进度以及剩余时间
     */
//    public void updataProgress() {
//        try {
//            if (AudioPlayManager.getInstance(mContext).mediaPlayer != null && AudioPlayManager.getInstance(mContext).mediaPlayer.isPlaying() && listView != null) {
//                if (selectPosition >= listView.getFirstVisiblePosition() && selectPosition <= listView.getLastVisiblePosition()) {
//                    View view = listView.getChildAt(selectPosition - listView.getFirstVisiblePosition());
//                    SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
//                    final TextView textView = (TextView) view.findViewById(R.id.textView1_total_time);
//                    seekBar.setProgress(AudioPlayManager.getInstance(mContext).mediaPlayer.getCurrentPosition());
//                    textView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText(Util.getTimeFromInt(AudioPlayManager.getInstance(mContext).mediaPlayer.getDuration() - AudioPlayManager.getInstance(mContext).mediaPlayer.getCurrentPosition()));
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_today_history_list, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
            holder.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
            holder.imgShowPop = (ImageView) convertView.findViewById(R.id.img_show_pop);
            holder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            holder.textView1CurrTime = (TextView) convertView.findViewById(R.id.textView1_curr_time);
            holder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tv_praise_num);
//            holder.seekBar1 = (SeekBar) convertView.findViewById(R.id.seekBar1);
//            holder.textView1TotalTime = (TextView) convertView.findViewById(R.id.textView1_total_time);
//            holder.mediaPlay = (ImageButton) convertView.findViewById(R.id.media_play);
            holder.tvCategoryTwo = (TextView) convertView.findViewById(R.id.tv_category_two);
            holder.imgTodayCor = (ImageView) convertView.findViewById(R.id.img_today_cor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(mDatas.get(position).getTitle());
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).placeholder(R.drawable.icon_readload_failed)
                .error(R.drawable.icon_readload_failed).into(holder.imgTodayCor);
//        holder.tvTime.setText(mDatas.get(position).getDuration());
        if (mDatas.get(position).getSubjects() != null && mDatas.get(position).getSubjects().size() > 0)
            holder.tvCategory.setText(mDatas.get(position).getSubjects().get(0).getName());
        if (mDatas.get(position).getSubjects() != null && mDatas.get(position).getSubjects().size() > 1) {
            holder.tvCategoryTwo.setText(mDatas.get(position).getSubjects().get(1).getName());
        }
        holder.tvPraiseNum.setText(TimeUtils.getMagazineLength((int) mDatas.get(position).getTimelength()));
        holder.tvReadCount.setText("主播:" + mDatas.get(position).getContentauthor());
        holder.imgShowPop.setTag(position);
        holder.imgShowPop.setOnClickListener(this);
        String mediaId = AudioPlayManager.getInstance().getCurrId();
        if (mDatas.get(position).getId().equals(mediaId)) {
            holder.tvTitle.setTextColor(Color.parseColor("#F1312E"));
        } else {
            holder.tvTitle.setTextColor(Color.BLACK);
        }
        if ((mDatas.get(position).getId()).equals(AudioPlayManager.getInstance().getCurrId()) && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
            //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.defaultcolor));
        } else {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.black_33));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AudioPlayManager.getInstance().isInited() || !(mDatas.get(position).getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
                    if (!DB.getInstance(DemoApplication.applicationContext).newsIsLooked(mDatas.get(position).getId())) {
                        DB.getInstance(DemoApplication.applicationContext).updataNews(mDatas.get(position).getId());
                        mDatas.get(position).setReadnum(mDatas.get(position).getReadnum() + 1);
                        toHttpRead(mDatas.get(position).getId());
                    }
//                    //开始播放
//                    AudioPlayManager.getInstance().init(mDatas, position, AudioPlayManager.PlayModel.ORDER);
//                    AudioPlayManager.getInstance().ijkStart();
                    Intent intent = new Intent(mContext, MyLocalService.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putParcelableArrayList("mData", null);
                    bundle.putParcelableArrayList("mDatas", (ArrayList<OneDayArticles>) mDatas);
                    intent.putExtras(bundle);
                    mContext.startService(intent);
                    DemoApplication.isPlay = true;
                    DemoApplication.isBuy = true;
                } else {
                    switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                        case SystemConstant.ON_PREPARE:
                            //停止
                            AudioPlayManager.getInstance().release();
                            break;
                        case SystemConstant.ON_PAUSE:
                            //恢复播放
                            AudioPlayManager.getInstance().resume();
                            DemoApplication.isBuy = true;
                            DemoApplication.isPlay = true;
                            break;
                        case SystemConstant.ON_PLAYING:
                            //暂停
                            AudioPlayManager.getInstance().pause();
                            DemoApplication.isBuy = true;
                    }
                }

                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    /**
     * 日课阅读量加1
     *
     * @param id 日课id
     */
    private void toHttpRead(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", SpUtils.getStringSp(mContext, "userId"));
        map.put("dailyarticleid", id);
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.READ_TODAY_AUDIO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.img_show_pop:
                if (mPopupWindow == null) {
                    View view = View.inflate(mContext, R.layout.popupwindow_news_flash_share, null);
                    llShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
                    llShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
                    llShareQq = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//                    imgReport = (CheckBox) view.findViewById(R.id.img_report);

//                    llShareQq = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);

                    mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setContentView(view);
                    mPopupWindow.setAnimationStyle(R.style.AnimBottom);
                    WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    ((Activity) mContext).getWindow().setAttributes(lp);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            Util.finishPop((Activity) mContext, mPopupWindow);
                        }
                    });
                    view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.finishPop((Activity) mContext, mPopupWindow);
                        }
                    });
                } else {
                    WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    ((Activity) mContext).getWindow().setAttributes(lp);
                    mPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
                }
                llShareQq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s;
                        if (mDatas.get(position).getAudiourl() != null && mDatas.get(position).getAudiourl().startsWith("/"))
                            s = SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getAudiourl();
                        else
                            s = SystemConstant.ACCESS_IMG_HOST + "/" + mDatas.get(position).getAudiourl();
                        UMusic music = new UMusic(s);
                        music.setTitle(mDatas.get(position).getTitle());//音乐的标题
                        music.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));//音乐的缩略图
                        music.setDescription(mDatas.get(position).getTitle());//音乐的描述
                        new ShareAction((Activity) mContext)
                                .setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).withText(mDatas.get(position).getTitle())
                                .withMedia(music).withTargetUrl(SystemConstant.MYURL + "view/appshare/courseShare.jsp?courseid=" + mDatas.get(position).getId()).share();
                    }
                });
//                llShareSina.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                        String url = "http://192.168.1.197/cmsweb/article/info/1ce2fe04b4b544dbafa6ff124dd29996";
//                        new ShareAction((TodayAchievementActivity) mContext).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                                .withTitle("赛思测试分享")
//                                .withMedia(image)
//                                .withTargetUrl(url)
//                                .share();
//                    }
//
//                });

                llShareWx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s;
                        if (mDatas.get(position).getAudiourl() != null && mDatas.get(position).getAudiourl().startsWith("/"))
                            s = SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getAudiourl();
                        else
                            s = SystemConstant.ACCESS_IMG_HOST + "/" + mDatas.get(position).getAudiourl();
                        UMusic music = new UMusic(s);
                        music.setTitle(mDatas.get(position).getTitle());//音乐的标题
                        music.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));//音乐的缩略图
                        music.setDescription(mDatas.get(position).getTitle());//音乐的描述
                        new ShareAction((Activity) mContext)
                                .setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).withText(mDatas.get(position).getTitle())
                                .withTargetUrl(SystemConstant.MYURL + "view/appshare/courseShare.jsp?courseid=" + mDatas.get(position).getId())
                                .withMedia(music).share();
                    }
                });
                llShareFriendCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s;
                        if (mDatas.get(position).getAudiourl() != null && mDatas.get(position).getAudiourl().startsWith("/"))
                            s = SystemConstant.ACCESS_IMG_HOST + mDatas.get(position).getAudiourl();
                        else
                            s = SystemConstant.ACCESS_IMG_HOST + "/" + mDatas.get(position).getAudiourl();
                        UMusic music = new UMusic(s);
                        music.setTitle(mDatas.get(position).getTitle());//音乐的标题
                        music.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));//音乐的缩略图
                        music.setDescription(mDatas.get(position).getTitle());//音乐的描述
                        new ShareAction((Activity) mContext)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).withText(mDatas.get(position).getTitle())
                                .withTargetUrl(SystemConstant.MYURL + "view/appshare/courseShare.jsp?courseid=" + mDatas.get(position).getId())
                                .withMedia(music).share();
                    }
                });
//                imgReport.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Util.finishPop((Activity) mContext, mPopupWindow);
//                        mContext.startActivity(new Intent(mContext, RecommendWebActivity.class).putExtra("url", SystemConstant.MYURL + "cmsweb/course/courseinfo/" + mDatas.get(position).getId()));
//                    }
//                });
                break;
        }
    }

    public LinearLayout getLlAll() {
        return llAll;
    }

    public void setLlAll(LinearLayout llAll) {
        this.llAll = llAll;
    }

    class ViewHolder {
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvCategory;
        private TextView tvReadCount;
        private ImageView imgShowPop;
        private TextView tvPraiseNum;
        private LinearLayout linearLayout2;
        private TextView textView1CurrTime;
        //        private SeekBar seekBar1;
//        private TextView textView1TotalTime;
//        private ImageButton mediaPlay;
        private TextView tvCategoryTwo;
        private ImageView imgTodayCor;
    }

    ;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogUtils.d("throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
