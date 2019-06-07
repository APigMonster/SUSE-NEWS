package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.adapter.PersonSimpleInfoAdapter;
import com.suse.susenews.bean.PersonSimpleInfoBean;
import com.suse.susenews.bean.Student;
import com.suse.susenews.java.PhotoPopupWindow;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends Activity implements View.OnClickListener {

    private ImageView iv_person_back;
    private ImageView iv_head_portrait;
    private RelativeLayout rl_head_portrait;

    private RelativeLayout rl_user_name;
    private TextView tv_user_name_value;
    private RelativeLayout rl_person_signature;
    private TextView tv_person_signature_value;

    private RecyclerView rv_person_simple_info;
    private List<PersonSimpleInfoBean> data = new ArrayList<>();

    private PhotoPopupWindow mPhotoPopupWindow;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;
    private static final String IMAGE_FILE_NAME = "icon.jpg";

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        findViews();
        //初始化数据
        student = new Gson().fromJson(getIntent().getStringExtra("student"), Student.class);
        initData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_person_simple_info.setLayoutManager(manager);
        rv_person_simple_info.setAdapter(new PersonSimpleInfoAdapter(data, this));
    }

    private void initData() {
        //用户名应该从数据库查询得到
        data.add(new PersonSimpleInfoBean("姓名", student.getName()));
        data.add(new PersonSimpleInfoBean("学号", student.getNo()));
        data.add(new PersonSimpleInfoBean("专业", student.getMajor()));
        data.add(new PersonSimpleInfoBean("学制", student.getEdusys()));
        data.add(new PersonSimpleInfoBean("入学时间", student.getCreattime()));

        //初始化头像昵称和个签
        x.image().bind(iv_head_portrait, Constants.UPLOAD_URL + student.getHeadpor());
        tv_user_name_value.setText(student.getNickname());
        tv_person_signature_value.setText(student.getPersonsign());

    }

    private void findViews() {
        iv_person_back = findViewById(R.id.iv_person_back);
        iv_head_portrait = findViewById(R.id.iv_head_portrait);
        rl_head_portrait = findViewById(R.id.rl_head_portrait);
        rv_person_simple_info = findViewById(R.id.rv_person_simple_info);

        rl_user_name = findViewById(R.id.rl_user_name);
        tv_user_name_value = findViewById(R.id.tv_user_name_value);
        rl_person_signature = findViewById(R.id.rl_person_signature);
        tv_person_signature_value = findViewById(R.id.tv_person_signature_value);

        iv_person_back.setOnClickListener(this);
        rl_head_portrait.setOnClickListener(this);

        rl_user_name.setOnClickListener(this);
        rl_person_signature.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PersonInfoActivity.this, UserNameSignActivity.class);
        if (v == iv_person_back) {
            finish();
        } else if (v == rl_head_portrait) {
            setImageHead();
        } else if (v == rl_user_name) {
            String value = tv_user_name_value.getText().toString();
            intent.putExtra("VALUE", value);
            startActivityForResult(intent, 1);
        } else if (v == rl_person_signature) {
            String value = tv_person_signature_value.getText().toString();
            intent.putExtra("VALUE", value);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 3) {
            String result = data.getStringExtra("RESULT");
            switch (requestCode) {
                case 1:
                    //昵称
                    tv_user_name_value.setText(result);
                    setStudentInfo("nickname", result);
                    break;
                case 2:
                    //个签
                    tv_person_signature_value.setText(result);
                    setStudentInfo("personsign", result);
                    break;
            }
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //小图切割
                case REQUEST_SMALL_IMAGE_CUTTING:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                //从相册中选择
                case REQUEST_IMAGE_GET:
                    try {
                        startSmallPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        //触发了这个方法，一定有信息被更改了。所以应该重新请求数据(重新请求数据应该在这个页面的上一层中 即PersonFragment.java)
    }

    private void setStudentInfo(String key, String value) {
        String no = student.getNo();
        RequestParams requestParams = new RequestParams(Constants.STUDENT_UPDATA_URL);
        requestParams.addQueryStringParameter("NO", no);
        requestParams.addQueryStringParameter("KEY", key);
        requestParams.addQueryStringParameter("VALUE", value);

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "更新个人信息成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "更新个人信息失败  " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 小图模式切割图片
     * 此方式直接返回截图后的 bitmap，由于内存的限制，返回的图片会比较小
     */
    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    /**
     * 小图模式中，保存图片后，设置到视图中
     */
    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        File file = null;
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data"); // 直接获得内存中保存的 bitmap
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(storage + "/smallIcon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.e("TAG", "文件夹创建失败");
                    } else {
                        Log.e("TAG", "文件夹创建成功");
                    }
                }
                file = new File(dirFile, "HEAD_PORTRAIT.jpg");
                // 保存图片
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //上传图片到服务器
            uploadFile(file);

            // 在视图中显示图片
            iv_head_portrait.setImageBitmap(photo);
        }
    }

    private void uploadFile(File file) {
        RequestParams params = new RequestParams(Constants.STUDENT_UPLOAD_URL);
        params.setMultipart(true);
        params.addBodyParameter("file", file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "图片上传成功 result == " + result);
                //设置fileName
                String fileName = result.substring(13, result.length() - 2);
                //把图片路径保存到数据库中
                setStudentInfo("headpor", fileName);
                //Log.e("TAG", "fileName == " + result.substring(13, result.length() - 2));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "图片上传失败  " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void setImageHead() {
        mPhotoPopupWindow = new PhotoPopupWindow(PersonInfoActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入相册
                mPhotoPopupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 判断系统中是否有处理该 Intent 的 Activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                } else {
                    Toast.makeText(PersonInfoActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
            }
        });
        View rootView = LayoutInflater.from(PersonInfoActivity.this).inflate(R.layout.activity_person_info, null);
        mPhotoPopupWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
