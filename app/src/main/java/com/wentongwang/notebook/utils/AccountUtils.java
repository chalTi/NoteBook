package com.wentongwang.notebook.utils;

import android.content.Context;
import android.text.TextUtils;

import com.wentongwang.notebook.model.User;

/**
 * 用户信息管理类
 * Created by Wentong WANG on 2016/6/12.
 */
public class AccountUtils {

    private static final String USER_NAME = "user_name";
    private static final String USER_PWD = "user_pwd";
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NICK_NAME = "user_nickname";
    private static final String USER_DIARY_PWD = "user_diraypwd";
    private static final String USER_SEX = "user_sex";
    private static final String USER_HEAD_URL = "user_head_url";

    /**
     * 保存用户的所有信息
     * @param context
     * @param user
     * @param user_pwd 用户的登录密码
     */
    public static void saveUserInfos(Context context, User user, String user_pwd) {
        saveUserName(context, user.getUsername());
        saveUserPwd(context, user_pwd);
        saveUserId(context, user.getObjectId());
        saveUserNickName(context, user.getUser_nickname());
        saveUserEmail(context, user.getEmail());
        saveUserDiaryPwd(context, user.getUser_diraypwd());
        saveUserSex(context, user.getUser_sex());
        saveUserHead(context, user.getUser_head_url());
    }


    public static String getUserName(Context context) {
        return (String) SPUtils.get(context, USER_NAME, "");
    }

    public static void saveUserName(Context context, String str) {
        SPUtils.put(context, USER_NAME, str);
    }

    public static String getUserPwd(Context context) {
        return (String) SPUtils.get(context, USER_PWD, "");
    }

    public static void saveUserPwd(Context context, String str) {
        SPUtils.put(context, USER_PWD, str);
    }

    public static String getUserId(Context context) {
        return (String) SPUtils.get(context, USER_ID, "");
    }

    public static void saveUserId(Context context, String str) {
        SPUtils.put(context, USER_ID, str);
    }

    public static String getUserEmail(Context context) {
        return (String) SPUtils.get(context, USER_EMAIL, "");
    }

    public static void saveUserEmail(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            SPUtils.put(context, USER_EMAIL, str);
        }
    }

    public static String getUserNickName(Context context) {
        return (String) SPUtils.get(context, USER_NICK_NAME, "");
    }

    public static void saveUserNickName(Context context, String str) {
        SPUtils.put(context, USER_NICK_NAME, str);
    }

    public static String getUserDiaryPwd(Context context) {
        return (String) SPUtils.get(context, USER_DIARY_PWD, "");
    }

    public static void saveUserDiaryPwd(Context context, String str) {
        SPUtils.put(context, USER_DIARY_PWD, str);
    }

    public static String getUserSex(Context context) {
        return (String) SPUtils.get(context, USER_SEX, "");
    }

    public static void saveUserSex(Context context, String str) {
        SPUtils.put(context, USER_SEX, str);
    }

    public static String getUserHeadUrl(Context context){
        return (String) SPUtils.get(context, USER_HEAD_URL, "");
    }
    public static void saveUserHead(Context context, String str) {
        SPUtils.put(context, USER_HEAD_URL, str);
    }
    public static void clearAllInfos(Context context){
        SPUtils.clear(context);
    }
}
