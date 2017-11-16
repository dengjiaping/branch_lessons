package com.yidiankeyan.science.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.ColumnArticleDetailsBean;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.information.entity.OneDayArticles;
import com.yidiankeyan.science.utils.proxy.utils.MediaPlayerProxy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;


/**
 * Created by nby on 2016/8/12.
 */
public class AudioPlayManager implements IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnPreparedListener, IjkMediaPlayer.OnBufferingUpdateListener, IjkMediaPlayer.OnErrorListener {
    private static AudioPlayManager instance;
    public MediaPlayer mediaPlayer;
    /**
     * 播放路径
     */
    private String mUrl;
    /**
     * 标题
     */
    private String mTitle;
    /**
     * 播放id
     */
    private String mMediaPlayId;
    private int mRes;
    /**
     * 列表中播放位置
     */
    private int playPosition;
    private List<Integer> mResList;
    /**
     * 当前状态
     */
    public int CURRENT_STATE;
    MediaPlayerProxy proxy;
    public IjkMediaPlayer mIjkMediaPlayer;

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    public List<Integer> getResList() {
        return mResList;
    }

    public void setResList(List<Integer> mResList) {
        this.mResList = mResList;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getRes() {
        return mRes;
    }

    public void setRes(int mRes) {
        this.mRes = mRes;
    }

    public String getmMediaPlayId() {
        return mMediaPlayId;
    }

    private AudioPlayManager() {

    }

    public void setmMediaPlayId(String mMediaPlayId) {
        this.mMediaPlayId = mMediaPlayId;
    }

    public static AudioPlayManager getInstance() {
        if (instance == null) {
            Class var0 = AudioPlayManager.class;
            synchronized (AudioPlayManager.class) {
                if (instance == null) {
                    instance = new AudioPlayManager();
                }
            }
        }
        return instance;
    }

    /**
     * 当前播放进度
     *
     * @return 播放进度
     */
    public long getCurrentPosition() {
        if (isPlaying() || SystemConstant.ON_PAUSE == CURRENT_STATE) {
            try {
                return mIjkMediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else return 0;
    }

//    /**
//     * 设置播放源
//     *
//     * @param url 路径
//     * @param id  id
//     */
//    public void setUp(String url, String id) {
//        this.mUrl = url;
//        this.mMediaPlayId = id;
//    }

    private void startProxy() {
        if (proxy == null) {
            proxy = new MediaPlayerProxy();
        }
        proxy.init();
        proxy.start();
    }

    /**
     * 科答偷听播放
     */
    public void playEavedrop(String url) {
        LogUtils.e(url);
        if (mediaPlayer == null) {
            release();
            mUrl = url;
            if (!new File(url).exists()) {
                startProxy();
                url = proxy.getProxyURL(url);
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            CURRENT_STATE = SystemConstant.ON_PREPARE;
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        CURRENT_STATE = SystemConstant.ON_PLAYING;
                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    CURRENT_STATE = SystemConstant.ON_STOP;
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.EAVEDROP_COMPLITE));
                    release();
                }
            });
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (CURRENT_STATE == SystemConstant.ON_PLAYING) {
            if (TextUtils.equals(mUrl, url))
                pause();
            else {
                release();
                mUrl = url;
                if (!new File(url).exists()) {
                    startProxy();
                    url = proxy.getProxyURL(url);
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                CURRENT_STATE = SystemConstant.ON_PREPARE;
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            CURRENT_STATE = SystemConstant.ON_PLAYING;
                            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
                        }
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        CURRENT_STATE = SystemConstant.ON_STOP;
                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.EAVEDROP_COMPLITE));
                        release();
                    }
                });
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (CURRENT_STATE == SystemConstant.ON_PAUSE) {
            if (TextUtils.equals(mUrl, url))
                resume();
            else {
                release();
                mUrl = url;
                if (!new File(url).exists()) {
                    startProxy();
                    url = proxy.getProxyURL(url);
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                CURRENT_STATE = SystemConstant.ON_PREPARE;
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            CURRENT_STATE = SystemConstant.ON_PLAYING;
                            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
                        }
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        CURRENT_STATE = SystemConstant.ON_STOP;
                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.EAVEDROP_COMPLITE));
                        release();
                    }
                });
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 播放录音
     *
     * @param url
     */
    public void playRecord(String url) {
//        if (CURRENT_STATE == SystemConstant.ON_PLAYING) {
//            pause();
//            return;
//        } else if (CURRENT_STATE == SystemConstant.ON_PAUSE) {
//            resume();
//            return;
//        }
        release();
        startProxy();
        if (!new File(url).exists()) {
            url = proxy.getProxyURL(url);
//            mediaPlayer = new MediaPlayer();
            mIjkMediaPlayer = new IjkMediaPlayer();
        }
        mIjkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        CURRENT_STATE = SystemConstant.ON_PREPARE;
        mIjkMediaPlayer.setOnPreparedListener(new IjkMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (mp != null) {
                    mIjkMediaPlayer = (IjkMediaPlayer) mp;
                    mp.start();
                    CURRENT_STATE = SystemConstant.ON_PLAYING;
                }
            }
        });
        mIjkMediaPlayer.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.AUDIO_COMPLET));
                release();
            }
        });
        try {
            mIjkMediaPlayer.setDataSource(url);
            mIjkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replay() {
        mIjkMediaPlayer.seekTo(0);
        mIjkMediaPlayer.start();
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
    }

    //////////////////////////////////////////////////////////////////

    /**
     * 播放列表
     */
    private List<AudioBean> audioList;
    /**
     * 当前播放位置
     */
    private int position;

    private PlayModel model;

    private PlayType type;

    /**
     * 播放源初始化
     *
     * @param audioList 播放列表
     * @param position  播放位置
     * @param playModel 播放模式
     */
    public void init(List audioList, int position, PlayModel playModel) {
        release();
        List<AudioBean> list = new ArrayList<>();
        if (audioList.get(0) instanceof OneDayArticles) {
            type = PlayType.OneDayArticles;
            for (Object oneDayArticles : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((OneDayArticles) oneDayArticles).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(((OneDayArticles) oneDayArticles).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((OneDayArticles) oneDayArticles).getTitle());
                audio.setAuthorName(((OneDayArticles) oneDayArticles).getTitle());
                audio.setId(((OneDayArticles) oneDayArticles).getId());
                audio.setAuthorName(((OneDayArticles) oneDayArticles).getContentauthor());
                audio.setAuthorId(((OneDayArticles) oneDayArticles).getContentauthorid());
                audio.setCoverImg(((OneDayArticles) oneDayArticles).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof AlbumContent) {
            type = PlayType.AlbumContent;
            for (Object album : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((AlbumContent) album).getMediaurl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(((AlbumContent) album).getArticleid());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((AlbumContent) album).getArticlename());
                audio.setAuthorName(((AlbumContent) album).getAuthorname());
                audio.setId(((AlbumContent) album).getArticleid());
                audio.setAuthorName(((AlbumContent) album).getAuthorname());
                audio.setCoverImg(((AlbumContent) album).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MonthlyDetailsBean.MonthlyDBBean) {
            type = PlayType.MonthlyDBBean;
            for (Object monthly : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                String filePath = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId()) == null ? null : DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId()).getFilePath();
                if (Util.fileExisted(filePath)) {
                    audio.setFilePath(filePath);
                }
                audio.setName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getName());
                audio.setAuthorName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAuthor());
                audio.setId(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId());
                audio.setAuthorName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAuthor());
                audio.setCoverImg(((MonthlyDetailsBean.MonthlyDBBean) monthly).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MagazineExcerptBean) {
            type = PlayType.MagazineExcerptBean;
            for (Object magazine : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MagazineExcerptBean) magazine).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                MagazineExcerptBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MagazineExcerptBean) magazine).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((MagazineExcerptBean) magazine).getName());
                audio.setAuthorName(((MagazineExcerptBean) magazine).getauthor());
                audio.setId(((MagazineExcerptBean) magazine).getId());
                audio.setAuthorName(TextUtils.isEmpty(((MagazineExcerptBean) magazine).getauthor()) ?
                        ((MagazineExcerptBean) magazine).getMonthlyName() :
                        ((MagazineExcerptBean) magazine).getauthor()
                );
                audio.setCoverImg(((MagazineExcerptBean) magazine).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MozReadDetailsBean) {
            type = PlayType.MozReadDetailsBean;
            for (Object book : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MozReadDetailsBean) book).getMediaurl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                MozReadDetailsBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFile(((MozReadDetailsBean) book).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((MozReadDetailsBean) book).getName());
                audio.setAuthorName(((MozReadDetailsBean) book).getAuthor());
                audio.setId(((MozReadDetailsBean) book).getId());
                audio.setAuthorName(((MozReadDetailsBean) book).getAuthor());
                audio.setCoverImg(((MozReadDetailsBean) book).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof IssuesDetailBean) {
            type = PlayType.IssuesDetailBean;
            for (Object issues : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((IssuesDetailBean) issues).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                IssuesDetailBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(((IssuesDetailBean) issues).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((IssuesDetailBean) issues).getName());
                audio.setAuthorName(((IssuesDetailBean) issues).getName());
                audio.setId(((IssuesDetailBean) issues).getId());
                audio.setAuthorName(((IssuesDetailBean) issues).getColumnname());
                audio.setCoverImg(((IssuesDetailBean) issues).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof ColumnAudioBean) {
            type = PlayType.ColumnAudioBean;
            for (Object issues : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((ColumnAudioBean) issues).getAudioUrl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                ColumnAudioBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryColumnDownloadFile(((ColumnAudioBean) issues).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((ColumnAudioBean) issues).getAudioName());
                audio.setAuthorName(((ColumnAudioBean) issues).getAuthorname());
                audio.setId(((ColumnAudioBean) issues).getId());
                audio.setAuthorName(((ColumnAudioBean) issues).getColumnName());
                audio.setCoverImg(((ColumnAudioBean) issues).getAudioImg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof ColumnArticleDetailsBean.ColumnAudioModleBean) {
            type = PlayType.ColumnArticleDetailsBean;
            for (Object issues : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((ColumnArticleDetailsBean.ColumnAudioModleBean) issues).getAudioUrl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                audio.setName(((ColumnArticleDetailsBean.ColumnAudioModleBean) issues).getAudioName());
                audio.setAuthorName(((ColumnArticleDetailsBean.ColumnAudioModleBean) issues).getAudioName());
                audio.setId(((ColumnArticleDetailsBean.ColumnAudioModleBean) issues).getId());
                audio.setCoverImg(((ColumnArticleDetailsBean.ColumnAudioModleBean) issues).getAudioImg());
                list.add(audio);
            }
        } else {
            throw new RuntimeException("不支持的播放类型!!!");
        }
        if (list.size() == 0) {
//            ToastMaker.showShortToast("播放地址无效");
            return;
        }
        this.audioList = list;
        this.position = position;
        this.model = playModel;
    }

    public void playForPosition(int position) {
        release();
        this.position = position;
//        start();
        ijkStart();
    }

    public void onDataChanged(List audioList, int position) {
        List<AudioBean> list = new ArrayList<>();
        if (audioList.get(0) instanceof OneDayArticles) {
            type = PlayType.OneDayArticles;
            for (Object oneDayArticles : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((OneDayArticles) oneDayArticles).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(((OneDayArticles) oneDayArticles).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((OneDayArticles) oneDayArticles).getTitle());
                audio.setAuthorName(((OneDayArticles) oneDayArticles).getTitle());
                audio.setId(((OneDayArticles) oneDayArticles).getId());
                audio.setAuthorName(((OneDayArticles) oneDayArticles).getContentauthor());
                audio.setAuthorId(((OneDayArticles) oneDayArticles).getContentauthorid());
                audio.setCoverImg(((OneDayArticles) oneDayArticles).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof AlbumContent) {
            type = PlayType.AlbumContent;
            for (Object album : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((AlbumContent) album).getMediaurl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(((AlbumContent) album).getArticleid());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((AlbumContent) album).getArticlename());
                audio.setAuthorName(((AlbumContent) album).getAuthorname());
                audio.setId(((AlbumContent) album).getArticleid());
                audio.setAuthorName(((AlbumContent) album).getAuthorname());
                audio.setCoverImg(((AlbumContent) album).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MonthlyDetailsBean.MonthlyDBBean) {
            type = PlayType.MonthlyDBBean;
            for (Object monthly : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                String filePath = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId()) == null ? null : DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId()).getFilePath();
                if (Util.fileExisted(filePath)) {
                    audio.setFilePath(filePath);
                }
                audio.setName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getName());
                audio.setAuthorName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAuthor());
                audio.setId(((MonthlyDetailsBean.MonthlyDBBean) monthly).getId());
                audio.setAuthorName(((MonthlyDetailsBean.MonthlyDBBean) monthly).getAuthor());
                audio.setCoverImg(((MonthlyDetailsBean.MonthlyDBBean) monthly).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MagazineExcerptBean) {
            type = PlayType.MagazineExcerptBean;
            for (Object magazine : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MagazineExcerptBean) magazine).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                MagazineExcerptBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryMagazineDownloadFile(((MagazineExcerptBean) magazine).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((MagazineExcerptBean) magazine).getName());
                audio.setAuthorName(((MagazineExcerptBean) magazine).getauthor());
                audio.setId(((MagazineExcerptBean) magazine).getId());
                audio.setAuthorName(TextUtils.isEmpty(((MagazineExcerptBean) magazine).getauthor()) ?
                        ((MagazineExcerptBean) magazine).getMonthlyName() :
                        ((MagazineExcerptBean) magazine).getauthor()
                );
                audio.setCoverImg(((MagazineExcerptBean) magazine).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof MozReadDetailsBean) {
            type = PlayType.MozReadDetailsBean;
            for (Object book : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((MozReadDetailsBean) book).getMediaurl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                MozReadDetailsBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryBookDownloadFile(((MozReadDetailsBean) book).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((MozReadDetailsBean) book).getName());
                audio.setAuthorName(((MozReadDetailsBean) book).getAuthor());
                audio.setId(((MozReadDetailsBean) book).getId());
                audio.setAuthorName(((MozReadDetailsBean) book).getAuthor());
                audio.setCoverImg(((MozReadDetailsBean) book).getCoverimgurl());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof IssuesDetailBean) {
            type = PlayType.IssuesDetailBean;
            for (Object issues : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((IssuesDetailBean) issues).getAudiourl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                IssuesDetailBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(((IssuesDetailBean) issues).getId());
                if (albumContent != null && Util.fileExisted(albumContent.getFilePath())) {
                    audio.setFilePath(albumContent.getFilePath());
                }
                audio.setName(((IssuesDetailBean) issues).getName());
                audio.setAuthorName(((IssuesDetailBean) issues).getName());
                audio.setId(((IssuesDetailBean) issues).getId());
                audio.setAuthorName(((IssuesDetailBean) issues).getColumnname());
                audio.setCoverImg(((IssuesDetailBean) issues).getCoverimg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof ColumnAudioBean) {
            type = PlayType.ColumnAudioBean;
            for (Object column : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((ColumnAudioBean) column).getAudioUrl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                ColumnAudioBean columnContent = DB.getInstance(DemoApplication.applicationContext).queryColumnDownloadFile(((ColumnAudioBean) column).getId());
                if (columnContent != null && Util.fileExisted(columnContent.getFilePath())) {
                    audio.setFilePath(columnContent.getFilePath());
                }
                audio.setName(((ColumnAudioBean) column).getAudioName());
                audio.setAuthorName(((ColumnAudioBean) column).getAuthorname());
                audio.setId(((ColumnAudioBean) column).getColumnId());
                audio.setAuthorName(((ColumnAudioBean) column).getColumnName());
                audio.setCoverImg(((ColumnAudioBean) column).getAudioImg());
                list.add(audio);
            }
        } else if (audioList.get(0) instanceof ColumnArticleDetailsBean.ColumnAudioModleBean) {
            type = PlayType.ColumnArticleDetailsBean;
            for (Object column : audioList) {
                AudioBean audio = new AudioBean();
                audio.setUrl(Util.getImgUrl(((ColumnArticleDetailsBean.ColumnAudioModleBean) column).getAudioUrl()));
                if (TextUtils.isEmpty(audio.getUrl()))
                    break;
                audio.setName(((ColumnArticleDetailsBean.ColumnAudioModleBean) column).getAudioName());
                audio.setAuthorName(((ColumnArticleDetailsBean.ColumnAudioModleBean) column).getAudioName());
                audio.setId(((ColumnArticleDetailsBean.ColumnAudioModleBean) column).getColumnId());
                audio.setCoverImg(((ColumnArticleDetailsBean.ColumnAudioModleBean) column).getAudioImg());
                list.add(audio);
            }
        }
        if (list.size() == 0) {
            ToastMaker.showShortToast("播放地址无效");
            return;
        }
        this.audioList = list;
        this.position = position;
    }

    public PlayModel getModel() {
        return model;
    }

    public PlayType getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public void setModel(PlayModel model) {
        this.model = model;
    }

    public List<AudioBean> getAudioList() {
        return audioList;
    }

    public boolean isInited() {
        return audioList != null && audioList.size() != 0;
    }

    public String getCurrId() {
        if (isInited()) {
            return audioList.get(position).getId();
        } else {
            return "";
        }
    }

    public AudioBean getCurrAudio() {
        if (isInited()) {
            return audioList.get(position);
        } else {
            return null;
        }
    }

    private AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback
                        if(DemoApplication.isPlay){
                            resume();
                        }
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        ((AudioManager) DemoApplication.getInstance().getSystemService(Context.AUDIO_SERVICE)).abandonAudioFocus(afChangeListener);
                        // Stop playback
                        stop();
                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_HIDE_PLAYBAR));
                    }
                }
            };

    private String url = null;

    /**
     * liuchao  add
     */
    public void ijkStart() {
        AudioManager am = (AudioManager) DemoApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        Log.i("start_play==", "1+" + audioList.size());
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //当前没有播放
            if (audioList == null)
                return;
            if (Util.fileExisted(audioList.get(position).getFilePath())) {
                url = audioList.get(position).getFilePath();
                Log.i("start_play==", "1+" + url + "+" + position);
            } else {
//                startProxy();
//                url = proxy.getProxyURL(audioList.get(position).getUrl());
                url = audioList.get(position).getUrl();
                Log.i("start_play==", "2+" + url + "+" + position);
            }
            Log.i("start_play==", "2");
        }
        try {
            if (mIjkMediaPlayer == null) {
                mIjkMediaPlayer = new IjkMediaPlayer();
                mIjkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mIjkMediaPlayer.reset();
                mIjkMediaPlayer.setDataSource(url);
                mIjkMediaPlayer.prepareAsync();
                mIjkMediaPlayer.start();
                mIjkMediaPlayer.setOnBufferingUpdateListener(this);
                mIjkMediaPlayer.setOnPreparedListener(this);
                mIjkMediaPlayer.setOnCompletionListener(this);
                mIjkMediaPlayer.setOnErrorListener(this);
                //通知activity改变UI
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PREPARE));
                CURRENT_STATE = SystemConstant.ON_PREPARE;
                Log.i("start_play==", "3");
            } else {
                Log.i("start_play==", "3=1=else");
                mIjkMediaPlayer.start();
                Log.i("start_play==", "3=2=else");
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
                CURRENT_STATE = SystemConstant.ON_PLAYING;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.MAINACTIVITY_AUDIO_REFRESH));

    }

//    public void start() {
//        AudioManager am = (AudioManager) DemoApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
//        int result = am.requestAudioFocus(afChangeListener,
//                AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        Log.i("start_play==","1+"+audioList.size());
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            //当前没有播放
//            if (audioList == null)
//                return;
//            if (Util.fileExisted(audioList.get(position).getFilePath())) {
//                url = audioList.get(position).getFilePath();
//                Log.i("start_play==","1+"+url+"+"+position);
//            } else {
//                startProxy();
//                url = proxy.getProxyURL(audioList.get(position).getUrl());
//                Log.i("start_play==","2+"+url+"+"+position);
//            }
//            Log.i("start_play==","2");
//            try {
//                if (mediaPlayer == null) {
//                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaPlayer.reset();
//                    mediaPlayer.setDataSource(url);
//                    mediaPlayer.prepareAsync();
////                    mediaPlayer.start();
//                    mediaPlayer.setOnBufferingUpdateListener(this);
//                    mediaPlayer.setOnPreparedListener(this);
//                    mediaPlayer.setOnCompletionListener(this);
//                    mediaPlayer.setOnErrorListener(this);
//                    //通知activity改变UI
//                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PREPARE));
//                    CURRENT_STATE = SystemConstant.ON_PREPARE;
//                    Log.i("start_play==","3");
//                } else {
//                    Log.i("start_play==","3=1=else");
//                    mediaPlayer.start();
//                    Log.i("start_play==","3=2=else");
//                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
//                    CURRENT_STATE = SystemConstant.ON_PLAYING;
//                }
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.MAINACTIVITY_AUDIO_REFRESH));
//        }
//    }

    /**
     * 上一首
     */
    public void last() {
        release();
        if (audioList.size() != 1) {
            //继续播放
            if (model == PlayModel.ORDER) {
                //顺序播放
                if (position != 0) {
                    position--;
                } else {
                    position = audioList.size() - 1;
                }
            } else {
                //随机播放
                int[] num = new int[audioList.size() - 1];
                for (int i = 0, j = 0; i < audioList.size(); i++) {
                    if (i != position) {
                        num[j] = i;
                        j++;
                    }
                }
                Random random = new Random();
                int max = num.length - 1;
                int min = 0;
                position = num[random.nextInt(max) % (max - min + 1) + min];
            }
//            start();
            ijkStart();
        }
    }

    /**
     * 下一首
     */
    public void next() {
        release();
        if (audioList.size() != 1) {
            //继续播放
            if (model == PlayModel.ORDER) {
                //顺序播放
                if (position != audioList.size() - 1) {
                    position++;
                } else {
                    position = 0;
                }
            } else {
                //随机播放
                int[] num = new int[audioList.size() - 1];
                for (int i = 0, j = 0; i < audioList.size(); i++) {
                    if (i != position) {
                        num[j] = i;
                        j++;
                    }
                }
                Random random = new Random();
                int max = num.length - 1;
                int min = 0;
                position = num[random.nextInt(max) % (max - min + 1) + min];
            }
//            start();
            ijkStart();
        }

    }

    public void pause() {
        Log.i("start_play==", "pause1...");
        if (mIjkMediaPlayer != null && isPlaying()) {
            try {
                Log.i("start_play==", "pause2...");
                mIjkMediaPlayer.pause();
                Log.i("start_play==", "pause3...");
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PAUSE));
                CURRENT_STATE = SystemConstant.ON_PAUSE;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.MAINACTIVITY_AUDIO_REFRESH));
    }

    public void resume() {
        Log.i("start_play==", "resume1...");
        if (mIjkMediaPlayer != null && CURRENT_STATE == SystemConstant.ON_PAUSE) {
            try {
                Log.i("start_play==", "resume2...");
                mIjkMediaPlayer.start();
                Log.i("start_play==", "resume3...");
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
                CURRENT_STATE = SystemConstant.ON_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.MAINACTIVITY_AUDIO_REFRESH));
    }

    public String getDuration() {
        Log.i("start_play==", "getDuration...");
        if (CURRENT_STATE == SystemConstant.ON_PLAYING || CURRENT_STATE == SystemConstant.ON_PAUSE) {
            if (CURRENT_STATE == SystemConstant.ON_PREPARE)
                return "00:00";
            return TimeUtils.getTimeFromLong(mIjkMediaPlayer.getDuration());
        } else {
            return "00:00";
        }
    }

    public long getIntDuration() {
//        Log.i("start_play==", "getIntDuration...");
        try {
            if (CURRENT_STATE == SystemConstant.ON_PREPARE)
                return 0;
            return mIjkMediaPlayer.getDuration();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isPlaying() {
        if (mIjkMediaPlayer == null)
            return false;
        try {
            return mIjkMediaPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stop() {
        try {
            if (mIjkMediaPlayer != null) {
                if (mIjkMediaPlayer.isPlaying()) {
                    mIjkMediaPlayer.stop();
                    mIjkMediaPlayer.reset();
                    mIjkMediaPlayer.release();
                }
                mIjkMediaPlayer = null;
            }
            CURRENT_STATE = SystemConstant.ON_STOP;
            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_STOP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            if (mIjkMediaPlayer != null) {
                mIjkMediaPlayer.stop();
                mIjkMediaPlayer.reset();
                mIjkMediaPlayer.release();
                mIjkMediaPlayer = null;
            }
            CURRENT_STATE = SystemConstant.ON_STOP;
            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_STOP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//////        Toast.makeText(DemoApplication.applicationContext, "播放完成", Toast.LENGTH_SHORT).show();
////        ToastMaker.showShortToast("播放完成");
//////        if (mDataList.get(playPosition).getDate().equals(mDataList.get(playPosition + 1).getDate())) {
//////            //播放的位置+1
//////            playPosition++;
//////            AudioPlayManager.getInstance(mContext).stop();
//////            AudioPlayManager.getInstance(mContext).mediaPlayer = MediaPlayer.create(mContext, mDataList.get(playPosition).getVoiceRes());
//////            AudioPlayManager.getInstance(mContext).mediaPlayer.start();
//////            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
//////            AudioPlayManager.getInstance(mContext).mediaPlayer.setOnCompletionListener(this);
//////            Toast.makeText(mContext, playPosition + "", Toast.LENGTH_SHORT).show();
//////        }
//////        if (myOnCompletionListener != null)
//////            myOnCompletionListener.onCompletion();
//        Log.i("start_play==","4");
//        release();
//        Log.i("start_play==","5");
//        if (model == PlayModel.LOOP) {
////            start();
//            ijkStart();
//            Log.i("start_play==","6");
//        } else {
//            if (audioList.size() != 1) {
//                Log.i("start_play==","7"+audioList.size());
//                //继续播放
//                if (model == PlayModel.ORDER) {
//                    Log.i("start_play==","8");
//                    //顺序播放
//                    if (position != audioList.size() - 1) {
//                        Log.i("start_play==","9");
//                        ++position;
//                        Log.i("start_play==","position=="+position);
////                        start();
//                        ijkStart();
//                        Log.i("start_play==","10");
//                    } else {
//                        Log.i("start_play==","11");
//                        position = 0;
//                        AudioPlayManager.getInstance().reset();
//                        Log.i("start_play==","12");
//                    }
//                } else if (model == PlayModel.RANDOM) {
//                    //随机播放
//                    int[] num = new int[audioList.size() - 1];
//                    for (int i = 0, j = 0; i < audioList.size(); i++) {
//                        if (i != position) {
//                            num[j] = i;
//                            j++;
//                        }
//                    }
//                    Random random = new Random();
//                    int max = num.length - 1;
//                    int min = 0;
//                    position = num[random.nextInt(max) % (max - min + 1) + min];
////                    start();
//                    ijkStart();
//                }
//                Log.i("start_play==","13");
//            }
//        }
//        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.AUDIO_COMPLET));
//    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        Log.i("start_play==","mp==14");
//        mp.seekTo(0);
//        mp.start();
//        Log.i("start_play==","mp==15");
//        CURRENT_STATE = SystemConstant.ON_PLAYING;
//        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
//    }

//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        Log.i("start_play==","onBufferingUpdate..."+mp.getDuration());
//        EventMsg msg = EventMsg.obtain(SystemConstant.ON_BUFFERING);
//        msg.setArg1(mp.getDuration());
//        msg.setArg2((int) (mp.getDuration() * percent * 0.01));
//        EventBus.getDefault().post(msg);
//    }

//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        LogUtils.e("what==" + what + ",extra==" + extra + ",url==" + url);
//        release();
////        start();
//        ijkStart();
////        mp.seekTo(0);
////        mp.reset();
//        return true;
//    }

    public void reset() {
        audioList = null;
        position = 0;
        model = PlayModel.ORDER;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
//        Log.i("start_play==", "onBufferingUpdate..." + mp.getDuration());
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_BUFFERING);
        msg.setArg1((int) mp.getDuration());
        msg.setArg2((int) (mp.getDuration() * percent * 0.01));
        EventBus.getDefault().post(msg);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

        Log.i("start_play==", "4");
        release();
        Log.i("start_play==", "5");
        if (model == PlayModel.LOOP) {
//            start();
            ijkStart();
            Log.i("start_play==", "6");
        } else {
            if (audioList.size() != 1) {
                Log.i("start_play==", "7" + audioList.size());
                //继续播放
                if (model == PlayModel.ORDER) {
                    Log.i("start_play==", "8");
                    //顺序播放
                    if (position != audioList.size() - 1) {
                        Log.i("start_play==", "9");
                        ++position;
                        Log.i("start_play==", "position==" + position);
//                        start();
                        ijkStart();
                        Log.i("start_play==", "10");
                    } else {
                        Log.i("start_play==", "11");
                        position = 0;
                        AudioPlayManager.getInstance().reset();
                        Log.i("start_play==", "12");
                    }
                } else if (model == PlayModel.RANDOM) {
                    //随机播放
                    int[] num = new int[audioList.size() - 1];
                    for (int i = 0, j = 0; i < audioList.size(); i++) {
                        if (i != position) {
                            num[j] = i;
                            j++;
                        }
                    }
                    Random random = new Random();
                    int max = num.length - 1;
                    int min = 0;
                    position = num[random.nextInt(max) % (max - min + 1) + min];
//                    start();
                    ijkStart();
                }
                Log.i("start_play==", "13");
            }
        }
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.AUDIO_COMPLET));
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        LogUtils.e("what==" + what + ",extra==" + extra + ",url==" + url);

        if (!isNetworkAvailable()) {
            Log.i("what==net", "what==net===" + "当前网络连接不稳定");
            ToastMaker.showShortToast("当前网络无连接或不稳定，请换个姿势重新连接");
        }
        release();
        ijkStart();
        return true;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public boolean isNetworkAvailable() {
        Context context = DemoApplication.applicationContext;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        Log.i("start_play==", "mp==14");
        mp.seekTo(0);
        mp.start();
        Log.i("start_play==", "mp==15");
        CURRENT_STATE = SystemConstant.ON_PLAYING;
        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_PLAYING));
    }


    /**
     * 播放音频实体
     */
    public static class AudioBean {
        private String name;
        private String id;
        private String url;
        private String filePath;
        private String authorName;
        private String authorId;
        private String coverImg;

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    /**
     * 播放模式
     */
    public enum PlayModel {
        ORDER,
        RANDOM,
        LOOP
    }

    /**
     * 播放类型
     */
    public enum PlayType {
        OneDayArticles,
        AlbumContent,
        MonthlyDBBean,
        MagazineExcerptBean,
        MozReadDetailsBean,
        IssuesDetailBean,
        ColumnAudioBean,
        ColumnArticleDetailsBean
    }
}
