package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Wentong WANG on 2016/6/23.
 */
public interface HomeView {
    Context getMyContext();
    /**
     * 头像设置
     */
    void setUserHead(Bitmap bitmap);

    /**
     * 设置用户昵称
     */
    void setUserNickName(String nickName);

    /**
     * 设置用户性别
     * @param sex
     */
    void setUserSex(Drawable sex);
    /**
     * 跳转到用户中心界面
     */
    void goToUserInfoActivity();

    /**
     * 获取缓存文件夹目录
     * @return
     */
    String getCachePath();

    Resources getResources();

    /**
     * 刷新界面
     * @param currentPage
     */
    void refresh(int currentPage);

    void setCurrentPage(int page);

    /**
     * 显示关于我们
     */
    void showAboutUsFragment();
    /**
     * 设置toolbar标题
     * @param title
     */
    void setTitle(String title);
}
