package com.wentongwang.notebook.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.custome.CircleImageView;
import com.wentongwang.notebook.fragment.DiariesFragment;
import com.wentongwang.notebook.fragment.NotesFragment;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.User;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;

    private ImageView leftBtn;
    private TextView title;
    private View toolbar;

    private List<TextView> mTextViews;

    private NotesFragment notesFragment;
    private DiariesFragment diariesFragment;

    private User user;
    //左侧菜单栏中用户头像
    private CircleImageView userHeade;
    private TextView userNickName;

    private Button leftMenuBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        initDatas();
        initViews();
        initEvents();
        showNotesFragment();
    }

    private void initDatas() {
        mTextViews = new ArrayList<>();
        notesFragment = new NotesFragment();
        diariesFragment = new DiariesFragment();

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
    }

    private void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        title = (TextView) toolbar.findViewById(R.id.title);

        title.setText("NoteBook");

        drawerLayout = (DrawerLayout) findViewById(R.id.home_activity_drawerlayout);

        mTextViews.add((TextView) findViewById(R.id.left_menu_note));
        mTextViews.add((TextView) findViewById(R.id.left_menu_diary));
        mTextViews.add((TextView) findViewById(R.id.left_menu_setting));
        mTextViews.add((TextView) findViewById(R.id.left_menu_about_us));

        userHeade = (CircleImageView) findViewById(R.id.iv_left_menu_user_head);
        userHeade.setImage(R.drawable.user_head_defaut);

        userNickName = (TextView) findViewById(R.id.tv_left_menu_user_name);
        if (!TextUtils.isEmpty(user.getUser_nickname())) {
            userNickName.setText(user.getUser_nickname());
        } else {
            userNickName.setText(user.getUsername());
        }
        leftMenuBtn = (Button) findViewById(R.id.btn_left_menu_userinfo);

    }


    private void initEvents() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        if (mTextViews != null && mTextViews.size() > 0) {
            for (int i = 0; i < mTextViews.size(); i++) {
                mTextViews.get(i).setOnClickListener(this);
            }
        }


        leftMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(HomeActivity.this, UserInfoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                it.putExtras(bundle);

                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_menu_note:
                //显示便签的fragment
                showNotesFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.left_menu_diary:
                //显示日记的fragment
                showDiariesFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.left_menu_setting:

                break;
            case R.id.left_menu_about_us:

                break;
        }
    }

    private void showNotesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, notesFragment);
        transaction.commit();
    }

    private void showDiariesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, diariesFragment);
        transaction.commit();
    }
}