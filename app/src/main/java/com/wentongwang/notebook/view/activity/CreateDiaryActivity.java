package com.wentongwang.notebook.view.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.CreatDiaryPresenter;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.CreatDiaryView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * 创建新的日记
 * Created by Wentong WANG on 2016/6/6.
 */
public class CreateDiaryActivity  extends BaseActivity implements CreatDiaryView{
    //顶部toolbar部分
    private View toolbar;
    private TextView title;
    private ImageView leftBtn;
    private ImageView rightBtn;
    //创建日记的按钮
    private Button confirmBtn;
    //日记部分
    private EditText titleText;
    private EditText contentText;
    private boolean isLocked = false;

    //进度条
    private View progressBar;
    //上锁和不上锁的两个图片
    private Bitmap nolock;
    private Bitmap lock;

    private CreatDiaryPresenter mPresenter = new CreatDiaryPresenter(this);
    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.create_diary_activity_layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, Constants.APPLICATION_ID);
        initDatas();
        initViews();
        initEvents();
    }
    @Override
    protected void initDatas() {
        nolock = BitmapFactory.decodeResource(getResources(), R.drawable.nolock);
        lock = BitmapFactory.decodeResource(getResources(), R.drawable.lock);
    }
    @Override
    protected void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("创建新的日记");
        //设置左侧返回按钮
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);
        //设置toolbar右侧加锁按钮
        rightBtn = (ImageView) toolbar.findViewById(R.id.right_btn);

        rightBtn.setImageBitmap(nolock);
        rightBtn.setVisibility(View.VISIBLE);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);

        contentText = (EditText) findViewById(R.id.diary_content);
        titleText = (EditText) findViewById(R.id.diary_title);


        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void initEvents() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().pop();
                onBackPressed();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.lockDiary();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.creatDiary();
            }
        });
    }


    /**
     * 获取当前界面的Context
     *
     * @return context
     */
    @Override
    public Context getMyContext() {
        return CreateDiaryActivity.this;
    }

    /**
     * 获取日记内容
     *
     * @return
     */
    @Override
    public String getDiaryContent() {
        return contentText.getText().toString();
    }

    /**
     * 获取日记标题
     *
     * @return
     */
    @Override
    public String getDiaryTitle() {
        return titleText.getText().toString();
    }

    /**
     * 返回
     */
    @Override
    public void goBack() {
        UpdataEvent event = new UpdataEvent();
        event.setType(UpdataEvent.UPDATE_DIARIES);
        EventBus.getDefault().post(event);
        MyActivityManager.getInstance().pop();
        onBackPressed();
    }

    /**
     * 显示进度条
     */
    @Override
    public void showPorgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hidePorgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void setLock(boolean is) {
        this.isLocked = is;
    }

    /**
     * 更换锁的图片
     */
    @Override
    public void updateBtnImg() {
        if (isLocked) {
            rightBtn.setImageBitmap(lock);
        } else {
            rightBtn.setImageBitmap(nolock);
        }
    }


}
