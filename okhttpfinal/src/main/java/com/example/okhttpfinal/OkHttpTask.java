package com.example.okhttpfinal;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/14.
 *
 */

public class OkHttpTask implements Callback, ProgressCallback{
    private Handler handler =new Handler(Looper.getMainLooper());
    public static final String  DEFAULT_HTTP_TASK_KEY="default_http_task_key";

    private String url;
    private RequestParams requestParams;
    private BaseHttpRequestCallback baseHttpRequestCallback;
    private Headers headers;
    private String requestKey;
    private Method method;
    private OkHttpClient okHttpClient;

    public OkHttpTask(Method method, String url, RequestParams params,OkHttpClient.Builder builder,BaseHttpRequestCallback callback){
        this.url=url;
        this.method=method;
        this.baseHttpRequestCallback=callback;
        if(params != null){
            this.requestParams=params;
        } else {
            this.requestParams=new RequestParams();
        }
        this.requestKey=this.requestParams.getHttpTaskKay();
        if (TextUtils.isEmpty(requestKey)) {
            requestKey = DEFAULT_HTTP_TASK_KEY;
        }
        okHttpClient=builder.build();
        // 添加任务
        HttpTaskHandler.getInstance().addHttpTask(this.requestKey, this);
    }

    public String getUrl() {
        return url;
    }

    protected void execute (){
        if(requestParams.headBuilder != null){
            headers=requestParams.headBuilder.build(); //取Headers
        }
        if(baseHttpRequestCallback != null){
            baseHttpRequestCallback.onStart();
        }
        try {
            run();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private  void run () throws Exception {
        String srcUrl = url;

        Request.Builder builder= new Request.Builder();

        // GET,DELETE,HEAD 命令没有requestbody，只取 FormParams
        switch(method){
            case GET:{
                url=Utils.getFullUrl(url, requestParams.getFormParams(),requestParams.isUrlEncoder());
                builder.get();
                break;
            }
            case DELETE: {
                url = Utils.getFullUrl(url, requestParams.getFormParams(), requestParams.isUrlEncoder());
                builder.delete();
                break;
            }
            case HEAD: {
                url = Utils.getFullUrl(url, requestParams.getFormParams(), requestParams.isUrlEncoder());
                builder.head();
                break;
            }
            case POST: {
                RequestBody requestBody = requestParams.getRequestBody();
                if (requestBody != null) {
                    builder.post(new ProgressRequestBody(requestBody,this));
                }
                break;
            }
            case PUT:{
                RequestBody requestBody =requestParams.getRequestBody();
                if(requestBody != null){
                    builder.put(new ProgressRequestBody(requestBody,this));
                }
                break;
            }
            case PATCH:{
                RequestBody requestBody= requestParams.getRequestBody();
                if(requestBody != null){
                    builder.patch(new ProgressRequestBody(requestBody,this));
                }
                break;
            }
        }
        if(requestParams.cacheControl != null){
            builder.cacheControl(requestParams.cacheControl);
        }
        builder.url(url).tag(srcUrl).headers(headers);
        Request request =builder.build();
        Call call =okHttpClient.newCall(request);
        OkHttpCallManager.getInstance().add(url,call);
        //执行请求
        call.enqueue(this);
    }

    /**
     * 处理进度
     *
     * @param progress
     * @param networkSpeed
     * @param done
     */
    @Override
    public void updateProgress(final int progress, final long networkSpeed, final boolean done) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (baseHttpRequestCallback != null) {
                    baseHttpRequestCallback.onProcess(progress, networkSpeed, done);
                }
            }
        });
    }
    public void onFailure(Call call, IOException e){
        ResponseData responseData = new ResponseData();
        if(e instanceof SocketTimeoutException){
            responseData.setTimeout(true);
        } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),"timeout")){
            responseData.setTimeout(true);
        }
        handlerResponse(responseData,null);
    }

    public void onResponse(Call call, Response response){
        ResponseData responseData = new ResponseData();
        handlerResponse(responseData, response);
    }
    public void handlerResponse(final ResponseData responseData, Response response){
        //获取请求结果
        if(response !=null){
            responseData.setResponseNull(false);
            responseData.setCode(response.code());
            responseData.setMessage(response.message());
            responseData.setSuccess(response.isSuccessful());
            responseData.setHeaders(response.headers());
            String rpnBody="";
            try{
                rpnBody=response.body().string();
            } catch (IOException e){
                e.printStackTrace();
            }
            responseData.setResponse(rpnBody);
        } else {
            responseData.setResponseNull(true);
            responseData.setCode(BaseHttpRequestCallback.ERROR_RESPONSE_UNKNOWN);
            if(responseData.isTimeout()){
                responseData.setMessage("request timeout");
            } else {
                responseData.setMessage("http exception");
            }
        }
        responseData.setHttpResponse(response);

        // 跳转到新线程
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPostExcecute(responseData);
            }
        });
    }

    private void onPostExcecute(ResponseData responseData){
        OkHttpCallManager.getInstance().removeCall(url);

        if(!HttpTaskHandler.getInstance().contain(requestKey)){
            return ;
        }

        if(baseHttpRequestCallback != null){
            baseHttpRequestCallback.setResponseHeaders(responseData.getHeaders());
            baseHttpRequestCallback.onResponse(responseData.getHttpResponse(),
                    responseData.getResponse(),responseData.getHeaders());
            baseHttpRequestCallback.onResponse(responseData.getResponse(),responseData.getHeaders());
        }

        int code = responseData.getCode();
        String msg= responseData.getMessage();

        if(!responseData.isResponseNull()){ //有返回
            if(responseData.isSuccess()){ // 成功的返回
                parseResponseBody(responseData, responseData.getHttpResponse());
            } else {
                if(baseHttpRequestCallback !=null){
                    baseHttpRequestCallback.onFailure(code, msg);
                }
            }
        } else {
            if(baseHttpRequestCallback !=null){
                baseHttpRequestCallback.onFailure(code, msg);
            }
        }

        if(baseHttpRequestCallback != null){
            baseHttpRequestCallback.onFinish();
        }
    }

    private void parseResponseBody (ResponseData responseData, Response response){
        if(baseHttpRequestCallback == null){
            return;
        }

        String result = responseData.getResponse(); // response.body

        if(baseHttpRequestCallback.type == String.class){
            baseHttpRequestCallback.onSuccess(responseData.getHeaders(),result);
            baseHttpRequestCallback.onSuccess(result);
            return ;
        } else if(baseHttpRequestCallback.type == JSONObject.class){
            JSONObject object =null;
            try {
                 object = new JSONObject(result);
            } catch (JSONException e){
                e.printStackTrace();
            }
            baseHttpRequestCallback.onSuccess(responseData.getHeaders(),object);
            baseHttpRequestCallback.onSuccess(object);
            return ;
        } else if (baseHttpRequestCallback.type == JSONArray.class){
            JSONArray jsonArray=null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e){
                e.printStackTrace();
            }
            baseHttpRequestCallback.onSuccess(responseData.getHeaders(), jsonArray);
            baseHttpRequestCallback.onSuccess(jsonArray);
        }
        baseHttpRequestCallback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_DATA_PARSE_EXCEPTION, "Data parse exception");
    }

}
