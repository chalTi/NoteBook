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
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.presenters.EditNotePresenter;
import com.wentongwang.notebook.managers.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.EditNoteView;

import org.greenrobot.eventbus.EventBus;


/**
 * 观看，修改note的界面
 * Created by Wentong WANG on 2016/6/6.
 */
public class EditNoteActivity extends BaseActivity implements View.OnClickListener,EditNoteView{
    private View toolbar;
    private TextView title;
    private ImageView leftBtn;

    private Button editBtn;

    private EditText text;

    private NoteItem thisNote;

    private boolean onEdit = false;
    //进度条
    private View progressBar;

    private EditNotePresenter mPresenter = new EditNotePresenter(this);
    /**
     * 获取布局
     *
     * @return 布局界面的Id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.edit_note_activity_layout;
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
        thisNote = (NoteItem) bundle.getSerializable("my_note");
    }
    @Override
    protected void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(thisNote.getNote_date());
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        text = (EditText) findViewById(R.id.note_content);
        //初始化
        mPresenter.setNoteContent();

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
        return EditNoteActivity.this;
    }

    /**
     * 获取便签
     *
     * @return
     */
    @Override
    public NoteItem getNoteFromIntent() {
        Bundle bundle = getIntent().getExtras();
        return (NoteItem) bundle.getSerializable("my_note");
    }

    /**
     * 设置便签内容
     *
     * @return
     */
    @Override
    public void setNoteContent(String content) {
        text.setText(content);
        text.setEnabled(false);
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
        MyActivityManager.getInstance().pop();
        onBackPressed();
    }

    /**
     * 修改完成跳转
     */
    @Override
    public void finishAndBack() {
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

    /**
     * 开启编辑
     */
    @Override
    public void beginEdit() {
        //EditText可编辑状态
        text.setEnabled(true);
        text.setSelection(text.getText().toString().length());
        //弹出软键盘的操作
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
