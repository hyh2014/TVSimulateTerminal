package com.external.cameraService.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import com.external.cameraService.helper.SensorJniHelper;

public class SensorHelper {
    private static ArrayList<Object> listBack = new ArrayList<Object> ();
	private static SensorJniHelper sensorHelper = new SensorJniHelper();
	
    public static void disposSensor(SensorEvent event) {
		try {
			Class<?> sensorManager = Class.forName("android.hardware.SystemSensorManager");
			
			Field expand = sensorManager.getDeclaredField("sListeners");
			expand.setAccessible(true);
			ArrayList<Object> list = (ArrayList<Object>) expand.get(sensorManager);
			Log.e("SensorHelper", "list.size() = " + list.size() + list);
			
			if (!list.isEmpty()) {
				listBack.clear();
			}
			
			if (listBack.isEmpty()) {
				listBack.addAll(list);
			}
			//list.clear();

			Log.e("SensorHelper", "listBack.size() = " + listBack.size());
			if (event == null) {
				return;
			}
			
			Class<?> listenerDelegate = Class.forName("android.hardware.SystemSensorManager$ListenerDelegate");
			
			Sensor sensor = event.sensor;
	        float[] floats = event.values;
	        long[] timestamp = {event.timestamp};
	        int accuracy = event.accuracy;
			
			Class<?> [] params = {sensor.getClass(), 
					floats.getClass(), timestamp.getClass(), int.class};

			Method method = listenerDelegate.getDeclaredMethod("onSensorChangedLocked",
					params[0], params[1], params[2], params[3]);
			method.setAccessible(true);

            int iSensorType = event.sensor.getType();
			float fGX = event.values[0];
			float fGY = event.values[1];
			float fGZ = event.values[2];
			
            Log.e("SensorHelper", "iSensorType = " + iSensorType);
			Log.e("SensorHelper", "x, y, z = " + fGX + fGY + fGZ);
			
			for (int i = 0; i < listBack.size(); i++) {
				//method.invoke(listBack.get(i), sensor, floats, timestamp, accuracy);
				//sensor type, x, y, z, 0, 0;
        		sensorHelper.sendSensorEventRemote(iSensorType, (int)fGX, (int)fGY, (int)fGZ, 0, 0);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
