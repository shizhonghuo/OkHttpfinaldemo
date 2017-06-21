package com.example.okhttpfinal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class Utils {
    public static String getFullUrl(String url, List<Part> params,boolean urlEncoder){
        StringBuffer urlFull=new StringBuffer();
        urlFull.append(url);
        //不包含“？”
        if(urlFull.indexOf("?",0)<0 && params.size()>0){
            urlFull.append("?");
        }
        int flag =0;
        for(Part param:params){
            String key =param.getKey();
            String value =param.getValue();
            if(urlEncoder) {
                try {
                    key = URLEncoder.encode(key, "UTF-8");
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            urlFull.append(key);
            urlFull.append("=");
            urlFull.append(value);
            if(++flag <params.size()){
                urlFull.append("&");
            }
        }

        return urlFull.toString();
    }
}
