package com.external.camera.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Util {
    public static final String START_CONN = "start_conn";
    public static final String END = "&";
    public static final String DEVIDE = "messagedevide";
    


    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String OK = "ok";
    public static final String BACK = "back";
    
    public static final String ACTION_DOWN = "action_down";
    public static final String ACTION_UP = "action_up";
    public static final String ACTION_CANCEL = "action_cancel";
    public static final String ACTION_MOVE = "action_move";
    
    public static final String ORIENTATION = "orientation";    
    public static final String ORIENTATION_LANDSCAPE = "orientation_landscape";    
    public static final String ORIENTATION_PORTRAIT = "orientation_portrait";    

    public static final String SENSOR_MODE = "sensormode";

    public static Point SCREEN_SIZE = new Point();
    
    private static final boolean LOGEV = true;
    
    private static final String NAME_PREF = "client_pref_name"; 
    private static final String NAME_MODE = "mode";
    private static final String NAME_ORIENTATION = ORIENTATION;
    
    public static final String MODE_TOUCH = "mode_touch";
    public static final String MODE_KEYBOARD = "mode_keyboard";
    public static final String MODE_SENSOR = "mode_sensor";
    public static final String MODE_SENSORTOTOUCH = "mode_sensortotouch";
    
    public static void initScreenRect(Context context) {
    	WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = manager.getDefaultDisplay();
		display.getRealSize(SCREEN_SIZE);
		if (SCREEN_SIZE.x > SCREEN_SIZE.y) {
			int x = SCREEN_SIZE.x;
			SCREEN_SIZE.x = SCREEN_SIZE.y;
			SCREEN_SIZE.y = x;
		}
    }
    
    public static void logd(Class<?> class1, String log) {
    	if (LOGEV) {
    		Log.d(class1.getName(), log);
    	}
    }
    
    public static void logi(Class<?> class1, String log) {
    	if (LOGEV) {
    		Log.i(class1.getName(), log);
    	}
    }
    
    public static void logv(Class<?> class1, String log) {
    	if (LOGEV) {
    		Log.v(class1.getName(), log);
    	}
    }
    
    public static void loge(Class<?> class1, String log) {
    	if (LOGEV) {
    		Log.e(class1.getName(), log);
    	}
    }
    
    public static String getMode(Context context) {
    	Context app = context.getApplicationContext();
    	SharedPreferences sharedPreferences = 
    			app.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);   	
    	return sharedPreferences.getString(NAME_MODE, MODE_KEYBOARD);
    }
    
    public static void setMode(Context context, String mode) {
    	Context app = context.getApplicationContext();
    	SharedPreferences sharedPreferences = 
    			app.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
    	Editor editor = sharedPreferences.edit();
    	editor.putString(NAME_MODE, mode);
    	editor.commit();
    }
    
    public static String getOrientation(Context context) {
    	Context app = context.getApplicationContext();
    	SharedPreferences sharedPreferences = 
    			app.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);   	
    	return sharedPreferences.getString(NAME_ORIENTATION, ORIENTATION_PORTRAIT);
    }
    
    public static void setOrientation(Context context, String orientation) {
    	Context app = context.getApplicationContext();
    	SharedPreferences sharedPreferences = 
    			app.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
    	Editor editor = sharedPreferences.edit();
    	editor.putString(NAME_ORIENTATION, orientation);
    	editor.commit();
    }
}
