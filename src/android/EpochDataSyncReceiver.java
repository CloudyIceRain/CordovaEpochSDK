package com.cordova.plugin.CordovaEpochSDK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.demo.sdk.SdkCfg;
import com.orhanobut.logger.Logger;
import com.yinda.datasyc.http.SDKManage;

import static com.demo.sdk.App.RETRY_COUNT;
import static com.demo.sdk.App.appCount;
import static com.demo.sdk.App.contactCount;
import static com.demo.sdk.App.deviceBaseCount;
import static com.demo.sdk.App.deviceCount;
import static com.demo.sdk.App.imgCount;
import static com.demo.sdk.App.msgCount;

public class EpochDataSyncReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String SyncedType = intent.getStringExtra("SyncedType");
        String msg = intent.getStringExtra("SyncedMsg");
        String borrowId = intent.getStringExtra("SyncedBorrowId");
        boolean syncedState = intent.getBooleanExtra("SyncedState", false);
        int code = intent.getIntExtra("SyncedCode", 0);
        Logger.e("SyncedType: " + SyncedType + ",  同步状态: " + syncedState + ",  code: " + code + ",   msg: " + msg + ", borrowId: " + borrowId);

        // 如果某一种数据同步数据失败, 尝试重新同步该类型的数据.
        // 如果权限未开启, 开启权限重新同步对应类型的数据.
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_APP) && !syncedState && appCount > 0) {
            SDKManage.getInstance().syncDataAppList(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_DEVICE_BASE) && !syncedState && deviceBaseCount > 0) {
            SDKManage.getInstance().syncDataDeviceBase(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
            deviceBaseCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_DEVICE) && !syncedState && deviceCount > 0) {
            SDKManage.getInstance().syncDataDevice(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
            deviceCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_IMG) && !syncedState && imgCount > 0) {
            SDKManage.getInstance().syncDataImg(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
            imgCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_CONTACT) && !syncedState && contactCount > 0) {
            SDKManage.getInstance().syncDataContact(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
            contactCount--;
        }
        if (TextUtils.equals(SyncedType, SDKManage.TYPE_MSG) && !syncedState && msgCount > 0) {
            SDKManage.getInstance().syncDataMsg(borrowId, "userId_103", "phoneNum_123", SdkCfg.POST_URL);
            msgCount--;
        }
    }
}
