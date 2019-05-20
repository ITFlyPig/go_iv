package com.pokemongo.ivgocalculator.utils.sub;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class GoogleBillingHelper {

    private static final String TAG = GoogleBillingHelper.class.getSimpleName();

    private GoogleBillingUtil googleBillingUtil;
    private String subId;//订阅的ID
    private Activity subActivity;//订阅的activity

    private GoogleBillingHelper(){
        EventBus.getDefault().register(this);
    }
    private static class Holder {
        private static GoogleBillingHelper googleBillingHelper = new GoogleBillingHelper();

    }

    public static GoogleBillingHelper getInstance() {
        return Holder.googleBillingHelper;
    }

    /**
     * 设置订阅的id
     * @param subId
     */
    public void setSubId(String subId) {
        this.subId = subId;
    }

    /**
     * 设置订阅的activity，必须得
     * @param subActivity
     */
    public void setSubActivity(Activity subActivity) {
        this.subActivity = subActivity;
    }

    private MyOnPurchaseFinishedListener mOnPurchaseFinishedListener = new MyOnPurchaseFinishedListener();//购买回调接口
    private MyOnQueryFinishedListener mOnQueryFinishedListener = new MyOnQueryFinishedListener();//查询回调接口
    private MyOnStartSetupFinishedListener mOnStartSetupFinishedListener = new MyOnStartSetupFinishedListener();//启动结果回调接口



    //查询商品信息回调接口
    private class MyOnQueryFinishedListener implements GoogleBillingUtil.OnQueryFinishedListener {
        @Override
        public void onQuerySuccess(String skuType, List<SkuDetails> list) {
            Log.d(TAG, "查询商品信息回调接口 onQuerySuccess");
            if (list != null) {
                for (SkuDetails skuDetails : list) {
                    String log = "";
                    if (skuType == BillingClient.SkuType.INAPP) {
                        log += "内购的商品:";
                    } else if (skuType == BillingClient.SkuType.SUBS) {
                        log += "订阅的商品:";
                    }
                    Log.d(TAG, log + skuDetails.getTitle() + " 序列号：" + skuDetails.getSku() + " 价格：" + skuDetails.getPrice());
                }
            }


            //查询成功，返回商品列表，
            //skuDetails.getPrice()获得价格(文本)
            //skuDetails.getType()获得类型 sub或者inapp,因为sub和inapp的查询结果都走这里，所以需要判断。
            //googleBillingUtil.getSubsPositionBySku(skuDetails.getSku())获得当前subs sku的序号
            //googleBillingUtil.getInAppPositionBySku(skuDetails.getSku())获得当前inapp suk的序号
        }

        @Override
        public void onQueryFail(int responseCode) {
            Log.d(TAG, "查询商品信息回调接口 onQueryFail");
            //查询失败

        }

        @Override
        public void onQueryError() {
            //查询错误
            Log.d(TAG, "查询商品信息回调接口 onQueryError");
        }
    }

    //服务初始化结果回调接口
    private class MyOnStartSetupFinishedListener implements GoogleBillingUtil.OnStartSetupFinishedListener {
        @Override
        public void onSetupSuccess() {
            Log.d(TAG, "服务初始化结果回调接口 onSetupSuccess");


            Log.d(TAG, "开始查询已经购买商品");
            List<Purchase> inapps = googleBillingUtil.queryPurchasesInApp();
            if (inapps != null) {
                for (Purchase inapp : inapps) {
                    Log.d(TAG, "已经购买的商品：" + inapp.getSku());
                }
            }
            Log.d(TAG, "开始查询已经订阅商品");
            List<Purchase> subs = googleBillingUtil.queryPurchasesSubs();
            if (subs != null) {
                for (Purchase sub : subs) {
                    Log.d(TAG, "已经订阅的商品：" + sub.getSku());
                }
            }

            int size = googleBillingUtil.getPurchasesSizeSubs();
            Log.d(TAG, "获取有效订阅的数量：" + size);
            handleQueryResult(size);


//            Toast.makeText(BaseApplication.getInstance(), "DownPro 有效订阅的数量：" + size + ":::" + (subs == null ? 0 :subs.size()), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onSetupFail(int responseCode) {
            Log.d(TAG, "服务初始化结果回调接口 onSetupFail");
            handleQueryResult(-1);

        }

        @Override
        public void onSetupError() {
            Log.d(TAG, "服务初始化结果回调接口 onSetupError");
            handleQueryResult(-1);

        }
    }

    //购买商品回调接口
    private class MyOnPurchaseFinishedListener implements GoogleBillingUtil.OnPurchaseFinishedListener {
        @Override
        public void onPurchaseSuccess(List<Purchase> list) {
            //内购或者订阅成功,可以通过purchase.getSku()获取suk进而来判断是哪个商品
            Log.d("wyl", "购买商品回调接口 onPurchaseSuccess");
            if (list != null && list.size() > 0) {

                //订阅成功，取消弹出框
                EventBus.getDefault().post(new PayModel(Event.HAVE_SUB));


                for (Purchase purchase : list) {
                    String sku = purchase.getSku();
                    if (!TextUtils.isEmpty(sku) && TextUtils.equals(sku, subId)) {//订阅商品成功，记录
                        PayStatusUtil.savePaySubStatus(true);
                    }

                    String log = "";
                    if (googleBillingUtil.handlePurchase(purchase)) {
                        log = log + "商品序列号：" + purchase.getSku();
                        Log.d("wyl", " 尚明" + "购买的商品通过验证：" + purchase.getSignature());
                    } else {
                        log = log + "商品序列号：" + purchase.getSku();
                        Log.d("wyl", "购买的商品未通过验证：" + purchase.getSignature());
                    }
                    Log.d("wyl", "购买或者订阅成功：" + log);
                }
            }
        }

        @Override
        public void onPurchaseFail(int responseCode) {
            Log.d("wyl", "购买商品回调接口 onPurchaseFail：" + responseCode);

        }

        @Override
        public void onPurchaseError() {
            Log.d("wyl", "购买商品回调接口 onPurchaseError");

        }
    }


    /**
     * 处理查询的结果
     *
     * @param size 大于0 表示查询到的数量 -1表示查询失败
     */
    private void handleQueryResult(int size) {
        Log.d(TAG, "获取有效订阅的数量：" + size);
        PayStatusUtil.savePaySubStatus(size > 0 ? true : false);

        if (PayStatusUtil.isSubAvailable()) {//取消弹出弹窗
            EventBus.getDefault().post(new PayModel(Event.HAVE_SUB));
        } else {//开始订阅
            if (size == 0 && !TextUtils.isEmpty(subId)) {
                googleBillingUtil.purchaseSubs(subActivity, subId);
            }
        }

    }

    /**
     * 初始化谷歌内购
     */
    private void initGoogleBilling() {
        GoogleBillingUtil.cleanListener();
        googleBillingUtil = GoogleBillingUtil.getInstance()
                .setOnPurchaseFinishedListener(mOnPurchaseFinishedListener)
                .setOnQueryFinishedListener(mOnQueryFinishedListener)
                .setOnStartSetupFinishedListener(mOnStartSetupFinishedListener)
                .build();
    }


    public static class PayModel{
            public int code;
            public String op;

            public PayModel(int code, String op) {
                this.code = code;
                this.op = op;
            }

            public PayModel(int code) {
                this.code = code;
            }
    }


    public interface Event{
        int SHOW_DIALOG = 1;//显示弹窗
        int HAVE_SUB = 2;  //已经存在订阅
        int START_SUB = 6; //开始订阅
        int QUERY_SUB = 3;//查询订阅
        int SUB = 4;//订阅
        int QUERY_SUB_AND_BUY = 5;//查询订阅和够买

    }


    @Subscribe
    public void onSubEvent(PayModel eventModel) {
        if (eventModel == null) {
            return;

        }
        if (eventModel.code == Event.QUERY_SUB) {// 查询是否有订阅
            if (googleBillingUtil != null && googleBillingUtil.isReady()) {
                int size = googleBillingUtil.getPurchasesSizeSubs();
                handleQueryResult(size);
            } else {//走正常的流程
                initGoogleBilling();
            }
        } else if (eventModel.code == Event.QUERY_SUB_AND_BUY) {//先查询订阅是否有效，无效的话开始订阅，有效的话取消弹窗
            if (googleBillingUtil != null && googleBillingUtil.isReady()) {
                int size = googleBillingUtil.getPurchasesSizeSubs();
                handleQueryResult(size);
            } else {//走正常的流程
                initGoogleBilling();
            }
        } else if (eventModel.code == Event.HAVE_SUB) {//取消订阅的弹框

        } else if (eventModel.code == Event.SHOW_DIALOG) {//显示订阅的弹框
        }

    }

    /**
     * 开始订阅
     * @param subId
     */
    public void startSub(String subId) {
        if (TextUtils.isEmpty(subId)) {
            return;
        }
        this.subId = subId;

        if (googleBillingUtil != null && googleBillingUtil.isReady()) {
            int size = googleBillingUtil.getPurchasesSizeSubs();
            handleQueryResult(size);
        } else {//走正常的流程
            initGoogleBilling();
        }

    }

}
