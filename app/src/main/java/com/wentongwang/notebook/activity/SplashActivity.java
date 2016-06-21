package com.wentongwang.notebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.SPUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 欢迎界面
 * Created by Wentong WANG on 2016/6/12.
 */
public class SplashActivity extends Activity {
    private String pwd;
    private String user_name;
    private AlphaAnimation myAnima;
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.splash_activity_layout);
        initDatas();
        initViews();
        initAnimation();
        initEvents();
    }

    private void initDatas() {
        Bmob.initialize(this, Constants.APPLICATION_ID);
    }

    private void initViews() {
        root = (RelativeLayout) findViewById(R.id.splash_root);

    }

    private void initEvents() {
        myAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后，实行自动登录
                autoLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initAnimation() {
        //0表示全透明，1表示不透明
        //alphaanimation渐变动画
        myAnima = new AlphaAnimation(0.0f, 1.0f);
        myAnima.setDuration(2000);
        //界面停留在动画结束状态
        myAnima.setFillAfter(true);
        root.setAnimation(myAnima);
    }

    /**
     * 判断是否可以自动登录
     */
    private void autoLogin() {

        if (SPUtils.contains(this, "user_name")) {
            //本地有记录，实行登录
            user_name = AccountUtils.getUserName(this);
            pwd = AccountUtils.getUserPwd(this);
            login();
        } else {
            //本地无记录，跳转到登录界面
            Intent it = new Intent();
            it.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(it);
        }
    }

    /**
     * 用户登录
     */
    private void login() {
        User bu2 = new User();
        bu2.setUsername(user_name);
        bu2.setPassword(MD5Util.MD5(pwd));
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                User user = new User();
                user = BmobUser.getCurrentUser(SplashActivity.this, User.class);
                Log.i("xxxx", "user_sex " + user.getUser_sex() + "   diaryPwd " + user.getUser_diraypwd());
                //将用户信息保存本地
                AccountUtils.saveUserInfos(SplashActivity.this, user, pwd);

                Intent it = new Intent();
                it.setClass(SplashActivity.this, HomeActivity.class);
                startActivity(it);
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(SplashActivity.this, "登录失败: " + msg, Toast.LENGTH_LONG).show();
                //自动登录失败，跳转到登录界面
                Intent it = new Intent();
                it.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });

    }
}
