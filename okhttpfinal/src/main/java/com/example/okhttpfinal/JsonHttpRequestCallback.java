package com.example.okhttpfinal;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/16.
 */

public class JsonHttpRequestCallback extends BaseHttpRequestCallback<JSONObject> {

    public JsonHttpRequestCallback() {
        super();
        type = JSONObject.class;
    }
}