package com.wentongwang.notebook.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.ImageLoader;
import com.wentongwang.notebook.utils.MD5Util;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.HomeActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 用户的业务逻辑
 * Created by Wentong WANG on 2016/6/21.
 */
public class UserBiz {
    /**
     * 更新哪个控件的标识
     */
    public static final int UPDATE_USER_HEAD = 0;
    public static final int UPDATE_USER_NICKNAME = 1;
    public static final int UPDATE_USER_EMAIL = 2;
    public static final int UPDATE_USER_SEX = 3;
    public static final int UPDATE_USER_DIARYPWD = 4;

    public String getUserId(Context context) {
        return AccountUtils.getUserId(context);
    }

    public String getUserName(Context context) {
        return AccountUtils.getUserName(context);
    }

    public String getUserPwd(Context context) {
        return AccountUtils.getUserPwd(context);
    }

    public String getUserNickName(Context context) {
        return AccountUtils.getUserNickName(context);
    }

    public String getUserEmail(Context context) {
        return AccountUtils.getUserEmail(context);
    }

    public String getUserSex(Context context) {
        return AccountUtils.getUserSex(context);
    }

    public String getUserDiaryPwd(Context context) {
        return AccountUtils.getUserDiaryPwd(context);
    }

    public String getUserHeadUrl(Context context) {
        return AccountUtils.getUserHeadUrl(context);
    }


    /**
     * 保存信息到服务器
     *
     * @param context 上下文
     * @param id      信息的编号
     * @param data    信息内容
     */
    public void updateInfos(final Context context, final int id, final String data, final OnResponseListener listener) {
        User p = new User();
        switch (id) {
            case UPDATE_USER_NICKNAME:
                p.setUser_nickname(data);
                break;
            case UPDATE_USER_EMAIL:
                p.setEmail(data);
                break;
            case UPDATE_USER_SEX:
                p.setUser_sex(data);
                break;
            case UPDATE_USER_DIARYPWD:
                p.setUser_diraypwd(data);
                break;
            case UPDATE_USER_HEAD:
                p.setUser_head_url(data);
                break;
        }
        p.update(context, AccountUtils.getUserId(context), new UpdateListener() {
            @Override
            public void onSuccess() {
                //根据修改内容更新界面信息
                saveUserInfos(context, id, data);
                //回调成功接口
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });
    }

    /**
     * 保存用户信息到本地
     *
     * @param context 上下文
     * @param id      信息的编号
     * @param data    信息内容
     */
    public void saveUserInfos(Context context, int id, String data) {
        switch (id) {
            case UPDATE_USER_NICKNAME:
                AccountUtils.saveUserNickName(context, data);
                break;
            case UPDATE_USER_EMAIL:
                AccountUtils.saveUserEmail(context, data);
                break;
            case UPDATE_USER_SEX:
                AccountUtils.saveUserSex(context, data);
                break;
            case UPDATE_USER_DIARYPWD:
                AccountUtils.saveUserDiaryPwd(context, data);
                break;
            case UPDATE_USER_HEAD:
                AccountUtils.saveUserHead(context, data);
                break;
        }
    }


    /**
     * 用户登录
     *
     * @param context  上下文
     * @param userName 用户名
     * @param pwd      密码
     * @param listener 登录的监听器
     */
    public void login(final Context context, String userName, final String pwd, final OnResponseListener listener) {
        User bu2 = new User();
        bu2.setUsername(userName);
        bu2.setPassword(MD5Util.MD5(pwd));
        bu2.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                MyToast.showLong(context, "登录成功");

                User user;
                user = BmobUser.getCurrentUser(context, User.class);
                //将登陆信息保存本地
                AccountUtils.saveUserInfos(context, user, pwd);
                //回调成功接口
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });
    }


    /**
     * 注销
     */
    public void logout(Context context) {
        AccountUtils.clearAllInfos(context);
    }

    /**
     * 注册用户
     * @param context 上下文
     * @param userName 用户名
     * @param userPwd 密码
     */
    public void registre(final Context context, String userName, final String userPwd, final OnResponseListener listener) {
            final User bu = new User();
            bu.setUsername(userName);
            bu.setPassword(MD5Util.MD5(userPwd));
            //注意：不能用save方法进行注册
            bu.signUp(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    User user = new User();
                    user = BmobUser.getCurrentUser(context, User.class);
                    //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                    AccountUtils.saveUserInfos(context, user, userPwd);

                    if (listener != null) {
                        listener.onSuccess(null);
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    if (listener != null) {
                        listener.onFailure(msg);
                    }
                }
            });

    }

    /**
     * 上传头像到服务器
     * @param context 上下文
     * @param savePath 保存路径
     * @param pic 图片文件
     * @param photo 图片
     * @param listener 回复的回调
     */
    public void updateUserHead(final Context context, String savePath, File pic, Bitmap photo, final OnResponseListener listener) {
        try {
            FileOutputStream out = new FileOutputStream(pic);
            photo.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            ImageLoader.getmInstance(context, savePath)
                    .upLoadBitmap(pic.getAbsolutePath(), new ImageLoader.onNetworkListener() {
                        @Override
                        public void onSuccess(String picUrl) {
                            //更新服务器里用户表中的头像
                            updateUserHead(context, picUrl, listener);
                        }

                        @Override
                        public void onSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onFailure(String msg) {
                            if (listener != null) {
                                listener.onFailure(msg);
                            }
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户表单中的头像列
     *
     * @param picUrl 图片的url
     */
    private void updateUserHead(Context context, String picUrl, final OnResponseListener listener) {
        String oldUrl = AccountUtils.getUserHeadUrl(context);
        if (!TextUtils.isEmpty(oldUrl) && !oldUrl.equals("")) {
            //删除服务器上的旧头像
            deleteUserOldHead(context, oldUrl);
        }
        AccountUtils.saveUserHead(context, picUrl);
        User user = new User();
        user.setUser_head_url(picUrl);
        user.update(context, AccountUtils.getUserId(context), new UpdateListener() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });
    }

    /**
     * 删除服务器上的旧头像
     *
     * @param oldUrl 旧头像的url
     */
    private void deleteUserOldHead(Context context, String oldUrl) {
        BmobFile file = new BmobFile();
        file.setUrl(oldUrl);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }

    /**
     * 从服务器获取头像
     *
     * @param savePath 头像保存路径
     */
    public void getUserHeadFromServer(Context context, String savePath, final OnResponseListener listener) {
        String photoUrl = AccountUtils.getUserHeadUrl(context);
        if (!TextUtils.isEmpty(photoUrl) && !photoUrl.equals("")) {
            ImageLoader.getmInstance(context, savePath)
                    .downLoadBitmap(photoUrl, new ImageLoader.onLoadBitmapListener() {
                        @Override
                        public void onLoad(Bitmap bitmap) {
                            if (listener != null) {
                                listener.onSuccess(bitmap);
                            }

                        }
                    });
        }
    }

    /**
     * 服务器回复的回调接口
     */
    public interface OnResponseListener {
        /**
         * 成功
         * @param response 回复的结果
         */
        void onSuccess(Object response);

        /**
         * 失败
         * @param msg 失败描述
         */
        void onFailure(String msg);
    }

}
