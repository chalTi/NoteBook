package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import com.wentongwang.notebook.view.BaseView;

/**
 *
 * Created by Wentong WANG on 2016/6/22.
 */
public interface UserInfoView extends BaseView{

    /**
     * 设置用户名
     * @param userName
     */
    void setUserName(String userName);
    /**
     * 设置用户昵称
     */
    void setUserNickName(String userNickName);

    /**
     * 设置用户邮箱
     * @param email
     */
    void setUserEmail(String email);

    /**
     * 设置用户性别
     * @param sex
     */
    void setUserSex(String sex);

    /**
     * 设置日记密码
     * @param pwd
     */
    void setUserDiaryPwd(String pwd);

    /**
     * 设置用户头像
     */
    void setUserHead(Bitmap bitmap);

    /**
     * 跳转到登录界面
     */
    void goToLoginActivity();

    /**
     * 获取缓存文件夹目录
     * @return
     */
    String getCachePath();

    /**
     * 刷新其他界面的头像
     */
    void toUpdateUserInfo();

    /**
     * 获取布局加载器
     * @return
     */
    LayoutInflater getLayoutInflater();

    /**
     * 根据资源id获取drawable
     * @param id
     * @return
     */
    Drawable getDrawableByResource(int id);
}
