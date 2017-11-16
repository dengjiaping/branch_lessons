package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.my.entity.ProfitDetailedBean;

import java.util.List;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/9/27 0027.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤         收益明细Adapter
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class ProfitDetailedAdapter extends BaseAdapter {

    private List<ProfitDetailedBean> mDatas;
    private Context context;

    public ProfitDetailedAdapter(Context context, List<ProfitDetailedBean> mDatas) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profit_detailed, parent, false);
            viewHolder.tvAccountName = (TextView) convertView.findViewById(R.id.tv_account_name);
            viewHolder.tvAccountTime = (TextView) convertView.findViewById(R.id.tv_account_time);
            viewHolder.tvAccountNumber = (TextView) convertView.findViewById(R.id.tv_account_number);
            viewHolder.imgProfit = (ImageView) convertView.findViewById(R.id.img_profit);
            viewHolder.tvDescribes = (TextView) convertView.findViewById(R.id.tv_describes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAccountName.setText(mDatas.get(position).getExplain());
        viewHolder.tvAccountTime.setText(mDatas.get(position).getTime().replace(".0", ""));
        if (mDatas.get(position).getIsUpdate() == 1) {
            viewHolder.imgProfit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgProfit.setVisibility(View.GONE);
        }
        if (TextUtils.equals("1", mDatas.get(position).getType())) {
            viewHolder.tvAccountNumber.setText("-" + mDatas.get(position).getMoney() + " 元");
        } else {
            viewHolder.tvAccountNumber.setText("+" + mDatas.get(position).getMoney() + " 元");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getDescribes()) && !TextUtils.equals("null", mDatas.get(position).getDescribes())) {
            viewHolder.tvDescribes.setText(mDatas.get(position).getDescribes());
        } else {
            viewHolder.tvDescribes.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvAccountName;
        TextView tvAccountTime;
        TextView tvAccountNumber;
        ImageView imgProfit;
        TextView tvDescribes;
    }
}
