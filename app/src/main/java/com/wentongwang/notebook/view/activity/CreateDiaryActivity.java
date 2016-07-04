package com.wentongwang.notebook.view.activity;

import android.app.Activity;
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
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyActivityManager;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * 创建新的日记
 * Created by Wentong WANG on 2016/6/6.
 */
public class CreateDiaryActivity  extends BaseActivity {
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
                onBackPressed();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocked) {
                    isLocked = false;
                    rightBtn.setImageBitmap(nolock);
                    Toast.makeText(CreateDiaryActivity.this, "日记已解锁", Toast.LENGTH_SHORT).show();
                } else {
                    isLocked = true;
                    rightBtn.setImageBitmap(lock);
                    Toast.makeText(CreateDiaryActivity.this, "日记已上锁", Toast.LENGTH_SHORT).show();
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String diary_content;
                diary_content = contentText.getText().toString();
                String diary_title;
                diary_title = titleText.getText().toString();

                DiaryItem diaryItem = new DiaryItem();
                //获取当前时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
                diaryItem.setDiary_date(sdf.format(new Date()));
                diaryItem.setDiary_title(diary_title);
                diaryItem.setDiary_content(diary_content);
                diaryItem.setIsLocked(isLocked);
                diaryItem.setDiary_user_id(AccountUtils.getUserId(CreateDiaryActivity.this));
                diaryItem.save(CreateDiaryActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        UpdataEvent event = new UpdataEvent();
                        event.setType(UpdataEvent.UPDATE_DIARIES);
                        EventBus.getDefault().post(event);
                        MyActivityManager.getInstance().pop();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateDiaryActivity.this, "操作失败: " + msg, Toast.LENGTH_LONG).show();
                    }
                });



            }
        });
    }



}
