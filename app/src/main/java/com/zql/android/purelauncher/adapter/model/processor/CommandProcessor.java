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
import com.zql.android.purelauncher.adapter.model.Action.CommandAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public class CommandProcessor extends Processor {
    @Override
    public void onKeyChanged(String newKey) {
        CommandAction commandAction = CommandAction.getCommandAction(newKey);
        List<Action> actions = new ArrayList<>();
        if(commandAction != null){
            actions.add(commandAction);
        }
        workDone(newKey,actions,Action.ACTION_COMMAND);
    }
}
