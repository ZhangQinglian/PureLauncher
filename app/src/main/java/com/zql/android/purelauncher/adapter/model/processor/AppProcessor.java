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

package com.zql.android.purelauncher.adapter.model.processor;


import com.zql.android.purelauncher.adapter.model.Action.AppAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class AppProcessor extends Processor {

    private Set<Worker> workers = new HashSet<>();

    private AppProcessor.Bridge mBridge;

    public AppProcessor(AppProcessor.Bridge bridge){
        mBridge = bridge;
    }

    public interface Bridge extends IBridge{
        List<AppAction> getAppActions(String key);
        void loadAppLogo(String packageName, Object imageView);
        void openApp(String packgeName);
    }
    @Override
    public void onKeyChanged(String newKey) {
        Worker worker = new Worker(newKey);
        workers.add(worker);
        worker.start();
    }

    private class Worker extends Thread {
        private String key;

        public Worker(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            if(key != null && key.trim().length() == 0){
                return;
            }
            if(mBridge != null){
                List<AppAction> appActions = mBridge.getAppActions(key);
                if(appActions != null){
                    workDone(key, appActions);
                }
            }

            workers.remove(this);
        }
    }
}
