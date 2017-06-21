package com.example.okhttpfinal;

import android.text.TextUtils;

import java.io.InputStream;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Authenticator;
/**
 * Created by Administrator on 2017/6/11.
 */

public class OKHttpFinal {
    OkHttpClient okHttpClient;
    public static OKHttpFinal okHttpFinal;
    private OkHttpFinalConfiguration configuration;

    private OKHttpFinal(){}

    public synchronized void init (OkHttpFinalConfiguration configuration){
        this.configuration=configuration;

        long timeout=configuration.getTimeout();
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout,TimeUnit.MILLISECONDS)
                .readTimeout(timeout,TimeUnit.MILLISECONDS);

        if(configuration.getHostnameVerifier() != null){
            builder.hostnameVerifier(configuration.getHostnameVerifier());
        }

        /*
        * 设置SSL 证书
         */
        List<InputStream> certificateList =configuration.getCertificateList();
        if(certificateList!=null &&certificateList.size()>0){
            HttpsCerManager httpsCerManager=new HttpsCerManager(builder);
            httpsCerManager.setCertificates(certificateList);
        }

        CookieJar cookieJar=configuration.getCookieJar();
        if(cookieJar != null){
            builder.cookieJar(cookieJar);
        }

        Cache cache =configuration.getCache();
        if(cache != null){
            builder.cache(cache);
        }
        Authenticator authenticator =configuration.getAuthenticator();
        if(authenticator != null){
            builder.authenticator(authenticator);
        }

        builder.followRedirects(configuration.isFollowRedirects());
        builder.followSslRedirects(configuration.isFollowSslRedirects());
        builder.retryOnConnectionFailure(configuration.isRetryOnConnectionFailure());

        SSLSocketFactory sslSocketFactory =configuration.getSslSocketFactory();
        if( sslSocketFactory != null){
            builder.sslSocketFactory(sslSocketFactory);
        }

        Dispatcher dispatcher =configuration.getDispatcher();
        if( dispatcher != null){
            builder.dispatcher(dispatcher);
        }

        List<Interceptor> networkInterceptor = configuration.getNetworkInterceptorList();

        if(networkInterceptor != null && networkInterceptor.size()>0){
            builder.networkInterceptors().addAll(networkInterceptor);
        }

        List<Interceptor> interceptorList =configuration.getInterceptorList();
        if(interceptorList != null && interceptorList.size() >0){
            builder.interceptors().addAll(interceptorList);
        }

        Proxy proxy =configuration.getProxy();
        if( proxy != null){
            builder.proxy(proxy);
        }

        okHttpClient = builder.build();
    }

    public static OKHttpFinal getInstance(){
        if(okHttpFinal == null){
            okHttpFinal= new OKHttpFinal();
        }
        return okHttpFinal;
    }

    public void updataCommonParams(String key, String value){
        List<Part> commonParams= configuration.getCommonParams();
        Part nParam=new Part(key,value);

        boolean add=false;

        if(commonParams != null && commonParams.size()>0){
            for(Part part:commonParams){
                if(TextUtils.equals(part.getKey(),key)){
                    part.setValue(value);
                    add = true;
                    break;
                }
            }
        }

        if(!add){
            commonParams.add(new Part(key, value));
        }
    }

    /**
     * 修改公共header信息
     * @param key
     * @param value
     */
    public void updateCommonHeader(String key,String value){
        Headers headers=configuration.getCommonHeaders();
        if ( headers == null){
            headers = new Headers.Builder().build();
        }
        configuration.commonHeader = headers.newBuilder().set(key, value).build();
    }
    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder(){
        return okHttpClient.newBuilder();
    }
    public List<Part> getCommonParams() {
        return configuration.getCommonParams();
    }

    public List<InputStream> getCertificateList() {
        return configuration.getCertificateList();
    }

    public HostnameVerifier getHostnameVerifier() {
        return configuration.getHostnameVerifier();
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }

    public Headers getCommonHeaders() {
        return configuration.getCommonHeaders();
    }
}
