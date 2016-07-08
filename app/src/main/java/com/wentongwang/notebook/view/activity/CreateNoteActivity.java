package com.wentongwang.notebook.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;

import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.CreatNotePresenter;
import com.wentongwang.notebook.managers.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.CreatNoteView;
import com.wentongwang.notebook.view.custome.AudioRecoderButton;

import org.greenrobot.eventbus.EventBus;


/**
 * 创建新的便签
 * Created by Wentong WANG on 2016/6/3.
 */
public class CreateNoteActivity extends BaseActivity implements CreatNoteView {

    private View toolbar;
    private TextView title;
    private ImageView leftBtn;
    private Button confirmBtn;

    private AudioRecoderButton audioRecoderButton;

    private EditText text;

    //进度条
    private View progressBar;

    private CreatNotePresenter mPresenter = new CreatNotePresenter(this);
    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.create_note_activity_layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, Constants.APPLICATION_ID);
        initViews();
        initEvents();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {

    }
    @Override
    protected void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("创建新的便签");
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);

        text = (EditText) findViewById(R.id.note_content);

        progressBar = findViewById(R.id.progress_bar);

        audioRecoderButton = (AudioRecoderButton) findViewById(R.id.audio_recoder);
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

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.creatNote();
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
        return CreateNoteActivity.this;
    }

    /**
     * 获取日记内容
     *
     * @return
     */
    @Override
    public String getNoteContent() {
        return text.getText().toString();
    }

    /**
     * 返回
     */
    @Override
    public void goBack() {
        UpdataEvent event = new UpdataEvent();
        event.setType(UpdataEvent.UPDATE_NOTES);
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
}
