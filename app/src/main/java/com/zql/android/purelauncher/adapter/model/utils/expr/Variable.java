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

package com.zql.android.purelauncher.adapter.model.utils.expr;

import com.zqlite.android.logly.Logly;

import java.util.Hashtable;

/**
 * A variable is a simple expression with a name (like "x") and a
 * settable value.
 */
public class Variable extends Expr {
    private static Hashtable variables = new Hashtable();
    
    /**
     * Return a unique variable named `name'.  There can be only one
     * variable with the same name returned by this method; that is,
     * make(s1) == make(s2) if and only if s1.equals(s2).
     * @param name the variable's name
     * @return the variable; create it initialized to 0 if it doesn't
     *         yet exist */
    static public synchronized Variable make(String name) {
	Variable result = (Variable) variables.get(name);
	if (result == null)
	    variables.put(name, result = new Variable(name));
	return result;
    }

    private String name;
    private double val;

    /**
     * Create a new variable, with initial value 0.
     * @param name the variable's name
     */
    public Variable(String name) { 
	this.name = name; val = 0; 
    }

    /** Return the name. */
    public String toString() { return name; }

    /** Get the value.
     * @return the current value */
    public double value() { 
	return val; 
    }
    /** Set the value.
     * @param value the new value */
    public void setValue(double value) { 
	val = value; 
    }

    public static boolean isEmpty(){
        Logly.d("  =+++++++++ " + variables.size());
        if(variables.size() <=1){
            return true;
        }else {
            return false;
        }
    }
}
