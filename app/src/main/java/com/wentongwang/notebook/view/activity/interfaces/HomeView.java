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
     * 替换成notes的fragment
     */
    void showNotesFragment();
    /**
     * 替换成Diaries的fragment
     */
    void showDiariesFragment();

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
}
