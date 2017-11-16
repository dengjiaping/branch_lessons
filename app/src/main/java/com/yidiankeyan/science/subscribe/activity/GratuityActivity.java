package com.yidiankeyan.science.subscribe.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.subscribe.adapter.GratuityPeopleAdapter;
import com.yidiankeyan.science.subscribe.entity.AuthorInfo;
import com.yidiankeyan.science.subscribe.entity.GratuityPersonInfo;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 打赏
 */
public class GratuityActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;

    private EditText txtContent;
    private TextView tvEmptyOfContent;
    private TextView tvTwoRmb;
    private TextView tvFiveRmb;
    private TextView tvTenRmb;
    private TextView tvFiftyRmb;
    private TextView tvHundredRmb;
    private TextView tvCustomRmb;
    private PopupWindow mFollowPopupWindow;
    private PopupWindow mShowPopupWindow;
    private RelativeLayout rlContainer;
    private ImageView imgExit;
    private TextView tvAssign;
    private ImageButton imgFollow;
    private boolean isFirst;
    private EditText etCustomPrice;
    private RecyclerView lvGratuityPeople;
    private GratuityPeopleAdapter adapter;
    private List<GratuityPersonInfo> gratuityPersonList = new ArrayList<>();
    private ImageView imgAnonymous;
    private ImageView llGratuity;
    private ImageView imgAliPay;
    private ImageView imgGratuityPortrait;
    private TextView tvAuthorName;
    private TextView tvAuthorAlbumCount;
    private TextView tvAuthorFansCount;
    private TextView tvAuthorTipTotal;

    private boolean isFo;
    private AutoLinearLayout llGratuityPeople;
    private TextView tvFinish, tvYesClick;
    StringBuffer sb;
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private WXReceiver wxReceiver;
    private String price;
    private String id;
    private AuthorInfo authorInfo;
    private Intent intent;

    @Override
    protected void initView() {

        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);

        txtContent = (EditText) findViewById(R.id.add_content);
        txtContent.clearFocus();
        imgAliPay = (ImageView) findViewById(R.id.img_ali_pay);
        tvEmptyOfContent = (TextView) findViewById(R.id.tv_empty_of_content);
        tvTwoRmb = (TextView) findViewById(R.id.tv_two_rmb);
        tvFiveRmb = (TextView) findViewById(R.id.tv_five_rmb);
        tvTenRmb = (TextView) findViewById(R.id.tv_ten_rmb);
        tvFiftyRmb = (TextView) findViewById(R.id.tv_fifty_rmb);
        tvHundredRmb = (TextView) findViewById(R.id.tv_hundred_rmb);
        tvCustomRmb = (TextView) findViewById(R.id.tv_custom_rmb);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_all);
        lvGratuityPeople = (RecyclerView) findViewById(R.id.lv_gratuity_people);
        imgAnonymous = (ImageView) findViewById(R.id.img_anonymous);
        llGratuity = (ImageView) findViewById(R.id.img_gratuity_number);
        imgFollow = (ImageButton) findViewById(R.id.btn_gratuity_follow);
        imgGratuityPortrait = (ImageView) findViewById(R.id.img_gratuity_portrait);
        tvAuthorName = (TextView) findViewById(R.id.tv_author_name);
        tvAuthorAlbumCount = (TextView) findViewById(R.id.tv_author_album_count);
        tvAuthorFansCount = (TextView) findViewById(R.id.tv_author_fans_count);
        tvAuthorTipTotal = (TextView) findViewById(R.id.tv_author_tip_total);
        llGratuityPeople = (AutoLinearLayout) findViewById(R.id.ll_gratuity_people);
        //设置打赏金额的宽度
        int width = Util.getScreenWidth(this);
        int textWidth = (width - Util.dip2px(this, 40)) / 3;
        setTextWidth(textWidth, tvTwoRmb);
        setTextWidth(textWidth, tvFiveRmb);
        setTextWidth(textWidth, tvTenRmb);
        setTextWidth(textWidth, tvFiftyRmb);
        setTextWidth(textWidth, tvHundredRmb);
        setTextWidth(textWidth, tvCustomRmb);
    }

    @Override
    protected void initAction() {
        toHttpGetAuthorInfo();
        toHttpGetGratuityHistory();
        titleBtn.setVisibility(View.GONE);
        txtTitle.setText("打赏主编");
        imgFollow.setImageResource(R.drawable.follow);
        txtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtContent.getText().length() == 0) {
                    tvEmptyOfContent.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyOfContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        llReturn.setOnClickListener(this);
        tvTwoRmb.setOnClickListener(this);
        tvFiveRmb.setOnClickListener(this);
        tvTenRmb.setOnClickListener(this);
        tvFiftyRmb.setOnClickListener(this);
        tvHundredRmb.setOnClickListener(this);
        tvCustomRmb.setOnClickListener(this);
        imgAnonymous.setOnClickListener(this);
        llGratuity.setOnClickListener(this);
        imgAliPay.setOnClickListener(this);
        imgFollow.setOnClickListener(this);
        llGratuityPeople.setOnClickListener(this);
        findViewById(R.id.img_wx_pay).setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        lvGratuityPeople.setLayoutManager(layoutManager);
        adapter = new GratuityPeopleAdapter(this, gratuityPersonList);
        lvGratuityPeople.setAdapter(adapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction("WX_result");
        // 注册广播接受器
        wxReceiver = new WXReceiver();
        registerReceiver(wxReceiver, filter);
        // 微信
        weixin();
    }

    private void toHttpGetGratuityHistory() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getStringExtra("authorId"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GRATUITY_HISTORY, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    gratuityPersonList.addAll(GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), GratuityPersonInfo.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpGetAuthorInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("authorId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_GRATUITY_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    authorInfo = (AuthorInfo) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AuthorInfo.class);
                    Glide.with(mContext).load(Util.getImgUrl(authorInfo.getImgurl())).placeholder(R.drawable.icon_default_avatar).into(imgGratuityPortrait);
                    tvAuthorName.setText(authorInfo.getUsername());
                    tvAuthorAlbumCount.setText(authorInfo.getAlbumnum() + "");
                    tvAuthorFansCount.setText(authorInfo.getFollower_num() + "");
                    tvAuthorTipTotal.setText(authorInfo.getTip_total() + "墨子币");
                    if (authorInfo.getIsfocus() == 1) {
                        isFo = true;
                        imgFollow.setImageResource(R.drawable.onfollow);
                    } else {
                        imgFollow.setImageResource(R.drawable.follow);
                        isFo = false;
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_gratuity;
    }

    /**
     * 设置打赏金额的宽度
     *
     * @param textWidth
     * @param tv
     */
    private void setTextWidth(int textWidth, TextView tv) {
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        params.width = textWidth;
        tv.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_two_rmb:
                tvCustomRmb.setText("自定义");
                setNormal();
                tvTwoRmb.setEnabled(false);
                price = "2";
                break;
            case R.id.tv_five_rmb:
                tvCustomRmb.setText("自定义");
                setNormal();
                tvFiveRmb.setEnabled(false);
                price = "5";
                break;
            case R.id.tv_ten_rmb:
                tvCustomRmb.setText("自定义");
                setNormal();
                tvTenRmb.setEnabled(false);
                price = "10";
                break;
            case R.id.tv_fifty_rmb:
                tvCustomRmb.setText("自定义");
                setNormal();
                tvFiftyRmb.setEnabled(false);
                price = "50";
                break;
            case R.id.btn_gratuity_follow:
                if (!Util.hintLogin(this))
                    return;
                if (isFo) {
                    //取消关注
                    FollowCustomPop();
                } else {
                    //关注
                    tvAuthorFansCount.setText(Integer.parseInt(tvAuthorFansCount.getText().toString()) + 1 + "");
                    isFo = true;
                    imgFollow.setImageResource(R.drawable.onfollow);
                }
                break;
            case R.id.tv_no_finish:
                finishPop(mFollowPopupWindow);
                imgFollow.setImageResource(R.drawable.onfollow);
                isFo = true;
                break;
            case R.id.tv_yes_onclick:
                tvAuthorFansCount.setText(Integer.parseInt(tvAuthorFansCount.getText().toString()) - 1 + "");
                finishPop(mFollowPopupWindow);
                imgFollow.setImageResource(R.drawable.follow);
                isFo = false;
                break;
            case R.id.tv_hundred_rmb:
                setNormal();
                tvHundredRmb.setEnabled(false);
                price = "100";
                break;
            case R.id.ll_gratuity_people:
                intent = new Intent(this, RewardTopActivity.class);
                intent.putExtra("authorId", getIntent().getStringExtra("authorId"));
                startActivity(intent);
                break;
            case R.id.img_gratuity_number:
                intent = new Intent(this, RewardTopActivity.class);
                intent.putExtra("authorId", getIntent().getStringExtra("authorId"));
                startActivity(intent);
                break;
            case R.id.img_anonymous:
                //匿名留言
                if (isFirst) {
                    isFirst = false;
                    imgAnonymous.setImageResource(R.drawable.anonymous);
                } else {
                    isFirst = true;
                    imgAnonymous.setImageResource(R.drawable.onanonymous);
                }
                break;
            case R.id.tv_custom_rmb:
                //自定义打赏金额
                setNormal();
                tvCustomRmb.setTextColor(Color.WHITE);
                tvCustomRmb.setBackgroundResource(R.drawable.shape_custom_textview_pressed);
                showCustomPop();
                break;
            case R.id.img_exit:
                finishPop(mShowPopupWindow);
                break;
            case R.id.tv_assign:
                //确定使用自定义打赏金额
                if (!TextUtils.isEmpty(etCustomPrice.getText())) {
                    finishPop(mShowPopupWindow);
                    tvCustomRmb.setText(etCustomPrice.getText() + "墨子币");
                } else {
                    Toast.makeText(GratuityActivity.this, "请输入您要打赏的金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_wx_pay:
//                GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//                getPrepayId.execute();
                if (!Util.hintLogin(this))
                    return;
                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(this, "请输入打赏金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                wxPay();
                break;
            case R.id.img_ali_pay:
                if (!Util.hintLogin(this))
                    return;
                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(this, "请输入打赏金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                aliPay();// 支付宝去支付
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authorInfo != null) {
            boolean b = authorInfo.getIsfocus() == 1 ? true : false;
            if (b != isFo) {
                toHttpFocus();
            }
        }
    }

    private void toHttpFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", authorInfo.getUserid());
        if (isFo)
            map.put("oparetion", 1);
        else
            map.put("oparetion", 0);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if (isFo)
                        authorInfo.setIsfocus(1);
                    else
                        authorInfo.setIsfocus(0);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void FollowCustomPop() {
        if (mFollowPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            mFollowPopupWindow = new PopupWindow(view, -2, -2);
            mFollowPopupWindow.setContentView(view);
            mFollowPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mFollowPopupWindow.setFocusable(true);
            mFollowPopupWindow.setOutsideTouchable(true);
            mFollowPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mFollowPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(mFollowPopupWindow);
                }
            });
            mFollowPopupWindow.showAtLocation(rlContainer, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mFollowPopupWindow.showAtLocation(rlContainer, Gravity.CENTER, 0, 0);
        }
        imgFollow.setImageResource(R.drawable.onfollow);
    }

    private void showCustomPop() {
        if (mShowPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_custom_rmb, null);
            imgExit = (ImageView) view.findViewById(R.id.img_exit);
            ((TextView) view.findViewById(R.id.tv_pop_author_name)).setText(authorInfo == null ? "" : authorInfo.getUsername());
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
                        setNormal();
                        price = "";
                    } else {
                        price = etCustomPrice.getText().toString();
                    }
                    finishPop(mShowPopupWindow);
                }
            });
            mShowPopupWindow.showAtLocation(rlContainer, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mShowPopupWindow.showAtLocation(rlContainer, Gravity.CENTER, 0, 0);
            etCustomPrice.setText("");
        }
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

    /**
     * 将打赏金额背景全部设为正常
     */
    private void setNormal() {
        tvTwoRmb.setEnabled(true);
        tvFiveRmb.setEnabled(true);
        tvTenRmb.setEnabled(true);
        tvFiftyRmb.setEnabled(true);
        tvHundredRmb.setEnabled(true);
        tvCustomRmb.setTextColor(Color.parseColor("#F1312E"));
        tvCustomRmb.setBackgroundResource(R.drawable.shape_custom_textview_normal);
    }

    /**************************************
     * 微信
     ********************************************/

    private void wxPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_USER");
        map.put("goods_id", getIntent().getStringExtra("authorId"));
        map.put("seller_id", getIntent().getStringExtra("authorId"));
        map.put("price", price);
//        map.put("price",  0.01);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/wechatPay", map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    id = wxPay.getId();
                    genPayReq(wxPay);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    // 微信初始化
    private void weixin() {
        sb = new StringBuffer();
        req = new PayReq();
    }

    private void genPayReq(WXPay wxPay) {
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
        sb.append("sign\n" + req.sign + "\n\n");
        LogUtils.e("orion=" + signParams.toString());
        msgApi.registerApp(req.appId);
        msgApi.sendReq(req);
    }

    class WXReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int result = arg1.getIntExtra("result", -100);
            if (result == -100) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
            } else if (result == 0) {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                toHttpBestow();
            } else if (result == -2) {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*************************************微信end********************************************/

    /**************************************
     * 支付宝
     ********************************************/

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            Log.e("map", ((Map<String, String>) msg.obj).toString());
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(GratuityActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                toHttpBestow();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(GratuityActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void aliPay() {

        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_USER");
        map.put("goods_id", getIntent().getStringExtra("authorId"));
        map.put("seller_id", getIntent().getStringExtra("authorId"));
        map.put("price", price);
//        map.put("price", 0.02);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/alipay", map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(GratuityActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /*************************************
     * 支付宝end
     ********************************************/

    /**
     * 打赏
     */
    private void toHttpBestow() {
        Map<String, Object> map = new HashMap<>();
        map.put("tip_type", "TIP_USER");
        map.put("trade_id", id);
        map.put("goods_id", getIntent().getStringExtra("authorId"));
        map.put("seller_id", getIntent().getStringExtra("authorId"));
        map.put("tip_price", price);
//        map.put("tip_price", 0.02);
        map.put("message", "");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.BESTOW, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wxReceiver);
    }
}
