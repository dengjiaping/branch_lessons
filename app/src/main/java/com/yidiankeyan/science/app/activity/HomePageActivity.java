package com.yidiankeyan.science.app.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.my.activity.HisAlbumActivity;
import com.yidiankeyan.science.my.activity.MyFansActivity;
import com.yidiankeyan.science.my.activity.MyFollowActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 主页
 */
public class HomePageActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private ImageView imgFollow, imgFanQuantity;
    private TextView tvFullText, tvPersonalIntroduce;
    private boolean flag;
    private AutoRelativeLayout llHisAlbum;
    private TextView tvFocusCount;
    private TextView tvFansCount;
    private ImageView ivHeadPortrait;
    private TextView tvNickName;
    private TextView tvNumber;
    private TextView tvMyAlnum, tvHeAlnum;
    private String userId;
    private String userName;
    private TextView tvFocusFollow, tvAlreadyFollow;
    private TextView tvDegree, tvPosition, tvProfession;
    private Intent intent;
    private boolean isFu = true;
    private TextView tvFinish, tvYesClick;
    private AutoLinearLayout llHome;
    private PopupWindow homePopupWindow;
    private int isFollow = 0;

    @Override
    protected int setContentView() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        imgFollow = (ImageView) findViewById(R.id.img_follow);
        imgFanQuantity = (ImageView) findViewById(R.id.img_fan_quantity);
        tvFullText = (TextView) findViewById(R.id.tv_full_text);
        tvPersonalIntroduce = (TextView) findViewById(R.id.tv_personal_introduce);
        llHisAlbum = (AutoRelativeLayout) findViewById(R.id.ll_his_album);
        tvFocusCount = (TextView) findViewById(R.id.tv_focus_count);
        tvFansCount = (TextView) findViewById(R.id.tv_fans_count);
        ivHeadPortrait = (ImageView) findViewById(R.id.iv_head_portrait);
        tvNickName = (TextView) findViewById(R.id.tv_nick_name);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvFocusFollow = (TextView) findViewById(R.id.tv_focus_follow);
        tvAlreadyFollow = (TextView) findViewById(R.id.tv_already_follow);
        tvMyAlnum = (TextView) findViewById(R.id.tv_my_alnum);
        tvHeAlnum = (TextView) findViewById(R.id.tv_he_alnum);
        tvDegree = (TextView) findViewById(R.id.tv_degree);
        tvPosition = (TextView) findViewById(R.id.tv_position);
        tvProfession = (TextView) findViewById(R.id.tv_profession);
        llHome = (AutoLinearLayout) findViewById(R.id.ll_home);
    }

    @Override
    protected void initAction() {
        txtTitle.setVisibility(View.GONE);
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        imgFollow.setOnClickListener(this);
        imgFanQuantity.setOnClickListener(this);
        tvFullText.setOnClickListener(this);
        llHisAlbum.setOnClickListener(this);
        tvFocusFollow.setOnClickListener(this);
        tvAlreadyFollow.setOnClickListener(this);
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        if (TextUtils.equals(userId, SpUtils.getStringSp(this, "userId"))) {
            tvFocusFollow.setVisibility(View.GONE);
            tvHeAlnum.setVisibility(View.GONE);
            tvMyAlnum.setVisibility(View.VISIBLE);
        } else {
            tvHeAlnum.setVisibility(View.VISIBLE);
            tvMyAlnum.setVisibility(View.GONE);
            Map<String, Object> map = new HashMap<>();
            map.put("id", userId);
            HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
                @Override
                public void successResult(ResultEntity result) throws JSONException {
                    if (result.getCode() == 200) {
                        UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                        if (user.getIsfocus() == 0) {
                            tvFocusFollow.setVisibility(View.VISIBLE);
                            tvAlreadyFollow.setVisibility(View.GONE);
                        } else {
                            tvFocusFollow.setVisibility(View.GONE);
                            tvAlreadyFollow.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void errorResult(Throwable ex, boolean isOnCallback) {
                }
            });
//            Map<String, Object> map = new HashMap<>();
//            map.put("pages", 1);
//            map.put("pagesize", 20);
//            HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALL_ATTENTION_ANSWER, map, new HttpUtil.HttpCallBack() {
//                @Override
//                public void successResult(ResultEntity result) throws JSONException {
//                    if (200 == result.getCode()) {
//                        List<AnswerPeopleBean> datas = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AnswerPeopleBean.class);
//                        for (int i = 0; i < datas.size(); i++) {
//                            if (datas.get(i).getId().equals(userId)) {
//                                Toast.makeText(HomePageActivity.this, "您已关注过他", Toast.LENGTH_SHORT).show();
//                            } else {
//
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void errorResult(Throwable ex, boolean isOnCallback) {
//
//                }
//            });
        }
        toHttpGetUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void toHttpGetUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    tvFansCount.setText(user.getFollowernum() + "");
                    tvFocusCount.setText(user.getFocusnum() + "");
                    if (user.getUsername().equals(null)) {
                        if (!userName.equals(null)) {
                            tvNickName.setText(userName);
                        } else {
                            tvNickName.setText("");
                        }
                    } else {
                        tvNickName.setText(user.getUsername());
                    }
                    tvNumber.setText(user.getSizeid());
                    if (user.getMysign() == null) {
                        user.setMysign("");
                    } else {
                        tvPersonalIntroduce.setText(user.getMysign() + "");
                    }
                    if (TextUtils.isEmpty(user.getDegree())) {
                        tvDegree.setText("学历");
                    } else {
                        tvDegree.setText("学历：" + user.getDegree());
                    }
                    if (TextUtils.isEmpty(user.getPosition())) {
                        tvProfession.setText("职业");
                    } else {
                        tvProfession.setText("职业：" + user.getPosition());
                    }
                    if (TextUtils.isEmpty(user.getProfession())) {
                        tvPosition.setText("行业");
                    } else {
                        tvPosition.setText("行业：" + user.getProfession());
                    }
                    Glide.with(mContext).load(Util.getImgUrl(user.getUserimgurl())).bitmapTransform(new CropCircleTransformation(mContext)).into(ivHeadPortrait);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.img_follow:
                intent = new Intent(this, MyFollowActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.img_fan_quantity:
                intent = new Intent(this, MyFansActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.tv_full_text:
                if (flag) {
                    flag = false;
                    tvPersonalIntroduce.setMaxLines(100);
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.START); // 展开
//                    tvPersonalIntroduce.setSingleLine(flag);
                } else {
                    flag = true;
//                    tvPersonalIntroduce.setSingleLine(flag);
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    tvPersonalIntroduce.setMaxLines(2);
                }
                break;
            case R.id.ll_his_album:
                intent = new Intent(this, HisAlbumActivity.class);
                intent.putExtra("isFo", tvMyAlnum.getText());
                intent.putExtra("id", userId);
                startActivity(intent);
                break;
            case R.id.tv_focus_follow:
                Map<String, Object> map = new HashMap<>();
                map.put("targetid", userId);
                map.put("oparetion", 1);
                HttpUtil.post(HomePageActivity.this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
                            isFollow = 1;
                            tvFocusFollow.setVisibility(View.GONE);
                            tvAlreadyFollow.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                break;
            case R.id.tv_already_follow:
                if (isFu) {
                    FollowCustomPop();
                } else {
                    isFu = true;
                    tvAlreadyFollow.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_no_finish:
                finishPop(homePopupWindow);
                tvAlreadyFollow.setVisibility(View.VISIBLE);
                isFu = true;
                break;
            case R.id.tv_yes_onclick:
                finishPop(homePopupWindow);
                Map<String, Object> maps = new HashMap<>();
                maps.put("targetid", userId);
                maps.put("oparetion", 0);
                HttpUtil.post(HomePageActivity.this, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, maps, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {
                        if (result.getCode() == 200) {
                            isFollow = 2;
                            tvFocusFollow.setVisibility(View.VISIBLE);
                            tvAlreadyFollow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                isFu = false;
                break;
        }
    }

    private void FollowCustomPop() {
        if (homePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            homePopupWindow = new PopupWindow(view, -2, -2);
            homePopupWindow.setContentView(view);
            homePopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            homePopupWindow.setFocusable(true);
            homePopupWindow.setOutsideTouchable(true);
            homePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            homePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(homePopupWindow);
                }
            });
            homePopupWindow.showAtLocation(llHome, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            homePopupWindow.showAtLocation(llHome, Gravity.CENTER, 0, 0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFollow == 1 || isFollow == 2) {
            Intent intent = new Intent();
            intent.setAction("action.home.refreshFriend");
            sendBroadcast(intent);
        }
    }
}
