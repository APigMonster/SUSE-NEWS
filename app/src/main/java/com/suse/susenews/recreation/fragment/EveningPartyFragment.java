package com.suse.susenews.recreation.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.activity.NewsDetailsActivity;
import com.suse.susenews.adapter.EveningPartyAdapter;
import com.suse.susenews.bean.LoginBean;
import com.suse.susenews.bean.NewsListBean;
import com.suse.susenews.bean.StudentAppoed;
import com.suse.susenews.bean.Susenews;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EveningPartyFragment extends BaseFragment {

    private SwipeRecyclerView swipe_recyclerview;
    private List<Susenews> data = new ArrayList<>();
    private List<Integer> isAppoData = new ArrayList<>();
    private EveningPartyAdapter madapter;
    private List<SwipeMenuItem> menuItems = new ArrayList<>();
    private int width = DensityUtil.dip2px(80);
    private int height = ViewGroup.LayoutParams.MATCH_PARENT;
    public static StringBuilder isappointment = new StringBuilder();
    public static StringBuilder issignup = new StringBuilder();
    private SwipeRefreshLayout refresh_layout;

    private String idArray = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, CacheUtils.READ_ARRAY_ID);

    private int pno = 1;
    private int ps = 5;
    private String subject = "2";
    private int maxPno;
    private LinearLayout ll_bottom;
    private String appointegral;
    private String account;

    private List<StudentAppoed.SusenewsListBean> appoedData = new ArrayList<>();



    public EveningPartyFragment(String title) {
        super(title);
    }



    @Override
    public View initView() {
        View view = View.inflate(MyApplication.getContext(), R.layout.evening_party, null);
        swipe_recyclerview = view.findViewById(R.id.swipe_recyclerview);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        ll_bottom = (LinearLayout) View.inflate(MyApplication.getContext(), R.layout.listview_foot, null);
        return view;
    }

    @Override
    public void initData() {

        //联网请求数据
        if (data.size() == 0) {
            selectStudentAppoed();
        } else {
            //从其他页面再次回到本fragment时，数据不为空，但监听和适配器已经清空
            setLayoutManger();
        }

    }

    SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            swipe_recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //data.add(new MainNewsBean(R.mipmap.icon, "123451", "这是一个标题，这是标题更多，对应第更多个新闻"));
                    if (maxPno == pno) {
                        //Toast.makeText(MyApplication.getContext(), "-- 到底啦 --", Toast.LENGTH_LONG).show();
                        swipe_recyclerview.loadMoreFinish(false, false);
                    } else {
                        pno = pno + 1;
                        initLoad(pno, ps, subject, true);
                        swipe_recyclerview.loadMoreFinish(false, true);
                    }

                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    //swipe_recyclerview.loadMoreError(0, "请求网络失败");
                }
            }, 1);
        }
    };

    class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            swipe_recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh_layout.setRefreshing(false);
                    loadData();
                }
            }, 1); // 延时模拟请求服务器。
        }


    }

    private void loadData() {
        //data.add(0, new MainNewsBean(R.mipmap.icon, "123451", "这是一个标题，这是标题0，对应第0个新闻"));
        pno = 1;
        data.clear();
        isAppoData.clear();
        initLoad(pno, ps, subject, true);

        refresh_layout.setRefreshing(false);

        // 第一次加载数据：一定要调用这个方法，否则不会触发加载更多。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
        swipe_recyclerview.loadMoreFinish(false, true);
    }

    class MyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int adapterPosition) {

            Susenews susenews = data.get(adapterPosition);
            String id = susenews.getId() + "";
            String url = susenews.getContent();

            //Log.e("TAG", view.toString());
            //改变字体颜色
            if (!idArray.contains(id)) {
                CacheUtils.putString(MyApplication.getContext(), CacheUtils.SPNAME, CacheUtils.READ_ARRAY_ID, idArray + id + ",");
                //点击时设置字体为灰色
                //viewHolder.title.setTextColor(Color.GRAY);
                //通知刷新适配器可让点击的新闻变灰色
            }

            //手动修改data数据，更新阅读次数(被注释掉的部分没有生效)
            susenews.setReadcounts(susenews.getReadcounts()+1);
            //通知适配器刷新
            madapter.notifyItemChanged(adapterPosition);
            //修改阅读次数（联网修改阅读次数）
            RequestParams params = new RequestParams(Constants.UPDATA_NEWS_URL);
            params.addParameter("newsId", id);
            params.addParameter("type", "readcounts");
            params.addParameter("update", susenews.getReadcounts());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("TAG", result);
                    madapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("TAG", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

            //跳转到详情Activity
            Intent intent = new Intent(mcontext, NewsDetailsActivity.class);
            intent.putExtra("url", Constants.UPLOAD_URL + url);
            mcontext.startActivity(intent);
        }
    }

    private void initListData() {
        //初始化新闻的数据

        //Toast.makeText(MyApplication.getContext(), "account == "+account, Toast.LENGTH_SHORT).show();
        //查询该学生的已预约的新闻
        //请求网络
        initLoad(pno, ps, subject, false);
        //初始化侧滑按钮数据(根据请求的数据也是动态的)(还要通过查询确定用户是否已预约)

        //-1:可预约；0:无预约功能；1:人数已满；2:预约时间已到；3:已预约
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.appointment_selector, "预约"));//-1
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.button_exit_press, "不可预约"));//0
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.red, "人数已满"));//1
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.yellow, "已过期"));//2
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.button_exit_press, "已预约"));//3
        menuItems.add(instanceSwipeMenuItem(height, width, R.drawable.button_exit_press, "积分为零，无法预约"));//4
    }

    private void selectStudentAppoed() {

        account = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, "login_account");
        appointegral = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, "appointegral");

        RequestParams params = new RequestParams(Constants.SELECT_APPOED_URL+account);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Log.e("TAG", "appoedData.size() == " +appoedData.size());
                StudentAppoed studentAppoed = new Gson().fromJson(result, StudentAppoed.class);
                appoedData.addAll(studentAppoed.getSusenewsList());

                initListData();
                //Log.e("TAG", "appoedData.size() == " +appoedData.size());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "ex:--==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void initLoad(int pno, int ps, String subject, final boolean isAdatper) {
        RequestParams params = new RequestParams(Constants.GETNEWS_URL + pno + "/" + ps + "/" + subject);
        params.addParameter("searchType", "subject");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析数据
                //processData(result);
                NewsListBean newsListBean = new Gson().fromJson(result, NewsListBean.class);
                maxPno = newsListBean.getMaxPno();

                //判断是否是最后一页
                data.addAll(newsListBean.getNewsList());
                //取出list中新闻是否可预约的数据
                getIsAppo(data);
                //设置适配器
                if (!isAdatper) {
                    setLayoutManger();
                } else {
                    madapter.notifyDataSetChanged(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "ex:----===" + ex.getMessage());
                swipe_recyclerview.loadMoreError(0, "请求网络失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getIsAppo(List<Susenews> data) {
        //Log.e("TAG", "appointegral ==" + appointegral);
        if (appointegral.equals("0")){
            for (Susenews susenews : data) {
                isAppoData.add(4);
            }
        }else {
            //通过循环取出每条新闻对应的预约状态
            boolean isappoed = false;
            for (Susenews susenews : data) {
                //联网获取该学生已预约的新闻
                for (StudentAppoed.SusenewsListBean appoed:appoedData){
                    if (susenews.getId() == appoed.getSusenews().getId()){
                        isAppoData.add(3);
                        isappoed = true;
                        break;
                    }
                }
                if (isappoed){
                    isappoed = false;
                }else {
                    isAppoData.add(susenews.getIsappo());
                }
            }
        }

    }

    private void setLayoutManger() {

        //设置刷新监听
        refresh_layout.setOnRefreshListener(new MyOnRefreshListener());
        //设置分割线

        //设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
        swipe_recyclerview.setLayoutManager(layoutManager);
        //设置菜单创建器
        swipe_recyclerview.setSwipeMenuCreator(new MySwipeMenuCreator());
        //设置点击监听
        swipe_recyclerview.setOnItemMenuClickListener(new MySwipeItemClickListener());
        //item点击监听
        swipe_recyclerview.setOnItemClickListener(new MyOnItemClickListener());
        //设置加载更多
        swipe_recyclerview.useDefaultLoadMore();
        swipe_recyclerview.setLoadMoreListener(mLoadMoreListener);

        madapter = new EveningPartyAdapter(data, mcontext);
        swipe_recyclerview.setAdapter(madapter);
        //触发加载更多
        swipe_recyclerview.loadMoreFinish(false, true);


    }

    class MySwipeItemClickListener implements OnItemMenuClickListener {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, final int position) {
            //menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 侧换菜单的Item中在RecyclerView的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                /*if (menuPosition == 0 && !arrarycontains(sb2as(isappointment), position + "")) {
                    //预约
                    isappointment.append(position + ",");
                    Toast.makeText(mcontext, "预约成功", Toast.LENGTH_SHORT).show();
                } else if (menuPosition == 1 && !arrarycontains(sb2as(issignup), position + "")) {
                    //报名
                    issignup.append(position + ",");
                    Toast.makeText(mcontext, "报名成功", Toast.LENGTH_SHORT).show();
                }*/
                final int p = position;
                if (isAppoData.get(p) == -1) {
                    //发送请求修改数据库中的值（且要判断是否可预约成功，若预约失败则返回失败信息）
                    Susenews susenews = data.get(p);
                    int newsId = susenews.getId();
                    RequestParams params = new RequestParams(Constants.UPDATA_ISAPPO_URL);
                    params.addParameter("newsId", newsId);
                    params.addParameter("studentId", account);
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            LoginBean loginBean = new Gson().fromJson(result, LoginBean.class);
                            switch (loginBean.getResultCode()){
                                case "100":
                                    //已到预约数
                                    Toast.makeText(MyApplication.getContext(), "预约失败：预约人数已满", Toast.LENGTH_SHORT).show();
                                    break;
                                case "200":
                                    //已到预约时间
                                    Toast.makeText(MyApplication.getContext(), "预约失败：已过期", Toast.LENGTH_SHORT).show();
                                    break;
                                case "300":
                                    //如果更新成功
                                    isAppoData.set(p, 3);
                                    madapter.notifyItemChanged(p);
                                    Toast.makeText(MyApplication.getContext(), "预约成功", Toast.LENGTH_SHORT).show();
                                    appointegral = (Integer.parseInt(appointegral) - 30) + "";
                                    //madapter.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                                }

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("TAG", ex.getMessage());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });


                }
            }
        }
    }

    class MySwipeMenuCreator implements SwipeMenuCreator {

        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

            /*String[] isappointments = sb2as(isappointment);
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
            }*/
            /*
             * -1:可预约；0:无预约功能；1:人数已满；2:预约时间已到；3:已预约
             * */
            int appoStauts = isAppoData.get(position);
            //Log.e("TAG","appoStauts == "+appoStauts+"postion == "+position);
            switch (appoStauts) {
                case -1:
                    rightMenu.addMenuItem(menuItems.get(0));
                    break;
                case 0:
                    rightMenu.addMenuItem(menuItems.get(1));
                    break;
                case 1:
                    rightMenu.addMenuItem(menuItems.get(2));
                    break;
                case 2:
                    rightMenu.addMenuItem(menuItems.get(3));
                    break;
                case 3:
                    rightMenu.addMenuItem(menuItems.get(4));
                    break;
                case 4:
                    rightMenu.addMenuItem(menuItems.get(5));
                    break;
                default:
                    break;
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
