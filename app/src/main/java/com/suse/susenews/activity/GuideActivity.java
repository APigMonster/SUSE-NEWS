package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.suse.susenews.R;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private List<ImageView> data = new ArrayList<>();
    private Button btn_into_main;

    private LinearLayout ll_point_group;
    private ImageView iv_red_point;

    private int dip_px;
    private int leftmargin;
    private int leftMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vp_guide = findViewById(R.id.vp_guide);
        btn_into_main = findViewById(R.id.btn_into_main);
        ll_point_group = findViewById(R.id.ll_point_group);
        iv_red_point = findViewById(R.id.iv_red_point);

        dip_px = DensityUtil.dip2px(GuideActivity.this, 10);
        initData();
        //设置adapter
        vp_guide.setAdapter(new GuideAdapter());
        //添加页面滑动监听
        vp_guide.addOnPageChangeListener(new GuideOnPageChangeListener());
        //测量左边距设置监听，保证灰色的背景点已经被测量完成
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new GuideOnGlobalLayoutListener());

    }
    class GuideOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //此方法不止执行一次，所以要移除监听
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            //获取leftMax
            leftMax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
        }
    }
    class GuideOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 测量页面偏移的百分比
         * @param i 当前页面，即你点击滑动的页面
         * @param v 当前页面偏移的百分比
         * @param i1 当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            // leftmargin = 移动的百分比 * 最大间距 + 初始位置
            leftmargin = (int) (v * leftMax + i * leftMax);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int i) {
            btn_into_main.setVisibility(View.GONE);
            if(i == data.size()-1){
                btn_into_main.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = data.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
           // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private void initData() {
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };
        for (int i = 0; i < ids.length; i++){
            //向data中添加数据
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setImageResource(ids[i]);
            data.add(imageView);
            //画出普通点
            ImageView point = new ImageView(GuideActivity.this);
            point.setBackgroundResource(R.drawable.point_nomal);
            //将点设置到布局中
                //设置point宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip_px, dip_px);
            if (i != 0){
                params.leftMargin = dip_px;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);

        }
    }

    public void toMain(View v) {
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        //设置数据库已经进入过引导页面
        CacheUtils.putBoolean(GuideActivity.this, WelcomeActivity.START_MAIN, true);
        finish();
    }
}
