package com.example.administrator.okhttpfinaldemo;

import android.app.Application;
import android.provider.SyncStateContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.okhttpfinal.Constants;
import com.example.okhttpfinal.OKHttpFinal;
import com.example.okhttpfinal.OkHttpFinalConfiguration;
import com.example.okhttpfinal.Part;

import okhttp3.Headers;
import okhttp3.Interceptor;

/**
 * Created by Administrator on 2017/6/19.
 *  设置
 */

public class IApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttpFinal();
    }
    private void initOkHttpFinal() {

        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();

        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(Constants.REQ_TIMEOUT)
                .setInterceptors(interceptorList)
                //.setCookieJar(CookieJar.NO_COOKIES)
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
//        addHttps(builder);
        Log.d("IApplication","init OKHttpFinal. ");
        OKHttpFinal.getInstance().init(builder.build());
    }
}
