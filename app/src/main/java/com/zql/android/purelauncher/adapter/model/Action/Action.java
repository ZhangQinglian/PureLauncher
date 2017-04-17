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
 * @author qinglian.zhang, created on 2017/4/12.
 */
public abstract class Action {

    public static final int ACTION_INVAL = 0x000;

    public static final int ACTION_APP = 0x001;

    public static final int ACTION_CONTACT = 0x002;

    public abstract String getContent();

}
