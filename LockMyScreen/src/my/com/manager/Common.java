package my.com.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Common {
	public static void setFlagLongClick (Context context, Boolean flag) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		android.content.SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean("flag_longclick",flag);
		editor.commit();
	}
	
	public static Boolean getLongClickFlag(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    Boolean aToken = app_preferences.getBoolean("flag_longclick", true);
	    return aToken;
	}
	public static void setColor (Context context, int flag) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		android.content.SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("color",flag);
		editor.commit();
	}
	public static int getColor(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    int aToken = app_preferences.getInt("color", 0);
	    return aToken;
	}
}
