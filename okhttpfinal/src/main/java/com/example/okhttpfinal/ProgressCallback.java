package com.example.okhttpfinal;

/**
 * Created by Administrator on 2017/6/14.
 * 进度显示
 */

public interface ProgressCallback {
    void updateProgress(int progress, long networkSpeed, boolean done);
}
