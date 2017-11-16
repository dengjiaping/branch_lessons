package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.MozAudioBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.utils.Util;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2017/7/15.
 * 作用：
 */

public class MozAudioAdapter extends RecyclerAdapter<MozAudioBean> {
    public MozAudioAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<MozAudioBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<MozAudioBean> {
        private ImageView imgAvatar;
        private TextView tvSubscribed;
        private TextView tvUpdateNum;
        private TextView tvName;


        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_moz_audio);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            tvSubscribed = (TextView) findViewById(R.id.tv_subscribed);
            tvUpdateNum = (TextView) findViewById(R.id.tv_update_num);
            tvName = (TextView) findViewById(R.id.tv_name);
        }

        @Override
        public void setData(MozAudioBean data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getCoverimgurl())).into(imgAvatar);
            tvName.setText(data.getAlbumname());
            if (data.getUpdates() > 0) {
                tvUpdateNum.setText(data.getUpdates()+"");
                tvUpdateNum.setVisibility(View.VISIBLE);
            } else {
                tvUpdateNum.setVisibility(View.GONE);
            }
            if (data.getIsorder() == 1) {
                tvSubscribed.setVisibility(View.VISIBLE);
            } else {
                tvSubscribed.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemViewClick(MozAudioBean data) {
            super.onItemViewClick(data);
            Intent intent = new Intent(getContext(), AudioAlbumActivity.class);
            intent.putExtra("albumId", data.getAlbumid());
            getContext().startActivity(intent);
        }
    }
}


