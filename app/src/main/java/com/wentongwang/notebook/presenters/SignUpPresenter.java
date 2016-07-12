package com.wentongwang.notebook.presenters;

import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.SignUpView;

/**
 * 注册界面交互类
 * Created by Wentong WANG on 2016/6/23.
 */
public class SignUpPresenter {

    private SignUpView signUpView;
    private UserBiz userBiz;
    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
        this.userBiz = new UserBiz();
    }

    /**
     * 注册
     */
    public void signUp() {
        if (signUpView.getUserPwd().equals(signUpView.getUserPwdConfirm())) {
            signUpView.showPorgressBar();
            userBiz.registre(signUpView.getMyContext(), signUpView.getUserName(), signUpView.getUserPwd(), new OnResponseListener() {
                @Override
                public void onResponse(Response response) {
                    signUpView.hidePorgressBar();
                    if (response.isSucces()) {
                        MyToast.showShort(signUpView.getMyContext(), "注册成功");
                        signUpView.goToLoginActivity();
                    } else {
                        MyToast.showLong(signUpView.getMyContext(),"注册失败" + response.getMsg());
                    }
                }
            });
        }

    }
}
