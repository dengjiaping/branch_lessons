package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.AnswerPeopleDetail;
import com.yidiankeyan.science.information.entity.KedaNoticeBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.information.fragment.AnswerAlbumFragment;
import com.yidiankeyan.science.information.fragment.AnswerTopFragment;
import com.yidiankeyan.science.information.fragment.EavesdropTopFragment;
import com.yidiankeyan.science.information.fragment.MyEavesdropFragment;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.utils.net.UploadResultEntity;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 科答
 */
public class ScienceHelpActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private ImageButton btnTitle;
    private TextView maintitleTxt;
    private TextView tvLeft;
    private ImageButton titleBtn;
    private TextView tvRecentAnswer;
    private TextView tvEavesdropTop;
    private TextView tvAnswerAlbum;
    private TextView tvAnswerTop;
    private ViewPager viewPager;
    private List<Fragment> fragments;// Tab页面列表
    private MyPagerAdapter adapter;
    private int currIndex = 0;// 当前页卡编号
    private TextView tvQuestion;
    private TextView tvAnswer;
    private ArrayList<AnswerAlbumBean> mData;
    private AutoLinearLayout llAll;

    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;

    private MyEavesdropFragment myEavesdropFragment;
    private EavesdropTopFragment eavesdropTopFragment;
    private NewScienceHelp keda;

    private List<String> clickList = new ArrayList<>();
    private Map<String, Object> updataMap;
    private int priceInsufficient = 0;

    private TextView questionMsgNumber,answerMsgNumber;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_science_help;
    }

    @Override
    protected void initView() {
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        tvRecentAnswer = (TextView) findViewById(R.id.tv_recent_answer);
        tvEavesdropTop = (TextView) findViewById(R.id.tv_eavesdrop_top);
        tvAnswerAlbum = (TextView) findViewById(R.id.tv_answer_album);
        tvAnswerTop = (TextView) findViewById(R.id.tv_answer_top);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);
        questionMsgNumber = (TextView) findViewById(R.id.question_msg_number);
        answerMsgNumber= (TextView) findViewById(R.id.answer_msg_number);
    }

    @Override
    protected void initAction() {
        btnTitle.setVisibility(View.GONE);
        maintitleTxt.setText("科答");
        llReturn.setOnClickListener(this);
        fragments = new ArrayList<>();
        answerMsgNumber.setVisibility(View.INVISIBLE);
        questionMsgNumber.setVisibility(View.INVISIBLE);
        toGetHttpNumber();
        myEavesdropFragment = new MyEavesdropFragment();
        eavesdropTopFragment = new EavesdropTopFragment();
        fragments.add(myEavesdropFragment);
        fragments.add(eavesdropTopFragment);
        fragments.add(new AnswerAlbumFragment());
        fragments.add(new AnswerTopFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tvRecentAnswer.setTextColor(getResources().getColor(R.color.red));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        tvRecentAnswer.setOnClickListener(this);
        tvEavesdropTop.setOnClickListener(this);
        tvAnswerAlbum.setOnClickListener(this);
        tvAnswerTop.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);
        tvAnswer.setOnClickListener(this);
    }

    private void toGetHttpNumber() {
        Map<String, Object> map = new HashMap<>();
        ApiServerManager.getInstance().getApiServer().getKedaNotice(map).enqueue(new RetrofitCallBack<KedaNoticeBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<KedaNoticeBean>> call, Response<RetrofitResult<KedaNoticeBean>> response) {
                if(response.body().getCode()==200){
                    answerMsgNumber.setText(response.body().getData().getNewQuestions()+"");
                    questionMsgNumber.setText(response.body().getData().getNewAnswers()+"");
                    if(response.body().getData().getNewQuestions()==0){
                        answerMsgNumber.setVisibility(View.INVISIBLE);
                    }else{
                        answerMsgNumber.setVisibility(View.VISIBLE);
                    }
                    if(response.body().getData().getNewAnswers()==0){
                        questionMsgNumber.setVisibility(View.INVISIBLE);
                    }else{
                        questionMsgNumber.setVisibility(View.VISIBLE);
                    }
                    if(response.body().getData().getNewQuestions()>99||response.body().getData().getNewAnswers()>99){
                        answerMsgNumber.setText("99+");
                        questionMsgNumber.setText("99+");
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<KedaNoticeBean>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_recent_answer:
                currIndex = 0;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_eavesdrop_top:
                currIndex = 1;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_answer_album:
                currIndex = 2;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_answer_top:
                currIndex = 3;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_question:
                startActivity(new Intent(mContext, QuestionActivity.class));
                break;
            case R.id.tv_answer:
                if (SpUtils.getStringSp(mContext, "access_token").startsWith("-")) {
                    startActivity(new Intent(this, WithoutAnswerActivity.class));
                    return;
                } else {
                    showLoadingDialog("请稍候");
                    toHttpGetDetail();
                }
//                startActivity(new Intent(mContext, MyAnswerActivity.class));
                break;
        }
    }

    /**
     * 获取答人详情
     */
    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SpUtils.getStringSp(this, "userId"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_ANSWER_PEOPLE_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    AnswerPeopleDetail answerPeopleDetail = (AnswerPeopleDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AnswerPeopleDetail.class);
                    if (answerPeopleDetail == null) {
                        Intent intent = new Intent(ScienceHelpActivity.this, WithoutAnswerActivity.class);
                        intent.putExtra("state", 2);
                        startActivity(intent);
                        return;
                    }
                    int authenticated = answerPeopleDetail.getAuthenticated();
                    if (authenticated == 0) {
                        //还没有提交审核
                        Intent intent = new Intent(ScienceHelpActivity.this, WithoutAnswerActivity.class);
                        intent.putExtra("state", 2);
                        startActivity(intent);
                    } else if (authenticated == 1) {
                        //正在审核中
                        Intent intent = new Intent(ScienceHelpActivity.this, WithoutAnswerActivity.class);
                        intent.putExtra("state", 3);
                        startActivity(intent);
                    } else if (authenticated == 2) {
                        //用户已经是答人
                        Intent intent = new Intent(ScienceHelpActivity.this, MyAnswerActivity.class);
                        intent.putExtra("bean", answerPeopleDetail);
                        startActivity(intent);
                    } else {
                        //审核失败
//                        Intent intent = new Intent(ScienceHelpActivity.this, WithoutAnswerActivity.class);
//                        intent.putExtra("state", 2);
//                        startActivity(intent);
                        showWaringDialog(
                                "提示",
                                "审核失败，请重新提交资料审核",
                                new OnDialogButtonClickListener() {
                                    @Override
                                    public void onPositiveButtonClick() {
                                        startActivity(new Intent(ScienceHelpActivity.this, CreateKedaUserActivity.class));
                                    }

                                    @Override
                                    public void onNegativeButtonClick() {

                                    }
                                }
                        );
                    }
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    public ArrayList<AnswerAlbumBean> getmData() {
        return mData;
    }

    public void setmData(ArrayList<AnswerAlbumBean> mData) {
        this.mData = mData;
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
                    tvRecentAnswer.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 1:
                    tvEavesdropTop.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2:
                    tvAnswerAlbum.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 3:
                    tvAnswerTop.setTextColor(getResources().getColor(R.color.red));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        tvRecentAnswer.setTextColor(Color.parseColor("#999999"));
        tvEavesdropTop.setTextColor(Color.parseColor("#999999"));
        tvAnswerAlbum.setTextColor(Color.parseColor("#999999"));
        tvAnswerTop.setTextColor(Color.parseColor("#999999"));
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof ScienceHelpActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.NOTIFY_ACTIVITY_SHOW_PAY:
                //弹出支付
                keda = (NewScienceHelp) msg.getBody();
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
                break;
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    switch (currIndex) {
                        case 0:
//                            recentAnswerFragment.toHttpEavedrop(keda);
                            break;
                        case 1:
                            eavesdropTopFragment.toHttpEavedrop(keda);
                            break;
                    }
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.ADD_ZAN:
                if (DB.getInstance(DemoApplication.applicationContext).existedClickTable((String) msg.getBody())) {
                    DB.getInstance(DemoApplication.applicationContext).updataClick(1, (String) msg.getBody());
                } else {
                    clickList.add((String) msg.getBody());
                    DB.getInstance(DemoApplication.applicationContext).insertClick((String) msg.getBody());
                }
                break;
            case SystemConstant.REMOVE_ZAN:
                if (clickList.contains((String) msg.getBody())) {
                    DB.getInstance(DemoApplication.applicationContext).deleteClick((String) msg.getBody());
                    clickList.remove((String) msg.getBody());
                } else {
                    DB.getInstance(DemoApplication.applicationContext).updataClick(0, (String) msg.getBody());
                }
                break;
            case SystemConstant.NOTIFY_INFORMATION_CHANGED:
                updataMap = (Map<String, Object>) msg.getBody();
                showWaringDialog("提示", "是否保存修改的信息?", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        showLoadingDialog("正在上传");
                        toHttpUpdataAnswerInfo();
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
                break;
            case SystemConstant.EAVEDROP_COMPLITE:
                switch (currIndex) {
                    case 0:
//                        recentAnswerFragment.notifyList();
                        break;
                    case 1:
                        eavesdropTopFragment.notifyList();
                        break;
                }
                break;
            case SystemConstant.ON_KEDA_NOTICE:
                if (msg.getArg1() == 1) {
                   toGetHttpNumber();
                }
                break;
        }
    }

    private void toHttpUpdataAnswerInfo() {
        if (updataMap.get("messageurl") != null) {
//            HttpUtil.ossUpload(this, (String) updataMap.get("messageurl"), new HttpUtil.HttpOSSUploadCallBack() {
//                @Override
//                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//
//                }
//
//                @Override
//                public void onSuccess(final PutObjectRequest request, PutObjectResult result, String successUrl) {
//                    Map<String, Object> map = new HashMap<String, Object>();
////                    map.put("audiourl", successUrl.substring(successUrl.indexOf("/")));
//                    map.put("messageurl", successUrl.substring(successUrl.indexOf("/")));
//                    HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.KEDA_USER_INFORMATION_CHANGED, map, new HttpUtil.HttpCallBack() {
//                        @Override
//                        public void successResult(ResultEntity result) throws JSONException {
//                            loadingDismiss();
//                            if (result.getCode() == 200) {
//                                Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void errorResult(Throwable ex, boolean isOnCallback) {
//                            loadingDismiss();
//                            Toast.makeText(mContext, "回答失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                    loadingDismiss();
//                    Toast.makeText(mContext, "回答失败", Toast.LENGTH_SHORT).show();
//                }
//            });
            HttpUtil.upload(DemoApplication.applicationContext, SystemConstant.MYURL + SystemConstant.UPLOAD_FILE, new HttpUtil.HttpUploadCallBack() {

                @Override
                public void successResult(UploadResultEntity result) throws JSONException {
                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("audiourl", successUrl.substring(successUrl.indexOf("/")));
                    map.put("messageurl", result.getFileurl());
                    HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.KEDA_USER_INFORMATION_CHANGED, map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            loadingDismiss();
                            if (result.getCode() == 200) {
                                Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {
                            loadingDismiss();
                            Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                    loadingDismiss();
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }, new File((String) updataMap.get("messageurl")));
        } else {
            HttpUtil.post(this, SystemConstant.URL + SystemConstant.KEDA_USER_INFORMATION_CHANGED, updataMap, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    loadingDismiss();
                    if (result.getCode() == 200)
                        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                    loadingDismiss();
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 选择支付方式
     */
    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ScienceHelpActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(ScienceHelpActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(ScienceHelpActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ScienceHelpActivity.this, payPopupWindow);
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
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ScienceHelpActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ScienceHelpActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ScienceHelpActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ScienceHelpActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ScienceHelpActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥1.00,请再次确认购买");
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
                    switch (currIndex) {
                        case 0:
//                            recentAnswerFragment.toHttpEavedrop(keda);
                            break;
                        case 1:
                            eavesdropTopFragment.toHttpEavedrop(keda);
                            break;
                    }
                } else if (response.body().getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
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
                            PayTask alipay = new PayTask(ScienceHelpActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                switch (currIndex) {
                                    case 0:
//                                        recentAnswerFragment.toHttpEavedrop(keda);
                                        break;
                                    case 1:
                                        eavesdropTopFragment.toHttpEavedrop(keda);
                                        break;
                                }
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
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(ScienceHelpActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                } else {
                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<WXPay>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!Util.hintLogin(this)) {
            EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_LOGOUT);
            EventBus.getDefault().post(msg);
        } else {
            EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
            EventBus.getDefault().post(msg);
        }
        EventBus.getDefault().unregister(this);
        AudioPlayManager.getInstance().release();
        clickList.removeAll(clickList);
    }
}
