package com.yidiankeyan.science.subscribe.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.subscribe.adapter.AlbumCommentListAdapter;
import com.yidiankeyan.science.subscribe.entity.CommentBean;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.utils.GsonUtils;
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
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 评论之评论
 */
public class CommentDetailsActivity extends BaseActivity {

    private TextView txtTitle;
    private ImageButton btnTitle;
    private AutoLinearLayout llReturn;
    private ListViewFinalLoadMore lvNews;
    private List<CommentBean> mDates = new ArrayList<>();
    private View headView;
    private AlbumCommentListAdapter adapter;
    private ContentCommentBean comment;
    private ImageView imgParentAvatar;
    private TextView tvParentName;
    private TextView tvParentDate;
    private AutoLinearLayout llParentComment;
    private ImageView imgParentComment;
    private TextView txtParentCommentCount;
    private TextView tvParentCommentContent;
    private EditText etComment;
    private InputMethodManager inputMethodManager;
    private int isReply = 0;
    private String replyId;
    private int pages = 1;
    private TextView tvCommentCount;
    private int position;
    private Intent intent;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_comment_details;
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        lvNews = (ListViewFinalLoadMore) findViewById(R.id.lv_comment);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        etComment = (EditText) findViewById(R.id.et_comment);
        txtTitle.setText("评论详情");
        headView = LayoutInflater.from(this).inflate(R.layout.head_comment_detail, null, false);
        imgParentAvatar = (ImageView) headView.findViewById(R.id.img_parent_avatar);
        tvParentName = (TextView) headView.findViewById(R.id.tv_parent_name);
        tvParentDate = (TextView) headView.findViewById(R.id.tv_parent_date);
        llParentComment = (AutoLinearLayout) headView.findViewById(R.id.ll_parent_comment);
        imgParentComment = (ImageView) headView.findViewById(R.id.img_parent_comment);
        tvCommentCount = (TextView) headView.findViewById(R.id.tv_comment_count);
        txtParentCommentCount = (TextView) headView.findViewById(R.id.txt_parent_comment_count);
        tvParentCommentContent = (TextView) headView.findViewById(R.id.tv_parent_comment_content);
    }

    @Override
    protected void initAction() {
        comment = getIntent().getParcelableExtra("bean");
        position = getIntent().getIntExtra("position", -1);
        if (position > -1)
            intent = new Intent();
        if (comment != null) {
            Glide.with(this).load(Util.getImgUrl(comment.getFather().getImgurl())).error(R.drawable.icon_default_avatar).into(imgParentAvatar);
            tvCommentCount.setText(comment.getFather().getCommentnum() + "");
            tvParentName.setText(comment.getFather().getUsername());
            tvParentDate.setText(TimeUtils.formatDate2(comment.getFather().getCreatetime()));
            txtParentCommentCount.setText(comment.getFather().getUps() + "");
            tvParentCommentContent.setText(comment.getFather().getContent());
            toHttpGetComment();
            etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        if (TextUtils.isEmpty(etComment.getText())) {
                            Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Util.hintLogin(CommentDetailsActivity.this)) {
                                toHttpSendComment();
//                                hideSoftKeyboard();
                                Util.closeKeybord(etComment, CommentDetailsActivity.this);
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            if (DB.getInstance(DemoApplication.applicationContext).isClick(comment.getFather().getCommentid())) {
                imgParentComment.setImageResource(R.drawable.icon_today_click_pressed);
            } else {
                imgParentComment.setImageResource(R.drawable.icon_today_click_normal);
            }
        }
        btnTitle.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        llParentComment.setOnClickListener(this);
        adapter = new AlbumCommentListAdapter(this, mDates);
        lvNews.addHeaderView(headView);
        lvNews.setAdapter(adapter);
        lvNews.setHasLoadMore(true);
        setCommDeleteListener();
        lvNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetComment();
            }
        });
        adapter.notifyDataSetChanged();
//        adapter.setMyOnItemClickListener(new AlbumCommentListAdapter.MyOnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                isReply = 1;
//                replyId = mDates.get(position).getUserid();
//                if (!TextUtils.isEmpty(mDates.get(position).getUsername()) && !TextUtils.equals("null", mDates.get(position).getUsername())) {
//                    etComment.setHint("回复" + mDates.get(position).getUsername());
//                } else {
//                    etComment.setHint("回复Ta");
//                }
//                etComment.requestFocus();
//                Util.openKeybord(etComment, mContext);
//            }
//        });
//        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0 || mDates.size() == 0 || position >= mDates.size())
//                    return;
//                position--;
//                isReply = 1;
//                replyId = mDates.get(position).getUserid();
//                etComment.requestFocus();
//                Util.openKeybord(etComment, mContext);
//            }
//        });
    }

    private void setCommDeleteListener() {
        adapter.setOnDeleteClickListener(new AlbumCommentListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mDates.get(position).getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    mDates.remove(position);
                                    adapter.notifyDataSetChanged();
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

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_CHANGE:
                String commentId = (String) msg.getBody();
                //向数据库插入该评论已点赞
                DB.getInstance(DemoApplication.applicationContext).insertClick(commentId);
                Map<String, Object> map = new HashMap<>();
                map.put("uptype", "COMMENT");
                map.put("targetid", commentId);
                HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                    @Override
                    public void successResult(ResultEntity result) throws JSONException {

                    }

                    @Override
                    public void errorResult(Throwable ex, boolean isOnCallback) {

                    }
                });
                break;
        }
    }

    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", comment.getFather().getCommentid());
        map.put("content", etComment.getText().toString());
        map.put("type", 2);
        if (isReply == 1) {
            map.put("isreply", 1);
            map.put("replyid", replyId);
        }
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    pages = 1;
                    isReply = 0;
                    etComment.setText("");
                    intent.putExtra("position", position);
                    intent.putExtra("isComment", true);
                    setResult(RESULT_OK, intent);
                    toHttpGetComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 获取评论列表
     */
    private void toHttpGetComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 10);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", comment.getFather().getCommentid());
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_COMMENT_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<CommentBean> mData = GsonUtils.json2List(jsonData, CommentBean.class);
                    if (pages == 1)
                        mDates.removeAll(mDates);
                    if (mData.size() > 0) {
                        lvNews.setHasLoadMore(true);
                        mDates.addAll(mData);
                        pages++;
                    } else {
                        lvNews.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvNews.showFailUI();
                }
//                if (result.getCode() == 200) {
//                    List<CommentBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), CommentBean.class);
//                    mDates.removeAll(mDates);
//                    mDates.addAll(data);
//                    adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvNews.showFailUI();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.ll_parent_comment:
                if (DB.getInstance(DemoApplication.applicationContext).isClick(comment.getFather().getCommentid())) {
                    Toast.makeText(mContext, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    imgParentComment.setImageResource(R.drawable.icon_today_click_pressed);
                    txtParentCommentCount.setText(comment.getFather().getUps() + 1 + "");
                    DB.getInstance(DemoApplication.applicationContext).insertClick(comment.getFather().getCommentid());
                    if (intent != null) {
                        intent.putExtra("position", position);
                        intent.putExtra("isClick", true);
                        setResult(RESULT_OK, intent);
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("uptype", "COMMENT");
                    map.put("targetid", comment.getFather().getCommentid());
                    HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {

                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {

                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
