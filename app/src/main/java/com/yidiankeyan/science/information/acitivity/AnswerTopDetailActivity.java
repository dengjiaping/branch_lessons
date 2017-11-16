package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.AnswerTopDetailAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.information.entity.EavedropMediaBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.my.activity.AccountBalanceActivity;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.drag.view.OtherGridView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 某个人的科答详情
 */
public class AnswerTopDetailActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private ImageButton titleBtn;
    private ListViewFinalLoadMore lvAnswerTop;
    private AnswerTopDetailAdapter adapter;
    private View headView;
    private ImageView imgAvatar;
    private TextView tvJob;
    private EditText etContent;
    private TextView tvSendRequest;
    private int selectionPosition = 0;
    private AutoLinearLayout llContainer;
    private TextView tvAttachedFive;
    private TextView tvAttachedTen;
    private TextView tvAttachedFifty;
    private TextView tvAttachedOneHundred;
    //    private TextView tvAttachedMoney;
    private int attachedMoney = 0;

    private TextView tvThroat;
    private double num = 0;
    private String numbers;
    //关注
    private TextView tvFollow;
    private boolean isFu;
    private PopupWindow mPopupWindow;
    private PopupWindow mShowPopupWindow;
    private ImageView imgExit;
    private TextView tvAssign;
    private EditText etCustomPrice;
    private AutoLinearLayout llAnswer;
    private TextView tvFinish, tvYesClick;
    private TextView tvName;
    private AnswerPeopleDetail answerPeopleDetail;
    private TextView tvAttention;
    private AutoLinearLayout llAudioCommit, llDetailExpand;
    private TextView tvAudioLength;
    private TextView tvTotalMoney;
    private TextView tvFansNumber;
    private ImageView imgMoreDoman;
    private boolean flag;
    private TextView tvFullText;
    private AutoRelativeLayout rlFullText;
    private OtherGridView gvDomans;
    private List<AnswerAlbumBean> domanList = new ArrayList<>();
    private DomanAdapter domanAdapter;
    private List<String> clickList = new ArrayList<>();
    private List<NewScienceHelp> mDatas = new ArrayList<>();
    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;
    private String UUID;
    private String catId;
    private boolean isSubmitQuestion;
    private NewScienceHelp keda;
    private List<AnswerAlbumBean> allDomanList = new ArrayList<>();
    private ImageView imgMessage;
    private TextView tvSign;
    private TextView tvCurrQuestion;
    private long startTime;
    private long endTime;
    private int pages = 1;
    private int priceInsufficient = 0;

    @Override
    protected int setContentView() {
        return R.layout.activity_answer_top_detail;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        lvAnswerTop = (ListViewFinalLoadMore) findViewById(R.id.lv_answer_top_detail);
        headView = LayoutInflater.from(mContext).inflate(R.layout.head_answer_top_detail, null, false);
        imgAvatar = (ImageView) headView.findViewById(R.id.img_avatar);
        tvJob = (TextView) headView.findViewById(R.id.tv_job);
        tvAttention = (TextView) headView.findViewById(R.id.tv_attention);
        llAudioCommit = (AutoLinearLayout) headView.findViewById(R.id.ll_audio_commit);
        llDetailExpand = (AutoLinearLayout) headView.findViewById(R.id.ll_detail_expand);
        tvAudioLength = (TextView) headView.findViewById(R.id.tv_audio_length);
        tvTotalMoney = (TextView) headView.findViewById(R.id.tv_total_money);
        tvSign = (TextView) headView.findViewById(R.id.tv_sign);
        imgMoreDoman = (ImageView) headView.findViewById(R.id.img_more_doman);
        etContent = (EditText) headView.findViewById(R.id.et_content);
        tvSendRequest = (TextView) headView.findViewById(R.id.tv_send_request);
        llContainer = (AutoLinearLayout) headView.findViewById(R.id.ll_container);
        imgMessage = (ImageView) headView.findViewById(R.id.img_message);
        tvThroat = (TextView) headView.findViewById(R.id.tv_throat_money);
        tvAttachedFive = (TextView) headView.findViewById(R.id.tv_five_rmb);
        gvDomans = (OtherGridView) headView.findViewById(R.id.gv_domans);
        tvAttachedTen = (TextView) headView.findViewById(R.id.tv_ten_rmb);
        tvAttachedFifty = (TextView) headView.findViewById(R.id.tv_fifty_rmb);
        tvAttachedOneHundred = (TextView) headView.findViewById(R.id.tv_custom_rmb);
//        tvAttachedMoney = (TextView) headView.findViewById(R.id.tv_attached_money);
        tvFollow = (TextView) headView.findViewById(R.id.txt_follow);
        llAnswer = (AutoLinearLayout) findViewById(R.id.ll_answer);
        tvFansNumber = (TextView) headView.findViewById(R.id.tv_fans_number);
        tvName = (TextView) headView.findViewById(R.id.tv_name);
        tvCurrQuestion = (TextView) headView.findViewById(R.id.tv_curr_question);
        tvFullText = (TextView) headView.findViewById(R.id.tv_full_text);
        rlFullText = (AutoRelativeLayout) headView.findViewById(R.id.rl_full_text);
    }

    @Override
    protected void initAction() {
        titleBtn.setVisibility(View.GONE);
        lvAnswerTop.addHeaderView(headView);
        adapter = new AnswerTopDetailAdapter(this, mDatas);
        lvAnswerTop.setAdapter(adapter);
        domanAdapter = new DomanAdapter();
        gvDomans.setAdapter(domanAdapter);
//        llDetailExpand.setOnClickListener(this);
//        llDetailExpand.setTag(0);
        gvDomans.setVisibility(View.VISIBLE);
        llDetailExpand.setTag(1);
        RotateAnimation expandAnim;
        if (System.currentTimeMillis() - startTime > 360)
            expandAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        else
            expandAnim = new RotateAnimation((System.currentTimeMillis() - startTime) / 4, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expandAnim.setDuration(360);
        expandAnim.setFillAfter(true);
        startTime = System.currentTimeMillis();
        imgMoreDoman.startAnimation(expandAnim);

        llReturn.setOnClickListener(this);
        numbers = (String) tvThroat.getText();
        llAudioCommit.setEnabled(false);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && selectionPosition >= 0) {
                    tvSendRequest.setEnabled(true);
                    tvSendRequest.setTextColor(Color.parseColor("#F1312E"));
                } else {
                    tvSendRequest.setEnabled(false);
                    tvSendRequest.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if (s.length() > 80) {
                    String s1 = s.toString().substring(0, 80);
                    etContent.setText(s1);
                    etContent.setSelection(s1.length());
                } else {
                    tvCurrQuestion.setText(s.length() + "/80");
                }
            }
        });
        gvDomans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < domanList.size(); i++) {
                    if (i == position) {
                        domanList.get(i).setSelected(true);
                    } else {
                        domanList.get(i).setSelected(false);
                    }
                }
                domanAdapter.notifyDataSetChanged();
            }
        });
        adapter.setOnEavesdropClick(new AnswerTopDetailAdapter.OnEavesdropClick() {
            @Override
            public void onClick(NewScienceHelp keda) {
                if (!Util.hintLogin((BaseActivity) mContext))
                    return;
                AnswerTopDetailActivity.this.keda = keda;
                isSubmitQuestion = false;
                toHttpEavedrop();
            }
        });
        toHttpGetDetail();
        toHttpGetAnswerList();
        lvAnswerTop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetAnswerList();
            }
        });
    }

    /**
     * 获取资源
     */
    public void toHttpEavedrop() {
        long surplusTime = (keda.getKederDB().getAnswertime() + 1 * 60 * 60 * 1000 - System.currentTimeMillis()) / (1000 * 60);
        if (surplusTime < 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("kederid", keda.getKederDB().getId());
            HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_EAVEDROP_MEDIA, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (result.getCode() == 200) {
                        EavedropMediaBean eavedrop = (EavedropMediaBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), EavedropMediaBean.class);
                        if (eavedrop.getAudiourl() != null && eavedrop.getAudiourl().startsWith("/"))
                            AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + eavedrop.getAudiourl());
                        else
                            AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + eavedrop.getAudiourl());
                        AudioPlayManager.getInstance().setmMediaPlayId(keda.getKederDB().getId());
                        adapter.updatePermission(keda.getKederDB().getId());
                        adapter.notifyDataSetChanged();
                    } else if (result.getCode() == 101) {
                        Map<String, Object> map = new HashMap<>();
                        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                            map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                        }else map.put("id", "");
                        ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                            @Override
                            public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                                if (response.body().getCode() == 200) {
                                    if (response.body().getData().getBalance() < 1) {
                                        priceInsufficient = 1;
                                    } else {
                                        priceInsufficient = 2;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                            }
                        });
                        showPayWindow();
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {

                }
            });
        } else {
            if (surplusTime == 0)
                surplusTime++;
            if (TextUtils.isEmpty(keda.getKederDB().getAnswerurl()))
                return;
            if (keda.getKederDB().getAnswerurl().startsWith("/"))
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + keda.getKederDB().getAnswerurl());
            else
                AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + keda.getKederDB().getAnswerurl());
            AudioPlayManager.getInstance().setmMediaPlayId(keda.getKederDB().getId());
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取问答列表
     */
    private void toHttpGetAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("solverid", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_CONDITIONS, map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    List<NewScienceHelp> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), NewScienceHelp.class);
                    if (data.size() > 0) {
                        lvAnswerTop.setHasLoadMore(true);
                        mDatas.addAll(data);
                        pages++;
                    } else {
                        lvAnswerTop.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvAnswerTop.showFailUI();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvAnswerTop.showFailUI();
            }
        });
    }

    /**
     * 获取答人详情
     */
    private void toHttpGetDetail() {
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    initData();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    /**
     * 获取到了详情后填充数据
     */
    private void initData() {
        tvFollow.setOnClickListener(this);
        llAudioCommit.setOnClickListener(this);
        tvSendRequest.setOnClickListener(this);
//        tvAttachedMoney.setOnClickListener(this);
        rlFullText.setOnClickListener(this);
        imgMessage.setOnClickListener(this);
        tvAttachedFive.setOnClickListener(this);
        tvAttachedTen.setOnClickListener(this);
        tvAttachedFifty.setOnClickListener(this);
        tvAttachedOneHundred.setOnClickListener(this);
        tvAttention.setText(answerPeopleDetail.getFocusnum() + "");
        tvFansNumber.setText(answerPeopleDetail.getFollowers() + "");
        tvAudioLength.setText(TimeUtils.getAnswerLength(answerPeopleDetail.getMessagetaketime()));
        tvTotalMoney.setText(answerPeopleDetail.getTotalincome() + "");
        maintitleTxt.setText(answerPeopleDetail.getName() + "的科答");
        llAudioCommit.setEnabled(true);
        Glide.with(this).load(Util.getImgUrl(answerPeopleDetail.getCoverimg())).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.drawable.icon_default_avatar).error(R.drawable.icon_default_avatar).into(imgAvatar);
        etContent.setHint("向" + answerPeopleDetail.getName() + "提问,等待Ta语音回答；超过48小时未回答,将按支付路径全额退款,被回答后可以免费向Ta追问。");
        if (answerPeopleDetail.getIsFocus() == 1) {
            isFu = true;
            tvFollow.setText("已关注");
            tvFollow.setTextColor(Color.parseColor("#FFFFFF"));
            tvFollow.setBackgroundResource(R.drawable.shape_answer_focused);
        } else {
            tvFollow.setText("关注答人");
            tvFollow.setTextColor(Color.parseColor("#F1312E"));
            tvFollow.setBackgroundResource(R.drawable.shape_sub);
            isFu = false;
        }
        if (!TextUtils.isEmpty(answerPeopleDetail.getSign()) && !TextUtils.equals("null", answerPeopleDetail.getSign())) {
            tvSign.setText(answerPeopleDetail.getSign());
        }
        if (!TextUtils.isEmpty(answerPeopleDetail.getProfession()) && !TextUtils.equals("null", answerPeopleDetail.getProfession())) {
            tvJob.setText(answerPeopleDetail.getProfession());
        }
        tvName.setText(answerPeopleDetail.getName());
        tvThroat.setText("¥" + answerPeopleDetail.getMouthprice());
        num = answerPeopleDetail.getMouthprice();
        toHttpGetAnswerAlbum();
    }

    /**
     * 获取问答专辑列表
     */
    private void toHttpGetAnswerAlbum() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALL_ANSWER_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<AnswerAlbumBean> mData = GsonUtils.json2List(jsonData, AnswerAlbumBean.class);
                    allDomanList.addAll(mData);
                    String[] ids = answerPeopleDetail.getDomain().split(",");
                    for (int i = 0; i < ids.length; i++) {
                        for (int j = 0; j < allDomanList.size(); j++) {
                            if (allDomanList.get(j).getId() == Integer.parseInt(ids[i])) {
                                domanList.add(allDomanList.get(j));
                                break;
                            }
                        }
                    }
                    domanList.get(0).setSelected(true);
                    domanAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.txt_follow:
                if (!Util.hintLogin(this))
                    return;
                if (isFu) {
                    FollowCustomPop();
                } else {
                    isFu = true;
                    tvFollow.setText("已关注");
                    tvFollow.setTextColor(Color.parseColor("#FFFFFF"));
                    tvFollow.setBackgroundResource(R.drawable.shape_answer_focused);
                }
                break;
            case R.id.tv_no_finish:
                finishPop(mPopupWindow);
                tvFollow.setText("已关注");
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                finishPop(mPopupWindow);
                tvFollow.setText("关注答人");
                tvFollow.setTextColor(Color.parseColor("#F1312E"));
                tvFollow.setBackgroundResource(R.drawable.shape_sub);
                isFu = false;
                break;
            case R.id.tv_send_request:
                if (!Util.hintLogin(this))
                    return;
                isSubmitQuestion = true;
                Map<String, Object> map = new HashMap<>();
                if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                    map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                }else map.put("id", "");
                ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                    @Override
                    public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                        if (response.body().getCode() == 200) {
                            if (response.body().getData().getBalance() < num) {
                                priceInsufficient = 1;
                            } else {
                                priceInsufficient = 2;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitResult<BalanceBean>> call, Throwable t) {

                    }
                });
                showPayWindow();
                break;
            case R.id.ll_detail_expand:
                if (llDetailExpand.getTag().equals(0)) {
                    //当先未选中的方向为折叠，需要展开
                    gvDomans.setVisibility(View.VISIBLE);
                    llDetailExpand.setTag(1);
                    RotateAnimation expandAnim;
                    if (System.currentTimeMillis() - startTime > 360)
                        expandAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    else
                        expandAnim = new RotateAnimation((System.currentTimeMillis() - startTime) / 4, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    expandAnim.setDuration(360);
                    expandAnim.setFillAfter(true);
                    startTime = System.currentTimeMillis();
                    imgMoreDoman.startAnimation(expandAnim);
                } else {
                    //当前未选中的方向为展开，需要折叠
                    gvDomans.setVisibility(View.GONE);
                    llDetailExpand.setTag(0);
                    RotateAnimation collapseAnim;
                    if (System.currentTimeMillis() - startTime > 360)
                        collapseAnim = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    else
                        collapseAnim = new RotateAnimation((System.currentTimeMillis() - startTime) / 4, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    collapseAnim.setDuration(360);
                    collapseAnim.setFillAfter(true);
                    startTime = System.currentTimeMillis();
                    imgMoreDoman.startAnimation(collapseAnim);
                }
                break;

//            case R.id.tv_attached_money:
//                if (attached) {
//                    tvAttachedMoney.setText("我要加价邀Ta回答");
//                    num = answerPeopleDetail.getMouthprice();
//                    tvThroat.setText("润喉费¥" + num);
//                    attached = false;
//                    attachedMoney = 0;
//                    llContainer.setVisibility(View.INVISIBLE);
//                    if (selectionPosition >= 0 && !TextUtils.isEmpty(etContent.getText()))
//                        tvSendRequest.setEnabled(true);
//                    else
//                        tvSendRequest.setEnabled(false);
//                } else {
//                    tvAttachedMoney.setText("取消加价");
//                    attached = true;
//                    llContainer.setVisibility(View.VISIBLE);
//                    setAttachedNormal();
//                    tvSendRequest.setEnabled(false);
//                }
//                break;
            case R.id.tv_five_rmb:
                setAttachedNormal();
                tvAttachedFive.setEnabled(false);
                attachedMoney = 5;
                num = answerPeopleDetail.getMouthprice();
                num += attachedMoney;
                tvThroat.setText("¥" + num);
                if (!TextUtils.isEmpty(etContent.getText()) && selectionPosition >= 0) {
                    tvSendRequest.setEnabled(true);
                    tvSendRequest.setTextColor(Color.parseColor("#F1312E"));
                } else {
                    tvSendRequest.setEnabled(false);
                    tvSendRequest.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.tv_ten_rmb:
                setAttachedNormal();
                tvAttachedTen.setEnabled(false);
                attachedMoney = 10;
                num = answerPeopleDetail.getMouthprice();
                num += attachedMoney;
                tvThroat.setText("¥" + num);
                if (!TextUtils.isEmpty(etContent.getText()) && selectionPosition >= 0) {
                    tvSendRequest.setEnabled(true);
                    tvSendRequest.setTextColor(Color.parseColor("#F1312E"));
                } else {
                    tvSendRequest.setEnabled(false);
                    tvSendRequest.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.tv_fifty_rmb:
                setAttachedNormal();
                tvAttachedFifty.setEnabled(false);
                attachedMoney = 50;
                num = answerPeopleDetail.getMouthprice();
                num += attachedMoney;
                tvThroat.setText("¥" + num);
                if (!TextUtils.isEmpty(etContent.getText()) && selectionPosition >= 0) {
                    tvSendRequest.setEnabled(true);
                    tvSendRequest.setTextColor(Color.parseColor("#F1312E"));
                } else {
                    tvSendRequest.setEnabled(false);
                    tvSendRequest.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.tv_custom_rmb:
                tvAttachedOneHundred.setEnabled(false);
                //自定义打赏金额
                setAttachedNormal();
                showCustomPop();
                if (!TextUtils.isEmpty(etContent.getText()) && selectionPosition >= 0) {
                    tvSendRequest.setEnabled(true);
                    tvSendRequest.setTextColor(Color.parseColor("#F1312E"));
                } else {
                    tvSendRequest.setEnabled(false);
                    tvSendRequest.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.img_exit:
                finishPop(mShowPopupWindow);
                tvThroat.setText("¥" + answerPeopleDetail.getMouthprice());
                break;
            case R.id.tv_assign:
                //确定使用自定义打赏金额
                if (!TextUtils.isEmpty(etCustomPrice.getText())) {
                    finishPop(mShowPopupWindow);
                } else {
                    Toast.makeText(AnswerTopDetailActivity.this, "请输入您要打赏的金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_audio_commit:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PAUSE:
                        AudioPlayManager.getInstance().resume();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        break;
                    case SystemConstant.ON_PREPARE:
                        break;
                    default:
                        if (answerPeopleDetail.getMessageurl().startsWith("/"))
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + answerPeopleDetail.getMessageurl());
                        else
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + "/" + answerPeopleDetail.getMessageurl());
                        break;
                }
                break;
            case R.id.rl_full_text:
                if (flag) {
                    flag = false;
                    tvSign.setMaxLines(3);
                    tvFullText.setText("全文");
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.START); // 展开
//                    tvPersonalIntroduce.setSingleLine(flag);
                } else {
                    flag = true;
//                    tvPersonalIntroduce.setSingleLine(flag);
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    tvSign.setMaxLines(100);
                    tvFullText.setText("收起");
                }
                break;
            case R.id.img_message:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PAUSE:
                        AudioPlayManager.getInstance().resume();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        break;
                    case SystemConstant.ON_PREPARE:
                        break;
                    default:
                        if (answerPeopleDetail.getMessageurl().startsWith("/"))
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + answerPeopleDetail.getMessageurl());
                        else
                            AudioPlayManager.getInstance().playRecord(SystemConstant.ACCESS_IMG_HOST + "/" + answerPeopleDetail.getMessageurl());
                        break;
                }
                break;
        }
    }

    private void showCustomPop() {
        if (mShowPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_custom_rmb, null);
            imgExit = (ImageView) view.findViewById(R.id.img_exit);
            ((TextView) view.findViewById(R.id.tv_pop_author_name)).setText(answerPeopleDetail == null ? "" : answerPeopleDetail.getName());
            tvAssign = (TextView) view.findViewById(R.id.tv_assign);
            etCustomPrice = (EditText) view.findViewById(R.id.et_custom_price);
            tvAssign.setOnClickListener(this);
            imgExit.setOnClickListener(this);
            etCustomPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String numString = s.toString();
                    if (numString.startsWith("0")) {
                        etCustomPrice.setText("");
                    } else {
                        if (numString.length() > 0) {
                            int num = Integer.parseInt(numString);
                            if (num > 200) {
                                etCustomPrice.setText(200 + "");
                                etCustomPrice.setSelection(3);
                            }
                        }
                    }
                }
            });
            mShowPopupWindow = new PopupWindow(view, -2, -2);
            mShowPopupWindow.setContentView(view);
            mShowPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mShowPopupWindow.setFocusable(true);
            mShowPopupWindow.setOutsideTouchable(true);
            mShowPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mShowPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(etCustomPrice.getText().toString())) {
                        setAttachedNormal();
                    } else {
                        int numone = Integer.parseInt(etCustomPrice.getText().toString());
                        num = answerPeopleDetail.getMouthprice();
                        num += numone;
                        tvThroat.setText("¥" + num);
                    }
                    finishPop(mShowPopupWindow);
                }
            });
            mShowPopupWindow.showAtLocation(llAnswer, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mShowPopupWindow.showAtLocation(llAnswer, Gravity.CENTER, 0, 0);
            etCustomPrice.setText("");
        }
    }

    /**
     * 选择支付方式
     */
    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    if (isSubmitQuestion) {
                        //提问
                        Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                        questionForWxPay();
                    } else {
                        //偷听
                        Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                        wxPay();
                    }
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    if (isSubmitQuestion) {
                        //提问
                        Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                        questionForAliPay();
                    } else {
                        //偷听
                        Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                        aliPay();
                    }
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                    final double price = isSubmitQuestion ? num : 1;
                    if (priceInsufficient == 1) {
                        ToastMaker.showShortToast("余额不足");
                    } else {
                        showWaringDialog("支付", "是否使用" + price + "墨子币？", new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                if (isSubmitQuestion)
                                    questionForBalancePay();
                                else
                                    toHttpBalancePay(price);
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    }
                }
            });
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(llAnswer, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(AnswerTopDetailActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(llAnswer, Gravity.BOTTOM, 0, 0);
        }
        if (isSubmitQuestion)
            tvOrderInfo.setText("您将向" + tvName.getText().toString() + "提问,总金额¥" + num + ",请再次确认购买");
        else
            tvOrderInfo.setText("您将偷听,请再次确认购买");
    }

    private void questionForBalancePay() {
        Map<String, Object> kedaQuestionsDB = new HashMap<>();
        kedaQuestionsDB.put("questions", etContent.getText().toString());
        for (AnswerAlbumBean answerAlbumBean : domanList) {
            if (answerAlbumBean.isSelected()) {
                kedaQuestionsDB.put("kedaalbumid", answerAlbumBean.getId());
                break;
            }
        }
        kedaQuestionsDB.put("solverid", getIntent().getStringExtra("id"));
        kedaQuestionsDB.put("amount", num);
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            kedaQuestionsDB.put("makerid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else kedaQuestionsDB.put("makerid", "");
        Map<String, Object> payHistory = new HashMap<>();
        payHistory.put("pay_type", 4);
        Map<String, Object> map = new HashMap<>();
        map.put("kedaQuestionsDB", kedaQuestionsDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().questionForBalancePay(map).enqueue(new RetrofitCallBack<Object>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                if (response.body().getCode() == 200) {
                    ToastMaker.showShortToast("提问成功");
                } else if (response.body().getCode() == 417) {
                    ToastMaker.showShortToast("余额不足");
                    startActivity(new Intent(AnswerTopDetailActivity.this, AccountBalanceActivity.class));
                    finish();
                } else {
                    ToastMaker.showShortToast("提问失败");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

            }
        });
    }

    private void questionForWxPay() {
        Map<String, Object> kedaQuestionsDB = new HashMap<>();
        kedaQuestionsDB.put("questions", etContent.getText().toString());
        for (AnswerAlbumBean answerAlbumBean : domanList) {
            if (answerAlbumBean.isSelected()) {
                kedaQuestionsDB.put("kedaalbumid", answerAlbumBean.getId());
                break;
            }
        }
        kedaQuestionsDB.put("solverid", getIntent().getStringExtra("id"));
        kedaQuestionsDB.put("amount", num);
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            kedaQuestionsDB.put("makerid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else kedaQuestionsDB.put("makerid", "");
        Map<String, Object> payHistory = new HashMap<>();
        payHistory.put("pay_type", 2);
        Map<String, Object> map = new HashMap<>();
        map.put("kedaQuestionsDB", kedaQuestionsDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().questionForWxPay(map).enqueue(new RetrofitCallBack<WXPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WXPay>> call, Response<RetrofitResult<WXPay>> response) {
                if (response.body().getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = response.body().getData();
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(AnswerTopDetailActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                } else {
                    ToastMaker.showShortToast("支付失败");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXPay>> call, Throwable t) {

            }
        });
    }

    private void questionForAliPay() {
        Map<String, Object> kedaQuestionsDB = new HashMap<>();
        kedaQuestionsDB.put("questions", etContent.getText().toString());
        for (AnswerAlbumBean answerAlbumBean : domanList) {
            if (answerAlbumBean.isSelected()) {
                kedaQuestionsDB.put("kedaalbumid", answerAlbumBean.getId());
                break;
            }
        }
        kedaQuestionsDB.put("solverid", getIntent().getStringExtra("id"));
        kedaQuestionsDB.put("amount", num);
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            kedaQuestionsDB.put("makerid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else kedaQuestionsDB.put("makerid", "");
        Map<String, Object> payHistory = new HashMap<>();
        payHistory.put("pay_type", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("kedaQuestionsDB", kedaQuestionsDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().questionForAliPay(map).enqueue(new RetrofitCallBack<AliPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<AliPay>> call, Response<RetrofitResult<AliPay>> response) {
                if (response.body().getCode() == 200) {
                    final AliPay aliPay = response.body().getData();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(AnswerTopDetailActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                ToastMaker.showShortToast("提问成功");
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    ToastMaker.showShortToast("支付失败");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<AliPay>> call, Throwable t) {

            }
        });
    }

    private void toHttpBalancePay(double price) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> kederDB = new HashMap<>();
        Map<String, Object> payHistory = new HashMap<>();
        kederDB.put("id", keda.getKederDB().getId());
        payHistory.put("pay_type", 4);
        map.put("kederDB", kederDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().kedaEavedropBalancePay(map).enqueue(new RetrofitCallBack<Object>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "支付成功", Toast.LENGTH_SHORT).show();
                    toHttpEavedrop();
                } else if (response.body().getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

            }
        });
    }

    private void aliPay() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> kederDB = new HashMap<>();
        Map<String, Object> payHistory = new HashMap<>();
        kederDB.put("id", keda.getKederDB().getId());
        payHistory.put("pay_type", 1);
        map.put("kederDB", kederDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().kedaEavedropAliPay(map).enqueue(new RetrofitCallBack<AliPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<AliPay>> call, Response<RetrofitResult<AliPay>> response) {
                if (response.body().getCode() == 200) {
                    final AliPay aliPay = response.body().getData();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(AnswerTopDetailActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                toHttpEavedrop();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<AliPay>> call, Throwable t) {

            }
        });
    }

    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> kederDB = new HashMap<>();
        Map<String, Object> payHistory = new HashMap<>();
        kederDB.put("id", keda.getKederDB().getId());
        payHistory.put("pay_type", 2);
        map.put("kederDB", kederDB);
        map.put("payHistory", payHistory);
        ApiServerManager.getInstance().getApiServer().kedaEavedropWxPay(map).enqueue(new RetrofitCallBack<WXPay>() {
            @Override
            public void onSuccess(Call<RetrofitResult<WXPay>> call, Response<RetrofitResult<WXPay>> response) {
                if (response.body().getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = response.body().getData();
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(AnswerTopDetailActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXPay>> call, Throwable t) {

            }
        });
    }

    /**
     * 提问
     */
//    private void sendAnswerToHttp() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("solverid", getIntent().getStringExtra("id"));
//        map.put("amount", num);
//        map.put("catid", catId);
//        map.put("id", UUID);
//        for (AnswerAlbumBean answerAlbumBean : domanList) {
//            if (answerAlbumBean.isSelected()) {
//                map.put("kedaalbumid", answerAlbumBean.getId());
//                break;
//            }
//        }
//        map.put("questions", etContent.getText().toString());
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SUBMIT_ANSWER, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
//                    etContent.setText("");
//                    tvThroat.setText("¥" + num);
//                } else {
//                    Toast.makeText(AnswerTopDetailActivity.this, "发送问题失败，金额将会3日内返回", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
//    }
    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof AnswerTopDetailActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.ADD_ZAN:
                if (DB.getInstance(DemoApplication.applicationContext).existedClickTable((String) msg.getBody())) {
                    DB.getInstance(DemoApplication.applicationContext).updataClick(1, (String) msg.getBody());
                } else {
                    clickList.add((String) msg.getBody());
                    DB.getInstance(DemoApplication.applicationContext).insertClick((String) msg.getBody());
                }
                break;
//            case SystemConstant.REMOVE_ZAN:
//                if (clickList.contains((String) msg.getBody())) {
//                    DB.getInstance(DemoApplication.applicationContext).deleteClick((String) msg.getBody());
//                    clickList.remove((String) msg.getBody());
//                } else {
//                    DB.getInstance(DemoApplication.applicationContext).updataClick(0, (String) msg.getBody());
//                }
//                break;
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    if (!isSubmitQuestion) {
                        toHttpEavedrop();
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "提问成功", Toast.LENGTH_SHORT).show();
                    }
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.EAVEDROP_COMPLITE:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void FollowCustomPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(llAnswer, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llAnswer, Gravity.CENTER, 0, 0);
        }
        tvFollow.setText("已关注");
    }

    /**
     * 隐藏PopupWindow
     *
     * @param popupWindow 要隐藏的PopupWindow
     */
    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    private void setAttachedNormal() {
        tvAttachedFive.setEnabled(true);
        tvAttachedTen.setEnabled(true);
        tvAttachedFifty.setEnabled(true);
        tvAttachedOneHundred.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (answerPeopleDetail != null) {
            boolean b = answerPeopleDetail.getIsFocus() == 1 ? true : false;
            if (b != isFu) {
                toHttpFocus();
            }
        }
    }

    private void toHttpFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", answerPeopleDetail.getId());
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_FOCUS_CHANGED);
        msg.setArg1(getIntent().getIntExtra("position", -1));
        if (isFu) {
            map.put("oparetion", 1);
            msg.setArg2(1);
        } else {
            map.put("oparetion", 0);
            msg.setArg2(0);
        }
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if (isFu)
                        answerPeopleDetail.setIsFocus(1);
                    else
                        answerPeopleDetail.setIsFocus(0);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AudioPlayManager.getInstance().release();
        clickList.removeAll(clickList);
    }


    public class DomanAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return domanList.size();
        }

        @Override
        public AnswerAlbumBean getItem(int position) {
            return domanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tv_doman, parent, false);
            }
            ((TextView) convertView).setText(domanList.get(position).getKdname());
            if (domanList.get(position).isSelected()) {
                ((TextView) convertView).setBackgroundResource(R.drawable.shape_answer_top_send_enable);
                ((TextView) convertView).setTextColor(Color.parseColor("#F1312E"));
            } else {
                ((TextView) convertView).setBackgroundResource(R.drawable.shape_answer_top_text_border);
                ((TextView) convertView).setTextColor(Color.parseColor("#999999"));
            }
            return convertView;
        }
    }

}
