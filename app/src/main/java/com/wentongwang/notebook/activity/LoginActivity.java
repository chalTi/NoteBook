package com.wentongwang.notebook.activity;

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
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.SPUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登陆界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Button loginBtn;
    private Button signUpBtn;

    private EditText userName;
    private EditText userPwd;
    private String pwd;
    private String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.login_activity_layout);
        initDatas();
        autoLogin();
        initViews();
        initEvents();
    }

    private void autoLogin() {

        if (SPUtils.contains(this, "user_name")) {

            user_name = (String) SPUtils.get(this, "user_name", "");
            pwd = (String) SPUtils.get(this, "user_pwd", "");
            login();
        }
    }

    private void initDatas() {
        Bmob.initialize(this, Constants.APPLICATION_ID);
    }

    private void initViews() {
        loginBtn = (Button) findViewById(R.id.btn_sign_in);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);

        userName = (EditText) findViewById(R.id.login_user_name);
        userPwd = (EditText) findViewById(R.id.login_user_pwd);
    }

    private void initEvents() {
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                user_name = userName.getText().toString();
                pwd = userPwd.getText().toString();
                login();
                break;
            case R.id.btn_sign_up:
                Intent it2signup = new Intent();
                it2signup.setClass(LoginActivity.this, SignUpActivity.class);
                startActivity(it2signup);
                break;
        }
    }

    private void login() {
        User bu2 = new User();
        bu2.setUsername(user_name);
        bu2.setPassword(MD5Util.MD5(pwd));
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                User user = new User();
                user = BmobUser.getCurrentUser(LoginActivity.this, User.class);

                SPUtils.put(LoginActivity.this,"user_name",user_name);
                SPUtils.put(LoginActivity.this,"user_pwd", pwd);
                SPUtils.put(LoginActivity.this,"user_id",user.getObjectId());

                Intent it = new Intent();
                it.setClass(LoginActivity.this, HomeActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                it.putExtras(bundle);

                startActivity(it);
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "登录失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });

    }


}
