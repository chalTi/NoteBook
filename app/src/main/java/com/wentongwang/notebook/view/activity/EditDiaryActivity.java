package com.wentongwang.notebook.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.EditDiaryPresenter;
import com.wentongwang.notebook.managers.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.EditDiaryView;

import org.greenrobot.eventbus.EventBus;

/**
 * 修改日记界面
 * Created by Wentong WANG on 2016/6/8.
 */
public class EditDiaryActivity extends BaseActivity implements View.OnClickListener, EditDiaryView {
    private View toolbar;
    private TextView title;
    private ImageView leftBtn;

    private Button editBtn;

    private EditText text;

    private DiaryItem thisDiary;

    private boolean onEdit = false;

    //进度条
    private View progressBar;

    private EditDiaryPresenter mPresenter = new EditDiaryPresenter(this);

    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.edit_diray_activity_layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        initViews();
        initEvents();
    }

    @Override
    protected void initDatas() {
        Bundle bundle = getIntent().getExtras();
        thisDiary = (DiaryItem) bundle.getSerializable("my_diary");
    }

    @Override
    protected void initViews() {

        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        text = (EditText) findViewById(R.id.note_content);
        mPresenter.initDiary();

        editBtn = (Button) findViewById(R.id.edit_btn);

        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void initEvents() {

        leftBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                goBack();
                break;
            case R.id.right_btn:

                break;
            case R.id.edit_btn:
                mPresenter.edit();
                break;
        }
    }

    /**
     * 获取当前界面的Context
     *
     * @return context
     */
    @Override
    public Context getMyContext() {
        return EditDiaryActivity.this;
    }

    /**
     * 获取日记内容
     *
     * @return
     */
    @Override
    public String getDiaryContent() {
        return text.getText().toString();
    }

    /**
     * 完成任务后返回
     */
    @Override
    public void finishAndBack() {
        UpdataEvent event = new UpdataEvent();
        event.setType(UpdataEvent.UPDATE_DIARIES);
        EventBus.getDefault().post(event);
        MyActivityManager.getInstance().pop();
        onBackPressed();
    }

    /**
     * 返回
     */
    @Override
    public void goBack() {
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

    /**
     * 从意图中获取日记
     *
     * @return
     */
    @Override
    public DiaryItem getDiaryFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return (DiaryItem) bundle.getSerializable("my_diary");
        } else {
            return null;
        }
    }

    /**
     * 设置日记内容
     *
     * @param content
     * @param t
     */
    @Override
    public void setDiary(String content, String t) {
        text.setText(content);
        text.setEnabled(false);

        title.setText(t);
    }


    /**
     * 开启编辑
     */
    @Override
    public void beginEdit() {
        //EditText可编辑状态
        text.setEnabled(true);
        text.setSelection(text.getText().toString().length());
        //弹出软键盘的操作
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        editBtn.setBackground(getResources().getDrawable(R.drawable.confirm_btn));
        onEdit = true;
    }

    /**
     * 完成编辑
     */
    @Override
    public void finishEdit() {
        text.setEnabled(false);
        editBtn.setBackground(getResources().getDrawable(R.drawable.edit_btn));
        onEdit = false;
    }

    /**
     * 判断是否是可编辑状态
     *
     * @return
     */
    @Override
    public boolean canEdit() {
        return onEdit;
    }
}