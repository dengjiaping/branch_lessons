package com.yidiankeyan.science.information.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.acitivity.ClassAudioActivity;
import com.yidiankeyan.science.information.acitivity.ClassClickItemActivity;
import com.yidiankeyan.science.information.acitivity.ClassImaTxtActivity;
import com.yidiankeyan.science.information.acitivity.ClassVideoActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.ShowAllGridview;
import com.yidiankeyan.science.view.drag.bean.ChannelItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 资讯-分类
 */
public class ClassificationFragment extends Fragment implements View.OnClickListener {

    private ShowAllGridview gridViewBottom;
    private ImageButton btnVideo, btnImgTxt, btnAudio;
    private Intent intent;
    private View footView;
    private List<ChannelItem> projects;
    private ImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classification, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        projects = DB.getInstance(DemoApplication.applicationContext).queryProject();
        if (projects.size() == 0) {
            toHttpGetProject();
        } else {
            //2  41
            Iterator<ChannelItem> iterator = projects.iterator();
            while (iterator.hasNext()) {
                ChannelItem channelItem = iterator.next();
                if (channelItem.getSubjectid() == 2 || channelItem.getSubjectid() == 41)
                    iterator.remove();
            }
        }
        adapter = new ImageAdapter(getContext());
        gridViewBottom.setAdapter(adapter);
        //单击GridView元素的响应
        gridViewBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), ClassClickItemActivity.class);
                intent.putExtra("id", projects.get(position).getId());
                intent.putExtra("title", projects.get(position).getName());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取专题
     */
    private void toHttpGetProject() {
        Map<String, Object> map = new HashMap<>();
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_PROJECT, map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(final ResultEntity result) {
                if (result.getCode() == 200) {
                    final String jsonData = GsonUtils.obj2Json(result.getData());
                    DB.getInstance(DemoApplication.applicationContext).deleteAllProject();
                    final List<ChannelItem> mDatas = GsonUtils.json2List(jsonData, ChannelItem.class);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DB.getInstance(DemoApplication.applicationContext).insertProject(mDatas);
                        }
                    }).start();
                    projects.addAll(mDatas);
                    Iterator<ChannelItem> iterator = projects.iterator();
                    while (iterator.hasNext()) {
                        ChannelItem channelItem = iterator.next();
                        if (channelItem.getSubjectid() == 2 || channelItem.getSubjectid() == 41)
                            iterator.remove();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void initView(View view) {
        gridViewBottom = (ShowAllGridview) view.findViewById(R.id.gridview_bottom);
        btnVideo = (ImageButton) view.findViewById(R.id.btn_clsvideo);
        btnImgTxt = (ImageButton) view.findViewById(R.id.btn_clsimgtxt);
        btnAudio = (ImageButton) view.findViewById(R.id.btn_clsaudio);

        btnVideo.setOnClickListener(this);
        btnImgTxt.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clsimgtxt:
                intent = new Intent(getContext(), ClassImaTxtActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clsaudio:
                intent = new Intent(getContext(), ClassAudioActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clsvideo:
                intent = new Intent(getContext(), ClassVideoActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return projects.size();
        }

        @Override
        public Object getItem(int position) {
            return projects.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //定义一个ImageView,显示在GridView里
            ImageView imageView;
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_class_image, parent, false);
                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    (int) (SystemConstant.ScreenHeight / 6.5));
            convertView.setLayoutParams(param);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, (int) (SystemConstant.ScreenHeight / 6.5 / 6));
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.imgAvatar.setLayoutParams(lp);
//            if (projects.get(position).getSubjectid() >= 0 && projects.get(position).getSubjectid() < 41) {
//                if (projects.get(position).getSubjectid() == 2) {
            holder.imgAvatar.setImageResource(0);
//                } else {
//                    holder.imgAvatar.setImageResource(Util.getResId("icon_clx_big_" + (projects.get(position).getSubjectid() - 1), R.drawable.class));
//                }
//            }
            return convertView;
        }

        class ViewHolder {
            private ImageView imgAvatar;
        }
    }

}
