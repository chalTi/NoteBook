package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.presenters.SplashPresenter;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.SPUtils;
import com.wentongwang.notebook.view.activity.interfaces.SplashView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 欢迎界面
 * Created by Wentong WANG on 2016/6/12.
 */
public class SplashActivity extends BaseActivity implements SplashView{
    private AlphaAnimation myAnima;
    private RelativeLayout root;

    private SplashPresenter mPresenter = new SplashPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        return R.layout.splash_activity_layout;
    }
    protected void initDatas() {
        Bmob.initialize(this, Constants.APPLICATION_ID);
    }

    protected void initViews() {
        root = (RelativeLayout) findViewById(R.id.splash_root);
        initAnimation();
    }

    /**
     * 初始化开始动画
     */
    private void initAnimation() {
        //0表示全透明，1表示不透明
        //alphaanimation渐变动画
        myAnima = new AlphaAnimation(0.0f, 1.0f);
        myAnima.setDuration(2000);
        //界面停留在动画结束状态
        myAnima.setFillAfter(true);
        root.setAnimation(myAnima);
    }
    protected void initEvents() {
        myAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后，实行自动登录
                mPresenter.autoLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 获取Context
     */
    @Override
    public Context getMyContext() {
        return SplashActivity.this;
    }

    /**
     * 跳转到主界面
     */
    @Override
    public void goToHomeActivity() {
        Intent it = new Intent();
        it.setClass(SplashActivity.this, HomeActivity.class);
        startActivity(it);
    }

    /**
     * 跳转到登录界面
     */
    @Override
    public void goToLoginActivity() {
        //自动登录失败，跳转到登录界面
        Intent it = new Intent();
        it.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(it);
    }
}
