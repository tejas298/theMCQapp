package com.tejas.root.finalapp.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by root on 29/12/18.
 */

public class Order {
    private final String MID = "MID";
    private final String MID_KEY = "KEY";
    private final String ORDER_ID = "ORDER_ID";
    private final String CUST_ID = "CUST_ID";
    private final String MOBILE_NO = "MOBILE_NO";
    private final String EMAIL = "EMAIL";
    private final String CHANNEL_ID = "CHANNEL_ID";
    private final String TXN_AMOUNT = "TXN_AMOUNT";
    private final String WEBSITE = "WEBSITE";
    private final String INDUSTRY_TYPE_ID = "INDUSTRY_TYPE_ID";
    private final String CALLBACK_URL = "CALLBACK_URL";
    private final String CHECKSUMHASH = "CHECKSUMHASH";

    private final String MID_VALUE = "KliBYa57251774348652";
    private final String MID_KEY_VALUE = "kxksomJK#g03FJ@s";
    private String ORDER_ID_VALUE = "order1";
    private String CUST_ID_VALUE = "cust123";
    private String MOBILE_NO_VALUE = "7777777777";
    private String EMAIL_VALUE = "username@emailprovider.com";
    private String CHANNEL_ID_VALUE = "WAP";
    private String TXN_AMOUNT_VALUE = "100.12";
    private String WEBSITE_VALUE = "WEBSTAGING";
    private String INDUSTRY_TYPE_ID_VALUE = "Retail";
    private String CALLBACK_URL_VALUE = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1";
    private String CHECKSUMHASH_VALUE = "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=";

    Context context;
    PaytmPGService Service;


    public Order(Context context) {
        this.context = context;
        Service = PaytmPGService.getStagingService();

    }

    public HashMap<String, String> putOrder(String mid, String orderId, String custId, String mobileNo,
                                        String email, String channelId, String txnAmount, String website,
                                        String industryTypeId, String callbackUrl, String checksumhash) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(MID, mid);
        paramMap.put(ORDER_ID, orderId);
        paramMap.put(CUST_ID, custId);
        paramMap.put(MOBILE_NO, mobileNo);
        paramMap.put(EMAIL, email);
        paramMap.put(CHANNEL_ID, channelId);
        paramMap.put(TXN_AMOUNT, txnAmount);
        paramMap.put(WEBSITE, website);
        paramMap.put(INDUSTRY_TYPE_ID, industryTypeId);
        paramMap.put(CALLBACK_URL, callbackUrl);
        paramMap.put(CHECKSUMHASH, checksumhash);

        return paramMap;
    }

    public void intiatePayment(){

        String checku = getCheckSum();

        HashMap<String,String> map = putOrder(MID_VALUE,ORDER_ID_VALUE,CUST_ID_VALUE,MOBILE_NO_VALUE,
                EMAIL_VALUE,CHANNEL_ID_VALUE,TXN_AMOUNT_VALUE,WEBSITE_VALUE,INDUSTRY_TYPE_ID_VALUE,
                CALLBACK_URL_VALUE,checku);
        PaytmOrder order = new PaytmOrder(map);

        Service.initialize(order,null);

        Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(context, "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
            }
            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(context, "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
            public void networkNotAvailable() {
                Toast.makeText(context, "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(context, "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(context, "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

            }
            public void onBackPressedCancelTransaction() {
                Toast.makeText(context, "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(context, "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startOrder(String amount){

    }

    //Checks whether primary data is present or not
    //if not proceed to provide the detail
    public void checkPrimaryData(){
        boolean check = false;

        SharedPreferences pref = context.getSharedPreferences("login",Context.MODE_PRIVATE);

        String mobile = pref.getString("mobile",null);
        String email = pref.getString("email",null);
        String cust_id = pref.getString("id",null);

    }


    //generate checksum
    public String getCheckSum(){
        TreeMap<String,String> treeMap = preChecksum(MID_VALUE,ORDER_ID_VALUE,CUST_ID_VALUE,MOBILE_NO_VALUE,
                EMAIL_VALUE,CHANNEL_ID_VALUE,TXN_AMOUNT_VALUE,WEBSITE_VALUE,INDUSTRY_TYPE_ID_VALUE,
                CALLBACK_URL_VALUE);
        String checksum = "";
        try {
            checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MID_KEY_VALUE, treeMap);
            //checksum = CheckSumServiceHelper.getCheckSumServiceHelper().
        } catch (Exception e) {
            checksum = null;
            e.printStackTrace();
        }
        Log.d("checksum",checksum);
        return checksum;
    }

    public TreeMap<String, String> preChecksum(String mid, String orderId, String custId, String mobileNo,
                                            String email, String channelId, String txnAmount, String website,
                                            String industryTypeId, String callbackUrl) {
        TreeMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put(MID, mid);
        paramMap.put(ORDER_ID, orderId);
        paramMap.put(CUST_ID, custId);
        paramMap.put(MOBILE_NO, mobileNo);
        paramMap.put(EMAIL, email);
        paramMap.put(CHANNEL_ID, channelId);
        paramMap.put(TXN_AMOUNT, txnAmount);
        paramMap.put(WEBSITE, website);
        paramMap.put(INDUSTRY_TYPE_ID, industryTypeId);
        paramMap.put(CALLBACK_URL, callbackUrl);

        return paramMap;
    }
}


