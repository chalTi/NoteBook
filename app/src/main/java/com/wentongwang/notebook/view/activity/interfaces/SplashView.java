package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

/**
 *
 * Created by Wentong WANG on 2016/6/21.
 */
public interface SplashView {
    /**
     * 获取Context
     */
    Context getMyContext();
    /**
     * 跳转到主界面
     */
    void goToHomeActivity();

    /**
     * 跳转到登录界面
     */
    void goToLoginActivity();
}
