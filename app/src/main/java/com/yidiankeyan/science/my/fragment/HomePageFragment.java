package com.yidiankeyan.science.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.my.entity.MyHomeStatisticsBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 我的
 * -我的主页
 * -简介
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    private TextView tvTextImageNumber;
    private TextView tvAudioFrequencyNumber;
    private TextView tvVideoNumber;
    private TextView tvAlbumNumber;
    private TextView tvHomeIntroduction;
    private MyHomeStatisticsBean statisticsBean;
    private UserInforMation user;
    private View llContainer;
    private TextView tvOperation;
    private TextView tvConversation;
    private Intent intent;
    private int isSelect;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        if (TextUtils.equals(getArguments().getString("id"), SpUtils.getStringSp(getActivity(), "userId"))) {
            llContainer.setVisibility(View.GONE);
            isSelect = 1;
        } else {
            toHttpGetUserInfo();
            llContainer.setVisibility(View.VISIBLE);
            isSelect = 2;
        }
        toHttpGetUserStatistics();
    }

    private void toHttpGetUserStatistics() {
        Map<String, Object> map = new HashMap<>();
        if (isSelect > 1) {
            map.put("userId", getArguments().getString("id"));
        } else {
            map.put("", "");
        }
        ApiServerManager.getInstance().getApiServer().getMyStatistics(map).enqueue(new RetrofitCallBack<MyHomeStatisticsBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<MyHomeStatisticsBean>> call, Response<RetrofitResult<MyHomeStatisticsBean>> response) {
                if (response.body().getCode() == 200) {
                    statisticsBean = response.body().getData();
                    if (TextUtils.isEmpty(statisticsBean.getImageTextNum())) {
                        tvTextImageNumber.setText("0");
                    } else {
                        tvTextImageNumber.setText(statisticsBean.getImageTextNum());
                    }
                    if (TextUtils.isEmpty(statisticsBean.getAudioNum())) {
                        tvAudioFrequencyNumber.setText("0");
                    } else {
                        tvAudioFrequencyNumber.setText(statisticsBean.getAudioNum());
                    }
                    if (TextUtils.isEmpty(statisticsBean.getVideoNum())) {
                        tvVideoNumber.setText("0");
                    } else {
                        tvVideoNumber.setText(statisticsBean.getVideoNum());
                    }
                    if (TextUtils.isEmpty(statisticsBean.getAlbumNum())) {
                        tvAlbumNumber.setText("0");
                    } else {
                        tvAlbumNumber.setText(statisticsBean.getAlbumNum());
                    }
                    tvHomeIntroduction.setText(statisticsBean.getMysign());
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<MyHomeStatisticsBean>> call, Throwable t) {

            }
        });
    }

    private void initView(View view) {
        tvTextImageNumber = (TextView) view.findViewById(R.id.tv_text_image_number);
        tvAudioFrequencyNumber = (TextView) view.findViewById(R.id.tv_audio_frequency_number);
        tvVideoNumber = (TextView) view.findViewById(R.id.tv_video_number);
        tvAlbumNumber = (TextView) view.findViewById(R.id.tv_album_number);
        tvHomeIntroduction = (TextView) view.findViewById(R.id.tv_home_introduction);
        llContainer = view.findViewById(R.id.ll_container);
        tvOperation = (TextView) view.findViewById(R.id.tv_operation);
        tvConversation = (TextView) view.findViewById(R.id.tv_conversation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_conversation:
//                intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra(Constant.EXTRA_USER_ID, user.getSizeid());
//                startActivity(intent);
                break;
            case R.id.tv_operation:
                if ((int) tvOperation.getTag() == 0) {
                    if (getArguments().getString("id") == null)
                        return;
//                    Intent intent = new Intent(getContext(), SendContactApplyActivity.class);
//                    intent.putExtra("id", user.getSizeid());
//                    startActivity(intent);
                } else {
//                    showPop("确定删除该好友吗？", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.finishPop(getActivity(), mPopupWindow);
//                        }
//                    }, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.finishPop(getActivity(), mPopupWindow);
//                            toHttpDeleteContact();
//                        }
//                    });
//                    intent = new Intent(getActivity(), ChatActivity.class);
//                    intent.putExtra(Constant.EXTRA_USER_ID, user.getSizeid());
//                    startActivity(intent);
                }
                break;
        }
    }


    private void toHttpGetUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getArguments().getString("id"));
        HttpUtil.post(getActivity(), SystemConstant.URL + SystemConstant.QUERY_USER_INFO, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
                    if (!TextUtils.equals(getArguments().getString("id"), SpUtils.getStringSp(getActivity(), "userId"))) {
                        toHttpGetUserIMDetail();
                    }
                    tvConversation.setOnClickListener(HomePageFragment.this);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    private void toHttpGetUserIMDetail() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", user.getSizeid());
//        ApiServerManager.getInstance().getApiServer().revealUser(map).enqueue(new RetrofitCallBack<RevealUserBean>() {
//            @Override
//            public void onSuccess(Call<RetrofitResult<RevealUserBean>> call, Response<RetrofitResult<RevealUserBean>> response) {
//                if (response.body().getCode() == 200) {
//                    if (response.body().getData().getIsfriend() == 0) {
//                        tvOperation.setText("加好友");
//                        tvOperation.setTag(0);
//                        tvOperation.setVisibility(View.VISIBLE);
//                        tvConversation.setVisibility(View.VISIBLE);
//                    } else {
//                        tvOperation.setText("发消息");
//                        tvOperation.setTag(1);
//                        tvOperation.setVisibility(View.VISIBLE);
//                        tvConversation.setVisibility(View.GONE);
//                    }
//                    tvOperation.setOnClickListener(HomePageFragment.this);
//                } else if (response.body().getCode() == 306) {
//                    tvOperation.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastMaker.showShortToast("服务器异常");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitResult<RevealUserBean>> call, Throwable t) {
//                tvOperation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ToastMaker.showShortToast("网络异常");
//                    }
//                });
//            }
//        });
    }

}
