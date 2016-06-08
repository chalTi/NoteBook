package com.wentongwang.notebook.model;
import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息封装类
 * Created by Wentong WANG on 2016/6/7.
 */
public class User extends BmobUser implements Serializable{

    private String user_nickname;
    private String user_sex;
    private String user_diraypwd;

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_diraypwd() {
        return user_diraypwd;
    }

    public void setUser_diraypwd(String user_diraypwd) {
        this.user_diraypwd = user_diraypwd;
    }
}
