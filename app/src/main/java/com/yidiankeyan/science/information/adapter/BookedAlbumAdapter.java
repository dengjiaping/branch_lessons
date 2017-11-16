package com.yidiankeyan.science.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 牛人
 * -最热专辑Adapter
 */
public class BookedAlbumAdapter extends BaseAdapter implements View.OnClickListener {
    private List<BusinessAllBean> mDatas;
    private Context context;
    private View llBooked;
    private PopupWindow mPopupWindow;
    private AutoLinearLayout ll_zhiding;
    private AutoLinearLayout ll_bottom;
    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public BookedAlbumAdapter(Context context, List<BusinessAllBean> mDatas) {
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

    public View getLlBooked() {
        return llBooked;
    }

    public void setLlBooked(View llBooked) {
        this.llBooked = llBooked;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (lmap.get(position) == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_subscribe, parent, false);
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
//        if (TextUtils.isEmpty(allBean.getOrderid())) {
//            viewHolder.textView.setText(allBean.getTitle());
//            viewHolder.imgAdd.setImageResource(allBean.getImgUrl());
//            viewHolder.txtContent.setText(allBean.getContentId() + allBean.getContent());
//            viewHolder.txtName.setText(allBean.getName());
//            viewHolder.txtNumber.setText(allBean.getContentNumber() + "篇");
//            viewHolder.txtTou.setText(allBean.getToPeekNumber() + "");
//        } else {
        viewHolder.textView.setText(allBean.getAlbumname());
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl())).into(viewHolder.imgAdd);
        viewHolder.txtContent.setText(allBean.getRecenttitle());
        if (!TextUtils.isEmpty(allBean.getAlbumauthor()) & !TextUtils.equals("null", allBean.getAlbumauthor())) {
            viewHolder.txtName.setVisibility(View.VISIBLE);
            viewHolder.txtName.setText(allBean.getAlbumauthor());
        } else {
            viewHolder.txtName.setVisibility(View.GONE);
        }
        viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
        viewHolder.txtTou.setText("浏览量" + allBean.getReadnum() + "");
        if (allBean.getUpdates() != 0)
            viewHolder.txtUpTime.setText(allBean.getUpdates() + "更新");
        else
            viewHolder.txtUpTime.setText(TimeUtils.formatDate(allBean.getRecentupdatetime()));
//        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.txtUpTime.setText(allBean.getUpdateTime());
                viewHolder.txtUpTime.setTextColor(Color.parseColor("#b9b9b9"));
                Intent intent = new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getName());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                context.startActivity(intent);
            }
        });

        if (mDatas.get(position).getIstop() == 1) {
            viewHolder.imgSubscribeTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSubscribeTop.setVisibility(View.GONE);
        }


        return convertView;
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_class_xiala:
//                final ViewHolder viewHolder = (ViewHolder) v.getTag();
//                if (mPopupWindow == null) {
//                    View view = View.inflate(context, R.layout.alert_dialog, null);
//                    //item置顶
//                    ll_zhiding = (AutoLinearLayout) view.findViewById(R.id.ll_top);
//                    ll_bottom = (AutoLinearLayout) view.findViewById(R.id.ll_bottom);
//                    mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                    mPopupWindow.setContentView(view);
//                    mPopupWindow.setAnimationStyle(R.style.AnimBottom);
//                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//                    lp.alpha = 0.6f;
//                    ((Activity) context).getWindow().setAttributes(lp);
//                    mPopupWindow.setFocusable(true);
//                    mPopupWindow.setOutsideTouchable(true);
//                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//                    mPopupWindow.showAtLocation(llBooked, Gravity.BOTTOM, 0, 0);
//                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            finishPop(mPopupWindow);
//                        }
//                    });
//                } else {
//                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//                    lp.alpha = 0.6f;
//                    ((Activity) context).getWindow().setAttributes(lp);
//                    mPopupWindow.showAtLocation(llBooked, Gravity.BOTTOM, 0, 0);
//                }
//
//
//                if (mDatas.get(viewHolder.position).getIstop() == 0) {
//                    ll_bottom.setVisibility(View.GONE);
//                    ll_zhiding.setVisibility(View.VISIBLE);
//                } else {
//                    ll_zhiding.setVisibility(View.GONE);
//                    ll_bottom.setVisibility(View.VISIBLE);
//                }
//
////                if (mDatas.get(viewHolder.position).getIstop() == 1)
//                ll_zhiding.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_TOP);
//                        msg.setBody(mDatas.get(viewHolder.position).getOrderid());
//                        EventBus.getDefault().post(msg);
//                        //数组置顶
////                      BusinessAllBean s = mDatas.get(viewHolder.position);
////                      mDatas.remove(viewHolder.position);
////                      mDatas.add(0, s);
////                      notifyDataSetChanged();
//                        finishPop(mPopupWindow);
//                    }
//                });
//
//
//                ll_bottom.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EventMsg msg = EventMsg.obtain(SystemConstant.ON_CLICK_BOTTOM);
//                        msg.setBody(mDatas.get(viewHolder.position).getOrderid());
//                        EventBus.getDefault().post(msg);
//                        finishPop(mPopupWindow);
//                    }
//                });
//
//                //取消弹出框
//                LinearLayout ll_cancel = (LinearLayout) mPopupWindow.getContentView().findViewById(R.id.ll_sub_cancel);
//                ll_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finishPop(mPopupWindow);
//                    }
//                });
//                mPopupWindow.getContentView().findViewById(R.id.ll_cancelsub).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finishPop(mPopupWindow);
//                        ((BaseActivity) context).showLoadingDialog("正在操作");
//                        toHttpAbolishOrder(viewHolder);
//                    }
//                });
//                break;
//        }
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

}
