package com.yidiankeyan.science.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.my.entity.MyAccountArticleBean;
import com.yidiankeyan.science.subscribe.activity.AudioAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.ImgTxtAlbumActivity;
import com.yidiankeyan.science.subscribe.activity.VideoAlbumActivity;
import com.yidiankeyan.science.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/5/19 0019.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                -全部文章
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class SingleArticleAdapter extends BaseAdapter {
    private List<MyAccountArticleBean> mDatas;
    private Context context;
    private Intent intent;

    public SingleArticleAdapter(Context context, List<MyAccountArticleBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article_home, parent, false);
            viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            viewHolder.txtAlbumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.album_title);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_creat_time);
            viewHolder.imgTypeItem = (ImageView) convertView.findViewById(R.id.img_type_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Glide.with(context).load(Util.getImgUrl(mDatas.get(position).getCoverimgurl()))
                .placeholder(R.drawable.icon_hotload_failed)
                .error(R.drawable.icon_hotload_failed)
                .into(viewHolder.imgAdd);
        viewHolder.txtAlbumName.setText(mDatas.get(position).getAlbumName());
        viewHolder.txtName.setText(mDatas.get(position).getArticleName());
        viewHolder.txtTime.setText(mDatas.get(position).getCreatetime().replace(".0", ""));
        final String type = mDatas.get(position).getType();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDatas.get(position).getType()) {
                    case "1":
                        intent = new Intent(context, ImgTxtAlbumActivity.class);
                        intent.putExtra("id", mDatas.get(position).getId());
                        context.startActivity(intent);
                        break;
                    case "2":
                        AlbumContent audio = new AlbumContent(null);
                        audio.setArticlename(mDatas.get(position).getArticleName());
                        audio.setArticleid(mDatas.get(position).getId());
//                        audio.setLastupdatetime(Long.parseLong(mDatas.get(position).getCreatetime().replace(".0", "")));
                        audio.setArticletype(2);
                        audio.setMediaurl(mDatas.get(position).getMediaurl());
                        audio.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent content = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
                        if (content != null)
                            audio.setFilePath(content.getFilePath());
                        ArrayList<AlbumContent> listItem = new ArrayList<AlbumContent>();
                        listItem.add(audio);
                        intent = new Intent(context, AudioAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItem);
                        intent.putExtra("position", 0);
                        intent.putExtra("single", true);
                        intent.putExtra("id", mDatas.get(position).getId());
                        context.startActivity(intent);
                        break;
                    case "3":
                        AlbumContent video = new AlbumContent(null);
                        video.setArticlename(mDatas.get(position).getArticleName());
                        video.setArticleid(mDatas.get(position).getId());
//                        video.setLastupdatetime(Long.valueOf(mDatas.get(position).getCreatetime().replace(".0", "")));
                        video.setArticletype(2);
                        video.setMediaurl(mDatas.get(position).getMediaurl());
                        video.setCoverimgurl(mDatas.get(position).getCoverimgurl());
                        AlbumContent contentVideo = new DB(DemoApplication.applicationContext).queryDownloadFile(mDatas.get(position).getId());
                        if (contentVideo != null)
                            video.setFilePath(contentVideo.getFilePath());
                        ArrayList<AlbumContent> listItemVideo = new ArrayList<AlbumContent>();
                        listItemVideo.add(video);
                        intent = new Intent(context, VideoAlbumActivity.class);
                        intent.putParcelableArrayListExtra("list", listItemVideo);
                        intent.putExtra("position", 0);
                        intent.putExtra("id", mDatas.get(0).getId());
                        context.startActivity(intent);
                        break;

                }
            }
        });


        switch (type) {
            case "1":
                viewHolder.imgTypeItem.setImageResource(R.drawable.icon_rec_tuwen);
                break;
            case "2":
                viewHolder.imgTypeItem.setImageResource(R.drawable.icon_rec_audio);
                break;
            case "3":
                viewHolder.imgTypeItem.setImageResource(R.drawable.icon_rec_video);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imgAdd;
        ImageView imgTypeItem;
        TextView txtAlbumName;
        TextView txtName;
        TextView txtTime;
    }
}
