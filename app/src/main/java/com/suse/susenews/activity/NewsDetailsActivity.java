package com.suse.susenews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.java.MorePopupWindow;
import com.suse.susenews.java.PhotoPopupWindow;

public class NewsDetailsActivity extends Activity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageView ivmore;
    private WebView webview;
    private ProgressBar pbLoading;
    private String url;
    private MorePopupWindow mMorePopupWindow;
    private int tempSize = 2;
    private int realSize = tempSize;
    private WebSettings settings;

    private void findViews() {
        ivBack = findViewById(R.id.iv_back);
        ivmore = findViewById(R.id.iv_more);
        webview = findViewById(R.id.webview);
        pbLoading = findViewById(R.id.pb_loading);

        ivBack.setOnClickListener(this);
        ivmore.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //初始化视图
        findViews();
        //获取数据
        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra("url");
        //Log.e("TAG", "url-==-"+url);
        settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);

                //设置图片适应屏幕
                String javascript = "javascript:function ResizeImages() {" +
                        "var myimg,oldwidth;" +
                        "var maxwidth = document.body.clientWidth;" +
                        "for(i=0;i <document.images.length;i++){" +
                        "myimg = document.images[i];" +
                        "if(myimg.width > maxwidth){" +
                        "oldwidth = myimg.width;" +
                        "myimg.width = maxwidth;" +
                        "}" +
                        "}" +
                        "}";
                //String width = String.valueOf(ScreenUtils.widthPixels(mContext));
                view.loadUrl(javascript);
                view.loadUrl("javascript:ResizeImages();");
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            finish();
        } else if (v == ivmore) {
            mMorePopupWindow = new MorePopupWindow(NewsDetailsActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 改变字体
                    //Toast.makeText(NewsDetailsActivity.this, "改变字体", Toast.LENGTH_SHORT).show();
                    showChangeTextSizeDialog();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 分享
                    //Toast.makeText(NewsDetailsActivity.this, "分享", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, getTitle()));
                }
            });
            View rootView = LayoutInflater.from(NewsDetailsActivity.this).inflate(R.layout.activity_main, null);
            mMorePopupWindow.showAtLocation(rootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }
    }

    private void showChangeTextSizeDialog() {
        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        new AlertDialog.Builder(this)
                .setTitle("设置字体大小")
                .setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempSize = which;
                    }
                })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realSize = tempSize;
                        setTextSize(realSize);
                    }
                })
                .show();

    }

    private void setTextSize(int realSize) {
        switch (realSize){
            case 0:
                settings.setTextZoom(200);//超大
                break;
            case 1:
                settings.setTextZoom(150);//大
                break;
            case 2:
                settings.setTextZoom(100);//正常
                break;
            case 3:
                settings.setTextZoom(75);//小
                break;
            case 4:
                settings.setTextZoom(50);//超小
                break;
            default:
                break;
            }
    }
}
