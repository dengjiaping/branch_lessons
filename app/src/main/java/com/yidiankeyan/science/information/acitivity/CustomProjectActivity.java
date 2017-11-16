package com.yidiankeyan.science.information.acitivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.CustomProjectAdapter;
import com.yidiankeyan.science.information.entity.ProjectBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomProjectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView lvProject;
    private List<ProjectBean> mData = new ArrayList<>();
    private CustomProjectAdapter adapter;
    private List<String> orderId = new ArrayList<>();
    private List<String> beginOrderId = new ArrayList<>();
    private TextView tvComplete;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete:
                if (orderId == null)
                    return;
                if (!orderId.equals(beginOrderId)) {
                    //初始的已订专题与当前已订专题不一致
                    if (orderId.size() < 3) {
                        Toast.makeText(DemoApplication.applicationContext, "至少选择三个专题", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    toHttpOrderProject();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "您定制的专题没有发生改变", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_return:
                finish();
                break;
        }
    }

    /**
     * 定制专题
     */
    private void toHttpOrderProject() {
        Map<String, Object> map = new HashMap<>();
        StringBuffer subjectIds = new StringBuffer();
        for (int i = 0; i < orderId.size(); i++) {
            if (i != 0) {
                subjectIds.append("," + orderId.get(i));
            } else {
                subjectIds.append(orderId.get(i));
            }
        }
        map.put("subjectIds", subjectIds);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.SUBMIT_USER_SUBJECT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "定制成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.CUSTOM_PROJECT));
                    finish();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_custom_project_modify;
    }

    @Override
    protected void initView() {
        lvProject = (ListView) findViewById(R.id.lv_project);
        tvComplete = (TextView) findViewById(R.id.tv_complete);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        adapter = new CustomProjectAdapter(mData, this);
        lvProject.setAdapter(adapter);
        toHttpGetProject();
        adapter.setOnOrderChangedListener(new CustomProjectAdapter.OnOrderChangedListener() {
            @Override
            public void onChanged(int id, boolean selected) {
                if (orderId == null)
                    return;
                if (selected) {
                    orderId.add(String.valueOf(id));
                } else {
                    orderId.remove(String.valueOf(id));
                }
                Log.e("orderId", orderId.toString());
                Log.e("beginOrderId", beginOrderId.toString());
            }
        });
    }

    /**
     * 获取专题
     */
    private void toHttpGetProject() {
        Map<String, Object> map = new HashMap<>();
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_PROJECT, map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(final ResultEntity result) {
                if (result.getCode() == 200) {
//                    final String jsonData = GsonUtils.obj2Json(result.getData());
                    List<ProjectBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ProjectBean.class);
                    mData.addAll(data);
//                    adapter.notifyDataSetChanged();
                    toHttpGetSubProject();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void toHttpGetSubProject() {
        Map<String, Object> map = new HashMap<>();
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_ORDER_PROJECT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    String jsonString = GsonUtils.obj2Json(result.getData());
                    String json = GsonUtils.getJsonElement(jsonString, "resultList");
                    List<String> data = GsonUtils.json2List(json, String.class);
                    if (data.size() > 0 && !TextUtils.isEmpty(data.get(0))) {
                        orderId.addAll(data);
                        beginOrderId.addAll(orderId);
                        for (String id : orderId) {
                            for (ProjectBean project : mData) {
                                if (TextUtils.equals(String.valueOf(project.getSubjectid()).trim(), id.trim())) {
                                    project.setSelected(true);
                                }
                            }
                        }
                        Log.e("project", mData.toString());
                    }
                    adapter.notifyDataSetChanged();
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * 用户栏目的GRIDVIEW
//     */
//    private MyDragGrid userGridView;
//    /**
//     * 其它栏目的GRIDVIEW
//     */
//    private OtherGridView otherGridView;
//    /**
//     * 用户栏目对应的适配器，可以拖动
//     */
//    MyDragAdapter userAdapter;
//    /**
//     * 其它栏目对应的适配器
//     */
//    MyOtherAdapter otherAdapter;
//    /**
//     * 其它栏目列表
//     */
//    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
//    /**
//     * 用户栏目列表
//     */
//    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
//    /**
//     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
//     */
//    private List<ChannelItem> projectList;
//    boolean isMove = false;
//    private TextView maintitleTxt;
//    private ImageButton btnTitle;
//    private AutoLinearLayout llReturn;
//    private ImageView titleReturn;
//    /**
//     * 进入页面时的已订专题
//     */
//    private List<ChannelItem> beginningSub = new ArrayList<>();
//
//    @Override
//    protected int setContentView() {
//        return R.layout.activity_custom_project;
//    }
//
//    @Override
//    protected void initView() {
//        btnTitle = (ImageButton) findViewById(R.id.title_btn);
//        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
//        titleReturn = (ImageView) findViewById(R.id.title_return);
//        llReturn.setOnClickListener(this);
//        userGridView = (MyDragGrid) findViewById(R.id.userGridView);
//        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
//        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
//        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
//    }
//
//    @Override
//    protected void initAction() {
//        btnTitle.setVisibility(View.GONE);
//        projectList = new DB(mContext).queryProject();
//        if (projectList.size() == 0) {
//            toHttpGetProject();
//        }else{
//            for (ChannelItem project : projectList) {
//                if (project.getSelected() == 1) {
//                    userChannelList.add(project);
//                    beginningSub.add(project);
//                } else {
//                    otherChannelList.add(project);
//                }
//            }
//        }
//        userAdapter = new MyDragAdapter(this, userChannelList);
//        userGridView.setAdapter(userAdapter);
//        otherAdapter = new MyOtherAdapter(this, otherChannelList);
//        otherGridView.setAdapter(this.otherAdapter);
//        //设置GRIDVIEW的ITEM的点击监听
//        otherGridView.setOnItemClickListener(this);
//        userGridView.setOnItemClickListener(this);
//        llReturn.setOnClickListener(this);
//        maintitleTxt.setText("专题定制");
//    }
//
//    /**
//     * 获取专题
//     */
//    private void toHttpGetProject() {
//        Map<String, Object> map = new HashMap<>();
//        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.QUERY_PROJECT, map, new HttpUtil.HttpCallBack() {
//
//            @Override
//            public void successResult(final ResultEntity result) {
//                if (result.getCode() == 200) {
//                    final String jsonData = GsonUtils.obj2Json(result.getData());
//                    DB.getInstance(DemoApplication.applicationContext).deleteAllProject();
//                    final List<ChannelItem> mDatas = GsonUtils.json2List(jsonData, ChannelItem.class);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            DB.getInstance(DemoApplication.applicationContext).insertProject(mDatas);
//                        }
//                    }).start();
//                    projectList.addAll(mDatas);
//                    for (ChannelItem project : projectList) {
//                        if (project.getSelected() == 1) {
//                            userChannelList.add(project);
//                            beginningSub.add(project);
//                        } else {
//                            otherChannelList.add(project);
//                        }
//                    }
//                    userAdapter.notifyDataSetChanged();
//                    otherAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_return:
//                finish();
//                break;
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//        if (isMove) {
//            return;
//        }
//        switch (parent.getId()) {
//
//            case R.id.ll_return:
//                finish();
//                break;
//
//            case R.id.userGridView:
//                //position为 0，1 的不可以进行任何操作
//                if (true) {
//                    final ImageView moveImageView = getView(view);
//                    if (moveImageView != null) {
//                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
//                        final int[] startLocation = new int[2];
//                        newTextView.getLocationInWindow(startLocation);
//                        final ChannelItem channel = ((MyDragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
//                        otherAdapter.setVisible(false);
//                        //添加到最后一个
//                        otherAdapter.addItem(channel);
//                        new DB(mContext).updataProject(channel, 0);
//                        new Handler().postDelayed(new Runnable() {
//                            public void run() {
//                                try {
//                                    int[] endLocation = new int[2];
//                                    //获取终点的坐标
//                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
//                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
//                                    userAdapter.setRemove(position);
//                                } catch (Exception localException) {
//                                }
//                            }
//                        }, 50L);
//                    }
//                }
//                break;
//            case R.id.otherGridView:
//                final ImageView moveImageView = getView(view);
//                if (moveImageView != null) {
//                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
//                    final int[] startLocation = new int[2];
//                    newTextView.getLocationInWindow(startLocation);
//                    final ChannelItem channel = ((MyOtherAdapter) parent.getAdapter()).getItem(position);
//                    userAdapter.setVisible(false);
//                    //添加到最后一个
//                    userAdapter.addItem(channel);
//                    new DB(mContext).updataProject(channel, 1);
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            try {
//                                int[] endLocation = new int[2];
//                                //获取终点的坐标
//                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
//                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
//                                otherAdapter.setRemove(position);
//                            } catch (Exception localException) {
//                            }
//                        }
//                    }, 50L);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 点击ITEM移动动画
//     *
//     * @param moveView
//     * @param startLocation
//     * @param endLocation
//     * @param moveChannel
//     * @param clickGridView
//     */
//    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
//                          final GridView clickGridView) {
//        int[] initLocation = new int[2];
//        //获取传递过来的VIEW的坐标
//        moveView.getLocationInWindow(initLocation);
//        //得到要移动的VIEW,并放入对应的容器中
//        final ViewGroup moveViewGroup = getMoveViewGroup();
//        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
//        //创建移动动画
//        TranslateAnimation moveAnimation = new TranslateAnimation(
//                startLocation[0], endLocation[0], startLocation[1],
//                endLocation[1]);
//        moveAnimation.setDuration(300L);//动画时间
//        //动画配置
//        AnimationSet moveAnimationSet = new AnimationSet(true);
//        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
//        moveAnimationSet.addAnimation(moveAnimation);
//        mMoveView.startAnimation(moveAnimationSet);
//        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//                isMove = true;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                moveViewGroup.removeView(mMoveView);
//                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
//                if (clickGridView instanceof MyDragGrid) {
//                    otherAdapter.setVisible(true);
//                    otherAdapter.notifyDataSetChanged();
//                    userAdapter.remove();
//                } else {
//                    userAdapter.setVisible(true);
//                    userAdapter.notifyDataSetChanged();
//                    otherAdapter.remove();
//                }
//                isMove = false;
//            }
//        });
//    }
//
//    /**
//     * 获取移动的VIEW，放入对应ViewGroup布局容器
//     *
//     * @param viewGroup
//     * @param view
//     * @param initLocation
//     * @return
//     */
//    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
//        int x = initLocation[0];
//        int y = initLocation[1];
//        viewGroup.addView(view);
//        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mLayoutParams.leftMargin = x;
//        mLayoutParams.topMargin = y;
//        view.setLayoutParams(mLayoutParams);
//        return view;
//    }
//
//    /**
//     * 创建移动的ITEM对应的ViewGroup布局容器
//     */
//    private ViewGroup getMoveViewGroup() {
//        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
//        LinearLayout moveLinearLayout = new LinearLayout(this);
//        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        moveViewGroup.addView(moveLinearLayout);
//        return moveLinearLayout;
//    }
//
//    /**
//     * 获取点击的Item的对应View，
//     *
//     * @param view
//     * @return
//     */
//    private ImageView getView(View view) {
//        view.destroyDrawingCache();
//        view.setDrawingCacheEnabled(true);
//        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//        ImageView iv = new ImageView(this);
//        iv.setImageBitmap(cache);
//        return iv;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (!beginningSub.equals(new DB(this).queryOrderSub())) {
//            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.CUSTOM_PROJECT));
//        }
//    }
}
