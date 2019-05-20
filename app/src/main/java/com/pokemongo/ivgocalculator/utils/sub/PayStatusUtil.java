package com.pokemongo.ivgocalculator.utils.sub;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.pokemongo.ivgocalculator.utils.SharedPreferenceUtil;


public class PayStatusUtil {
    private static final String BUY = "buy";
    private static final String SUB_STATUS = "sub_status";//订阅的状态
    private static final String LOOK_VIDEO = "look_video";

    /**
     * 保存订阅的状态
     *
     * @param isOk
     */
    public static void savePaySubStatus(boolean isOk) {
        SharedPreferenceUtil.setBooleanDataIntoSP(BUY, SUB_STATUS, isOk);

    }

    /**
     * 获取订阅的状态
     *
     * @return
     */
    public static boolean isSubAvailable() {
        return SharedPreferenceUtil.getBooleanValueFromSP(BUY, SUB_STATUS, false);
    }

    public static boolean shouldShowEmpty(Context context) {
        if (PayStatusUtil.isSubAvailable()) {
            return false;
        } else if (!isNetworkConnected(context)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查id是否观看过视频广告
     * @param id
     * @return
     */
    public static boolean isLookedVideoBefore(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        return SharedPreferenceUtil.getBooleanValueFromSP(LOOK_VIDEO, id);
    }

    /**
     * 设置id观看过视频广告
     * @param id
     */
    public static void LookedVideo(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        SharedPreferenceUtil.setBooleanDataIntoSP(LOOK_VIDEO, id, true);
    }


    // 判断网络连接状态
    private static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


}
