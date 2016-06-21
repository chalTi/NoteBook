package com.wentongwang.notebook.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.utils.SPUtils;
import com.wentongwang.notebook.view.activity.LoginActivity;
import com.wentongwang.notebook.view.activity.interfaces.SplashView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wentong WANG on 2016/6/21.
 */
public class SplashPresenter {

    private SplashView splashView;
    private String user_name;
    private String user_pwd;

    public SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
    }

    /**
     * 判断自动登录
     */
    public void autoLogin(){
        if (SPUtils.contains(splashView.getMyContext(), "user_name")) {
            //本地有记录，实行登录
            user_name = AccountUtils.getUserName(splashView.getMyContext());
            user_pwd = AccountUtils.getUserPwd(splashView.getMyContext());
            login();
        } else {
            //本地无记录，跳转到登录界面
            splashView.goToLoginActivity();
        }
    }

    /**
     * 用户登录
     */
    private void login() {
        User bu2 = new User();
        bu2.setUsername(user_name);
        bu2.setPassword(MD5Util.MD5(user_pwd));
        bu2.login(splashView.getMyContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                MyToast.showLong(splashView.getMyContext(), "登录成功");

                User user = new User();
                user = BmobUser.getCurrentUser(splashView.getMyContext(), User.class);
                Log.i("xxxx", "user_sex " + user.getUser_sex() + "   diaryPwd " + user.getUser_diraypwd());
                //将用户信息保存本地
                AccountUtils.saveUserInfos(splashView.getMyContext(), user, user_pwd);

                splashView.goToHomeActivity();
            }

            @Override
            public void onFailure(int code, String msg) {
                MyToast.showLong(splashView.getMyContext(), "登录失败：" + msg);
                splashView.goToLoginActivity();
            }
        });

    }
}
