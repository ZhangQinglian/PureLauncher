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

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public abstract class Processor {

    private String mKey = "";

    protected Processor.Callback mCallback;
    public interface Callback{
        void onWorkDone(String key, List<Action> actions,int actionType);
    }
    public synchronized void updateInput(String input){
        if(!mKey.equals(input)){
            mKey = input;
            onKeyChanged(mKey);
        }
    }


    public void setCallback(Processor.Callback callback){
        mCallback = callback;
    }
    public void workDone(String key,List actions,int actionType){
        if(!key.equals(mKey)){
            return ;
        }
        if(mCallback != null){
            mCallback.onWorkDone(key,actions,actionType);
        }
    }

    public abstract void onKeyChanged(String newKey);

}
