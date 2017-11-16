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
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.entity.ColumnAllListBean;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.GlideRoundTransform;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/28 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                全部
 * //       █▓▓▓▓██◤                        -专栏Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecColumnAllAdapter extends RecyclerAdapter<ColumnAllListBean> {

    private int isPrice;
    private Context mContext;

    public RecColumnAllAdapter(Context context) {
        super(context);
        mContext = context;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public BaseViewHolder<ColumnAllListBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<ColumnAllListBean> {
        ImageView imgCoverUrl;
        TextView tvReadTime;
        TextView tvName;
        TextView tvPrice;
        TextView tvDesc;
        TextView tvColumnContent;
        TextView mtvActivePrice;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_all_column);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgCoverUrl = findViewById(R.id.img_author);
            tvName = findViewById(R.id.txt_albumname);
            tvDesc = findViewById(R.id.txt_editor);
            tvPrice = findViewById(R.id.tv_price);
            tvReadTime = findViewById(R.id.tv_read_time);
            tvColumnContent = findViewById(R.id.tv_column_title);
            mtvActivePrice = findViewById(R.id.tv_active_price);
        }

        @Override
        public void setData(final ColumnAllListBean data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getColumnPicture()))
                    .placeholder(R.drawable.icon_readload_failed)
                    .transform(new GlideRoundTransform(mContext))
                    .error(R.drawable.icon_readload_failed).into(imgCoverUrl);

            //TODO
            if (DemoApplication.mListselect.contains(data.getId())) {
                tvName.setTextColor(mContext.getResources().getColor(R.color.menu));//
                tvColumnContent.setTextColor(mContext.getResources().getColor(R.color.menu));
            } else {
                tvName.setTextColor(mContext.getResources().getColor(R.color.black_33));
                tvColumnContent.setTextColor(mContext.getResources().getColor(R.color.textgains));
            }
            tvName.setText(data.getColumnName());
            if (!StringUtils.isEmpty(data.getHaveYouPurchased()) &&
                    !StringUtils.isEmpty(String.valueOf(data.getIshasactivityprice()))) {
                if ("1".equals(data.getHaveYouPurchased())) {
                    tvPrice.setVisibility(View.INVISIBLE);
                    mtvActivePrice.setVisibility(View.VISIBLE);
                    mtvActivePrice.setText("已购");
                } else if ("0".equals(data.getHaveYouPurchased()) && 1 == data.getIshasactivityprice()) {  //未购买 有活动价
                    tvPrice.setVisibility(View.VISIBLE);
                    mtvActivePrice.setVisibility(View.VISIBLE);
                    tvPrice.setText(data.getColumnPrice().replace(".00", "") + " 墨子币");
                    tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mtvActivePrice.setText(data.getColumnActivityPrice().replace(".00", "") +
                            mContext.getResources().getString(R.string.moz_money));
                } else if ("0".equals(data.getHaveYouPurchased()) && 0 == data.getIshasactivityprice()) {  //未购买 无活动价
                    tvPrice.setVisibility(View.INVISIBLE);
                    mtvActivePrice.setVisibility(View.VISIBLE);
                    mtvActivePrice.setText(data.getColumnPrice().replace(".00", "") +
                            mContext.getResources().getString(R.string.moz_money));
                }
            }
            tvDesc.setText(data.getColumnWriterIntro());
            tvReadTime.setText(data.getCreateTime() +
                    mContext.getResources().getString(R.string.update));
            tvColumnContent.setText(data.getRankUpdate());
        }

        @Override
        public void onItemViewClick(ColumnAllListBean data) {
            super.onItemViewClick(data);
            if (RecColumnAllAdapter.this.onItemClickListener != null) {
                RecColumnAllAdapter.this.onItemClickListener.onItemClick(getLayoutPosition(), isPrice);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int isPrice);
    }

}
