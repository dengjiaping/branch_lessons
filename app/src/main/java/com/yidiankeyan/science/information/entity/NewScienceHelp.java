package com.yidiankeyan.science.information.entity;

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
 * Created by nby on 2017/1/10.
 * 作用：
 */

public class NewScienceHelp {
    /**
     * id : -0012
     * isfrozen : 0
     * mouthprice : 0.5
     * answersnum : 6
     * totalincome : 30.05
     * createtime : 1474610055000
     * domain : *2*,*5*,*11*
     * focusnum : 0
     * listennum : 0
     * eavesdropnum : 1
     * messageurl : /2.mp3
     * messagetaketime : 0
     * name : Eric
     * coverimg : aaa.jpg
     * profession : Docter/IT Developer
     * authenticated : 1
     * sign : i amd super man
     */

    /**
     * 提问者信息
     */
    private MakerBean maker;
    /**
     * id : -1
     * isfrozen : 0
     * mouthprice : 56.2
     * answersnum : 0
     * totalincome : 0.0
     * createtime : 1480588354000
     * domain : *2*,*5*,*11*
     * focusnum : 0
     * listennum : 0
     * eavesdropnum : 0
     * messageurl : asdasd
     * messagetaketime : 0
     * name : 科答用户名
     * authenticated : 0
     */

    /**
     * 回答者信息
     */
    private SolverBean solver;
    /**
     * id : aaaassss
     * makerid : -0012
     * solverid : -1
     * createtime : 1483525940000
     * kedaalbumid : 3
     * answertime : 1483502318000
     * questions : 春风十里，不及你青丝随风
     * payoff : 0.05
     * answerurl : null
     * eavesdropnum : 12
     * praisenum : 3
     * taketime : 54
     * isuse : 1
     * ischeck : 2
     */

    /**
     * 科答详细信息
     */
    private KederDBBean kederDB;
    /**
     * permission : 0
     */

    /**
     * 包含是否有权限播放
     */
    private KederInfoBean kederInfo;
    /**
     * 科答问题信息
     */
    private KedaQuestionsDBBean kedaQuestionsDB;
    /**
     * 科答追问，回答信息
     */
    private KedaReQuestionDBBean kedaReQuestionDB;

    public KedaQuestionsDBBean getKedaQuestionsDB() {
        return kedaQuestionsDB;
    }

    public void setKedaQuestionsDB(KedaQuestionsDBBean kedaQuestionsDB) {
        this.kedaQuestionsDB = kedaQuestionsDB;
    }

    public KedaReQuestionDBBean getKedaReQuestionDB() {
        return kedaReQuestionDB;
    }

    public void setKedaReQuestionDB(KedaReQuestionDBBean kedaReQuestionDB) {
        this.kedaReQuestionDB = kedaReQuestionDB;
    }

    public MakerBean getMaker() {
        return maker;
    }

    public void setMaker(MakerBean maker) {
        this.maker = maker;
    }

    public SolverBean getSolver() {
        return solver;
    }

    public void setSolver(SolverBean solver) {
        this.solver = solver;
    }

    public KederDBBean getKederDB() {
        return kederDB;
    }

    public void setKederDB(KederDBBean kederDB) {
        this.kederDB = kederDB;
    }

    public KederInfoBean getKederInfo() {
        return kederInfo;
    }

    public void setKederInfo(KederInfoBean kederInfo) {
        this.kederInfo = kederInfo;
    }

    public static class KedaQuestionsDBBean {

        /**
         * id : 4a6cb909ce8f497b92ca71799996691e
         * kedaalbumid : 1
         * makerid : 419f619d2cb64e869e1a90ae10b248e7
         * solverid : -0012
         * createtime : 1484121842000
         * overtime : 1484294658000
         * amount : 0.01
         * catid : 1701110404000002
         * cattime : 1484121922000
         * catchanel : 2
         * questions : 啦啦啦啦啦了
         * isanswer : 1
         * checkstatus : null
         */

        private String id;
        private int kedaalbumid;
        private String makerid;
        private String solverid;
        private long createtime;
        private long overtime;
        private double amount;
        private String catid;
        private long cattime;
        private int catchanel;
        private String questions;
        private int isanswer;
        private Object checkstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getKedaalbumid() {
            return kedaalbumid;
        }

        public void setKedaalbumid(int kedaalbumid) {
            this.kedaalbumid = kedaalbumid;
        }

        public String getMakerid() {
            return makerid;
        }

        public void setMakerid(String makerid) {
            this.makerid = makerid;
        }

        public String getSolverid() {
            return solverid;
        }

        public void setSolverid(String solverid) {
            this.solverid = solverid;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getOvertime() {
            return overtime;
        }

        public void setOvertime(long overtime) {
            this.overtime = overtime;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public long getCattime() {
            return cattime;
        }

        public void setCattime(long cattime) {
            this.cattime = cattime;
        }

        public int getCatchanel() {
            return catchanel;
        }

        public void setCatchanel(int catchanel) {
            this.catchanel = catchanel;
        }

        public String getQuestions() {
            return questions;
        }

        public void setQuestions(String questions) {
            this.questions = questions;
        }

        public int getIsanswer() {
            return isanswer;
        }

        public void setIsanswer(int isanswer) {
            this.isanswer = isanswer;
        }

        public Object getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(Object checkstatus) {
            this.checkstatus = checkstatus;
        }
    }

    public static class MakerBean {
        private String id;
        private int isfrozen;
        private double mouthprice;
        private int answersnum;
        private double totalincome;
        private long createtime;
        private String domain;
        private int focusnum;
        private int listennum;
        private int eavesdropnum;
        private String messageurl;
        private int messagetaketime;
        private String name;
        private String coverimg;
        private String profession;
        private int authenticated;
        private String sign;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIsfrozen() {
            return isfrozen;
        }

        public void setIsfrozen(int isfrozen) {
            this.isfrozen = isfrozen;
        }

        public double getMouthprice() {
            return mouthprice;
        }

        public void setMouthprice(double mouthprice) {
            this.mouthprice = mouthprice;
        }

        public int getAnswersnum() {
            return answersnum;
        }

        public void setAnswersnum(int answersnum) {
            this.answersnum = answersnum;
        }

        public double getTotalincome() {
            return totalincome;
        }

        public void setTotalincome(double totalincome) {
            this.totalincome = totalincome;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public int getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(int focusnum) {
            this.focusnum = focusnum;
        }

        public int getListennum() {
            return listennum;
        }

        public void setListennum(int listennum) {
            this.listennum = listennum;
        }

        public int getEavesdropnum() {
            return eavesdropnum;
        }

        public void setEavesdropnum(int eavesdropnum) {
            this.eavesdropnum = eavesdropnum;
        }

        public String getMessageurl() {
            return messageurl;
        }

        public void setMessageurl(String messageurl) {
            this.messageurl = messageurl;
        }

        public int getMessagetaketime() {
            return messagetaketime;
        }

        public void setMessagetaketime(int messagetaketime) {
            this.messagetaketime = messagetaketime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public int getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(int authenticated) {
            this.authenticated = authenticated;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    public static class SolverBean {

        private String id;
        private int isfrozen;
        private double mouthprice;
        private int answersnum;
        private double totalincome;
        private long createtime;
        private String domain;
        private int focusnum;
        private int listennum;
        private int eavesdropnum;
        private String messageurl;
        private int messagetaketime;
        private String name;
        private int authenticated;
        /**
         * coverimg : aaa.jpg
         * profession : Docter/IT Developer
         * sign : i amd super man
         */

        private String coverimg;
        private String profession;
        private String sign;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIsfrozen() {
            return isfrozen;
        }

        public void setIsfrozen(int isfrozen) {
            this.isfrozen = isfrozen;
        }

        public double getMouthprice() {
            return mouthprice;
        }

        public void setMouthprice(double mouthprice) {
            this.mouthprice = mouthprice;
        }

        public int getAnswersnum() {
            return answersnum;
        }

        public void setAnswersnum(int answersnum) {
            this.answersnum = answersnum;
        }

        public double getTotalincome() {
            return totalincome;
        }

        public void setTotalincome(double totalincome) {
            this.totalincome = totalincome;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public int getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(int focusnum) {
            this.focusnum = focusnum;
        }

        public int getListennum() {
            return listennum;
        }

        public void setListennum(int listennum) {
            this.listennum = listennum;
        }

        public int getEavesdropnum() {
            return eavesdropnum;
        }

        public void setEavesdropnum(int eavesdropnum) {
            this.eavesdropnum = eavesdropnum;
        }

        public String getMessageurl() {
            return messageurl;
        }

        public void setMessageurl(String messageurl) {
            this.messageurl = messageurl;
        }

        public int getMessagetaketime() {
            return messagetaketime;
        }

        public void setMessagetaketime(int messagetaketime) {
            this.messagetaketime = messagetaketime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(int authenticated) {
            this.authenticated = authenticated;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    public static class KedaReQuestionDBBean {

        /**
         * kederid : aaaassss
         * question : who's the most import one"
         * cretetime : 1483512892000
         * answerurl : 111.mp3
         * reanswertime : 1483512991000
         * checkstatus : 2
         * isuse : 1
         * isanswer : 1
         */

        private String kederid;
        private String question;
        private long cretetime;
        private String answerurl;
        private long reanswertime;
        private int checkstatus;
        private int isuse;
        private int isanswer;
        /**
         * taketime : null
         */

        private int taketime;

        public String getKederid() {
            return kederid;
        }

        public void setKederid(String kederid) {
            this.kederid = kederid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public long getCretetime() {
            return cretetime;
        }

        public void setCretetime(long cretetime) {
            this.cretetime = cretetime;
        }

        public String getAnswerurl() {
            return answerurl;
        }

        public void setAnswerurl(String answerurl) {
            this.answerurl = answerurl;
        }

        public long getReanswertime() {
            return reanswertime;
        }

        public void setReanswertime(long reanswertime) {
            this.reanswertime = reanswertime;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public int getIsanswer() {
            return isanswer;
        }

        public void setIsanswer(int isanswer) {
            this.isanswer = isanswer;
        }

        public int getTaketime() {
            return taketime;
        }

        public void setTaketime(int taketime) {
            this.taketime = taketime;
        }
    }

    public static class KederDBBean {
        private String id;
        private String makerid;
        private String solverid;
        private long createtime;
        private int kedaalbumid;
        private long answertime;
        private String questions;
        private double payoff;
        private String answerurl;
        private int eavesdropnum;
        private int praisenum;
        private int taketime;
        private int isuse;
        private int ischeck;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMakerid() {
            return makerid;
        }

        public void setMakerid(String makerid) {
            this.makerid = makerid;
        }

        public String getSolverid() {
            return solverid;
        }

        public void setSolverid(String solverid) {
            this.solverid = solverid;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getKedaalbumid() {
            return kedaalbumid;
        }

        public void setKedaalbumid(int kedaalbumid) {
            this.kedaalbumid = kedaalbumid;
        }

        public long getAnswertime() {
            return answertime;
        }

        public void setAnswertime(long answertime) {
            this.answertime = answertime;
        }

        public String getQuestions() {
            return questions;
        }

        public void setQuestions(String questions) {
            this.questions = questions;
        }

        public double getPayoff() {
            return payoff;
        }

        public void setPayoff(double payoff) {
            this.payoff = payoff;
        }

        public String getAnswerurl() {
            return answerurl;
        }

        public void setAnswerurl(String answerurl) {
            this.answerurl = answerurl;
        }

        public int getEavesdropnum() {
            return eavesdropnum;
        }

        public void setEavesdropnum(int eavesdropnum) {
            this.eavesdropnum = eavesdropnum;
        }

        public int getPraisenum() {
            return praisenum;
        }

        public void setPraisenum(int praisenum) {
            this.praisenum = praisenum;
        }

        public int getTaketime() {
            return taketime;
        }

        public void setTaketime(int taketime) {
            this.taketime = taketime;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public int getIscheck() {
            return ischeck;
        }

        public void setIscheck(int ischeck) {
            this.ischeck = ischeck;
        }
    }

    public static class KederInfoBean {
        private int permission;
        /**
         * profit : 0
         */

        private double profit;

        public int getPermission() {
            return permission;
        }

        public void setPermission(int permission) {
            this.permission = permission;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }
    }
}
