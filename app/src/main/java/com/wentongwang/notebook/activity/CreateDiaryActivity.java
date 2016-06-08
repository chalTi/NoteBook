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
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.DatabaseUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by Wentong WANG on 2016/6/6.
 */
public class CreateDiaryActivity  extends Activity {

    private View toolbar;
    private TextView title;
    private ImageView leftBtn;
    private Button confirmBtn;

    private EditText titleText;
    private EditText contentText;

    private DatabaseUtils databaseUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_diary_activity_layout);

        databaseUtils = new DatabaseUtils(getBaseContext());

        initViews();
        initEvents();
    }

    private void initViews() {
        toolbar = findViewById(R.id.top_toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("创建新的日记");
        leftBtn = (ImageView) toolbar.findViewById(R.id.left_btn);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_btn);
        leftBtn.setImageBitmap(bitmap);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);

        contentText = (EditText) findViewById(R.id.diary_content);
        titleText = (EditText) findViewById(R.id.diary_title);

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
                String diary_content;
                diary_content = contentText.getText().toString();
                String diary_title;
                diary_title = titleText.getText().toString();

                DiaryItem diaryItem = new DiaryItem();
                //获取当前时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd " + "hh:mm:ss");
                diaryItem.setDiary_date(sdf.format(new Date()));
                diaryItem.setDiary_title(diary_title);
                diaryItem.setDiary_content(diary_content);
                diaryItem.setIsLocked(false);
                Long id = databaseUtils.saveDiary(diaryItem);
                Log.e("xxxx", "diaryID = " + id);

                databaseUtils.close();
                UpdataEvent event = new UpdataEvent();
                event.setType(UpdataEvent.UPDATE_DIARIES);
                EventBus.getDefault().post(event);
                onBackPressed();
            }
        });
    }

}
