package Functionality_Class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import Controllers.Config;


//Shared preference saved class for saving data in shared preferences
public class SharedPreferenceFileAll {

    private SharedPreferences Stulity;
    private static String file = "Stulity";

    //SharedPrefSave constructor
    public SharedPreferenceFileAll(Context context) {
        Stulity = ((Activity) context).getSharedPreferences(file, 0);
    }

    //Shared Preferences saved boolean value
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editors = Stulity.edit();
        editors.putBoolean(key, value);
        editors.commit();
    }

    //Shared Preferences saved string value
    public void setString(String key, String value) {
        SharedPreferences.Editor editors = Stulity.edit();
        editors.putString(key, value);
        editors.commit();
    }

    //Shared Preferences saved int value
    public void setInt(String key, int value) {
        SharedPreferences.Editor editors = Stulity.edit();
        editors.putInt(key, value);
        editors.commit();
    }

    //Shared Preferences saved long value
    public void setLong(String key, long value) {
        SharedPreferences.Editor editors = Stulity.edit();
        editors.putLong(key, value);
        editors.commit();
    }

    //Shared Preferences get long value
    public long getLong(String key) {
        long res = Stulity.getLong(key, 0);
        return res;
    }

    //Shared Preferences get boolean value
    public boolean getBoolean(String key) {
        boolean result = Stulity.getBoolean(key, false);
        return result;
    }

    //Shared Preferences get string value
    public String getString(String key) {
        String res = Stulity.getString(key, Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING);
        return res;
    }

    public int getInt(String key) {
        int res = Stulity.getInt(key, Config.SHARED_PREFRENCE_NO_DATA_KEY_INT);
        return res;
    }

    public void clearSharedPreference() {
        SharedPreferences.Editor editors = Stulity.edit();
        editors.clear();
        editors.commit();
    }

    //Remove values from shared preferences
    public void removeSharedPreferences(String key) {
        Stulity.edit().remove(key).commit();
    }
}