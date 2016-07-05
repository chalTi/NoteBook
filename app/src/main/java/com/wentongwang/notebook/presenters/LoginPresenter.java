package com.wentongwang.notebook.presenters;



import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
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
        userBiz.login(loginView.getMyContext(), loginView.getUserName(), loginView.getUserPwd(), new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                loginView.hidePorgressBar();
                if (response.isSucces()) {
                    loginView.goToHomeActivity();
                } else {
                    MyToast.showLong(loginView.getMyContext(), "登录失败：" + response.getMsg());
                }
            }
        });
    }

}
