package com.example.administrator.okhttpfinaldemo;

import android.content.Context;

import com.example.okhttpfinal.HttpCycleContext;

/**
 * Created by Administrator on 2017/6/19.
 */

public interface MyHttpCycleContext extends HttpCycleContext {
    Context getContext();
}
