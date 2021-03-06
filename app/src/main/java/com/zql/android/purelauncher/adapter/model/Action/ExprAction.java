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
 * @author qinglian.zhang, created on 2017/4/17.
 */
public class ExprAction extends Action {

    public String expr;

    public double value;

    public ExprAction(){
        type = ACTION_EXPR;
    }
    @Override
    public String getContent() {
        return expr + " = " + value;
    }

    @Override
    public String getFingerPrint() {
        return ExprAction.class.getSimpleName() + expr + value;
    }
}
