package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.managers.MyActivityManager;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.presenters.SignUpPresenter;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.view.activity.interfaces.SignUpView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class SignUpActivity extends Activity implements SignUpView{

    private EditText userName;
    private EditText userPwd;
    private EditText userPwd2;
    private Button singUpBtn;

    private View progressBar;

    private SignUpPresenter mPresenter = new SignUpPresenter(this);
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

        progressBar = findViewById(R.id.progress_bar);
    }

    private void initEvents() {
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行注册功能
                mPresenter.signUp();
            }
        });
    }


    @Override
    public Context getMyContext() {
        return SignUpActivity.this;
    }

    /**
     * 显示进度条
     */
    @Override
    public void showPorgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hidePorgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 返回
     */
    @Override
    public void goBack() {
        MyActivityManager.getInstance().pop();
        onBackPressed();
    }

    /**
     * 获取注册用户名
     *
     * @return
     */
    @Override
    public String getUserName() {
        return userName.getText().toString();
    }

    /**
     * 获取注册密码
     *
     * @return
     */
    @Override
    public String getUserPwd() {
        return userPwd.getText().toString();
    }

    /**
     * 获取注册密码校验
     *
     * @return
     */
    @Override
    public String getUserPwdConfirm() {
        return userPwd2.getText().toString();
    }

    @Override
    public void goToLoginActivity() {
        Intent it = new Intent();
        it.setClass(SignUpActivity.this, LoginActivity.class);
        startActivity(it);
    }
}
