package com.example.okhttpfinal;

import java.lang.reflect.Type;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/14.
 */

public class BaseHttpRequestCallback<T> {
    public static final int ERROR_RESPONSE_TIMEOUT=1;
    public static final int ERROR_RESPONSE_DATA_PARSE_EXCEPTION=2;
    public static final int ERROR_RESPONSE_UNKNOWN =3;

    protected Type type; //Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
    protected Headers headers;

    public BaseHttpRequestCallback(){
        type = ClassTypeReflect.getModelClazz(getClass());
    }

    //请求执行的回调
    public void onStart(){

    }

    //请求响应的回调
    public void onResponse(Response httpResponse, String response, Headers headers){

    }

    public void onResponse (String response,Headers headers){

    }

    //请求完成
    public void onFinish(){

    }

    //请求成功，带Headers 信息
    public void onSuccess(Headers headers, T t){

    }

    //请求成功
    public void onSuccess(T t){

    }

    /**
     * 上传文件进度
     * @param process
     * @param networkSpeed 网速
     * @param done
     */
    public void onProcess(int process, long networkSpeed, boolean done){

    }

    //请求失败
    public void onFailure(int errorCode, String msg){

    }

    //获取响应头
    public Headers getHeaders(){
        return headers;
    }

    protected void setResponseHeaders(Headers headers) {
        this.headers = headers;
    }
}
