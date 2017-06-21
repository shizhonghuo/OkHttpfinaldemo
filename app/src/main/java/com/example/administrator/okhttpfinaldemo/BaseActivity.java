package com.example.administrator.okhttpfinaldemo;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.example.okhttpfinal.HttpTaskHandler;

/**
 * Created by Administrator on 2017/6/19.
 */

public class BaseActivity extends AppCompatActivity implements MyHttpCycleContext{
    protected final String HTTP_TASK_KEY="HttpTaskkey_"+hashCode();

    /*
    * 获得屏幕大小
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Global.SCREEN_WIDTH = dm.widthPixels;
        Global.SCREEN_HEIGHT = dm.heightPixels;
    }
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpTaskHandler.getInstance().removeHttpTask(HTTP_TASK_KEY);
    }
}
