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

package com.zql.android.purelauncher.adapter.model.Action;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public class CommandAction extends Action {

    public static final String SET_DEFAULT_LAUNCHER = "#fuck";
    public static final String ADD_WIDGET = "#widget";

    public String command = "";

    public CommandAction(String name){
        command = name;
    }
    @Override
    public String getContent() {
        return command;
    }

    @Override
    public String getFingerPrint() {
        return CommandAction.class.getSimpleName() + command;
    }

    public static CommandAction getCommandAction(String key){
        if(SET_DEFAULT_LAUNCHER.equals(key)){
            return new CommandAction(SET_DEFAULT_LAUNCHER);
        }
        if(ADD_WIDGET.equals(key)){
            return new CommandAction(ADD_WIDGET);
        }
        return null;
    }
}
