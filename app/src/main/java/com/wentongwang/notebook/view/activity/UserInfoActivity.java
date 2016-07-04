package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.business.UserBiz;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.UserInfoPresenter;
import com.wentongwang.notebook.utils.ImageLoader;
import com.wentongwang.notebook.utils.MyActivityManager;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.UserInfoView;
import com.wentongwang.notebook.view.custome.CircleImageView;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人信息界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class UserInfoActivity extends BaseActivity implements UserInfoView{

    private View rootview;

    private CircleImageView userHead;

    private TextView tvUserName;
    private TextView tvUserNickName;
    private TextView tvUserEmail;
    private ImageView ivUserSex;
    private TextView tvUserDiaryPwd;

    private String userSex;



    private PopupWindow mPopupWindow;

    private View progressBar;

    private UserInfoPresenter mPresenter = new UserInfoPresenter(this);

    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.userinfo_activity_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        initViews();
        initEvents();
    }
    @Override
    protected void initDatas() {

    }
    @Override
    protected void initViews() {
        progressBar = findViewById(R.id.progress_bar);

        rootview = findViewById(R.id.userinfo_root_view);
        userHead = (CircleImageView) findViewById(R.id.iv_user_head);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserNickName = (TextView) findViewById(R.id.tv_user_nickname);
        tvUserEmail = (TextView) findViewById(R.id.tv_user_email);
        ivUserSex = (ImageView) findViewById(R.id.iv_user_sex);
        tvUserDiaryPwd = (TextView) findViewById(R.id.tv_user_diarypwd);

        mPresenter.setUserInfo();
    }
    @Override
    protected void initEvents() {

    }



    /**
     * 点击事件,用于xml里绑定
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_change_user_nickname:
                //改变用户昵称
                AlertDialog nickNameDialog = mPresenter.buildeChangeInfoDialog(UserBiz.UPDATE_USER_NICKNAME);
                nickNameDialog.show();
                break;
            case R.id.to_change_user_email:
                //改变用户邮箱
                AlertDialog emailDialog = mPresenter.buildeChangeInfoDialog(UserBiz.UPDATE_USER_EMAIL);
                emailDialog.show();
                break;
            case R.id.to_change_user_sex:
                changeUserSex();
                break;
            case R.id.to_change_user_diarypwd:
                //改变用户日记锁密码
                AlertDialog pwdDialog = mPresenter.buildeChangeInfoDialog(UserBiz.UPDATE_USER_DIARYPWD);
                pwdDialog.show();
                break;
            case R.id.iv_back_btn:
                MyActivityManager.getInstance().pop();
                onBackPressed();
                break;
            case R.id.unregistre_btn:
                //清除本地保存的用户信息
                mPresenter.logout();
                break;
            case R.id.iv_user_head:
                showPopwindow();
                break;
        }
    }

    /**
     * 改变用户性别
     */
    private void changeUserSex() {
        final String[] sexItems = {"男", "女"};
        AlertDialog.Builder sexBuilder = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("选择性别")
                .setSingleChoiceItems(sexItems, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSex = sexItems[which];
                    }
                });
        sexBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.upDateInfo(UserBiz.UPDATE_USER_SEX, userSex);
            }
        });
        sexBuilder.create().show();
    }



    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        View view = getLayoutInflater().inflate(R.layout.popupwindow_choose_layout, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        // 这里检验popWindow里的button是否可以点击
        TextView album = (TextView) view.findViewById(R.id.go_to_album);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                mPopupWindow.dismiss();
            }
        });

        // 在底部显示
        mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    private static final int PHOTO_ALBUM = 1;
    private static final int CROP_PHOTO = 2;
    /**
     * 从相册中获取图片
     */
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    Log.e("xxxx", "url = " + data.getData());
                    Uri url = data.getData();
                    if (url != null) {
                        //通过url返回
//                    ContentResolver cr = this.getContentResolver();
//                    try {
//                        //从url中获取图片
//                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(url));
//                        userHead.setImage(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                        //从url中获取图片
                        cropImage(url, 500, 500, CROP_PHOTO);
                    } else {
                        Toast.makeText(getApplicationContext(), "未能获取到图片", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case CROP_PHOTO:
                Bitmap photo = null;
                Uri photourl = data.getData();
                if (photourl != null) {
                    photo = BitmapFactory.decodeFile(photourl.getPath());
                } else {
                    Bundle extra = data.getExtras();
                    if (extra != null) {
                        photo = (Bitmap) extra.get("data");
                        File f = new File(getCacheDir(), "userHead");
                        if (f.exists()) {
                            f.delete();
                        }
                        mPresenter.uploadUserHead(f,photo);
                    }
                }
                break;
        }


    }

    /**
     * 图片的裁剪
     *
     * @param uri 图片URL
     * @param outputX 图片输出大小
     * @param outputY 图片输出大小
     * @param requestCode 请求码
     */
    private void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getMyContext() {
        return UserInfoActivity.this;
    }

    /**
     * 设置用户名
     *
     * @param userName
     */
    @Override
    public void setUserName(String userName) {
        tvUserName.setText(userName);
    }

    /**
     * 设置用户昵称
     *
     * @param userNickName
     */
    @Override
    public void setUserNickName(String userNickName) {
        //TODO:把这些判断逻辑写到presenter里去，让外部不知道底层逻辑是什么
        if (!TextUtils.isEmpty(userNickName) && !userNickName.equals("")) {
            tvUserNickName.setText(userNickName);
        } else {
            tvUserNickName.setText(userNickName);
        }
    }

    /**
     * 设置用户邮箱
     *
     * @param email
     */
    @Override
    public void setUserEmail(String email) {
        if (!TextUtils.isEmpty(email) && !email.equals("")) {
            tvUserEmail.setText(email);
        } else {
            tvUserEmail.setText("您还没绑定你的邮箱");
        }
    }

    /**
     * 设置用户性别
     *
     * @param sex
     */
    @Override
    public void setUserSex(String sex) {
        if (!TextUtils.isEmpty(sex) && !sex.equals("")) {
            if (sex.equals("女"))
                ivUserSex.setImageDrawable(getResources().getDrawable(R.drawable.female));
            else
                ivUserSex.setImageDrawable(getResources().getDrawable(R.drawable.male));
        } else {
            ivUserSex.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
    }

    /**
     * 设置日记密码
     *
     * @param pwd
     */
    @Override
    public void setUserDiaryPwd(String pwd) {
        if (!TextUtils.isEmpty(pwd) && !pwd.equals("")) {
            tvUserDiaryPwd.setText(pwd);
        } else {
            tvUserDiaryPwd.setText("");
        }
    }

    /**
     * 设置用户头像
     */
    @Override
    public void setUserHead(Bitmap bitmap) {
        if (bitmap != null) {
            userHead.setImage(bitmap);
        }
    }

    /**
     * 跳转到登录界面
     */
    @Override
    public void goToLoginActivity() {
        Intent it = new Intent();
        it.setClass(UserInfoActivity.this, LoginActivity.class);
        startActivity(it);
        MyActivityManager.getInstance().pop();
    }

    /**
     * 显示进度条
     */
    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 获取缓存文件夹目录
     *
     * @return
     */
    @Override
    public String getCachePath() {
        return getCacheDir().getAbsolutePath();
    }

    /**
     * 刷新其他界面的头像
     */
    @Override
    public void toUpdateUserInfo() {
        UpdataEvent event = new UpdataEvent();
        event.setType(UpdataEvent.UPDATE_USER_INFOS);
        EventBus.getDefault().post(event);
    }

    /**
     * 根据资源id获取drawable
     *
     * @param id
     * @return
     */
    @Override
    public Drawable getDrawableByResource(int id) {
        return getResources().getDrawable(id);
    }


}
