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
import com.yidiankeyan.science.information.entity.IssuesDetailBean;

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
 * 作用：下载--期刊
 */

public class DownloadFileIssuesAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private List<IssuesDetailBean> issuesFileList;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isDeleteState;
    private OnDeleteChangeListener onDeleteChangeListener;

    public DownloadFileIssuesAdapter(List<IssuesDetailBean> issuesFileList, Context context) {
        this.issuesFileList = issuesFileList;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return issuesFileList == null ? 0 : issuesFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return issuesFileList == null ? null : issuesFileList.get(position);
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
        holder.tvColumnName.setText(issuesFileList.get(position).getColumnname() + "丨");
        holder.tvTitle.setText(issuesFileList.get(position).getName());
        if (issuesFileList.get(position).getAlreadyWatch() == 0) {
            holder.tvAlreadyWatch.setText("未收听丨");
        } else {
            holder.tvAlreadyWatch.setText("已收听丨");
        }
        if (isDeleteState) {
            holder.cbDelete.setVisibility(View.VISIBLE);
            holder.cbDelete.setOnCheckedChangeListener(this);
            if (issuesFileList.get(position).isNeedDelete())
                holder.cbDelete.setChecked(true);
            else
                holder.cbDelete.setChecked(false);
        } else {
            holder.cbDelete.setVisibility(View.GONE);
        }
        holder.cbDelete.setTag(position);
        holder.tvFileSize.setText(issuesFileList.get(position).getFileSize());
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        if (issuesFileList != null && position >= issuesFileList.size())
            return;
        issuesFileList.get(position).setNeedDelete(isChecked);
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
