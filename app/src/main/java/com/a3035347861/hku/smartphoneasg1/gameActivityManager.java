package com.a3035347861.hku.smartphoneasg1;

/**
 * Created by SHI Zhongqi on 2016-10-09.
 * Singleton
 */

import java.util.LinkedList;
import android.support.v7.app.AppCompatActivity;

class gameActivityManager {

    private LinkedList<AppCompatActivity> activityLinkedList = new LinkedList<>();
    private static gameActivityManager instance;


    private gameActivityManager() {

    }

    static gameActivityManager getInstance(){
        if(null == instance){
            instance = new gameActivityManager();
        }
        return instance;
    }

    gameActivityManager addActivity(AppCompatActivity activity){
        activityLinkedList.add(activity);
        return instance;
    }


    gameActivityManager finshAllActivities() {
        for (AppCompatActivity activity : activityLinkedList) {
            activity.finish();
        }
        return instance;
    }
}