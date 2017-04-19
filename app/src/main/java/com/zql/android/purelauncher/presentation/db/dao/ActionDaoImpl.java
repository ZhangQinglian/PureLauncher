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

package com.zql.android.purelauncher.presentation.db.dao;

import android.util.ArrayMap;

import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zql.android.purelauncher.presentation.db.entity.ActionEntity;
import com.zql.android.purelauncher.presentation.db.entity.ActionEntityDao;
import com.zql.android.purelauncher.presentation.db.entity.DaoSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public class ActionDaoImpl implements ActionDao {


    private static ActionDao sInstance;

    private DaoSession daoSession;

    private Map<String,ActionEntity> actionEntityCache = new HashMap<>();

    private ActionDaoImpl(){
        daoSession = LauncherApplication.own().getDaoSession();
        updateActionCache();
    }

    public static ActionDao own(){
        if(sInstance == null){
            sInstance = new ActionDaoImpl();
        }
        return sInstance;
    }
    @Override
    public void updateAction(Action action) {
        List<ActionEntity> actionEntities = getActinEntiry(action);
        ActionEntity entity = null;
        if(actionEntities.size()>0){
            entity = actionEntities.get(0);
        }

        switch (action.type){
            case Action.ACTION_APP:
                AppAction appAction = (AppAction)action;
                if(entity == null){
                    entity = new ActionEntity();
                    entity.setCount(AppAction.DEFAULT_COUNT);
                    entity.setType(appAction.type);
                    entity.setFinger_print(appAction.getFingerPrint());
                    entity.setStr1(appAction.packageName);
                    entity.setStr2(appAction.appName);
                }else {
                    entity.setCount(entity.getCount() + 1);
                }
                break;
            case Action.ACTION_CONTACT:
                ContactAction contactAction = (ContactAction) action;
                if(entity == null){
                    entity = new ActionEntity();
                    entity.setCount(ContactAction.DEFAULT_COUNT);
                    entity.setType(contactAction.type);
                    entity.setFinger_print(contactAction.getFingerPrint());
                    entity.setStr1(contactAction.displayName);
                    entity.setStr2(contactAction.lookupKey);
                    entity.setStr3(contactAction.contactId);
                }else {
                    entity.setCount(entity.getCount() + 1);
                }
                break;

        }
        if(entity != null){
            daoSession.getActionEntityDao().save(entity);
            actionEntityCache.put(entity.getFinger_print(),entity);
        }
    }

    @Override
    public int getActionCount(Action action) {
        ActionEntity entity = actionEntityCache.get(action.getFingerPrint());
        if(entity != null){
            return entity.getCount();
        }else {
            return 0;
        }
    }

    @Override
    public List<ActionEntity>  getActinEntiry(Action action) {
        String fingerprint = action.getFingerPrint();
        List<ActionEntity> actionDaoList = daoSession.getActionEntityDao().queryRaw("WHERE " + ActionEntityDao.Properties.Finger_print.columnName+"=?",fingerprint);
        return actionDaoList;
    }

    private void updateActionCache(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<ActionEntity> actionEntities = daoSession.getActionEntityDao().loadAll();
                actionEntityCache.clear();
                for(int i = 0;i<actionEntities.size();i++){
                    actionEntityCache.put(actionEntities.get(i).getFinger_print(),actionEntities.get(i));
                }
            }
        });

    }
}
