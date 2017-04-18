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

package com.zql.android.purelauncher.adapter.model.processor;

import com.zql.android.purelauncher.adapter.model.Action.Action;
import com.zql.android.purelauncher.adapter.model.Action.ExprAction;
import com.zql.android.purelauncher.adapter.model.utils.expr.Expr;
import com.zql.android.purelauncher.adapter.model.utils.expr.Parser;
import com.zql.android.purelauncher.adapter.model.utils.expr.SyntaxException;
import com.zql.android.purelauncher.adapter.model.utils.expr.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qinglian.zhang, created on 2017/4/17.
 */
public class ExprProcessor extends Processor {

    private Set<Worker> workers = new HashSet<>();

    @Override
    public void onKeyChanged(String newKey) {
        Worker worker = new Worker(newKey);
        workers.add(worker);
        worker.start();
    }

    private class Worker extends Thread{
        private String key ;

        public Worker(String key){
            this.key = key;
        }

        @Override
        public void run() {
            List<ExprAction> actions = new ArrayList<>();
            if(key != null && key.trim().length() == 0){
                workDone(key,actions, Action.ACTION_EXPR);
                return;
            }
            try {
                Variable.clean();
                Expr expr = Parser.parse(key);
                ExprAction action = new ExprAction();
                if(Variable.isEmpty(key)){
                    workDone(key,actions, Action.ACTION_EXPR);
                    return;
                }
                action.expr = key;
                action.value = expr.value();
                actions.add(action);
            } catch (SyntaxException e) {
                e.printStackTrace();
                actions.clear();
            }finally {
                workDone(key,actions, Action.ACTION_EXPR);
                workers.remove(this);
            }
        }
    }
}
