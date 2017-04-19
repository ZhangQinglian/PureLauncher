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

package com.zql.android.purelauncher.presentation.db.dao;

import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.presentation.db.entity.ActionEntity;

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public interface ActionDao {

    /**
     * 如存在此action则更新数据库
     * 如不存在则插入
     * @param action
     */
    void updateAction(Action action);

    /**
     * 获得action的点击次数
     * @param action
     * @return
     */
    int getActionCount(Action action);

    List<ActionEntity> getActinEntiry(Action action);
}
