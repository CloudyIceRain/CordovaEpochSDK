package com.cordova.plugin.CordovaEpochSDK;

import android.app.Activity;
import android.content.Context;

import org.apache.cordova.BuildConfig;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yinda.datasyc.http.SDKManage;

public class CordovaEpochSDK extends CordovaPlugin {
    public static String m_uploadUrl = "";
    private static int initEpochSDK(Context ctx, String _APP_ID, String _SECRET) {
        try{
            /**
             * @param context
             * @param appId         机构id
             * @param appSecret     机构秘钥
             * @param isDebug       是否打印日志, 默认为false不打印.
             */
            SDKManage.getInstance().init(ctx, _APP_ID, _SECRET, BuildConfig.DEBUG);
            return 0;//正常返回
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    private static int syncData(Context context, String borrowId, String userId, String phoneNum, String uploadUrl) {
        try{
            m_uploadUrl = uploadUrl;
            SDKManage.getInstance().SynData(borrowId, userId, phoneNum, uploadUrl);
            return 0;//正常返回
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext   callbackContext) throws JSONException {
        Activity activity = this.cordova.getActivity();//获取项目activity的方法
        Context context = this.cordova.getContext();//获取项目context的方法

        if (action.equals("initEpochSDK")) {
            String _APP_ID = args.getString(0);//如何获取传入的参数
            String _SECRET = args.getString(1);
            int vcode = initEpochSDK(context, _APP_ID, _SECRET);
            callbackContext.success(vcode);
            return true;
        }else if (action.equals("syncData")) {
            String borrowId = args.getString(0);//如何获取传入的参数
            String userId = args.getString(1);
            String phoneNum = args.getString(2);
            String uploadUrl = args.getString(3);
            int vcode = syncData(context, borrowId, userId, phoneNum, uploadUrl);
            callbackContext.success(vcode);
            return true;
        }
        return false;
    }
}