package com.example.okhttpfinal;

import org.json.JSONArray;

/**
 * Created by Administrator on 2017/6/16.
 */

public class JsonArrayHttpRequestCallback extends BaseHttpRequestCallback<JSONArray> {

    public JsonArrayHttpRequestCallback() {
        super();
        type = JSONArray.class;
    }
}