package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.entity.ColumnQueryArticleBean;
import com.yidiankeyan.science.utils.Util;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/14 0014.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤    专栏详情
 * //       █▓▓▓▓██◤            -文章列表Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class ColumnArticleAdapter extends RecyclerAdapter<ColumnQueryArticleBean> {

    private Context mContext;
    private int isPrice;

    public ColumnArticleAdapter(Context context) {
        super(context);
        mContext = context;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder<ColumnQueryArticleBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    class ViewHolder extends BaseViewHolder<ColumnQueryArticleBean> {
        TextView tvTitleName;
        TextView tvTime;
        TextView tvDesc;
        ImageView imgContentAvatar;
        ImageView tvFreeOfCharge;
        private TextView mtvUpdate;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_column_article);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgContentAvatar = findViewById(R.id.img_content_avatar);
            tvTitleName = findViewById(R.id.tv_title_name);
            tvTime = findViewById(R.id.tv_update_time);
            tvDesc = findViewById(R.id.tv_desc);
            tvFreeOfCharge = findViewById(R.id.tv_free_of_charge);
        }

        @Override
        public void setData(final ColumnQueryArticleBean object) {
            super.setData(object);
            if(DemoApplication.mListArticleselect.size()>0 && !DemoApplication.isBuyImagetext){
                if(DemoApplication.mListArticleselect.contains(object.getId())){
                    tvTitleName.setTextColor(mContext.getResources().getColor(R.color.menu));//
                }else {
                    tvTitleName.setTextColor(mContext.getResources().getColor(R.color.black_33));
                }
            }
            if(DemoApplication.mListImageTextselect.size()>0 &&DemoApplication.isBuyImagetext){
                if(DemoApplication.mListImageTextselect.contains(object.getId())){
                    tvTitleName.setTextColor(mContext.getResources().getColor(R.color.menu));//
                }else {
                    tvTitleName.setTextColor(mContext.getResources().getColor(R.color.black_33));
                }
            }

            Glide.with(getContext()).load(Util.getImgUrl(object.getArticleImg()))
                    .error(R.drawable.icon_banner_load)
                    .placeholder(R.drawable.icon_banner_load)
                    .into(imgContentAvatar);
            tvTitleName.setText(object.getArticleName());
            tvTime.setText("更新：" + object.getCreateTime());
            tvDesc.setText(object.getArticleIntro());
            if (TextUtils.equals("1", object.getHaveYouPurchased())) {
                tvFreeOfCharge.setVisibility(View.GONE);
            } else {
                if (TextUtils.equals("1", object.getFree())) {
                    tvFreeOfCharge.setVisibility(View.VISIBLE);
                } else {
                    tvFreeOfCharge.setVisibility(View.GONE);
                }
            }
        }


        @Override
        public void onItemViewClick(ColumnQueryArticleBean data) {
            super.onItemViewClick(data);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), isPrice);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int isPrice);
    }
}
