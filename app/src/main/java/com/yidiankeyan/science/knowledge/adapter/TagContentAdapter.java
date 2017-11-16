package com.yidiankeyan.science.knowledge.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.HotNewsBean;
import com.yidiankeyan.science.knowledge.activity.MozDetailActivity;
import com.yidiankeyan.science.knowledge.activity.TagDetailActivity;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

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
 * Created by nby on 2017/7/16.
 * 作用：
 */

public class TagContentAdapter extends RecyclerAdapter<HotNewsBean> {
    public TagContentAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<HotNewsBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<HotNewsBean> {

        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvTag;
        private TextView tvDate;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_tag_content);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            imgAvatar = (ImageView) findViewById(R.id.img_avatar);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvTag = (TextView) findViewById(R.id.tv_tag);
            tvDate = (TextView) findViewById(R.id.tv_date);
        }

        @Override
        public void setData(final HotNewsBean data) {
            super.setData(data);
            Glide.with(getContext()).load(Util.getImgUrl(data.getCoverimgurl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(imgAvatar);
            tvName.setText(data.getName());
            tvTag.setText(data.getTagModels() != null && data.getTagModels().size() != 0 ? data.getTagModels().get(0).getName() : data.getMozname());
            tvDate.setText(new SimpleDateFormat("MM月dd日").format(new Date(data.getCreatetime())));

            tvTag.setText(data.getTagModels() == null || data.getTagModels().size() == 0 ? data.getMozname() : "#" + data.getTagModels().get(0).getName());

            tvTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = data.getTagModels() == null || data.getTagModels().size() == 0 ? data.getMozid() : data.getTagModels().get(0).getId();
                    int type = data.getTagModels() == null || data.getTagModels().size() == 0 ? 1 : 2;
                    if (type == 1) {
                        Intent intent = new Intent(getContext(), MozDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("type", type);
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), TagDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("type", type);
                        getContext().startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onItemViewClick(HotNewsBean data) {
            super.onItemViewClick(data);
            Intent intent;
            switch (data.getType()) {
                case 1:
                    intent = new Intent(getContext(), ImgTxtAlbumActivity.class);
                    intent.putExtra("id", data.getId());
                    getContext().startActivity(intent);
                    break;
                case 2:
                    AlbumContent audio = new AlbumContent(null);
                    audio.setArticlename(data.getName());
                    audio.setArticleid(data.getId());
                    audio.setLastupdatetime(data.getCreatetime());
                    audio.setArticletype(2);
                    audio.setMediaurl(data.getMediaurl());
                    audio.setCoverimgurl(data.getCoverimgurl());
                    AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(data.getId());
                    if (content != null)
                        audio.setFilePath(content.getFilePath());
                    ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                    listItem.add(audio);
                    intent = new Intent(getContext(), AudioAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItem);
                    intent.putExtra("position", 0);
                    intent.putExtra("single", true);
                    intent.putExtra("id", data.getId());
                    getContext().startActivity(intent);
                    break;
                case 3:
                    AlbumContent video = new AlbumContent(null);
                    video.setArticlename(data.getName());
                    video.setArticleid(data.getId());
                    video.setLastupdatetime(data.getCreatetime());
                    video.setArticletype(2);
                    video.setMediaurl(data.getMediaurl());
                    video.setCoverimgurl(data.getCoverimgurl());
                    AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(data.getId());
                    if (contentVideo != null)
                        video.setFilePath(contentVideo.getFilePath());
                    ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                    listItemVideo.add(video);
                    intent = new Intent(getContext(), VideoAlbumActivity.class);
                    intent.putParcelableArrayListExtra("list", listItemVideo);
                    intent.putExtra("position", 0);
                    intent.putExtra("id", data.getId());
                    getContext().startActivity(intent);
                    break;
            }
        }
    }
}
