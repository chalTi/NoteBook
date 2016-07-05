package com.wentongwang.notebook.presenters;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.UserInfoView;

import java.io.File;


/**
 * 用户信息的presenter
 * Created by Wentong WANG on 2016/6/22.
 */
public class UserInfoPresenter {

    private UserInfoView userInfoView;

    private UserBiz userBiz;

    public UserInfoPresenter(UserInfoView userInfoView) {
        this.userInfoView = userInfoView;
        this.userBiz = new UserBiz();
    }

    /**
     * 在View中设置用户信息
     */
    public void setUserInfo() {
        userInfoView.setUserName(userBiz.getUserName(userInfoView.getMyContext()));
        userInfoView.setUserNickName(userBiz.getUserNickName(userInfoView.getMyContext()));
        userInfoView.setUserEmail(userBiz.getUserEmail(userInfoView.getMyContext()));
        userInfoView.setUserSex(userBiz.getUserSex(userInfoView.getMyContext()));
        userInfoView.setUserDiaryPwd(userBiz.getUserPwd(userInfoView.getMyContext()));
        setUserHead(userInfoView.getCachePath());
    }

    /**
     * 更新信息的网络请求
     *
     * @param id   属于哪一个信息
     * @param data 修改内容
     */
    public void upDateInfo(final int id, final String data) {
        userInfoView.showProgressBar();
        //更新用户在服务器上的表单
        userBiz.updateInfos(userInfoView.getMyContext(), id, data, new OnResponseListener() {
            @Override
            public void onSuccess(Object response) {
                //刷新Ui界面
                notifyChanges(id);
                userInfoView.hideProgressBar();
                userInfoView.toUpdateUserInfo();
            }

            @Override
            public void onFailure(String msg) {
                userInfoView.hideProgressBar();
                MyToast.showLong(userInfoView.getMyContext(), "更新失败：" + msg);
            }
        });
    }

    /**
     * 界面信息更新
     *
     * @param id 更新哪个控件
     */
    private void notifyChanges(int id) {
        String data;
        switch (id) {
            case UserBiz.UPDATE_USER_NICKNAME:
                data = userBiz.getUserNickName(userInfoView.getMyContext());
                userInfoView.setUserNickName(data);
                break;
            case UserBiz.UPDATE_USER_EMAIL:
                data = userBiz.getUserEmail(userInfoView.getMyContext());
                userInfoView.setUserEmail(data);
                break;
            case UserBiz.UPDATE_USER_SEX:
                data = userBiz.getUserSex(userInfoView.getMyContext());
                userInfoView.setUserSex(data);
                break;
            case UserBiz.UPDATE_USER_DIARYPWD:
                data = userBiz.getUserDiaryPwd(userInfoView.getMyContext());
                userInfoView.setUserDiaryPwd(data);
                break;
            case UserBiz.UPDATE_USER_HEAD:
                setUserHead(userInfoView.getCachePath());
                break;
        }
    }

    /**
     * 用户的注销
     */
    public void logout() {
        userBiz.logout(userInfoView.getMyContext());
        userInfoView.goToLoginActivity();
    }

    /**
     * 上传头像到服务器
     *
     * @param pic   图片保存路径
     * @param photo 图片
     */
    public void uploadUserHead(File pic, Bitmap photo) {
        userInfoView.showProgressBar();
        userBiz.updateUserHead(userInfoView.getMyContext(), userInfoView.getCachePath(), pic, photo, new OnResponseListener() {


            @Override
            public void onSuccess(Object response) {
                userInfoView.hideProgressBar();
                setUserHead(userInfoView.getCachePath());
            }

            @Override
            public void onFailure(String msg) {
                userInfoView.hideProgressBar();
                MyToast.showLong(userInfoView.getMyContext(), "修改头像失败" + msg);
            }
        });
    }


    /**
     * 从服务器获取头像
     *
     * @param savePath 头像保存路径
     */
    public void setUserHead(String savePath) {
        userInfoView.showProgressBar();
        userBiz.getUserHeadFromServer(userInfoView.getMyContext(), savePath, new OnResponseListener() {

            @Override
            public void onSuccess(Object response) {
                if (response instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) response;
                    userInfoView.setUserHead(bitmap);
                    userInfoView.toUpdateUserInfo();
                }
                userInfoView.hideProgressBar();
            }

            @Override
            public void onFailure(String msg) {
                userInfoView.hideProgressBar();
                MyToast.showLong(userInfoView.getMyContext(), "获取头像失败" + msg);
            }
        });
    }

    /**
     * 创建修改信息的对话框
     * @param chageType
     * @return
     */
    public AlertDialog buildeChangeInfoDialog(final int chageType) {
        View layout;
        final EditText et_pwd;
        final EditText et_change;
        ImageView icon;
        String dialogTitle = "";
        //载入,初始化自定义对话框布局
        layout = userInfoView.getLayoutInflater().inflate(R.layout.change_info_dialog_layout, null);
        et_pwd = (EditText) layout.findViewById(R.id.ed_pwd_confirm);
        et_change = (EditText) layout.findViewById(R.id.ed_change_info);
        icon = (ImageView) layout.findViewById(R.id.iv_change_info_icon);
        switch (chageType) {
            case UserBiz.UPDATE_USER_DIARYPWD:
                dialogTitle = "修改日记密码";
                et_change.setHint("新的日记密码");
                Drawable drawable1 = userInfoView.getDrawableByResource(R.drawable.diary_pwd);
                icon.setImageDrawable(drawable1);
                break;
            case UserBiz.UPDATE_USER_EMAIL:
                dialogTitle = "修改邮箱";
                et_change.setHint("新的邮箱");
                Drawable drawable2 = userInfoView.getDrawableByResource(R.drawable.email);
                icon.setImageDrawable(drawable2);
                break;
            case UserBiz.UPDATE_USER_NICKNAME:
                dialogTitle = "修改昵称";
                et_change.setHint("新的昵称");
                Drawable drawable3 = userInfoView.getDrawableByResource(R.drawable.nickname);
                icon.setImageDrawable(drawable3);
                break;
        }
        //设置对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(userInfoView.getMyContext())
                .setTitle(dialogTitle)
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = et_pwd.getText().toString();
                        if (pwd.equals(AccountUtils.getUserPwd(userInfoView.getMyContext()))) {
                            upDateInfo(chageType, et_change.getText().toString());
                        } else {
                            Toast.makeText(userInfoView.getMyContext(), "密码不正确，重新输入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //弹出对话框
        return builder.create();
    }
}
