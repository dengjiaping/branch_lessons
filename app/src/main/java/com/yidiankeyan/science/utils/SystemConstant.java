package com.yidiankeyan.science.utils;

/**
 * Created by nby on 2016/7/21.
 * 系统常量
 */
public class SystemConstant {

    public static final String APP_IS_FIRST_START = "app_is_first_start";
    public static final String CUSTOM_AVATAR_URL = "custom_avatar_url";
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static final String endpoint = "http://oss-cn-qingdao.aliyuncs.com";
    public static final String accessKeyId = "I0oayYSxEGqVlFTL";
    public static final String accessKeySecret = "T6qcCExJ16m0N61S5yv04gnXmetBT7";
    public static final String testBucket = "poinetech-attach";
    public static final int SDK_APPID = 1400016834;
    public static final int ACCOUNT_TYPE = 13459;

    /**
     * 首页音频播放器入口
     */
    public static final int INFO_AUDIO_PLAYER = 1;
    /**
     * adapter点击分享通知activity
     */
    public static final int NEW_FLASH_SHARE_CLICK = 2;
    /**
     * 视频专辑，视频播放状态发生改变
     */
    public static final int VIDEO_ALBUM_PLAY_CHANGED = 3;
    /**
     * 注册成功，销毁登录页面
     */
    public static final int NOTIFY_LOGIN_FINISH = 4;
    /**
     * 定制了专题
     */
    public static final int CUSTOM_PROJECT = 5;
    /**
     * 音频开始播放
     */
    public static final int ON_PLAYING = 6;
    /**
     * 音频停止播放
     */
    public static final int ON_STOP = 7;
    /**
     * 音频准备中
     */
    public static final int ON_PREPARE = 8;
    /**
     * 音频缓冲中
     */
    public static final int ON_BUFFERING = 9;
    /**
     * 音频暂停
     */
    public static final int ON_PAUSE = 10;
    /**
     * 文件下载完成
     */
    public static final int DOWNLOAD_FINISH = 11;
    /**
     * 播放完成
     */
    public static final int AUDIO_COMPLET = 12;
    /**
     * 进入全屏
     */
    public static final int QUIT_FULLSCREEN = 13;
    /**
     * 我的调用相机
     */
    public static final int PHOTO_GRAPH = 14;
    /**
     * 编辑昵称
     */
    public static final int USER_UPDATE_NAME = 14;
    /**
     * 我的头像选择后通知fragment设置
     */
    public static final int LOAD_AVATAR = 15;
    /**
     * 添加点赞通知
     */
    public static final int ADD_ZAN = 16;
    /**
     * 移除点赞的通知
     */
    public static final int REMOVE_ZAN = 17;
    /**
     * 图文详情评论展开
     */
    public static final int COMMENT_EXPAND = 18;
    /**
     * 获取当前播放列表
     */
    public static final int GET_CUTTENT_PLAY_LIST = 19;
    /**
     * 科答关注
     */
    public static final int ON_FOCUS_CHANGED = 20;
    /**
     * 点赞变化
     */
    public static final int ON_CLICK_CHANGE = 21;
    /**
     * 返回用户信息
     */
    public static final int ON_USER_INFORMATION = 22;
    /**
     * 用户注册成功
     */
    public static final int ON_USER_REGISTER_BACK = 23;
    /**
     * 微信支付结果
     */
    public static final int ON_WEIXIN_PAY_FINISH = 24;
    /**
     * fragment通知activity弹出支付
     */
    public static final int NOTIFY_ACTIVITY_SHOW_PAY = 25;

    /**
     * 置顶
     */
    public static final int ON_CLICK_TOP = 26;
    /**
     * 置顶
     */
    public static final int ON_CLICK_BOTTOM = 27;

    /**
     * 答人资料修改
     */
    public static final int NOTIFY_INFORMATION_CHANGED = 26;
    /**
     * 个人资料修改
     */
    public static final int NOTIFY_USER_INFORMATION_CHANGED = 27;
    /**
     * 答人资料保存
     */
    public static final int NOTYFY_ANSWER_INFO_SAVE = 28;
    /**
     * 偷听完成
     */
    public static final int EAVEDROP_COMPLITE = 29;
    /**
     * 用户退出
     */
    public static final int ON_USER_LOGOUT = 30;
    /**
     * 音视频内容订阅
     */
    public static final int ON_ORDER_ALBUM = 31;
    /**
     * 音视频内容取消订阅
     */
    public static final int ON_ABOLISH_ORDER_ALBUM = 32;
    /**
     * 在图文web页改变了关注状态，然后将新的值传给DetauksFragment
     */
    public static final int DETAUKS_FRAGMENT_RECEIVER_FOCUS = 33;
    /**
     * 个人简介信息
     */
    public static final int ON_BRIEF_INTRODUCTION = 34;
    /**
     * 点藏更多图标显示或隐藏
     */
    public static final int NOTIFY_MAIN_COLLECT_SHOW_OR_HIDE = 35;
    /**
     * mainactivity显示或隐藏取消
     */
    public static final int NOTIFY_MAIN_CANCEL_SHOW_OR_HIDE = 36;
    /**
     * apk下载进度
     */
    public static final int DOWNLOAD_APK = 37;
    /**
     * 墨子读书 -购买
     */
    public static final int MOZ_READ_PURCHASE = 38;
    /**
     * 注册成功 -返回资讯
     */
    public static final int MOZ_REGISTER_RETURN = 39;
    /**
     * 墨子读书 -全部 -购买
     */
    public static final int MOZ_READS_PURCHASE = 40;
    /**
     * 注册成功 -返回资讯 -定位到热点
     */
    public static final int MOZ_REGISTER_HOT = 41;
    /**
     * 知识红包 -卡片弹出框取消
     */
    public static final int KNOWLEDGE_CARD_CLOSE = 42;
    /**
     * 知识红包 -卡片显示多层
     */
    public static final int KNOWLEDGE_CARD_VISIBLE = 43;
    /**
     * 评论 -删除
     */
    public static final int MOZ_COMMENT_DELETE = 44;
    /**
     * 知识红包 -卡片移动隐藏多层
     */
    public static final int KNOWLEDGE_CARD_GOVE = 45;
    /**
     * 环信接受到消息
     */
    public static final int ON_MESSAGE_RECEIVED = 46;
    /**
     * 环信联系人变动
     */
    public static final int ACTION_CONTACT_CHANAGED = 47;
    /**
     * 环信删除好友
     */
    public static final int ON_DELETE_CONTACT = 48;
    /**
     * 科答个人简介信息
     */
    public static final int ON_KEDA_INTRODUCTION = 49;
    /**
     * 环信消息撤回
     */
    public static final int ON_MESSAGE_REVOKE = 50;
    /**
     * 科答提问回答状态
     */
    public static final int ON_KEDA_NOTICE = 51;
    /**
     * 群资料发生改变
     */
    public static final int ON_GROUP_MODIFY_INFORMATION = 52;
    /**
     * 群删除
     */
    public static final int ON_GROUP_DELETE = 53;
    /**
     * 环信群组变动
     */
    public static final int ACTION_GROUP_CHANAGED = 54;
    /**
     * 群组成员移除
     */
    public static final int ON_GROUP_MEMBER_REMOVE = 55;
    /**
     * 视频分享收藏
     */
    public static final int ON_INFOR_VIDEO = 56;
    /**
     * 日课状态刷新
     */
    public static final int ON_TODAY_FLASH = 57;
    /**
     * 日课打卡状态
     */
    public static final int ON_TODAY_STATE = 58;
    /**
     * 充值状态
     */
    public static final int ON_BALANCE_STATE = 59;
    /**
     * 删除返回状态
     */
    public static final int ON_DOWNLOAD_STATE = 60;
    /**
     * 成员角色变动，成为管理员或取消管理员
     */
    public static final int ON_GROUP_MEMBER_OPERATE_CHANGED = 61;
    /**
     * 专访评论弹出框
     */
    public static final int ON_INTERVIEW_COMMENT = 62;
    /**
     * 跳转专访评论列表
     */
    public static final int ON_INTERVIEW_COMMENT_LIST = 63;
    /**
     * 刷新专访评论列表
     */
    public static final int ON_INTERVIEW_COMMENT_LISTS = 64;
    /**
     * 刷新专访评论列表
     */
    public static final int ON_MAGAZINE_LOGINE_NOTIFY = 65;
    public static final int MAINACTIVITY_AUDIO_REFRESH = 66;
    /**
     * 拒绝接听
     */
    public static final int ON_REJECT = 67;
    /**
     * 挂断
     */
    public static final int ON_HANGUP = 68;
    /**
     * 音频暂停返回主界面隐藏播放条
     */
    public static final int ON_HIDE_PLAY = 69;
    /**
     * 专栏购买刷新列表
     */
    public static final int ON_COLUMN_FLASH = 70;
    public static final int ON_HIDE_PLAYBAR = 71;
    public static final int ON_PURCHASE_MY = 72;
    //    /**
//     * 专辑详情点击关注
//     */
//    public static final int ALBUM_FOLLOW = 12;
//    /**
//     * 专辑详情关注弹出框取消
//     */
//    public static final int ALBUM_NO_POP = 12;
//    /**
//     * 专辑详情关注弹出框确定
//     */
//    public static final int ALBUM_YES_FOLLOW = 12;
    //域名
//    public static final String URL = "http://mozapp.poinetech.com/";
    public static final String URL = "http://192.168.1.197:8081/";
    //cmsweb
//    public static final String MYURL = "http://moz.poinetech.com/";
    public static final String MYURL = "http://cmsweb.poinetech.com/";    //内网测试环境普通接口
    //用户相关
//    public static final String USER_URL = "http://poinetechuser.poinetech.com/";
    public static final String USER_URL = "http://192.168.1.77:8052/";
    /**
     * 通过id查询轮播图
     */
    public static final String QUERY_BANNER_BY_IDS = "size/banner/querybyids";
    /**
     * 查询轮播图
     */
    public static final String QUERY_BANNER = "size/banner/query";
    /**
     * 通过id查询文章
     */
    public static final String QUERY_ARICTLE_BY_IDS = "size/article/querybyids";
    /**
     * 获取总编推荐
     */
    public static final String QUERY_RECOMMEND_ALBUM = "size/recommend/queryalbum";
    /**
     * 猜你喜欢
     */
    public static final String QUERY_GUESS_ALBUM = "size/order/guess/detail";
    /**
     * 访问图片
     */
//    public static final String ACCESS_IMG_HOST = "http://58.135.93.115/cmsweb";
    public static final String ACCESS_IMG_HOST = "http://static.poinetech.com/cms";
    public static final String ALI_CLOUD = "http://static.poinetech.com/cms";
    /**
     * 获取专题
     */
    public static final String QUERY_PROJECT = "size/subject/queryall";
    /**
     * 获取推荐下的专题列表
     */
    public static final String QUERY_RECOMMEND_PROJECT_ALL = "size/display/all";
    /**
     * 获取科答偷听榜
     */
    public static final String QUERY_EAVESDROPTOP = "size/keda/keder/mostwisper";
    /**
     * 获取科答最新问答
     */
    public static final String QUERY_RECENT_ANSWER = "size/keda/keder/recentkeders";
    /**
     * 获取科答所有问答专辑
     */
    public static final String QUERY_ALL_ANSWER_ALBUM = "size/keda/album/queryall";
    /**
     * 查询所有专辑列表
     */
    public static final String QUERY_ALL_ALBUM_LIST = "size/album/conditionsquery";
    /**
     * 分类专辑
     */
    public static final String GET_CLASS_ALBUM_LIST = "size/order/classify/conditions";
    /**
     * 获取固定板块下最新内容
     */
    public static final String QUERY_ALL_RECENT_CONTENT = "size/article/recentarticles";
    /**
     * 获取分类下图文专辑
     * -已订专辑
     */
    public static final String QUERY_ALL_BOOKED_ALBUM = "size/order/queryorderalbum";
    /**
     * 查询专辑详情
     */
    public static final String QUERY_ALBUM_DETAIL = "size/album/one";
    /**
     * 查询专辑详情里面的内容
     */
    public static final String QUERY_ALBUM_CONTENT = "size/article/simples";
    /**
     * 查询科答答人榜
     */
    public static final String QUERY_ANSWER_TOP = "size/keda/user/all";
    /**
     * 查询快讯
     */
    public static final String QUERY_NEWS = "size/flashnews/multi";
    /**
     * 获取排行榜首页的专辑标题
     */
    public static final String QUERY_RANKING_ALBUM_TITLE = "size/top/indextitles";
    /**
     * 获取专辑排行榜
     */
    public static final String QUERY_RANKING_ALBUM_LIST = "size/top/albumlist";
    /**
     * 主编排行榜
     */
    public static final String QUERY_AUTHOR_TOP = "size/top/authorlist";
    /**
     * 提交用户定制的专题
     */
    public static final String SUBMIT_USER_SUBJECT = "size/subject/ordersubject";
    /**
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST = "size/comment/list";
    /**
     * 获取内容下面的评论
     */
    public static final String GET_MIX_COMMENT_LIST = "size/comment/mix";
    /**
     * 提交评论
     */
    public static final String SEND_COMMENT = "size/comment/insert";
    /**
     * 获取内容详情
     */
    public static final String GET_CONTENT_DETAIL = "size/article/queryone";
    /**
     * 点赞
     */
    public static final String TO_HTTP_CLICK = "size/comment/up";
    /**
     * 获取点藏专辑列表
     */
    public static final String QUERY_COLLECTION_ALBUM_LIST = "size/mycollect/albumlist";
    /**
     * 科答偷听
     */
    public static final String GET_KEDA_EAVEDROP_MEDIA = "size/keda/eavedrop";
    /**
     * 问答专辑下面的内容
     */
    public static final String GET_KEDA_CONDITIONS = "size/keda/keder/conditions";
    /**
     * 获取科答-我问下关注答人列表
     */
    public static final String QUERY_ALL_ATTENTION_ANSWER = "size/keda/user/focus";
    /**
     * 获取科答-我问下认证答人列表
     */
    public static final String QUERY_ALL_AUTHENTICATE_ANSWER = "size/keda/user/authenticated";
    /**
     * 获取科答-我问下新晋答人列表
     */
    public static final String QUERY_ALL_NEW_ANSWER = "size/keda/user/recent";
    /**
     * 获取科答-我问下所有答人列表
     */
    public static final String QUERY_ALL_ALL_ANSWER = "size/keda/user/allkedauser";
    /**
     * 杂志列表
     */
    public static final String GET_MAGAZINE = "size/affiche/magazine/list";
    /**
     * 公众号
     */
    public static final String GET_WECHAT = "size/affiche/wechat/list";
    /**
     * 网站
     */
    public static final String GET_WEBSITE = "size/affiche/website/list";
    /**
     * 关于我们
     */
    public static final String GET_ABOUT_US = "size/affiche/us/list";
    /**
     * 公告，讲座信息和科技活动信息
     */
    public static final String GET_NOTIFY_ACTIVITY = "size/affiche/activity/query";
    /**
     * 订阅专辑
     */
    public static final String ORDER_ALBUM = "size/order/insert";
    /**
     * 查询某专辑订阅数
     */
    public static final String QUERY_ALBUM_SUBNUMBER = "size/order/ordernum";
    /**
     * 取消订阅
     */
    public static final String ORDER_ABOLISH_ALBUM = "size/order/abolish";
    /**
     * 搜索接口
     */
    public static final String POST_SEARCH_SEARCH = "size/search/search";
    /**
     * 获取科答我答下待回答接口
     */
    public static final String GET_WAIT_ANSWER = "size/keda/quest/questme";
    /**
     * 关注与取消关注
     */
    public static final String AUTHOR_FOUCE = "size/order/focus";
    /**
     * 日课
     */
    public static final String GET_ONE_DAY_ARTICLES = "size/daily/getOneDayArticles";
    /**
     * 答人详情
     */
    public static final String GET_ANSWER_PEOPLE_DETAIL = "size/keda/user/one";
    /**
     * 用户注册发送验证码
     */
    public static final String GET_MY_SET_UP = "u/npcode/86/";
    /**
     * 用户注册发送验证码-----new
     */
    public static final String GET_MY_SET_UP_NEW = "u/mscode";
    /**
     * 提问
     */
    public static final String SUBMIT_ANSWER = "size/keda/quest/make";
    /**
     * 打赏
     */
    public static final String BESTOW = "size/cms/tip";
    /**
     * 获取打赏界面的详情
     */
    public static final String GET_GRATUITY_DETAIL = "size/cms/user";

    /**
     * 答人资料修改
     */
    public static final String KEDA_USER_INFORMATION_CHANGED = "size/keda/user/update";


    /**
     * 专辑详情
     * -相关推荐
     */
    public static final String POST_RELEVANT_RECOMMEND = "size/order/related";

    /**
     * 订阅置顶
     */
    public static final String POST_CLICK_TOP = "size/order/pushtop";

    /**
     * 订阅取消置顶
     */
    public static final String POST_CLICK_CANCEL = "size/order/abolishtop";
    /**
     * 更新用户信息
     */
    public static final String UPDATA_USER_INFO = "size/mine/update";
    /**
     * 举报
     */
    public static final String SUBMIT_REPORT = "size/report/insert";
    /**
     * 取消举报
     */
    public static final String DELETE_REPORT = "size/report/delete";
    /**
     * 注册成为科答用户
     */
    public static final String INSERT_KEDA_USER = "size/keda/user/insert";
    /**
     * 是否举报过
     */
    public static final String QUERY_REPORTED = "size/report/done";
    /**
     * 取消举报
     */
    public static final String REMOVE_REPORT = "size/report/delete";
    /**
     * 上传资料，身份认证
     */
    public static final String ANSWER_AUTHENTICATION = "size/keda/user/auth";
    /**
     * 查询用户信息
     */
    public static final String QUERY_USER_INFO = "size/target/one";
    /**
     * 回答
     */
    public static final String KEDA_ANSWER = "size/keda/keder/answer";

    /**
     * 意见反馈
     */
    public static final String MY_OPINION_FEEDBACK = "size/suggestion/insert";

    /**
     * 我的关注
     */
    public static final String MY_FOCUS_LIST = "size/order/focusUser";
    /**
     * 他的关注
     */
    public static final String HI_FOCUS_LIST = "size/order/u/focusUser";
    /**
     * 我的粉丝
     */
    public static final String MY_FANS_LIST = "size/order/followes";
    /**
     * 他的粉丝
     */
    public static final String HI_FANS_LIST = "size/order/u/followes";
    /**
     * 打赏记录
     */
    public static final String GRATUITY_HISTORY = "size/cms/tippers";

    /**
     * 获取积分
     */
    public static final String GET_INTEGRATE = "size/points/history/insert";
    /**
     * 查询积分
     */
    public static final String QUERY_INTEGRATE = "size/points/one";
    /**
     * 收益中心
     */
    public static final String PROFIT_CORE_SUM = "size/profit/record/total";

    /**
     * 收益记录
     */
    public static final String PROFIT_LIST = "size/profit/record/list";


    /**
     * 收益中心
     * -提现申请
     */
    public static final String WITHDRAWALS_APPLY = "size/withdraw/apply/insert";

    /**
     * 阅读快讯
     */
    public static final String READ_NEWS = "size/flashnews/read";

    /**
     * 阅读日课
     */
    public static final String READ_TODAY_AUDIO = "size/daily/addUserDailyHistory";
    /**
     * 收藏列表
     */
    public static final String GET_COLLECTION_LIST = "size/mycollect/articlelist";
    /**
     * 判断是否收藏
     */
    public static final String ARTICLE_IS_COLLECTED = "size/mycollect/iscollected";
    /**
     * 收藏
     */
    public static final String COLLECT_ARTICLE = "size/mycollect/add";
    /**
     * 取消收藏
     */
    public static final String UNCOLLECT_ARTICLE = "size/mycollect/del";

    /**
     * 批量取消收藏
     */
    public static final String BATCHDER_UNCOLLECT_ARTICLE = "size/mycollect/batchdel";
    /**
     * 日课点赞
     */
    public static final String TODAY_CLICK = "size/daily/praise";
    /**
     * 分享后增加分享量
     */
    public static final String ADD_SHARE_NUMBER = "size/comment/forward";

    /**
     * 支付宝充值余额
     */
    public static final String BALANCE_RECHARGE_FOR_ALIPAY = "size/transactions/alipay";
    /**
     * 微信充值余额
     */
    public static final String BALANCE_RECHARGE_FOR_WX = "size/transactions/wechatPay";
    /**
     * 查询余额
     */
    public static final String QUERY_BALANCE = "size/balance/one";
    /**
     * 获取最新apk信息
     */
    public static final String GET_THE_LAST_VERSION = "size/version/android";
    /**
     * 首页快报信息
     */
    public static final String MOZ_ACTIVITY_BANNER = "size/flashreport/slidelist";
    /**
     * 子活动列表
     */
    public static final String ACTIVITY_ITEM_LIST = "size/flashreport/itemlist";
    /**
     * 获取投票信息
     */
    public static final String GET_VOTE_INFO = "size/votes/voteactlist";
    /**
     * 投票
     */
    public static final String MOZ_ACTIVITY_VOTE = "size/votes/vote";
    /**
     * 获取中奖人列表
     */
    public static final String GET_WINNER_LIST = "size/lottery/luckylist";
    /**
     * 获取我的奖品列表
     */
    public static final String GET_MY_PRIZZE_LIST = "size/lottery/myluckylist";
    /**
     * 领奖
     */
    public static final String RECEIVE_PRIZE = "size/lottery/lucky/addinfo";
    /**
     * 获取奖品列表
     */
    public static final String GET_PRIZE_LIST = "size/lottery/prize/getlist";
    /**
     * 抽奖
     */
    public static final String LOTTERY = "size/lottery/draw";
    /**
     * 获取可抽奖次数
     */
    public static final String GET_LOTTERY_COUNT = "size/lottery/getLeftDrawNum";
    /**
     * 验证码申请
     */
    public static final String POST_PASSWORD_APPLY = "size/mine/applycode/";
    /**
     * 密码重置
     */
    public static final String POST_PASSWORD_RESET = "size/mine/repswd";
    /**
     * 队伍详情
     */
    public static final String GET_TEAM_DETAIL = "size/votes/enrolinfo";
    /**
     * 获取热点新闻
     */
    public static final String GET_HOT_NEWS = "size/article/hotspot";
    /**
     * 记录push信息
     */
    public static final String SUBMIT_PUSH_ID = "size/user/pushInfo";
    /**
     * 推荐
     * -订阅专栏列表
     */
    public static final String QUERY_RECOMMEND_SUB_COLUMN = "size/columns/list";
    /**
     * 推荐
     * -墨子读书列表
     */
    public static final String QUERY_RECOMMEND_MOZ_READ = "size/books/unlist";
    /**
     * 推荐
     * -墨子读书列表
     * -单条详情
     */
    public static final String QUERY_READ_DETAILS = "size/books/info";
    /**
     * 推荐
     * -墨子读书列表
     * -单条详情
     * -相关书籍
     */
    public static final String QUERY_RELEVANT_READ = "size/books/others";
    /**
     * 获取专栏详情
     */
    public static final String GET_COLUMNS_DETAIL = "size/columns/one";
    /**
     * 获取专栏期刊列表
     */
    public static final String GET_COLUMNS_ISSUES_LIST = "size/columns/issues";
    /**
     * 试看列表
     */
    public static final String GET_COLUMNS_FREE_ISSUES_LIST = "size/issues/free";
    /**
     * 查询用户与用户之间关注关系
     */
    public static final String QUERY_USER_ISFOCUS = "size/order/u/focus/relation";
    /**
     * 墨子读书
     * -书籍购买
     */
    public static final String QUERY_BOOKS_BUYS = "size/books/buys";
    /**
     * 收费专辑
     * -购买
     */
    public static final String QUERY_ALBUM_BUYS = "size/album/buy";
    /**
     * 专栏购买
     */
    public static final String BUY_COLUMN = "size/column/pay";
    /**
     * 期刊详情
     */
    public static final String ISSUES_DETAIL = "size/issues/one";
    /**
     * 最新专栏
     */
    public static final String GET_RECENT_COLUMN = "size/issues/recent";
    /**
     * 购买书籍
     */
    public static final String GET_PURCHASE_BOOK = "size/books/alreadylist";
    /**
     * 已购专栏最近内容
     */
    public static final String GET_COLUMNS_RECENT = "size/columns/recent";
    /**
     * 已购的专栏
     */
    public static final String GET_PURCHASE_COLUMN = "size/columns/purchase";
    /**
     * 付费榜
     */
    public static final String GET_PAY_TOP = "size/top/paidlist";
    /**
     * 获取用户定制专题
     */
    public static final String GET_ORDER_PROJECT = "size/subject/userordersubList";

    /**
     * 增加阅读数
     */
    public static final String INSERT_READ_NUMBER = "size/books/readplus";

    /**
     * 上传文件
     */
    public static final String UPLOAD_FILE = "cmsweb/upload/upload";

    /**
     * 知识红包
     */
    public static final String GET_RED_KNOWLEDGE = "size/infobag/list";
    /**
     * 专栏分享
     * -获取海报地址
     */
    public static final String POST_COLUMN_POSTER = "column4/poster/";
    /**
     * 杂志购买
     */
    public static final String POST_BUY_MAGAZINE = "size/monthly/buy";
    /**
     * 用户行为采集--日课
     */
    public static final String USER_BEHAVIOR_ACQUISITION_DAYLY = "size/click/DAILY";
    /**
     * 用户行为采集--快讯
     */
    public static final String USER_BEHAVIOR_ACQUISITION_MESSAGE = "size/click/FLASHNEWS";
    /**
     * 获取热点列表
     */
    public static final String GET_HOT_LIST = "size/comment/hot";

}
