package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.MozReadBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.GlideRoundTransform;

import java.text.DecimalFormat;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/28 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                墨子读书
 * //       █▓▓▓▓██◤                        -全部列表Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozInformationAllAdapter extends RecyclerAdapter<MozReadBean.ListBean> {
    private int isPrice;

    public MozInformationAllAdapter(Context context) {
        super(context);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder<MozReadBean.ListBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    class ViewHolder extends BaseViewHolder<MozReadBean.ListBean> {
        ImageView imgCoverUrl;
        TextView tvName;
        TextView tvDesc;
        int position;
        TextView mtvTimeLength;
        TextView mtvRowPrice;
        TextView mtvActivePrice;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_moz_read);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgCoverUrl = findViewById(R.id.img_author);
            tvName = findViewById(R.id.txt_albumname);
            tvDesc = findViewById(R.id.txt_editor);
            mtvActivePrice = findViewById(R.id.tv_active_price);
            mtvRowPrice = findViewById(R.id.tv_row_price);
            mtvTimeLength = findViewById(R.id.tv_time_length);
        }

        @Override
        public void setData(final MozReadBean.ListBean data) {
            super.setData(data);

            Glide.with(getContext()).load(Util.getImgUrl(data.getCoverimgurl()))
                    .placeholder(R.drawable.icon_readload_failed)
                    .transform(new GlideRoundTransform(getContext()))
                    .error(R.drawable.icon_readload_failed)
                    .into(imgCoverUrl);
            mtvTimeLength.setText(TimeUtils.formatTime(data.getMedialength().longValue()));  //专栏时长
            if(StringUtils.isEmpty(String.valueOf(data.getOriginalprice()))){
                //原价为0说明当前专栏免费
                mtvRowPrice.setVisibility(View.INVISIBLE);
                mtvActivePrice.setText("免费");
            }
            if(!StringUtils.isEmpty(String.valueOf(data.getIsbuy())) && 1 == data.getIsbuy()){
                mtvRowPrice.setVisibility(View.INVISIBLE);
                mtvActivePrice.setText("已购");
            }else {
                if(!StringUtils.isEmpty(String.valueOf(data.getIsActPriceShow())) && 1 == data.getIsActPriceShow()){//显示活动价
                    mtvRowPrice.setVisibility(View.VISIBLE);
                    mtvRowPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mtvRowPrice.setText(data.getOriginalprice()+" 墨子币");  //原价
                    mtvActivePrice.setText(data.getActivityprice()+" 墨子币"); //显示活动价
                }else if(!StringUtils.isEmpty(String.valueOf(data.getIsActPriceShow())) && 0 == data.getIsActPriceShow()){
                    mtvRowPrice.setVisibility(View.INVISIBLE);
                    mtvActivePrice.setText(data.getOriginalprice()+" 墨子币");
                }
            }
            tvName.setText(data.getName());
            tvDesc.setText(data.getDesc());
        }

        @Override
        public void onItemViewClick(MozReadBean.ListBean data) {
            super.onItemViewClick(data);
            if (MozInformationAllAdapter.this.onItemClickListener != null) {
                MozInformationAllAdapter.this.onItemClickListener.onItemClick(getLayoutPosition(), (int) data.getOriginalprice());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int isPrice);

    }
}
