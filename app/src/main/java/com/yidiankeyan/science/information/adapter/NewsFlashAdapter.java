package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * 快讯
 */
public class NewsFlashAdapter extends RecyclerAdapter<FlashBean> {

    private String mTitle;

    public NewsFlashAdapter(Context context) {
        super(context);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder<FlashBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<FlashBean> {
        private View viewDate;
        private TextView tvDate;
        private TextView tvContent;
        private View divider;


        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_newsflash);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            viewDate = (View) findViewById(R.id.view_date);
            tvDate = (TextView) findViewById(R.id.tv_date);
            tvContent = (TextView) findViewById(R.id.tv_content);
            divider = (View) findViewById(R.id.divider);
        }

        @Override
        public void setData(FlashBean data) {
            super.setData(data);
            boolean isRead = DB.getInstance(DemoApplication.applicationContext).newsIsLooked(data.getId());
            if (isRead) {
                tvContent.setTextColor(Color.parseColor("#999999"));
            } else {
                tvContent.setTextColor(Color.parseColor("#333333"));
            }
            if (!TextUtils.isEmpty(data.getTitle()) && !TextUtils.equals("null", data.getTitle())) {
                mTitle = "【" + data.getTitle() + "】";
            } else {
                mTitle = "";
            }
            tvContent.setText(TimeUtils.formatKedaTime(data.getCreatetime()) + mTitle + data.getContent());
            int position = getLayoutPosition();
            if (position == 0) {
                viewDate.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(new SimpleDateFormat("MM月dd日").format(new Date(data.getCreatetime())));
            } else {
                if (TextUtils.equals(new SimpleDateFormat("yyyy-MM-dd").format(data.getCreatetime()), new SimpleDateFormat("yyyy-MM-dd").format(getData().get(position - 1).getCreatetime()))) {
                    viewDate.setVisibility(View.GONE);
                    tvDate.setVisibility(View.GONE);
                } else {
                    viewDate.setVisibility(View.VISIBLE);
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText(new SimpleDateFormat("MM月dd日").format(new Date(data.getCreatetime())));
                }
            }
            if (position == getData().size() - 1) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onItemViewClick(FlashBean data) {
            super.onItemViewClick(data);
            if (NewsFlashAdapter.this.onItemClickListener != null) {
                NewsFlashAdapter.this.onItemClickListener.onItemClick(getLayoutPosition());
            }
//            Intent intent = new Intent(getContext(), NewsFlashCardActivity.class);
//            intent.putExtra("position", getLayoutPosition());
//
//            getContext().startActivity(intent);
//            ((Activity)getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
