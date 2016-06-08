package com.wentongwang.notebook.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.DatabaseUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 修改日记界面
 * Created by Wentong WANG on 2016/6/8.
 */
public class EditDiaryActivity extends Activity implements View.OnClickListener{
    private View toolbar;
    private TextView title;
    private ImageView leftBtn;

    private Button editBtn;

    private EditText text;

    private DiaryItem thisDiary;

    private boolean onEdit = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_diray_activity_layout);


        initDatas();
        initViews();
        initEvents();
    }

    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        thisDiary = (DiaryItem) bundle.getSerializable("my_diary");
    }

    private void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(thisDiary.getDiary_title());
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        text = (EditText) findViewById(R.id.note_content);
        text.setText(thisDiary.getDiary_content());
        text.setEnabled(false);

        editBtn = (Button) findViewById(R.id.edit_btn);
    }

    private void initEvents() {

        leftBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.right_btn:

                break;
            case R.id.edit_btn:
                if (!onEdit) {
                    text.setEnabled(true);
                    text.setSelection(text.getText().toString().length());
                    //弹出软键盘的操作
                    InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    editBtn.setBackground(getResources().getDrawable(R.drawable.confirm_btn));
                    onEdit = true;
                } else {
                    text.setEnabled(false);
                    editBtn.setBackground(getResources().getDrawable(R.drawable.edit_btn));
                    onEdit = false;
                    submitText();
                    onBackPressed();

                }
                break;
        }
    }

    private void submitText(){
        String note_content;
        note_content = text.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        thisDiary.setDiary_date(sdf.format(new Date()));
        thisDiary.setDiary_content(note_content);

        thisDiary.update(this, thisDiary.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                //通知界面更新
                UpdataEvent event = new UpdataEvent();
                event.setType(UpdataEvent.UPDATE_DIARIES);
                EventBus.getDefault().post(event);
                onBackPressed();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(EditDiaryActivity.this, "操作失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });

    }
}