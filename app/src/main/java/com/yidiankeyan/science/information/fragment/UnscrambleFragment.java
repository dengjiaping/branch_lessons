package com.yidiankeyan.science.information.fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 杂志详情
 * -解读
 */
public class UnscrambleFragment extends Fragment implements View.OnClickListener {

    private AutoFrameLayout flAudio;
    private ImageView imgMediaState;
    private TextView tvTitleName;
    private TextView tvLecturer;
    private TextView tvTime;
    private TextView tvSize;
    private ImageView imgContentAvatar;
    private ImageView imgDownload;
    private AutoRelativeLayout rlDisplayAll;
    private MonthlyDetailsBean detailsBean;
    private int priceInsufficient;

    private PopupWindow tipstPopupwindow;
    private View llUnscramble;

    private WebView wvDecs;
    private String id;

    private String imgAudio;
    private NestedScrollView scrollView;

    public void setId(String id, String imgAudio) {
        this.id = id;
        this.imgAudio = imgAudio;
        if (TextUtils.equals(id, AudioPlayManager.getInstance().getCurrId()) && AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING) {
            imgMediaState.setImageResource(R.drawable.audio_click_stop);
        }
        toHttpMagazineDetails();
    }

    public UnscrambleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_unscramble, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void initAction() {
        flAudio.setOnClickListener(this);
        imgDownload.setOnClickListener(this);
        rlDisplayAll.setOnClickListener(this);
    }

    private void initView(View view) {
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);
        flAudio = (AutoFrameLayout) view.findViewById(R.id.fl_audio);
        imgMediaState = (ImageView) view.findViewById(R.id.img_media_state);
        tvTitleName = (TextView) view.findViewById(R.id.tv_title_name);
        tvLecturer = (TextView) view.findViewById(R.id.tv_lecturer);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        rlDisplayAll = (AutoRelativeLayout) view.findViewById(R.id.rl_display_all);
        imgDownload = (ImageView) view.findViewById(R.id.img_download);
        imgContentAvatar = (ImageView) view.findViewById(R.id.img_content_avatar);
        llUnscramble = view.findViewById(R.id.ll_unscramble);
        wvDecs = (WebView) view.findViewById(R.id.wv_decs);
    }


    private void toHttpMagazineDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        ApiServerManager.getInstance().getApiServer().GetMonthlyDetails(map)
                .enqueue(new RetrofitCallBack<MonthlyDetailsBean>() {
                             @Override
                             public void onSuccess(Call<RetrofitResult<MonthlyDetailsBean>> call, Response<RetrofitResult<MonthlyDetailsBean>> response) {
                                 if (response.body().getCode() == 200) {
                                     detailsBean = response.body().getData();
                                     if (detailsBean.getPermission() > 0) {
                                         rlDisplayAll.setVisibility(View.GONE);
                                     } else {
                                         rlDisplayAll.setVisibility(View.VISIBLE);
                                     }
//                                     tvDecs.setText(detailsBean.getMonthlyDB().getContent());
                                     wvDecs.loadDataWithBaseURL(SystemConstant.MYURL, detailsBean.getMonthlyDB().getContent(), "text/html", "utf-8", null);
                                     Glide.with(getActivity()).load(Util.getImgUrl(imgAudio)).placeholder(R.drawable.icon_readload_failed)
                                             .error(R.drawable.icon_readload_failed).into(imgContentAvatar);
                                     tvTitleName.setText("解读 " + detailsBean.getMonthlyDB().getName());
                                     tvLecturer.setText(detailsBean.getMonthlyDB().getAuthor());
                                     tvTime.setText(TimeUtils.formatDateTime(detailsBean.getMonthlyDB().getCreatetime()));
                                     tvSize.setText("大小 " + detailsBean.getMonthlyDB().getSpace() + "M");
                                     MagazineExcerptBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(detailsBean.getMonthlyDB().getId());
                                     if (albumContent == null) {
                                         imgDownload.setImageResource(R.drawable.icon_magazine_by_download);
                                         return;
                                     }
                                     if (albumContent.getDownloadState() == 1) {
                                         imgDownload.setImageResource(R.drawable.icon_magazine_by_dowcomplete);
                                     } else {
                                         imgDownload.setImageResource(R.drawable.icon_magazine_by_download);
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<RetrofitResult<MonthlyDetailsBean>> call, Throwable
                                     t) {

                             }
                         }

                );
    }


//    private void toHttpPostBalance() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
//        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
//                if (response.body().getCode() == 200) {
//                    if (response.body().getData().getBalance() < detailsBean.getMonthlyDB().getPrice()) {
//                        priceInsufficient = 1;
//                    } else {
//                        priceInsufficient = 2;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_audio:
                if (detailsBean.getPermission() == 0) {
                    ToastMaker.showShortToast("请先购买");
                } else if (TextUtils.isEmpty(detailsBean.getMonthlyDB().getAudiourl()) || "null".equals(detailsBean.getMonthlyDB().getAudiourl())) {
                    ToastMaker.showShortToast("该文件有误");
                } else {
                    if (TextUtils.equals(id, AudioPlayManager.getInstance().getCurrId())) {
                        switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                            case SystemConstant.ON_PREPARE:
                                //音频播放器在准备过程中
                                Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
                                break;
                            case SystemConstant.ON_PAUSE:
                                //音频播放器暂停中，开始播放
                                AudioPlayManager.getInstance().ijkStart();
                                DemoApplication.isBuy = false;
                                DemoApplication.isPlay = true;
                                break;
                            case SystemConstant.ON_PLAYING:
                                //暂停
                                AudioPlayManager.getInstance().pause();
                                DemoApplication.isPlay = false;
                                DemoApplication.isBuy = false;
                                break;
                            default:
                                //音频播放器处于停止中，重新开始播放
                                initAudio();
                                AudioPlayManager.getInstance().ijkStart();
                                DemoApplication.isBuy = false;
                                DemoApplication.isPlay = true;
                                break;
                        }
                    } else {
                        initAudio();
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        DemoApplication.isBuy = false;
                    }
                }
                break;
            case R.id.img_download:
                if (detailsBean.getPermission() == 0) {
                    ToastMaker.showShortToast("请先购买");
                    break;
                }
                if (TextUtils.isEmpty(detailsBean.getMonthlyDB().getAudiourl()) || "null".equals(detailsBean.getMonthlyDB().getAudiourl())) {
                    Toast.makeText(DemoApplication.applicationContext, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                    break;
                }
                //查询该内容是否下载过
                MagazineExcerptBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(detailsBean.getMonthlyDB().getId());
                String suffixes = detailsBean.getMonthlyDB().getAudiourl().substring(detailsBean.getMonthlyDB().getAudiourl().lastIndexOf("."));
                //未下载过,开始下载
                if (albumContent == null) {
                    Toast.makeText(DemoApplication.applicationContext, "开始下载", Toast.LENGTH_SHORT).show();
                    try {
                        detailsBean.getMonthlyDB().setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getMonthlyDB().getName() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(detailsBean.getMonthlyDB());
                        DownloadManager.getInstance().startDownload(
                                SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getAudiourl()
                                , detailsBean.getMonthlyDB().getName()
                                , Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getMonthlyDB().getName() + suffixes
                                , true
                                , false
                                , null
                                , detailsBean.getMonthlyDB().getId(), 4, 0);
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
                            detailsBean.getMonthlyDB().setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getMonthlyDB().getName() + suffixes);
                            try {
                                DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(detailsBean.getMonthlyDB().getId(), 0, 4);
                                DownloadManager.getInstance().startDownload(
                                        SystemConstant.ACCESS_IMG_HOST + detailsBean.getMonthlyDB().getAudiourl()
                                        , detailsBean.getMonthlyDB().getName()
                                        , Util.getSDCardPath() + "/MOZDownloads/" + detailsBean.getMonthlyDB().getName() + suffixes, true, false, null, detailsBean.getMonthlyDB().getId(), 4, 0);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case R.id.rl_display_all:
                if (detailsBean == null)
                    return;
                if (!Util.hintLogin((BaseActivity) getActivity()))
                    return;
                if (detailsBean.getPermission() > 0) {
                    toHttpMagazineDetails();
                } else {
                    showReportPop();
                }
                break;
        }
    }


    /**
     * 弹出购买提示
     */
    private void showReportPop() {
        if (tipstPopupwindow == null) {
            View view = View.inflate(getContext(), R.layout.popupwindow_purchase_tips, null);
            tipstPopupwindow = new PopupWindow(view, -2, -2);
            tipstPopupwindow.setContentView(view);
            tipstPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            tipstPopupwindow.setFocusable(true);
            tipstPopupwindow.setBackgroundDrawable(new BitmapDrawable());
            tipstPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(getActivity(), tipstPopupwindow);
                }
            });
            //购买
            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("action.unscramble.shopping");
                    getActivity().sendBroadcast(intent);
                    Util.finishPop(getActivity(), tipstPopupwindow);
                }
            });
            //取消
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(getActivity(), tipstPopupwindow);
                }
            });
            tipstPopupwindow.showAtLocation(llUnscramble, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            tipstPopupwindow.showAtLocation(llUnscramble, Gravity.CENTER, 0, 0);
        }
    }


    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_PREPARE:
                //正在准备
                break;
            case SystemConstant.ON_PLAYING:
                Log.e("state", "ON_PLAYING");
                //开始播放
                imgMediaState.setImageResource(R.drawable.audio_click_stop);
                break;
            case SystemConstant.ON_BUFFERING:
                //缓冲区变化
                break;
            case SystemConstant.ON_STOP:
                Log.e("state", "ON_STOP");
                //停止播放
                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.ON_PAUSE:
                Log.e("state", "ON_PAUSE");
                //暂停播放
                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.AUDIO_COMPLET:
//                AudioPlayManager.getInstance().replay();
//                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.ON_MAGAZINE_LOGINE_NOTIFY:
                //登录后刷新fragment
                toHttpMagazineDetails();
                break;
            case SystemConstant.ON_USER_INFORMATION:
                //登录后刷新fragment
                toHttpMagazineDetails();
                break;
        }
    }


    /**
     * 初始化音频，如果已下载播放本地，未下载播放网络音频
     */
    private void initAudio() {
        List<MonthlyDetailsBean.MonthlyDBBean> list = new ArrayList<>();
        list.add(detailsBean.getMonthlyDB());
        AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imgMediaState.setImageResource(R.drawable.audio_click_play);
        EventBus.getDefault().unregister(this);
    }

    public void goTop() {
        scrollView.smoothScrollTo(0, 0);
    }
}
