package com.yidiankeyan.science.subscribe.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.subscribe.adapter.AlbumDetailAuthorAlbumAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.AlbumDetailAuthorAlbum;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订阅-专辑详情-详情
 */
public class DetauksFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private List<AlbumDetailAuthorAlbum> listItem = new ArrayList<>();
    private TextView tvContentDes;
    private TextView tvAuthorName;
    private TextView tvFansCount;
    private ImageView imgPortrait;
    private ImageButton btnFollow;
    private boolean isFo;
    private AutoLinearLayout llMdetails;
    private PopupWindow mPopupWindow;
    private TextView tvFinish, tvYesClick;
    private AlbumDetail albumDetail;

    private AlbumDetailAuthorAlbumAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.lv_detailsitem);
        View mainin = LayoutInflater.from(getContext()).inflate(R.layout.activity_album_details, null);
        llMdetails = (AutoLinearLayout) mainin.findViewById(R.id.ll_mdetails);
        View inflater = LayoutInflater.from(getContext()).inflate(R.layout.albumdetails_item, null);
        tvContentDes = (TextView) inflater.findViewById(R.id.tv_content_des);
        tvAuthorName = (TextView) inflater.findViewById(R.id.tv_author_name);
        tvFansCount = (TextView) inflater.findViewById(R.id.tv_fans_count);
        imgPortrait = (ImageView) inflater.findViewById(R.id.img_editor_head_portrait);
        btnFollow = (ImageButton) inflater.findViewById(R.id.btn_album_details);

        btnFollow.setImageResource(R.drawable.follow);
        listView.addHeaderView(inflater);
//        LvConAdapter adapter = new LvConAdapter(listItem, getActivity());
        adapter = new AlbumDetailAuthorAlbumAdapter(getContext(), listItem);
        listView.setAdapter(adapter);
        btnFollow.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                if (position >= 0) {
                    Intent intent = new Intent(getContext(), AlbumDetailsActivity.class);
                    intent.putExtra("albumId", listItem.get(position).getAlbumid());
                    intent.putExtra("albumName", listItem.get(position).getAlbumname());
                    intent.putExtra("albumAvatar", listItem.get(position).getCoverimgurl());
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }


    /**
     * 获取到了专辑详情信息
     *
     * @param albumDetail 专辑详情
     */
    public void loadInfo(AlbumDetail albumDetail) {
        this.albumDetail = albumDetail;
        tvContentDes.setText(albumDetail.getDescribes());
        tvAuthorName.setText(albumDetail.getAuthorname());
        albumDetail.getFollowersnum();
        if (albumDetail.getFollowersnum() == 0) {
            tvFansCount.setText("喜欢就关注吧→");
        } else {
            tvFansCount.setText("已被" + albumDetail.getFollowersnum() + "人关注");
        }
        Glide.with(this).load(Util.getImgUrl(albumDetail.getAuthorimgurl())).placeholder(R.drawable.icon_default_avatar)
                .error(R.drawable.icon_default_avatar).into(imgPortrait);
        toHttpGetDetauks(albumDetail.getAuthorid());
        if (albumDetail.getIsfocus() == 1) {
            isFo = true;
            btnFollow.setImageResource(R.drawable.onfollow);
        } else {
            btnFollow.setImageResource(R.drawable.follow);
            isFo = false;
        }
    }

    /**
     * 获取作者专辑
     *
     * @param authorid
     */
    private void toHttpGetDetauks(String authorid) {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("authorid", authorid);
        map.put("entity", entity);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.QUERY_ALL_ALBUM_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                List<AlbumDetailAuthorAlbum> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), AlbumDetailAuthorAlbum.class);
                listItem.removeAll(listItem);
                listItem.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String state;
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_FOCUS_CHANGED);
        switch (v.getId()) {
            case R.id.btn_album_details:
                if (!Util.hintLogin((BaseActivity) getActivity()))
                    return;
                if (isFo) {
                    showCustomPop();
                } else {
                    isFo = true;
                    state = "1";
                    msg.setBody(state);
                    EventBus.getDefault().post(msg);
                    btnFollow.setImageResource(R.drawable.onfollow);
                }
                break;
            case R.id.tv_no_finish:
                finishPop(mPopupWindow);
                btnFollow.setImageResource(R.drawable.onfollow);
                isFo = true;
                state = "1";
                msg.setBody(state);
                EventBus.getDefault().post(msg);
                break;
            case R.id.tv_yes_onclick:
                finishPop(mPopupWindow);
                btnFollow.setImageResource(R.drawable.follow);
                isFo = false;
                state = "0";
                msg.setBody(state);
                EventBus.getDefault().post(msg);
                break;
        }
    }


    private void showCustomPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(getActivity(), R.layout.popupwindow_detauks, null);
            tvFinish = (TextView) view.findViewById(R.id.tv_no_finish);
            tvYesClick = (TextView) view.findViewById(R.id.tv_yes_onclick);
            tvFinish.setOnClickListener(this);
            tvYesClick.setOnClickListener(this);
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.8f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    finishPop(mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(llMdetails, Gravity.CENTER, 0, 0);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f;
            getActivity().getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(llMdetails, Gravity.CENTER, 0, 0);
        }
        btnFollow.setImageResource(R.drawable.onfollow);
    }

    /**
     * 隐藏PopupWindow
     *
     * @param popupWindow 要隐藏的PopupWindow
     */
    private void finishPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (albumDetail != null) {
            boolean b = albumDetail.getIsfocus() == 1 ? true : false;
            if (b != isFo) {
                toHttpFocus();
            }
        }
    }

    private void toHttpFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", albumDetail.getAuthorid());
        if (isFo)
            map.put("oparetion", 1);
        else
            map.put("oparetion", 0);
        HttpUtil.post(getContext(), SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    if (isFo)
                        albumDetail.setIsfocus(1);
                    else
                        albumDetail.setIsfocus(0);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.DETAUKS_FRAGMENT_RECEIVER_FOCUS:
                if (msg.getBody().equals("1")) {
                    isFo = true;
                    btnFollow.setImageResource(R.drawable.onfollow);
                } else {
                    btnFollow.setImageResource(R.drawable.follow);
                    isFo = false;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
