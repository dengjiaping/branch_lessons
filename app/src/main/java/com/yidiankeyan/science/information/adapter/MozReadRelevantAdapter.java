package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.entity.MozRelevantReadBean;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.GlideRoundTransform;

import java.text.DecimalFormat;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created zn on 2016/11/30 0030.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆           墨子读书
 * //    █▓▓▓██◆                -推荐相关书籍
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozReadRelevantAdapter extends RecyclerAdapter<MozRelevantReadBean> {

    private Context mContext;
    public MozReadRelevantAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<MozRelevantReadBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<MozRelevantReadBean> {
        ImageView imgCoverUrl;
        ImageView imgReadHead;
//        TextView tvUnscramblePeople;
        TextView tvReadTime;
        TextView tvName;
        TextView tvPrice;
        TextView tvDesc;
        int position;
        TextView mtvRowPrice;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_notice_magazine);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgCoverUrl = findViewById(R.id.img_author);
            imgReadHead = findViewById(R.id.img_read_head);
            tvName = findViewById(R.id.txt_albumname);
            tvDesc = findViewById(R.id.txt_editor);
            tvPrice = findViewById(R.id.tv_price);
            mtvRowPrice = findViewById(R.id.tv_row_price);
            tvReadTime = findViewById(R.id.tv_read_time);
        }

        @Override
        public void setData(final MozRelevantReadBean data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getCoverimgurl()))
                    .placeholder(R.drawable.icon_readload_failed)
                    .transform(new GlideRoundTransform(mContext))
                    .error(R.drawable.icon_readload_failed)
                    .into(imgCoverUrl);

            tvName.setText(data.getName());
            if(!StringUtils.isEmpty(String.valueOf(data.getIsBuy())) && 1 == data.getIsBuy()){  //已购
                mtvRowPrice.setVisibility(View.INVISIBLE);
                tvPrice.setText("已购");
            }else if(0 == data.getIsBuy() &&
                    !StringUtils.isEmpty(String.valueOf(data.getIsActPriceShow()))&& 1 == data.getIsActPriceShow()){  //未购买  有活动价
                mtvRowPrice.setVisibility(View.VISIBLE);
                mtvRowPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mtvRowPrice.setText(data.getPrice()+" 墨子币");
                tvPrice.setText(data.getActivityprice()+" 墨子币");
            }else if(0 == data.getIsBuy() &&
                    !StringUtils.isEmpty(String.valueOf(data.getIsActPriceShow()))&& 0 == data.getIsActPriceShow()){  //未购买  无活动价
                mtvRowPrice.setVisibility(View.INVISIBLE);
                tvPrice.setText(data.getPrice()+" 墨子币");
            }else if(0 == Integer.parseInt(String.valueOf(data.getPrice()))){
                mtvRowPrice.setVisibility(View.INVISIBLE);
                tvPrice.setText("免费");
            }
            tvReadTime.setText(TimeUtils.formatSeriesTime(data.getMedialength()));
            tvDesc.setText(data.getDesc());
        }

        @Override
        public void onItemViewClick(MozRelevantReadBean data) {
            super.onItemViewClick(data);
            //跳转到书籍详情
            Intent intent = new Intent(getContext(), MozReadDetailsActivity.class);
            intent.putExtra("id", data.getId());
            intent.putExtra("name", data.getName());
            getContext().startActivity(intent);
            Intent intents = new Intent();
            intents.setAction("action.read.finish");
            getContext().sendBroadcast(intents);
        }
    }

}
