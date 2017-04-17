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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.processor.AppProcessor;
import com.zql.android.purelauncher.adapter.model.utils.Trans2PinYin;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class AppBridge implements AppProcessor.Bridge {

    private final int cacheSize = 8 * 1024*1024;
    private List<ResolveInfo> resolveInfoList = new ArrayList<>();
    private PackageManager packageManager;
    private Map<String,String> appNameMap = new HashMap<>();
    LruCache<String,Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public AppBridge(){
        init();
    }

    @Override
    public List<AppAction> getAppActions(String key) {
        List<AppAction> appActions = new ArrayList<>();

        if(packageManager == null){
            packageManager = LauncherApplication.own().getPackageManager();
        }

        if(resolveInfoList == null){
           updatePackageInfos();
        }
        for(int i = 0;i<resolveInfoList.size();i++){
            ResolveInfo resolveInfo = resolveInfoList.get(i);
            String packageName = resolveInfo.activityInfo.packageName;
            String appName = appNameMap.get(resolveInfo.activityInfo.packageName);
            if(appName == null){
                appName = (String) resolveInfo.loadLabel(packageManager);
                appNameMap.put(resolveInfo.activityInfo.packageName,appName);
            }
            if(packageName.toUpperCase().contains(key.toUpperCase())
                    || appName.toUpperCase().contains(key.toUpperCase())
                    || Trans2PinYin.getInstance().convertAll(appName).contains(key.toLowerCase())){
                    appActions.add(new AppAction(appName,packageName));
            }

        }

        return appActions;
    }

    @Override
    public void loadAppLogo(final String packageName, final Object imageView) {
        AsyncTask<String,Void,Drawable> asyncTask = new AsyncTask<String,Void,Drawable>() {

            @Override
            protected Drawable doInBackground(String... params) {
                Drawable drawable = null;
                Bitmap bitmap = lruCache.get(params[0]);
                if(bitmap != null){
                    drawable = new BitmapDrawable(LauncherApplication.own().getResources(),bitmap);
                    return drawable;
                }
                try {
                    drawable = packageManager.getApplicationIcon(packageName);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    lruCache.put(packageName,bitmapDrawable.getBitmap());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return drawable;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                ((ImageView)imageView).setImageDrawable(drawable);
            }
        };
        asyncTask.execute(packageName);
    }

    @Override
    public void openApp(final String packgeName) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = packageManager.getLaunchIntentForPackage(packgeName);
                LauncherApplication.own().startActivity(intent);
            }
        },300);

    }

    @Override
    public void init() {
        Logly.d("AppBridge init");
        AppReceiver receiver = new AppReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        intentFilter.addAction(Intent.ACTION_PACKAGES_SUSPENDED);
        intentFilter.addAction(Intent.ACTION_PACKAGES_UNSUSPENDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        LauncherApplication.own().registerReceiver(receiver,intentFilter);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                packageManager = LauncherApplication.own().getPackageManager();

                resolveInfoList = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
                for(int i = 0;i<resolveInfoList.size();i++){
                    ResolveInfo resolveInfo = resolveInfoList.get(i);
                    String appName = (String) resolveInfo.loadLabel(packageManager);
                    Logly.d("appName : " + appName + "   package : " + resolveInfo.activityInfo.packageName);
                    appNameMap.put(resolveInfo.activityInfo.packageName,appName);
                }

            }
        });
    }

    private class AppReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Logly.d("   app receiver : " + intent.getAction() + "   " + intent.getDataString());
            final String p = intent.getDataString().substring(intent.getData().getScheme().length() + 1);
            updatePackageInfos();
            if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ApplicationInfo info  = packageManager.getApplicationInfo(p,PackageManager.GET_META_DATA);
                            String appName = (String) info.loadLabel(packageManager);
                            appNameMap.put(p,appName);
                            Logly.d(" install app : " + appName);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            if(Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())){
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(p,0);
                    if(packageInfo.applicationInfo.enabled){
                        String appName = (String) packageInfo.applicationInfo.loadLabel(packageManager);
                        appNameMap.put(p,appName);

                    }else {
                        appNameMap.remove(p);
                    }
                    Logly.d("");
                } catch (PackageManager.NameNotFoundException e) {
                    Logly.d("  " + p + "  uninstall");
                    e.printStackTrace();
                }
            }
            if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){

                List<String> keys = new ArrayList<>();

                String appName = appNameMap.get(p);
                appNameMap.remove(p);
                Logly.d(" remove app : " + appName);
            }
        }
    }

    private void updatePackageInfos(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfoList = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
    }
}
