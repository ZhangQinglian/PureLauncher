/*******************************************************************************
 *    Copyright 2017-present, PureLauncher Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.purelauncher.presentation.framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;

import com.zql.android.purelauncher.presentation.LauncherApplication;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public class SharePreferenceMgr {

    public static final String SHARE_NAME = "launcher_share";
    public static final String KEY_APP_WIDGET_ID = "key_app_widget_id";

    private static SharePreferenceMgr sInstance ;

    private SharedPreferences sharedPreferences ;
    private SharePreferenceMgr(){
        sharedPreferences = LauncherApplication.own().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharePreferenceMgr own(){
        if(sInstance == null) {
            sInstance = new SharePreferenceMgr();
        }
        return sInstance;
    }

    public void saveWidgetId(int id){
        sharedPreferences.edit().putInt(KEY_APP_WIDGET_ID,id).apply();
    }

    public int getWidgetId(){
        return  sharedPreferences.getInt(KEY_APP_WIDGET_ID,-1);
    }
    private void saveString(String key,String value){}
    private void saveInt(String key,int value){}


}
