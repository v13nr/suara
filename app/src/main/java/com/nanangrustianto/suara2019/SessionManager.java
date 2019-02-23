package com.nanangrustianto.suara2019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "suara2019";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NAME = "name";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLogin(String username, String name) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public boolean isLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
