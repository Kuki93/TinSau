package com.example.helpme.mvpandroid.module.weather;

import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.entity.weather.Weather;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Created by helpme on 2018/1/23.
 * @Description weather 业务逻辑相关实现类
 */
public class WeatherModelImpl implements WeatherContract.WeatherModel {
    
    private OkHttpClient client;
    private Gson gson;
    public CacheControl FORCE_CACHE;
    
    public WeatherModelImpl() {
        client = new OkHttpClient();
        FORCE_CACHE = new CacheControl.Builder().noCache().build();
        // Register an adapter to manage the date types as long values
        gson = new Gson();
    }
    
    @Override
    public void weather(String url, final boolean isLocation, final long cityId, final WeatherContract
            .WeatherCallBcak callBcak) throws Exception {
        final Request request = new Request.Builder().url(url).cacheControl(FORCE_CACHE).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled())
                    return;
                callBcak.onFail(isLocation, cityId, e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callBcak.onFail(isLocation, cityId, "unSuccessful");
                } else {
                    callBcak.onSuccess(isLocation, cityId, gson.fromJson(response.body().string().trim(), Weather.class));
                }
            }
        });
    }
    
    public void cancelAllRequest() {
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
    
    public void cancelTagRequest(String tag) {
        for (Call call : client.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
    }
}

