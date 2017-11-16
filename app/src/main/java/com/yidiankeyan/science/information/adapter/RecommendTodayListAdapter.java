package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.service.MyLocalService;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yidiankeyan.science.R.id.line;


/**
 * Created by zn on 2016/7/7.
 * 推荐下日课列表
 */
public class RecommendTodayListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<OneDayArticles> mDatas = new ArrayList<>();
    private static final int MAX_ITEM_COUNT = 5;
    /**
     * 代表控制器的位置
     */
    private int selectPosition = -1;
    private ListView listView;

    /**
     * 正在播放的位置
     */
//    private int playPosition = -1;
    public RecommendTodayListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<OneDayArticles> mDatas) {
        this.mDatas.removeAll(this.mDatas);
        if (mDatas != null)
            this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        return Math.min(MAX_ITEM_COUNT, mDatas.size());
    }

    @Override
    public OneDayArticles getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_today_list, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.imgState = (ImageView) convertView.findViewById(R.id.img_state);
            holder.line = (View) convertView.findViewById(line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == mDatas.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.imgState.setVisibility(View.VISIBLE);
        holder.tvTitle.setText(mDatas.get(position).getTitle());
        if (mDatas.size() == 0) {
            holder.imgState.setVisibility(View.GONE);
        }
        if (TextUtils.equals(mDatas.get(position).getId(), AudioPlayManager.getInstance().getCurrId()) &&
                (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                        || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
            //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.defaultcolor));
            holder.imgState.setImageResource(R.drawable.icon_recomm_stops);
        } else {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.today_f6));
            holder.imgState.setImageResource(R.drawable.icon_recomm_plays);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AudioPlayManager.getInstance().isInited() || !(mDatas.get(position).getId()).equals(AudioPlayManager.getInstance().getCurrId())) {
                    if (!DB.getInstance(DemoApplication.applicationContext).newsIsLooked(mDatas.get(position).getId())) {
                        DB.getInstance(DemoApplication.applicationContext).updataNews(mDatas.get(position).getId());
                        mDatas.get(position).setReadnum(mDatas.get(position).getReadnum() + 1);
                        toHttpRead(mDatas.get(position).getId());
                    }
//                    //开始播放
                    Intent intent = new Intent(mContext, MyLocalService.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putParcelableArrayList("mDatas", (ArrayList<OneDayArticles>) mDatas);
                    intent.putExtras(bundle);
                    mContext.startService(intent);
                    DemoApplication.isPlay = true;
                    Log.i("start_play==", "start3");
                    DemoApplication.isBuy = true;
                    toHttpBehaviorAcquisition();
                } else {
                    switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                        case SystemConstant.ON_PREPARE:
                            //停止
                            AudioPlayManager.getInstance().release();
                            break;
                        case SystemConstant.ON_PAUSE:
                            //恢复播放
                            AudioPlayManager.getInstance().resume();
                            DemoApplication.isPlay = true;
                            break;
                        case SystemConstant.ON_PLAYING:
                            //暂停
                            AudioPlayManager.getInstance().pause();
                            DemoApplication.isPlay = false;
                    }
                }

                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    /**
     * 用户行为采集接口
     */
    private void toHttpBehaviorAcquisition() {
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.USER_BEHAVIOR_ACQUISITION_DAYLY, null, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    public List<OneDayArticles> getDatas() {
        return mDatas;
    }

    /**
     * 日课阅读量加1
     *
     * @param id 日课id
     */
    private void toHttpRead(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", SpUtils.getStringSp(mContext, "userId"));
        map.put("dailyarticleid", id);
        HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.READ_TODAY_AUDIO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {

            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    class ViewHolder {
        private TextView tvTitle;
        private ImageView imgState;
        private View line;
    }

}
