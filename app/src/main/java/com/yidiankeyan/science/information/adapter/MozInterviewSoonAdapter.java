package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/5/23 0023.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤            墨子专访Adapter
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozInterviewSoonAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ArrayList<InterviewSoonBean> mDatas = new ArrayList<>();
    private Context context;
    private List<Integer> lists;


    public MozInterviewSoonAdapter(Context context, ArrayList<InterviewSoonBean> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
        getRandomHeights(mDatas);
    }


    private void getRandomHeights(List<InterviewSoonBean> mDatas) {
        lists = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
//            lists.add((int) (200 + Math.random() * 400));
            lists.add(400);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interview_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = 400;//把随机的高度赋予item布局
        holder.itemView.setLayoutParams(params);
//        holder.tvContentName.setText(mDatas.get(position).getInterviewName());
        if (position == 0) {
            holder.tvTitle.setText(mDatas.get(position).getInterviewType().equals("0") ? "即将上线" : "往期专访");
            holder.llTitle.setVisibility(View.VISIBLE);
        }else{
            if (mDatas.get(position).getInterviewType().equals(mDatas.get(position-1).getInterviewType())) {
                holder.tvContentName.setText(mDatas.get(position).getInterviewName());
                holder.llTitle.setVisibility(View.GONE);
            }else{
                holder.tvTitle.setText(mDatas.get(position).getInterviewType().equals("0") ? "即将上线" : "往期专访");
                holder.tvTitle.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MozInterviewDetailsActivity.class);
                intent.putParcelableArrayListExtra("list", mDatas);
                intent.putExtra("id", mDatas.get(position).getId());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgInterviewBg;
    AutoLinearLayout llTitle;
    AutoRelativeLayout rlBgImg;
    TextView tvTitle;
    TextView tvContentName;

    public MyViewHolder(View itemView) {
        super(itemView);
        imgInterviewBg = (ImageView) itemView.findViewById(R.id.img_interview_bg);
        tvContentName = (TextView) itemView.findViewById(R.id.tv_content_name);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        llTitle = (AutoLinearLayout) itemView.findViewById(R.id.ll_title);
        rlBgImg = (AutoRelativeLayout) itemView.findViewById(R.id.rl_bg_img);
    }
}