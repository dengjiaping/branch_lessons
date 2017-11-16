package com.yidiankeyan.science.information.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.adapter.InterviewCommentListAdapter;
import com.yidiankeyan.science.information.entity.InterviewCommentListBean;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 专访评论
 * -子评论列表
 */
public class InterviewCommentListActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private PtrClassicFrameLayout ptrLayout;
    private ListViewFinalLoadMore lvComment;
    private int pages = 1;
    private List<InterviewCommentListBean> mDatas = new ArrayList<>();
    private InterviewCommentListAdapter listAdapter;
    private List<String> commentClickList;
    private View headView;
    private ImageView imgAvatar;
    private TextView txtContent;
    private TextView tvDate;
    private TextView tvCommentCount;
    private TextView txtName;
    private TextView tvClickCount;
    private AutoLinearLayout llClick;
    private AutoLinearLayout llComment;
    private List<String> interviewClickList;
    private Button btnSubmitComment;
    private EditText etPopupwindowComm;
    private PopupWindow commPopupWindow;
    private View commView;
    private AutoLinearLayout llInterviewCommentList;


    private String userName;
    private String userImg;
    private int clickNum;
    private int commentNum;
    private String userId;
    private String content;
    private long data;

    private int position;
    private EditText etComment;
    private TextView btnComment;
    private Intent intent;

    private int isSend = 0;


    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_interview_comment_list;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        ptrLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_recommend_layout);
        lvComment = (ListViewFinalLoadMore) findViewById(R.id.lv_comment);
//        etComment = (EditText) findViewById(R.id.et_comment);
        btnComment = (TextView) findViewById(R.id.btn_comment);
        headView = LayoutInflater.from(this).inflate(R.layout.item_head_comt_list, null, false);
        imgAvatar = (ImageView) headView.findViewById(R.id.img_avatar);
        txtName = (TextView) headView.findViewById(R.id.tv_name);
        txtContent = (TextView) headView.findViewById(R.id.tv_father);
        tvDate = (TextView) headView.findViewById(R.id.tv_date);
        tvCommentCount = (TextView) headView.findViewById(R.id.tv_comment_count);
        tvClickCount = (TextView) headView.findViewById(R.id.tv_click_count);
        llClick = (AutoLinearLayout) headView.findViewById(R.id.ll_click);
        llComment = (AutoLinearLayout) headView.findViewById(R.id.ll_comment);
        llInterviewCommentList = (AutoLinearLayout) findViewById(R.id.ll_interview_comment_list);
    }

    @Override
    protected void initAction() {
        txtTitle.setText("评论详情");
        llReturn.setOnClickListener(this);
        commentClickList = new ArrayList<String>();
        interviewClickList = new ArrayList<>();
        userName = getIntent().getStringExtra("userName");
        userImg = getIntent().getStringExtra("userImg");
        userId = getIntent().getStringExtra("id");
        commentNum = getIntent().getIntExtra("comNum", 0);
        clickNum = getIntent().getIntExtra("clickNum", 0);
        content = getIntent().getStringExtra("content");
        position = getIntent().getIntExtra("position", -1);
        data = getIntent().getLongExtra("data", 0);
        if (position > -1)
            intent = new Intent();
        Glide.with(this).load(Util.getImgUrl(userImg)).error(R.drawable.icon_default_avatar).into(imgAvatar);
        txtName.setText(userName);
        tvDate.setText(TimeUtils.formatDate2(data));
        txtContent.setText(content);
        tvCommentCount.setText(commentNum + "");
        tvClickCount.setText(clickNum + "");
        //填充数据
        ptrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pages = 1;
                initDatas();
            }
        });
        if (mDatas.size() == 0) {
            ptrLayout.autoRefresh();
        } else {
            lvComment.setHasLoadMore(true);
        }

        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        listAdapter = new InterviewCommentListAdapter(this, mDatas);
        lvComment.addHeaderView(headView);
        lvComment.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        setCommDeleteListener();
//        etComment.setHint("回复@" + getIntent().getStringExtra("userName"));
//        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    if (TextUtils.isEmpty(etComment.getText())) {
//                        Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (Util.hintLogin(InterviewCommentListActivity.this)) {
//                            isSend=1;
//                            toHttpSendComment();
////                                hideSoftKeyboard();
//                            Util.closeKeybord(etComment, InterviewCommentListActivity.this);
//                        }
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
        lvComment.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (ptrLayout.isRefreshing())
                    return;
                initDatas();
            }
        });
        llClick.setOnClickListener(this);
        llComment.setOnClickListener(this);
        btnComment.setOnClickListener(this);
    }

    private void setCommDeleteListener() {
        listAdapter.setOnDeleteClickListener(new InterviewCommentListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mDatas.get(position).getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    mDatas.remove(position);
                                    listAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_CHANGE:
                //评论的赞发生变化
                String commentId = (String) msg.getBody();
                if (msg.getArg1() == 0) {
                    //取消点赞
                    //该评论存在于点赞列表
                    if (mDatas.contains(commentId)) {
                        //删除数据库的音频
                        DB.getInstance(DemoApplication.applicationContext).deleteClick(commentId);
                        //将该音频从点赞列表移除
                        mDatas.remove(commentId);
                    } else {
                        //将数据库的音频点赞状态设置成未点赞
                        DB.getInstance(DemoApplication.applicationContext).updataClick(0, commentId);
                    }
                } else {
                    //点赞
                    if (DB.getInstance(DemoApplication.applicationContext).existedClickTable(commentId)) {
                        //将评论状态设置成已点赞
                        DB.getInstance(DemoApplication.applicationContext).updataClick(1, commentId);
                    } else {
                        //该评论未被点赞，将其加到需要点赞的列表
                        commentClickList.add(commentId);
                        //向数据库插入该评论已点赞
                        DB.getInstance(DemoApplication.applicationContext).insertClick(commentId);
                    }
                    setResult(RESULT_OK);
                }
                break;
        }
    }


    /**
     * 获取评论列表
     */
    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getInterviewCommentList(map).enqueue(new RetrofitCallBack<ArrayList<InterviewCommentListBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<InterviewCommentListBean>>> call, Response<RetrofitResult<ArrayList<InterviewCommentListBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (pages == 1)
                        mDatas.removeAll(mDatas);
                    if (response.body().getData().size() > 0) {
                        lvComment.setHasLoadMore(true);
                        mDatas.addAll(response.body().getData());
                        pages++;
                    } else {
                        lvComment.setHasLoadMore(false);
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
                    lvComment.showFailUI();
                }
                ptrLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<InterviewCommentListBean>>> call, Throwable t) {
                ptrLayout.onRefreshComplete();
                lvComment.showFailUI();
            }
        });
    }


    /**
     * 提交评论
     */
//    private void toHttpSendComment() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("targetid", getIntent().getStringExtra("id"));
//        map.put("content", etComment.getText().toString());
//        map.put("type", 2);
//        map.put("isreply", 1);
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
//                    pages = 1;
//                    etComment.setText("");
//                    intent.putExtra("position", position);
//                    intent.putExtra("isComment", true);
//                    setResult(RESULT_OK, intent);
//                    initDatas();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_click:
                if (!Util.hintLogin(this)) return;
                String id = getIntent().getStringExtra("id");
                if (DB.getInstance(DemoApplication.applicationContext).isClick(id)) {
                    Toast.makeText(mContext, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    tvClickCount.setText(getIntent().getIntExtra("clickNum", 0) + 1 + "");
                    if (DB.getInstance(DemoApplication.applicationContext).existedClickTable(id)) {
                        //将评论状态设置成已点赞
                        DB.getInstance(DemoApplication.applicationContext).updataClick(1, id);
                    } else {
                        //该评论未被点赞，将其加到需要点赞的列表
                        interviewClickList.add(id + "");
                        //向数据库插入该评论已点赞
                        DB.getInstance(DemoApplication.applicationContext).insertClick(id);
                    }
                }
                break;
            case R.id.btn_comment:
                showCommPop();
                break;
            case R.id.ll_comment:
                showCommPop();
                break;
        }
    }

    private void showCommPop() {
        if (commPopupWindow == null) {
            commView = View.inflate(this, R.layout.popupwindow_comm, null);
            etPopupwindowComm = (EditText) commView.findViewById(R.id.et_popupwindow_comm);
            btnSubmitComment = (Button) commView.findViewById(R.id.btn_submit_comment);
            etPopupwindowComm.setHint("回复@" + getIntent().getStringExtra("userName"));
            commPopupWindow = new PopupWindow(commView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT) {
                @Override
                public void dismiss() {
                    if (etPopupwindowComm != null)
                        inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    super.dismiss();
                }
            };
            commPopupWindow.setContentView(commView);
            commPopupWindow.setAnimationStyle(R.style.AnimBottom);
            commPopupWindow.setFocusable(true);
            commPopupWindow.setTouchable(true);
            commPopupWindow.setOutsideTouchable(false);
            commPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            btnSubmitComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Util.hintLogin(InterviewCommentListActivity.this)) return;
                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    Util.finishPop(InterviewCommentListActivity.this, commPopupWindow);
                    isSend = 2;
                    toHttpSendComment();
                    etPopupwindowComm.setText("");
                }
            });
//            commPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
//                    return false;
//                }
//            });
            commPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(InterviewCommentListActivity.this, commPopupWindow);
                }
            });
            etPopupwindowComm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        btnSubmitComment.setEnabled(true);
                        btnSubmitComment.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        btnSubmitComment.setEnabled(false);
                        btnSubmitComment.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            });
        }
        commPopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Util.openKeybord(etPopupwindowComm, this);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        commPopupWindow.showAtLocation(llInterviewCommentList, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("content", etPopupwindowComm.getText().toString());
        map.put("type", 2);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    ptrLayout.autoRefresh();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (interviewClickList != null) {
            for (String id : interviewClickList) {
                Map<String, Object> map = new HashMap<>();
                map.put("uptype", "COMMENT");
                map.put("targetid", id);
                HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {

                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
            }
        }
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_INTERVIEW_COMMENT_LISTS);
        EventBus.getDefault().post(msg);
    }
}
