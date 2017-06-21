package com.example.okhttpfinal;

import android.preference.PreferenceActivity;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import okhttp3.Authenticator;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Administrator on 2017/6/7.
 */

public class OkHttpFinalConfiguration {
    private List<Part> commonParam;
    protected Headers commonHeader;
    private List<InputStream> certificateList;
    private HostnameVerifier hostnameVerifier;
    private long timeout=Constants.REQ_TIMEOUT;
    private boolean debug;
    private CookieJar cookieJar = CookieJar.NO_COOKIES;
    private Cache cache;
    private Authenticator authenticator;
    private CertificatePinner certificatePinner;
    private boolean followSslRedirects;
    private boolean followRedirects;
    private boolean retryOnConnectionFailure; // 连接失败是否重连
    private Proxy proxy;
    private List<Interceptor> networkInterceptor; //Interceptor, 拦截器
    private List<Interceptor> interceptorList;
    private SSLSocketFactory sslSocketFactory;
    private Dispatcher dispatcher;

    public OkHttpFinalConfiguration(final Builder builder){
        this.commonParam=builder.commonParams;
        this.commonHeader=builder.commonHeaders;
        this.certificateList= builder.certificateList;
        this.hostnameVerifier=builder.hostnameVerifier;
        this.timeout= builder.timeout;
        this.debug= builder.debug;
        this.cookieJar= builder.cookieJar;
        this.cache= builder.cache;
        this.authenticator= builder.authenticator;
        this.certificatePinner= builder.certificatePinner;
        this.followSslRedirects= builder.followSslRedirects;
        this.followRedirects= builder.followRedirects;
        this.retryOnConnectionFailure= builder.retryOnConnectionFailure;
        this.proxy= builder.proxy;
        this.networkInterceptor= builder.networkInterceptor;
        this.interceptorList= builder.interceptorList;
        this.sslSocketFactory= builder.sslSocketFactory;
        this.dispatcher= builder.dispatcher;
    }


   public static class Builder {
       private List<Part> commonParams;
       protected Headers commonHeaders;
       private List<InputStream> certificateList;
       private HostnameVerifier hostnameVerifier;
       private long timeout=Constants.REQ_TIMEOUT;
       private boolean debug;
       private CookieJar cookieJar = CookieJar.NO_COOKIES;
       private Cache cache;
       private Authenticator authenticator;
       private CertificatePinner certificatePinner;
       private boolean followSslRedirects;
       private boolean followRedirects;
       private boolean retryOnConnectionFailure; // 连接失败是否重连
       private Proxy proxy;
       private List<Interceptor> networkInterceptor; //Interceptor, 拦截器
       private List<Interceptor> interceptorList;
       private SSLSocketFactory sslSocketFactory;
       private Dispatcher dispatcher;

       public Builder (){
           certificateList = new ArrayList<>();
           followSslRedirects = true;
           followRedirects = true;
           retryOnConnectionFailure = true;
           networkInterceptor = new ArrayList<>();
       }

       /**
        * 添加公共参数
        * 有时候我们请求网络接口每个请求都需要传递一些通用的参数，比如IMEI、UDID、版本号等。那么可以通过setCommenParams来满足你的需求
        * @param params
        * @return
        */
       public Builder setCommenParams(List<Part> params){
           this.commonParams = params;
           return this;
       }

       /**
        * 公共header
        * @param headers
        * @return
        */
       public Builder setCommenHeaders(Headers headers) {
           commonHeaders = headers;
           return this;
       }

       /**
        * 指定证书
        * 为了安全访问有些公司采用了https请求。setCertificates参数为List,证书可以是文件也可以是文本信息，如果是PEM文本信息可以通过Okio的Buffer类来转换成inputstream
        * @param
        * @return
        */

       public Builder setcertificateList(List<InputStream> certificateList){
           for(InputStream inputStream:certificateList){
               if(inputStream!=null){
                   this.certificateList.add(inputStream);
               }
           }
           return this;
       }

       public Builder setcertificateList(String... certificates){
           for(String certificate:certificates){
               if(!TextUtils.isEmpty(certificate)){
                   InputStream inputStream=new Buffer().writeUtf8(certificate).inputStream();
                   this.certificateList.add(inputStream);
               }
           }
           return this;
       }

       public Builder setCertificatePinner(CertificatePinner certificatePinner){
           this.certificatePinner=certificatePinner;
           return this;
       }

       public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier){
           this.hostnameVerifier=hostnameVerifier;
           return this;
       }

       /**
        * 设置调试开关
        * @param debug
        * @return
        */
       public Builder setDebug(boolean debug) {
           this.debug = debug;
           return this;
       }

       /**
        * 设置timeout
        * 设置超时时间（setTimeout方法） 默认请求时间是30秒
        * @param timeout
        * @return
        */
       public Builder setTimeout(long timeout) {
           this.timeout = timeout;
           return this;
       }

       /**
        * 设置cookie jar
        * @param cookieJar
        * @return
        */
       public Builder setCookieJar(CookieJar cookieJar) {
           this.cookieJar = cookieJar;
           return this;
       }

       /**
        * 设置缓存
        * @param cache
        * @return
        */
       public Builder setCache(Cache cache) {
           this.cache = cache;
           return this;
       }

       /*
       * 设置缓存-并添加网络拦截器修改响应头（有无网络都先读缓存）
       * @param cache
       * @param cacheControlValue Cache-Control值
       * @return
       */

       public Builder setCache(Cache cache,final String cacheControlValue){
           Interceptor CACHE_INTERCEPTOR =new Interceptor() {
               @Override
               public Response intercept(Chain chain) throws IOException {
                   Response originResponse =chain.proceed(chain.request());
                       Response finalResponse = originResponse.newBuilder()
                               .removeHeader("Pragma")
                               .header("Cache-Control",cacheControlValue)
                               .build();
                       return finalResponse;
               }
           };
           networkInterceptor.add(CACHE_INTERCEPTOR);
           this.cache=cache;
           return this;
       }

       /**
        * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
        * 强制响应缓存者根据该值校验新鲜性.即与自身的Age值,与请求时间做比较.如果超出max-age值,则强制去服务器端验证.以确保返回一个新鲜的响应.
        * @param cache
        * @param cacheTime 缓存时间 单位秒
        * @return
        */
       public Builder setCacheAge(Cache cache, final int cacheTime) {
           setCache(cache, String.format("max-age=%d", cacheTime));
           return this;
       }

       /**
        * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
        * 允许缓存者发送一个过期不超过指定秒数的陈旧的缓存.
        * @param cache
        * @param cacheTime 缓存时间 单位秒
        * @return
        */
       public Builder setCacheStale(Cache cache, final int cacheTime) {
           setCache(cache, String.format("max-stale=%d", cacheTime));
           return this;
       }

       /*
       * 设置Authenticator
        private String requestingHost;
        private InetAddress requestingSite;
        private int requestingPort;
        private String requestingProtocol;
        private String requestingPrompt;
        private String requestingScheme;
        private URL requestingURL;
        */

       public  Builder setAuthenticator(Authenticator authenticator){
           this.authenticator=authenticator;
           return this;
       }

       public Builder setFollowSslRedirects(boolean followProtocolRedirects) {
           this.followSslRedirects = followProtocolRedirects;
           return this;
       }

       public Builder setFollowRedirects(boolean followRedirects) {
           this.followRedirects = followRedirects;
           return this;
       }

       //开启请求失败重试
       public Builder setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
           this.retryOnConnectionFailure = retryOnConnectionFailure;
           return this;
       }

       //设置代理实例
       public Builder setProxy(Proxy proxy) {
           this.proxy = proxy;
           return this;
       }

       /**
        * 设置网络拦截器
        * @param interceptors
        * @return
        */
       public Builder setNetworkInterceptors(List<Interceptor> interceptors) {
           if (interceptors != null) {
               networkInterceptor.addAll(interceptors);
           }
           return this;
       }

       /**
        * 设置应用拦截器
        * @param interceptors
        * @return
        */
       public Builder setInterceptors(List<Interceptor> interceptors) {
           this.interceptorList = interceptors;
           return this;
       }

       /**
        * 设置SSLSocketFactory实例
        * @param sslSocketFactory
        * @return
        */
       public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
           this.sslSocketFactory = sslSocketFactory;
           return this;
       }

       /**
        * 设置Dispatcher实例
        * @param dispatcher
        * @return
        */
       public Builder setDispatcher(Dispatcher dispatcher) {
           this.dispatcher = dispatcher;
           return this;
       }

       public OkHttpFinalConfiguration build() {
           return new OkHttpFinalConfiguration(this);
       }
   }

    public List<Part> getCommonParams() {
        return commonParam;
    }

    public Headers getCommonHeaders() {
        return commonHeader;
    }

    public List<InputStream> getCertificateList() {
        return certificateList;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public long getTimeout() {
        return timeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Cache getCache() {
        return cache;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return certificatePinner;
    }

    public boolean isFollowSslRedirects() {
        return followSslRedirects;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public List<Interceptor> getNetworkInterceptorList() {
        return networkInterceptor;
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

}
