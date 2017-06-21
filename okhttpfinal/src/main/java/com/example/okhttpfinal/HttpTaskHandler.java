package com.example.okhttpfinal;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/14.
 */

public class HttpTaskHandler {

    /** 正在请求的任务集合 */
    private static Map<String,List<OkHttpTask>> httptaskmap;
    /** 单例请求处理器 */
    private static HttpTaskHandler httpTaskHandler =null;

    private HttpTaskHandler(){
        httptaskmap= new ConcurrentHashMap<>();
    }

    public static HttpTaskHandler getInstance(){
        if(httpTaskHandler == null){
            httpTaskHandler=new HttpTaskHandler();
        }
        return  httpTaskHandler;
    }

    /**
     * 移除任务
     * @param key
     */
    public void removeHttpTask(String key){
        if(httptaskmap.containsKey(key)){
            httptaskmap.remove(key);
        }
    }

    /**
     * 添加任务
     * @param key
     * @param task
     */
    public void addHttpTask(String key, OkHttpTask task){
        if(httptaskmap.containsKey(key)){
            List<OkHttpTask> list= httptaskmap.get(key);
            list.add(task);
            httptaskmap.put(key,list);
        }else {
            List<OkHttpTask> list =new ArrayList<>();
            list.add(task);
            httptaskmap.put(key,list);
        }
    }

    public boolean contain(String key){
        return httptaskmap.containsKey(key);
    }
}
