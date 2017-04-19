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

package com.zql.android.purelauncher.presentation;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.provider.Settings;

import com.zql.android.purelauncher.BuildConfig;
import com.zql.android.purelauncher.presentation.db.entity.DaoMaster;
import com.zql.android.purelauncher.presentation.db.entity.DaoSession;
import com.zqlite.android.logly.Logly;

import org.greenrobot.greendao.database.Database;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherApplication extends Application {

    public static String APPLICATION_NAME = BuildConfig.APPLICATION_NAME;

    private static LauncherApplication sInstance;

    private DaoSession daoSession;

    public static final boolean ENCRYPTED = false;
    @Override
    public void onCreate() {
        super.onCreate();
        if(sInstance == null){
            sInstance = this;
        }
        initApplication();
    }

    private void initApplication(){
        Logly.setGlobalTag(new Logly.Tag(Logly.FLAG_THREAD_NAME,APPLICATION_NAME,Logly.DEBUG));
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Logly.d("############# MANUFACTURER #############" + Build.MANUFACTURER);
    }

    public static LauncherApplication own(){
        return sInstance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
