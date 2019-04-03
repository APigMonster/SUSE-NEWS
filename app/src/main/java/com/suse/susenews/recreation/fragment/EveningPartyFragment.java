package com.suse.susenews.recreation.fragment;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.adapter.EveningPartyAdapter;
import com.suse.susenews.bean.MainNewsBean;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.xutils.common.util.DensityUtil;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EveningPartyFragment extends BaseFragment {

    private SwipeRecyclerView swipe_recyclerview;
    private List<MainNewsBean> data = new ArrayList<>();
    private EveningPartyAdapter madapter;
    private List<SwipeMenuItem> menuItems = new ArrayList<>();
    private int width = DensityUtil.dip2px(80);
    private int height = ViewGroup.LayoutParams.MATCH_PARENT;
    public static StringBuilder isappointment = new StringBuilder();
    public static StringBuilder issignup = new StringBuilder();
    private SwipeRefreshLayout refresh_layout;

    public EveningPartyFragment(String title) {
        super(title);
    }

    public EveningPartyFragment() {
        this("");
    }


    @Override
    public View initView() {
        View view = View.inflate(MyApplication.getContext(), R.layout.evening_party, null);
        swipe_recyclerview = view.findViewById(R.id.swipe_recyclerview);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        return view;
    }

    @Override
    public void initData() {
        initListData();
        //设置刷新监听
        refresh_layout.setOnRefreshListener(new MyOnRefreshListener());
        //设置分割线

        //item点击监听
        swipe_recyclerview.setOnItemClickListener(new MyOnItemClickListener());
        //设置菜单创建器
        swipe_recyclerview.setSwipeMenuCreator(new MySwipeMenuCreator());
        //设置点击监听
        swipe_recyclerview.setOnItemMenuClickListener(new MySwipeItemClickListener());
        //设置加载更多
        swipe_recyclerview.useDefaultLoadMore();
        swipe_recyclerview.setLoadMoreListener(mLoadMoreListener);
        //设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
        swipe_recyclerview.setLayoutManager(layoutManager);
        madapter = new EveningPartyAdapter(data, mcontext);
        swipe_recyclerview.setAdapter(madapter);
        //触发加载更多
        swipe_recyclerview.loadMoreFinish(false, true);
    }

    SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            swipe_recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    data.add(new MainNewsBean(R.mipmap.icon, "123451", "这是一个标题，这是标题更多，对应第更多个新闻"));
                    madapter.notifyDataSetChanged(data);
                    swipe_recyclerview.loadMoreFinish(false, true);

                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    //swipe_recyclerview.loadMoreError(0, "请求网络失败");
                }
            }, 1000);
        }
    };

    class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            swipe_recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 1000); // 延时模拟请求服务器。
        }


    }

    private void loadData() {
        data.add(0, new MainNewsBean(R.mipmap.icon, "123451", "这是一个标题，这是标题0，对应第0个新闻"));
        madapter.notifyDataSetChanged(data);

        refresh_layout.setRefreshing(false);

        // 第一次加载数据：一定要调用这个方法，否则不会触发加载更多。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
        swipe_recyclerview.loadMoreFinish(false, true);
    }

    class MyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int adapterPosition) {
            //改变字体颜色
            //跳转到指定页面
        }
    }

    private void initListData() {
        //初始化新闻的数据
        data.add(new MainNewsBean(R.mipmap.icon, "123451", "这是一个标题，这是标题1，对应第1个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon, "123452", "这是一个标题，这是标题2，对应第2个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon, "123453", "这是一个标题，这是标题3，对应第3个新闻"));
        //初始化侧滑按钮数据
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.appointment_selector, "预约"));
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.button_exit_press, "已预约"));
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.singup_selector, "报名"));
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.button_exit_press, "已报名"));
    }

    class MySwipeItemClickListener implements OnItemMenuClickListener {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            //menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0 && !arrarycontains(sb2as(isappointment), position + "")) {
                    //预约
                    isappointment.append(position + ",");
                    Toast.makeText(mcontext, "预约成功", Toast.LENGTH_SHORT).show();
                } else if (menuPosition == 1 && !arrarycontains(sb2as(issignup), position + "")) {
                    //报名
                    issignup.append(position + ",");
                    Toast.makeText(mcontext, "报名成功", Toast.LENGTH_SHORT).show();
                }
                madapter.notifyDataSetChanged();
            }
        }
    }

    class MySwipeMenuCreator implements SwipeMenuCreator {

        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

            String[] isappointments = sb2as(isappointment);
            String[] issignups = sb2as(issignup);

            if (!arrarycontains(isappointments, position + "")) {
                rightMenu.addMenuItem(menuItems.get(0));
            } else {
                rightMenu.addMenuItem(menuItems.get(1));
            }

            if (!arrarycontains(issignups, position + "")) {
                rightMenu.addMenuItem(menuItems.get(2));
            } else {
                rightMenu.addMenuItem(menuItems.get(3));
            }
        }
    }

    /**
     * 把StringBuilder转化为String[]
     *
     * @return
     */
    public static String[] sb2as(StringBuilder sb) {
        /*if (sb.length() > 0){
            //去掉最后一个逗号","
            sb.deleteCharAt(sb.length() - 1);
        }*/
        //把StringBuilder转化为String
        String str = sb.toString(); //String str=URLDecoder.decode(sb.toString(), "UTF-8");
        //把String拆分为String[]
        return str.split(",");
    }

    /**
     * string[]中是否包含一个字符串
     *
     * @return
     */
    public static boolean arrarycontains(String[] as, String str) {
        List<String> strings = Arrays.asList(as);
        return strings.contains(str);
    }

    private SwipeMenuItem instanceSwipeMenuItem(int height, int width, int bgPath, String title) {
        return new SwipeMenuItem(mcontext)
                .setBackground(bgPath)
                .setText(title)
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);
    }

}
