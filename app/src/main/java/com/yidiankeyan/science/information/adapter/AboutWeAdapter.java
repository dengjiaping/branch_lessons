package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.AboutUs;
import com.yidiankeyan.science.information.entity.AboutWe;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.ShowAllListView;

import java.util.List;

/**
 * Created by zn on 2016/8/3 0003.
 */
public class AboutWeAdapter extends BaseAdapter {
    private Context mContext;
    private List<AboutWe> mAboutList;
    private LayoutInflater mInflater;
    private int type;
    private Intent intent;

    public AboutWeAdapter(Context mContext, List<AboutWe> mAboutList) {
        this.mContext = mContext;
        this.mAboutList = mAboutList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mAboutList.size();
    }

    @Override
    public AboutWe getItem(int position) {
        return mAboutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mAboutList.get(position).getType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderTwo viewHolderTwo = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 1:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 2:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
                case 1:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
                case 2:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case 0:
                viewHolderTwo.tvTitle.setText(mAboutList.get(position).getTitle());
                viewHolderTwo.imgClick.setVisibility(View.GONE);
                viewHolderTwo.listview.setAdapter(new ChildFourListAdapter(mAboutList.get(position).getAboutList()));
                break;
            case 1:
                viewHolderTwo.tvTitle.setText(mAboutList.get(position).getTitle());
                viewHolderTwo.imgClick.setVisibility(View.GONE);
                viewHolderTwo.listview.setAdapter(new ChildFourListAdapter(mAboutList.get(position).getAboutList()));
                break;
            case 2:
                viewHolderTwo.tvTitle.setText(mAboutList.get(position).getTitle());
                viewHolderTwo.imgClick.setVisibility(View.GONE);
                viewHolderTwo.listview.setAdapter(new ChildFourListAdapter(mAboutList.get(position).getAboutList()));
                break;
        }

        return convertView;
    }

    class ViewHolderTwo {
        private ImageView imgClick;
        private TextView tvTitle;
        private ShowAllListView listview;
    }

    class ChildFourListAdapter extends BaseAdapter {

        private List<AboutUs> mDatas;

        public ChildFourListAdapter(List<AboutUs> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public AboutUs getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_about_we, parent, false);
                holder.imgAbout = (ImageView) convertView.findViewById(R.id.img_about);
                holder.tvAboutName = (TextView) convertView.findViewById(R.id.tv_about_name);
                holder.tvAboutDes = (TextView) convertView.findViewById(R.id.tv_about_des);
                holder.txtAboutTime = (TextView) convertView.findViewById(R.id.txt_about_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl())).into(holder.imgAbout);
            holder.tvAboutDes.setText(mDatas.get(position).getDescribes());
            holder.tvAboutName.setText(mDatas.get(position).getName());
            holder.txtAboutTime.setText(mDatas.get(position).getCreatetime());

            return convertView;
        }


        class ViewHolder {
            private ImageView imgAbout;
            private TextView tvAboutName;
            private TextView tvAboutDes;
            private TextView txtAboutTime;
        }
    }
}
