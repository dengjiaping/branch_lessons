package com.yidiankeyan.science.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.ClassVideoActivity;
import com.yidiankeyan.science.information.entity.BookedAlbumBean;
import com.yidiankeyan.science.subscribe.activity.TransitionActivity;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
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
 * 分类
 * -音频已订专辑Adapter
 */
public class ClassVideosAdapter extends BaseAdapter implements View.OnClickListener {
    private List<BookedAlbumBean> mDatas;
    private Context context;
    private AutoLinearLayout llClassTxt;
    private PopupWindow mPopupWindow;
    private AutoLinearLayout ll_zhiding;
    private AutoLinearLayout ll_bottom;
    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public ClassVideosAdapter(Context context, List<BookedAlbumBean> mDatas) {
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

    public AutoLinearLayout getLlClassTxt() {
        return llClassTxt;
    }

    public void setLlClassTxt(AutoLinearLayout llClassTxt) {
        this.llClassTxt = llClassTxt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (lmap.get(position) == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classbooked, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            lmap.put(position, convertView);
        } else {
            convertView = lmap.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        viewHolder.btnXiala.setTag(viewHolder);
        viewHolder.btnXiala.setOnClickListener(this);

        final BookedAlbumBean allBean = mDatas.get(position);
//        switch (allBean.getAlbumtype()) {
//            case 1:
//                viewHolder.imgNameType.setImageResource(R.drawable.imgtxt);
//                break;
//            case 2:
//                viewHolder.imgNameType.setImageResource(R.drawable.audio);
//                break;
//            case 3:
//                viewHolder.imgNameType.setImageResource(R.drawable.video);
//                break;
//        }

        viewHolder.txtContent.setText(allBean.getRecenttitle());
        viewHolder.txtTitle.setText(allBean.getAlbumname());
        if (!TextUtils.isEmpty(allBean.getAuthorname()) && !TextUtils.equals("null", allBean.getAuthorname())) {
            viewHolder.txtName.setVisibility(View.VISIBLE);
            viewHolder.txtName.setText(allBean.getAuthorname());
        } else {
            viewHolder.txtName.setVisibility(View.GONE);
        }
        viewHolder.txtNumber.setText("内容量" + allBean.getContentnum() + "篇");
        Glide.with(context).load(Util.getImgUrl(allBean.getCoverimgurl()))
                .error(R.drawable.icon_hotload_failed)
                .placeholder(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);
        viewHolder.txtTou.setText("浏览量" + allBean.getReadnum());
        viewHolder.txtUpTime.setText(TimeUtils.questionCreateDuration(allBean.getRecentupdatetime()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                viewHolder.txtUpTime.setText(TimeUtils.questionCreateDuration(allBean.getRecentupdatetime()));
                viewHolder.txtUpTime.setTextColor(Color.parseColor("#f1312e"));
                Intent intent = new Intent(context, TransitionActivity.class);
                intent.putExtra("albumId", mDatas.get(position).getAlbumid());
                intent.putExtra("albumName", mDatas.get(position).getAlbumname());
                intent.putExtra("albumAvatar", mDatas.get(position).getCoverimgurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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


//    private void showPop(final int position) {
//        if (mPopupWindow == null) {
//            View view = View.inflate(context, R.layout.alert_dialog, null);
//            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPopupWindow.setContentView(view);
//            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
//            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            ((Activity) context).getWindow().setAttributes(lp);
//            mPopupWindow.setFocusable(true);
//            mPopupWindow.setOutsideTouchable(true);
//            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//            mPopupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_all), Gravity.BOTTOM, 0, 0);
//            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    Util.finishPop((Activity) context, mPopupWindow);
//                }
//            });
//        } else {
//            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            ((Activity) context).getWindow().setAttributes(lp);
//            mPopupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_all), Gravity.BOTTOM, 0, 0);
//        }
//        //取消弹出框
//        LinearLayout ll_cancel = (LinearLayout) mPopupWindow.getContentView().findViewById(R.id.ll_sub_cancel);
//        ll_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.finishPop((Activity) context, mPopupWindow);
//            }
//        });
//        mPopupWindow.getContentView().findViewById(R.id.ll_cancelsub).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.finishPop((Activity) context, mPopupWindow);
//                ((ClassImaTxtActivity) context).showLoadingDialog("正在操作");
//                toHttpAbolishOrder(position);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_xiala:
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
                    mPopupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_class_video), Gravity.BOTTOM, 0, 0);
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            Util.finishPop((Activity) context, mPopupWindow);
                        }
                    });
                } else {
                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    ((Activity) context).getWindow().setAttributes(lp);
                    mPopupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_class_video), Gravity.BOTTOM, 0, 0);
                }

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

                ll_zhiding.setVisibility(View.GONE);


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
                        ((ClassVideoActivity) context).showLoadingDialog("正在操作");
                        toHttpAbolishOrder(viewHolder);
                    }
                });
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
                    ((ClassVideoActivity) context).loadingDismiss();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "操作失败，请检查网络", Toast.LENGTH_SHORT);
                ((ClassVideoActivity) context).loadingDismiss();
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
        ImageView imgAdd, btnXiala, imgSubscribeTop, imgNameType;
        TextView txtUpTime, txtTou, txtNumber, txtName, txtContent, txtTitle;
        int position;

        public ViewHolder(View convertView) {
            txtTitle = (TextView) convertView.findViewById(R.id.sub_title);
            btnXiala = (ImageView) convertView.findViewById(R.id.btn_xiala);
            imgAdd = (ImageView) convertView.findViewById(R.id.sub_item_button);
            txtContent = (TextView) convertView.findViewById(R.id.title_sub);
            txtName = (TextView) convertView.findViewById(R.id.txt_zhuname);
            txtNumber = (TextView) convertView.findViewById(R.id.txt_neirong);
            txtTou = (TextView) convertView.findViewById(R.id.yuedu);
            txtUpTime = (TextView) convertView.findViewById(R.id.txt_gengxin);
            imgSubscribeTop = (ImageView) convertView.findViewById(R.id.img_subscribe_Top);
            imgNameType = (ImageView) convertView.findViewById(R.id.sub_nametype);
        }
    }

}
