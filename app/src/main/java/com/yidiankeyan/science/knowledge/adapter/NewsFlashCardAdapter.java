package com.yidiankeyan.science.knowledge.adapter;

import android.graphics.Rect;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.card.CardAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2017/7/17.
 * 作用：
 */

public class NewsFlashCardAdapter extends CardAdapter {

    private List<FlashBean> listAll;
    private BaseActivity activity;

    public NewsFlashCardAdapter(BaseActivity activity, List<FlashBean> listAll) {
        this.listAll = listAll;
        this.activity = activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_flash_card;
    }

    @Override
    public int getCount() {
        return listAll.size();
    }

    @Override
    public void bindView(View view, final int index) {
        Object tag = view.getTag();
        final ViewHolder holder;
        if (tag == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) tag;
        }
        holder.tvContent.setText(listAll.get(index).getContent());
        holder.tvTime.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(new Date(listAll.get(index).getCreatetime())));
        holder.imgWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(SHARE_MEDIA.WEIXIN, listAll.get(index).getId(), holder.etComment.getText().toString());
            }
        });
        holder.imgQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(SHARE_MEDIA.QQ, listAll.get(index).getId(), holder.etComment.getText().toString());
            }
        });
        holder.imgWxCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(SHARE_MEDIA.WEIXIN_CIRCLE, listAll.get(index).getId(), holder.etComment.getText().toString());
            }
        });
    }

    private void share(final SHARE_MEDIA platform, String id, String text) {
        activity.showLoadingDialog("请稍后");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("comment", text);
        map.put("userimg", SpUtils.getStringSp(DemoApplication.applicationContext, "userimgurl"));
        map.put("username", SpUtils.getStringSp(DemoApplication.applicationContext, "userName"));
        ApiServerManager.getInstance().getWebApiServer().createNewsShareImg(map).enqueue(new RetrofitCallBack<String>() {
            @Override
            public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                activity.loadingDismiss();
                if (response.body().getCode() == 200) {
                    activity.shareImage(
                            platform,
                            "快讯",
                            Util.getImgUrl(response.body().getData()),
                            null,
                            null
                    );
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {
                activity.loadingDismiss();
            }
        });
    }

    @Override
    public Rect obtainDraggableArea(View view) {
        // 可滑动区域定制，该函数只会调用一次
        View contentView = view.findViewById(R.id.content_view);
        return new Rect(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    class ViewHolder {
        private TextView tvTime;
        private ImageView imgWx;
        private ImageView imgWxCircle;
        private ImageView imgQq;
        private TextView tvContent;
        private EditText etComment;

        public ViewHolder(View view) {
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            imgWx = (ImageView) view.findViewById(R.id.img_wx);
            imgWxCircle = (ImageView) view.findViewById(R.id.img_wx_circle);
            imgQq = (ImageView) view.findViewById(R.id.img_qq);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            etComment = (EditText) view.findViewById(R.id.et_comment);
        }
    }
}
