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
import com.zql.android.purelauncher.adapter.model.processor.Processor;
import com.zql.android.purelauncher.adapter.mvp.IContract;
import com.zql.android.purelauncher.adapter.mvp.IPresenter;
import com.zql.android.purelauncher.adapter.mvp.IView;

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public interface Contract extends IContract{

    interface Presenter extends IPresenter,Processor.Callback{
        void onKeyChanged(String key);
        void loadApplicationLog(String packageName, ImageView imageView);
        void action(Action action);
        void hideSearchView();
    }

    interface View extends IView<Presenter>{
        void updateAction(List<Action> actions);
        void hideSearchView();
    }
}
