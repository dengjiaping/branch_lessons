package com.yidiankeyan.science.subscribe.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.subscribe.adapter.ContentCommentAdapter;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;

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

public class AllCommentActivity extends BaseActivity {

    private ListViewFinalLoadMore lvComment;
    private EditText etComment;
    private List<ContentCommentBean> commDatas = new ArrayList<>();
    private ContentCommentAdapter adapter;
    private int pages = 1;
    private List<String> commentClickList;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_all_comment;
    }

    @Override
    protected void initView() {
        lvComment = (ListViewFinalLoadMore) findViewById(R.id.lv_comment);
        etComment = (EditText) findViewById(R.id.et_comment);
    }

    @Override
    protected void initAction() {
        commentClickList = new ArrayList<String>();
        ((TextView) findViewById(R.id.maintitle_txt)).setText("评论详情");
        findViewById(R.id.ll_return).setOnClickListener(this);
        adapter = new ContentCommentAdapter(commDatas, this);
        lvComment.setAdapter(adapter);
        setCommDeleteListener();
        lvComment.setHasLoadMore(true);
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (TextUtils.isEmpty(etComment.getText())) {
                        Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Util.hintLogin(AllCommentActivity.this)) {
                            toHttpSendComment();
//                                hideSoftKeyboard();
                            Util.closeKeybord(etComment, AllCommentActivity.this);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        toHttpGetComment();
        lvComment.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toHttpGetComment();
            }
        });
    }

    /**
     * 设置删除回调及点击后续操作
     */
    private void setCommDeleteListener() {
        adapter.setOnDeleteClickListener(new ContentCommentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", commDatas.get(position).getFather().getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    commDatas.remove(position);
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
        adapter.setOnSonDeleteClickListener(new ContentCommentAdapter.OnSonDeleteClickListener() {
            @Override
            public void onDeleteClick(final int parentPosition, final int sonPosition) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", commDatas.get(parentPosition).getSons().get(sonPosition).getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    commDatas.get(parentPosition).getSons().remove(sonPosition);
                                    commDatas.get(parentPosition).getFather().setCommentnum(commDatas.get(parentPosition).getFather().getCommentnum() - 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (commDatas != null && commDatas.size() > 0)
            adapter.notifyDataSetChanged();
    }

    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("content", etComment.getText().toString());
        map.put("type", 1);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    pages = 1;
                    toHttpGetComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpGetComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        map.put("entity", entity);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_MIX_COMMENT_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonData = GsonUtils.obj2Json(result.getData());
                    List<ContentCommentBean> data = GsonUtils.json2List(jsonData, ContentCommentBean.class);
                    if (pages == 1)
                        commDatas.removeAll(commDatas);
                    if (data.size() > 0) {
                        lvComment.setHasLoadMore(true);
                        commDatas.addAll(data);
                        pages++;
                    } else {
                        lvComment.setHasLoadMore(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lvComment.showFailUI();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                lvComment.setHasLoadMore(false);
            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_CLICK_CHANGE:
                String commentId = (String) msg.getBody();
                commentClickList.add(commentId);
                //向数据库插入该评论已点赞
                DB.getInstance(DemoApplication.applicationContext).insertClick(commentId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImgTxtAlbumActivity.INTO_COMMENT_DETAIL:
                    int position = data.getIntExtra("position", -1);
                    if (position > -1) {
                        if (data.getBooleanExtra("isClick", false)) {
                            //在评论详情页面进行了点赞操作
                            commDatas.get(position).getFather().setUps(commDatas.get(position).getFather().getUps() + 1);
                        }
                        if (data.getBooleanExtra("isComment", false)) {
                            //在评论详情页面进行了评论操作
                            commDatas.get(position).getFather().setCommentnum(commDatas.get(position).getFather().getCommentnum() + 1);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        for (String id : commentClickList) {
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
}
