package com.wentongwang.notebook.presenters;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.HomeView;

/**
 * 主界面的交互类
 * Created by Wentong WANG on 2016/6/23.
 */
public class HomePresenter {

    private HomeView homeView;
    private UserBiz userBiz;
    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
        this.userBiz = new UserBiz();
    }


    /**
     * 设置用户头像
     */
    public void setUserHead() {
        userBiz.getUserHeadFromServer(homeView.getMyContext(), homeView.getCachePath(), new OnResponseListener() {
            @Override
            public void onSuccess(Object response) {
                if (response instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) response;
                    homeView.setUserHead(bitmap);
                }

            }

            @Override
            public void onFailure(String msg) {
                MyToast.showLong(homeView.getMyContext(), "设置头像失败:" + msg);
            }
        });
    }

    /**
     * 设置用户昵称
     */
    public void setUserNickName() {
        String nickName = AccountUtils.getUserNickName(homeView.getMyContext());
        if (!TextUtils.isEmpty(nickName)) {
            homeView.setUserNickName(nickName);
        } else {
            homeView.setUserNickName(AccountUtils.getUserName(homeView.getMyContext()));
        }
    }

    /**
     * 设置用户性别
     */
    public void setUserSex(){
        String sex = AccountUtils.getUserSex(homeView.getMyContext());
        if (!TextUtils.isEmpty(sex) && !sex.equals("")) {
            if (sex.equals("女"))
                homeView.setUserSex(homeView.getResources().getDrawable(R.drawable.female));
            else
                homeView.setUserSex(homeView.getResources().getDrawable(R.drawable.male));
        } else {
            homeView.setUserSex(homeView.getResources().getDrawable(R.drawable.male));
        }
    }
}
