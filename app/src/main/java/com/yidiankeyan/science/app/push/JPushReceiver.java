package com.yidiankeyan.science.app.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.LoginActivity;
import com.yidiankeyan.science.app.activity.MainLoginActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.acitivity.NewsFlashActivity;
import com.yidiankeyan.science.information.acitivity.PreviewColumnContentActivity;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SharedPreferencesConstant;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 * <p/>
 * 点击消息后续动作：
 * <p>
 * 1) status=10001:用户在其他设备登录
 * <p>
 * 2) type = 20001:跳到专辑
 * <p>
 * 3) type = 20002:跳到期刊
 * <p>
 * 4) type = 20003:跳到快讯
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            String jsonData = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Map<String, String> map = (Map<String, String>) GsonUtils
                    .json2Map(jsonData);
            String status = map.get("status");
            if (TextUtils.equals("10001", status)) {
                Util.logout();
                Toast.makeText(DemoApplication.applicationContext, "您的账号在其他设备登录", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
            }
            String type = map.get("type");

            if (TextUtils.equals("20004", type)) {
                if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                    int count = SpUtils.getIntSp(DemoApplication.applicationContext, SharedPreferencesConstant.SYSTEM_NOTIFY_COUNT);
                    count++;
                    SpUtils.putIntSp(DemoApplication.applicationContext, SharedPreferencesConstant.SYSTEM_NOTIFY_COUNT, count);
                } else {
                    //将通知消息放到会话列表
//                    SpUtils.putIntSp(DemoApplication.applicationContext, SharedPreferencesConstant.SYSTEM_NOTIFY_COUNT, 0);
//                    EMMessage emMessage = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//                    EMTextMessageBody body = new EMTextMessageBody("系统消息");
//                    emMessage.setFrom("1102");
//                    emMessage.addBody(body);
//                    emMessage.setChatType(EMMessage.ChatType.Chat);
//                    EMClient.getInstance().chatManager().sendMessage(emMessage);
//                    if (DemoApplication.getInstance().activityExisted(MainActivity.class)) {
//                        EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_MESSAGE_RECEIVED));
//                    }
                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");
            String jsonData = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Map<String, String> map = (Map<String, String>) GsonUtils
                    .json2Map(jsonData);
            String status = map.get("status");
            if (TextUtils.equals("10001", status)) {
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else {
                String type = map.get("type");
                if (TextUtils.equals("20001", type)) {
                    //专辑
                    Intent i = new Intent(context, AlbumDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("albumId", map.get("id"));
                    context.startActivity(i);
                } else if (TextUtils.equals("20002", type)) {
                    //期刊
                    Intent i = new Intent(context, PreviewColumnContentActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("id", map.get("id"));
                    context.startActivity(i);
                } else if (TextUtils.equals("20003", type)) {
                    Intent i = new Intent(context, NewsFlashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (TextUtils.equals("20004", type)) {
                    if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                        ToastMaker.showShortToast("请先登录");
                        Intent i = new Intent(context, MainLoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else {
//                        Intent i = new Intent(context, SystemNotifyActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(i);
                    }
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[JPushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }
}
