package com.example.okhttpfinal;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/6/7.
 */

public class Part {
    private String key;
    private String value;
    private FileWrapper fileWrapper;

    public Part(String key, String value){
        this.key=key;
        this.value=value;
    }
    public Part(String key, FileWrapper fileWrapper){
        this.key=key;
        this.fileWrapper=fileWrapper;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public FileWrapper getFileWrapper(){
        return fileWrapper;
    }

    protected  void setKey(String key){
        if(key==null){
            this.key="";
        } else{
            this.key=key;
        }
    }

    protected  void setValue(String value){
        if(value==null){
            this.value="";
        } else{
            this.value=value;
        }
    }

    public boolean equal( Object obj){
        if(obj == null){
            return false;
        }
        Part part=(Part) obj;
        if(part == null){
            return false;
        }

        if(TextUtils.equals(part.getKey(),getKey()) && TextUtils.equals(part.getValue(),getValue())){
            return true;
        }
        return false;
    }

}
