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

package com.zql.android.purelauncher.presentation.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
@Entity
public class ActionEntity {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private int type;

    @NotNull
    private int count;

    @NotNull
    private String finger_print;

    /**
     * {@link com.zql.android.purelauncher.adapter.model.Action.AppAction#packageName}
     * {@link com.zql.android.purelauncher.adapter.model.Action.ContactAction#displayName}
     */
    private String str1;

    /**
     * {@link com.zql.android.purelauncher.adapter.model.Action.AppAction#appName}
     * {@link com.zql.android.purelauncher.adapter.model.Action.ContactAction#lookupKey}
     */
    private String str2;

    /**
     * {@link com.zql.android.purelauncher.adapter.model.Action.ContactAction#contactId}
     */
    private String str3;

    private String str4;

    private String str5;

    private String str6;

    @Generated(hash = 287115110)
    public ActionEntity(Long id, int type, int count, @NotNull String finger_print,
            String str1, String str2, String str3, String str4, String str5, String str6) {
        this.id = id;
        this.type = type;
        this.count = count;
        this.finger_print = finger_print;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
        this.str5 = str5;
        this.str6 = str6;
    }

    @Generated(hash = 2064731073)
    public ActionEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFinger_print() {
        return this.finger_print;
    }

    public void setFinger_print(String finger_print) {
        this.finger_print = finger_print;
    }

    public String getStr1() {
        return this.str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return this.str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return this.str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return this.str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String getStr5() {
        return this.str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5;
    }

    public String getStr6() {
        return this.str6;
    }

    public void setStr6(String str6) {
        this.str6 = str6;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
