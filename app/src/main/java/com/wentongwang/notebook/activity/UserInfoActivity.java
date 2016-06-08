package com.wentongwang.notebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.custome.CircleImageView;
import com.wentongwang.notebook.model.User;

/**
 * 个人信息界面
 * Created by Wentong WANG on 2016/6/7.
 */
public class UserInfoActivity extends Activity {

    private User user;

    private CircleImageView userHead;
    private TextView userName;
    private TextView userNickName;
    private TextView userEmail;
    private TextView userSex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_activity_layout);
        initDatas();
        initViews();
        initEvents();
    }

    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
    }

    private void initViews() {
        userHead = (CircleImageView) findViewById(R.id.iv_user_head);
        userHead.setImage(R.drawable.user_head_defaut);

        userName = (TextView) findViewById(R.id.tv_user_name);
        userName.setText(user.getUsername());

        userNickName = (TextView) findViewById(R.id.tv_user_nickname);
        if (!TextUtils.isEmpty(user.getUser_nickname())) {
            userNickName.setText(user.getUser_nickname());
        } else {
            userNickName.setText(user.getUsername());
        }

        userEmail = (TextView) findViewById(R.id.tv_user_email);
        if (!TextUtils.isEmpty(user.getEmail())) {
            userEmail.setText(user.getEmail());
        } else {
            userEmail.setText("您还没绑定你的邮箱");
        }

        userSex = (TextView) findViewById(R.id.tv_user_sex);
        if (!TextUtils.isEmpty(user.getUser_sex())) {
            userSex.setText(user.getUser_sex());
        } else {
            userSex.setText("男");
        }

    }

    private void initEvents() {

    }

}
