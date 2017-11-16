package com.yidiankeyan.science.my.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 邀请好友
 */
public class InvitationFriendsActivity extends BaseActivity {

    private String a;
    private AutoLinearLayout titleReturn;
    private ImageView imgInvitationBg;
    private TextView tvScienceNumber;
    private ImageView imgShareWx,imgShareFriendCircle,imgShareQQ,imgShareSina;

    @Override
    protected int setContentView() {
        return R.layout.activity_invitation_friends;
    }

    @Override
    protected void initView() {
        titleReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        imgInvitationBg = (ImageView) findViewById(R.id.img_invitation_bg);
        tvScienceNumber= (TextView) findViewById(R.id.tv_science_number);
        imgShareWx = (ImageView) findViewById(R.id.img_share_wx);
        imgShareFriendCircle = (ImageView) findViewById(R.id.img_share_friend_circle);
        imgShareQQ = (ImageView) findViewById(R.id.img_share_qq);
        imgShareSina = (ImageView) findViewById(R.id.img_share_sina);
    }

    @Override
    protected void initAction() {
        titleReturn.setOnClickListener(this);
        a = String.valueOf((int) (Math.random() * 10 + 1));
        Glide.with(this).load(SystemConstant.ACCESS_IMG_HOST + "/inviteshare/" + a + ".jpg").crossFade().into(imgInvitationBg);
        tvScienceNumber.setText(SpUtils.getStringSp(this, "scienceNum"));
        imgShareWx.setOnClickListener(this);
        imgShareFriendCircle.setOnClickListener(this);
        imgShareQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.img_share_wx:
                shareWeb(
                        SHARE_MEDIA.WEIXIN,
                        "我正在使用【墨子学堂】，推荐给你",
                        "墨子学堂",
                        SystemConstant.ACCESS_IMG_HOST + "/inviteshare/" + a + ".jpg",
                        SystemConstant.MYURL+"share/invite/"+SpUtils.getStringSp(this, "scienceNum")+"/"+a
                        , null);
                break;
            case R.id.img_share_friend_circle:
                shareWeb(
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        "我正在使用【墨子学堂】，推荐给你",
                        "墨子学堂",
                        SystemConstant.ACCESS_IMG_HOST + "/inviteshare/" + a + ".jpg",
                        SystemConstant.MYURL+"share/invite/"+SpUtils.getStringSp(this, "scienceNum")+"/"+a, null);
                break;
            case R.id.img_share_qq:
                shareWeb(
                        SHARE_MEDIA.QQ,
                        "我正在使用【墨子学堂】，推荐给你",
                        "墨子学堂",
                        SystemConstant.ACCESS_IMG_HOST + "/inviteshare/" + a + ".jpg",
                        SystemConstant.MYURL+"share/invite/"+SpUtils.getStringSp(this, "scienceNum")+"/"+a, null);
                break;
        }
    }
}
