package com.wentongwang.notebook.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentong WANG on 2016/6/6.
 */
public class DatabaseUtils {

    // 数据库名称
    private static String DB_NAME = "mynotes.db";
    // 数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private MyDatabaseHelper dbUtil;

    public DatabaseUtils(Context context) {
        dbUtil = new MyDatabaseHelper(context, DB_NAME, null, DB_VERSION);
        db = dbUtil.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbUtil.close();
    }


    // 添加notes表的记录
    public Long saveNote(NoteItem note) {
        ContentValues values = new ContentValues();
        values.put(NoteItem.NOTE_DATE, note.getNote_date());
        values.put(NoteItem.NOTE_CONTENT, note.getNote_content());
        Long uid = db.insert(MyDatabaseHelper.NOTE_TB_NAME, null, values);
        Log.e("SaveNoteInfo", uid + "");
        return uid;
    }


    //从数据库中查询notes表数据
    public List<NoteItem> getNotes() {
        List<NoteItem> userList = new ArrayList<>();
        Cursor cursor = db.query(MyDatabaseHelper.NOTE_TB_NAME, null, null, null, null,
                null, NoteItem.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            NoteItem note = new NoteItem();
            note.setNote_id(cursor.getString(0));
            note.setNote_date(cursor.getString(1));
            note.setNote_content(cursor.getString(2));
            userList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 更新notes表的记录
    public int UpdateNoteInfo(NoteItem note) {
        ContentValues values = new ContentValues();
        values.put(NoteItem.ID, note.getNote_id());
        values.put(NoteItem.NOTE_DATE, note.getNote_date());
        values.put(NoteItem.NOTE_CONTENT, note.getNote_content());
        int id = db.update(MyDatabaseHelper.NOTE_TB_NAME, values, NoteItem.ID + "="
                + note.getNote_id(), null);
        Log.e("UpdateNoteInfo", id + "");
        return id;
    }


    //对日记操作的


    // 添加Diary表的记录
    public Long saveDiary(DiaryItem diary) {
        ContentValues values = new ContentValues();
        values.put(DiaryItem.DIARY_DATE, diary.getDiary_date());
        values.put(DiaryItem.DIARY_TITLE, diary.getDiary_title());
        values.put(DiaryItem.DIARY_CONTENT, diary.getDiary_content());
        values.put(DiaryItem.DIARY_IS_LOCKED, diary.isLocked());
        Long uid = db.insert(MyDatabaseHelper.DIARY_TB_NAME, null, values);
        Log.e("SaveDiaryInfo", uid + "");
        return uid;
    }


    //从数据库中查询Diary表数据
    public List<DiaryItem> getDiaries() {
        List<DiaryItem> userList = new ArrayList<>();
        Cursor cursor = db.query(MyDatabaseHelper.DIARY_TB_NAME, null, null, null, null,
                null, DiaryItem.ID + " DESC");
        cursor.moveToFirst();
        //不是最后一条，且有值
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            DiaryItem diary = new DiaryItem();
            diary.setDiary_id(cursor.getString(0));
            diary.setDiary_date(cursor.getString(1));
            diary.setDiary_title(cursor.getString(2));
            diary.setDiary_content(cursor.getString(3));
            diary.setIsLocked(cursor.getString(4));
            userList.add(diary);
            //到下一条数据
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 更新Diary表
    public int UpdateDiaryInfo(DiaryItem diary) {
        ContentValues values = new ContentValues();
        values.put(NoteItem.ID, diary.getDiary_id());
        values.put(DiaryItem.DIARY_DATE, diary.getDiary_date());
        values.put(DiaryItem.DIARY_TITLE, diary.getDiary_title());
        values.put(DiaryItem.DIARY_CONTENT, diary.getDiary_content());
        values.put(DiaryItem.DIARY_IS_LOCKED, diary.isLocked());
        int id = db.update(MyDatabaseHelper.DIARY_TB_NAME, values, NoteItem.ID + "="
                + diary.getDiary_id(), null);
        Log.e("UpdateDiaryInfo", id + "");
        return id;
    }


}
