package com.example.helpme.mvpandroid.module.video;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.helpme.mvpandroid.contract.VideoContract;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class VideoModelImpl<T> implements VideoContract.VideoModel<T> {
    
    private OkHttpClient client;
    private Gson gson;
    
    
    public VideoModelImpl() {
        client = new OkHttpClient();
        gson = new Gson();
    }
    
    @Override
    public void okhttp_get(boolean refresh, int position, Class<T> classOfT, VideoContract.VideoOkHttpCallBcak<T>
            callBcak, String... params) throws Exception {
        Request request = new Request.Builder().url(params[0]).build();
        getRequest(refresh, position, classOfT, callBcak, request, params);
    }
    
    @Override
    public void okhttp_header_get(boolean refresh, int position, Class<T> classOfT, VideoContract.VideoOkHttpCallBcak<T>
            callBcak, Map<String, String> header, String... params) throws Exception {
        Request.Builder builder = new Request.Builder().url(params[0]);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        getRequest(refresh, position, classOfT, callBcak, builder.build(), params);
    }
    
    private void getRequest(final boolean refresh, final int position, final Class<T> classOfT, final VideoContract
            .VideoOkHttpCallBcak<T> callBcak, Request request, final String[] params) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (call.isCanceled())
                    return;
                callBcak.onFail(refresh, position, e.getMessage());
            }
            
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callBcak.onFail(refresh, position, "unSuccessful");
                } else {
                    String body = response.body().string().trim();
                    T data = null;
                    String error = null;
                    body= body.replace("360p_video","video_360p");
                    body= body.replace("480p_video","video_480p");
                    body= body.replace("720p_video","video_720p");
                    i("convert", body);
                    try {
                        data = gson.fromJson(body, classOfT);
                    } catch (JsonParseException e) {
                        i("JsonParseException", body);
                        data = null;
                        error = "JsonParseException:" + e.getMessage();
                        i("JsonParseException", e.getMessage());
                    } finally {
                        if (data == null)
                            callBcak.onFail(refresh, position, error);
                        else
                            callBcak.onSuccess(data, refresh, position);
                    }
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
    
    public void i(String tag, String msg) {  //信息太长,分段打印  
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，  
        //  把4*1024的MAX字节打印长度改为2001字符数  
        int max_str_length = 2001 - tag.length();
        //大于4000时  
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分  
        Log.i(tag, msg);
    }
}
