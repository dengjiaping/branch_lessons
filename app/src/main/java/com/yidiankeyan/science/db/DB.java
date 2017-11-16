package com.yidiankeyan.science.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.entity.AlbumContent;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.ColumnAudioBean;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.information.entity.MagazineExcerptBean;
import com.yidiankeyan.science.information.entity.MonthlyDetailsBean;
import com.yidiankeyan.science.information.entity.MozReadDetailsBean;
import com.yidiankeyan.science.utils.FileUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.view.drag.bean.ChannelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nby on 2016/7/26.
 */
public class DB {
    Context context;
    SQLiteDatabase db;
    private static DB myDB;

    public DB(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        DBHelp dbHelp = new DBHelp(context);
        db = dbHelp.getWritableDatabase();
    }

    public DB() {
    }

    public static DB getInstance(Context context) {
        if (myDB == null) {
            synchronized (DB.class) {//同步代码块
                if (myDB == null) {
                    myDB = new DB();//创建一个新的实例
                    DBHelp dbHelp = new DBHelp(DemoApplication.applicationContext);
                    myDB.context = DemoApplication.applicationContext;
                    myDB.db = dbHelp.getWritableDatabase();
                }
            }
        }
        return myDB;
    }

    /**
     * 插入专题
     *
     * @param projects 专题列表
     */
    public void insertProject(List<ChannelItem> projects) {
        for (ChannelItem project : projects) {
            ContentValues values = new ContentValues();
            values.put(DBData.PROJECY_ID, project.getSubjectid());
            values.put(DBData.PROJECT_SELECTED, 0);
            values.put(DBData.PROJECT_NAME, project.getSubjectname());
            long rowId = db.insert(DBData.PROJECT_TABLE_NAME, null, values);
            if (rowId > 0) {
                Log.e("db message :", "==专题数据插入成功====");
            } else {
                Log.e("db message :", "==专题数据插入失败====");
            }
        }
        //db.close();
    }

    /**
     * 查询专题列表
     */
    public List<ChannelItem> queryProject() {
        List<ChannelItem> list = new ArrayList<>();
        String sql = "select * from " + DBData.PROJECT_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ChannelItem project = new ChannelItem(cursor.getInt(cursor.getColumnIndex(DBData.PROJECY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBData.PROJECT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBData.PROJECT_SELECTED)));
            list.add(project);
        }
        cursor.close();
        //db.close();
        return list;
    }

    /**
     * 查询已订专辑
     *
     * @return
     */
    public List<ChannelItem> queryOrderSub() {
        List<ChannelItem> list = new ArrayList<>();
        String sql = "select * from " + DBData.PROJECT_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ChannelItem project = new ChannelItem(cursor.getInt(cursor.getColumnIndex(DBData.PROJECY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBData.PROJECT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBData.PROJECT_SELECTED)));
            if (project.getSelected() == 1)
                list.add(project);
        }
        cursor.close();
        //db.close();
        return list;
    }

    /**
     * 查询专辑
     */
    public ChannelItem querySub(int id) {
        String sql = "select * from " + DBData.PROJECT_TABLE_NAME + " where " + DBData.PROJECT_TABLE_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        ChannelItem state = null;
        while (cursor.moveToNext()) {
            state = new ChannelItem(cursor.getInt(cursor.getColumnIndex(DBData.PROJECY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBData.PROJECT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBData.PROJECT_SELECTED)));
        }
        cursor.close();
        //db.close();
        return state;
    }

    /**
     * 更新专题选择状态
     */
    public void updataProject(ChannelItem project, int selected) {
        ContentValues values = new ContentValues();
        values.put(DBData.PROJECT_SELECTED, selected);
        int rowId = db.update(DBData.PROJECT_TABLE_NAME, values, DBData.PROJECY_ID + " = ?", new String[]{String.valueOf(project.getId())});
        if (rowId > 0) {
            Log.e("db message :", "====" + "更新专题成功");
        } else {
            Log.e("db message :", "====" + "更新专题失败");
        }
        //db.close();
    }

    public void deleteAllProject() {
        int rowId = db.delete(DBData.PROJECT_TABLE_NAME, null, null);
        if (rowId > 0) {
            Log.e("====", "==专题删除成功==");
        } else
            Log.e("====", "==专题删除失败==");
        //db.close();
    }

    /**
     * 插入轮播图
     */
    public void insertBannerList(List<BannerBean> bannerList) {
        int rowId = db.delete(DBData.BANNER_TABLE_NAME, null, null);
        if (rowId > 0) {
            Log.e("====", "==轮播图删除成功==");
        } else
            Log.e("====", "==轮播图删除失败==");
        for (BannerBean banner : bannerList) {
            ContentValues values = new ContentValues();
            values.put(DBData.BANNER_DESC, banner.getDesc());
            values.put(DBData.BANNER_ID, banner.getId());
            values.put(DBData.BANNER_IMG_URL, SystemConstant.ALI_CLOUD + banner.getImgurl());
            values.put(DBData.BANNER_LINK_URL, banner.getLinkurl());
            values.put(DBData.BANNER_POSITION_ID, banner.getPositionid());
            values.put(DBData.BANNER_TITLE, banner.getTitle());
            long rowId2 = db.insert(DBData.BANNER_TABLE_NAME, null, values);
            if (rowId2 > 0) {
                Log.e("db message :", "==轮播图数据插入成功====");
            } else {
                Log.e("db message :", "==轮播图数据插入失败====");
            }
        }
        //db.close();
    }

    /**
     * 查询轮播图
     */
    public List<BannerBean> queryBanner() {
        List<BannerBean> list = new ArrayList<>();
        String sql = "select * from " + DBData.BANNER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BannerBean banner = new BannerBean();
            banner.setDesc(cursor.getString(cursor.getColumnIndex(DBData.BANNER_DESC)));
            banner.setId(cursor.getString(cursor.getColumnIndex(DBData.BANNER_ID)));
            banner.setImgurl(cursor.getString(cursor.getColumnIndex(DBData.BANNER_IMG_URL)));
            banner.setLinkurl(cursor.getString(cursor.getColumnIndex(DBData.BANNER_LINK_URL)));
            banner.setPositionid(cursor.getInt(cursor.getColumnIndex(DBData.BANNER_POSITION_ID)));
            banner.setTitle(cursor.getString(cursor.getColumnIndex(DBData.BANNER_TITLE)));
            list.add(banner);
        }
        cursor.close();
        //db.close();
        return list;
    }

    /**
     * 是否点赞
     */
    public boolean isClick(String id) {
        String sql = "select * from " + DBData.CLICK_TABLE_NAME + " where " + DBData.CLICK_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        boolean flag = false;
        while (cursor.moveToNext()) {
            flag = cursor.getInt(cursor.getColumnIndex(DBData.IS_CLICK)) == 1;
        }
        cursor.close();
        //db.close();
        return flag;
    }

    /**
     * 是否存在点赞表里
     */
    public boolean existedClickTable(String id) {
        String sql = "select * from " + DBData.CLICK_TABLE_NAME + " where " + DBData.CLICK_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        boolean flag = false;
        while (cursor.moveToNext()) {
            flag = true;
        }
        cursor.close();
        //db.close();
        return flag;
    }

    public void updataClick(int i, String id) {
        ContentValues values = new ContentValues();
        values.put(DBData.IS_CLICK, i);
        int rowId = db.update(DBData.CLICK_TABLE_NAME, values, DBData.CLICK_ID + " = ?", new String[]{String.valueOf(id)});
        if (rowId > 0) {
            Log.e("db message :", "====" + "更新成功");
        } else {
            Log.e("db message :", "====" + "更新点赞失败");
        }
        //db.close();
    }

    public void deleteClick(String id) {
        db.delete(DBData.CLICK_TABLE_NAME, DBData.CLICK_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    public void deleteAllClick() {
        int rowId = db.delete(DBData.CLICK_TABLE_NAME, null, null);
        if (rowId > 0) {
            Log.e("====", "==点赞删除成功==");
        } else
            Log.e("====", "==点赞删除失败==");
        //db.close();
    }

    public void insertClick(String id) {
        ContentValues values = new ContentValues();
        values.put(DBData.CLICK_ID, id);
        values.put(DBData.IS_CLICK, 1);
        long rowId2 = db.insert(DBData.CLICK_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==点赞数据插入成功====");
        } else {
            Log.e("db message :", "==点赞数据插入失败====");
        }
        //db.close();
    }

    public void insertDownloadFile(AlbumContent content) {
        db.delete(DBData.DOWN_LOAD_TABLE_NAME, DBData.DOWN_LOAD_FILE_ID + " = ?",
                new String[]{String.valueOf(content.getArticleid())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_FILE_ID, content.getArticleid());
        values.put(DBData.DOWN_LOAD_FILE_NAME, content.getArticlename());
        values.put(DBData.DOWN_LOAD_FILE_ALBUM_NAME, content.getAlbumName());
        values.put(DBData.DOWN_LOAD_FILE_AUTHOR_NAME, content.getAuthorname());
        values.put(DBData.DOWN_LOAD_FILE_ALBUM_ID, content.getAlbumId());
        values.put(DBData.DOWN_LOAD_FILE_TYPE, content.getArticletype());
        values.put(DBData.DOWN_LOAD_FILE_ALBUM_AVATAR, content.getAlbumAvatar());
        values.put(DBData.DOWN_LOAD_FILE_AVATAR, content.getCoverimgurl());
        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, content.getMediaurl());
        values.put(DBData.DOWN_LOAD_STATE, content.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_CONTENT_NUM, content.getContentNum());
        values.put(DBData.DOWN_LOAD_FILE_PATH, content.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    public void insertDownloadFile(IssuesDetailBean issues) {
        db.delete(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, DBData.DOWN_LOAD_ISSUES_ID + " = ?",
                new String[]{String.valueOf(issues.getId())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_ISSUES_ID, issues.getId());
        values.put(DBData.DOWN_LOAD_ISSUES_NAME, issues.getName());
        values.put(DBData.DOWN_LOAD_COLUMN_ID, issues.getColumnid());
        values.put(DBData.DOWN_LOAD_COLUMN_NAME, issues.getColumnname());
        values.put(DBData.DOWN_LOAD_ISSUES_COVER, issues.getCoverimg());
        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, issues.getAudiourl());
        values.put(DBData.DOWN_LOAD_STATE, issues.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_PATH, issues.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    public void insertDownloadFile(ColumnAudioBean column) {
        db.delete(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, DBData.DOWN_LOAD_ISSUES_ID + " = ?",
                new String[]{String.valueOf(column.getId())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_ISSUES_ID, column.getId());
        values.put(DBData.DOWN_LOAD_ISSUES_NAME, column.getAudioName());
        values.put(DBData.DOWN_LOAD_COLUMN_ID, column.getColumnId());
        values.put(DBData.DOWN_LOAD_COLUMN_NAME, column.getColumnName());
        values.put(DBData.DOWN_LOAD_ISSUES_COVER, column.getAudioImg());
        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, column.getAudioUrl());
        values.put(DBData.DOWN_LOAD_STATE, column.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_PATH, column.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    /**
     * @param id
     * @param state    0:下载中或下载失败；1:下载成功
     * @param fileType 文件类型；1:专辑；2:专栏；3:书；4:期刊
     */
    public void updataDownloadFileState(String id, int state, int fileType) {
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_STATE, state);
        int rowId = 0;
        switch (fileType) {
            case 1:
                rowId = db.update(DBData.DOWN_LOAD_TABLE_NAME, values, DBData.DOWN_LOAD_FILE_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 2:
                rowId = db.update(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, values, DBData.DOWN_LOAD_ISSUES_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 3:
                rowId = db.update(DBData.DOWN_LOAD_BOOK_TABLE_NAME, values, DBData.DOWN_LOAD_BOOK_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 4:
                rowId = db.update(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, values, DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ?", new String[]{String.valueOf(id)});
                break;
        }
        if (rowId > 0) {
            Log.e("db message :", "====" + "下载更新成功");
        } else {
            Log.e("db message :", "====" + "下载更新失败");
        }
        //db.close();
    }

    /**
     * 由未观看更新为已观看
     *
     * @param id        需要更新的id
     * @param tableCode 1:专辑下载;2:专栏下载;3:书下载
     */
    public void updateDownloadFileAlready(String id, int tableCode) {
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 1);
        int rowId = 0;
        switch (tableCode) {
            case 1:
                rowId = db.update(DBData.DOWN_LOAD_TABLE_NAME, values, DBData.DOWN_LOAD_FILE_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 2:
                rowId = db.update(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, values, DBData.DOWN_LOAD_ISSUES_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 3:
                rowId = db.update(DBData.DOWN_LOAD_BOOK_TABLE_NAME, values, DBData.DOWN_LOAD_BOOK_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case 4:
                rowId = db.update(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, values, DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ?", new String[]{String.valueOf(id)});
                break;
        }
        if (rowId > 0) {
            Log.e("db message :", "====" + "下载更新成功");
        } else {
            Log.e("db message :", "====" + "下载更新失败");
        }
        //db.close();
    }

    public AlbumContent queryDownloadFile(String id) {
        AlbumContent albumContent = null;
        String sql = "select * from " + DBData.DOWN_LOAD_TABLE_NAME + " where " + DBData.DOWN_LOAD_FILE_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            albumContent = new AlbumContent(null);
            albumContent.setArticleid(id);
            albumContent.setArticlename(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_NAME)));
            albumContent.setAlbumName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_NAME)));
            albumContent.setAlbumId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_ID)));
            albumContent.setArticletype(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_TYPE)));
            albumContent.setAlbumAvatar(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_AVATAR)));
            albumContent.setCoverimgurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_AVATAR)));
            albumContent.setMediaurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
            albumContent.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
            albumContent.setContentNum(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_CONTENT_NUM)));
            albumContent.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
            albumContent.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
            albumContent.setFileSize(FileUtils.getFileOrFilesSize(albumContent.getFilePath(), FileUtils.SIZETYPE_MB) + "");
        }
        cursor.close();
        //db.close();
        return albumContent;
    }

    public IssuesDetailBean queryIssuesDownloadFile(String id) {
        IssuesDetailBean issues = null;
        String sql = "select * from " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME + " where " + DBData.DOWN_LOAD_ISSUES_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            issues = new IssuesDetailBean();
            issues.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_ID)));
            issues.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_NAME)));
            issues.setColumnid(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_COLUMN_ID)));
            issues.setCoverimg(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_COVER)));
            issues.setColumnname(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_COLUMN_NAME)));
            issues.setAudiourl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
            issues.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
            issues.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
            issues.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
            issues.setFileSize(FileUtils.getFileOrFilesSize(issues.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
        }
        cursor.close();
        //db.close();
        return issues;
    }


    public MozReadDetailsBean queryBookDownloadFile(String id) {
        MozReadDetailsBean issues = null;
        String sql = "select * from " + DBData.DOWN_LOAD_BOOK_TABLE_NAME + " where " + DBData.DOWN_LOAD_BOOK_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            issues = new MozReadDetailsBean();
            issues.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_ID)));
            issues.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_NAME)));
            issues.setAuthor(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_AUTHOR)));
            issues.setCoverimgurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_COVER)));
            issues.setMediaurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
            issues.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
            issues.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
            issues.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
            issues.setFileSize(FileUtils.getFileOrFilesSize(issues.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
        }
        cursor.close();
        //db.close();
        return issues;
    }

    public void deleteDownloadIssues(String id) {
        db.delete(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, DBData.DOWN_LOAD_ISSUES_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    /**
     * 更新期刊阅读状态
     */
    public void updataIssuesWatch(String id, int selected) {
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, selected);
        int rowId = db.update(DBData.DOWN_LOAD_ISSUES_TABLE_NAME, values, DBData.DOWN_LOAD_ISSUES_ID + " = ?", new String[]{String.valueOf(id)});
        //db.close();
    }

    public List<IssuesDetailBean> queryIssuesDownloadFiles() {
        List<IssuesDetailBean> list = new ArrayList<>();
        String sql = "select * from " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)) == 1 && Util.fileExisted(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)))) {
                IssuesDetailBean issues = new IssuesDetailBean();
                issues.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_ID)));
                if (!list.contains(issues)) {
                    issues.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_NAME)));
                    issues.setColumnid(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_COLUMN_ID)));
                    issues.setCoverimg(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_COVER)));
                    issues.setColumnname(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_COLUMN_NAME)));
                    issues.setAudiourl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
                    issues.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
                    issues.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
                    issues.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
                    issues.setFileSize(FileUtils.getFileOrFilesSize(issues.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
                    list.add(issues);
                }
            }
        }
        cursor.close();
        //db.close();
        return list;
    }


    public ColumnAudioBean queryColumnDownloadFile(String id) {
        ColumnAudioBean issues = null;
        String sql = "select * from " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME + " where " + DBData.DOWN_LOAD_ISSUES_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            issues = new ColumnAudioBean();
            issues.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_ID)));
            issues.setAudioName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_NAME)));
            issues.setAudioImg(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_ISSUES_COVER)));
            issues.setAudioUrl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
            issues.setColumnName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_COLUMN_NAME)));
            issues.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
            issues.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
            issues.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
            issues.setFileSize(FileUtils.getFileOrFilesSize(issues.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
        }
        cursor.close();
        //db.close();
        return issues;
    }

    public List<AlbumContent> queryDownloadFiles() {
        List<AlbumContent> list = new ArrayList<>();
        String sql = "select * from " + DBData.DOWN_LOAD_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)) == 1 && Util.fileExisted(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)))) {
                AlbumContent albumContent = new AlbumContent(null);
                albumContent.setArticleid(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ID)));
                if (!list.contains(albumContent)) {
                    albumContent.setArticlename(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_NAME)));
                    albumContent.setAlbumName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_NAME)));
                    albumContent.setAlbumId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_ID)));
                    albumContent.setArticletype(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_TYPE)));
                    albumContent.setAlbumAvatar(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_ALBUM_AVATAR)));
                    albumContent.setCoverimgurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_AVATAR)));
                    albumContent.setMediaurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
                    albumContent.setContentNum(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_CONTENT_NUM)));
                    albumContent.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
                    albumContent.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
                    albumContent.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
                    albumContent.setFileSize(FileUtils.getFileOrFilesSize(albumContent.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
                    list.add(albumContent);
                }
            }
        }
        cursor.close();
        //db.close();
        return list;
    }

    public void deleteDownloadFile(String id) {
        int rowId = db.delete(DBData.DOWN_LOAD_TABLE_NAME, DBData.DOWN_LOAD_FILE_ID + " = ?",
                new String[]{String.valueOf(id)});
        LogUtils.e("rowId==" + rowId);
        //db.close();
    }

    public void insertSearch(String content) {
        ContentValues values = new ContentValues();
        values.put(DBData.SEARCH_CONTENT, content);
        long rowId2 = db.insert(DBData.SEARCH_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==搜索历史数据插入成功====");
        } else {
            Log.e("db message :", "==搜索历史数据插入失败====");
        }
        //db.close();
    }

    /**
     * 查询历史记录
     *
     * @return
     */
    public List<String> querySearch() {
        List<String> list = new ArrayList<>();
        String sql = "select * from " + DBData.SEARCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String project = cursor.getString(cursor.getColumnIndex(DBData.SEARCH_CONTENT));
            list.add(project);
        }
        cursor.close();
        //db.close();
        return list;
    }


    public void deleteAllSearch() {
        int rowId = db.delete(DBData.SEARCH_TABLE_NAME, null, null);
        if (rowId > 0) {
            Log.e("====", "==历史搜索删除成功==");
        } else
            Log.e("====", "==历史搜索删除失败==");
        //db.close();
    }

    public boolean newsIsLooked(String id) {
        String sql = "select * from " + DBData.NEWS_TABLE_NAME + " where " + DBData.NEWS_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        boolean b = false;
        while (cursor.moveToNext()) {
            b = true;
        }
        cursor.close();
        //db.close();
        return b;
    }

    public void updataNews(String id) {
        String sql = "select * from " + DBData.NEWS_TABLE_NAME + " where " + DBData.NEWS_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        boolean b = false;
        while (cursor.moveToNext()) {
            b = true;
        }
        cursor.close();
        if (!b) {
            ContentValues values = new ContentValues();
            values.put(DBData.NEWS_ID, id);
            long rowId = db.insert(DBData.NEWS_TABLE_NAME, null, values);
            if (rowId > 0) {
                Log.e("db message :", "====" + "更新快讯成功");
            } else {
                Log.e("db message :", "====" + "更新快讯失败");
            }
        }
        //db.close();
    }

    public void insertDownloadFile(MozReadDetailsBean detailsBean) {
        db.delete(DBData.DOWN_LOAD_BOOK_TABLE_NAME, DBData.DOWN_LOAD_BOOK_ID + " = ?",
                new String[]{String.valueOf(detailsBean.getId())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_BOOK_ID, detailsBean.getId());
        values.put(DBData.DOWN_LOAD_BOOK_NAME, detailsBean.getName());
        values.put(DBData.DOWN_LOAD_BOOK_AUTHOR, detailsBean.getAuthor());
        values.put(DBData.DOWN_LOAD_BOOK_COVER, detailsBean.getCoverimgurl());
        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, detailsBean.getMediaurl());
        values.put(DBData.DOWN_LOAD_STATE, detailsBean.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_PATH, detailsBean.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_BOOK_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    public void insertDownloadFile(MonthlyDetailsBean.MonthlyDBBean detailsBean) {
        db.delete(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ?",
                new String[]{String.valueOf(detailsBean.getId())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_MONTHLY_SON_ID, detailsBean.getId());
        values.put(DBData.DOWN_LOAD_MONTHLY_SON_NAME, detailsBean.getName());
        values.put(DBData.DOWN_LOAD_MONTHLY_COVER, detailsBean.getCoverimg());
        values.put(DBData.DOWN_LOAD_MONTHLY_ID, detailsBean.getId());
        values.put(DBData.DOWN_LOAD_MONTHLY_NAME, detailsBean.getName());

        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, detailsBean.getAudiourl());
        values.put(DBData.DOWN_LOAD_STATE, detailsBean.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_PATH, detailsBean.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    public void insertDownloadFile(MagazineExcerptBean detailsBean) {
        db.delete(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ?",
                new String[]{String.valueOf(detailsBean.getId())});
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_MONTHLY_SON_ID, detailsBean.getId());
        values.put(DBData.DOWN_LOAD_MONTHLY_SON_NAME, detailsBean.getName());
        values.put(DBData.DOWN_LOAD_MONTHLY_COVER, detailsBean.getCoverimg());
        values.put(DBData.DOWN_LOAD_MONTHLY_ID, detailsBean.getMonthlyId());
        values.put(DBData.DOWN_LOAD_MONTHLY_NAME, detailsBean.getMonthlyName());

        values.put(DBData.DOWN_LOAD_FILE_MEDIA_URL, detailsBean.getAudiourl());
        values.put(DBData.DOWN_LOAD_STATE, detailsBean.getDownloadState());
        values.put(DBData.DOWN_LOAD_FILE_PATH, detailsBean.getFilePath());
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, 0);
        long rowId2 = db.insert(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, null, values);
        if (rowId2 > 0) {
            Log.e("db message :", "==下载数据插入成功====");
        } else {
            Log.e("db message :", "==下载数据插入失败====");
        }
        //db.close();
    }

    public void deleteDownloadBook(String id) {
        db.delete(DBData.DOWN_LOAD_BOOK_TABLE_NAME, DBData.DOWN_LOAD_BOOK_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    public void deleteDownloadMagazine(String id) {
        db.delete(DBData.DOWN_LOAD_MONTHLY_TABLE_NAME, DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    /**
     * 更新书籍阅读状态
     */
    public void updataBookWatch(String id, int selected) {
        ContentValues values = new ContentValues();
        values.put(DBData.DOWN_LOAD_ALREADY_WATCH, selected);
        int rowId = db.update(DBData.DOWN_LOAD_BOOK_TABLE_NAME, values, DBData.DOWN_LOAD_BOOK_ID + " = ?", new String[]{String.valueOf(id)});
        //db.close();
    }

    public MagazineExcerptBean queryMagazineDownloadFile(String id) {
        MagazineExcerptBean magazine = null;
        String sql = "select * from " + DBData.DOWN_LOAD_MONTHLY_TABLE_NAME + " where " + DBData.DOWN_LOAD_MONTHLY_SON_ID + " = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            magazine = new MagazineExcerptBean();
            magazine.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_SON_ID)));
            magazine.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_SON_NAME)));
            magazine.setCoverimg(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_COVER)));
            magazine.setMonthlyId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_ID)));
            magazine.setMonthlyName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_NAME)));

            magazine.setAudiourl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
            magazine.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
            magazine.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
            magazine.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
            magazine.setFileSize(FileUtils.getFileOrFilesSize(magazine.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
        }
        cursor.close();
        //db.close();
        return magazine;
    }


    public List<MagazineExcerptBean> queryMagazineDownloadFiles() {
        List<MagazineExcerptBean> list = new ArrayList<>();
        String sql = "select * from " + DBData.DOWN_LOAD_MONTHLY_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)) == 1 && Util.fileExisted(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)))) {
                MagazineExcerptBean magazine = new MagazineExcerptBean();
                magazine.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_SON_ID)));
                magazine.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_SON_NAME)));
                magazine.setCoverimg(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_COVER)));
                magazine.setMonthlyId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_ID)));
                magazine.setMonthlyName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_MONTHLY_NAME)));

                magazine.setAudiourl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
                magazine.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
                magazine.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
                magazine.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
                magazine.setFileSize(FileUtils.getFileOrFilesSize(magazine.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
                list.add(magazine);
            }
        }
        return list;
    }

    public List<MozReadDetailsBean> queryBookDownloadFiles() {
        List<MozReadDetailsBean> list = new ArrayList<>();
        String sql = "select * from " + DBData.DOWN_LOAD_BOOK_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)) == 1 && Util.fileExisted(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)))) {
                MozReadDetailsBean issues = new MozReadDetailsBean();
                issues.setId(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_ID)));
                if (!list.contains(issues)) {
                    issues.setName(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_NAME)));
                    if (issues.getName().startsWith("类型：杂志")) {
                        continue;
                    }
                    issues.setAuthor(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_AUTHOR)));
                    issues.setCoverimgurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_BOOK_COVER)));
                    issues.setMediaurl(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_MEDIA_URL)));
                    issues.setFilePath(cursor.getString(cursor.getColumnIndex(DBData.DOWN_LOAD_FILE_PATH)));
                    issues.setAlreadyWatch(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_ALREADY_WATCH)));
                    issues.setDownloadState(cursor.getInt(cursor.getColumnIndex(DBData.DOWN_LOAD_STATE)));
                    issues.setFileSize(FileUtils.getFileOrFilesSize(issues.getFilePath(), FileUtils.SIZETYPE_MB) + "M");
                    list.add(issues);
                }
            }
        }
        cursor.close();
        //db.close();
        return list;
    }

}
