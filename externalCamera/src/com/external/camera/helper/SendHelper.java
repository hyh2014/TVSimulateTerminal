package com.external.camera.helper;

import android.util.Log;

import com.external.camera.utils.Util;
import com.external.cameraClient.bluetooth.Bluetoothhelper;
import com.external.cameraClient.udp.UdpHelper;

public class SendHelper {
    public static Bluetoothhelper sBluetoothhelper = new Bluetoothhelper();
    public static boolean sUseBluetooth = false;
    
	public static void send(String message) {
		final String str = message + Util.DEVIDE;
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (sUseBluetooth) {
					Log.e("SendHelper", "blue tooth send data");
					sBluetoothhelper.SendData(str);
				} else {
					Log.e("SendHelper", "wifi send data");
					UdpHelper.sendMessage(str);
				}
			}
		}).start();
    }
}
