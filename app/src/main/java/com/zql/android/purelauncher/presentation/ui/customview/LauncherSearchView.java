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
import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.presentation.LauncherApplication;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherSearchView extends CardView {

    private EditText mEditText;

    private Callback mCallback;

    public interface Callback{
        /**
         * 当搜索框完全可见后的回调
         */
        void beVisible();

        /**
         * 当搜索框完全消失后的回调
         */
        void beInVisible();

        /**
         * 用户输入后的回调
         * @param key
         */
        void inputChanged(String key);
    }

    public LauncherSearchView(Context context) {
        this(context,null);
    }

    public LauncherSearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LauncherSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        LayoutInflater.from(context).inflate(R.layout.custom_launcher_search,this);
        mEditText = (EditText) findViewById(R.id.search_input_edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mCallback != null){
                    mCallback.inputChanged(s.toString());
                }
            }
        });
    }

    private void show(){
        if(getVisibility() == VISIBLE) return;
        setAlpha(0);
        setVisibility(VISIBLE);
        animate().alpha(1).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                showKeyboard();
                mEditText.setClickable(true);
                if(mCallback != null) mCallback.beVisible();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mEditText.requestFocus();
            }
        }).start();
    }

    private void hide(){
        animate().alpha(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mEditText.setText("");
                mEditText.clearFocus();
                mEditText.setClickable(false);
                if(mCallback != null) mCallback.beInVisible();
                hideKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(INVISIBLE);
            }
        }).start();
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void switchVisible(){
        if(getVisibility() == INVISIBLE){
            show();
            return;
        }
        if(getVisibility() == VISIBLE){
            hide();
        }
    }

    public void hideSearchView(){
        if(getVisibility() == VISIBLE){
            hide();
        }
    }
    public void showKeyboard(){
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) LauncherApplication.own().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mEditText,0);
    }

    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) LauncherApplication.own().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
    }
}
