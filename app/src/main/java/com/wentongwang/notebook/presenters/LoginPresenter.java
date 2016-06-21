package com.wentongwang.notebook.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.HomeActivity;
import com.wentongwang.notebook.view.activity.interfaces.LoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 连接View与model层的登录界面的presenter
 * Created by Wentong WANG on 2016/6/21.
 */
public class LoginPresenter {
    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    /**
     * 用户登录功能
     */
    public void login() {
        loginView.showPorgressBar();
        User bu2 = new User();
        bu2.setUsername(loginView.getUserName());
        bu2.setPassword(MD5Util.MD5(loginView.getUserPwd()));
        bu2.login(loginView.getMyContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                MyToast.showLong(loginView.getMyContext(), "登录成功");

                User user = new User();
                user = BmobUser.getCurrentUser(loginView.getMyContext(), User.class);
                AccountUtils.saveUserInfos(loginView.getMyContext(), user, loginView.getUserPwd());

                loginView.hidePorgressBar();
                loginView.goToHomeActivity();
            }

            @Override
            public void onFailure(int code, String msg) {
                loginView.hidePorgressBar();
                MyToast.showLong(loginView.getMyContext(), "登录失败：" + msg);
            }
        });
    }

}
