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

import com.zql.android.purelauncher.adapter.model.processor.AppProcessor;
import com.zql.android.purelauncher.adapter.model.processor.Bridges;
import com.zql.android.purelauncher.adapter.model.processor.ContactProcessor;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class BridgesImp implements Bridges {

    private AppBridge appBridge;

    private ContactBridge contactBridge;

    private static BridgesImp sInstance;


    public static BridgesImp own(){
        if(sInstance == null){
            sInstance = new BridgesImp();
        }
        return sInstance;
    }

    private BridgesImp() {
        appBridge = new AppBridge();
        contactBridge = new ContactBridge();
    }

    @Override
    public AppProcessor.Bridge getAppProcessorBridge() {
        if (appBridge != null) {
            return appBridge;
        } else {
            appBridge = new AppBridge();
            return appBridge;
        }
    }

    @Override
    public ContactProcessor.Bridge getContactProcessorBridge() {
        if(contactBridge != null){
            return contactBridge;
        }else {
            contactBridge = new ContactBridge();
            return contactBridge;
        }
    }
}
