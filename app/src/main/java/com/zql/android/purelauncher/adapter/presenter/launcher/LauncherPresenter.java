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

package com.zql.android.purelauncher.adapter.presenter.launcher;

import android.widget.ImageView;

import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.processor.AppProcessor;
import com.zql.android.purelauncher.adapter.model.processor.Bridges;
import com.zql.android.purelauncher.adapter.model.processor.Processor;
import com.zql.android.purelauncher.adapter.presenter.launcher.Contract.Presenter;
import com.zql.android.purelauncher.presentation.framework.BridgesImp;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherPresenter implements Presenter {

    private Contract.View mView;

    private List<Processor> processors = new ArrayList<>();

    public LauncherPresenter(Contract.View view, Bridges bridges){
        mView = view;
        mView.setPresenter(this);
        Processor processor = new AppProcessor(bridges.getAppProcessorBridge());
        processor.setCallback(this);
        processors.add(processor);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onKeyChanged(String key) {
        if(key == null ||(key.trim().length() == 0)){
            mView.updateAction(null);
        }
        for(int i = 0;i<processors.size();i++){
            Processor processor = processors.get(i);
            processor.updateInput(key);
        }
    }

    @Override
    public void loadApplicationLog(String packageName, ImageView imageView) {
        BridgesImp.own().getAppProcessorBridge().loadAppLogo(packageName,imageView);
    }

    @Override
    public void action(Action action) {
        if(action instanceof AppAction){
            AppAction appAction = (AppAction) action;
            BridgesImp.own().getAppProcessorBridge().openApp(appAction.packageName);
        }
    }

    @Override
    public void hideSearchView() {
        mView.hideSearchView();
    }

    @Override
    public synchronized void onWorkDone(String key, List<Action> actions) {
        Logly.d(" the query key : " + key);
        for(int i = 0;i<actions.size();i++){
            Logly.d(actions.get(i).toString());
        }
        mView.updateAction(actions);
    }
}
