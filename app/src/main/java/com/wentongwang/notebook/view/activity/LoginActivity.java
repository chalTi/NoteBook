package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.presenters.LoginPresenter;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.LoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登陆界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class LoginActivity extends BaseActivity implements LoginView,View.OnClickListener {
    private Button loginBtn;
    private Button signUpBtn;

    private EditText userName;
    private EditText userPwd;

    private View porgessBar;
    private LoginPresenter mPresenter = new LoginPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.login_activity_layout;
    }
    @Override
    protected void initDatas() {
//        Bmob.initialize(this, Constants.APPLICATION_ID);
    }
    @Override
    protected void initViews() {
        loginBtn = (Button) findViewById(R.id.btn_sign_in);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);

        userName = (EditText) findViewById(R.id.login_user_name);
        userPwd = (EditText) findViewById(R.id.login_user_pwd);

        porgessBar = findViewById(R.id.progress_bar);
    }
    @Override
    protected void initEvents() {
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                mPresenter.login();
                break;
            case R.id.btn_sign_up:
                goToSignUpActivity();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyActivityManager.getInstance().onTerminate();
        }
        return true;
    }

    @Override
    public String getUserName() {
        return userName.getText().toString();
    }

    @Override
    public String getUserPwd() {
        return userPwd.getText().toString();
    }

    @Override
    public Context getMyContext() {
        return LoginActivity.this;
    }

    /**
     * 跳转到主界面
     */
    @Override
    public void goToHomeActivity() {
        Intent it = new Intent();
        it.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(it);
    }

    /**
     * 跳转到注册界面
     */
    @Override
    public void goToSignUpActivity() {
        Intent it = new Intent();
        it.setClass(LoginActivity.this, SignUpActivity.class);
        startActivity(it);
    }

    /**
     * 显示进度条
     */
    @Override
    public void showPorgressBar() {
        porgessBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hidePorgressBar() {
        porgessBar.setVisibility(View.GONE);
    }

}
