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

package com.zql.android.purelauncher.presentation.ui.fragment;

import android.animation.ValueAnimator;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.Action.CommandAction;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;
import com.zql.android.purelauncher.adapter.model.Action.ExprAction;
import com.zql.android.purelauncher.adapter.presenter.launcher.Contract;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zql.android.purelauncher.presentation.framework.SharePreferenceMgr;
import com.zql.android.purelauncher.presentation.ui.activity.PureActivity;
import com.zql.android.purelauncher.presentation.ui.customview.LauncherContainer;
import com.zql.android.purelauncher.presentation.ui.customview.LauncherWidget;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherFragment extends Fragment implements Contract.View ,LauncherContainer.Callback{

    private static final int HOST_ID = 1024 ;
    private static final int MY_REQUEST_APPWIDGET = 1;
    private static final int MY_CREATE_APPWIDGET = 2;

    private Contract.Presenter mPresenter;

    private LauncherContainer mLauncherContainer;

    private RecyclerView mSearchResultList;

    private LauncherWidget mLauncherWidget;

    private SearchResultAdapter mResultAdapter;

    private AppWidgetHost mAppWidgetHost = null ;
    private AppWidgetManager appWidgetManager = null;
    private int mOldWidgetId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAppWidgetHost = new AppWidgetHost(LauncherApplication.own().getApplicationContext(), HOST_ID) ;
        mAppWidgetHost.startListening() ;
        appWidgetManager = AppWidgetManager.getInstance(LauncherApplication.own().getApplicationContext()) ;

        return inflater.inflate(R.layout.fragment_launcher,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLauncherContainer = (LauncherContainer) view.findViewById(R.id.launcher_container);
        mLauncherContainer.setCallback(this);
        mSearchResultList = (RecyclerView) view.findViewById(R.id.launcher_search_result_list);
        mResultAdapter = new SearchResultAdapter();
        mSearchResultList.setAdapter(mResultAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mSearchResultList.setLayoutManager(linearLayoutManager);
        mSearchResultList.setHasFixedSize(true);
        mSearchResultList.setItemAnimator(new DefaultItemAnimator());
        mLauncherWidget = (LauncherWidget) view.findViewById(R.id.launcher_widget);
        initWidget();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAppWidgetHost.startListening() ;

    }

    @Override
    public void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static LauncherFragment getInstance(Bundle bundle){

        LauncherFragment launcherFragment = new LauncherFragment();
        launcherFragment.setArguments(bundle);
        return launcherFragment;

    }

    @Override
    public void onInputChanged(String key) {
        if(mPresenter != null){
            mPresenter.onKeyChanged(key);
        }
    }

    @Override
    public void updateAction(final List<Action> actions, final int actionType) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultAdapter.updateAction(actions,actionType);
            }
        });
    }

    @Override
    public void hideSearchView() {
        mLauncherContainer.hideSearchVIew();
    }

    @Override
    public void addWidget() {
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK) ;

        //向系统申请一个新的appWidgetId ，该appWidgetId与我们发送Action为ACTION_APPWIDGET_PICK
        //  后所选择的AppWidget绑定 。 因此，我们可以通过这个appWidgetId获取该AppWidget的信息了

        //为当前所在进程申请一个新的appWidgetId
        int newAppWidgetId = mAppWidgetHost.allocateAppWidgetId() ;
        Logly.d("newAppWidgetId : " + newAppWidgetId);
        //作为Intent附加值 ， 该appWidgetId将会与选定的AppWidget绑定
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newAppWidgetId) ;

        //选择某项AppWidget后，立即返回，即回调onActivityResult()方法
        startActivityForResult(pickIntent , MY_REQUEST_APPWIDGET) ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //直接返回，没有选择任何一项 ，例如按Back键
        if(resultCode == RESULT_CANCELED)
            return ;

        switch(requestCode){
            case MY_REQUEST_APPWIDGET :
                int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;


                //得到的为有效的id
                if(appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
                    //查询指定appWidgetId的 AppWidgetProviderInfo对象 ， 即在xml文件配置的<appwidget-provider />节点信息
                    AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;

                    //如果配置了configure属性 ， 即android:configure = "" ，需要再次启动该configure指定的类文件,通常为一个Activity
                    if(appWidgetProviderInfo.configure != null){


                        //配置此Action
                        Intent intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE) ;
                        intent.setComponent(appWidgetProviderInfo.configure) ;
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


                        startActivityForResult(intent , MY_CREATE_APPWIDGET) ;
                    }
                    else  //直接创建一个AppWidget
                        onActivityResult(MY_CREATE_APPWIDGET , RESULT_OK , data) ;  //参数不同，简单回调而已
                }
                break ;
            case  MY_CREATE_APPWIDGET:
                completeAddAppWidget(data) ;
                break ;
        }
    }


    private void initWidget(){
        int id = SharePreferenceMgr.own().getWidgetId();
        if(id != -1){
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID ,id);
            completeAddAppWidget(intent);
        }
    }
    private void completeAddAppWidget(Intent data){
        Bundle extra = data.getExtras() ;
        int appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID , -1) ;
        Logly.d("appWidgetId = " + appWidgetId);
        //等同于上面的获取方式
        //int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;


        if(appWidgetId == -1){
            Toast.makeText(getActivity(), "添加窗口小部件有误", Toast.LENGTH_SHORT) ;
            return ;
        }

        AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;

        AppWidgetHostView hostView = mAppWidgetHost.createView(LauncherApplication.own().getApplicationContext(), appWidgetId, appWidgetProviderInfo);

        //设置长宽  appWidgetProviderInfo 对象的 minWidth 和  minHeight 属性
        //AppWidgetHostView.LayoutParams framelayoutP = new AppWidgetHostView.LayoutParams(AppWidgetHostView.LayoutParams.MATCH_PARENT,AppWidgetHostView.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams framelayoutP = new FrameLayout.LayoutParams(AppWidgetHostView.LayoutParams.MATCH_PARENT,appWidgetProviderInfo.minHeight
                + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,LauncherApplication.own().getResources().getDisplayMetrics()));

        framelayoutP.gravity = Gravity.TOP;
        if(mOldWidgetId != -1){
            mAppWidgetHost.deleteAppWidgetId(mOldWidgetId);
        }
        mOldWidgetId = appWidgetId;
        SharePreferenceMgr.own().saveWidgetId(mOldWidgetId);
        //添加至LinearLayout父视图中
        mLauncherWidget.addWidget(hostView,framelayoutP) ;
        hostView.requestLayout();
    }


    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultHolder>{

        private List<Action> actionList = new ArrayList<>();

        private List<Action> appList = new ArrayList<>();

        private List<Action> contactList = new ArrayList<>();

        private List<Action> exprList = new ArrayList<>();

        private List<Action> commandList = new ArrayList<>();
        public synchronized void updateAction(List<Action> actions,int actionType){

            if(actionType == Action.ACTION_APP){
                appList.clear();
                appList.addAll(actions);
            }
            if(actionType == Action.ACTION_CONTACT){
                contactList.clear();
                contactList.addAll(actions);
            }
            if(actionType == Action.ACTION_EXPR){
                exprList.clear();
                exprList.addAll(actions);
            }
            if(actionType == Action.ACTION_COMMAND){
                commandList.clear();
                commandList.addAll(actions);
            }

            if(actionType == Action.ACTION_INVAL){
                appList.clear();
                contactList.clear();
            }

            actionList.clear();
            actionList.addAll(contactList);
            actionList.addAll(appList);
            actionList.addAll(exprList);
            actionList.addAll(commandList);
            if(actionList.size() == 0){
                mLauncherContainer.hideResultView();
            }else {
                mLauncherContainer.showResultView();
            }

            mPresenter.sortByCount(actionList);
            notifyDataSetChanged();
        }

        @Override
        public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(LauncherApplication.own()).inflate(R.layout.listitem_search_result,parent,false);
            SearchResultHolder holder = new SearchResultHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SearchResultHolder holder, int position) {
            Action action = actionList.get(position);
            initHolder(holder);
            holder.content.setText(action.getContent());

            if(action instanceof AppAction){
                AppAction appAction = (AppAction)action;
                mPresenter.loadApplicationLogo(appAction.packageName,holder.thumbnail);
                holder.icon.setImageResource(R.drawable.ic_search_app);
                holder.action1.setVisibility(View.GONE);
                holder.action2.setVisibility(View.GONE);
            }
            if(action instanceof ContactAction){
                final ContactAction contactAction = (ContactAction)action;
                holder.icon.setImageResource(R.drawable.ic_search_contact);
                mPresenter.loadContactPhoto(contactAction.contactId,holder.thumbnail);
                holder.action1.setVisibility(View.GONE);
                holder.action2.setVisibility(View.GONE);
            }
            if(action instanceof ExprAction){
                holder.icon.setImageResource(R.drawable.ic_search_expr);
                holder.action1.setVisibility(View.GONE);
                holder.action2.setVisibility(View.GONE);
                holder.thumbnail.setVisibility(View.GONE);
            }
            if(action instanceof CommandAction){
                CommandAction commandAction = (CommandAction) action;

                if(commandAction.command.equals(CommandAction.ADD_WIDGET)){
                    holder.content.setText(R.string.command_widget_name);
                }
                if(commandAction.command.equals(CommandAction.SET_DEFAULT_LAUNCHER)){
                    holder.content.setText(R.string.command_fuck_name);
                }
                holder.icon.setImageResource(R.drawable.ic_search_command);
                holder.action1.setVisibility(View.GONE);
                holder.action2.setVisibility(View.GONE);
                holder.thumbnail.setVisibility(View.GONE);
            }

            //animation
            holder.itemView.setAlpha(0);
            if(holder.animator != null){
                holder.animator.cancel();
            }
            ValueAnimator animator = ValueAnimator.ofFloat(0,1);
            animator.setDuration(300).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    holder.itemView.setAlpha(v);
                }
            });
            animator.start();
            holder.animator = animator;
        }

        private void initHolder(SearchResultHolder holder){
            holder.thumbnail.setImageDrawable(null);
            holder.thumbnail.setTag(R.id.search_asynctask,null);
            holder.action1.setVisibility(View.VISIBLE);
            holder.action2.setVisibility(View.VISIBLE);
            holder.thumbnail.setVisibility(View.VISIBLE);
            holder.action1.setOnClickListener(null);
            holder.action2.setOnClickListener(null);
            holder.action1.setClickable(false);
            holder.action2.setClickable(false);
        }
        @Override
        public int getItemCount() {
            return actionList.size();
        }

        public Action getAction(int position){
            return actionList.get(position);
        }
    }

    private class SearchResultHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        ImageView thumbnail;
        TextView content;
        ImageView action1;
        ImageView action2;
        ValueAnimator animator;
        public SearchResultHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.search_result_item_icon);
            thumbnail = (ImageView) itemView.findViewById(R.id.search_result_item_thumbnail);
            content = (TextView) itemView.findViewById(R.id.search_result_item_content);
            action1 = (ImageView) itemView.findViewById(R.id.search_result_item_action1);
            action2 = (ImageView) itemView.findViewById(R.id.search_result_item_action2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLauncherContainer.hideKeyboard();
                    int position = getAdapterPosition();
                    Action action = mResultAdapter.getAction(position);
                    mPresenter.action(action);
                }
            });
        }
    }


}
