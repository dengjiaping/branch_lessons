package com.yidiankeyan.science.my.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.my.adapter.PersonalDataListAdapter;
import com.yidiankeyan.science.my.entity.BirthData;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.calendar.utils.TimeUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 用户个人信息设置
 */
public class PersonalDataActivity extends BaseActivity {

    private ImageView imgpersonalReturn;
    private TextView tvEditData;
    private ImageView imgHeadPortrait;
    private ImageView icnAmera;
    private ImageView ivBgZoom;
    private PopupWindow avatarPopupWindow;
    private AutoLinearLayout llPersonal;
    private AutoRelativeLayout llSex;
    private TextView tvMan;
    private TextView tvGirl;
    private TextView tvSecrecy;
    private TextView tvSexSelect;
    private AutoRelativeLayout llModifyName, llBirthData, llEducation, llIndustry, llOccupation, rlPersonality;
    private PopupWindow mPopupWindow;
    private PopupWindow educationPopupWindow;
    private PopupWindow dataPopupWindow;
    private PopupWindow industryPopupWindow;
    private PopupWindow occupationPopupWindow;
    private PopupWindow showSexPopupWindow;
    private PopupWindow OptionalPopuWindow;
    private PopupWindow OptIndustryPopuWindow;
    private TextView tvAssign;
    private TextView tvFinsh;
    private TextView tvDetermine;
    private TextView tvCancel;
    private TextView tvMyName;
    private TextView tvData;
    private TextView tvItemEducation;
    private TextView tvItemIndustry;
    private TextView tvItemOccupation;
    private EditText etCustomPrice;
    private EditText etOptionalPrice;
    private TextView tvCancels;
    private TextView tvDetermines;
    private EditText etOptionalPrices;
    private TextView tvIntroduce;
    private AutoRelativeLayout rlHeadPortrait, rlPersonalReturn;
    public static final int REQUEST_CODE_PICTURE = 101;
    public static final int REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_CUT = 103;
    private Uri imageUri;
    private Uri imageCropUri;

    private ListView lvData;
    private ListView educationLv;
    private PersonalDataListAdapter dataListAdapter;
    private PersonalDataListAdapter educationAdapter;
    private PersonalDataListAdapter industryAdapter;
    private PersonalDataListAdapter occupationAdapter;
    //    private EditText edtPersonalityAutograph;
    private List<BirthData> listData = new ArrayList<>();
    private String[] education = {"高中及以下", "专科", "本科", "研究生", "博士及以上"};
    private String[] industry = {
            "未毕业", "互联网", "IT", "制造业", "金融", "教育", "医药医疗",
            "建筑房地产", "传媒", "政府机关", "我自己填",
    };
    private String[] occupation = {
            "学生", "创业者", "程序猿", "工程师", "销售", "运营", "企业老板",
            "中高层管理者", "普通白领", "公务员", "事业单位员工", "我自己填",
    };
    private List<BirthData> listIndustry = new ArrayList<>();
    private List<BirthData> listOccupation = new ArrayList<>();
    private List<BirthData> listEducation = new ArrayList<>();
    private boolean avatarIsChanged;
    private int gender;
    private InputMethodManager imm;
    private String introduction = "";

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_personal_data;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initView() {
        imgpersonalReturn = (ImageView) findViewById(R.id.personal_return);
        tvEditData = (TextView) findViewById(R.id.tv_edit_data);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imgHeadPortrait = (ImageView) findViewById(R.id.iv_head_portrait);
        icnAmera = (ImageView) findViewById(R.id.icn_amera);
        llPersonal = (AutoLinearLayout) findViewById(R.id.ll_personal);
        llSex = (AutoRelativeLayout) findViewById(R.id.ll_sex);
        rlPersonality = (AutoRelativeLayout) findViewById(R.id.rl_personality_autograph);
        rlPersonalReturn = (AutoRelativeLayout) findViewById(R.id.rl_personal_return);
        tvSexSelect = (TextView) findViewById(R.id.tv_sex_select);
        llModifyName = (AutoRelativeLayout) findViewById(R.id.ll_modify_name);
        tvFinsh = (TextView) findViewById(R.id.tv_finsh_edit);
        tvMyName = (TextView) findViewById(R.id.tv_my_name);
        llBirthData = (AutoRelativeLayout) findViewById(R.id.ll_birth_data);
        ivBgZoom = (ImageView) findViewById(R.id.iv_bg_zoom);
        tvData = (TextView) findViewById(R.id.tv_data);
        llEducation = (AutoRelativeLayout) findViewById(R.id.ll_education);
        tvItemEducation = (TextView) findViewById(R.id.tv_item_education);
        llIndustry = (AutoRelativeLayout) findViewById(R.id.ll_industry);
        tvItemIndustry = (TextView) findViewById(R.id.tv_item_industry);
        llOccupation = (AutoRelativeLayout) findViewById(R.id.ll_occupation);
        tvItemOccupation = (TextView) findViewById(R.id.tv_item_occupation);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
//        edtPersonalityAutograph= (EditText) findViewById(R.id.edt_personality_autograph);
        rlHeadPortrait = (AutoRelativeLayout) findViewById(R.id.rl_head_portrait);
    }

    @Override
    protected void initAction() {
        tvEditData.setText("编辑资料");
        initData();
        imgHeadPortrait.setOnClickListener(this);
        icnAmera.setOnClickListener(this);
        llSex.setOnClickListener(this);
        llModifyName.setOnClickListener(this);
        llBirthData.setOnClickListener(this);
        llEducation.setOnClickListener(this);
        llIndustry.setOnClickListener(this);
        llOccupation.setOnClickListener(this);
//        llPersonal.setOnClickListener(this);
//        rlHeadPortrait.setOnClickListener(this);
        rlPersonality.setOnClickListener(this);
        rlPersonalReturn.setOnClickListener(this);
        for (int i = 0; i < education.length; i++) {
            listEducation.add(new BirthData(false, "" + education[i]));
        }
        for (int i = 0; i < industry.length; i++) {
            listIndustry.add(new BirthData(false, "" + industry[i]));
        }
        for (int i = 0; i < occupation.length; i++) {
            listOccupation.add(new BirthData(false, "" + occupation[i]));
        }
        boolean b = false;
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "birthday")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "birthday"))) {
            b = true;
        }
        for (int i = 1900; i < TimeUtils.getCurrentYear(); i++) {
            if (b && TextUtils.equals(i + "", SpUtils.getStringSp(this, "birthday").substring(0, SpUtils.getStringSp(this, "birthday").indexOf("/")))) {
                listData.add(new BirthData(true, "" + i));
            } else
                listData.add(new BirthData(false, "" + i));
        }
    }

    private void initData() {
        String path = Util.getSDCardPath();
        File file = new File(path + "/temp_avatar.jpg");
        imageUri = Util.getUriForFile(this, file);
        File cropFile = new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg");
        imageCropUri = Uri.fromFile(new File(Util.getSDCardPath() + "/temp_avatar_crop2.jpg"));
        if (Util.fileExisted(cropFile)) {
            Glide.with(this).load(Util.getSDCardPath() + "/temp_avatar_crop.jpg").skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(this)).into(imgHeadPortrait);
//            Glide.with(this).load(Util.getSDCardPath() + "/temp_avatar_crop.jpg").skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivBgZoom);
//            Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + SpUtils.getStringSp(this, "userimgurl")).asBitmap().transform(new BlurTransformation(this)).into(ivBgZoom);
        }
        tvMyName.setText(SpUtils.getStringSp(this, "userName"));
        switch (SpUtils.getIntSp(this, "gender")) {
            case 0:
                gender = 0;
                tvSexSelect.setText("保密");
                break;
            case 1:
                gender = 1;
                tvSexSelect.setText("男");
                break;
            case 2:
                gender = 2;
                tvSexSelect.setText("女");
                break;
        }
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "birthday")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "birthday"))) {
            tvData.setText(SpUtils.getStringSp(this, "birthday").substring(0, SpUtils.getStringSp(this, "birthday").indexOf("/")));
        } else
            tvData.setText("");
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "degree")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "degree"))) {
//            tvItemEducation.setText(SpUtils.getStringSp(this, "degree").substring(0, SpUtils.getStringSp(this, "degree").indexOf("/")));
            tvItemEducation.setText(SpUtils.getStringSp(this, "degree"));
        } else
            tvItemEducation.setText("");
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "profession")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "profession"))) {
            tvItemIndustry.setText(SpUtils.getStringSp(this, "profession"));
//            tvItemIndustry.setText(SpUtils.getStringSp(this, "profession").substring(0, SpUtils.getStringSp(this, "profession").indexOf("/")));
        } else
            tvItemIndustry.setText("");
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "position")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "position"))) {
            tvItemOccupation.setText(SpUtils.getStringSp(this, "position"));
//            tvItemOccupation.setText(SpUtils.getStringSp(this, "position").substring(0, SpUtils.getStringSp(this, "position").indexOf("/")));
        } else
            tvItemOccupation.setText("");
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "mysign")) && !TextUtils.equals("null", SpUtils.getStringSp(this, "mysign"))) {
            tvIntroduce.setText(SpUtils.getStringSp(this, "mysign"));
//            tvItemOccupation.setText(SpUtils.getStringSp(this, "position").substring(0, SpUtils.getStringSp(this, "position").indexOf("/")));
        } else if (TextUtils.isEmpty(tvIntroduce.getText())) {
            tvIntroduce.setText("让更多的人来认识你吧");
        }

//        tvItemEducation.setText(SpUtils.getStringSp(this, "degree"));
//        tvItemIndustry.setText(SpUtils.getStringSp(this, "profession"));
//        tvItemOccupation.setText(SpUtils.getStringSp(this, "position"));
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.rl_personal_return:
                finish();
                break;
//            case R.id.ll_personal:
//                imm.hideSoftInputFromWindow(edtPersonalityAutograph.getWindowToken(),0);
//                break;
//            case R.id.rl_head_portrait:
//                imm.hideSoftInputFromWindow(edtPersonalityAutograph.getWindowToken(),0);
//                break;
            case R.id.ll_modify_name:
//                showCustomPop();
                Intent intent = new Intent(this, MyUpdateNameActivity.class);
                intent.putExtra("userName", tvMyName.getText().toString());
                startActivity(intent);
                break;
//            case R.id.tv_finsh_edit:
//                finishPop(mPopupWindow);
//                break;
//            case R.id.tv_assign:
//                if (!TextUtils.isEmpty(etCustomPrice.getText())) {
//                    finishPop(mPopupWindow);
//                    tvMyName.setText(etCustomPrice.getText());
//                } else {
//                    Toast.makeText(PersonalDataActivity.this, "请输入您的昵称", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.iv_head_portrait:
                //点击头像选择照相或者相册功能
                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        card();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(PersonalDataActivity.this, "无法选择头像", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.icn_amera:
                //点击头像选择照相或者相册功能
                Acp.getInstance(DemoApplication.applicationContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        card();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(PersonalDataActivity.this, "无法选择头像", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.ll_sex:
                sex();
                break;
            case R.id.man_btn:
                tvSexSelect.setText("男");
                gender = 1;
                Util.finishPop(PersonalDataActivity.this, showSexPopupWindow);
                break;
            case R.id.girl_btn:
                tvSexSelect.setText("女");
                gender = 2;
                Util.finishPop(PersonalDataActivity.this, showSexPopupWindow);
                break;
            case R.id.tv_secrecy_btn:
                tvSexSelect.setText("保密");
                gender = 0;
                Util.finishPop(PersonalDataActivity.this, showSexPopupWindow);
                break;
            case R.id.ll_birth_data:
                showPop();
                break;
            case R.id.rl_personality_autograph:
                Intent intent1 = new Intent(this, BriefIntroductionActivity.class);
                if (!TextUtils.equals("null", introduction) && !TextUtils.isEmpty(introduction)) {
                    intent1.putExtra("preservation", introduction);
                } else {
                    intent1.putExtra("preservation", tvIntroduce.getText().toString());
                }
                startActivity(intent1);
                break;
            case R.id.ll_education:
                education();
                break;
            case R.id.ll_industry:
                industry();
                break;
            case R.id.ll_occupation:
                occupation();
                break;
            case R.id.tv_optional_finsh:
                finishPop(OptionalPopuWindow);
                break;
            case R.id.tv_optional_assign:
                if (!TextUtils.isEmpty(etOptionalPrice.getText())) {
                    finishPop(OptionalPopuWindow);
                    tvItemOccupation.setText(etOptionalPrice.getText());
                } else {
                    Toast.makeText(PersonalDataActivity.this, "请输入您的职业", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_opt_industry_finsh:
                finishPop(OptIndustryPopuWindow);
                break;
            case R.id.tv_opt_industry_assign:
                if (!TextUtils.isEmpty(etOptionalPrices.getText())) {
                    finishPop(OptIndustryPopuWindow);
                    tvItemIndustry.setText(etOptionalPrices.getText());
                } else {
                    Toast.makeText(PersonalDataActivity.this, "请输入您的职业", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 置顶、置底
     *
     * @param msg
     */
    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_BRIEF_INTRODUCTION:
                introduction = msg.getBody().toString();
                tvIntroduce.setText(msg.getBody().toString() + "");
                break;
            case SystemConstant.USER_UPDATE_NAME:
                tvMyName.setText(msg.getBody().toString() + "");
                break;
        }
    }


    private void occupation() {
        if (occupationPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_data, null);
            educationLv = (ListView) view.findViewById(R.id.lv_data_list);
            occupationAdapter = new PersonalDataListAdapter(listOccupation, this);
            educationLv.setAdapter(occupationAdapter);
            occupationPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            occupationPopupWindow.setContentView(view);
            occupationPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            occupationPopupWindow.setFocusable(true);
            occupationPopupWindow.setOutsideTouchable(true);
            occupationPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            occupationPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
            occupationPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(PersonalDataActivity.this, occupationPopupWindow);
                }
            });
            educationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listOccupation.get(position).setTag(false);
                    occupationAdapter.notifyDataSetChanged();
                    tvItemOccupation.setText(listOccupation.get(position).getYear());
                    finishPop(occupationPopupWindow);
                    if (listOccupation.get(position).getYear().equals("我自己填")) {
                        tvItemOccupation.setText("");
                        optional();
                    }
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            occupationPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
    }

    private void industry() {
        if (industryPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_data, null);
            educationLv = (ListView) view.findViewById(R.id.lv_data_list);
            industryAdapter = new PersonalDataListAdapter(listIndustry, this);
            educationLv.setAdapter(industryAdapter);
            industryPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            industryPopupWindow.setContentView(view);
            industryPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            industryPopupWindow.setFocusable(true);
            industryPopupWindow.setOutsideTouchable(true);
            industryPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            industryPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
            industryPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(PersonalDataActivity.this, industryPopupWindow);
                }
            });
            educationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listIndustry.get(position).setTag(false);
                    industryAdapter.notifyDataSetChanged();
                    tvItemIndustry.setText(listIndustry.get(position).getYear());
                    finishPop(industryPopupWindow);
                    if (listIndustry.get(position).getYear().equals("我自己填")) {
                        tvItemIndustry.setText("");
                        optIndustry();
                    }
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            industryPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
    }


    private void education() {
        if (educationPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_data, null);
            educationLv = (ListView) view.findViewById(R.id.lv_data_list);
            educationAdapter = new PersonalDataListAdapter(listEducation, this);
            educationLv.setAdapter(educationAdapter);
            educationPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            educationPopupWindow.setContentView(view);
            educationPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            educationPopupWindow.setFocusable(true);
            educationPopupWindow.setOutsideTouchable(true);
            educationPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            educationPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
            educationPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(PersonalDataActivity.this, educationPopupWindow);
                }
            });
            educationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listEducation.get(position).setTag(false);
                    educationAdapter.notifyDataSetChanged();
                    tvItemEducation.setText(listEducation.get(position).getYear());
                    finishPop(educationPopupWindow);

                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            educationPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
    }

    private void showPop() {
        if (dataPopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_data, null);
            lvData = (ListView) view.findViewById(R.id.lv_data_list);
            dataListAdapter = new PersonalDataListAdapter(listData, this);
            lvData.setAdapter(dataListAdapter);
            dataPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dataPopupWindow.setContentView(view);
            dataPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            dataPopupWindow.setFocusable(true);
            dataPopupWindow.setOutsideTouchable(true);
            dataPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            dataPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
            dataPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(PersonalDataActivity.this, dataPopupWindow);
                }
            });
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (BirthData data : listData) {
                        data.setTag(false);
                    }
                    listData.get(position).setTag(true);
                    dataListAdapter.notifyDataSetChanged();
                    tvData.setText(listData.get(position).getYear());
                    finishPop(dataPopupWindow);
//                    listData.removeAll(listData);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            dataPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
//        String date = SpUtils.getStringSp(this, "birthday");
//        String year = null;
//        String month = null;
//        String day = null;
//        if (!TextUtils.isEmpty(date) && !TextUtils.equals("null", date)) {
//            year = date.split("-")[0];
//            month = date.split("-")[1];
//            day = date.split("-")[2];
//        } else {
//            year = TimeUtils.getCurrentYear() + "";
//            month = TimeUtils.getCurrentMonth() + "";
//            day = TimeUtils.getCurrentDay() + "";
//        }
//        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(this, new DatePickerPopWin.OnDatePickedListener() {
//            @Override
//            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            }
//        }).textConfirm("确认")
//                .textCancel("取消")
//                .btnTextSize(16)
//                .viewTextSize(25)
//                .colorCancel(Color.parseColor("#999999"))
//                .colorConfirm(Color.parseColor("#009900"))
//                .minYear(TimeUtils.getTimeByPosition(100, 1900, 1, "year"))
//                .maxYear(TimeUtils.getTimeByPosition(900, TimeUtils.getCurrentYear(), TimeUtils.getCurrentMonth(), "year"))
//                .dateChose(year + "-" + month + day)
//                .build();
//        pickerPopWin.showPopWin(this);
    }

    public void card() {
//        filepath = new File(Environment.getExternalStorageDirectory(), "temp_avatar" + ".png");
        if (avatarPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_picture_popup, null);
            view.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    Util.finishPop(PersonalDataActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                    Util.finishPop(PersonalDataActivity.this, avatarPopupWindow);
                }
            });
            view.findViewById(R.id.cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(PersonalDataActivity.this, avatarPopupWindow);
                }
            });
            avatarPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            avatarPopupWindow.setContentView(view);
            avatarPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            avatarPopupWindow.setFocusable(true);
            avatarPopupWindow.setOutsideTouchable(true);
            avatarPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            avatarPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(PersonalDataActivity.this, avatarPopupWindow);
                }
            });
            avatarPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            avatarPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
    }

    public void sex() {
        if (showSexPopupWindow == null) {
            View view = View.inflate(this, R.layout.select_sex_popup, null);
            tvMan = (TextView) view.findViewById(R.id.man_btn);
            tvGirl = (TextView) view.findViewById(R.id.girl_btn);
            tvSecrecy = (TextView) view.findViewById(R.id.tv_secrecy_btn);
            tvMan.setOnClickListener(this);
            tvGirl.setOnClickListener(this);
            tvSecrecy.setOnClickListener(this);
            view.findViewById(R.id.sex_cancle_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(PersonalDataActivity.this, showSexPopupWindow);
                }
            });
            showSexPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, -2);
            showSexPopupWindow.setContentView(view);
            showSexPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            showSexPopupWindow.setFocusable(true);
            showSexPopupWindow.setOutsideTouchable(true);
            showSexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            showSexPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    Util.finishPop(PersonalDataActivity.this, showSexPopupWindow);
                }
            });
            showSexPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            showSexPopupWindow.showAtLocation(llPersonal, Gravity.BOTTOM, 0, 0);
        }
    }


    private void showCustomPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_set_name, null);
            tvFinsh = (TextView) view.findViewById(R.id.tv_finsh_edit);
            tvAssign = (TextView) view.findViewById(R.id.tv_assign);
            etCustomPrice = (EditText) view.findViewById(R.id.et_set_price);
//            tvAssign.setOnClickListener(this);
//            tvFinsh.setOnClickListener(this);
            etCustomPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            etCustomPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
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
            mPopupWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
        } else {
            etCustomPrice.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
        }
    }

//    private InputFilter filter=new InputFilter() {
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            if(source.equals(" ")||source.toString().contentEquals("\n"))return "";
//            else return null;
//        }
//    };

    private void optional() {
        if (OptionalPopuWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_optional_occupation, null);
            tvCancel = (TextView) view.findViewById(R.id.tv_optional_finsh);
            tvDetermine = (TextView) view.findViewById(R.id.tv_optional_assign);
            etOptionalPrice = (EditText) view.findViewById(R.id.et_optional_price);
            tvDetermine.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
            etOptionalPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            OptionalPopuWindow = new PopupWindow(view, -2, -2);
            OptionalPopuWindow.setContentView(view);
            OptionalPopuWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            OptionalPopuWindow.setFocusable(true);
            OptionalPopuWindow.setOutsideTouchable(true);
            OptionalPopuWindow.setBackgroundDrawable(new BitmapDrawable());
            OptionalPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
//                    tvItemOccupation.setText("未设置");
                    finishPop(OptionalPopuWindow);
                }
            });
            OptionalPopuWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
        } else {
            etOptionalPrice.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            OptionalPopuWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
        }
    }

    private void optIndustry() {
        if (OptIndustryPopuWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_optional_industry, null);
            tvCancels = (TextView) view.findViewById(R.id.tv_opt_industry_finsh);
            tvDetermines = (TextView) view.findViewById(R.id.tv_opt_industry_assign);
            etOptionalPrices = (EditText) view.findViewById(R.id.et_optional_industry);
            tvDetermines.setOnClickListener(this);
            tvCancels.setOnClickListener(this);
            etOptionalPrices.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            OptIndustryPopuWindow = new PopupWindow(view, -2, -2);
            OptIndustryPopuWindow.setContentView(view);
            OptIndustryPopuWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            OptIndustryPopuWindow.setFocusable(true);
            OptIndustryPopuWindow.setOutsideTouchable(true);
            OptIndustryPopuWindow.setBackgroundDrawable(new BitmapDrawable());
            OptIndustryPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
//                    tvItemIndustry.setText("未设置");
                    finishPop(OptIndustryPopuWindow);
                }
            });
            OptIndustryPopuWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
        } else {
            etOptionalPrices.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            OptIndustryPopuWindow.showAtLocation(llPersonal, Gravity.CENTER, 0, 0);
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    private void openCamera() {
        String path = Util.getSDCardPath();
        File file = new File(path + "/temp_avatar.jpg");
        imageUri = Util.getUriForFile(this, file);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    cropImg();
                }
                break;
            case REQUEST_CODE_CUT:
                if (resultCode == Activity.RESULT_OK) {
                    if (getTempFile() != null) {
                        avatarIsChanged = true;
                        Glide.with(this).load(Util.getSDCardPath() + "/temp_avatar_crop2.jpg").skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CropCircleTransformation(this)).into(imgHeadPortrait);
                    }
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
        }
        cropImg();
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        cropImg();
    }

    public void cropImg() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CUT);
    }

    private File getTempFile() {
        try {
            return new File(Util.getSDCardPath() + "/temp_avatar_crop.jpg");
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map<String, String> map = new HashMap<>();
        if (avatarIsChanged)
            map.put("avatarIsChanged", "1");
        map.put("username", tvMyName.getText().toString());
        map.put("gender", gender + "");
        if (tvIntroduce.getText().toString().equals("让更多的人来认识你吧")) {
            map.put("mysign", null);
        } else {
            map.put("mysign", tvIntroduce.getText().toString());
        }
        map.put("degree", tvItemEducation.getText().toString());
        map.put("profession", tvItemIndustry.getText().toString());
        map.put("position", tvItemOccupation.getText().toString());
        if (!TextUtils.isEmpty(tvData.getText().toString()) && !TextUtils.equals(tvData.getText().toString(), "null"))
            map.put("birthday", tvData.getText().toString() + "/01/01");
        EventMsg msg = EventMsg.obtain(SystemConstant.NOTIFY_USER_INFORMATION_CHANGED);
        msg.setBody(map);
        EventBus.getDefault().post(msg);
    }
}
