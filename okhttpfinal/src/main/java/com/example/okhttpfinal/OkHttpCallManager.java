package com.example.okhttpfinal;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/14.
 */

public class OkHttpCallManager {
    private ConcurrentHashMap<String, Call> map;
    private static OkHttpCallManager manager;

    private OkHttpCallManager(){
        map=new ConcurrentHashMap<>();
    }

    public static OkHttpCallManager getInstance(){
        if(manager == null){
            manager=new OkHttpCallManager();
        }
        return manager;
    }

    public void add(String url,Call call){
        map.put(url,call);
    }

    public Call getCall(String url){
        if(map.containsKey(url)){
            return map.get(url);
        }
        return null;
    }

    public void removeCall(String url){
        if(!TextUtils.isEmpty(url)){
            map.remove(url);
        }
    }

}
