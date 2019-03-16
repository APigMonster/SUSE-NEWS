package com.suse.susenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.fragment.MainFragment;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
    }

    private boolean exit = false;//标识是否可以退出
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==1) {
                exit = false;
            }
        }
    };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
            if(!exit) {
                exit = true;
                Toast.makeText(this, "再按一次就退出应用", 0).show();
                //发消息延迟2s将exit=false
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;//不退出
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main, new MainFragment());
        transaction.commit();
    }
}
