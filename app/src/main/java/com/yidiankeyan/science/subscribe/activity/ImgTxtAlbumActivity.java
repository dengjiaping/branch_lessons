package com.yidiankeyan.science.subscribe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.alipay.sdk.app.PayTask;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.EmptyActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.knowledge.activity.TagDetailActivity;
import com.yidiankeyan.science.subscribe.adapter.ContentCommentAdapter;
import com.yidiankeyan.science.subscribe.adapter.RelevantReadAdapter;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.subscribe.entity.ContentDetailBean;
import com.yidiankeyan.science.subscribe.entity.InterviewCommentBean;
import com.yidiankeyan.science.subscribe.entity.RelatedArticleBean;
import com.yidiankeyan.science.subscribe.entity.TxtClickBean;
import com.yidiankeyan.science.subscribe.entity.TxtRelevantTagBean;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.view.ObservableScrollView;
import com.yidiankeyan.science.view.ShowAllListView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.loadingviewfinal.ListViewFinalLoadMore;
import me.next.tagview.TagCloudView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 图文专辑详情
 */
public class ImgTxtAlbumActivity extends BaseActivity implements TagCloudView.OnTagClickListener, AdapterView.OnItemClickListener {

    private AutoLinearLayout llReturn;
    private WebView webImgTxt;
    private AutoRelativeLayout rlShare;
    private PopupWindow sharePopupWindow;
    private AutoLinearLayout alWebTv;
    private ShowAllListView lvComment;
    private List<ContentCommentBean> commDatas = new ArrayList<>();
    //最新
    private ArrayList<ContentCommentBean> mNewestDates = new ArrayList<>();
    private ContentCommentAdapter adapter;
    private ObservableScrollView svContainer;
    private AutoLinearLayout llContainer;
    //    private AutoRelativeLayout rlComment;
    private int currentScrollHeight;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private View xCustomView;
    //    private ProgressDialog waitdialog = null;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private myWebChromeClient xwebchromeclient;
    private AutoLinearLayout layoutBottom;
    private int topLayoutHeight;
    private int bottomLayoutHeight;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            rlComment.setEnabled(true);
            if (mTimer != null) {
                mTimer.cancel();
                mTimerTask.cancel();
                mTimer = null;
                mTimerTask = null;
            }
        }
    };
    private TextView btnComment;
    private InputMethodManager inputMethodManager;
    private EditText etPopupwindowComm;
    private Button btnSubmitComment;
    private PopupWindow commPopupWindow;
    private View commView;
    private CheckBox imgClick;
    private TextView imgReport;

    /**
     * 评论的id，退出时开始点赞请求
     */
    private List<String> commentClickList = new ArrayList<>();

    private AutoLinearLayout llSearchWx;
    private AutoLinearLayout llSearchWxCircle;
    private AutoLinearLayout llSearchQQ;
    private AutoLinearLayout llShareUrl;
    private AutoLinearLayout llShareCollect;
    private AutoLinearLayout llSearchSina;
    private TextView txtCloseShare;
    private ZoomButtonsController zoom_controll;
    private AutoRelativeLayout rlBestow;
    private PopupWindow mPopupWindow;
    private EditText etCustomPrice;
    private String price;
    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;
    private String id;

    private AlbumContent albumContent;

    private PopupWindow reportPopupwindow;
    private EditText etTitle;
    private EditText etContent;
    private String isFocus;
    private Intent resultIntent;
    private ImageView imgCollect;
    /**
     * 标题是否展示作者名以及关注的标识
     */
    private boolean titleBarShow = false;
    /**
     * 关注
     */
    private AutoLinearLayout llPlay;
    private TextView imgPlay;
    //分享
    private AutoRelativeLayout llTxtFollow;
    private ImageView imgTxtFollow;


    private ImageView imgGratuity;
    public final static int INTO_COMMENT_DETAIL = 101;
    private AlbumDetail albumDetail;
    private ContentDetailBean contentDetailBean;

    //点赞
    private AutoLinearLayout llTxtClick;
    private TextView tvTxtNumber;
    private TxtClickBean clickBean;

    //相关标签
    private TagCloudView tagCloudView;
    private List<TxtRelevantTagBean> txtRelevantTagBeen = new ArrayList<>();
    private List<String> nameList = new ArrayList<String>();
    private String name;

    private TextView allComment;

    //相关阅读
    private ShowAllListView lvRelevantRead;
    private List<RelatedArticleBean> articleBeen = new ArrayList<>();
    private RelevantReadAdapter readAdapter;
    private View vRelevantRead;


    private AutoLinearLayout llComment;
    private AutoLinearLayout llTag;
    private AutoLinearLayout llRelevantRead;
    private String errorHtml = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
            "\"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "\n" +
            "<html>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<h1 align=\"center\">当前网络没有连接，请重新连接。</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    //    private ProgressBar mProgressBar;
    private WebSettings mWebSettings;
    private ShowAllListView mlvNewComment;
    private ContentCommentAdapter newCommentAdapter;
    private AutoLinearLayout mllNewComment;
    private ImageView mIvDefultPage;

//    //web页面图片线程
//    Handler handler =new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String picFile = (String) msg.obj;
//            String[] split = picFile.split("/");
//            String fileName = split[split.length-1];
//            try {
//                MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            // 最后通知图库更新
//            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
//            Toast.makeText(context,"图片保存图库成功",Toast.LENGTH_LONG).show();
//        }
//    };

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_img_txt;
    }

    @Override
    protected void initView() {

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        rlBestow = (AutoRelativeLayout) findViewById(R.id.rl_bestow);
        btnComment = (TextView) findViewById(R.id.btn_comment);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        alWebTv = (AutoLinearLayout) findViewById(R.id.al_web_tv);
        rlShare = (AutoRelativeLayout) findViewById(R.id.rl_share);
        lvComment = (ShowAllListView) findViewById(R.id.lv_comment);
        svContainer = (ObservableScrollView) findViewById(R.id.sv_container);
        llContainer = (AutoLinearLayout) findViewById(R.id.ll_container);
//        rlComment = (AutoRelativeLayout) findViewById(R.id.rl_comment);
        imgGratuity = (ImageView) findViewById(R.id.img_gratuity);
//        imgCollect = (ImageView) findViewById(R.id.img_collect);
        video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
        layoutBottom = (AutoLinearLayout) findViewById(R.id.layout_bottom);
        llTxtFollow = (AutoRelativeLayout) findViewById(R.id.rl_share);
        imgTxtFollow = (ImageView) findViewById(R.id.img_audio_more);
//        llPlay = (AutoLinearLayout) findViewById(R.id.ll_tv_follow);
//        imgPlay = (TextView) findViewById(R.id.tv_follow);
        llTxtClick = (AutoLinearLayout) findViewById(R.id.ll_txt_click);
        tvTxtNumber = (TextView) findViewById(R.id.tv_click_number);
        tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud_view);
        lvRelevantRead = (ShowAllListView) findViewById(R.id.lv_relevant_read);
        llComment = (AutoLinearLayout) findViewById(R.id.ll_comment);
        mllNewComment = (AutoLinearLayout) findViewById(R.id.ll_new);
        llTag = (AutoLinearLayout) findViewById(R.id.ll_tag);
        llRelevantRead = (AutoLinearLayout) findViewById(R.id.ll_relevant_read);
        allComment = (TextView) findViewById(R.id.tv_more_comment);
        mlvNewComment = (ShowAllListView) findViewById(R.id.lv_new_comment);
        mIvDefultPage = (ImageView) findViewById(R.id.iv_defult_page);
        vRelevantRead = findViewById(R.id.v_relevant_read);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initAction() {
        albumContent = getIntent().getParcelableExtra("bean");
        isFocus = getIntent().getStringExtra("isFocus");
//        if (isFocus == null) {
//            imgPlay.setEnabled(false);
//        }
        toHttpGetDetail();
        toHttpGetFocusStatus();
        toHttpGetMozId();
        toHttpGetComment();
        resultIntent = new Intent();
        resultIntent.putExtra("isFocus", isFocus);
        setResult(RESULT_OK, resultIntent);
        btnComment.setOnClickListener(this);
        imgGratuity.setOnClickListener(this);
//        rlComment.setOnClickListener(this);
//        imgCollect.setOnClickListener(this);
        llTxtFollow.setOnClickListener(this);
//        llPlay.setOnClickListener(this);
        adapter = new ContentCommentAdapter(commDatas, this);
        lvComment.setAdapter(adapter);
        newCommentAdapter = new ContentCommentAdapter(mNewestDates, this);
        mlvNewComment.setAdapter(newCommentAdapter);
        llReturn.setOnClickListener(this);
        tagCloudView.setOnTagClickListener(this);
        llTxtClick.setOnClickListener(this);
        lvRelevantRead.setOnItemClickListener(this);
        rlShare.setOnClickListener(this);
        allComment.setOnClickListener(this);
        setCommDeleteListener();
        webImgTxt = new WebView(this);
        llContainer.addView(webImgTxt, 0);
        mWebSettings = webImgTxt.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Use the API 11+ calls to disable the controls
            mWebSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
            mWebSettings.setDisplayZoomControls(false);
        } else {
            // Use the reflection magic to make it work on earlier APIs
            getControlls();
        }
        webImgTxt.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        mWebSettings.setSupportZoom(true);          //支持缩放
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕x
        //设置自适应屏幕，两者合用
        mWebSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        mWebSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        mWebSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        mWebSettings.setSavePassword(true);
        mWebSettings.setSaveFormData(true);// 保存表单数据
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setGeolocationEnabled(true);// 启用地理定位
        mWebSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        mWebSettings.setSupportMultipleWindows(true);// 新加

        /**
         * liuchao new  add
         */
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        mWebSettings.setBlockNetworkImage(true);
        xwebchromeclient = new myWebChromeClient();
        webImgTxt.setWebChromeClient(xwebchromeclient);
        webImgTxt.setWebViewClient(new myWebViewClient());
        webImgTxt.loadUrl(SystemConstant.MYURL + "/article/m/" + getIntent().getStringExtra("id"));
        webImgTxt.addJavascriptInterface(new JsInterface(), "test");
        webImgTxt.addJavascriptInterface(new JsUserJump(), "userimg");
        webImgTxt.addJavascriptInterface(new JsPhotoEnlargeJump(), "photo");

//        svContainer.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                if (!titleBarShow && y > SystemConstant.ScreenHeight / 2) {
//                    if (albumDetail != null)
//                        txtTitle.setText(contentDetailBean.getUsername());
//                    titleBarShow = true;
//                    llPlay.setVisibility(View.GONE);
//                    llTxtFollow.setVisibility(View.GONE);
//                    if (isFocus.equals("1")) {
//                        imgPlay.setText("关注+");
//                        imgPlay.setTextColor(R.color.white);
//                        imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow_black);
//                    } else {
//                        imgPlay.setText("已关注");
//                        imgPlay.setTextColor(R.color.black);
//                        imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow);
//                    }
//
//                } else if (titleBarShow && y <= SystemConstant.ScreenHeight / 2) {
//                    txtTitle.setText("");
//                    titleBarShow = false;
//                    llPlay.setVisibility(View.GONE);
//                    llTxtFollow.setVisibility(View.GONE);
//                }
//            }
//        });
//        webImgTxt.setOnScrollListener(new ProgressWebView.OnScrollListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                currentScrollHeight = oldScrollY;
//            }
//        });
        svContainer.post(new Runnable() {
            @Override
            public void run() {
                int[] loc = new int[2];
                svContainer.getLocationOnScreen(loc);
                topLayoutHeight = loc[1];
            }
        });
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomLayoutHeight = layoutBottom.getHeight();
            }
        });
    }

    //相关阅读
    private void toHttpRelevantRead() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pages", 1);
        map.put("pagesize", 3);
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("id", contentDetailBean.getId());
        map.put("entity", entity);
        ApiServerManager.getInstance().getApiServer().getRelatedArticle(map).enqueue(new RetrofitCallBack<List<RelatedArticleBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<RelatedArticleBean>>> call, Response<RetrofitResult<List<RelatedArticleBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().size() > 0) {
                        articleBeen = response.body().getData();
                        readAdapter = new RelevantReadAdapter(articleBeen, ImgTxtAlbumActivity.this);
                        lvRelevantRead.setAdapter(readAdapter);
                        readAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<RelatedArticleBean>>> call, Throwable t) {
                llRelevantRead.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ImgTxtAlbumActivity.class);
        intent.putExtra("id", articleBeen.get(position).getId());
        startActivity(intent);
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
                                    if (commDatas.size() == 0) {
                                        llComment.setVisibility(View.GONE);
                                    }
                                    toHttpGetComment();
                                    toHttpGetNewestComment();
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
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).
                                enqueue(new RetrofitCallBack<Object>() {
                                    @Override
                                    public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                                    }

                                    @Override
                                    public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                        if (response.body().getCode() == 200) {
                                            //删除成功
                                            commDatas.get(parentPosition).getSons().remove(sonPosition);
                                            commDatas.get(parentPosition).getFather().
                                                    setCommentnum(commDatas.get(parentPosition).getFather().getCommentnum() - 1);
                                            toHttpGetComment();
                                            toHttpGetNewestComment();
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

        newCommentAdapter.setOnDeleteClickListener(new ContentCommentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mNewestDates.get(position).getFather().getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).enqueue(new RetrofitCallBack<Object>() {
                            @Override
                            public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                            }

                            @Override
                            public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                if (response.body().getCode() == 200) {
                                    //删除成功
                                    mNewestDates.remove(position);
                                    if (mNewestDates.size() == 0) {
                                        mllNewComment.setVisibility(View.GONE);
                                    }
                                    toHttpGetComment();
                                    toHttpGetNewestComment();
                                } else
                                    ToastMaker.showShortToast(response.body().getMsg());
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
            }
        });

        newCommentAdapter.setOnSonDeleteClickListener(new ContentCommentAdapter.OnSonDeleteClickListener() {
            @Override
            public void onDeleteClick(final int parentPosition, final int sonPosition) {
                showWaringDialog(null, "确认删除此内容？", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("commentid", mNewestDates.get(parentPosition).getSons().get(sonPosition).getCommentid());
                        ApiServerManager.getInstance().getApiServer().deleteComment(map).
                                enqueue(new RetrofitCallBack<Object>() {
                                    @Override
                                    public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {

                                    }

                                    @Override
                                    public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                                        if (response.body().getCode() == 200) {
                                            //删除成功
                                            mNewestDates.get(parentPosition).getSons().remove(sonPosition);
                                            mNewestDates.get(parentPosition).getFather().
                                                    setCommentnum(mNewestDates.get(parentPosition).getFather().getCommentnum() - 1);
                                            toHttpGetComment();
                                            toHttpGetNewestComment();
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

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.GET_CONTENT_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    contentDetailBean = (ContentDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), ContentDetailBean.class);
                    toHttpGetAlbumDetail();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    private String mozId;

    private void toHttpGetMozId() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().articleIdGetMozId(map).enqueue(new RetrofitCallBack<String>() {
            @Override
            public void onSuccess(Call<RetrofitResult<String>> call, Response<RetrofitResult<String>> response) {
                if (response.body().getCode() == 200) {
                    mozId = response.body().getData();
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<String>> call, Throwable t) {

            }
        });
    }

    private void toHttpGetFocusStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        ApiServerManager.getInstance().getApiServer().articleIdGetFocus(map).enqueue(new RetrofitCallBack<Boolean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<Boolean>> call, Response<RetrofitResult<Boolean>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData()) {
                        isFocus = "1";
                    } else {
                        isFocus = "0";
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<Boolean>> call, Throwable t) {

            }
        });
    }

    /**
     * 获取专辑详情
     */
    private void toHttpGetAlbumDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("albumid", contentDetailBean.getAlbumid());
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_ALBUM_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    albumDetail = (AlbumDetail) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AlbumDetail.class);
                    toHttpClickLove();
                    toHttpRelevantLabel();
                    if (isFocus != null)
                        return;
                    Map<String, Object> sonMap = new HashMap<String, Object>();
                    sonMap.put("id", contentDetailBean.getUserid());
                    HttpUtil.post(mContext, SystemConstant.URL + SystemConstant.QUERY_USER_INFO, sonMap, new HttpUtil.HttpCallBack() {
                        @Override
                        public void successResult(ResultEntity result) throws JSONException {
                            if (result.getCode() == 200) {
                                UserInforMation user = (UserInforMation) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), UserInforMation.class);
//                                imgPlay.setEnabled(true);
//                                isFocus = user.getIsfocus() + "";
//                                if (isFocus.equals("1")) {
//                                    imgPlay.setText("关注+");
//                                    imgPlay.setTextColor(R.color.white);
//                                    imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow_black);
//                                } else {
//                                    imgPlay.setText("已关注");
//                                    imgPlay.setTextColor(R.color.black);
//                                    imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow);
//                                }
                                webImgTxt.loadUrl("javascript:showInfoFromJava('" + isFocus + "')");
                            }
                        }

                        @Override
                        public void errorResult(Throwable ex, boolean isOnCallback) {
                        }
                    });
                } else {
                    imgGratuity.setEnabled(false);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
                imgGratuity.setEnabled(false);
                toHttpClickLove();
                toHttpRelevantLabel();
            }
        });
    }

    //点赞详情
    private void toHttpClickLove() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", contentDetailBean.getId());
        map.put("type", "ARTICLE");
        ApiServerManager.getInstance().getApiServer().getTxtClick(map).enqueue(new RetrofitCallBack<TxtClickBean>() {
            @Override
            public void onSuccess(Call<RetrofitResult<TxtClickBean>> call, Response<RetrofitResult<TxtClickBean>> response) {
                if (response.body().getCode() == 200) {
                    clickBean = response.body().getData();
                    tvTxtNumber.setText(clickBean.getLikeNumber() + "");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult<TxtClickBean>> call, Throwable t) {
            }
        });
    }

    //相关标签
    private void toHttpRelevantLabel() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", contentDetailBean.getId());
        ApiServerManager.getInstance().getApiServer().getRelatedTag(map).enqueue(new RetrofitCallBack<List<TxtRelevantTagBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<List<TxtRelevantTagBean>>> call, Response<RetrofitResult<List<TxtRelevantTagBean>>> response) {
                if (response.body().getCode() == 200) {
                    if (response.body().getData().size() > 0) {
                        txtRelevantTagBeen = response.body().getData();
                        for (int i = 0; i < txtRelevantTagBeen.size(); i++) {
                            name = "#" + txtRelevantTagBeen.get(i).getName();
                            nameList.add(name);
                        }
                        tagCloudView.setTags(nameList);
                    }
                }
                toHttpRelevantRead();
            }

            @Override
            public void onFailure(Call<RetrofitResult<List<TxtRelevantTagBean>>> call, Throwable t) {
                toHttpRelevantRead();
                llTag.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onTagClick(int position) {
        Intent intent = new Intent(this, TagDetailActivity.class);
        intent.putExtra("id", txtRelevantTagBeen.get(position).getId());
        intent.putExtra("type", 2);
        mContext.startActivity(intent);
    }

    /**
     * 判断是否收藏
     */
//    private void toHttpGetCollectState() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("contentid", getIntent().getStringExtra("id"));
//        map.put("type", 5);
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ARTICLE_IS_COLLECTED, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    if ((boolean) result.getData()) {
//                        imgCollect.setImageResource(R.drawable.icon_silhouete_collection);
//                        imgCollect.setTag(1);
//                    } else {
//                        imgCollect.setImageResource(R.drawable.icon_share_imgtxt_collents);
//                        imgCollect.setTag(0);
//                    }
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
//    }
    private void getControlls() {
        try {
            Class webview = Class.forName("android.webkit.WebView");
            Method method = webview.getMethod("getZoomButtonsController");
            zoom_controll = (ZoomButtonsController) method.invoke(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热门评论
     */
    private void toHttpGetComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 5);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        entity.put("sons", 3);
        entity.put("type", 1);
        map.put("entity", entity);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_HOT_LIST, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    List<ContentCommentBean> data = GsonUtils.json2List(GsonUtils.obj2Json(result.getData()), ContentCommentBean.class);
                    commDatas.removeAll(commDatas);
                    commDatas.addAll(data);
                    if (data.size() > 0) {
                        llComment.setVisibility(View.VISIBLE);
                        allComment.setVisibility(View.VISIBLE);
                    } else {
                        llComment.setVisibility(View.GONE);
                        allComment.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    llComment.setVisibility(View.GONE);
                    allComment.setVisibility(View.GONE);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                llComment.setVisibility(View.GONE);
                allComment.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取最新评论列表
     */
    private void toHttpGetNewestComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("pages", 1);
        map.put("pagesize", 20);
        Map<String, Object> entity = new HashMap<>();
        entity.put("targetid", getIntent().getStringExtra("id"));
        entity.put("type", 1);
        entity.put("sons", 1);
        map.put("entity", entity);

        ApiServerManager.getInstance().getApiServer().getCommentNewestList(map).enqueue(new RetrofitCallBack<ArrayList<ContentCommentBean>>() {
            @Override
            public void onSuccess(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Response<RetrofitResult<ArrayList<ContentCommentBean>>> response) {
                if (response.body().getCode() == 200) {
                    mNewestDates.removeAll(mNewestDates);
                    mNewestDates.addAll(response.body().getData());
                    if (mNewestDates.size() > 0) {
                        mllNewComment.setVisibility(View.VISIBLE);
                        allComment.setVisibility(View.VISIBLE);
                    } else {
                        mllNewComment.setVisibility(View.GONE);
                        allComment.setVisibility(View.GONE);
                    }
                } else {
                    mllNewComment.setVisibility(View.GONE);
                    allComment.setVisibility(View.GONE);
                }
                if (commDatas.size() == 0 && mNewestDates.size() == 0) {
                    mIvDefultPage.setVisibility(View.VISIBLE);
                    mllNewComment.setVisibility(View.VISIBLE);
                } else mIvDefultPage.setVisibility(View.GONE);
                newCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetrofitResult<ArrayList<ContentCommentBean>>> call, Throwable t) {

            }
        });
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            mWebSettings.setBlockNetworkImage(false);
//            if (!mWebSettings.getLoadsImagesAutomatically()) {
//                //设置wenView加载图片资源
//                mWebSettings.setLoadsImagesAutomatically(true);
//            }

            super.onPageFinished(view, url);
//            waitdialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadData(errorHtml, "text/html", "UTF-8");
        }
    }

    public class myWebChromeClient extends WebChromeClient {
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webImgTxt.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            video_fullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webImgTxt.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
//        @Override
//        public View getVideoLoadingProgressView() {
//            if (xprogressvideo == null) {
//                LayoutInflater inflater = LayoutInflater
//                        .from(ImgTxtAlbumActivity.this);
//                xprogressvideo = inflater.inflate(
//                        R.layout.video_loading_progress, null);
//            }
//            return xprogressvideo;
//        }
//
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            llContainer.setVisibility(View.VISIBLE);
            if (newProgress == 100) {
                //加载网页完成
//                btnComment.setEnabled(true);
                toHttpGetComment();
                toHttpGetNewestComment();
//                imgGratuity.setVisibility(View.VISIBLE);
                mIvDefultPage.setVisibility(View.VISIBLE);
                llTxtClick.setVisibility(View.VISIBLE);
                llTag.setVisibility(View.VISIBLE);
                llRelevantRead.setVisibility(View.VISIBLE);
                llComment.setVisibility(View.VISIBLE);
                allComment.setVisibility(View.GONE);
                vRelevantRead.setVisibility(View.VISIBLE);
                llTag.setVisibility(View.VISIBLE);
//                rlComment.setEnabled(true);
//                webImgTxt.getProgressbar().setVisibility(View.GONE);
                webImgTxt.loadUrl("javascript:showInfoFromJava('" + isFocus + "')");
//                mProgressBar.setVisibility(View.GONE);
            } else {
//                mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                mProgressBar.setProgress(newProgress);//设置进度值
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoApplication.getInstance().activityExisted(EmptyActivity.class)) {
            DemoApplication.getInstance().finishActivity(EmptyActivity.class);
        }
        webImgTxt.onResume();
        webImgTxt.resumeTimers();
        if (commDatas != null && commDatas.size() > 0)
            adapter.notifyDataSetChanged();
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "onPause");
        webImgTxt.onPause();
        webImgTxt.pauseTimers();
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        if (!(DemoApplication.getInstance().currentActivity() instanceof ImgTxtAlbumActivity))
            return;
        switch (msg.getWhat()) {
            case SystemConstant.COMMENT_EXPAND:
//                int old = svContainer.getHeight();
//                ViewGroup.LayoutParams params = svContainer.getLayoutParams();
//                params.height = old + msg.getArg1();
//                svContainer.setLayoutParams(params);
//                int newHeight = svContainer.getHeight();
//
                break;
            case SystemConstant.ON_CLICK_CHANGE:
                //评论的赞发生变化
                String commentId = (String) msg.getBody();
                if (msg.getArg1() == 0) {
                    //取消点赞
                    //该评论存在于点赞列表
                    if (commentClickList.contains(commentId)) {
                        //删除数据库的音频
                        DB.getInstance(DemoApplication.applicationContext).deleteClick(commentId);
                        //将该音频从点赞列表移除
                        commentClickList.remove(commentId);
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
                }
                break;
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "打赏失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "打赏成功", Toast.LENGTH_SHORT).show();
                    toHttpBestow();
                } else if (result == -2) {
                    Toast.makeText(mContext, "打赏取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "打赏失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case SystemConstant.MOZ_COMMENT_DELETE:
                toHttpGetComment();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
        EventBus.getDefault().unregister(this);
        video_fullView.removeAllViews();
        if (webImgTxt != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (llContainer != null) {
                    llContainer.removeView(webImgTxt);
                }
                webImgTxt.removeAllViews();
                webImgTxt.destroy();
            } else {
                webImgTxt.removeAllViews();
                webImgTxt.destroy();
                if (llContainer != null) {
                    llContainer.removeView(webImgTxt);
                }
            }
            webImgTxt = null;
        }
//        webImgTxt.clearCache(true);
//        webImgTxt.loadUrl("about:blank");
//        Log.e("onDestroy", "about:blank");
//        webImgTxt.stopLoading();
//        webImgTxt.setWebChromeClient(null);
//        webImgTxt.setWebViewClient(null);
//        webImgTxt.removeAllViews();
//        webImgTxt.destroy();
//        webImgTxt = null;
        if (mTimer != null) {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        }
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

    private void toHttpClick() {
        Map<String, Object> map = new HashMap<>();
        map.put("uptype", "ARTICLE");
        map.put("targetid", getIntent().getStringExtra("id"));
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.TO_HTTP_CLICK, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                Toast.makeText(DemoApplication.applicationContext, "点赞成功", Toast.LENGTH_SHORT).show();
                toHttpClickLove();
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                // webViewDetails.loadUrl("about:blank");
                hideCustomView();
                return true;
            } else {
                webImgTxt.loadUrl("about:blank");
                ImgTxtAlbumActivity.this.finish();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_audio_more:
                if (!Util.hintLogin(ImgTxtAlbumActivity.this))
                    return;
                showSharePop();
                break;
//            case R.id.ll_tv_follow:
//                //关注或取消关注
//                if (isFocus.equals("1")) {
//                    isFocus = "0";
//                    imgPlay.setText("关注+");
//                    imgPlay.setTextColor(R.color.white);
//                    imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow_black);
//                    webImgTxt.loadUrl("javascript:showInfoFromJava('" + isFocus + "')");
//                    resultIntent.putExtra("isFocus", isFocus);
//                    setResult(RESULT_OK, resultIntent);
//                } else {
//                    isFocus = "1";
//                    imgPlay.setText("已关注");
//                    imgPlay.setTextColor(R.color.black);
//                    imgPlay.setBackgroundResource(R.drawable.shape_imgtxt_follow);
//                    webImgTxt.loadUrl("javascript:showInfoFromJava('" + isFocus + "')");
//                    resultIntent.putExtra("isFocus", isFocus);
//                    setResult(RESULT_OK, resultIntent);
//                }
//                break;
            case R.id.ll_return:
//                webImgTxt.loadUrl("javascript:showInfoFromJava('" + "1" + "')");
                finish();
                break;
            case R.id.rl_share:
                if (!Util.hintLogin(ImgTxtAlbumActivity.this))
                    return;
                showSharePop();
                break;
            case R.id.tv_more_comment:
                Intent intent = new Intent(this, AllCommentActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
                break;
//            case R.id.rl_comment:
//                Intent intent = new Intent(this, AllCommentActivity.class);
//                intent.putExtra("id", getIntent().getStringExtra("id"));
//                startActivity(intent);
//                try {
//                    AlbumCommentListAdapter.ViewHolder holder = (AlbumCommentListAdapter.ViewHolder) lvComment.getChildAt(lvComment.getFirstVisiblePosition()).getTag();
//                    int[] loc = new int[2];
//                    lvComment.getChildAt(lvComment.getFirstVisiblePosition()).getLocationOnScreen(loc);
//                    if (loc[1] < SystemConstant.ScreenHeight - bottomLayoutHeight) {
//                        if (holder.position == 0 && loc[1] > topLayoutHeight && commDatas.size() > 0 && lvComment.getHeight() > SystemConstant.ScreenHeight - bottomLayoutHeight - topLayoutHeight) {
////                        svContainer.scrollTo(0, webImgTxt.getHeight() - SystemConstant.ScreenHeight + topLayoutHeight + bottomLayoutHeight);
//                            scroll(webImgTxt.getHeight() - SystemConstant.ScreenHeight + topLayoutHeight + bottomLayoutHeight);
//                            currentScrollHeight = svContainer.getScrollY();
//                            return;
//                        }
//                        scroll(currentScrollHeight);
////                    svContainer.scrollTo(0, currentScrollHeight);
//                    } else {
//                        currentScrollHeight = svContainer.getScrollY();
//                        scroll(webImgTxt.getHeight());
////                    svContainer.scrollTo(0, webImgTxt.getHeight());
//                    }
//                } catch (Exception e) {
//                    if (svContainer.getScrollY() + SystemConstant.ScreenHeight - bottomLayoutHeight - topLayoutHeight == webImgTxt.getHeight()) {
//                        scroll(currentScrollHeight);
//                    } else {
//                        currentScrollHeight = svContainer.getScrollY();
//                        scroll(webImgTxt.getHeight());
//                    }
//                }
//                break;
            case R.id.btn_comment:
                if (!Util.hintLogin(ImgTxtAlbumActivity.this))
                    return;
                showCommPop();
                break;
            case R.id.img_gratuity:
                if (!Util.hintLogin(ImgTxtAlbumActivity.this))
                    return;
                showCustomPop();
                break;
            case R.id.img_collect:
                if (!Util.hintLogin(ImgTxtAlbumActivity.this))
                    return;
                break;
            case R.id.ll_txt_click:
                if (clickBean == null) {
                    return;
                }
                if (clickBean.getIsLike() == 1) {
                    Toast.makeText(mContext, "你已经点过了", Toast.LENGTH_SHORT).show();
                } else {
                    tvTxtNumber.setText((clickBean.getLikeNumber() + 1) + "");
                    toHttpClick();
                }
                break;
        }
    }

//    private void toHttpCollect() {
//        showLoadingDialog("正在操作");
//        Map<String, Object> map = new HashMap<>();
//        String url;
//        if (imgCollect.getTag().equals(0)) {
//            url = SystemConstant.URL + SystemConstant.COLLECT_ARTICLE;
//        } else {
//            url = SystemConstant.URL + SystemConstant.UNCOLLECT_ARTICLE;
//        }
//        map.put("contentid", getIntent().getStringExtra("id"));
//        map.put("type", 5);
//        HttpUtil.post(this, url, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    if (imgCollect.getTag().equals(0)) {
//                        imgCollect.setTag(1);
//                        imgCollect.setImageResource(R.drawable.icon_silhouete_collection);
//                    } else {
//                        imgCollect.setTag(0);
//                        imgCollect.setImageResource(R.drawable.icon_share_imgtxt_collents);
//                    }
//                }
//                loadingDismiss();
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                loadingDismiss();
//            }
//        });
//    }

    private void showCustomPop() {
        if (mPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_custom_rmb, null);
            view.findViewById(R.id.img_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ImgTxtAlbumActivity.this, mPopupWindow);
                }
            });
            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etCustomPrice.getText()) && !TextUtils.equals("0", etCustomPrice.getText().toString())) {
                        Util.finishPop(ImgTxtAlbumActivity.this, mPopupWindow);
                    } else {
                        Toast.makeText(ImgTxtAlbumActivity.this, "请输入您要打赏的金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((TextView) view.findViewById(R.id.tv_pop_author_name)).setText(albumDetail == null ? "" : contentDetailBean.getUsername());
            etCustomPrice = (EditText) view.findViewById(R.id.et_custom_price);
            etCustomPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                    String numString = s.toString();
//                    if (numString.startsWith("0")) {
//                        etCustomPrice.setText("");
//                        etCustomPrice.setSelection(3);
//                    } else {
//                        if (numString.length() > 0) {
//                            double num = Integer.parseInt(numString);
//                            if (num > 200) {
//                                etCustomPrice.setText(200 + "");
//                                etCustomPrice.setSelection(3);
//                            }
//                        }else{
//                            etCustomPrice.setText("");
//                            etCustomPrice.setSelection(3);
//                        }
//                    }

                    final String text = s == null ? "" : s.toString();
                    int index = text.indexOf(".");
                    int index0 = text.indexOf("0");
                    if (index0 == 0 && text.substring(1).equals("0")) {
                        etCustomPrice.setText("0");
                        etCustomPrice.setSelection(1);
                    }
                    if (index > 0) {
                        if (text.length() > index + 3) {
                            String payText = text.substring(0, index + 3);
                            etCustomPrice.setText(payText);
                            etCustomPrice.setSelection(payText.length());
                        }
                    } else if (index == 0) {
                        etCustomPrice.setText("");
                    }
                    if (!TextUtils.isEmpty(etCustomPrice.getText().toString())) {
                        float f = Float.parseFloat(etCustomPrice.getText().toString());
                        if (f >= 1.00) {
                            if (f > 200.00) {
                                etCustomPrice.setText(200 + "");
//                                etCustomPrice.setSelection(3);
                                etCustomPrice.setSelection(etCustomPrice.length());
                            }
                        }
                    } else {
                        ToastMaker.showShortToast("价格不能为空");
                    }
                }
            });
            mPopupWindow = new PopupWindow(view, -2, -2);
            mPopupWindow.setContentView(view);
            mPopupWindow.setAnimationStyle(R.style.popwin_hint_anim_style);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f;
            getWindow().setAttributes(lp);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(etCustomPrice.getText().toString())) {
                        price = "";
                    } else {
                        price = etCustomPrice.getText().toString();
                        if (TextUtils.isEmpty(price) && TextUtils.equals("0", price)) {
                            Util.finishPop(ImgTxtAlbumActivity.this, mPopupWindow);
                        } else {
                            showPayWindow();
                        }

                    }
                    Util.finishPop(ImgTxtAlbumActivity.this, mPopupWindow);
                }
            });
            mPopupWindow.showAtLocation(alWebTv, Gravity.CENTER, 0, 0);
        } else {
            etCustomPrice.setText("");
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            mPopupWindow.showAtLocation(alWebTv, Gravity.CENTER, 0, 0);
        }
    }

    private void showPayWindow() {
        hideSoftKeyboard();
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ImgTxtAlbumActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(ImgTxtAlbumActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(ImgTxtAlbumActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ImgTxtAlbumActivity.this, payPopupWindow);
                    showWaringDialog("支付", "是否使用墨子币打赏文章？", new OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            toHttpBalancePay();
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                }
            });
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ImgTxtAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ImgTxtAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(alWebTv, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ImgTxtAlbumActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = ImgTxtAlbumActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            ImgTxtAlbumActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(alWebTv, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥" + price + ",请再次确认购买");
    }

    private void toHttpBalancePay() {
        if (albumDetail == null)
            return;
        showLoadingDialog("请稍候");
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        map.put("goodid", getIntent().getStringExtra("id"));
        map.put("amount", price);
        map.put("goodtype", "TIP_ARTICLE");
        map.put("sellerid", contentDetailBean.getUserid());
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + "size/balance/pay/insert", map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                loadingDismiss();
                if (result.getCode() == 200) {
                    id = (String) result.getData();
                    toHttpBestow();
                } else if (result.getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                loadingDismiss();
            }
        });
    }

    private void aliPay() {
        if (albumDetail == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("price", price);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/alipay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    id = aliPay.getId();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ImgTxtAlbumActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "打赏成功", Toast.LENGTH_SHORT).show();
                                        toHttpBestow();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "打赏失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 打赏
     */
    private void toHttpBestow() {
        if (albumDetail == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("tip_type", "TIP_ARTICLE");
        map.put("trade_id", id);
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("tip_price", price);
        map.put("message", "");
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.BESTOW, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "打赏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "打赏失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DemoApplication.applicationContext, "打赏失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void wxPay() {
        if (albumDetail == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", "TIP_ARTICLE");
        map.put("goods_id", getIntent().getStringExtra("id"));
        map.put("seller_id", contentDetailBean.getUserid());
        map.put("price", price);
        HttpUtil.post(this, SystemConstant.URL + "size/transactions/wechatPay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    id = wxPay.getId();
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(ImgTxtAlbumActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void showCommPop() {
        if (commPopupWindow == null) {
            commView = View.inflate(this, R.layout.popupwindow_comm, null);
            etPopupwindowComm = (EditText) commView.findViewById(R.id.et_popupwindow_comm);
            btnSubmitComment = (Button) commView.findViewById(R.id.btn_submit_comment);
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
                    inputMethodManager.hideSoftInputFromWindow(etPopupwindowComm.getWindowToken(), 0);
                    Util.finishPop(ImgTxtAlbumActivity.this, commPopupWindow);
                    toHttpSendComment();
                    etPopupwindowComm.setText("");
                }
            });
            commPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Util.finishPop(ImgTxtAlbumActivity.this, commPopupWindow);
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
        commPopupWindow.showAtLocation(alWebTv, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 提交评论
     */
    private void toHttpSendComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("targetid", getIntent().getStringExtra("id"));
        map.put("content", etPopupwindowComm.getText().toString());
        map.put("type", 1);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SEND_COMMENT, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    toHttpGetComment();
                    toHttpGetNewestComment();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void scroll(int y) {
//        rlComment.setEnabled(false);
        int temp = currentScrollHeight;
        final int current = svContainer.getScrollY();
        int lenght = y - current;
        boolean flag = false;
        if (lenght < 0) {
            lenght = 0 - lenght;
            flag = true;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        }
        mTimer = new Timer();
        final int finalLenght = lenght;
        final int j = finalLenght / 20;
        if (j == 0)
            return;
        final boolean finalFlag = flag;
        //在0.3秒内滑动完成
        mTimerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (i < finalLenght / j) {
                    if (finalFlag) {
                        if (i == finalLenght / j - 1) {
                            svContainer.scrollTo(0, current - j * (i + 1) - finalLenght + j * (i + 1));
                        } else {
                            svContainer.scrollTo(0, current - j * (i + 1));
                        }
                    } else {
                        if (i == finalLenght / j - 1) {
                            svContainer.scrollTo(0, current + j * (i + 1) + finalLenght - j * (i + 1));
                        } else {
                            svContainer.scrollTo(0, current + j * (i + 1));
                        }
                    }
                    i++;
                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 10);
        currentScrollHeight = temp;
    }

    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(mContext, R.layout.popupwindow_info_share, null);
            llSearchWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            llSearchWxCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            llSearchQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
            llShareUrl = (AutoLinearLayout) view.findViewById(R.id.ll_share_url);
            llShareCollect = (AutoLinearLayout) view.findViewById(R.id.ll_share_collect);
            txtCloseShare = (TextView) view.findViewById(R.id.txt_close_share);
//            imgCollect = (ImageView) view.findViewById(R.id.img_collect);
//            imgClick = (CheckBox) view.findViewById(R.id.img_click);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(alWebTv, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(ImgTxtAlbumActivity.this, sharePopupWindow);
                }
            });
//            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.finishPop(ImgTxtAlbumActivity.this, sharePopupWindow);
//                }
//            });
//            imgCollect.setTag(0);
//            toHttpGetCollectState();
            llSearchWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "data:image/jpeg;base64,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");
//                    String url = "http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                            .withText("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (albumContent != null) {
                        if (albumContent.getAlbumName() == null)
                            albumContent.setAlbumName("墨子文章");
                        shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                albumContent.getArticlename(),
                                albumContent.getAlbumName(),
                                SystemConstant.ACCESS_IMG_HOST + albumContent.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    } else if (contentDetailBean != null && albumDetail != null) {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                contentDetailBean.getName(),
                                albumDetail.getAlbumname(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    }
                }
            });
            llSearchWxCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                            .withTitle("test")
//                            .withText("赛思测试分享")
//                            .withMedia(new UMImage(ImgTxtAlbumActivity.this, "data:image/jpeg;base64,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"))
//                            .withTargetUrl("http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996")
//                            .share();
                    if (albumContent != null) {
                        if (albumContent.getAlbumName() == null)
                            albumContent.setAlbumName("墨子文章");
                        shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                albumContent.getArticlename(),
                                albumContent.getAlbumName(),
                                SystemConstant.ACCESS_IMG_HOST + albumContent.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    } else if (contentDetailBean != null && albumDetail != null) {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                contentDetailBean.getName(),
                                albumDetail.getAlbumname(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    }
                }
            });
            llSearchQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
//                    String url = "http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996";
//                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                            .withTitle("赛思测试分享")
//                            .withMedia(image)
//                            .withTargetUrl(url)
//                            .share();
                    if (albumContent != null) {
                        if (albumContent.getAlbumName() == null)
                            albumContent.setAlbumName("墨子文章");
                        shareWeb(
                                SHARE_MEDIA.QQ,
                                albumContent.getArticlename(),
                                albumContent.getAlbumName(),
                                SystemConstant.ACCESS_IMG_HOST + albumContent.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    } else if (contentDetailBean != null && albumDetail != null) {
                        shareWeb(
                                SHARE_MEDIA.QQ,
                                contentDetailBean.getName(),
                                albumDetail.getAlbumname(),
                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getCoverimgurl(),
                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
                                , getIntent().getStringExtra("id")
                        );
                    }
                }
            });
            llShareUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id"));
                    ToastMaker.showShortToast("复制成功！");
                }
            });
//            llShareCollect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toHttpCollect();
//                }
//            });

            txtCloseShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(ImgTxtAlbumActivity.this, sharePopupWindow);
                }
            });
//            llSearchSina.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    UMImage image = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
////                    String url = "http://192.168.1.197/cmsweb/article/m/1ce2fe04b4b544dbafa6ff124dd29996";
////                    new ShareAction(ImgTxtAlbumActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
////                            .withText("赛思测试分享")
////                            .withTitle("test")
////                            .withMedia(image)
////                            //.withExtra(new UMImage(ShareActivity.this,R.drawable.ic_launcher))
////                            .withTargetUrl(url)
////                            .share();
//                    if (albumContent != null) {
//                        if (albumContent.getAlbumName() == null)
//                            albumContent.setAlbumName("墨子文章");
//                        shareWeb(
//                                SHARE_MEDIA.SINA,
//                                albumContent.getArticlename(),
//                                albumContent.getAlbumName(),
//                                SystemConstant.ACCESS_IMG_HOST + albumContent.getCoverimgurl(),
//                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
//                                , getIntent().getStringExtra("id")
//                        );
//                    } else if (contentDetailBean != null && albumDetail != null) {
//                        shareWeb(
//                                SHARE_MEDIA.SINA,
//                                contentDetailBean.getName(),
//                                albumDetail.getAlbumname(),
//                                SystemConstant.ACCESS_IMG_HOST + contentDetailBean.getCoverimgurl(),
//                                SystemConstant.MYURL + "cmsweb/article/share/" + getIntent().getStringExtra("id")
//                                , getIntent().getStringExtra("id")
//                        );
//                    }
//                }
//            });
//            imgClick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (DB.getInstance(DemoApplication.applicationContext).isClick(getIntent().getStringExtra("id"))) {
//                        Toast.makeText(DemoApplication.applicationContext, "你已经点过赞了", Toast.LENGTH_SHORT).show();
//                    } else {
//                        DB.getInstance(DemoApplication.applicationContext).insertClick(getIntent().getStringExtra("id"));
//                        toHttpClick();
//                    }
//                }
//            });
//            imgReport.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Util.finishPop(ImgTxtAlbumActivity.this, sharePopupWindow);
//                    showReportPop();
//                }
//            });
        } else {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = 0.6f;
            ((Activity) mContext).getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(alWebTv, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 取消举报
     */
//    private void toHttpRemoveReport() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", getIntent().getStringExtra("id"));
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.REMOVE_REPORT, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    Toast.makeText(DemoApplication.applicationContext, "取消举报成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DemoApplication.applicationContext, "取消举报失败", Toast.LENGTH_SHORT).show();
//                    imgReport.setChecked(true);
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(DemoApplication.applicationContext, "取消举报失败", Toast.LENGTH_SHORT).show();
//                imgReport.setChecked(true);
//            }
//        });
//    }
//
//    /**
//     * 弹出举报框
//     */
//    private void showReportPop() {
//        if (reportPopupwindow == null) {
//            View view = View.inflate(this, R.layout.popupwindow_report, null);
//            etTitle = (EditText) view.findViewById(R.id.et_title);
//            etContent = (EditText) view.findViewById(R.id.et_content);
//            reportPopupwindow = new PopupWindow(view, -2, -2);
//            reportPopupwindow.setContentView(view);
//            reportPopupwindow.setAnimationStyle(R.style.popwin_hint_anim_style);
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.8f;
//            getWindow().setAttributes(lp);
//            reportPopupwindow.setFocusable(true);
//            reportPopupwindow.setBackgroundDrawable(new BitmapDrawable());
//            reportPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//                @Override
//                public void onDismiss() {
//                    Util.finishPop(ImgTxtAlbumActivity.this, reportPopupwindow);
//                }
//            });
//            //发送举报
//            view.findViewById(R.id.tv_assign).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (TextUtils.isEmpty(etTitle.getText().toString()) || TextUtils.isEmpty(etContent.getText().toString())) {
//                        Toast.makeText(DemoApplication.applicationContext, "标题或内容不能为空", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Util.finishPop(ImgTxtAlbumActivity.this, reportPopupwindow);
//                    toHttpReport(etTitle.getText().toString(), etContent.getText().toString());
//                }
//            });
//            //取消举报
//            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imgReport.setChecked(false);
//                    Util.finishPop(ImgTxtAlbumActivity.this, reportPopupwindow);
//                }
//            });
//            reportPopupwindow.showAtLocation(alWebTv, Gravity.CENTER, 0, 0);
//        } else {
//            etTitle.setText("");
//            etContent.setText("");
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            getWindow().setAttributes(lp);
//            reportPopupwindow.showAtLocation(alWebTv, Gravity.CENTER, 0, 0);
//        }
//    }
//
//    /**
//     * 发送举报
//     *
//     * @param title
//     * @param content
//     */
//    private void toHttpReport(String title, String content) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("title", title);
//        map.put("targetid", getIntent().getStringExtra("id"));
//        map.put("report_type", "ARTICLE");
//        map.put("content", content);
//        HttpUtil.post(this, SystemConstant.URL + SystemConstant.SUBMIT_REPORT, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    Toast.makeText(DemoApplication.applicationContext, "举报成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
//                    imgReport.setChecked(false);
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(DemoApplication.applicationContext, "举报失败", Toast.LENGTH_SHORT).show();
//                imgReport.setChecked(false);
//            }
//        });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImgTxtAlbumActivity.INTO_COMMENT_DETAIL:
                    //从评论详情页面返回
                    int position = data.getIntExtra("position", -1);
                    if (position > -1) {
//                        if (data.getBooleanExtra("isClick", false)) {
//                            //在评论详情页面进行了点赞操作
//                            commDatas.get(position).getFather().setUps(commDatas.get(position).getFather().getUps() + 1);
//                        }
//                        if (data.getBooleanExtra("isComment", false)) {
//                            //在评论详情页面进行了评论操作
//                            commDatas.get(position).getFather().setCommentnum(commDatas.get(position).getFather().getCommentnum() + 1);
//                        }
                        toHttpGetComment();
                        toHttpGetNewestComment();
                    }
                    break;
            }
        }
    }

    private class JsInterface {

        public JsInterface() {
        }

        @JavascriptInterface
        public void showInfoFromJs() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!Util.hintLogin(ImgTxtAlbumActivity.this) || isFocus == null)
                        return;
                    isFocus = isFocus.equals("1") ? "0" : "1";
                    toHttpFocus();
                    webImgTxt.loadUrl("javascript:showInfoFromJava('" + isFocus + "')");
                    resultIntent.putExtra("isFocus", isFocus);
                    setResult(RESULT_OK, resultIntent);
                }
            });
        }
    }

    private void toHttpFocus() {
        if (isFocus == null || mozId == null)
            return;
        if (TextUtils.equals(isFocus, "1")) {
            Map<String, Object> map = new HashMap<>();
            map.put("targetid", mozId);
            map.put("type", 1);
            ApiServerManager.getInstance().getApiServer().focusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                    if (response.body().getCode() == 200) {
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                }
            });
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("targetid", mozId);
            map.put("type", 1);
            ApiServerManager.getInstance().getApiServer().unfocusKnowledge(map).enqueue(new RetrofitCallBack<Object>() {
                @Override
                public void onSuccess(Call<RetrofitResult<Object>> call, Response<RetrofitResult<Object>> response) {
                    if (response.body().getCode() == 200) {
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult<Object>> call, Throwable t) {
                }
            });
        }

//        if (isFocus == null || albumDetail == null)
//            return;
//        Map<String, Object> map = new HashMap<>();
//        map.put("targetid", contentDetailBean.getUserid());
//        map.put("oparetion", Integer.parseInt(isFocus));
//        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.AUTHOR_FOUCE, map, new HttpUtil.HttpCallBack() {
//            @Override
//            public void successResult(ResultEntity result) throws JSONException {
//                if (result.getCode() == 200) {
//                    EventMsg msg = EventMsg.obtain(SystemConstant.ON_USER_INFORMATION);
//                    EventBus.getDefault().post(msg);
//                }
//            }
//
//            @Override
//            public void errorResult(Throwable ex, boolean isOnCallback) {
//
//            }
//        });
    }

    private class JsUserJump {

        public JsUserJump() {
        }

        @JavascriptInterface
        public void jumpUserInformation() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!Util.hintLogin(ImgTxtAlbumActivity.this) || mozId == null)
                        return;
                    Intent intent = new Intent(mContext, MozDetailActivity.class);
                    intent.putExtra("id", mozId);
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
//                    Intent intent = new Intent(ImgTxtAlbumActivity.this, MyHomePageActivity.class);
//                    intent.putExtra("id", contentDetailBean.getUserid());
//                    intent.putExtra("name", contentDetailBean.getUsername());
//                    startActivity(intent);
//                    if (!TextUtils.equals(getIntent().getStringExtra("authorId"), SpUtils.getStringSp(ImgTxtAlbumActivity.this, "userId"))) {
//                        Intent intent = new Intent(ImgTxtAlbumActivity.this, HisDataActivity.class);
//                        intent.putExtra("id", getIntent().getStringExtra("authorId"));
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(ImgTxtAlbumActivity.this, PersonalDataActivity.class);
//                        startActivity(intent);
//                    }
                }
            });
        }
    }


    private class JsPhotoEnlargeJump {

        public JsPhotoEnlargeJump() {
        }

        @JavascriptInterface
        public void startPhotoActivity(String imageUrl, String[] imageUrls) {
            Intent intent = new Intent(ImgTxtAlbumActivity.this, PhotoEnlargeActivity.class);
            intent.putExtra("listulrs", imageUrls);
            intent.putExtra("image_url", imageUrl);
            startActivity(intent);
        }
    }
}
