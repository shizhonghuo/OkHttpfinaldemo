package com.example.administrator.okhttpfinaldemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.okhttpfinal.HttpRequest;
import com.example.okhttpfinal.ImgCallBack;
import com.example.okhttpfinal.JsonHttpRequestCallback;
import com.example.okhttpfinal.RequestParams;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.Request;

import static android.R.attr.onClick;

public class MainActivity extends BaseActivity  {

    String ImgUrl="http://img.my.csdn.net/uploads/201706/19/1497857861_8518.jpg-thumb.jpg";
    String JsonUrl="http://www.2cto.com/kf/201502/374951.html/sa3a1ece91f2cef238db14798aafed22e01be6ce1c3fec7fe1103c.js";

    Button btn1,btn2;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       // ButterKnife.bind(this);
        imageView =(ImageView)findViewById(R.id.imag);
        textView=(TextView)findViewById(R.id.text);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","images.");
                // RequestParams params=new RequestParams(this);
        /*
        params.addHeader("Content-Type","image/jpeg");
        params.addFormDataPart("page", 1);
        params.addFormDataPart("limit", 12);*/
                HttpRequest.get(ImgUrl,new ImgCallBack(){
                    public void onSuccess(Headers headers, Bitmap bitmap){
                        Log.d("MainActivity","image success.");
                        super.onSuccess(headers,bitmap);
                        imageView.setImageBitmap(bitmap);
                    }
                    //请求成功
                    public void onSuccess(Bitmap bitmap){
                        Log.d("MainActivity","image success.");
                        super.onSuccess(bitmap);
                        imageView.setImageBitmap(bitmap);
                    }

                    //请求失败
                    public void onFailure(int errorCode, String msg){
                        Log.d("MainActivity","image fail " + msg);
                    }
                });
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","JSONObjec");
                HttpRequest.get(JsonUrl,new JsonHttpRequestCallback(){
                    public void onSuccess(Headers headers, JSONObject object){
                        Log.d("MainActivity","JSONObjec success");
                        super.onSuccess(headers,object);
                        textView.setText(object.toString());
                    }
                    //请求成功
                    public void onSuccess(JSONObject object){
                        Log.d("MainActivity","JSONObjec success");
                        super.onSuccess(object);
                        textView.setText(object.toString());
                    }

                    //请求失败
                    public void onFailure(int errorCode, String msg){
                        Log.d("MainActivity","JSONObject fail " + msg);
                    }
                });
            }
        });
    }

}
