package com.wentongwang.notebook.presenters;


import android.graphics.Bitmap;

import com.wentongwang.notebook.business.UserBiz;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.LoginView;


/**
 * 连接View与model层的登录界面的presenter
 * Created by Wentong WANG on 2016/6/21.
 */
public class LoginPresenter {
    private LoginView loginView;
    private UserBiz userBiz;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.userBiz = new UserBiz();
    }

    /**
     * 用户登录功能
     */
    public void login() {
        loginView.showPorgressBar();
        userBiz.login(loginView.getMyContext(), loginView.getUserName(), loginView.getUserPwd(), new UserBiz.OnResponseListener() {
            @Override
            public void onSuccess(Object response) {
                loginView.hidePorgressBar();
                loginView.goToHomeActivity();
            }

            @Override
            public void onFailure(String msg) {
                loginView.hidePorgressBar();
                MyToast.showLong(loginView.getMyContext(), "登录失败：" + msg);
            }
        });
    }

}
