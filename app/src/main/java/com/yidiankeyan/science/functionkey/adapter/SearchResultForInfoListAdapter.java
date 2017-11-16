package com.yidiankeyan.science.functionkey.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.functionkey.entity.SearchInfoBean;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.activity.MyHomePageActivity;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.ShowAllListView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索更多类型Adapter
 */
public class SearchResultForInfoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchInfoBean> mSearchList;
    private LayoutInflater mInflater;

    public SearchResultForInfoListAdapter(Context mContext, List<SearchInfoBean> mSearchList) {
        this.mContext = mContext;
        this.mSearchList = mSearchList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mSearchList == null ? 0 : mSearchList.size();
    }

    @Override
    public SearchInfoBean getItem(int position) {
        return mSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mSearchList.get(position).getType()) {
            case 0:
                //专辑
                return 0;
            case 1:
                //内容
                return 1;
            case 2:
                //主编
                return 2;
            case 3:
                //答人
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
        ViewHolderTwo viewHolderTwo = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 1:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 2:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    viewHolderTwo.listview = (ShowAllListView) convertView.findViewById(R.id.lv_search_album);
                    convertView.setTag(viewHolderTwo);
                    break;
                case 3:
                    viewHolderTwo = new ViewHolderTwo();
                    convertView = mInflater.inflate(R.layout.search_result_whole, parent, false);
                    viewHolderTwo.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
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
                case 3:
                    viewHolderTwo = (ViewHolderTwo) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case 0:
                viewHolderTwo.tvTitle.setText(mSearchList.get(position).getTitle());
                viewHolderTwo.listview.setAdapter(new ChildOneListAdapter(mSearchList.get(position).getSearchList()));
                break;
            case 1:
                viewHolderTwo.tvTitle.setText(mSearchList.get(position).getTitle());
                viewHolderTwo.listview.setAdapter(new ChildTwoListAdapter(mSearchList.get(position).getSearchList()));
                break;
            case 2:
                viewHolderTwo.tvTitle.setText(mSearchList.get(position).getTitle());
                viewHolderTwo.listview.setAdapter(new ChildThreeListAdapter(mSearchList.get(position).getSearchList()));
                break;
            case 3:
                viewHolderTwo.tvTitle.setText(mSearchList.get(position).getTitle());
                viewHolderTwo.listview.setAdapter(new ChildFourListAdapter(mSearchList.get(position).getSearchList()));
                break;
        }
        return convertView;
    }

    class ViewHolderTwo {
        private TextView tvTitle;
        private ShowAllListView listview;
    }


    /**
     * 专辑
     */
    class ChildOneListAdapter extends BaseAdapter {

        private List<SearchInfoBean.SearchChildBean> mDatas;

        public ChildOneListAdapter(List<SearchInfoBean.SearchChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public SearchInfoBean.SearchChildBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.search_album_item, parent, false);
                holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.txtContentId = (TextView) convertView.findViewById(R.id.tv_content_num);
                holder.txtContent = (TextView) convertView.findViewById(R.id.tv_des);
                holder.txtNumber = (TextView) convertView.findViewById(R.id.tv_read_num);
                holder.txtTou = (TextView) convertView.findViewById(R.id.tv_subject_name);
                holder.txtUpTime = (TextView) convertView.findViewById(R.id.tv_whole_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getImgUrl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(holder.imgAdd);
            holder.txtName.setText(mDatas.get(position).getContentNumber());
            holder.txtContentId.setText("内容量" + mDatas.get(position).getContentAmount() + "篇");
            holder.txtContent.setText(mDatas.get(position).getContentName());
            holder.txtUpTime.setText(mDatas.get(position).getDateTime());
            holder.txtNumber.setText("浏览量" + mDatas.get(position).getReadAmount());

            if (!TextUtils.isEmpty(mDatas.get(position).getAlbumName()) && !TextUtils.equals("null", mDatas.get(position).getAlbumName())) {
                holder.txtTou.setVisibility(View.VISIBLE);
                holder.txtTou.setText(mDatas.get(position).getAlbumName());
            } else {
                holder.txtTou.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AlbumDetailsActivity.class);
                    intent.putExtra("albumId", mDatas.get(position).getAlbumId());
                    intent.putExtra("albumName", mDatas.get(position).getAlbumName());
                    intent.putExtra("albumAvatar", mDatas.get(position).getImgUrl());
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView imgAdd;
            private TextView txtContentId;
            private TextView txtContent;
            private TextView txtName;
            private TextView txtNumber;
            private TextView txtTou;
            private TextView txtUpTime;
        }
    }

    /**
     * 内容
     */
    class ChildTwoListAdapter extends BaseAdapter {

        private List<SearchInfoBean.SearchChildBean> mDatas;

        public ChildTwoListAdapter(List<SearchInfoBean.SearchChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public SearchInfoBean.SearchChildBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.search_content_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatars);
                holder.tvContentId = (TextView) convertView.findViewById(R.id.search_number);
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_read_number);
                holder.tvUpTime = (TextView) convertView.findViewById(R.id.tv_data_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getImgUrl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(holder.imgAvatar);


            holder.tvName.setText(mDatas.get(position).getAlbumName());
            holder.tvContentId.setText(mDatas.get(position).getContentAmount() + "");
            holder.tvUpTime.setText(mDatas.get(position).getDateTime());
            holder.tvNumber.setText(mDatas.get(position).getReadAmount() + "");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (mDatas.get(position).getType() == 1) {
                        intent = new Intent(mContext, ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mDatas.get(position).getArticleId());
                        mContext.startActivity(intent);
                    } else if (mDatas.get(position).getType() == 2) {
                        ArrayList<AlbumContent> listItem = new ArrayList<>();
                        AlbumContent albumContent = new AlbumContent(mDatas.get(position).getAlbumName());
                        albumContent.setArticleid(mDatas.get(position).getArticleId());
                        albumContent.setMediaurl(mDatas.get(position).getMediaurl());
                        listItem.add(albumContent);
                        intent = new Intent(mContext, AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("single", true);
                        intent.putExtra("id", listItem.get(0).getArticleid());
                        intent.putExtra("position", 0);
                        mContext.startActivity(intent);
                    } else if (mDatas.get(position).getType() == 3) {
                        ArrayList<AlbumContent> listItem = new ArrayList<>();
                        AlbumContent albumContent = new AlbumContent(mDatas.get(position).getAlbumName());
                        albumContent.setArticleid(mDatas.get(position).getArticleId());
                        albumContent.setMediaurl(mDatas.get(position).getMediaurl());
                        listItem.add(albumContent);
                        intent = new Intent(mContext, VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("id", listItem.get(0).getArticleid());
                        intent.putExtra("position", 0);
                        mContext.startActivity(intent);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView imgAvatar;
            private TextView tvName;
            private TextView tvContentId;
            private TextView tvNumber;
            private TextView tvUpTime;
        }
    }

    /**
     * 主编
     */
    class ChildThreeListAdapter extends BaseAdapter {

        private List<SearchInfoBean.SearchChildBean> mDatas;

        public ChildThreeListAdapter(List<SearchInfoBean.SearchChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public SearchInfoBean.SearchChildBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.search_editor_item, parent, false);
                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.llInto = (AutoLinearLayout) convertView.findViewById(R.id.ll_into);
                holder.tvFans = (TextView) convertView.findViewById(R.id.tv_fans);
                holder.tvFansCount = (TextView) convertView.findViewById(R.id.tv_album_name);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getImgurl())).error(R.drawable.icon_default_avatar).into(holder.imgAvatar);

            holder.tvName.setText(mDatas.get(position).getName());
            holder.tvFans.setText(mDatas.get(position).getFansnum() + "人已关注");
            holder.tvFansCount.setText(mDatas.get(position).getNick());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyHomePageActivity.class);
                    intent.putExtra("id", mDatas.get(position).getAuthorid());
                    intent.putExtra("name", mDatas.get(position).getName());
                    mContext.startActivity(intent);
                }
            });

//            holder.tvName.setText(albumBean.getName());
//            holder.tvDes.setText(albumBean.getDes());
//            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + albumBean.getAvatar()).into(holder.imgAvatar);
//            holder.tvFansCount.setText(albumBean.getFansCount() + "");
            return convertView;
        }


        class ViewHolder {
            private ImageView imgAvatar;
            private TextView tvName;
            private TextView tvFans;
            private TextView tvFansCount;
            private AutoLinearLayout llInto;
        }
    }

    /**
     * 答人
     */
    class ChildFourListAdapter extends BaseAdapter {

        private List<SearchInfoBean.SearchChildBean> mDatas;

        public ChildFourListAdapter(List<SearchInfoBean.SearchChildBean> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public SearchInfoBean.SearchChildBean getItem(int position) {
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
                convertView = mInflater.inflate(R.layout.search_answer_item, parent, false);
                holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.tvAnswerNumber = (TextView) convertView.findViewById(R.id.tv_answer_number);
                holder.txtContent = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvEavesdroppingNumber = (TextView) convertView.findViewById(R.id.tv_eavesdropping_number);
                holder.tvFansNumber = (TextView) convertView.findViewById(R.id.tv_fans_number);
                holder.imgIsfocus = (ImageView) convertView.findViewById(R.id.img_isfocus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getUserimgurl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(holder.imgAdd);

            holder.txtName.setText(mDatas.get(position).getKdname());
            holder.txtContent.setText(mDatas.get(position).getProfession());
            holder.tvAnswerNumber.setText(mDatas.get(position).getAnswersnum() + "");
            holder.tvEavesdroppingNumber.setText(mDatas.get(position).getEavesdropnum() + "人偷听");
            holder.tvFansNumber.setText(mDatas.get(position).getFollowernum() + "人关注");


//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, AnswerTopDetailActivity.class);
//                    intent.putExtra("id", mDatas.get(position).getId());
//                    intent.putExtra("position", position);
//                    intent.putExtra("answerName", mDatas.get(position).getKdname());
//                    intent.putExtra("answerAvatar", mDatas.get(position).getProfession());
//                    mContext.startActivity(intent);
//                }
//            });


            if (mDatas.get(position).getIsfocus() == 0)
                holder.imgIsfocus.setVisibility(View.GONE);
            else
                holder.imgIsfocus.setVisibility(View.VISIBLE);
            return convertView;
        }


        class ViewHolder {
            private ImageView imgAdd;
            private TextView txtContent;
            private TextView txtName;
            private TextView tvAnswerNumber;
            private TextView tvEavesdroppingNumber;
            private TextView tvFansNumber;
            private ImageView imgIsfocus;
        }
    }

}
