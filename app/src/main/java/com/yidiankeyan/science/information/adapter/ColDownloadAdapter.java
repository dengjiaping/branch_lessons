package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.collection.entity.CollectionBean;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 专辑下载
 */
public class ColDownloadAdapter extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private List<CollectionBean> mDatas;
    private Context context;
    private AutoLinearLayout ll_all;
    private PopupWindow mPopupWindow;
    private boolean isDeleteState;
    private OnDeleteChangeListener onDeleteChangeListener;

    public ColDownloadAdapter(Context context, List<CollectionBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    public void setDeleteState(boolean deleteState) {
        isDeleteState = deleteState;
    }

    public void setOnDeleteChangeListener(OnDeleteChangeListener onDeleteChangeListener) {
        this.onDeleteChangeListener = onDeleteChangeListener;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public AutoLinearLayout getLl_all() {
        return ll_all;
    }

    public void setLl_all(AutoLinearLayout ll_all) {
        this.ll_all = ll_all;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_album_download, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.sub_title);
            viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.sub_item_button);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txt_neirong);
            viewHolder.cbDelete = (CheckBox) convertView.findViewById(R.id.cb_delete);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tvContentNum = (TextView) convertView.findViewById(R.id.tv_content_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final CollectionBean allBean = mDatas.get(position);
//        if (TextUtils.isEmpty(allBean.getLastupdatetitle()))
//            viewHolder.textView.setText(allBean.getRecenttitle());
//        else
        viewHolder.textView.setText(allBean.getName());
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .bitmapTransform(new RoundedCornersTransformation(context, 16, 0, RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);
        viewHolder.txtNumber.setText("已缓存 " + allBean.getContentnum() + "集");
        viewHolder.cbDelete.setTag(position);
        if (allBean.getType() == 2) {
            viewHolder.tvType.setText("音频");
        } else if (allBean.getType() == 3) {
            viewHolder.tvType.setText("视频");
        } else {
            viewHolder.tvType.setVisibility(View.GONE);
        }
        if (isDeleteState) {
            viewHolder.cbDelete.setVisibility(View.VISIBLE);
            viewHolder.cbDelete.setOnCheckedChangeListener(this);
            if (mDatas.get(position).isNeedDelete())
                viewHolder.cbDelete.setChecked(true);
            else
                viewHolder.cbDelete.setChecked(false);
        } else {
            viewHolder.cbDelete.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getPraisenum() != 0) {
            viewHolder.tvContentNum.setVisibility(View.VISIBLE);
            viewHolder.tvContentNum.setText("共" + mDatas.get(position).getPraisenum() + "集 丨 ");
        } else {
            viewHolder.tvContentNum.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_trash:
//                final int position = (int) v.getTag();
//                showDialog(position);
//                break;
//        }
    }

    private void showDialog(final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("这将会删除该专辑下的所有内容是否继续？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<AlbumContent> list = DB.getInstance(DemoApplication.applicationContext).queryDownloadFiles();
                        for (AlbumContent content : list) {
                            if (content.getAlbumId().equals(mDatas.get(position).getId())) {
                                DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(content.getArticleid());
                                try {
                                    new File(content.getFilePath()).delete();
                                } catch (Exception e) {
                                }
                            }
                        }
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                }
        );
        builder.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        mDatas.get(position).setNeedDelete(isChecked);
        if (onDeleteChangeListener != null)
            onDeleteChangeListener.onDeleteChanged();
    }

    public interface OnDeleteChangeListener {
        void onDeleteChanged();
    }

    class ViewHolder {
        private CheckBox cbDelete;
        ImageView imgAdd;
        TextView textView;
        TextView txtNumber;
        private TextView tvType;
        private TextView tvContentNum;
    }

}
