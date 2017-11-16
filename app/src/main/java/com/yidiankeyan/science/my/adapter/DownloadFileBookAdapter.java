package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;

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
 * Created by nby on 2016/12/10.
 * 作用：
 */

public class DownloadFileBookAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private List<MozReadDetailsBean> bookFileList;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isDeleteState;
    private OnDeleteChangeListener onDeleteChangeListener;

    public DownloadFileBookAdapter(List<MozReadDetailsBean> bookFileList, Context context) {
        this.bookFileList = bookFileList;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return bookFileList == null ? 0 : bookFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookFileList == null ? null : bookFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDeleteState(boolean deleteState) {
        isDeleteState = deleteState;
    }

    public void setOnDeleteChangeListener(OnDeleteChangeListener onDeleteChangeListener) {
        this.onDeleteChangeListener = onDeleteChangeListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_download_purchase, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvColumnName.setVisibility(View.GONE);
        if (bookFileList.get(position).getName() != null && bookFileList.get(position).getName().startsWith("类型：杂志")) {
            holder.tvTitle.setText(bookFileList.get(position).getName().subSequence("类型：杂志".length(), bookFileList.get(position).getName().length()));
        } else {
            holder.tvTitle.setText(bookFileList.get(position).getName());
        }
        if (bookFileList.get(position).getAlreadyWatch() == 0) {
            holder.tvAlreadyWatch.setText("未收听丨");
        } else {
            holder.tvAlreadyWatch.setText("已收听丨");
        }
        holder.tvFileSize.setText(bookFileList.get(position).getFileSize());
        if (isDeleteState) {
            holder.cbDelete.setVisibility(View.VISIBLE);
            holder.cbDelete.setOnCheckedChangeListener(this);
            if (bookFileList.get(position).isNeedDelete())
                holder.cbDelete.setChecked(true);
            else
                holder.cbDelete.setChecked(false);
        } else {
            holder.cbDelete.setVisibility(View.GONE);
        }
        holder.cbDelete.setTag(position);
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        bookFileList.get(position).setNeedDelete(isChecked);
        if (onDeleteChangeListener != null)
            onDeleteChangeListener.onDeleteChanged();
    }

    class ViewHolder {
        private TextView tvColumnName;
        private TextView tvTitle;
        private TextView tvAlreadyWatch;
        private TextView tvFileSize;
        private CheckBox cbDelete;

        public ViewHolder(View convertView) {
            tvColumnName = (TextView) convertView.findViewById(R.id.tv_column_name);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvAlreadyWatch = (TextView) convertView.findViewById(R.id.tv_already_watch);
            tvFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);
            cbDelete = (CheckBox) convertView.findViewById(R.id.cb_delete);
        }
    }

    public interface OnDeleteChangeListener {
        void onDeleteChanged();
    }
}
