package com.example.okhttpfinal;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ImgCallBack extends BaseHttpRequestCallback<Bitmap> {
    public ImgCallBack(){
        super();
        type=Bitmap.class;
    }

}
