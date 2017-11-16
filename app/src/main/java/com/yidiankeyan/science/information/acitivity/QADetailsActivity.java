package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.information.entity.EavedropMediaBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
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
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 科答
 * -问答详情
 */
public class QADetailsActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private AutoLinearLayout llQadetails, llRecentAnswer;
    private AutoRelativeLayout imgResponderEavedrop;
    private ImageView imgMakeAvatar, imgResponderAvatar, imgResponderPlay;
    private TextView tvMakeName, tvMakeContent, tvMakeTime, tvResponderName, tvResponderPosition, tvEavesdropCount, tvNameAll, tvResponderFollow, tvAlreadyFollow, tvResponderLength;
    private PopupWindow paysPopupWindow;
    private TextView tvOrderInfo;
    private boolean isFu;
    private TextView tvFinish, tvYesClick;
    private PopupWindow qadPopupWindow;
    private TextView tvPermission;
    private NewScienceHelp keda;
    private int priceInsufficient = 0;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_qadetails;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        llQadetails = (AutoLinearLayout) findViewById(R.id.ll_qadetails);
        imgMakeAvatar = (ImageView) findViewById(R.id.img_make_avatar);
        imgResponderAvatar = (ImageView) findViewById(R.id.img_responder_avatar);
        imgResponderPlay = (ImageView) findViewById(R.id.img_responder_play);
        tvMakeName = (TextView) findViewById(R.id.tv_make_name);
        tvMakeContent = (TextView) findViewById(R.id.tv_make_content);
        tvMakeTime = (TextView) findViewById(R.id.tv_make_time);
        tvResponderName = (TextView) findViewById(R.id.tv_responder_name);
        tvPermission = (TextView) findViewById(R.id.tv_permission);
        tvResponderPosition = (TextView) findViewById(R.id.tv_responder_position);
        tvEavesdropCount = (TextView) findViewById(R.id.tv_eavesdrop_count);
        tvResponderLength = (TextView) findViewById(R.id.tv_responder_length);
        tvNameAll = (TextView) findViewById(R.id.tv_name_all);
        tvResponderFollow = (TextView) findViewById(R.id.txt_responder_follow);
        tvAlreadyFollow = (TextView) findViewById(R.id.txt_already_follow);
        imgResponderEavedrop = (AutoRelativeLayout) findViewById(R.id.img_responder_eavedrop);
        llRecentAnswer = (AutoLinearLayout) findViewById(R.id.ll_recent_answer);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("问答详情");
        titleBtn.setVisibility(View.GONE);
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", getIntent().getStringExtra("solverid"));
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
//                    if (user.getIsfocus() == 0) {
//                        isFu = false;
//                        tvResponderFollow.setVisibility(View.VISIBLE);
//                        tvAlreadyFollow.setVisibility(View.GONE);
//                    } else {
//                        isFu = true;
//                        tvResponderFollow.setVisibility(View.GONE);
//                        tvAlreadyFollow.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//            }
//        });
        llReturn.setOnClickListener(this);
        llRecentAnswer.setOnClickListener(this);
        tvResponderFollow.setOnClickListener(this);
        tvAlreadyFollow.setOnClickListener(this);
//        Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + getIntent().getStringExtra("makerUrl")).placeholder(R.drawable.icon_science_help).error(R.drawable.icon_science_help).into(imgMakeAvatar);
//        tvMakeName.setText(getIntent().getStringExtra("makername"));
//        Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + getIntent().getStringExtra("imgUrl")).placeholder(R.drawable.icon_science_help).error(R.drawable.icon_science_help).into(imgResponderAvatar);
//        tvResponderName.setText(getIntent().getStringExtra("name"));
//        tvMakeContent.setText("【" + getIntent().getStringExtra("contentName") + "】 " + getIntent().getStringExtra("content"));
//        tvMakeTime.setText(TimeUtils.formatKedaTime(getIntent().getLongExtra("time", 0L)) + "");
//        tvResponderPosition.setText(getIntent().getStringExtra("position"));
//        tvResponderLength.setText(TimeUtils.getAnswerLength(getIntent().getIntExtra("taketime", 0)) + "");
//        tvEavesdropCount.setText(getIntent().getIntExtra("eavesdropnum", 0) + "");
//        if (!TextUtils.equals("null", getIntent().getStringExtra("name")) && !TextUtils.isEmpty(getIntent().getStringExtra("name")) && !getIntent().getStringExtra("name").equals("null")) {
//            tvNameAll.setText(getIntent().getStringExtra("name") + "的全部回答");
//        } else {
//            tvNameAll.setText("Ta的全部回答");
//        }
//        tvPermission.setText(getIntent().getIntExtra("permission", 0) == 1 ? "点击播放" : "一元偷听");
        imgResponderEavedrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.hintLogin(QADetailsActivity.this))
                    return;
                toHttpEavedrop(keda.getKederDB().getId());
            }
        });
        imgResponderEavedrop.setEnabled(false);
        llRecentAnswer.setEnabled(false);
        tvResponderFollow.setEnabled(false);
        tvAlreadyFollow.setEnabled(false);
        toHttpGetDetail();
    }

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("id", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getKeDaDetailList(map).enqueue(new RetrofitCallBack<List<NewScienceHelp>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<NewScienceHelp>>> call, Response<RetrofitResult<List<NewScienceHelp>>> response) {
                if (response.body().getCode() == 200) {
                    keda = response.body().getData() == null || response.body().getData().size() == 0 ? null : response.body().getData().get(0);
                    initData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<NewScienceHelp>>> call, Throwable t) {

            }
        });
    }

    private void initData() {
        if (keda == null)
            return;
        Glide.with(this).load(Util.getImgUrl(keda.getMaker().getCoverimg())).bitmapTransform(new CropCircleTransformation(this)).into(imgMakeAvatar);
        tvMakeName.setText(keda.getMaker().getName());
        tvMakeTime.setText(TimeUtils.formatKedaTime(keda.getKederDB().getAnswertime()) + "");
        tvMakeContent.setText(keda.getKederDB().getQuestions());
        Glide.with(this).load(Util.getImgUrl(keda.getSolver().getCoverimg())).bitmapTransform(new CropCircleTransformation(this)).into(imgResponderAvatar);
        tvResponderName.setText(keda.getSolver().getName());
        tvResponderPosition.setText(keda.getSolver().getProfession());
        //获取是否对回答者关注
        Map<String, Object> map = new HashMap<>();
        map.put("id", keda.getSolver().getId());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    if (user.getIsfocus() == 0) {
                        isFu = false;
                        tvResponderFollow.setVisibility(View.VISIBLE);
                        tvAlreadyFollow.setVisibility(View.GONE);
                    } else {
                        isFu = true;
                        tvResponderFollow.setVisibility(View.GONE);
                        tvAlreadyFollow.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
        tvPermission.setText(keda.getKederInfo().getPermission() == 1 ? "点击播放" : "一墨子币偷听");
        tvResponderLength.setText(TimeUtils.getAnswerLength(keda.getKederDB().getTaketime()) + "");
        tvEavesdropCount.setText(keda.getKederDB().getEavesdropnum() + "");
        if (!TextUtils.equals("null", keda.getSolver().getName()) && !TextUtils.isEmpty(keda.getSolver().getName()) && !keda.getSolver().getName().equals("null")) {
            tvNameAll.setText(keda.getSolver().getName() + "的全部回答");
        } else {
            tvNameAll.setText("Ta的全部回答");
        }
        imgResponderEavedrop.setEnabled(true);
        llRecentAnswer.setEnabled(true);
        tvResponderFollow.setEnabled(true);
        tvAlreadyFollow.setEnabled(true);
    }


    /**
     * 获取资源
     *
     * @param id
     */
    public void toHttpEavedrop(final String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("kederid", id);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_KEDA_EAVEDROP_MEDIA, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    EavedropMediaBean eavedrop = (EavedropMediaBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), EavedropMediaBean.class);
                    if (eavedrop.getAudiourl().startsWith("/"))
                        AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + eavedrop.getAudiourl());
                    else
                        AudioPlayManager.getInstance().playEavedrop(SystemConstant.ACCESS_IMG_HOST + "/" + eavedrop.getAudiourl());
                    AudioPlayManager.getInstance().setmMediaPlayId(keda.getKederDB().getId());
                    AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
                    boolean isPlaying = AudioPlayManager.getInstance().isPlaying();
                    int state = AudioPlayManager.getInstance().CURRENT_STATE;
                    String mediaId = AudioPlayManager.getInstance().getmMediaPlayId();
                    keda.getKederInfo().setPermission(1);
                    if ((isPlaying || state == SystemConstant.ON_PREPARE) && mediaId.equals(keda.getKederDB().getId())) {
                        animationDrawable.start();
                        tvPermission.setText("点击暂停");
                    } else {
                        animationDrawable.stop();
                        if (keda.getKederInfo().getPermission() == 1) {
                            tvPermission.setText("点击播放");
                        } else {
                            tvPermission.setText("一墨子币偷听");
                        }
                    }
                } else if (result.getCode() == 101) {
                    Map<String, Object> map = new HashMap<>();
                    if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                        map.put("id", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                    }else map.put("id", "");
                    ApiServerManager.getInstance().getApiServer().getQueryBalance(map).enqueue(new RetrofitCallBack<BalanceBean>() {
                        @Override
                        public void onSuccess(Call<RetrofitResult<BalanceBean>> call, Response<RetrofitResult<BalanceBean>> response) {
                            if (response.body().getCode() == 200) {
                                if ( response.body().getData().getBalance() < 1) {
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
    }

    /**
     * 选择支付方式
     */
    private void showPayWindow() {
        hideSoftKeyboard();
        if (paysPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(QADetailsActivity.this, paysPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(QADetailsActivity.this, paysPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(QADetailsActivity.this, paysPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(QADetailsActivity.this, paysPopupWindow);
                    if (priceInsufficient == 1) {
                        ToastMaker.showShortToast("余额不足");
                    } else {
                        showWaringDialog("支付", "是否使用1墨子币？", new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                toHttpBalancePay();
                            }

                            @Override
                            public void onNegativeButtonClick() {

                            }
                        });
                    }

                }
            });
            paysPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paysPopupWindow.setContentView(view);
            paysPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = QADetailsActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            QADetailsActivity.this.getWindow().setAttributes(lp);
            paysPopupWindow.setFocusable(true);
            paysPopupWindow.setOutsideTouchable(true);
            paysPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            paysPopupWindow.showAtLocation(llQadetails, Gravity.BOTTOM, 0, 0);
            paysPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(QADetailsActivity.this, paysPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = QADetailsActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            QADetailsActivity.this.getWindow().setAttributes(lp);
            paysPopupWindow.showAtLocation(llQadetails, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥1,请再次确认购买");
    }

    private void toHttpBalancePay() {
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
                    toHttpEavedrop(keda.getKederDB().getId());
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
                            PayTask alipay = new PayTask(QADetailsActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                toHttpEavedrop(keda.getKederDB().getId());
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
                    IWXAPI api = WXAPIFactory.createWXAPI(QADetailsActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXPay>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_recent_answer:
                Intent intent = new Intent(mContext, AnswerTopDetailActivity.class);
                intent.putExtra("name", keda.getSolver().getName());
                intent.putExtra("id", keda.getSolver().getId());
                mContext.startActivity(intent);
                break;
            case R.id.txt_responder_follow:
                if (!Util.hintLogin(this))
                    return;
                //加关注
                Map<String, Object> map = new HashMap<>();
                map.put("targetid", keda.getSolver().getId());
                map.put("oparetion", 1);
                HttpUtil.post(QADetailsActivity.this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
                            isFu = true;
                            tvResponderFollow.setVisibility(View.GONE);
                            tvAlreadyFollow.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                break;
            case R.id.txt_already_follow:
                if (!Util.hintLogin(this))
                    return;
                if (isFu) {
                    FollowCustomPop();
                } else {
                    isFu = true;
                    tvAlreadyFollow.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_no_finish:
                finishPop(qadPopupWindow);
                tvAlreadyFollow.setVisibility(View.VISIBLE);
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                //取消关注
                finishPop(qadPopupWindow);
                Map<String, Object> maps = new HashMap<>();
                maps.put("targetid", keda.getSolver().getId());
                maps.put("oparetion", 0);
                HttpUtil.post(QADetailsActivity.this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
                            tvResponderFollow.setVisibility(View.VISIBLE);
                            tvAlreadyFollow.setVisibility(View.GONE);
                            isFu = false;
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                break;
        }
    }


    private void FollowCustomPop() {
        if (qadPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            qadPopupWindow = new PopupWindow(view, -2, -2);
            qadPopupWindow.setContentView(view);
            qadPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            qadPopupWindow.setFocusable(true);
            qadPopupWindow.setOutsideTouchable(true);
            qadPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            qadPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(qadPopupWindow);
                }
            });
            qadPopupWindow.showAtLocation(llQadetails, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            qadPopupWindow.showAtLocation(llQadetails, Gravity.CENTER, 0, 0);
        }
        tvAlreadyFollow.setVisibility(View.VISIBLE);
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

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof QADetailsActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    toHttpEavedrop(keda.getKederDB().getId());
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.EAVEDROP_COMPLITE:
                AnimationDrawable animationDrawable = (AnimationDrawable) imgResponderPlay.getDrawable();
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                    tvPermission.setText("点击播放");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AudioPlayManager.getInstance().release();
    }
}
