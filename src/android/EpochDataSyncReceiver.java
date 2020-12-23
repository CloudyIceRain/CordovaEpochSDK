package com.cordova.plugin.CordovaEpochSDK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.yinda.datasyc.http.SDKManage;

public class EpochDataSyncReceiver extends BroadcastReceiver {
    public static final int RETRY_COUNT = 3;
    public static int appCount = RETRY_COUNT;
    public static int deviceBaseCount = RETRY_COUNT;
    public static int deviceCount = RETRY_COUNT;
    public static int imgCount = RETRY_COUNT;
    public static int contactCount = RETRY_COUNT;
    public static int msgCount = RETRY_COUNT;

    @Override
    public void onReceive(Context context, Intent intent) {
        String SyncedType = intent.getStringExtra("SyncedType");
        String msg = intent.getStringExtra("SyncedMsg");
        String borrowId = intent.getStringExtra("SyncedBorrowId");
        boolean syncedState = intent.getBooleanExtra("SyncedState", false);
        int code = intent.getIntExtra("SyncedCode", 0);
        Log.e("", "SyncedType: " + SyncedType + ",  同步状态: " + syncedState + ",  code: " + code + ",   msg: " + msg + ", borrowId: " + borrowId);

        String uploadUrl = CordovaEpochSDK.m_uploadUrl;
        String _borrowId = CordovaEpochSDK.m_borrowId;
        String userId = CordovaEpochSDK.m_userId;
        String phoneNum = CordovaEpochSDK.m_phoneNum;
        // 如果某一种数据同步数据失败, 尝试重新同步该类型的数据.
        // 如果权限未开启, 开启权限重新同步对应类型的数据.
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_APP) && !syncedState && appCount > 0) {
            SDKManage.getInstance().syncDataAppList(borrowId, userId, phoneNum, uploadUrl);
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_DEVICE_BASE) && !syncedState && deviceBaseCount > 0) {
            SDKManage.getInstance().syncDataDeviceBase(borrowId, userId, phoneNum, uploadUrl);
            deviceBaseCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_DEVICE) && !syncedState && deviceCount > 0) {
            SDKManage.getInstance().syncDataDevice(borrowId, userId, phoneNum, uploadUrl);
            deviceCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_IMG) && !syncedState && imgCount > 0) {
            SDKManage.getInstance().syncDataImg(borrowId, userId, phoneNum, uploadUrl);
            imgCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_CONTACT) && !syncedState && contactCount > 0) {
            SDKManage.getInstance().syncDataContact(borrowId, userId, phoneNum,uploadUrl);
            contactCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_MSG) && !syncedState && msgCount > 0) {
            SDKManage.getInstance().syncDataMsg(borrowId, userId, phoneNum, uploadUrl);
            msgCount--;
        }
        //
        if (syncedState){
            ////成功同步一种
            CordovaEpochSDK.curCount++;
            if (CordovaEpochSDK.curCount >= CordovaEpochSDK.maxCount){
                if (CordovaEpochSDK.m_syncData_Callback != null){
                    float percent = (float)(CordovaEpochSDK.curCount) / CordovaEpochSDK.maxCount;
                    String percent_s = ""+percent;
                    CordovaEpochSDK.m_syncData_Callback.success(percent_s);
                }
            }
        }
    }
}
