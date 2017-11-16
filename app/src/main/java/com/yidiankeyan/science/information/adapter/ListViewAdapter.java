package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.ClassClickItemActivity;
import com.yidiankeyan.science.information.acitivity.EditorActivity;
import com.yidiankeyan.science.information.acitivity.YouLikeActivity;
import com.yidiankeyan.science.information.entity.NewRecommendBean;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;


/**
 * 推荐
 * -页面数据Adapter
 */
public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewRecommendBean> mList;

    public ListViewAdapter(List<NewRecommendBean> mList, Context mContext) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null, false);
            holder.txtlove = (TextView) convertView.findViewById(R.id.txt_love);
            holder.gridView = (GridView) convertView.findViewById(R.id.listview_item_gridview);
            holder.txt_more = (ImageButton) convertView.findViewById(R.id.txt_more);
            holder.viewLine = (ImageView) convertView.findViewById(R.id.view_line);
            holder.tvMore = (TextView) convertView.findViewById(R.id.tv_more);
            holder.llAlbumTitle = (AutoLinearLayout) convertView.findViewById(R.id.ll_album_title);
            holder.rlTodayAll = (AutoRelativeLayout) convertView.findViewById(R.id.rl_today_all);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextPaint tp = holder.txtlove.getPaint();
        tp.setFakeBoldText(true);


//        if (position + 1 == mList.size()) {
//            holder.viewLine.setVisibility(View.GONE);
//        }
        holder.llAlbumTitle.setVisibility(View.VISIBLE);
        holder.viewLine.setVisibility(View.VISIBLE);
        if (this.mList != null) {
            if (holder.txtlove != null) {
                if (mList.get(position).getSubjectid() == -1) {
                    holder.txtlove.setText("猜你喜欢");
                } else if (mList.get(position).getSubjectid() == -2) {
                    holder.txtlove.setText("推荐专辑");
                } else
                    holder.txtlove.setText(mList.get(position).getName());
            }
            if (holder.gridView != null) {
                GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mList.get(position).getSimpleAlbumModles());
                holder.gridView.setAdapter(gridViewAdapter);
                if (mList.get(position).getSimpleAlbumModles().size() == 0) {
                    holder.llAlbumTitle.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                } else {
                    holder.llAlbumTitle.setVisibility(View.VISIBLE);
                    holder.viewLine.setVisibility(View.VISIBLE);
                }
            }
        }
        holder.rlTodayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).getSubjectid() == -1) {
                    mContext.startActivity(new Intent(mContext, YouLikeActivity.class));
                } else if (mList.get(position).getSubjectid() == -2) {
                    mContext.startActivity(new Intent(mContext, EditorActivity.class));
                } else {
                    Intent intent = new Intent(mContext, ClassClickItemActivity.class);
                    intent.putExtra("title", mList.get(position).getName());
                    intent.putExtra("id", mList.get(position).getSubjectid());
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView txtlove;
        private ImageView viewLine;
        GridView gridView;
        ImageButton txt_more;
        private TextView tvMore;
        AutoLinearLayout llAlbumTitle;
        private AutoRelativeLayout rlTodayAll;
    }
}
