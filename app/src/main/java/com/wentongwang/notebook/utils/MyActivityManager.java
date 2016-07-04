package com.wentongwang.notebook.utils;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Activity栈堆管理，用来实现APP完全退出
 * Created by Wentong WANG on 2016/7/4.
 */
public class MyActivityManager {

    private List<Activity> activities = new ArrayList<Activity>();

    private static MyActivityManager instance = new MyActivityManager();

    public static MyActivityManager getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 将一个Activity出栈
     */
    public void pop() {
        if (activities != null && activities.size() > 0) {
            activities.remove(activities.size() - 1).finish();
        }
    }


    /**
     * 关闭所有Activity并退出
     */
    public void onTerminate() {
        Iterator<Activity> e = activities.iterator();
        while(e.hasNext()){
            Activity element = e.next();
            e.remove();
            element.finish();
        }
        System.exit(0);
    }
}
