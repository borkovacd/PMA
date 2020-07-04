package com.android.exconvictslocator;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * All session related functions
 * ( to make them available in all activities)
 */
public class SessionManagement {

    // Shared Preferences
    SharedPreferences pref;

    // Shared Preferences File name
    // (Note: Conventionally it has the same name as the package name of your app)
    private static final String PREF_NAME = "com.android.exconvictslocator";

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    // Email address
    // (Variable is public in order to access it from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";

    public static final String KEY_NOTIFICATION = "notification";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGGED_IN, true);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);

        // commit changes
        editor.apply(); //editor.commit();
        // apply() VS commit()
        //The apply() method saves the preferences asynchronously, off of the UI thread.
        //The shared preferences editor also has a commit() method to synchronously save the preferences.
        //The commit() method is discouraged as it can block other operations.
    }

    /**
     * Update Login Info
     */
    public void updateLoginSession(String email, String name) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        // return user
        return user;
    }

    /**
     * checkLogin() method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin() {
        // Check login status
        if(!this.isLoggedIn()){
            // if user is not logged in, redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Starting Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     * */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply(); //editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, ListOfExConvicts.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Starting Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Notification Service
     */
    public void updateNotificationService(boolean started) {
        editor.putBoolean(KEY_NOTIFICATION, started);
        editor.apply();
    }

    /**
     * Quick check for Notification Service
     * **/
    // Get Login State
    public boolean isNotificationServiceStarted() {
        return pref.getBoolean(KEY_NOTIFICATION, false);
    }

}