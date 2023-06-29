package com.example.brain_uof;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREFERENCE_NAME = "UserPreferences";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_ID, null);
    }

    public static void saveUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_NAME, null);
    }

    public static void clearUserPreferences(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
