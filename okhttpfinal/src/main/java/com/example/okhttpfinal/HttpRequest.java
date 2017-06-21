package com.example.okhttpfinal;

import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/14.
 */

public class HttpRequest {

    /**
     * Get请求
     * @param url
     * @param
     * @param
     */
   /* public static void get(String url){
        get(url,null,null);
    }

    public static void get(String url, RequestParams params){
        get(url,params,null);
    }*/

    public static void get(String url, BaseHttpRequestCallback callback) {
        get(url, null, callback);
    }
    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback){
        get(url,params,Constants.REQ_TIMEOUT,callback);
    }

    public static void get(String url, RequestParams requestParams, long timeout,BaseHttpRequestCallback callback){
        OkHttpClient.Builder builder=OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(timeout,TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout,TimeUnit.MILLISECONDS);
        executeRequest(Method.GET,url,requestParams,builder,callback);
    }
    public static void get(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.GET, url, params, builder, callback);
    }

    /**
     * Post请求
     * @param url
     * @param
     * @param
     */
   /* public static void post(String url){
        post(url,null,null);
    }
    public static void post(String url, RequestParams params){
        post(url,params,null);
    }
*/
    public static void post(String url, BaseHttpRequestCallback callback) {
        post(url, null, callback);
    }

    public static void post(String url,RequestParams params, BaseHttpRequestCallback callback){
        post(url,params,Constants.REQ_TIMEOUT,callback);
    }

    public static void post(String url,RequestParams params,long timeout, BaseHttpRequestCallback callback){
        OkHttpClient.Builder builder =OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.connectTimeout(timeout,TimeUnit.MILLISECONDS);
        builder.readTimeout(timeout,TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout,TimeUnit.MILLISECONDS);
        executeRequest(Method.POST,url,params,builder,callback);
    }

    public static void post(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.POST, url, params, builder, callback);
    }

    /**
     * put请求
     * @param url
     * @param
     * @param
     */

   /* public static void put(String url) {
        put(url, null, null);
    }

    public static void put(String url, RequestParams params) {
        put(url, params, null);
    }*/

    public static void put(String url, BaseHttpRequestCallback callback) {
        put(url, null, callback);
    }

    public static void put(String url,RequestParams params,BaseHttpRequestCallback callback){
        put(url,params,Constants.REQ_TIMEOUT,callback);
    }

    public static void put(String url, RequestParams params, long timeout, BaseHttpRequestCallback callback){
        OkHttpClient.Builder builder = OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(timeout,TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout,TimeUnit.MILLISECONDS);
        executeRequest(Method.PUT,url,params,builder,callback);
    }

    public static void put(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.PUT, url, params, builder, callback);
    }

    /**
     * delete请求
     * @param url
     */
/*
    public static void delete(String url) {
        delete(url, null, null);
    }

    public static void delete(String url, RequestParams params) {
        delete(url, params, null);
    }*/

    public static void delete(String url, BaseHttpRequestCallback callback) {
        delete(url, null, callback);
    }
    public static void delete(String url, RequestParams params, BaseHttpRequestCallback callback) {
        delete(url, params, Constants.REQ_TIMEOUT, callback);
    }

    public static void delete(String url, RequestParams params, long timeout, BaseHttpRequestCallback callback) {
        OkHttpClient.Builder builder = OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        executeRequest(Method.DELETE, url, params, builder, callback);
    }

    public static void delete(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.DELETE, url, params, builder, callback);
    }


    /**
     * head请求
     * @param url
     */
   /* public static void head(String url) {
        head(url, null, null);
    }

    public static void head(String url, RequestParams params) {
        head(url, params, null);
    }*/

    public static void head(String url, BaseHttpRequestCallback callback) {
        head(url, null, callback);
    }

    public static void head(String url, RequestParams params, BaseHttpRequestCallback callback) {
        head(url, params, Constants.REQ_TIMEOUT, callback);
    }

    public static void head(String url, RequestParams params, long timeout, BaseHttpRequestCallback callback) {
        OkHttpClient.Builder builder = OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        executeRequest(Method.HEAD, url, params, builder, callback);
    }

    public static void head(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.HEAD, url, params, builder, callback);
    }
    /**
     * patch请求
     * @param url
     */
    /*public static void patch(String url) {
        patch(url, null, null);
    }

    public static void patch(String url, RequestParams params) {
        patch(url, params, null);
    }*/

    public static void patch(String url, BaseHttpRequestCallback callback) {
        patch(url, null, callback);
    }

    public static void patch(String url, RequestParams params, BaseHttpRequestCallback callback) {
        patch(url, params, Constants.REQ_TIMEOUT, callback);
    }

    public static void patch(String url, RequestParams params, long timeout, BaseHttpRequestCallback callback) {
        OkHttpClient.Builder builder = OKHttpFinal.getInstance().getOkHttpClientBuilder();
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        executeRequest(Method.PATCH, url, params, builder, callback);
    }

    public static void patch(String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        executeRequest(Method.PATCH, url, params, builder, callback);
    }

    /**
     * 取消请求
     * @param url
     */
    public static void cancel(String url) {
        if ( !TextUtils.isEmpty(url) ) {
            Call call = OkHttpCallManager.getInstance().getCall(url);
            if ( call != null ) {
                call.cancel();
            }

            OkHttpCallManager.getInstance().removeCall(url);
        }
    }


    private  static void executeRequest(Method method,String url,RequestParams params,
                                       OkHttpClient.Builder builder,BaseHttpRequestCallback callback){
        if(!TextUtils.isEmpty(url)){
            if(builder == null){
                builder=OKHttpFinal.getInstance().getOkHttpClientBuilder();
            }
        }
        OkHttpTask task=new OkHttpTask(method,url,params,builder,callback);
        task.execute();
    }
}
