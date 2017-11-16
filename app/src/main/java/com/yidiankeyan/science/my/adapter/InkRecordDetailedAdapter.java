package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.entity.InKRecordBean;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/5/17 0017.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤            墨水明细Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InkRecordDetailedAdapter extends BaseAdapter {

    private List<InKRecordBean> mDatas;
    private Context context;
    private String remove;

    public InkRecordDetailedAdapter(Context context, List<InKRecordBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detailed_ink, parent, false);
            viewHolder.tvAccountName = (TextView) convertView.findViewById(R.id.tv_account_name);
            viewHolder.tvAccountTime = (TextView) convertView.findViewById(R.id.tv_account_time);
            viewHolder.tvAccountNumber = (TextView) convertView.findViewById(R.id.tv_account_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAccountName.setText(mDatas.get(position).getTypeName());
        viewHolder.tvAccountTime.setText( mDatas.get(position).getCreateTime().replace(".0",""));
        if (TextUtils.equals("1", mDatas.get(position).getType())) {
            viewHolder.tvAccountNumber.setText("-" + mDatas.get(position).getNum()+"滴");
        } else {
            viewHolder.tvAccountNumber.setText("+" + mDatas.get(position).getNum()+"滴");
        }

//        if (position == 4) {
//            viewHolder.vAccountLine.setVisibility(View.GONE);
//        }

        return convertView;
    }

    class ViewHolder {
        TextView tvAccountName;
        TextView tvAccountTime;
        TextView tvAccountNumber;
    }
}
