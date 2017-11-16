package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 脑补
 */
public class BraunRepairAdapter extends BaseAdapter implements View.OnClickListener{
    private List<RecentContentBean> list = new ArrayList<>();
    private Context context;
    private int screenHeight;

    public BraunRepairAdapter(Context context, List<RecentContentBean> list) {
        this.context = context;
        this.list = list;
        screenHeight = Util.getScreenWidth(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_braun, parent, false);
            viewHolder.imgadd = (ImageView) convertView.findViewById(R.id.img_braun);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_brauntitle);
            viewHolder.textPlay = (TextView) convertView.findViewById(R.id.txt_braunplay);
            viewHolder.imgShare= (ImageView) convertView.findViewById(R.id.img_braun_share);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        RecentContentBean contentBean = list.get(position);
        viewHolder.imgadd.setImageResource(contentBean.getImgUrl());
        viewHolder.textView.setText(contentBean.getTitle());
        viewHolder.textPlay.setText(contentBean.getAlbumName());
        int width = (int) (screenHeight * 0.89);
        int height = (int) ((width * 3) / 4);
        ViewGroup.LayoutParams params = viewHolder.imgadd.getLayoutParams();
        params.height = height;
        viewHolder.imgadd.setLayoutParams(params);

        viewHolder.imgShare.setTag(position);
        viewHolder.imgShare.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_braun_share:
                EventMsg msg = EventMsg.obtain(SystemConstant.NEW_FLASH_SHARE_CLICK);
                msg.setBody(v.getTag());
                EventBus.getDefault().post(msg);
                break;
        }
    }

    class ViewHolder {
        ImageView imgadd;
        ImageView imgShare;
        TextView textView;
        TextView textPlay;
    }
}
