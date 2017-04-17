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

import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qinglian.zhang, created on 2017/4/17.
 */
public class ContactProcessor extends Processor {

    private Set<Worker> workers = new HashSet<>();

    private ContactProcessor.Bridge mBridge;

    public interface Bridge extends IBridge{
        List<ContactAction> getContactActions(String key);
        void loadContactPhoto(String id,Object imageView);
        void openContact(String lookupKey,String contactId);
    }

    public ContactProcessor(ContactProcessor.Bridge bridge){
        mBridge = bridge;
    }
    @Override
    public void onKeyChanged(String newKey) {
        Worker worker = new Worker(newKey);
        workers.add(worker);
        worker.start();
    }

    private class Worker extends Thread{

        private String key;

        public Worker(String key){
            this.key = key;
        }

        @Override
        public void run() {
            if(key != null && key.trim().length() == 0){
                return;
            }
            if(mBridge != null){
                List<ContactAction> actions = mBridge.getContactActions(key);
                if(actions != null){
                    workDone(key,actions, Action.ACTION_CONTACT);
                }
            }
            workers.remove(this);
        }
    }
}
