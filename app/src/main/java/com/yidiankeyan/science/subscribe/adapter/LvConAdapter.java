package com.yidiankeyan.science.subscribe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专辑内容适配
 */
public class LvConAdapter extends BaseAdapter implements View.OnClickListener {

    private List<AlbumContent> list;
    private Context context;
    private boolean remove;
    private boolean localFile;

    public LvConAdapter(List<AlbumContent> list, Context context) {
        this.context = context;
        this.list = list;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_headline, parent, false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.txt_albtitle);
            viewHolder.img_left = (ImageView) convertView.findViewById(R.id.img_left);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.tvReadCount = (TextView) convertView.findViewById(R.id.tv_read_count);
            viewHolder.txtDianzan = (TextView) convertView.findViewById(R.id.txt_dianzan);
            viewHolder.imgPlayer = (ImageView) convertView.findViewById(R.id.img_player);
            viewHolder.imgDownload = (ImageView) convertView.findViewById(R.id.img_download);
            viewHolder.txtAlbtitleSecond = (TextView) convertView.findViewById(R.id.txt_albtitle_second);
            viewHolder.llBottomContainer = (AutoLinearLayout) convertView.findViewById(R.id.ll_bottom_container);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(list.get(position).getArticleid())) {
            viewHolder.tv_title.setText(list.get(position).getArticlename());
            if (localFile) {
                viewHolder.txtAlbtitleSecond.setText(list.get(position).getArticlename());
                viewHolder.txtAlbtitleSecond.setVisibility(View.VISIBLE);
                viewHolder.tv_title.setVisibility(View.GONE);
                viewHolder.llBottomContainer.setVisibility(View.GONE);
            }
        } else {
            Glide.with(context).load(Util.getImgUrl(list.get(position).getCoverimgurl()))
                    .error(R.drawable.icon_hotload_failed)
                    .placeholder(R.drawable.icon_hotload_failed)
                    .into(viewHolder.img_left);
            viewHolder.tv_title.setText(list.get(position).getArticlename());
            viewHolder.txt_time.setText(TimeUtils.questionCreateDuration(list.get(position).getLastupdatetime()));
            viewHolder.tvReadCount.setText("浏览量" + list.get(position).getReadnum());
//            viewHolder.txtDianzan.setText("点赞量" + list.get(position).getPraisenum());
            if (localFile) {
                viewHolder.txtAlbtitleSecond.setText(list.get(position).getArticlename());
                viewHolder.txtAlbtitleSecond.setVisibility(View.VISIBLE);
                viewHolder.tv_title.setVisibility(View.GONE);
                viewHolder.llBottomContainer.setVisibility(View.GONE);
            }
        }
        if (list.get(position).getArticletype() != 2)
            //非音频专辑不可显示播放按钮
            viewHolder.imgPlayer.setVisibility(View.GONE);
        else {
            viewHolder.imgPlayer.setVisibility(View.VISIBLE);
            if ((list.get(position).getArticleid()).equals(AudioPlayManager.getInstance().getCurrId()) && (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING
                    || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PREPARE)) {
                //正在播放的音频与当前音频一致并且正在播放的音频处于播放中或准备中，按钮显示暂停
                viewHolder.imgPlayer.setImageResource(R.drawable.audio_click_stop);
            } else {
                viewHolder.imgPlayer.setImageResource(R.drawable.audio_click_play);
            }
            viewHolder.imgPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(AudioPlayManager.getInstance().getCurrId()) || !(list.get(position).getArticleid()).equals(AudioPlayManager.getInstance().getCurrId())) {
                        toHttpGetUrl(list.get(position));
                        //当前点击的条目不在播放状态
                        AudioPlayManager.getInstance().init(list, position, AudioPlayManager.PlayModel.ORDER);
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                    } else {
                        switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                            case SystemConstant.ON_PREPARE:
                                AudioPlayManager.getInstance().release();
                                viewHolder.imgPlayer.setImageResource(R.drawable.audio_click_play);
                                break;
                            case SystemConstant.ON_PAUSE:
                                AudioPlayManager.getInstance().ijkStart();
                                DemoApplication.isPlay = true;
                                viewHolder.imgPlayer.setImageResource(R.drawable.audio_click_stop);
                                break;
                            case SystemConstant.ON_PLAYING:
                                AudioPlayManager.getInstance().pause();
                                DemoApplication.isPlay = false;
                                viewHolder.imgPlayer.setImageResource(R.drawable.audio_click_play);
                                break;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
        if (list.get(position).getArticletype() == 1) {
            //图文专辑不显示下载按钮
            viewHolder.imgDownload.setVisibility(View.GONE);
        } else {
            viewHolder.imgDownload.setVisibility(View.VISIBLE);
            viewHolder.imgDownload.setTag(position);
            viewHolder.imgDownload.setOnClickListener(this);
            if (Util.fileExisted(list.get(position).getFilePath())) {
                //下载完成后在专辑详情的内容显示下载完毕，在点藏显示可删除
                if (remove) {
                    viewHolder.imgDownload.setImageResource(R.drawable.trash);
                } else {
                    viewHolder.imgDownload.setImageResource(R.drawable.dowcomplete);
                }
            } else {
                viewHolder.imgDownload.setImageResource(R.drawable.by_download_no);
            }
        }
        return convertView;
    }

    /**
     * 发送已经看过的请求
     *
     * @param content
     */
    private void toHttpGetUrl(final AlbumContent content) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", content.getArticleid());
        HttpUtil.post(context, SystemConstant.URL + SystemConstant.GET_CONTENT_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.img_download:
                if (!Util.hintLogin((BaseActivity) context))
                    return;
                position = (int) v.getTag();
                //查询该内容是否下载过
                AlbumContent albumContent = DB.getInstance(DemoApplication.applicationContext).queryDownloadFile(list.get(position).getArticleid());
                if (list.get(position).getMediaurl() == null || TextUtils.isEmpty(list.get(position).getMediaurl()) || "null".equals(list.get(position).getMediaurl())) {
                    Toast.makeText(context, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                    return;
                }
                String suffixes = list.get(position).getMediaurl().substring(list.get(position).getMediaurl().lastIndexOf("."));
                //未下载过,开始下载
                if (albumContent == null) {
                    try {
                        list.get(position).setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + list.get(position).getArticlename() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(list.get(position));
                        DownloadManager.getInstance().startDownload(
                                SystemConstant.ACCESS_IMG_HOST + list.get(position).getMediaurl()
                                , list.get(position).getArticlename()
                                , Util.getSDCardPath() + "/MOZDownloads/" + list.get(position).getArticlename() + suffixes
                                , true
                                , false
                                , null
                                , list.get(position).getContentNum()
                                , list.get(position).getArticleid());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (albumContent.getDownloadState() == 0) {
                        Toast.makeText(context, "下载中...", Toast.LENGTH_SHORT).show();
                        //代表该内容已下载完成
                    } else if (albumContent.getDownloadState() == 1) {
                        //判断本地中是否存在该文件
                        if (Util.fileExisted(albumContent.getFilePath())) {
                            if (remove)
                                showDialog(position);
                            //本地不存在该内容，开始下载，同时将数据库的标识置为0，代表正在下载
                        } else {
                            list.get(position).setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + list.get(position).getArticlename() + suffixes);
                            try {
                                DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(list.get(position).getArticleid(), 0, 1);
                                DownloadManager.getInstance().startDownload(
                                        SystemConstant.ACCESS_IMG_HOST + list.get(position).getMediaurl()
                                        , list.get(position).getArticlename()
                                        , Util.getSDCardPath() + "/MOZDownloads/" + list.get(position).getArticlename() + suffixes, true, false, null, list.get(position).getContentNum(), list.get(position).getArticleid());
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    /**
     * 在点藏可以弹出确定删除对话框
     *
     * @param position
     */
    private void showDialog(final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定删除吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //正在播放的数据与要删除的数据一致，需要先释放
                if (list.get(position).getArticleid().equals(AudioPlayManager.getInstance().getCurrId())) {
                    AudioPlayManager.getInstance().release();
                }
                //删除数据库记录并且删除本地文件
                DB.getInstance(DemoApplication.applicationContext).deleteDownloadFile(list.get(position).getArticleid());
                try {
                    new File(list.get(position).getFilePath()).delete();
                } catch (Exception e) {
                }
                if (remove) {
                    list.remove(position);
                }
                notifyDataSetChanged();
            }
        });
        builder.show();
    }

    public void setLocalFile(boolean localFile) {
        this.localFile = localFile;
    }

    class ViewHolder {
        private ImageView img_left;
        private TextView tv_title;
        private TextView txt_time;
        private TextView tvReadCount;
        private TextView txtDianzan;
        private ImageView imgPlayer;
        private ImageView imgDownload;
        private TextView txtAlbtitleSecond;
        private AutoLinearLayout llBottomContainer;
    }
}
