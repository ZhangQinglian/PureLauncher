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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zql.android.purelauncher.R;

/**
 * @author qinglian.zhang, created on 2017/4/18.
 */
public class LauncherWidget extends FrameLayout {

    private FrameLayout mWidgetContainer;
    public LauncherWidget(Context context) {
        super(context);
        init();
    }

    public LauncherWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LauncherWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.custom_launcher_widget,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWidgetContainer = (FrameLayout) findViewById(R.id.launcher_widget_container);
    }

    public void addWidget(View view, AppWidgetHostView.LayoutParams params){
        mWidgetContainer.removeAllViews();
        view.setLayoutParams(params);
        mWidgetContainer.addView(view);
    }

    public void show(){
        if(getVisibility() == VISIBLE) return;
        setAlpha(0);
        setVisibility(VISIBLE);
        animate().alpha(1).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).start();
    }

    public void hide(){
        animate().alpha(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }
        }).start();
    }
}
