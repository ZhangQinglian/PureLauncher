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

package com.zql.android.purelauncher.presentation.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zqlite.android.logly.Logly;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherContainer extends RelativeLayout implements LauncherSearchView.Callback{


    private LauncherSearchView mSearchView;
    private LauncherSearchResultContainer mResultView;

    private LauncherContainer.Callback mCallback;
    public interface Callback{
        void onInputChanged(String key);
    }

    public LauncherContainer(Context context) {
        this(context,null);
    }

    public LauncherContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LauncherContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.switchVisible();
            }
        });
    }

    public void setCallback(LauncherContainer.Callback callback){
        mCallback = callback;
    }

    public void showResultView(){
        mResultView.show();
    }

    public void hideResultView(){
        mResultView.hide();
    }

    public void hideSearchVIew(){
        mSearchView.hideSearchView();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSearchView = (LauncherSearchView) findViewById(R.id.launcher_search_view);
        mSearchView.setCallback(this);
        mResultView = (LauncherSearchResultContainer) findViewById(R.id.launcher_search_result);
    }

    @Override
    public void beVisible() {
        Logly.d("beVisible");

    }

    @Override
    public void beInVisible() {
        Logly.d("beInvisible");

    }

    @Override
    public void inputChanged(String key) {
        if(mCallback != null){
            mCallback.onInputChanged(key);
        }
    }

    public void hideKeyboard(){
        mSearchView.hideKeyboard();
    }
}
