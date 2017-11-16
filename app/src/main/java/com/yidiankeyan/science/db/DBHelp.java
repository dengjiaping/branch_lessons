package com.yidiankeyan.science.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yidiankeyan.science.utils.LogUtils;

import static com.yidiankeyan.science.db.DBData.DOWN_LOAD_TABLE_NAME;

/**
 * Created by nby on 2016/7/26.
 * DB_VERSION:1对应的应用版本为14以下。
 * DB_VERSION:2对应的应用版本15，修改下载表结构，并且新增专栏下载，moz读书下载表。
 * DB_VERSION:3对应的应用版本24，增加用户表（为了缓存im的头像及显示名称）,环信置顶和置底表,联系人缓存表
 * DB_VERSION:4对应的应用版本29，增加群组表
 * DB_VERSION:5对应的应用版本?，修改群组表，增加成员个数。新增群组用户缓存
 * DB_VERSION:6增加月刊下载表
 */
public class DBHelp extends SQLiteOpenHelper {

    public DBHelp(Context context) {
        super(context, DBData.DB_NAME, null, DBData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.e("onCreate, version=" + db.getVersion());
        //专题
        String projectSql = "create table if not exists " + DBData.PROJECT_TABLE_NAME
                + " ( "
                + DBData.PROJECT_TABLE_ID + " integer primary key autoincrement , "
                + DBData.PROJECY_ID + " integer , "
                + DBData.PROJECT_SELECTED + " integer , "
                + DBData.PROJECT_NAME + " text "
                + ")";
        //轮播图
        String bannerSql = "create table if not exists " + DBData.BANNER_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.BANNER_TITLE + " text , "
                + DBData.BANNER_ID + " text , "
                + DBData.BANNER_DESC + " text , "
                + DBData.BANNER_IMG_URL + " text , "
                + DBData.BANNER_LINK_URL + " text , "
                + DBData.BANNER_POSITION_ID + " integer "
                + ")";
        //点赞
        String clickSql = "create table if not exists " + DBData.CLICK_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.IS_CLICK + " integer , "
                + DBData.CLICK_ID + " text "
                + ")";
        //下载
        String fileSql = "create table if not exists " + DOWN_LOAD_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.DOWN_LOAD_FILE_ID + " text , "
                + DBData.DOWN_LOAD_FILE_NAME + " text , "
                + DBData.DOWN_LOAD_FILE_ALBUM_NAME + " text , "
                + DBData.DOWN_LOAD_FILE_ALBUM_ID + " text , "
                + DBData.DOWN_LOAD_FILE_ALBUM_AVATAR + " text , "
                + DBData.DOWN_LOAD_FILE_AVATAR + " text , "
                + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                + DBData.DOWN_LOAD_FILE_PATH + " text , "
                + DBData.DOWN_LOAD_FILE_AUTHOR_NAME + " text , "
                + DBData.DOWN_LOAD_FILE_TYPE + " integer , "
                + DBData.DOWN_LOAD_FILE_CONTENT_NUM + " integer , "
                + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                + DBData.DOWN_LOAD_STATE + " integer "
                + ")";
        //期刊下载
        String fileIssuesSql = "create table if not exists " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.DOWN_LOAD_ISSUES_ID + " text , "
                + DBData.DOWN_LOAD_ISSUES_NAME + " text , "
                + DBData.DOWN_LOAD_COLUMN_NAME + " text , "
                + DBData.DOWN_LOAD_COLUMN_ID + " text , "
                + DBData.DOWN_LOAD_ISSUES_COVER + " text , "
                + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                + DBData.DOWN_LOAD_FILE_PATH + " text , "
                + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                + DBData.DOWN_LOAD_STATE + " integer "
                + ")";
        //书下载
        String fileBookSql = "create table if not exists " + DBData.DOWN_LOAD_BOOK_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.DOWN_LOAD_BOOK_ID + " text , "
                + DBData.DOWN_LOAD_BOOK_NAME + " text , "
                + DBData.DOWN_LOAD_BOOK_AUTHOR + " text , "
                + DBData.DOWN_LOAD_BOOK_COVER + " text , "
                + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                + DBData.DOWN_LOAD_FILE_PATH + " text , "
                + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                + DBData.DOWN_LOAD_STATE + " integer "
                + ")";
        //期刊下载
        String fileMonthlySql = "create table if not exists " + DBData.DOWN_LOAD_MONTHLY_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.DOWN_LOAD_MONTHLY_ID + " text , "
                + DBData.DOWN_LOAD_MONTHLY_NAME + " text , "
                + DBData.DOWN_LOAD_MONTHLY_COVER + " text , "
                + DBData.DOWN_LOAD_MONTHLY_SON_ID + " text , "
                + DBData.DOWN_LOAD_MONTHLY_SON_NAME + " text , "
                + DBData.DOWN_LOAD_MONTHLY_SON_COUNT + " text , "
                + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                + DBData.DOWN_LOAD_FILE_PATH + " text , "
                + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                + DBData.DOWN_LOAD_STATE + " integer "
                + ")";
        //搜索热词
        String searchSql = "create table if not exists " + DBData.SEARCH_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.SEARCH_CONTENT + " text "
                + ")";
        //快讯
        String newsSql = "create table if not exists " + DBData.NEWS_TABLE_NAME
                + " ( "
                + DBData.DB_ID + " integer primary key autoincrement , "
                + DBData.NEWS_ID + " text "
                + ")";
        db.execSQL(projectSql);
        db.execSQL(bannerSql);
        db.execSQL(clickSql);
        db.execSQL(fileSql);
        db.execSQL(fileIssuesSql);
        db.execSQL(fileBookSql);
        db.execSQL(searchSql);
        db.execSQL(newsSql);
        db.execSQL(fileMonthlySql);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.e("oldVersion=" + oldVersion + ",newVersion=" + newVersion);
        if (oldVersion < 7) {
            db.execSQL("DROP TABLE if exists " + DBData.PROJECT_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.BANNER_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.CLICK_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.DOWN_LOAD_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.SEARCH_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + DBData.NEWS_TABLE_NAME);
            db.execSQL("DROP TABLE if exists " + "user_cache_table_name");
            db.execSQL("DROP TABLE if exists " + "im_top_bottom_table_name");
            db.execSQL("DROP TABLE if exists " + "contact_table_name");
            db.execSQL("DROP TABLE if exists " + "im_group_table_name");
            db.execSQL("DROP TABLE if exists " + "im_group_member_table_name");
            //专题
            String projectSql = "create table if not exists " + DBData.PROJECT_TABLE_NAME
                    + " ( "
                    + DBData.PROJECT_TABLE_ID + " integer primary key autoincrement , "
                    + DBData.PROJECY_ID + " integer , "
                    + DBData.PROJECT_SELECTED + " integer , "
                    + DBData.PROJECT_NAME + " text "
                    + ")";
            //轮播图
            String bannerSql = "create table if not exists " + DBData.BANNER_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.BANNER_TITLE + " text , "
                    + DBData.BANNER_ID + " text , "
                    + DBData.BANNER_DESC + " text , "
                    + DBData.BANNER_IMG_URL + " text , "
                    + DBData.BANNER_LINK_URL + " text , "
                    + DBData.BANNER_POSITION_ID + " integer "
                    + ")";
            //点赞
            String clickSql = "create table if not exists " + DBData.CLICK_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.IS_CLICK + " integer , "
                    + DBData.CLICK_ID + " text "
                    + ")";
            //下载
            String fileSql = "create table if not exists " + DOWN_LOAD_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.DOWN_LOAD_FILE_ID + " text , "
                    + DBData.DOWN_LOAD_FILE_NAME + " text , "
                    + DBData.DOWN_LOAD_FILE_ALBUM_NAME + " text , "
                    + DBData.DOWN_LOAD_FILE_ALBUM_ID + " text , "
                    + DBData.DOWN_LOAD_FILE_ALBUM_AVATAR + " text , "
                    + DBData.DOWN_LOAD_FILE_AVATAR + " text , "
                    + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                    + DBData.DOWN_LOAD_FILE_PATH + " text , "
                    + DBData.DOWN_LOAD_FILE_AUTHOR_NAME + " text , "
                    + DBData.DOWN_LOAD_FILE_TYPE + " integer , "
                    + DBData.DOWN_LOAD_FILE_CONTENT_NUM + " integer , "
                    + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                    + DBData.DOWN_LOAD_STATE + " integer "
                    + ")";
            //期刊下载
            String fileIssuesSql = "create table if not exists " + DBData.DOWN_LOAD_ISSUES_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.DOWN_LOAD_ISSUES_ID + " text , "
                    + DBData.DOWN_LOAD_ISSUES_NAME + " text , "
                    + DBData.DOWN_LOAD_COLUMN_NAME + " text , "
                    + DBData.DOWN_LOAD_COLUMN_ID + " text , "
                    + DBData.DOWN_LOAD_ISSUES_COVER + " text , "
                    + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                    + DBData.DOWN_LOAD_FILE_PATH + " text , "
                    + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                    + DBData.DOWN_LOAD_STATE + " integer "
                    + ")";
            //书下载
            String fileBookSql = "create table if not exists " + DBData.DOWN_LOAD_BOOK_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.DOWN_LOAD_BOOK_ID + " text , "
                    + DBData.DOWN_LOAD_BOOK_NAME + " text , "
                    + DBData.DOWN_LOAD_BOOK_AUTHOR + " text , "
                    + DBData.DOWN_LOAD_BOOK_COVER + " text , "
                    + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                    + DBData.DOWN_LOAD_FILE_PATH + " text , "
                    + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                    + DBData.DOWN_LOAD_STATE + " integer "
                    + ")";
            //期刊下载
            String fileMonthlySql = "create table if not exists " + DBData.DOWN_LOAD_MONTHLY_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.DOWN_LOAD_MONTHLY_ID + " text , "
                    + DBData.DOWN_LOAD_MONTHLY_NAME + " text , "
                    + DBData.DOWN_LOAD_MONTHLY_COVER + " text , "
                    + DBData.DOWN_LOAD_MONTHLY_SON_ID + " text , "
                    + DBData.DOWN_LOAD_MONTHLY_SON_NAME + " text , "
                    + DBData.DOWN_LOAD_MONTHLY_SON_COUNT + " text , "
                    + DBData.DOWN_LOAD_FILE_MEDIA_URL + " text , "
                    + DBData.DOWN_LOAD_FILE_PATH + " text , "
                    + DBData.DOWN_LOAD_ALREADY_WATCH + " integer , "
                    + DBData.DOWN_LOAD_STATE + " integer "
                    + ")";
            //搜索热词
            String searchSql = "create table if not exists " + DBData.SEARCH_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.SEARCH_CONTENT + " text "
                    + ")";
            //快讯
            String newsSql = "create table if not exists " + DBData.NEWS_TABLE_NAME
                    + " ( "
                    + DBData.DB_ID + " integer primary key autoincrement , "
                    + DBData.NEWS_ID + " text "
                    + ")";
            db.execSQL(projectSql);
            db.execSQL(bannerSql);
            db.execSQL(clickSql);
            db.execSQL(fileSql);
            db.execSQL(fileIssuesSql);
            db.execSQL(fileBookSql);
            db.execSQL(searchSql);
            db.execSQL(newsSql);
            db.execSQL(fileMonthlySql);
        }
    }

}
