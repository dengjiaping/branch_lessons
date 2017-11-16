package com.yidiankeyan.science.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by zn nby
 * On 2016/04/27 10:49
 */
public class JCVideoPlayerStandardShowTitleAfterFullscreen extends JCVideoPlayerStandard {

    private String mediaUrl;
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }


    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

//    @Override
//    public boolean setUp(String url, Object... objects) {
//        if (super.setUp(url, objects)) {
//            mediaUrl = url;
//            if (mIfCurrentIsFullscreen) {
//                titleTextView.setVisibility(View.VISIBLE);
//            } else {
//                titleTextView.setVisibility(View.INVISIBLE);
//            }
//            return true;
//        }
//        return false;
//    }


    public JCVideoPlayerStandardShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public void setUp(String url, int screen, Object... objects) {
//        super.setUp(url, screen, objects);
//        mediaUrl = url;
//        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//            titleTextView.setVisibility(View.VISIBLE);
//        } else {
//            titleTextView.setVisibility(View.INVISIBLE);
//        }
//    }

    @Override
    public boolean setUp(String url, int screen, Map<String, String> mapHeadData, Object... objects) {
        mediaUrl = url;
        return super.setUp(url, screen, mapHeadData, objects);
    }

    @Override
    public boolean setUp(String url, int screen, Object... objects) {
        mediaUrl = url;
        return super.setUp(url, screen, objects);
    }
}
