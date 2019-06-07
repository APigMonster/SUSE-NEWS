package com.suse.susenews.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.activity.MainActivity;
import com.suse.susenews.activity.SearchActivity;
import com.suse.susenews.adapter.MainNewsAdapter;
import com.suse.susenews.bean.NewsListBean;
import com.suse.susenews.bean.Susenews;
import com.suse.susenews.bean.TopNewsBean;
import com.suse.susenews.bean.TopNewsListBean;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainNewsFragment extends BaseFragment {

    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;

    private RecyclerView rv_main_news;
    private List<Susenews> data = new ArrayList<>();
    private List<TopNewsBean> topData = new ArrayList<>();
    private EditText et_search;

    private int prePosition;

    private Dialog dialog;


    private boolean isinitListDataed;//判断是否初始化过数据
    private MainNewsAdapter adapter;
    private View view;

    @Override
    public View initView() {
        view = View.inflate(mcontext, R.layout.main_news_layout, null);

        viewpager = view.findViewById(R.id.viewpager);
        tv_title = view.findViewById(R.id.tv_title);
        ll_point_group = view.findViewById(R.id.ll_point_group);

        rv_main_news = view.findViewById(R.id.rv_main_news);
        et_search = view.findViewById(R.id.et_search);
        //设置点击监听
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, SearchActivity.class));
            }
        });
        return view;
    }


    @Override
    public void initData() {
        if (topData.size() == 0){
            initTopNewsData();
        }else {
            setLayouManager();
            adapter.notifyDataSetChanged();
        }
        if (data.size() == 0) {
            initLoad();
        }else {
            setViewPagerAdapter();
        }

    }

    private void setLayouManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
        rv_main_news.setLayoutManager(layoutManager);
        adapter = new MainNewsAdapter(data, mcontext);
        rv_main_news.setAdapter(adapter);
    }

    private void initLoad() {

        //请求RecylerView 中的数据
        RequestParams params = new RequestParams(Constants.GETNEWS_URL + "/1/30/1");
        params.addParameter("searchType", "subject");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析数据
                //processData(result);
                NewsListBean newsListBean = new Gson().fromJson(result, NewsListBean.class);

                data.clear();
                data.addAll(newsListBean.getNewsList());
                //设置适配器
                setLayouManager();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "ex:----===" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initTopNewsData() {
        //请求顶部轮播图的数据（此处图片可点击下载，并且存到项目里面；如果没有网络可以显示本地下载的图片）
        RequestParams param = new RequestParams(Constants.GETTOPNEWS_URL);
        x.http().get(param, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析数据
                TopNewsListBean topNewsListBean = new Gson().fromJson(result, TopNewsListBean.class);

                topData.clear();
                topData.addAll(topNewsListBean.getData());
                //设置viewPager的适配器
                setViewPagerAdapter();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "ex:----===" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void setViewPagerAdapter() {
        //设置适配器
        viewpager.setAdapter(new TopNewsAdapter());
        //设置红点
        ll_point_group.removeAllViews();//移除红点
        for (int i = 0; i < topData.size(); i++) {
            ImageView imageView = new ImageView(mcontext);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(6), DensityUtil.dip2px(6));

            imageView.setEnabled(false);
            if (i != 0){
                params.leftMargin = DensityUtil.dip2px(4);
            }

            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);

        }
        //监听页面变化
        viewpager.addOnPageChangeListener(new MyPageChangeListener());
        //设置初始标题prePosition和默认选中
        tv_title.setText(topData.get(prePosition).getTitle());
        ll_point_group.getChildAt(prePosition).setEnabled(true);
    }


    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            //1.设置标题文本
            tv_title.setText(topData.get(i).getTitle());
            //2.设置对应页面点高亮
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(i).setEnabled(true);

            prePosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mcontext);
            imageView.setBackgroundResource(R.drawable.default_bg);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //可以在这里给图片设置id并且设置的点击事件
            container.addView(imageView);

            //联网请求图片(从topData中的图片路径请求图片)
            String url = topData.get(position).getImage();
            x.image().bind(imageView, Constants.UPLOAD_URL+url);

            //给image设置点击事件
            //simageView.setOnClickListener(new MyOnClickListener(url));

            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    class MyOnClickListener implements View.OnClickListener{

        private String url;
        public MyOnClickListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            //Log.i("TAG", "你点击了" + v.getAccessibilityClassName());
            //显示大图
           /* ViewGroup parents = (ViewGroup) v.getParent();
            if (parents != null){
                parents.removeAllViewsInLayout();
            }*/
            initDialog(url);
            dialog.show();
            Log.e("TAG", "正在显示？"+dialog.isShowing());
        }
    }

    private void initDialog(String url) {
        dialog = new Dialog(MyApplication.getContext(),R.style.edit_AlertDialog_style);

        dialog.setContentView(R.layout.dialog_image);
        ImageView imageView = dialog.findViewById(R.id.iv_dialog);
        x.image().bind(imageView, Constants.UPLOAD_URL+url);

        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dialog.setCanceledOnTouchOutside(true); // Sets whether this dialog is

        MainActivity mainActivity = (MainActivity) mcontext;
        WindowManager.LayoutParams attributes =  mainActivity.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);

        /*Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小
        lp.height = 400;
        dialog.getWindow().setAttributes(lp);*/

        //大图的点击事件（点击让他消失）
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
