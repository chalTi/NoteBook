package com.wentongwang.notebook.view.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.HomePresenter;
import com.wentongwang.notebook.managers.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.HomeView;
import com.wentongwang.notebook.view.custome.CircleImageView;
import com.wentongwang.notebook.view.fragment.AboutUsFragment;
import com.wentongwang.notebook.view.fragment.DiariesFragment;
import com.wentongwang.notebook.view.fragment.NotesFragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener,HomeView {
    //侧拉菜单界面
    private DrawerLayout drawerLayout;
    //toolbar部分的控件
    private ImageView leftBtn;
    private TextView title;
    private View toolbar;
    //菜单栏里的textViews
    private List<TextView> mTextViews;
    //两个切换的fragments
    private NotesFragment notesFragment;
    private DiariesFragment diariesFragment;

    //左侧菜单栏中用户头像
    private CircleImageView userHead;
    //昵称
    private TextView userNickName;
    //性别
    private ImageView userSex;
    //个人中心按钮
    private Button leftMenuBtn;


    private HomePresenter mPresenter = new HomePresenter(this);
    /**
     * 用于刷新头像的
     * @param event
     */
    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_USER_INFOS) {
            mPresenter.setUserHead();
            mPresenter.setUserNickName();
            mPresenter.setUserSex();
        }
    }
    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_layout;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);

        initDatas();
        initViews();
        initEvents();
        showNotesFragment();
    }

    @Override
    protected void initDatas() {

        mTextViews = new ArrayList<>();
        notesFragment = new NotesFragment();
        diariesFragment = new DiariesFragment();

    }

    @Override
    protected void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        title = (TextView) toolbar.findViewById(R.id.title);

        title.setText("NoteBook");

        drawerLayout = (DrawerLayout) findViewById(R.id.home_activity_drawerlayout);

        mTextViews.add((TextView) findViewById(R.id.left_menu_note));
        mTextViews.add((TextView) findViewById(R.id.left_menu_diary));
        mTextViews.add((TextView) findViewById(R.id.left_menu_setting));
        mTextViews.add((TextView) findViewById(R.id.left_menu_about_us));

        userHead = (CircleImageView) findViewById(R.id.iv_left_menu_user_head);
        mPresenter.setUserHead();
        userNickName = (TextView) findViewById(R.id.tv_left_menu_user_name);
        mPresenter.setUserNickName();
        userSex = (ImageView) findViewById(R.id.iv_user_sex);
        mPresenter.setUserSex();

        leftMenuBtn = (Button) findViewById(R.id.btn_left_menu_userinfo);

    }

    @Override
    protected void initEvents() {
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
                goToUserInfoActivity();
            }
        });

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.left_menu_about_us:
                showAboutUsFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public Context getMyContext() {
        return HomeActivity.this;
    }

    /**
     * 显示便笺列表界面
     */
    @Override
    public void showNotesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, notesFragment);
        transaction.commit();
    }

    /**
     * 显示日记列表界面
     */
    @Override
    public void showDiariesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, diariesFragment);
        transaction.commit();
    }

    /**
     * 显示日记列表界面
     */
    public void showAboutUsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, new AboutUsFragment());
        transaction.commit();
    }


    private long mExitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                MyActivityManager.getInstance().onTerminate();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置用户头像
     */
    @Override
    public void setUserHead(Bitmap bitmap) {
        userHead.setImage(bitmap);
    }

    /**
     * 设置用户昵称
     */
    @Override
    public void setUserNickName(String nickName) {
        userNickName.setText(nickName);
    }

    /**
     * 设置用户性别
     *
     * @param sex
     */
    @Override
    public void setUserSex(Drawable sex) {
        userSex.setImageDrawable(sex);
    }

    /**
     * 跳转到用户中心界面
     */
    @Override
    public void goToUserInfoActivity() {
        Intent it = new Intent();
        it.setClass(HomeActivity.this, UserInfoActivity.class);
        startActivity(it);
    }

    /**
     * 获取缓存文件夹目录
     *
     * @return
     */
    @Override
    public String getCachePath() {
        return getCacheDir().getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }
}
