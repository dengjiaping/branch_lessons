package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MagazineDetailsActivity;
import com.yidiankeyan.science.information.entity.MonthlySeriesBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/28 0028.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                        -月刊系列列表Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MonthlyAllAdapter extends RecyclerAdapter<MonthlySeriesBean> {
    public MonthlyAllAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<MonthlySeriesBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<MonthlySeriesBean> {
        ImageView imgCoverUrl;
        TextView tvName;
        TextView tvAudioLength;
        TextView tvDesc;
        TextView tvAuthor;
        TextView tvReaders;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_monthly_listall);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgCoverUrl = (ImageView) findViewById(R.id.img_author);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvAudioLength = (TextView) findViewById(R.id.tv_audio_length);
            tvAuthor = (TextView) findViewById(R.id.tv_author);
            tvReaders = (TextView) findViewById(R.id.tv_readers);
            tvDesc = (TextView) findViewById(R.id.tv_desc);
        }

        @Override
        public void setData(MonthlySeriesBean data) {
            super.setData(data);
            if (data.getMonthlyDB().getCoverimg() != null && data.getMonthlyDB().getCoverimg().startsWith("/"))
                Glide.with(getContext()).load(Util.getImgUrl(data.getMonthlyDB().getCoverimg())).placeholder(R.drawable.icon_readload_failed)
                        .error(R.drawable.icon_readload_failed).into(imgCoverUrl);
            else
                Glide.with(getContext()).load(SystemConstant.ACCESS_IMG_HOST + "/" + data.getMonthlyDB().getCoverimg()).placeholder(R.drawable.icon_readload_failed)
                        .error(R.drawable.icon_readload_failed).into(imgCoverUrl);

            tvAuthor.setText(data.getMonthlyDB().getAuthor());
            tvReaders.setText(data.getMonthlyDB().getInterpreter());
            tvName.setText(data.getMonthlyDB().getName());
            tvAudioLength.setText(TimeUtils.formatSeriesTime(data.getMonthlyDB().getLength()) + "");
            tvDesc.setText(data.getMonthlyDB().getDesc());
        }

        @Override
        public void onItemViewClick(MonthlySeriesBean data) {
            super.onItemViewClick(data);
            Intent intent = new Intent(getContext(), MagazineDetailsActivity.class);
            intent.putExtra("id", data.getMonthlyDB().getId());
            intent.putExtra("name", data.getMonthlyDB().getName());
            getContext().startActivity(intent);
        }
    }

//    private Context mContext;
//    private List<MonthlySeriesBean> mList;
//
//    public MonthlyAllAdapter(List<MonthlySeriesBean> mList, Context mContext) {
//        super();
//        this.mList = mList;
//        this.mContext = mContext;
//    }
//
//    @Override
//    public int getCount() {
//        if (mList != null) {
//            return mList.size();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_monthly_listall, null, false);
//            holder.imgCoverUrl = (ImageView) convertView.findViewById(R.id.img_author);
//            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//            holder.tvAudioLength = (TextView) convertView.findViewById(R.id.tv_audio_length);
//            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
//            holder.tvReaders = (TextView) convertView.findViewById(R.id.tv_readers);
//            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        if (mList.get(position).getMonthlyDB().getCoverimg() != null && mList.get(position).getMonthlyDB().getCoverimg().startsWith("/"))
//            Glide.with(mContext).load(Util.getImgUrl(mList.get(position).getMonthlyDB().getCoverimg())).placeholder(R.drawable.icon_readload_failed)
//                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);
//        else
//            Glide.with(mContext).load(SystemConstant.ACCESS_IMG_HOST + "/" + mList.get(position).getMonthlyDB().getCoverimg()).placeholder(R.drawable.icon_readload_failed)
//                    .error(R.drawable.icon_readload_failed).into(holder.imgCoverUrl);
//
//        holder.tvAuthor.setText(mList.get(position).getMonthlyDB().getAuthor());
//        holder.tvReaders.setText(mList.get(position).getMonthlyDB().getInterpreter());
//        holder.tvName.setText(mList.get(position).getMonthlyDB().getName());
//        holder.tvAudioLength.setText("音频时长：" + TimeUtils.formatSeriesTime(mList.get(position).getMonthlyDB().getLength()) + "");
//        holder.tvDesc.setText(mList.get(position).getMonthlyDB().getDesc());
//
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MagazineDetailsActivity.class);
//                intent.putExtra("id", mList.get(position).getMonthlyDB().getId());
//                intent.putExtra("name", mList.get(position).getMonthlyDB().getName());
//                mContext.startActivity(intent);
//            }
//        });
//        return convertView;
//    }
//
//
//    private class ViewHolder {
//        ImageView imgCoverUrl;
//        TextView tvName;
//        TextView tvAudioLength;
//        TextView tvDesc;
//        TextView tvAuthor;
//        TextView tvReaders;
//    }


}
