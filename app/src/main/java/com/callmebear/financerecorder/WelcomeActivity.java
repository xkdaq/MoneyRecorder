package com.callmebear.financerecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.callmebear.financerecorder.vest.MainVestActivity;
import com.callmebear.financerecorder.vest.utils.MD5Util;
import com.callmebear.financerecorder.vest.utils.MapUtil;
import com.callmebear.financerecorder.vest.utils.RetrofitHelper;
import com.callmebear.financerecorder.vest.utils.TypeEntity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends Activity {
    //private final long SPLASH_LENGTH = 1500;
    //Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//            handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转
//
//                public void run() {
//                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, SPLASH_LENGTH);//2秒后跳转至应用主界面MainActivity


        Map<String, Object> map = new HashMap<>();
        map.put("app_type", "android");
        map.put("app_version", "62");
        map.put("app_package", "com.soul.burke");
        map.put("channel", "app");
        map.put("guid", "b25c033ce0d5bbe3bfdc1c8a608f69ee");
        map.put("position", "");
        map.put("userid", "");
        map.put("version", "1");
        Map<String, Object> driverInfo = getSimpleDriverInfo();
        map.put("device_info", driverInfo);
        String timestamp = String.valueOf(System.currentTimeMillis());
        map.put("sign", getRequestSign(map, timestamp));
        map.put("timestamp", timestamp);
        String mapToString = getMapToString(map);
        Log.e("xuke", "--->" + mapToString);
        Call<TypeEntity> type = RetrofitHelper.getInstance().getType(mapToString);
        type.enqueue(new Callback<TypeEntity>() {
            @Override
            public void onResponse(Call<TypeEntity> call, Response<TypeEntity> response) {
                TypeEntity body = response.body();
                if (body != null) {
                    TypeEntity.DataBean data = body.getData();
                    if (data != null) {
                        int status = data.getStatus();
                        if (status == 0) {
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(WelcomeActivity.this, MainVestActivity.class));
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TypeEntity> call, Throwable t) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });

    }


    public static Map<String, Object> getSimpleDriverInfo() {
        Map<String, Object> mapClient = new HashMap<String, Object>();
        try {
            mapClient.put("and_id", "c43e77649e51d4ce");
            mapClient.put("gaid", "8508785c-f435-44c0-875d-f3c575700197");
            mapClient.put("imei", "869157024059774");
            mapClient.put("mac", "60:83:34:FE:2E:6A");
            mapClient.put("sn", "");
            mapClient.put("model", "EVA-TL00");
            mapClient.put("brand", "HUAWEI");
            mapClient.put("release", "8.0.0");
            mapClient.put("sdk_version", "26");
            mapClient.put("is_root", "0");
            mapClient.put("imsi", "510109872719107");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapClient;
    }

    public static String getMapToString(Map<String, Object> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getRequestSign(Map<String, Object> paraMap, String time) {
        try {
            Map<String, Object> data = MapUtil.sortMapByKey(paraMap);
            String jsonData = new Gson().toJson(data);
            String result = "zmaoniany@mjb@tao!cashcash96300" + "*|*" + jsonData + "@!@" + time;
            return MD5Util.encrypt(MD5Util.encrypt(result));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
