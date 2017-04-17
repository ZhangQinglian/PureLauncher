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

package com.zql.android.purelauncher.presentation.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.adapter.presenter.launcher.Contract;
import com.zql.android.purelauncher.adapter.presenter.launcher.LauncherPresenter;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zql.android.purelauncher.presentation.framework.BridgesImp;
import com.zql.android.purelauncher.presentation.ui.fragment.LauncherFragment;

public class PureActivity extends AppCompatActivity {

    private Contract.Presenter mLauncerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_pure);
        LauncherFragment fragment = LauncherFragment.getInstance(null);
        mLauncerPresenter = new LauncherPresenter(fragment,BridgesImp.own());
        getSupportFragmentManager().beginTransaction().add(R.id.launcher_container,fragment).commitNow();

    }

    @Override
    public void onBackPressed() {
        mLauncerPresenter.hideSearchView();
        return;
    }
}
