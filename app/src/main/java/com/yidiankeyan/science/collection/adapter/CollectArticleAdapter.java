package com.yidiankeyan.science.collection.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.collection.entity.CollectArticle;
import com.yidiankeyan.science.utils.Util;

import java.util.List;


/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/10/20.
 * 作用：
 */
public class CollectArticleAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private List<CollectArticle> mDatas;
    private LayoutInflater mInflater;
    private boolean isDeleteState;
    private OnDeleteChangeListener onDeleteChangeListener;

    public CollectArticleAdapter(Context context, List<CollectArticle> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnDeleteChangeListener(OnDeleteChangeListener onDeleteChangeListener) {
        this.onDeleteChangeListener = onDeleteChangeListener;
    }

    public void setDeleteState(boolean deleteState) {
        isDeleteState = deleteState;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public CollectArticle getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.item_collect, parent, false);
            holder.cbDelete = (CheckBox) convertView.findViewById(R.id.cb_delete);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.tvReadNum = (TextView) convertView.findViewById(R.id.tv_read_num);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Glide.with(mContext).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(holder.imgAvatar);
        holder.tvName.setText(mDatas.get(position).getAlbumname());
        holder.tvTitle.setText(mDatas.get(position).getName());
        holder.tvReadNum.setText("浏览量" + mDatas.get(position).getReadnum());
        if (!TextUtils.isEmpty(mDatas.get(position).getAuthorname()) && !TextUtils.equals("null", mDatas.get(position).getAuthorname())) {
            holder.tvAuthorName.setVisibility(View.VISIBLE);
            holder.tvAuthorName.setText(mDatas.get(position).getAuthorname());
        } else {
            holder.tvAuthorName.setVisibility(View.GONE);
            holder.tvAuthorName.setText("");
        }

        holder.cbDelete.setTag(position);
        if (isDeleteState) {
            holder.cbDelete.setVisibility(View.VISIBLE);
            holder.cbDelete.setOnCheckedChangeListener(this);
            if (mDatas.get(position).isNeedDelete())
                holder.cbDelete.setChecked(true);
            else
                holder.cbDelete.setChecked(false);
        } else {
            holder.cbDelete.setVisibility(View.GONE);
        }
        return convertView;
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
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvTitle;
        private TextView tvAuthorName;
        private TextView tvReadNum;
    }
}
