package com.wentongwang.notebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.DatabaseUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 观看，修改note的界面
 * Created by Wentong WANG on 2016/6/6.
 */
public class EditNoteActivity extends Activity implements View.OnClickListener{
    private View toolbar;
    private TextView title;
    private ImageView leftBtn;

    private Button editBtn;

    private EditText text;
    private DatabaseUtils databaseUtils;

    private NoteItem thisNote;

    private boolean onEdit = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_activity_layout);

        databaseUtils = new DatabaseUtils(getBaseContext());
        initDatas();
        initViews();
        initEvents();
    }

    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        thisNote = (NoteItem) bundle.getSerializable("my_note");
    }

    private void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(thisNote.getNote_date());
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        text = (EditText) findViewById(R.id.note_content);
        text.setText(thisNote.getNote_content());
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd " + "hh:mm:ss");
        thisNote.setNote_date(sdf.format(new Date()));
        thisNote.setNote_content(note_content);

        int id = databaseUtils.UpdateNoteInfo(thisNote);
        Log.e("xxxx", "noteID = " + id + " 修改完成");

        databaseUtils.close();
        //通知界面更新
        EventBus.getDefault().post(new UpdataEvent());
    }
}
