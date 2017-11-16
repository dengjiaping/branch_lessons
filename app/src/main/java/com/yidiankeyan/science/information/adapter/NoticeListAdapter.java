package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.AboutWeActivity;
import com.yidiankeyan.science.information.acitivity.MoreLectureActivity;
import com.yidiankeyan.science.information.acitivity.RecommendMagazineActivity;
import com.yidiankeyan.science.information.acitivity.RecommendWebActivity;
import com.yidiankeyan.science.information.acitivity.ScienceRecommendActivity;
import com.yidiankeyan.science.information.entity.NoticeBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.NoScrollGridView;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

/**
 * Created by nby on 2016/7/22.
 */
public class NoticeListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<NoticeBean> mNoticeList;
    private LayoutInflater mInflater;
    private int type;
    private Intent intent;

    public NoticeListAdapter(Context mContext, List<NoticeBean> mNoticeList) {
        this.mContext = mContext;
        this.mNoticeList = mNoticeList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mNoticeList.size();
    }

    @Override
    public NoticeBean getItem(int position) {
        return mNoticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mNoticeList.get(position).getType()) {
            case 0:
                //关于我们
                return 0;
            case 1:
                //讲座信息, 科技活动信息
                return 1;
            case 2:
                //科技公众号推荐, 科技网站推荐
                return 2;
            case 3:
                //科技杂志推荐
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderOne viewHolderOne = null;
        ViewHolderTwo viewHolderTwo = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    viewHolderOne = new ViewHolderOne();
                    convertView = mInflater.inflate(R.layout.item_notice_type_one, parent, false);
                    viewHolderOne.llTitle = (AutoLinearLayout) convertView.findViewById(R.id.ll_title);
                    viewHolderOne.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderOne.imgClick = (ImageView) convertView.findViewById(R.id.img_click_type);
                    viewHolderOne.listviewItemGridview = (NoScrollGridView) convertView.findViewById(R.id.listview_item_gridview);
                    convertView.setTag(viewHolderOne);
                    break;
                case 1:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.item_notice_two, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.imgOnClick = (ImageView) convertView.findViewById(R.id.img_click_twotype);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.listview);
                    viewHolderTwo.llTitle = (AutoLinearLayout) convertView.findViewById(R.id.ll_title);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 2:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.item_notice_two, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.imgOnClick = (ImageView) convertView.findViewById(R.id.img_click_twotype);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.listview);
                    viewHolderTwo.llTitle = (AutoLinearLayout) convertView.findViewById(R.id.ll_title);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 3:
                    viewHolderOne = new ViewHolderOne();
                    convertView = mInflater.inflate(R.layout.item_notice_type_one, parent, false);
                    viewHolderOne.llTitle = (AutoLinearLayout) convertView.findViewById(R.id.ll_title);
                    viewHolderOne.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderOne.imgClick = (ImageView) convertView.findViewById(R.id.img_click_type);
                    viewHolderOne.listviewItemGridview = (NoScrollGridView) convertView.findViewById(R.id.listview_item_gridview);
                    convertView.setTag(viewHolderOne);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    viewHolderOne = (ViewHolderOne) convertView.getTag();
                    break;
                case 1:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
                case 2:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
                case 3:
                    viewHolderOne = (ViewHolderOne) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case 0:
                viewHolderOne.tvTitle.setText(mNoticeList.get(position).getTitle());
                viewHolderOne.imgClick.setTag(position);
                viewHolderOne.imgClick.setOnClickListener(this);
                viewHolderOne.listviewItemGridview.setAdapter(new GridAdapter(mNoticeList.get(position).getNotifyList()));
                if (mNoticeList.get(position).getNotifyList().size() == 0)
                    viewHolderOne.llTitle.setVisibility(View.GONE);
                else
                    viewHolderOne.llTitle.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewHolderTwo.tvTitle.setText(mNoticeList.get(position).getTitle());
                viewHolderTwo.imgOnClick.setTag(position);
                viewHolderTwo.imgOnClick.setOnClickListener(this);
                ChildTwoListAdapter twoListAdapter = new ChildTwoListAdapter(mNoticeList.get(position).getNotifyList());
                twoListAdapter.setType(mNoticeList.get(position).getTitle());
                if (mNoticeList.get(position).getNotifyList().size() == 0) {
                    viewHolderTwo.llTitle.setVisibility(View.GONE);
                } else {
                    viewHolderTwo.llTitle.setVisibility(View.VISIBLE);
                }
                viewHolderTwo.listview.setAdapter(twoListAdapter);
                break;
            case 2:
                viewHolderTwo.tvTitle.setText(mNoticeList.get(position).getTitle());
                viewHolderTwo.imgOnClick.setTag(position);
                viewHolderTwo.imgOnClick.setOnClickListener(this);
                ChildThreeListAdapter threeListAdapter = new ChildThreeListAdapter(mNoticeList.get(position).getNotifyList());
                if ("科技网站推荐".equals(mNoticeList.get(position).getTitle()))
                    threeListAdapter.setShowUnderscores(true);
                else
                    threeListAdapter.setShowUnderscores(false);
                if (mNoticeList.get(position).getNotifyList().size() == 0)
                    viewHolderTwo.llTitle.setVisibility(View.GONE);
                else
                    viewHolderTwo.llTitle.setVisibility(View.VISIBLE);
                viewHolderTwo.listview.setAdapter(threeListAdapter);
                break;
            case 3:
                viewHolderOne.tvTitle.setText(mNoticeList.get(position).getTitle());
                viewHolderOne.imgClick.setTag(position);
                viewHolderOne.imgClick.setOnClickListener(this);
                if (mNoticeList.get(position).getNotifyList().size() == 0)
                    viewHolderOne.llTitle.setVisibility(View.GONE);
                else
                    viewHolderOne.llTitle.setVisibility(View.VISIBLE);
                viewHolderOne.listviewItemGridview.setAdapter(new MagazineAdapter(mNoticeList.get(position).getNotifyList()));
                break;
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_click_type:
                int positionOne = (int) v.getTag();
                if (mNoticeList.get(positionOne).getType() == 0 && mNoticeList.get(positionOne).getTitle().equals("关于我们")) {
                    intent = new Intent(mContext, AboutWeActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionOne).getTitle());
                    mContext.startActivity(intent);
                } else if (mNoticeList.get(positionOne).getType() == 3 && mNoticeList.get(positionOne).getTitle().equals("科技杂志推荐")) {
                    intent = new Intent(mContext, RecommendMagazineActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionOne).getTitle());
                    mContext.startActivity(intent);
                }

                break;
            case R.id.img_click_twotype:
                int positionTwo = (int) v.getTag();
                if (mNoticeList.get(positionTwo).getType() == 1 && mNoticeList.get(positionTwo).getTitle().equals("讲座信息")) {
                    intent = new Intent(mContext, MoreLectureActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionTwo).getTitle());
                    mContext.startActivity(intent);
                } else if (mNoticeList.get(positionTwo).getType() == 1 && mNoticeList.get(positionTwo).getTitle().equals("科技活动信息")) {
                    intent = new Intent(mContext, MoreLectureActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionTwo).getTitle());
                    mContext.startActivity(intent);
                }

                if (mNoticeList.get(positionTwo).getType() == 2 && mNoticeList.get(positionTwo).getTitle().equals("科技公众号推荐")) {
                    intent = new Intent(mContext, ScienceRecommendActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionTwo).getTitle());
                    mContext.startActivity(intent);
                } else if (mNoticeList.get(positionTwo).getType() == 2 && mNoticeList.get(positionTwo).getTitle().equals("科技网站推荐")) {
                    intent = new Intent(mContext, ScienceRecommendActivity.class);
                    intent.putExtra("title", mNoticeList.get(positionTwo).getTitle());
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    class ViewHolderOne {
        private TextView tvTitle;
        private AutoLinearLayout llTitle;
        private ImageView imgClick;
        private NoScrollGridView listviewItemGridview;
    }

    class ViewHolderTwo {
        private TextView tvTitle;
        private AutoLinearLayout llTitle;
        private ImageView imgOnClick;
        private ShowAllListView listview;
    }

    class GridAdapter extends BaseAdapter {

        private List<NoticeBean.NotifyChildBean> mDatas;

        public GridAdapter(List<NoticeBean.NotifyChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public NoticeBean.NotifyChildBean getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.notice_grid_item, parent, false);
                holder.gridviewItemButton = (ImageView) convertView.findViewById(R.id.gridview_item_button);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(mDatas.get(position).getName());
            Glide.with(mContext).load(mDatas.get(position).getAvatar()).into(holder.gridviewItemButton);

//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.startActivity(new Intent(mContext, RecommendWebActivity.class).putExtra("url", "http://moz.poinetech.com/cmsweb/relateus/info/" + mDatas.get(position).getId()));
//                }
//            });
            return convertView;
        }

        class ViewHolder {
            private ImageView gridviewItemButton;
            private TextView tvName;
        }
    }

    class ChildTwoListAdapter extends BaseAdapter {

        private List<NoticeBean.NotifyChildBean> mDatas;
        private String type;

        public void setType(String type) {
            this.type = type;
        }

        public ChildTwoListAdapter(List<NoticeBean.NotifyChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public NoticeBean.NotifyChildBean getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.notice_item_child_two, parent, false);
                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if ("讲座信息".equals(type)) {
                holder.tvName.setText("题目：" + mDatas.get(position).getName());
                holder.tvDes.setText("主讲人：" + mDatas.get(position).getDes());
            } else {
                holder.tvName.setText("主题：" + mDatas.get(position).getName());
                holder.tvDes.setText("承办：" + mDatas.get(position).getDes());
            }
            Glide.with(mContext).load(mDatas.get(position).getAvatar()).into(holder.imgAvatar);
            holder.tvDate.setText(mDatas.get(position).getDate());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, RecommendWebActivity.class).putExtra("url", SystemConstant.MYURL + "notice/info/" + mDatas.get(position).getId()));
//
// Toast.makeText(mContext, mDatas.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView imgAvatar;
            private TextView tvName;
            private TextView tvDes;
            private TextView tvDate;
        }
    }

    class ChildThreeListAdapter extends BaseAdapter implements View.OnClickListener {

        private List<NoticeBean.NotifyChildBean> mDatas;
        private boolean showUnderscores;

        public void setShowUnderscores(boolean showUnderscores) {
            this.showUnderscores = showUnderscores;
        }

        public ChildThreeListAdapter(List<NoticeBean.NotifyChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public NoticeBean.NotifyChildBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.notice_item_child_three, parent, false);
                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            Glide.with(mContext).load(mDatas.get(position).getAvatar()).into(holder.imgAvatar);
            holder.tvDes.setText(mDatas.get(position).getDes());
            holder.tvName.setText(mDatas.get(position).getName());
            if (showUnderscores) {
                holder.tvDes.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                holder.tvDes.getPaint().setAntiAlias(true);//抗锯齿
                holder.tvDes.setTag(position);
                holder.tvDes.setOnClickListener(this);
            } else {
                holder.tvDes.setOnClickListener(null);
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_des:
                    int webPosition = (int) v.getTag();
                    mContext.startActivity(new Intent(mContext, RecommendWebActivity.class).putExtra("url", mDatas.get(webPosition).getDate()));
                    break;
            }
        }

        class ViewHolder {
            private ImageView imgAvatar;
            private TextView tvName;
            private TextView tvDes;
        }
    }

    class MagazineAdapter extends BaseAdapter {

        private List<NoticeBean.NotifyChildBean> mDatas;

        public MagazineAdapter(List<NoticeBean.NotifyChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.item_notice_magazine, parent, false);
                holder.imgMagazineAvatar = (ImageView) convertView.findViewById(R.id.img_magazine_avatar);
                holder.txtMagazineName = (TextView) convertView.findViewById(R.id.txt_magazine_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getAvatar())).into(holder.imgMagazineAvatar);
            holder.txtMagazineName.setText(mDatas.get(position).getName());
            ViewGroup.LayoutParams params = holder.imgMagazineAvatar.getLayoutParams();
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView imgMagazineAvatar;
        private TextView txtMagazineName;
    }
}
