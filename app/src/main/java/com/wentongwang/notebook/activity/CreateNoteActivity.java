package com.wentongwang.notebook.activity;

import android.app.Activity;
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
 * 创建新的便签
 * Created by Wentong WANG on 2016/6/3.
 */
public class CreateNoteActivity extends Activity {

    private View toolbar;
    private TextView title;
    private ImageView leftBtn;
    private Button confirmBtn;

    private EditText text;

    private DatabaseUtils databaseUtils;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note_activity_layout);

        databaseUtils = new DatabaseUtils(getBaseContext());

        initViews();
        initEvents();
    }

    private void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("创建新的便签");
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);

        text = (EditText) findViewById(R.id.note_content);
    }


    private void initEvents() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note_content;
                note_content = text.getText().toString();
                NoteItem noteItem = new NoteItem();
                //获取当前时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd " + "hh:mm:ss");
                noteItem.setNote_date(sdf.format(new Date()));
                noteItem.setNote_content(note_content);

                Long id = databaseUtils.saveNote(noteItem);
                Log.e("xxxx", "noteID = " + id);

                databaseUtils.close();
                UpdataEvent event = new UpdataEvent();
                event.setType(UpdataEvent.UPDATE_NOTES);
                EventBus.getDefault().post(event);
                onBackPressed();
            }
        });
    }

}
