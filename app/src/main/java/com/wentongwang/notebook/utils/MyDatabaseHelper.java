package com.wentongwang.notebook.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.NoteItem;

/**
 * 创建数据库中表的类
 * Created by Wentong WANG on 2016/6/6.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String NOTE_TB_NAME = "note_info";
    public static final String DIARY_TB_NAME = "diary_info";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建便签表
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        NOTE_TB_NAME + "(" +
                        NoteItem.ID + " integer primary key autoincrement," +
                        NoteItem.NOTE_DATE + " varchar," +
                        NoteItem.NOTE_CONTENT + " varchar" +
                        ")"
        );
        Log.e("Database", "onCreate");

        //创建日记表
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        DIARY_TB_NAME + "(" +
                        DiaryItem.ID + " integer primary key autoincrement," +
                        DiaryItem.DIARY_DATE + " varchar," +
                        DiaryItem.DIARY_TITLE + " varchar," +
                        DiaryItem.DIARY_CONTENT + " varchar," +
                        DiaryItem.DIARY_IS_LOCKED + " varchar" +
                        ")"
        );
        Log.e("Database", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DIARY_TB_NAME);
        onCreate(db);
        Log.e("Database", "onUpgrade");
    }
}
