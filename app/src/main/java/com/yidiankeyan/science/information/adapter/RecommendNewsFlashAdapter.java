package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.NewsFlashActivity;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.ExpandableTextView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快讯
 */
public class RecommendNewsFlashAdapter extends BaseAdapter implements View.OnClickListener {

    private List<FlashBean> list;
    private Context context;
    private final SparseBooleanArray mCollapsedStatus;

    public RecommendNewsFlashAdapter(Context context, List<FlashBean> list) {
        this.context = context;
        this.list = list;
        mCollapsedStatus = new SparseBooleanArray();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_newsflash, parent, false);
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            viewHolder.tvLookCount = (TextView) convertView.findViewById(R.id.tv_look_count);
            viewHolder.llShare = (AutoLinearLayout) convertView.findViewById(R.id.ll_share);
            viewHolder.rlLoveClick = (AutoRelativeLayout) convertView.findViewById(R.id.rl_praise_num);
            viewHolder.imgClick = (ImageView) convertView.findViewById(R.id.img_click);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置文本
        ForegroundColorSpan blueSpan1 = new ForegroundColorSpan(
                context.getResources().getColor(
                        R.color.account_balance1));
        ForegroundColorSpan blueSpan2 = new ForegroundColorSpan(
                context.getResources().getColor(
                        R.color.black_33));
        String time = TimeUtils.formatDateTime(list.get(position).getCreatetime());
        String title = list.get(position).getTitle();
        String content = "【" + title + "】" + list.get(position).getContent();
        SpannableStringBuilder builder = null;
        builder = new SpannableStringBuilder(time + "  " + content);
        builder.setSpan(blueSpan1, 0, time.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//

        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length() + time.length() + 4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//);

        builder.setSpan(blueSpan2, time.length(),
                content.length() + time.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.expandableTextView.setText(builder, mCollapsedStatus, position);
        viewHolder.expandableTextView.setFakeBoldText(false);
        viewHolder.tvLookCount.setText(list.get(position).getPraisenum() + "");
        if (list.get(position).getIsPraised() == 1) {
            viewHolder.imgClick.setImageResource(R.drawable.icon_newsflsh_click_pressed);
        } else {
            viewHolder.imgClick.setImageResource(R.drawable.icon_newsflash_click_normal);
        }
        viewHolder.position = position;
        viewHolder.expandableTextView.setTag(position);
        viewHolder.expandableTextView.setOnInnerTextClickListener(new ExpandableTextView.OnInnerTextClickListener() {
            @Override
            public void onInnerTextClick(ExpandableTextView expandableTextView, TextView mTv, boolean lessThreeLine) {
                context.startActivity(new Intent(context, NewsFlashActivity.class));
            }
        });
        viewHolder.llShare.setTag(position);
        viewHolder.llShare.setOnClickListener(this);
        viewHolder.rlLoveClick.setTag(position);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.rlLoveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.hintLogin((BaseActivity) context)) return;
                if (finalViewHolder.imgClick.getDrawable().getCurrent().getConstantState().equals(context.getResources().getDrawable(R.drawable.icon_newsflsh_click_pressed).getConstantState())) {
//                if (list.get(position).getIsPraised() == 1) {
                    Toast.makeText(context, "你已经赞过了", Toast.LENGTH_SHORT).show();
                } else {
                    toHttpLoveClick(list.get(finalViewHolder.position).getId(), finalViewHolder);
                }

            }
        });
        return convertView;
    }

    private void toHttpLoveClick(String id, final ViewHolder finalViewHolder) {
        Map<String, Object> map = new HashMap<>();
        map.put("uptype", "FLASHNEWS");
        map.put("targetid", id);
        HttpUtil.post(context, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    finalViewHolder.imgClick.setImageResource(R.drawable.icon_newsflsh_click_pressed);
                    finalViewHolder.tvLookCount.setText((Integer.parseInt(finalViewHolder.tvLookCount.getText().toString()) + 1) + "");
                    ToastMaker.showShortToast("点赞成功");
                } else if (result.getCode() == 306) {
                    ToastMaker.showShortToast("您已经点过赞了");
                } else {
                    ToastMaker.showShortToast("点赞失败");
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share:
                EventMsg msg = EventMsg.obtain(SystemConstant.NEW_FLASH_SHARE_CLICK);
                msg.setBody(v.getTag());
                EventBus.getDefault().post(msg);
                break;
        }
    }

    class ViewHolder {
        LinearLayout layout;
        ExpandableTextView expandableTextView;
        private TextView tvLookCount;
        private AutoLinearLayout llShare;
        private AutoRelativeLayout rlLoveClick;
        private ImageView imgClick;
        public int position;
    }
}
