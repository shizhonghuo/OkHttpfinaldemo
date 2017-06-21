package com.example.okhttpfinal;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/6/7.
 */
/*
* 文件包装类
 */
public class FileWrapper {
    public File file;
    public String fileName;
    public MediaType mediaType;
    private long fileSize;

    public FileWrapper(File file,MediaType mediaType){
        this.file=file;
        this.fileName=file.getName();
        this.fileSize=file.length();
        this.mediaType=mediaType;
    }

    public String getFileName(){
        if(fileName!=null){
            return fileName;
        } else{
            return "nofile";
        }
    }

    public File getFile(){
        return file;
    }

    public long getFileSize(){
        return fileSize;
    }
    public MediaType getMediaType(){
        return mediaType;
    }
}
