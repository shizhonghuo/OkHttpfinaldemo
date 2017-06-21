package com.example.okhttpfinal;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/16.
 */

public class FileDownloadTask extends AsyncTask<Void, Long, Boolean> {
    private OkHttpClient client;
    private FileDownloadCallback callback;
    private String url;
    private File target;
    //开始下载时间，用户计算加载速度
    private long previousTime;

    public FileDownloadTask(String url,File target, FileDownloadCallback callback){
        this.url=url;
        this.callback=callback;
        this.target=target;
        this.client=OKHttpFinal.getInstance().getOkHttpClientBuilder().build();
    }

    protected void onPreExcute(){
        super.onPreExecute();
        previousTime = System.currentTimeMillis();
        if( callback != null){
            callback.onStart();;
        }
    }
    protected Boolean doInBackground(Void... params){
        boolean suc= false;

        final Request request=new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            long total = response.body().contentLength();
            saveFile(response);
            if (total == target.length()) {
                suc = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            suc = false;
        }
        return suc;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        if (callback != null && values != null && values.length >= 2) {
            long sum = values[0];
            long total = values[1];

            int progress = (int) (sum * 100.0f / total);
            //计算下载速度
            long totalTime = (System.currentTimeMillis() - previousTime)/1000;
            if ( totalTime == 0 ) {
                totalTime += 1;
            }
            long networkSpeed = sum / totalTime;
            callback.onProgress(progress, networkSpeed);
        }
    }

    @Override
    protected void onPostExecute(Boolean suc) {
        super.onPostExecute(suc);
        if (suc) {
            if ( callback != null ) {
                callback.onDone();
            }
        } else {
            if ( callback != null ) {
                callback.onFailure();
            }
        }
    }
    public String saveFile(Response response) throws IOException {
        InputStream in = null;
        byte [] buf= new byte[2048];
        int len =0;
        FileOutputStream fos= null;
        try {
            in = response.body().byteStream();
            final long length = response.body().contentLength();
            long sum =0;
            fos = new FileOutputStream(target);
            while ((len = in.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    publishProgress(sum, length);
                }
            }
            fos.flush();
            return target.getAbsolutePath();
        }  finally {
            try {
                if (in != null) { in.close(); }
            } catch (IOException e) {
            }
            try {
                if (fos != null) { fos.close(); }
            } catch (IOException e) {
            }
        }
    }
}
