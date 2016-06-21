package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.view.custome.CircleImageView;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;

import java.io.FileNotFoundException;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人信息界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class UserInfoActivity extends Activity {

    //    private ImageView backBtn;
//
//    private Button logout;
    private View rootview;

    private CircleImageView userHead;

    private TextView tvUserName;
    private TextView tvUserNickName;
    private TextView tvUserEmail;
    private TextView tvUserSex;
    private TextView tvUserDiaryPwd;

    private String userName;
    private String userNickName;
    private String userEmail;
    private String userSex;
    private String diarypwd;


    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_activity_layout);
        initDatas();
        initViews();
        initEvents();
    }

    private void initDatas() {
//        Bmob.initialize(this, Constants.APPLICATION_ID);
        userName = AccountUtils.getUserName(this);
        userNickName = AccountUtils.getUserNickName(this);
        userEmail = AccountUtils.getUserEmail(this);
        userSex = AccountUtils.getUserSex(this);
        diarypwd = AccountUtils.getUserDiaryPwd(this);
    }

    private void initViews() {
        rootview = findViewById(R.id.userinfo_root_view);
//        backBtn = (ImageView) findViewById(R.id.iv_back_btn);
//
//        logout = (Button) findViewById(R.id.unregistre_btn);

        userHead = (CircleImageView) findViewById(R.id.iv_user_head);
        userHead.setImage(R.drawable.user_head_defaut);

        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText(userName);

        tvUserNickName = (TextView) findViewById(R.id.tv_user_nickname);
        if (!TextUtils.isEmpty(userNickName) && !userNickName.equals("")) {
            tvUserNickName.setText(userNickName);
        } else {
            tvUserNickName.setText(userName);
        }

        tvUserEmail = (TextView) findViewById(R.id.tv_user_email);
        if (!TextUtils.isEmpty(userEmail) && !userEmail.equals("")) {
            tvUserEmail.setText(userEmail);
        } else {
            tvUserEmail.setText("您还没绑定你的邮箱");
        }

        tvUserSex = (TextView) findViewById(R.id.tv_user_sex);
        if (!TextUtils.isEmpty(userSex) && !userSex.equals("")) {
            tvUserSex.setText(userSex);
        } else {
            tvUserSex.setText("男");
        }

        tvUserDiaryPwd = (TextView) findViewById(R.id.tv_user_diarypwd);
        if (!TextUtils.isEmpty(diarypwd) && !diarypwd.equals("")) {
            tvUserDiaryPwd.setText(diarypwd);
        } else {
            tvUserDiaryPwd.setText("");
        }
    }

    private void initEvents() {

    }

    /**
     * 点击事件,用于xml里绑定
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_change_user_nickname:
                toChangeUserNickName();
                break;
            case R.id.to_change_user_email:
                toChangeUserEmail();
                break;
            case R.id.to_change_user_sex:
                toChangeUserSex();
                break;
            case R.id.to_change_user_diarypwd:
                toChangeUserDiaryPwd();
                break;
            case R.id.iv_back_btn:
                onBackPressed();
                break;
            case R.id.unregistre_btn:
                //清除本地保存的用户信息
                AccountUtils.clearAllInfos(UserInfoActivity.this);
                Intent it = new Intent();
                it.setClass(UserInfoActivity.this, LoginActivity.class);
                startActivity(it);
                break;
            case R.id.iv_user_head:
                showPopwindow();
                break;
        }
    }

    /**
     * 改变用户日记锁密码
     */
    private void toChangeUserDiaryPwd() {
        View layout;
        final EditText et_pwd;
        final EditText et_change;
        //载入,初始化自定义对话框布局
        layout = getLayoutInflater().inflate(R.layout.change_info_dialog_layout, null);
        et_pwd = (EditText) layout.findViewById(R.id.ed_pwd_confirm);
        et_change = (EditText) layout.findViewById(R.id.ed_change_info);
        et_change.setHint("新的日记密码");
        //设置对话框
        AlertDialog.Builder diarypwdBuilder = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("修改日记密码")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = et_pwd.getText().toString();
                        if (pwd.equals(AccountUtils.getUserPwd(UserInfoActivity.this))) {
                            diarypwd = et_change.getText().toString();
                            upDateInfo(R.id.to_change_user_diarypwd, diarypwd);
                        } else {
                            Toast.makeText(UserInfoActivity.this, "密码不正确，重新输入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //弹出对话框
        diarypwdBuilder.create().show();
    }

    /**
     * 改变用户性别
     */
    private void toChangeUserSex() {
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
                upDateInfo(R.id.to_change_user_sex, userSex);
            }
        });
        sexBuilder.create().show();
    }

    /**
     * 改变用户邮箱
     */
    private void toChangeUserEmail() {
        View layout;
        final EditText et_pwd;
        final EditText et_change;

        layout = getLayoutInflater().inflate(R.layout.change_info_dialog_layout, null);
        et_pwd = (EditText) layout.findViewById(R.id.ed_pwd_confirm);
        et_change = (EditText) layout.findViewById(R.id.ed_change_info);
        et_change.setHint("新的邮箱");
        AlertDialog.Builder emailBuilder = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("修改邮箱")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = et_pwd.getText().toString();
                        if (pwd.equals(AccountUtils.getUserPwd(UserInfoActivity.this))) {
                            userEmail = et_change.getText().toString();
                            upDateInfo(R.id.to_change_user_email, userEmail);
                        } else {
                            Toast.makeText(UserInfoActivity.this, "密码不正确，重新输入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        emailBuilder.create().show();
    }

    /**
     * 改变用户昵称
     */
    private void toChangeUserNickName() {
        View layout;
        final EditText et_pwd;
        final EditText et_change;

        layout = getLayoutInflater().inflate(R.layout.change_info_dialog_layout, null);
        et_pwd = (EditText) layout.findViewById(R.id.ed_pwd_confirm);
        et_change = (EditText) layout.findViewById(R.id.ed_change_info);
        et_change.setHint("新的昵称");
        AlertDialog.Builder nicknameBuilder = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("修改昵称")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = et_pwd.getText().toString();
                        if (pwd.equals(AccountUtils.getUserPwd(UserInfoActivity.this))) {
                            userNickName = et_change.getText().toString();
                            upDateInfo(R.id.to_change_user_nickname, userNickName);
                        } else {
                            Toast.makeText(UserInfoActivity.this, "密码不正确，重新输入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        nicknameBuilder.create().show();
    }

    /**
     * 更新信息的网络请求
     *
     * @param id 属于哪一个信息
     * @param data 修改内容
     */
    private void upDateInfo(int id, String data) {
        User p = new User();
        switch (id) {
            case R.id.to_change_user_nickname:
                p.setUser_nickname(data);
                break;
            case R.id.to_change_user_email:
                p.setEmail(data);
                break;
            case R.id.to_change_user_sex:
                p.setUser_sex(data);
                break;
            case R.id.to_change_user_diarypwd:
                p.setUser_diraypwd(data);
                break;
        }
        p.update(this, AccountUtils.getUserId(this), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserInfoActivity.this, "修改成功!", Toast.LENGTH_LONG).show();
                notifyChanges();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(UserInfoActivity.this, "修改失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 通知Ui界面改变
     */
    private void notifyChanges() {
        userHead.setImage(R.drawable.user_head_defaut);

        tvUserName.setText(userName);
        AccountUtils.saveUserName(this, userName);

        if (!TextUtils.isEmpty(userNickName) && !userNickName.equals("")) {
            tvUserNickName.setText(userNickName);
            AccountUtils.saveUserNickName(this, userNickName);
        } else {
            tvUserNickName.setText(userName);
        }

        if (!TextUtils.isEmpty(userEmail) && !userEmail.equals("")) {
            tvUserEmail.setText(userEmail);
            AccountUtils.saveUserEmail(this, userEmail);
        } else {
            tvUserEmail.setText("您还没绑定你的邮箱");
        }

        if (!TextUtils.isEmpty(userSex) && !userSex.equals("")) {
            tvUserSex.setText(userSex);
            AccountUtils.saveUserSex(this, userSex);
        } else {
            tvUserSex.setText("男");
        }

        if (!TextUtils.isEmpty(diarypwd) && !diarypwd.equals("")) {
            tvUserDiaryPwd.setText(diarypwd);
            AccountUtils.saveUserDiaryPwd(this, diarypwd);
        }
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

    /**
     * 从相册中获取图片
     */
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("xxxx", "url = " + data.getData());
        Uri url = data.getData();
        if (url != null) {
            //通过url返回
            ContentResolver cr = this.getContentResolver();
            try {
                //从url中获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(url));
                userHead.setImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //通过intent中的bundle返回
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = (Bitmap) bundle.get("data");
                userHead.setImage(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        }

    }
}
