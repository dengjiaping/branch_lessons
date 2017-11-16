package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 某人的资料
 */
public class HisDataActivity extends BaseActivity {


    private TextView tvEditData;
    private AutoRelativeLayout llPersonalReturn;
    private ImageView ivHeadPortrait;
    private ImageView ivBgZoom;
    private TextView tvMyName, tvSexSelect, tvData, tvIntroduce, tvItemEducation, tvItemIndustry, tvItemOccupation;
    private boolean flag;

    @Override
    protected int setContentView() {
        return R.layout.activity_his_data;
    }

    @Override
    protected void initView() {
        tvEditData = (TextView) findViewById(R.id.tv_edit_data);
        llPersonalReturn = (AutoRelativeLayout) findViewById(R.id.ll_personal_return);
        ivHeadPortrait = (ImageView) findViewById(R.id.iv_head_portrait);
        ivBgZoom = (ImageView) findViewById(R.id.iv_bg_zoom);
        tvMyName = (TextView) findViewById(R.id.tv_my_name);
        tvSexSelect = (TextView) findViewById(R.id.tv_sex_select);
        tvData = (TextView) findViewById(R.id.tv_data);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        tvItemEducation = (TextView) findViewById(R.id.tv_item_education);
        tvItemIndustry = (TextView) findViewById(R.id.tv_item_industry);
        tvItemOccupation = (TextView) findViewById(R.id.tv_item_occupation);
    }

    @Override
    protected void initAction() {
        llPersonalReturn.setOnClickListener(this);
        tvIntroduce.setOnClickListener(this);
        toHttpGetDetail();
    }

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    if (user.getUsername().equals(null)) {
                        tvEditData.setText("他的资料");
                    } else
                        tvEditData.setText(user.getUsername() + "的资料");
                    Glide.with(mContext).load(Util.getImgUrl(user.getUserimgurl())).bitmapTransform(new CropCircleTransformation(mContext)).into(ivHeadPortrait);
                    Glide.with(mContext).load(Util.getImgUrl(user.getUserimgurl())).into(ivBgZoom);
                    Glide.with(mContext).load(Util.getImgUrl(user.getUserimgurl())).asBitmap().transform(new BlurTransformation(HisDataActivity.this)).into(ivBgZoom);

                    if (user.getUsername().equals(null)) {
                        tvMyName.setText("");
                    } else
                        tvMyName.setText(user.getUsername());
                    if (user.getGender() == 0) {
                        tvSexSelect.setText("保密");
                    } else if (user.getGender() == 1) {
                        tvSexSelect.setText("男");
                    } else if (user.getGender() == 2) {
                        tvSexSelect.setText("女");
                    } else {
                        tvSexSelect.setText("");
                    }
                    if (user.getBirthday().equals(null)) {
                        tvData.setText("");
                    } else
                        tvData.setText(user.getBirthday());
                    if (user.getMysign().equals(null)) {
                        tvIntroduce.setText("让更多的人来认识你吧");
                    } else {
                        tvIntroduce.setText(user.getMysign() + "");
                    }
                    if (user.getDegree().equals(null)) {
                        tvItemEducation.setText("");
                    } else
                        tvItemEducation.setText(user.getDegree());
                    if (user.getProfession().equals(null)) {
                        tvItemIndustry.setText("");
                    } else
                        tvItemIndustry.setText(user.getProfession());
                    if (user.getPosition().equals(null)) {
                        tvItemOccupation.setText("");
                    } else
                        tvItemOccupation.setText(user.getPosition());
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
            case R.id.ll_personal_return:
                finish();
                break;
            case R.id.tv_introduce:
                if (flag) {
                    flag = false;
                    tvIntroduce.setMaxLines(100);
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.START); // 展开
//                    tvPersonalIntroduce.setSingleLine(flag);
                } else {
                    flag = true;
//                    tvPersonalIntroduce.setSingleLine(flag);
//                    tvPersonalIntroduce.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    tvIntroduce.setMaxLines(1);
                }
                break;
        }
    }
}
