package com.made_easy;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig
{
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preference),status);
        editor.commit();
    }
    public boolean readLoginStatus()
    {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference), false);
        return status;
    }

    public void writeLoginUserId(String userId)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.login_session_user_id),userId);
        editor.commit();
    }
    public String readUserId()
    {
        String userId = "";
        userId = sharedPreferences.getString("com.made_easy_login_user_id",userId);
        return userId;
    }

    public void writeLoginUsername(String username)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.login_session_user_username),username);
        editor.commit();
    }
    public String readUsername()
    {
        String username = "";
        username = sharedPreferences.getString("com.made_easy_login_user_username",username);
        return username;
    }

    public void writeLoginRole(String role)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.login_session_user_role),role);
        editor.commit();
    }
    public String readRole()
    {
        String role = "";
        role = sharedPreferences.getString("com.made_easy_login_user_role",role);
        return role;
    }
}
