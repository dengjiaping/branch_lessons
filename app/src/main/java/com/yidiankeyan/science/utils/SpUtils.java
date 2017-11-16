package com.yidiankeyan.science.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yidiankeyan.science.app.DemoApplication;

/**
 * Created by 北哥 on 2016/6/2.
 * SharedPreferences工具类
 */
public class SpUtils {
    /**
     * SharedPreferences名称
     */
    public static String STORE_NAME_SETTING = "setting";

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    private static SharedPreferences getSettingPreferences(Context context) {
        SharedPreferences setting = null;
        if (context == null) {
            setting = DemoApplication.applicationContext.getSharedPreferences(STORE_NAME_SETTING,
                    Context.MODE_PRIVATE);
        } else {
            setting = context.getSharedPreferences(STORE_NAME_SETTING,
                    Context.MODE_PRIVATE);
        }
        return setting;
    }

    /**
     * 获得String
     *
     * @param ctx
     * @param key
     * @return
     */
    public static String getStringSp(Context ctx, String key) {
        String value = null;
        getSettingPreferences(ctx).edit();
        if (getSettingPreferences(ctx).contains(key)) {
            value = getSettingPreferences(ctx).getString(key, "");
        }
        return value;
    }

    /**
     * 获得int
     *
     * @param ctx
     * @param key
     * @return
     */
    public static int getIntSp(Context ctx, String key) {
        int value = 0;
        getSettingPreferences(ctx).edit();
        if (getSettingPreferences(ctx).contains(key)) {
            value = getSettingPreferences(ctx).getInt(key, 0);
        }
        return value;
    }

    public static long getLongSp(Context ctx, String key) {
        long value = 0;
        getSettingPreferences(ctx).edit();
        if (getSettingPreferences(ctx).contains(key)) {
            value = getSettingPreferences(ctx).getLong(key, 0);
        }
        return value;
    }

    /**
     * put
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putStringSp(Context ctx, String key, String value) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.putString(key, "" + value);
        edit.commit();
    }

    /**
     * put
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putIntSp(Context ctx, String key, int value) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putLongSp(Context ctx, String key, long value) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static boolean getBooleanSp(Context ctx, String key) {
        boolean value = false;
        getSettingPreferences(ctx).edit();
        if (getSettingPreferences(ctx).contains(key)) {
            value = getSettingPreferences(ctx).getBoolean(key, false);
        }
        return value;
    }

    public static void putBooleanSp(Context ctx, String key, boolean value) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void removeSpKey(Context ctx, String key) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.remove(key);
        edit.commit();
    }

    public static void clearSp(Context ctx) {
        SharedPreferences.Editor edit = getSettingPreferences(ctx).edit();
        edit.clear();
        edit.commit();
    }


}
