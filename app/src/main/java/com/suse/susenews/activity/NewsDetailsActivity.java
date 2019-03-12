package com.suse.susenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suse.susenews.R;

public class NewsDetailsActivity extends Activity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageView ivmore;
    private WebView webview;
    private ProgressBar pbLoading;

    private void findViews() {
        ivBack = findViewById( R.id.iv_back );
        ivmore = findViewById( R.id.iv_more );
        webview = findViewById( R.id.webview );
        pbLoading = findViewById( R.id.pb_loading );

        ivBack.setOnClickListener(this);
        ivmore.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //初始化视图
        findViews();
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack){
            finish();
        }else if (v == ivmore){
            Toast.makeText(NewsDetailsActivity.this, "更多", Toast.LENGTH_SHORT).show();
        }
    }
}
