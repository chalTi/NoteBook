package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

import com.wentongwang.notebook.view.BaseView;

/**
 * 登录界面View的功能
 * Created by Wentong WANG on 2016/6/21.
 */
public interface LoginView extends BaseView{

    /**
     * 获取用户名
     * @return 用户名
     */
    String getUserName();

    /**
     * 获取用户密码
     * @return 密码
     */
    String getUserPwd();

    /**
     * 跳转到主界面
     */
    void goToHomeActivity();

    /**
     * 跳转到注册界面
     */
    void goToSignUpActivity();

}
