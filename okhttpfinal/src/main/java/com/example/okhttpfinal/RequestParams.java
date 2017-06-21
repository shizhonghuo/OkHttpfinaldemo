package com.example.okhttpfinal;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/13.
 * 1. Requestparam 包含 Head 和requestBody
 * 2. Head 是由key/value 对保存， 都是字符串
 * 3. requesbody 必须包含key/value 对的数据
 *    如果需要处理文件，需要包含 key/file信息对
 *    不包含file 信息的是formbody
 *    包含file 信息的是MultipartBody
 */

public class RequestParams {

    protected  final Headers.Builder headBuilder=new Headers.Builder();
    private final List<Part> params= new ArrayList<>();
    private final List<Part> files = new ArrayList<>();

    protected  HttpCycleContext httpCycleContext;

    private String httpTaskKay;
    private RequestBody requestBody;
    private boolean applicationJson;
    private boolean uriEncoder; //是否进行URL 编码
    private JSONObject jsonObject;
    protected CacheControl cacheControl;

    public RequestParams(){
        this(null);
    }

    public RequestParams(HttpCycleContext httpCycleContext){
        this.httpCycleContext=httpCycleContext;
        init();
    }


    private void  init(){
        headBuilder.add("charset","UTF-8"); //charset 属性规定 HTML 文档的字符编码
        List<Part> commonParam=OKHttpFinal.getInstance().getCommonParams();
        if(commonParam!= null && commonParam.size()>0){
            params.addAll(commonParam);
        }

        //添加公共header
        Headers headers=OKHttpFinal.getInstance().getCommonHeaders();
        if(headers != null && headers.size()>0){
            for(int i=0; i<headers.size(); i++){
                headBuilder.add(headers.name(i),headers.value(i));
            }
        }

        if(httpCycleContext != null){
            httpTaskKay=httpCycleContext.getHttpTaskKey();
        }
    }

    public String getHttpTaskKay (){
        return httpTaskKay;
    }

    public void addFormDataPart(String key, String value){
        if ( value == null ) {
            value = "";
        }
        Part newParam =new Part(key,value);

        if(TextUtils.isEmpty(key) && !params.contains(newParam)){
            params.add(newParam);
        }
    }

    public void addFormDataPart(String key, int value){
        addFormDataPart(key,Integer.toString(value));
    }

    public void addFormDataPart(String key, long value){
        addFormDataPart(key,String.valueOf(value));
    }
    public void addFormDataPart(String key, float value){
        addFormDataPart(key,String.valueOf(value));
    }
    public void addFormDataPart(String key, double value) {
        addFormDataPart(key, String.valueOf(value));
    }

    public void addFormDataPart(String key, boolean value) {
        addFormDataPart(key, String.valueOf(value));
    }

    public void addFormDataPart(String key, FileWrapper fileWrapper){
        if(!TextUtils.isEmpty(key)&&fileWrapper != null){
            File file =fileWrapper.getFile();
            if(file == null || file.length()==0){
                return ;
            }
            Part param=new Part(key,fileWrapper);
            files.add(param);
        }
    }

    public void addFormDataPart(String key, List<FileWrapper> fileWrappers) {
        for (FileWrapper fileWrapper:fileWrappers){
            addFormDataPart(key, fileWrapper);
        }
    }

    public void addFormDataParts(List<Part> params) {
        this.params.addAll(params);
    }

    public void addFormDataPart(String key,File file){
        if(file == null || file.length()==0){
            return ;
        }

        boolean isPng =file.getName().lastIndexOf("png") > 0
                || file.getName().lastIndexOf("PNG")> 0;
        if(isPng){
            addFormDataPart(key, file, "image/png; charset=UTF-8");
            return;
        }

        boolean isJpg = file.getName().lastIndexOf("jpg") > 0 || file.getName().lastIndexOf("JPG") > 0
                ||file.getName().lastIndexOf("jpeg") > 0 || file.getName().lastIndexOf("JPEG") > 0;
        if (isJpg) {
            addFormDataPart(key, file, "image/jpeg; charset=UTF-8");
            return;
        }

        if(!isJpg && ! isPng){
            addFormDataPart(key, new FileWrapper(file,null));
        }
    }
    public void addFormDataPart(String key,File file, String contextType){
        if(file ==null || file.length() == 0){
            return ;
        }
        MediaType mediaType=null;
        mediaType = MediaType.parse(contextType);

        addFormDataPart(key, new FileWrapper(file,mediaType));
    }

    public void addFormDataPart(String key, File file, MediaType mediaType){
        if(file == null && file.length()== 0){
            return ;
        }
        addFormDataPart(key, new FileWrapper(file,mediaType));
    }

    public void addFormDataPartFiles(String key, List<File> files){
        for(File file: files){
            if(file == null && file.length() ==0){
                continue;
            }
            addFormDataPart(key,file);
        }
    }

    public void addFormDataPart(String key, List<File> files, MediaType mediaType) {
        for (File file:files){
            if (file == null || !file.exists() || file.length() == 0) {
                continue;
            }
            addFormDataPart(key, new FileWrapper(file, mediaType));
        }
    }


    // key  与value 以： 分隔
    public void addHeader(String line){
        headBuilder.add(line);
    }

    public void addHeader(String key, String value){
        if(value == null){
            value = "";
        }

        if(!TextUtils.isEmpty(key)){
            headBuilder.add(key,value);
        }
    }

    public void addHeader(String key, int value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, long value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, float value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, double value) {
        addHeader(key, String.valueOf(value));
    }

    public void addHeader(String key, boolean value) {
        addHeader(key, String.valueOf(value));
    }

    public void urlEncoder(){
        uriEncoder=true;
    }
    public boolean isUrlEncoder(){
        return uriEncoder;
    }

    public void setCacheControl(CacheControl cacheControl){
        this.cacheControl=cacheControl;
    }

    public void clear(){
        params.clear();;
        files.clear();
    }

    /**
     * 设置application/json方式传递数据
     * @param json 请求的JSON实例
     */
    public void setApplicationJson(JSONObject json){
        this.jsonObject=json;
        applicationJson=true;
    }

    public boolean isApplicationJson(){
        return applicationJson;
    }

    public void setRequestBody(RequestBody requestBody){
        this.requestBody=requestBody;
    }

    public void setRequestBodyString(String body){
        setRequestBody(MediaType.parse("text/plain; charset=utf-8"), body);
    }

    public void setRequestbody(String mediaType, String body){
        setRequestBody(MediaType.parse(mediaType),body);
    }


    public void setRequestBody(MediaType mediaType, String string) {
        setRequestBody(RequestBody.create(mediaType, string));
    }

    public List<Part> getFormParams() {
        return params;
    }

    protected RequestBody getRequestBody(){
        RequestBody body =null;
        // Json 格式,从params 中获得
        if(applicationJson){
            String json;
            if(jsonObject != null){
               json = jsonObject.toString();
            } else {
                JSONObject object = new JSONObject();
                try {
                    for (Part part : params) {
                        object.put(part.getKey(), (Object) part.getValue());
                    }
                } catch (JSONException  e){
                    e.printStackTrace();
                }
                json=object.toString();
            }
            body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        } else if(requestBody != null){
            body=requestBody;
        } else if(files.size()>0){  // 上传文件
            boolean hasData= false;
            MultipartBody.Builder builder= new MultipartBody.Builder(); //MultipartBody 复合型requestBody.也就是既包含字符串， 也包含文件
            for(Part part: params){
                builder.addFormDataPart(part.getKey(),part.getValue());
                hasData=true;
            }
            for(Part part:files){
                String key= part.getKey();
                FileWrapper fileWrapper=part.getFileWrapper();
                builder.addFormDataPart(key,fileWrapper.fileName,RequestBody.create(fileWrapper.getMediaType(),fileWrapper.getFile()));
            }

            if(hasData){
                body=builder.build();
            }
        } else {
            FormBody.Builder builder=new FormBody.Builder(); //标准requestbody
            for(Part part: params){
                builder.add(part.getKey(),part.getValue());
            }
            body=builder.build();
        }
        return body;
    }

    public String toString(){
        StringBuilder result= new StringBuilder();
        for(Part part:params){
            if(result.length()>0){
                result.append("&");
            }
            result.append(part.getKey());
            result.append("=");
            result.append(part.getValue());
        }

        for(Part part :files){
            if(result.length()>0){
                result.append("&");
            }
            result.append(part.getKey());
            result.append("=");
            result.append(part.getFileWrapper().getFileName());
        }
        return result.toString();
    }

}
