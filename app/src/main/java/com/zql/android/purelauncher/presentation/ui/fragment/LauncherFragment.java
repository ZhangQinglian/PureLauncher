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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.AppAction;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;
import com.zql.android.purelauncher.adapter.presenter.launcher.Contract;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zql.android.purelauncher.presentation.ui.customview.LauncherContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/12.
 */
public class LauncherFragment extends Fragment implements Contract.View ,LauncherContainer.Callback{

    private Contract.Presenter mPresenter;

    private LauncherContainer mLauncherContainer;

    private RecyclerView mSearchResultList;

    private SearchResultAdapter mResultAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mPresenter.onKeyChanged(key);
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

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultHolder>{

        private List<Action> actionList = new ArrayList<>();

        private List<Action> appList = new ArrayList<>();

        private List<Action> contactList = new ArrayList<>();

        public synchronized void updateAction(List<Action> actions,int actionType){

            if(actionType == Action.ACTION_APP){
                appList.clear();
                appList.addAll(actions);
            }
            if(actionType == Action.ACTION_CONTACT){
                contactList.clear();
                contactList.addAll(actions);
            }
            if(actionType == Action.ACTION_INVAL){
                appList.clear();
                contactList.clear();
            }

            actionList.clear();
            actionList.addAll(contactList);
            actionList.addAll(appList);

            if(actionList.size() == 0){
                mLauncherContainer.hideResultView();
            }else {
                mLauncherContainer.showResultView();
            }
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
            holder.content.setText(action.getContent());
            if(action instanceof AppAction){
                AppAction appAction = (AppAction)action;
                mPresenter.loadApplicationLogo(appAction.packageName,holder.thumbnail);
                holder.icon.setImageResource(R.drawable.ic_search_app);
            }
            if(action instanceof ContactAction){
                ContactAction contactAction = (ContactAction)action;
                holder.icon.setImageResource(R.drawable.ic_search_contact);
                mPresenter.loadContactPhoto(contactAction.contactId,holder.thumbnail);
            }


            //animation
            holder.itemView.setAlpha(0);
            if(holder.animator != null){
                holder.animator.cancel();
            }
            ValueAnimator animator = ValueAnimator.ofFloat(0,1);
            animator.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    holder.itemView.setAlpha(v);
                }
            });
            animator.start();
            holder.animator = animator;
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
        ValueAnimator animator;
        public SearchResultHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.search_result_item_icon);
            thumbnail = (ImageView) itemView.findViewById(R.id.search_result_item_thumbnail);
            content = (TextView) itemView.findViewById(R.id.search_result_item_content);
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
