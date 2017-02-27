package com.example.yls.android_0227;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn_js;
    private Button btn_jsc;
    private WebView mWebView;
    private  Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext=this;
        mWebView= (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/myHtml.html");
        mWebView.addJavascriptInterface(MainActivity.this,"android");
        initViews();


    }

    private void initViews() {
        btn_js= (Button) findViewById(R.id.btn_js);
        btn_jsc= (Button) findViewById(R.id.btn_jsc);


        btn_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });
        btn_jsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
            }
        });
    }
    @JavascriptInterface
    public void  startFunction(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"show",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @JavascriptInterface
    public void startFunction(final String text){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setMessage(text).show();
            }
        });
    }
    @JavascriptInterface
    public void add(int a,int b){//做加法
        int sum=a+b;
        String result=String.valueOf(sum);
        Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
   public void open(){//界面传递
        Intent i=new Intent();
        i.setClass(MainActivity.this,ActivityB.class);
        startActivity(i);
    }



    @JavascriptInterface
    public void call(){//93-133的代码为动态注册后打电话给10086
        int checkCallPhonePermissiom= ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        if(checkCallPhonePermissiom!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE);
            return;
        }else {
            callPhone();
        }

    }
    public void callPhone(){
        Intent phoneIntent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + "10086"));
        startActivity(phoneIntent);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case  RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callPhone();
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public interface RequestPermissionType
    {

        /**
         * 请求打电话的权限码
         */
        int REQUEST_CODE_ASK_CALL_PHONE = 100;
    }

    @JavascriptInterface
    public void send(){//发短信,注意权限
        Uri uri=Uri.parse("smsto:13800000000");
        Intent intent1=new Intent(Intent.ACTION_SENDTO,uri);
        intent1.putExtra("sms_body","Hello ,How Are You?");
        startActivity(intent1);
    }
}
