package com.yidiankeyan.science.subscribe.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订阅listview的Adapter
 */
public class MySubscribeAdapter extends BaseAdapter implements View.OnClickListener {

    private List<BusinessAllBean> mDatas;
    private Context context;
    private View ll_all;
    private PopupWindow mPopupWindow;
    private OnItemClickListener onItemClickListener;
    private AutoLinearLayout ll_zhiding;
    private AutoLinearLayout ll_bottom;
    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private OnAbolishOrderListener onAbolishOrderListener;


    public MySubscribeAdapter(Context context, List<BusinessAllBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    public View getLl_all() {
        return ll_all;
    }

    public void setLl_all(View ll_all) {
        this.ll_all = ll_all;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (lmap.get(position) == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ablum_subscribe, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            lmap.put(position, convertView);
        } else {
            convertView = lmap.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.btn_xiala.setTag(viewHolder);
        viewHolder.btn_xiala.setOnClickListener(this);

        final BusinessAllBean allBean = mDatas.get(position);
        viewHolder.txtContent.setText(allBean.getRecenttitle());
        if (!TextUtils.isEmpty(allBean.getAuthorname()) && !TextUtils.equals("null", allBean.getAuthorname())) {
            viewHolder.txtName.setVisibility(View.VISIBLE);
            viewHolder.txtName.setText(allBean.getAuthorname());
        } else {
            viewHolder.txtName.setVisibility(View.GONE);
            viewHolder.txtName.setText("");
        }

        viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
        viewHolder.textView.setText(allBean.getAlbumname());
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);

        viewHolder.txtTou.setText("浏览量" + allBean.getReadnum());
//        viewHolder.txtUpTime.setText(TimeUtils.questionCreateDuration(allBean.getRecentupdatetime()));
        if (allBean.getUpdates() == 0)
            viewHolder.txtUpTime.setVisibility(View.GONE);
        else {
            viewHolder.txtUpTime.setVisibility(View.VISIBLE);
            viewHolder.txtUpTime.setText(allBean.getUpdates() + "更新");
        }
        if (onItemClickListener != null) {
            ((ViewHolder) convertView.getTag()).position = position;
            convertView.setOnClickListener(this);
        }
        if (mDatas.get(position).getIstop() == 1) {
            viewHolder.imgSubscribeTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSubscribeTop.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_class_xiala:
                final ViewHolder viewHolder = (ViewHolder) v.getTag();
                if (mPopupWindow == null) {
                    View view = View.inflate(context, R.layout.alert_dialog, null);
                    //item置顶
                    ll_zhiding = (AutoLinearLayout) view.findViewById(R.id.ll_top);
                    ll_bottom = (AutoLinearLayout) view.findViewById(R.id.ll_bottom);
                    mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setContentView(view);
                    mPopupWindow.setAnimationStyle(R.style.AnimBottom);
                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    ((Activity) context).getWindow().setAttributes(lp);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopupWindow.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            finishPop(mPopupWindow);
                        }
                    });
                } else {
                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    ((Activity) context).getWindow().setAttributes(lp);
                    mPopupWindow.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
                }


                if (mDatas.get(viewHolder.position).getIstop() == 0) {
                    ll_bottom.setVisibility(View.GONE);
                    ll_zhiding.setVisibility(View.VISIBLE);
                } else {
                    ll_zhiding.setVisibility(View.GONE);
                    ll_bottom.setVisibility(View.VISIBLE);
                }

//                if (mDatas.get(viewHolder.position).getIstop() == 1)
                ll_zhiding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_TOP);
                        msg.setBody(mDatas.get(viewHolder.position).getOrderid());
                        EventBus.getDefault().post(msg);
                        //数组置顶
//                      BusinessAllBean s = mDatas.get(viewHolder.position);
//                      mDatas.remove(viewHolder.position);
//                      mDatas.add(0, s);
//                      notifyDataSetChanged();
                        finishPop(mPopupWindow);
                    }
                });


                ll_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_BOTTOM);
                        msg.setBody(mDatas.get(viewHolder.position).getOrderid());
                        EventBus.getDefault().post(msg);
                        finishPop(mPopupWindow);
                    }
                });

                //取消弹出框
                LinearLayout ll_cancel = (LinearLayout) mPopupWindow.getContentView().findViewById(R.id.ll_sub_cancel);
                ll_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishPop(mPopupWindow);
                    }
                });
                mPopupWindow.getContentView().findViewById(R.id.ll_cancelsub).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishPop(mPopupWindow);
                        ((BaseActivity) context).showLoadingDialog("正在操作");
                        toHttpAbolishOrder(viewHolder);
                    }
                });
                break;
            case R.id.rl_all:
                LogUtils.e("adapter onclick");
                onItemClickListener.onItemClick(v, ((ViewHolder) v.getTag()).position);
                break;
        }
    }

    private void toHttpAbolishOrder(final ViewHolder viewHolder) {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", mDatas.get(viewHolder.position).getAlbumid());
        HttpUtil.post(context, SystemConstant.URL + SystemConstant.ORDER_ABOLISH_ALBUM, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (200 == result.getCode()) {
                    mDatas.remove(viewHolder.position);
                    notifyDataSetChanged();
                    ((BaseActivity) context).loadingDismiss();
                    if (onAbolishOrderListener != null)
                        onAbolishOrderListener.OnAbolishOrder();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "操作失败，请检查网络", Toast.LENGTH_SHORT);
                ((BaseActivity) context).loadingDismiss();
            }
        });
    }

    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 1.0f;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void setOnAbolishOrderListener(OnAbolishOrderListener onAbolishOrderListener) {
        this.onAbolishOrderListener = onAbolishOrderListener;
    }

    class ViewHolder {
        ImageView imgAdd, btn_xiala, imgSubscribeTop;
        TextView textView, txtUpTime, txtTou, txtNumber, txtName, txtContent;
        int position;

        public ViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.sub_title);
            btn_xiala = (ImageView) convertView.findViewById(R.id.btn_class_xiala);
            imgAdd = (ImageView) convertView.findViewById(R.id.sub_item_button);
            txtContent = (TextView) convertView.findViewById(R.id.title_subscrjbe);
            txtName = (TextView) convertView.findViewById(R.id.txt_zhuname);
            txtNumber = (TextView) convertView.findViewById(R.id.txt_neirong);
            txtTou = (TextView) convertView.findViewById(R.id.yuedu);
            txtUpTime = (TextView) convertView.findViewById(R.id.txt_gengxin);
            imgSubscribeTop = (ImageView) convertView.findViewById(R.id.img_subscribe_Top);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnAbolishOrderListener {
        void OnAbolishOrder();
    }

}
