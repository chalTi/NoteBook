package com.wentongwang.notebook.presenters;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.Response;
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


    private int currentPage;
    private HomeView homeView;
    private UserBiz userBiz;
    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
        this.userBiz = new UserBiz();
    }

    /**
     * 设置ViewPager当前页
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if (currentPage == 0) {
            homeView.setTitle("我的便签");
        } else {
            homeView.setTitle("我的日记");
        }
        homeView.setCurrentPage(currentPage);
    }

    public void showAboutUsFragment(){
        homeView.setTitle("关于");
        homeView.showAboutUsFragment();
    }

    /**
     * 刷新界面内容
     */
    public void refresh(){
        homeView.refresh(currentPage);
    }

    /**
     * 设置用户头像
     */
    public void setUserHead() {
        userBiz.getUserHeadFromServer(homeView.getMyContext(), homeView.getCachePath(), new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.isSucces()) {
                    Bitmap bitmap = response.getBitmap();
                    homeView.setUserHead(bitmap);
                } else {
                    MyToast.showLong(homeView.getMyContext(), "设置头像失败:" + response.getMsg());
                }
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
