package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class SignUpActivity extends Activity {


    private EditText userName;
    private EditText userPwd;
    private EditText userPwd2;
    private Button singUpBtn;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.signup_activity_layout);
        Bmob.initialize(this, Constants.APPLICATION_ID);
        initViews();

        initEvents();
    }

    private void initViews() {
        userName = (EditText) findViewById(R.id.et_sign_up_username);
        userPwd = (EditText) findViewById(R.id.et_sign_up_pwd);
        userPwd2 = (EditText) findViewById(R.id.et_sign_up_pwd_confirm);

        singUpBtn = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = userPwd.getText().toString();
                if (pwd.equals(userPwd2.getText().toString())) {
                    final User bu = new User();
                    bu.setUsername(userName.getText().toString());
                    bu.setPassword(MD5Util.MD5(pwd));
                    //注意：不能用save方法进行注册
                    bu.signUp(SignUpActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {

                            User user = new User();
                            user = BmobUser.getCurrentUser(SignUpActivity.this, User.class);
                            Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息

                            AccountUtils.saveUserInfos(SignUpActivity.this, user, pwd);
                            Intent it = new Intent();
                            it.setClass(SignUpActivity.this, HomeActivity.class);
                            startActivity(it);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Toast.makeText(SignUpActivity.this, "注册失败:" + msg, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "两次输入密码不正确，请重新输入", Toast.LENGTH_LONG).show();
                }


            }
        });
    }


}
