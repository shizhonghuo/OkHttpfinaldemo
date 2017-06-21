package com.example.okhttpfinal;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2017/6/16.
 *处理进度
 */

public class ProgressRequestBody  extends RequestBody{
    private long previousTime;

    protected RequestBody  delegate;
    protected ProgressCallback callback;
    protected CountingSink countingSink;
    public ProgressRequestBody(RequestBody requestBody, ProgressCallback callback){
        this.delegate=requestBody;
        this.callback=callback;
    }

    public MediaType contentType(){
        return delegate.contentType();
    }
    public void writeTo(BufferedSink sink) throws IOException {
        previousTime = System.currentTimeMillis();
        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        delegate.writeTo(bufferedSink);

        bufferedSink.flush();

    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;
        //总字节长度，避免多次调用contentLength()方法
        long contentLength = 0L;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            if (contentLength == 0) {
                //获得contentLength的值，后续不再调用
                contentLength = contentLength();
            }
            bytesWritten += byteCount;
            //回调
            if (callback != null) {
                //计算速度
                long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
                if (totalTime == 0) {
                    totalTime += 1;
                }
                long networkSpeed = bytesWritten / totalTime;
                int progress = (int) (bytesWritten * 100 / contentLength);
                boolean done = bytesWritten == contentLength;
                callback.updateProgress(progress, networkSpeed, done);
            }
        }
    }
}
