package com.wentongwang.notebook.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wentongwang.notebook.model.User;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.ImageLoader;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.UserInfoActivity;
import com.wentongwang.notebook.view.activity.interfaces.UserInfoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 用户信息的presenter
 * Created by Wentong WANG on 2016/6/22.
 */
public class UserInfoPresenter {
    /**
     * 更新哪个控件的标识
     */
    public static final int UPDATE_USER_HEAD = 0;
    public static final int UPDATE_USER_NICKNAME = 1;
    public static final int UPDATE_USER_EMAIL = 2;
    public static final int UPDATE_USER_SEX = 3;
    public static final int UPDATE_USER_DIARYPWD = 4;

    private UserInfoView userInfoView;

    public UserInfoPresenter(UserInfoView userInfoView) {
        this.userInfoView = userInfoView;

    }

    /**
     * 在View中设置用户信息
     */
    public void setUserInfo(){
        userInfoView.setUserName(AccountUtils.getUserName(userInfoView.getMyContext()));
        userInfoView.setUserNickName(AccountUtils.getUserNickName(userInfoView.getMyContext()));
        userInfoView.setUserEmail(AccountUtils.getUserEmail(userInfoView.getMyContext()));
        userInfoView.setUserSex(AccountUtils.getUserSex(userInfoView.getMyContext()));
        userInfoView.setUserDiaryPwd(AccountUtils.getUserPwd(userInfoView.getMyContext()));
        setUserHead(userInfoView.getCachePath());
    }

    /**
     * 更新信息的网络请求
     *
     * @param id   属于哪一个信息
     * @param data 修改内容
     */
    public void upDateInfo(final int id, final String data) {
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
        p.update(userInfoView.getMyContext(), AccountUtils.getUserId(userInfoView.getMyContext()), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(userInfoView.getMyContext(), "修改成功!", Toast.LENGTH_LONG).show();
                //根据修改内容更新界面信息
                notifyChanges(id, data);
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(userInfoView.getMyContext(), "修改失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 界面信息更新
     * @param id 更新哪个控件
     * @param data 更新内容
     */
    private void notifyChanges(int id, String data) {
        switch (id) {
            case UPDATE_USER_NICKNAME:
                AccountUtils.saveUserNickName(userInfoView.getMyContext(),data);
                userInfoView.setUserNickName(data);
                break;
            case UPDATE_USER_EMAIL:
                AccountUtils.saveUserEmail(userInfoView.getMyContext(),data);
                userInfoView.setUserEmail(data);
                break;
            case UPDATE_USER_SEX:
                AccountUtils.saveUserSex(userInfoView.getMyContext(), data);
                userInfoView.setUserSex(data);
                break;
            case UPDATE_USER_DIARYPWD:
                AccountUtils.saveUserDiaryPwd(userInfoView.getMyContext(),data);
                userInfoView.setUserDiaryPwd(data);
                break;
            case UPDATE_USER_HEAD:
                AccountUtils.saveUserNickName(userInfoView.getMyContext(), data);

                break;
        }
    }

    /**
     * 用户的注销
     */
    public void logout(){
        AccountUtils.clearAllInfos(userInfoView.getMyContext());
        userInfoView.goToLoginActivity();
    }

    /**
     * 上传头像到服务器
     * @param pic 图片保存路径
     * @param photo 图片
     */
    public void uploadUserHead(File pic, Bitmap photo){
        try {
            FileOutputStream out = new FileOutputStream(pic);
            photo.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            userInfoView.showProgressBar();
            ImageLoader.getmInstance(userInfoView.getMyContext(), userInfoView.getCachePath())
                    .upLoadBitmap(pic.getAbsolutePath(), new ImageLoader.onNetworkListener() {
                        @Override
                        public void onSuccess(String picUrl) {
                            //更新用户表中的头像
                            updateUserHead(picUrl);
                        }

                        @Override
                        public void onSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onFailure(String msg) {
                            userInfoView.hideProgressBar();
                            MyToast.showLong(userInfoView.getMyContext(), "上传头像失败：" + msg);
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
     * @param picUrl 图片的url
     */
    private void updateUserHead(String picUrl) {
        String oldUrl = AccountUtils.getUserHeadUrl(userInfoView.getMyContext());
        if (!TextUtils.isEmpty(oldUrl) && !oldUrl.equals("")) {
            deleteUserOldHead(oldUrl);
        }
        AccountUtils.saveUserHead(userInfoView.getMyContext(), picUrl);
        User user = new User();
        user.setUser_head_url(picUrl);
        user.update(userInfoView.getMyContext(), AccountUtils.getUserId(userInfoView.getMyContext()), new UpdateListener() {
            @Override
            public void onSuccess() {
                //更新表单成功后,重新设置自己的头像
                setUserHead(userInfoView.getCachePath());
                //更新其他界面的头像
                userInfoView.toUpdateUserHead();
                MyToast.showLong(userInfoView.getMyContext(), "修改头像成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                MyToast.showLong(userInfoView.getMyContext(), "修改头像失败：" + msg);
            }
        });
    }

    /**
     * 设置用户头像
     */
    public void setUserHead(String savePath) {
        String photoUrl = AccountUtils.getUserHeadUrl(userInfoView.getMyContext());
        userInfoView.showProgressBar();
        if (!TextUtils.isEmpty(photoUrl) && !photoUrl.equals("")) {
            ImageLoader.getmInstance(userInfoView.getMyContext(), savePath)
                    .downLoadBitmap(photoUrl, new ImageLoader.onLoadBitmapListener() {
                        @Override
                        public void onLoad(Bitmap bitmap) {
                            userInfoView.hideProgressBar();
                            userInfoView.setUserHead(bitmap);
                        }
                    });
        }
    }

    /**
     * 删除服务器上的旧头像
     * @param oldUrl
     */
    private void deleteUserOldHead(String oldUrl) {
        BmobFile file = new BmobFile();
        file.setUrl(oldUrl);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(userInfoView.getMyContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }
}
