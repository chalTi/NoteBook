package com.wentongwang.notebook.view.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.ImageLoader;
import com.wentongwang.notebook.view.custome.CircleImageView;
import com.wentongwang.notebook.view.fragment.DiariesFragment;
import com.wentongwang.notebook.view.fragment.NotesFragment;
import com.wentongwang.notebook.utils.AccountUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;

    private ImageView leftBtn;
    private TextView title;
    private View toolbar;

    private List<TextView> mTextViews;

    private NotesFragment notesFragment;
    private DiariesFragment diariesFragment;

    private String user_nickname;
    //左侧菜单栏中用户头像
    private CircleImageView userHead;
    private TextView userNickName;

    private Button leftMenuBtn;

    /**
     * 用于刷新头像的
     * @param event
     */
    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_USER_INFOS) {
            setUserHead();
            userNickName.setText(AccountUtils.getUserNickName(HomeActivity.this));
        }
    }
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
        //注册EventBus
        EventBus.getDefault().register(this);

        mTextViews = new ArrayList<>();
        notesFragment = new NotesFragment();
        diariesFragment = new DiariesFragment();

        user_nickname = AccountUtils.getUserNickName(this);

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

        userHead = (CircleImageView) findViewById(R.id.iv_left_menu_user_head);
        setUserHead();

        userNickName = (TextView) findViewById(R.id.tv_left_menu_user_name);
        if (!TextUtils.isEmpty(user_nickname)) {
            userNickName.setText(user_nickname);
        } else {
            userNickName.setText(AccountUtils.getUserName(this));
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

    /**
     * 显示便笺列表界面
     */
    private void showNotesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, notesFragment);
        transaction.commit();
    }

    /**
     * 显示日记列表界面
     */
    private void showDiariesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_activity_container, diariesFragment);
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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置用户头像
     */
    private void setUserHead() {
        String photoUrl = AccountUtils.getUserHeadUrl(HomeActivity.this);
        if (!TextUtils.isEmpty(photoUrl) && !photoUrl.equals("")) {
            ImageLoader.getmInstance(HomeActivity.this, getCacheDir().getAbsolutePath())
                    .downLoadBitmap(photoUrl, new ImageLoader.onLoadBitmapListener() {
                        @Override
                        public void onLoad(Bitmap bitmap) {
                            userHead.setImage(bitmap);
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }
}
