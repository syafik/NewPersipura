/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dg.android.facebook;

import java.util.LinkedList;

public class SessionEvents {

    private static LinkedList<AuthListener> mAuthListeners = 
        new LinkedList<AuthListener>();
    private static LinkedList<LogoutListener> mLogoutListeners = 
        new LinkedList<LogoutListener>();

    public static void addAuthListener(AuthListener listener) {
        mAuthListeners.add(listener);
    }

   
    public static void removeAuthListener(AuthListener listener) {
        mAuthListeners.remove(listener);
    }

    public static void addLogoutListener(LogoutListener listener) {
        mLogoutListeners.add(listener);
    }

    public static void removeLogoutListener(LogoutListener listener) {
        mLogoutListeners.remove(listener);
    }
    
    public static void onLoginSuccess() {
        for (AuthListener listener : mAuthListeners) {
            listener.onAuthSucceed();
        }
    }
    
    public static void onLoginError(String error) {
        for (AuthListener listener : mAuthListeners) {
            listener.onAuthFail(error);
        }
    }
    
    public static void onLogoutBegin() {
        for (LogoutListener l : mLogoutListeners) {
            l.onLogoutBegin();
        }
    }
    
    public static void onLogoutFinish() {
        for (LogoutListener l : mLogoutListeners) {
            l.onLogoutFinish();
        }   
    }
    
    public static interface AuthListener {
        public void onAuthSucceed();
        public void onAuthFail(String error);
    }
    
    public static interface LogoutListener {
        public void onLogoutBegin();
        public void onLogoutFinish();
    }
    
}
