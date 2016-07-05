package com.wentongwang.notebook.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
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
    private UserBiz userBiz;

    private String user_name;
    private String user_pwd;

    public SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
        userBiz = new UserBiz();
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

        userBiz.login(splashView.getMyContext(), user_name, user_pwd, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.isSucces()) {
                    MyToast.showShort(splashView.getMyContext(), "登录成功");
                    splashView.goToHomeActivity();
                } else {
                    MyToast.showShort(splashView.getMyContext(), "登录失败：" + response.getMsg());
                    splashView.goToLoginActivity();
                }
            }
        });
    }
}
