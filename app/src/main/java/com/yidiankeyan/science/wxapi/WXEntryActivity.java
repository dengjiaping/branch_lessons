package com.yidiankeyan.science.wxapi;


import android.content.Intent;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
//    public void onResp(BaseResp resp) {
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                String code = ((SendAuth.Resp) resp).code; //即为所需的code
//                Intent intent = new Intent();
//                intent.setAction("action.wx.login.code");
//                intent.putExtra("code", code);
//                sendBroadcast(intent);
//                break;
//        }
//    }


    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    String code = ((SendAuth.Resp) resp).code; //即为所需的code
                    Intent intent = new Intent();
                    intent.setAction("action.wx.login.code");
                    intent.putExtra("code", code);
                    sendBroadcast(intent);
                }
                break;
        }
    }
}
