package com.yidiankeyan.science.utils.net;


import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.app.entity.UpdataBean;
import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.information.entity.AnswerAlbumDetailUserBean;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.BookJudgeBuysBean;
import com.yidiankeyan.science.information.entity.ColumnAllListBean;
import com.yidiankeyan.science.information.entity.ColumnArticleDetailsBean;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.ColumnDetailsBean;
import com.yidiankeyan.science.information.entity.ColumnQueryArticleBean;
import com.yidiankeyan.science.information.entity.FeedbackListBean;
import com.yidiankeyan.science.information.entity.FlashBean;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.information.entity.InterviewCommentListBean;
import com.yidiankeyan.science.information.entity.InterviewIntroduceBean;
import com.yidiankeyan.science.information.entity.InterviewSoonBean;
import com.yidiankeyan.science.information.entity.IsshareonBean;
import com.yidiankeyan.science.information.entity.KedaNoticeBean;
import com.yidiankeyan.science.information.entity.MagazineDetailsBean;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.information.entity.MonthlySeriesBean;
import com.yidiankeyan.science.information.entity.MozActivityBean;
import com.yidiankeyan.science.information.entity.MozAudioBean;
import com.yidiankeyan.science.information.entity.MozForumBean;
import com.yidiankeyan.science.information.entity.MozMagazineAllBean;
import com.yidiankeyan.science.information.entity.MozMagazinePurchaseBean;
import com.yidiankeyan.science.information.entity.MozReadBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.information.entity.NewRecommendBean;
import com.yidiankeyan.science.information.entity.NewScienceHelp;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.information.entity.RecMagazineList;
import com.yidiankeyan.science.information.entity.RecentContentBean;
import com.yidiankeyan.science.information.entity.RecommendFollowBean;
import com.yidiankeyan.science.information.entity.SubscribeColumnBean;
import com.yidiankeyan.science.knowledge.entity.TagBean;
import com.yidiankeyan.science.my.entity.AccountDetailedBean;
import com.yidiankeyan.science.my.entity.BalanceBean;
import com.yidiankeyan.science.my.entity.BlanaceEarnBean;
import com.yidiankeyan.science.my.entity.EditorAlbum;
import com.yidiankeyan.science.my.entity.FansFollow;
import com.yidiankeyan.science.my.entity.FocusBean;
import com.yidiankeyan.science.my.entity.InKRecordBean;
import com.yidiankeyan.science.my.entity.InkBalanceBean;
import com.yidiankeyan.science.my.entity.MyAccountArticleBean;
import com.yidiankeyan.science.my.entity.MyCollectInterviewBean;
import com.yidiankeyan.science.my.entity.MyHomeStatisticsBean;
import com.yidiankeyan.science.my.entity.PhoneBindingBean;
import com.yidiankeyan.science.my.entity.ProfitDetailedBean;
import com.yidiankeyan.science.my.entity.PurchaseBookBean;
import com.yidiankeyan.science.my.entity.UpdateProfitBalance;
import com.yidiankeyan.science.my.entity.UserCardBean;
import com.yidiankeyan.science.my.entity.VideoArticleFollowBean;
import com.yidiankeyan.science.my.entity.WitthdrawalsContentBean;
import com.yidiankeyan.science.purchase.entity.ColumnPurchaseBean;
import com.yidiankeyan.science.subscribe.entity.AlbumDetail;
import com.yidiankeyan.science.subscribe.entity.ContentCommentBean;
import com.yidiankeyan.science.subscribe.entity.ContentDetailBean;
import com.yidiankeyan.science.subscribe.entity.RelatedArticleBean;
import com.yidiankeyan.science.subscribe.entity.RelevantRecommend;
import com.yidiankeyan.science.subscribe.entity.TxtClickBean;
import com.yidiankeyan.science.subscribe.entity.TxtRelevantTagBean;
import com.yidiankeyan.science.wx.WXPay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nby on 2016/12/23 0023.
 */

public interface ApiServer {
    /**
     * 获取反馈历史记录
     */
    @POST("size/suggestion/query")
    Call<RetrofitResult<List<FeedbackListBean>>> getFeedbackList(@Body Map map);

    /**
     * 验证邀请码是否可用、正确
     */
    @POST("size/invitationcode/binginvitationcode")
    Call<RetrofitResult<Object>> getInvitationCode(@Body Map map);

    /**
     * 获取推荐数据
     */
    @POST("size/click")
    Call<RetrofitResult<Object>> getBehaviorAcquisition(@Body Map map);

    /**
     * 获取推荐数据
     */
    @POST("size/display/all")
    Call<RetrofitResult<List<NewRecommendBean>>> getRecommend(@Body Map map);

    /**
     * 获取热点数据
     * -按推荐排序
     */
    @POST("size/article/hotspot")
    Call<RetrofitResult<List<HotNewsBean>>> getHotNews(@Body Map map);

    /**
     * 获取热点数据
     * -按时间排序
     */
    @POST("size/article/normal")
    Call<RetrofitResult<List<HotNewsBean>>> getHotNewsTime(@Body Map map);

    /**
     * 首页moz读书
     */
    @POST("size/books/unlist")
    Call<RetrofitResult<MozReadBean>> getRecommendMozRead(@Body Map map);

    /**
     * 首页moz读书   liuchao  add new
     */
    @POST("size/books/bookslist")
    Call<RetrofitResult<MozReadBean>> getMozReadList(@Body Map map);

    /**
     * 获取轮播图
     */
    @POST("size/banner/query")
    Call<RetrofitResult<List<BannerBean>>> getBanner(@Body Map map);

    /**
     * 获取首页快报信息
     */
    @POST("size/flashreport/slidelist")
    Call<RetrofitResult<MozActivityBean>> getMozActivityBanner(@Body Map map);

    /**
     * 检查更新
     */
    @POST("size/version/android")
    Call<RetrofitResult<UpdataBean>> checkUpdate(@Body Map map);

    /**
     * 获取moz读书详情
     */
    @POST("size/books/info")
    Call<RetrofitResult<MozReadDetailsBean>> getMozBookDetail(@Body Map map);

    /**
     * 获取推荐专辑
     */
    @POST("size/recommend/query")
    Call<RetrofitResult<NewRecommendBean>> getRecommendAlbum(@Body Map map);

    /**
     * 删除评论
     */
    @POST("size/comment/delete")
    Call<RetrofitResult<Object>> deleteComment(@Body Map map);

    /**
     * 获取专辑详情
     */
    @POST("size/album/one")
    Call<RetrofitResult<AlbumDetail>> getAlbummDetail(@Body Map map);

    /**
     * 获取专辑相关推荐
     */
    @POST("size/order/related")
    Call<RetrofitResult<List<RelevantRecommend>>> getRelevantAlbum(@Body Map map);

    /**
     * 获取文章详情
     */
    @POST("size/article/queryone")
    Call<RetrofitResult<ContentDetailBean>> getArticleDetail(@Body Map map);

    /**
     * 获取科答专辑
     */
    @POST("size/keda/album/queryall")
    Call<RetrofitResult<List<AnswerAlbumBean>>> getAnswerAlbum(@Body Map map);

    /**
     * 支付宝提问
     */
    @POST("size/keda/quest/quest_v2")
    Call<RetrofitResult<AliPay>> questionForAliPay(@Body Map map);

    /**
     * 微信提问
     */
    @POST("size/keda/quest/quest_v2")
    Call<RetrofitResult<WXPay>> questionForWxPay(@Body Map map);

    /**
     * 余额支付
     */
    @POST("size/keda/quest/quest_v2")
    Call<RetrofitResult<Object>> questionForBalancePay(@Body Map map);

    /**
     * 科答详情列表
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "id":"aaaassss",  //科答id
     * "kedaalbumid":"2",//科答专辑id
     * "makerid":"-0012",//提问者id
     * "solverid":"-0012",//回答者id
     * "isanswer":"1"//是否已回答
     * }
     * }
     */
    @POST("size/keda/quest/condition")
    Call<RetrofitResult<List<NewScienceHelp>>> getKeDaDetailList(@Body Map map);

    /**
     * 科答追问
     *
     * @param map {
     *            "kederid":"226000a4299043c79586d7aea25997e2", //科答id
     *            "question":"zheshi这是yige" //问题
     *            }
     */
    @POST("size/keda/quest/request")
    Call<RetrofitResult<Object>> submitRequestQuestion(@Body Map map);

    /**
     * 科答追答
     * {
     * "kederid":"226000a4299043c79586d7aea25997e2", //科答id
     * "answerurl":"aasd.mp3" //文件路径
     * "taketime":"28" //回答时长
     * }
     */
    @POST("size/keda/quest/reanswer")
    Call<RetrofitResult<Object>> submitReplyAnswer(@Body Map map);

    /**
     * 更新答人信息
     * {
     * "mouthprice":0.01,
     * "messageurl":"/2.mp3",
     * "domain":"1,4,12",
     * "name":"大和哥",
     * "sign":"u r the fancy gay to me",
     * "messagetaketime":"23",
     * "coverimg":"xxx.jpg",
     * "profession":"计算机博士"
     * }
     */
    @POST("size/keda/user/update")
    Call<RetrofitResult<Object>> updateKedaUserInfo(@Body Map map);

    /**
     * 追问列表
     * {"pages":1,"pagesize":20}
     */
    @POST("size/keda/quest/requestme")
    Call<RetrofitResult<List<NewScienceHelp>>> getReplyQuestion(@Body Map map);

    /**
     * 偷听
     * {
     * "kederDB":{
     * "id":"00950bb5e4bd4bba99619056b689ffbd"
     * },
     * "payHistory":{
     * "pay_type":2 //1：微信 2：支付宝 4：余额支付
     * }
     * }
     */
    @POST("size/keda/eavedrop_v2")
    Call<RetrofitResult<AliPay>> kedaEavedropAliPay(@Body Map map);

    /**
     * 偷听
     * {
     * "kederDB":{
     * "id":"00950bb5e4bd4bba99619056b689ffbd"
     * },
     * "payHistory":{
     * "pay_type":2 //1：微信 2：支付宝 4：余额支付
     * }
     * }
     */
    @POST("size/keda/eavedrop_v2")
    Call<RetrofitResult<Object>> kedaEavedropBalancePay(@Body Map map);

    /**
     * 偷听
     * {
     * "kederDB":{
     * "id":"00950bb5e4bd4bba99619056b689ffbd"
     * },
     * "payHistory":{
     * "pay_type":2 //1：微信 2：支付宝 4：余额支付
     * }
     * }
     */
    @POST("size/keda/eavedrop_v2")
    Call<RetrofitResult<WXPay>> kedaEavedropWxPay(@Body Map map);

    /**
     * 订阅更新总数
     */
    @POST("size/order/updates")
    Call<RetrofitResult<Integer>> getUpdateNum();

    /**
     * 记录用户使用哪个渠道包注册
     */
    @POST("size/app/statistics/addcount")
    Call<RetrofitResult<Object>> postRegFromChannel(@Body Map map);

    /**
     * 科答问答专辑的答人列表
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "id":"1" //问答专辑id
     * }
     * }
     */
    @POST("size/keda/user/albummark")
    Call<RetrofitResult<List<AnswerAlbumDetailUserBean>>> getKedaAnswerAlbumUserList(@Body Map map);


    /**
     * 账号绑定
     * -获取验证码
     *
     * @param map
     * @return
     */
    @POST("size/mozsms/sendsms")
    Call<RetrofitResult<PhoneBindingBean>> getBinddingCode(@Body Map map);

    /**
     * 我的关注
     *
     * @return
     */
    @POST("size/order/focusUser")
    Call<RetrofitResult<List<FansFollow>>> getMyFollow(@Body Map map);

    /**
     * 我的粉丝
     *
     * @return
     */
    @POST("size/order/followes")
    Call<RetrofitResult<List<FansFollow>>> getMyFans(@Body Map map);

    /**
     * 他的关注
     *
     * @return
     */
    @POST("size/order/u/focusUser")
    Call<RetrofitResult<List<FansFollow>>> getHeFollow(@Body Map map);

    /**
     * 他的粉丝
     *
     * @return
     */
    @POST("size/order/u/followes")
    Call<RetrofitResult<List<FansFollow>>> getHeFans(@Body Map map);

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("size/mine/card")
    Call<RetrofitResult<UserCardBean>> getUserInformation(@Body Map map);

    /**
     * 获取我的偷听接口
     * {
     * "pages":1,"pagesize":29
     * }
     */
    @POST("size/keda/keder/myKeder")
    Call<RetrofitResult<List<NewScienceHelp>>> getMyEavesdrop(@Body Map map);

    /**
     * 查询读书是否购买
     *
     * @return
     */
    @POST("size/books/judgebuy")
    Call<RetrofitResult<BookJudgeBuysBean>> getBookJudgeBuy(@Body Map map);

    /**
     * 查询账户余额
     */
    @POST("size/balance/one")
    Call<RetrofitResult<BalanceBean>> getQueryBalance(@Body Map map);

    /**
     * 获取文章详情
     */
    @POST("size/keda/keder/notice")
    Call<RetrofitResult<KedaNoticeBean>> getKedaNotice(@Body Map map);

    /**
     * 获取推荐墨子FM
     */
    @POST("size/article/recentarticles")
    Call<RetrofitResult<List<RecentContentBean>>> getRecommendFm(@Body Map map);


    /**
     * 举报
     * {
     * "title":"语言反动",
     * "targetid":"stt23235sfa33",
     * "imgurl":"/23.jpg",
     * "report_type":"USER",                 举报类型  ARTICLE:文章,USER:用户,COMMENT:评论,ESCROW_GROUP:即时通讯群， ESCROW_USER:即时通讯用户， OTHER:其他
     * "content":"这个用户说话有毒"
     * }
     */
    @POST("size/report/insert")
    Call<RetrofitResult<Object>> report(@Body Map map);

    /**
     * 音频
     * 获取音频专辑
     */
    @POST("size/album/conditionsquery")
    Call<RetrofitResult<ArrayList<EditorAlbum>>> getEditorAlbum(@Body Map map);

    /**
     * 日课签到
     */
    @POST("size/daily/signon")
    Call<RetrofitResult<Integer>> todaySign();

    /**
     * 获取签到列表
     * {
     * "begintime":"2017-04-00",
     * "endtime":"2017-05-00"
     * }
     */
    @POST("size/daily/signonhistory")
    Call<RetrofitResult<List<Long>>> getDailySignList(@Body Map map);


    /**
     * 账号余额
     * -获取账号明细
     */
    @POST("size/account/myAD/{pageNum}/{pageSize}")
    Call<RetrofitResult<List<AccountDetailedBean>>> getAccountDetailed(@Path("pageNum") int pageNum, @Path("pageSize") int pageSize);

    /**
     * 我的主页
     * -获取用户统计
     */
    @POST("size/account/statistics")
    Call<RetrofitResult<MyHomeStatisticsBean>> getMyStatistics(@Body Map map);

    /**
     * 我的主页
     * -获取全部文章
     */
    @POST("size/account/article/{pageNum}/{pageSize}")
    Call<RetrofitResult<List<MyAccountArticleBean>>> getAccountArticle(@Path("pageNum") int pageNum, @Path("pageSize") int pageSize, @Body Map map);


    /**
     * 获取墨子杂志列表
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * checkstatus":2
     * }
     */
    @POST("size/monthly/list")
    Call<RetrofitResult<List<MozMagazineAllBean>>> GetMagazineAll(@Body Map map);

    /**
     * 获取墨子月刊详情
     * "id":
     */
    @POST("size/monthly/one")
    Call<RetrofitResult<MonthlyDetailsBean>> GetMonthlyDetails(@Body Map map);

    /**
     * 获取墨子杂志详情
     * "id":
     */
    @POST("size/magazine/one")
    Call<RetrofitResult<MagazineDetailsBean>> GetMagazineDetails(@Body Map map);

    /**
     * 获取墨子月刊列表
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "magazineid":"1f775b04cac84ef394eb50dd728703e4"
     * }
     */
    @POST("size/monthly/list")
    Call<RetrofitResult<List<MonthlySeriesBean>>> GetMonthlyAll(@Body Map map);


    /**
     * 购买月刊
     * {
     * "goodid":"25a83c4b85b9412e98c447ec72cf906e",
     * "payment":"4"
     * }
     */
    @POST("size/monthly/buy")
    Call<RetrofitResult<String>> GetMonthlyBuy(@Body Map map);

    /**
     * 墨子杂志已购列表
     * {
     * "pagesize":20,
     * "pages":1
     * }
     */
    @POST("size/monthly/purchase")
    Call<RetrofitResult<List<MozMagazinePurchaseBean>>> GetMagazinePurchase(@Body Map map);

    /**
     * 获取墨子杂志列表
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "magazineid":"1f775b04cac84ef394eb50dd728703e4"
     * }
     */
    @POST("size/magazine/list")
    Call<RetrofitResult<List<RecMagazineList>>> GetRecMagazineList(@Body Map map);

    /**
     * 获取专访列表
     * {
     * "pageNum":1,
     * "pageSize":20,
     * "interviewType":0即将上线  1往期专访
     * }
     */
    @POST("size/Interview/info")
    Call<RetrofitResult<ArrayList<InterviewSoonBean>>> getInterViewSoonList(@Body Map map);

    /**
     * 获取专访热门评论
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "targetid":"56da3137769e4084a40f1d74d774643a",
     * "sons":5
     * }
     * }
     */
    @POST("size/comment/hot")
    Call<RetrofitResult<ArrayList<ContentCommentBean>>> getCommentHotList(@Body Map map);

    /**
     * 获取专访最新评论
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "targetid":"f23d9fc118bb4907ac3b005c72d5243a",
     * "sons":1
     * }
     * }
     */
    @POST("size/comment/mix")
    Call<RetrofitResult<ArrayList<ContentCommentBean>>> getCommentNewestList(@Body Map map);

    /**
     * 专访单条详情
     * {
     * "id":"10"
     * }
     *
     * @param map
     * @return
     */
    @POST("size/Interview/queryInterview")
    Call<RetrofitResult<InterviewIntroduceBean>> getInterviewIntroduce(@Body Map map);

    /**
     * 获取专访评论列表
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "targetid":10
     * }
     * }
     */
    @POST("size/comment/list")
    Call<RetrofitResult<ArrayList<InterviewCommentListBean>>> getInterviewCommentList(@Body Map map);

    /**
     * 取消专访上线提醒
     */
    @POST("size/Interview/cancelOnlineRemind")
    Call<RetrofitResult<Boolean>> getOnlineRemind(@Body Map map);

    /**
     * 专访上线提醒
     */
    @POST("size/Interview/onlineRemind")
    Call<RetrofitResult<Boolean>> getQueryInterview(@Body Map map);

    /**
     * 获取专访最新评论
     * {
     * {"pageNum":1,"pagesize":20,"type":4}
     */
    @POST("size/Interview/myCollect")
    Call<RetrofitResult<ArrayList<MyCollectInterviewBean>>> getMyCollectList(@Body Map map);

    /**
     * 获取月刊详情 -节选列表
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "monthlyId":"21575e70e8c44e0684aefb8207531f30"
     * }}
     */
    @POST("size/monthly/excerpt")
    Call<RetrofitResult<ArrayList<MagazineExcerptBean>>> getExcerptList(@Body Map map);

    /**
     * 获取全部日课
     * {
     * "pages":1,
     * "pagesize":20,
     * "tagid":0标签id (查询全部请忽略entity或者tagid传0)
     * }
     */
    @POST("size/daily/getarticlepage")
    Call<RetrofitResult<List<OneDayArticles>>> getAllArticles(@Body Map map);

    /**
     * @return 日课标签列表
     */
    @POST("size/daily/tag/list")
    Call<RetrofitResult<List<OneDayArticles.TagListBean>>> getTodayTags();

    /**
     * 获取分销id
     * {
     * "goodsid":"asdas31asd1212312d"
     * }
     */
    @POST("size/monthly/getShareid")
    Call<RetrofitResult<String>> getShareId(@Body Map map);

    /**
     * 获取墨子论坛
     * {
     * }
     */
    @POST("size/Forum/info")
    Call<RetrofitResult<ArrayList<MozForumBean>>> getForumList(@Body Map map);

    /**
     * {
     * "pages":1,
     * "pagesize":20
     * }
     *
     * @return 快讯
     */
    @POST("size/flashnews/multi")
    Call<RetrofitResult<List<FlashBean>>> getNews(@Body Map map);

    @POST("size/daily/istodaysigned")
    /**
     * 查看当天是否签到
     */
    Call<RetrofitResult<Boolean>> isSigned();

    /**
     * 获取热门推荐
     */
    @POST("size/article/normal_article")
    Call<RetrofitResult<List<HotNewsBean>>> getHotRecommend(@Body Map map);

    /**
     * 读书
     * -换一批
     */
    @POST("size/books/{number}/pickothers")
    Call<RetrofitResult<List<MozReadBean.ListBean>>> getBookPickothers(@Path("number") int number);

    /**
     * 知识
     * -我的关注
     * {
     * "pages":1,
     * "pagesize":10
     * }
     */
    @POST("size/article/focus_article")
    Call<RetrofitResult<List<HotNewsBean>>> getMyRecFollow(@Body Map map);

    /**
     * 知识
     * -推荐关注
     * {
     * "pages":1,
     * "pagesize":10
     * }
     */
    @POST("size/order/v4/recommend")
    Call<RetrofitResult<List<RecommendFollowBean>>> getRecommendFollow(@Body Map map);

    /**
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "albumid":"d6d63a0fca8a42a2ae86da787557e411"
     * }}
     */
    @POST("size/article/simples")
    Call<RetrofitResult<List<AlbumContent>>> getAlbumContent(@Body Map map);

    @POST("size/album/audio_album")
    Call<RetrofitResult<List<MozAudioBean>>> getMozAudio(@Body Map map);

    /**
     * 关注墨子号/关注标签
     * {
     * "targetid":"-0012",
     * "type":1
     * }
     */
    @POST("size/order/v4/focus")
    Call<RetrofitResult<Object>> focusKnowledge(@Body Map map);

    @POST("size/order/v4/unfocus")
    Call<RetrofitResult<Object>> unfocusKnowledge(@Body Map map);

    /**
     * {
     * "targetid":"9fa0426699374d6bbe1133d216875e80",
     * "type":2
     * }
     */
    @POST("size/article/tag_detail")
    Call<RetrofitResult<TagBean>> getTagDetail(@Body Map map);

    /**
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "targetid":1,
     * "type":2
     * }
     */
    @POST("size/article/tag_article")
    Call<RetrofitResult<List<HotNewsBean>>> getTagContentList(@Body Map map);

    /**
     * 我关注的墨子号或标签
     * {
     * "pages":1,
     * "pagesize":6
     * }
     */
    @POST("size/order/v4/focus_list")
    Call<RetrofitResult<List<FocusBean>>> getFocusList(@Body Map map);

    /**
     * 查询墨水余额
     */
    @POST("size/ink/queryInk")
    Call<RetrofitResult<InkBalanceBean>> getInkBalance(@Body Map map);

    /**
     * 墨水余额
     * -获取墨水明细
     */
    @POST("size/ink/info")
    Call<RetrofitResult<List<InKRecordBean>>> getInkDetailed(@Body Map map);

    /**
     * 我的收益统计
     */
    @POST("size/balance/earn/total")
    Call<RetrofitResult<BlanaceEarnBean>> getBlanaceEarn(@Body Map map);

    /**
     * 墨水余额
     * -获取收益明细
     */
    @POST("size/balance/earn/record")
    Call<RetrofitResult<List<ProfitDetailedBean>>> getBalanceEarnRecord(@Body Map map);

    /**
     * 获取文章详情
     * -相关阅读
     * {
     * "pages":1,
     * "pagesize":3,
     * "entity":{
     * "id":"98bcbfbe4b5a48929d0bf1537a85ae45"
     * }
     * }
     */
    @POST("size/article/related_article")
    Call<RetrofitResult<List<RelatedArticleBean>>> getRelatedArticle(@Body Map map);

    /**
     * 获取文章详情
     * -相关标签
     * {
     * "targetid":"a2fd38cf63d74dc3be2beef6326230e5"
     * }
     */
    @POST("size/article/article_tag")
    Call<RetrofitResult<List<TxtRelevantTagBean>>> getRelatedTag(@Body Map map);

    /**
     * 获取文章详情
     * -点赞详情
     * {
     * "targetid":"0021a1007afc4868887f3a67d081a92f",
     * "type":"ARTICLE"
     * }
     */
    @POST("size/comment/info")
    Call<RetrofitResult<TxtClickBean>> getTxtClick(@Body Map map);

    /**
     * 获取收藏内容
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "type":5 5图文 , 6音频 , 7视频
     * }
     * }
     */
    @POST("size/article/collection_article")
    Call<RetrofitResult<List<HotNewsBean>>> getCollection(@Body Map map);

    /**
     * 获取日课收藏内容
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "type":8
     * }
     * }
     */
    @POST("size/article/collection_article")
    Call<RetrofitResult<List<OneDayArticles>>> getOneDayArticlesCollection(@Body Map map);

    /**
     * 获取专访收藏内容
     * {
     * "pages":1,
     * "pagesize":20,
     * "entity":{
     * "type":4
     * }
     * }
     */
    @POST("size/article/collection_article")
    Call<RetrofitResult<List<InterviewSoonBean>>> getInterviewCollection(@Body Map map);

    /**
     * 获取可提现金额
     */
    @POST("size/withdraw/apply/allow")
    Call<RetrofitResult<Double>> getWithdrawApplyAllow(@Body Map map);

    /**
     * 获取可提现金额
     */
    @POST("size/withdraw/apply/validate")
    Call<RetrofitResult<WitthdrawalsContentBean>> getReturnContent(@Body Map map);

    /**
     * 文章id判断文章的墨子号是否被关注
     * {
     * "id":sljnvkdfnvknjk
     * }
     */
    @POST("size/order/v4/is_article_focus")
    Call<RetrofitResult<Boolean>> articleIdGetFocus(@Body Map map);

    /**
     * 根据文章id获取文章的墨子号id
     * {
     * "id":"000577a404b04dada6f380c4081e7d37"
     * }
     */
    @POST("size/order/v4/mozid")
    Call<RetrofitResult<String>> articleIdGetMozId(@Body Map map);

    /**
     * 获取已购列表
     * <p>
     * {
     * "pages":1,
     * "pagesize":2,
     * "orientation":0
     * <p>
     * }
     */
    @POST("size/mine/purchased")
    Call<RetrofitResult<List<PurchaseBookBean>>> getPurchaseBook(@Body Map map);

    /**
     * 收益更新提示
     */
    @POST("size/account/purseUpdates")
    Call<RetrofitResult<UpdateProfitBalance>> getUpdateProfit(@Body Map map);

    /**
     * 我的关注
     * -视频
     * {
     * "pages":"1",
     * "pagesize":20
     * }
     */
    @POST("size/article/focus_video_article")
    Call<RetrofitResult<List<VideoArticleFollowBean>>> getMyRecVideoFollow(@Body Map map);

    /**
     * 获取专栏列表
     * {
     * "id":"",
     * "columnName":"",
     * "type":"",
     * "release":"",
     * "pageNum":"",
     * "pageSize":""
     * }
     */
    @POST("size/column/queryColumnInfo")
    Call<RetrofitResult<List<ColumnAllListBean>>> getColumns(@Body Map map);

    /**
     * 专栏
     * 文章列表
     * {
     * "id":"",
     * "pageNum":"",  *
     * "pageSize":"",  *
     * "columnId":"",  *
     * "startTime":"",
     * "endTime":"",
     * "articleName":""
     * }
     */
    @POST("size/column/queryArticleInfo")
    Call<RetrofitResult<List<ColumnQueryArticleBean>>> getColumnArticle(@Body Map map);

    /**
     * 专栏
     * 文章列表
     * {
     * "id":"",
     * "pageNum":"",  *
     * "pageSize":"",  *
     * "columnId":"",  *
     * "startTime":"",
     * "endTime":"",
     * "articleName":""
     * }
     */
    @POST("size/column/queryAudio")
    Call<RetrofitResult<List<ColumnAudioBean>>> getColumnAudio(@Body Map map);

    /**
     * 专栏
     * 文章详情
     * {
     * "id":""
     * }
     */
    @POST("size/column/queryArticle")
    Call<RetrofitResult<ColumnArticleDetailsBean>> getArticleDetails(@Body Map map);

    /**
     * 查询分销是否开启
     * data为true , 则表示分销状态开启
     */
    @POST("size/sharesale/isshareon")
    Call<RetrofitResult<IsshareonBean>> isShareon();

    /**
     * 获取当前用户在此商品上的分销id
     * data字段即当前用户在此商品上的分销id , 如果没有分销记录则返回空字符串
     */
    @POST("size/sharesale/getshareid")
    Call<RetrofitResult<String>> getColumnShareid(@Body Map map);


    /**
     * 获取专栏详情
     */
    @POST("size/column/queryColumn")
    Call<RetrofitResult<ColumnDetailsBean>> getQueryColumn(@Body Map map);

    /**
     * 获取首页精品专栏列表
     * {
     * "pageNum":1,
     * "pageSize":3
     * }
     */
    @POST("size/column/queryRandomColumn")
    Call<RetrofitResult<SubscribeColumnBean>> getRandomColumn(@Body Map map);

    /**
     * 获取已购专栏
     * {
     * "pages":1,
     * "pagesize":20,
     * "orientation":0,
     * "entity":{
     * "type":3
     * }
     * }
     */
    @POST("size/mine/purchased")
    Call<RetrofitResult<List<ColumnPurchaseBean>>> getColumnPurchased(@Body Map map);


    /**
     * 判断手机号是否注册
     * {
     * 1  已注册   0为注册
     * }
     */
    @POST("/size/mine/user/{phone}")
    Call<RetrofitResult<Integer>> getIsRegistPhone(@Path("phone") String phone);

    /**
     * 判断验证码是正确
     * 40005  验证码错误
     * 40003  用户不存在
     * 200    成功
     */
    @POST("/size/user/phonelogin/{phone}/{code}")
    Call<RetrofitResult<UserInforMation>> getIsCodePhone(@Path("phone") String phone, @Path("code") String code);
}
