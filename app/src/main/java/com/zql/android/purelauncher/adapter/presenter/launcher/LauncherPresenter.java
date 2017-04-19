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

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.Action.CommandAction;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;
import com.zql.android.purelauncher.adapter.model.processor.AppProcessor;
import com.zql.android.purelauncher.adapter.model.processor.Bridges;
import com.zql.android.purelauncher.adapter.model.processor.CommandProcessor;
import com.zql.android.purelauncher.adapter.model.processor.ContactProcessor;
import com.zql.android.purelauncher.adapter.model.processor.ExprProcessor;
import com.zql.android.purelauncher.adapter.model.processor.Processor;
import com.zql.android.purelauncher.adapter.presenter.launcher.Contract.Presenter;
import com.zql.android.purelauncher.presentation.db.dao.ActionDaoImpl;
import com.zql.android.purelauncher.presentation.framework.BridgesImp;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherPresenter implements Presenter {

    private Contract.View mView;

    private List<Processor> processors = new ArrayList<>();

    public LauncherPresenter(Contract.View view, Bridges bridges) {
        mView = view;
        mView.setPresenter(this);
        //APP
        Processor appProcessor = new AppProcessor(bridges.getAppProcessorBridge());
        appProcessor.setCallback(this);
        processors.add(appProcessor);
        //Contact
        Processor contactProcessor = new ContactProcessor(bridges.getContactProcessorBridge());
        contactProcessor.setCallback(this);
        processors.add(contactProcessor);
        //Expr
        Processor exprProcessor = new ExprProcessor();
        exprProcessor.setCallback(this);
        processors.add(exprProcessor);
        //Command
        Processor commandProcessor = new CommandProcessor();
        commandProcessor.setCallback(this);
        processors.add(commandProcessor);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onKeyChanged(String key) {
        if (key == null || (key.trim().length() == 0)) {
            mView.updateAction(null, Action.ACTION_INVAL);
        }
        for (int i = 0; i < processors.size(); i++) {
            Processor processor = processors.get(i);
            processor.updateInput(key);
        }
    }

    @Override
    public void loadApplicationLogo(String packageName, ImageView imageView) {
        BridgesImp.own().getAppProcessorBridge().loadAppLogo(packageName, imageView);
    }

    @Override
    public void loadContactPhoto(String photoUri, ImageView imageView) {
        BridgesImp.own().getContactProcessorBridge().loadContactPhoto(photoUri, imageView);
    }

    @Override
    public void action(Action action) {
        if (action instanceof AppAction) {
            AppAction appAction = (AppAction) action;
            BridgesImp.own().getAppProcessorBridge().openApp(appAction.packageName);
        }
        if (action instanceof ContactAction) {
            ContactAction contactAction = (ContactAction) action;
            BridgesImp.own().getContactProcessorBridge().openContact(contactAction.lookupKey, contactAction.contactId);
        }
        if (action instanceof CommandAction) {
            CommandAction commandAction = (CommandAction) action;
            if (commandAction.command.equals(CommandAction.SET_DEFAULT_LAUNCHER)) {
                defaultLauncher();
            }
            if (commandAction.command.equals(CommandAction.ADD_WIDGET)) {
                addWidget();
            }
        }
        saveAction(action);
    }

    @Override
    public void hideSearchView() {
        mView.hideSearchView();
    }

    @Override
    public void saveAction(Action action) {
        ActionDaoImpl.own().updateAction(action);
    }

    @Override
    public synchronized void onWorkDone(String key, List<Action> actions, int actionType) {
//        Logly.d(" the query key : " + key);
//        for(int i = 0;i<actions.size();i++){
//            Logly.d(actions.get(i).toString());
//        }
        mView.updateAction(actions, actionType);
    }

    @Override
    public void sortByCount(List<Action> actions) {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            action.count = ActionDaoImpl.own().getActionCount(action);
        }

        Collections.sort(actions, new Comparator<Action>() {
            @Override
            public int compare(Action o1, Action o2) {
                return o2.count - o1.count;
            }
        });
    }

    private void defaultLauncher() {
        FancyAlertDialog.Builder builder = new FancyAlertDialog.Builder(((Fragment) mView).getActivity());
        builder.setTextSubTitle(R.string.command_fuck_title)
                .setSubtitleColor(R.color.colorPrimary)
                .setBody(R.string.command_fuck_dialog_body)
                .setPositiveButtonText(R.string.comm_ok)
                .setPositiveColor(R.color.colorPrimary)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButtonText(R.string.command_fuck_neg_text)
                .setNegativeColor(R.color.random_c_1)
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://www.baidu.com/s?wd=小米华为设置默认桌面");
                        intent.setData(content_url);
                        ((Fragment) mView).getActivity().startActivity(intent);
                    }
                })
                .build();
        builder.show();
    }

    private void addWidget() {
        mView.addWidget();
    }

}
