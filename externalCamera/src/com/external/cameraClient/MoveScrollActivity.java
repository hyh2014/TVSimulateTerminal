package com.external.cameraClient;

import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;
import com.external.cameraClient.customview.MoveScrollButton;
import com.external.cameraClient.customview.ScrollButton;
import com.external.cameraClient.inter.ScrollListener;
import com.external.cameraClient.udp.UdpHelper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MoveScrollActivity extends Activity implements ScrollListener {
	MoveScrollButton mButton;
	String mMode;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOritation();
		setContentView(R.layout.activity_movescroll);
		mButton = (MoveScrollButton)findViewById(R.id.scroll);
		mButton.setListener(this);
        //scrollButton.disposSensor(null);
	}
	
	private void setOritation() {
		String oritation = getIntent().getStringExtra(Util.ORIENTATION);
		if (Util.ORIENTATION_PORTRAIT.equals(oritation)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
	}
	
	public void onResume() {
		super.onResume();
		mMode = Util.getMode(this);

		/*
		if (!Util.MODE_TOUCH.equals(mMode)) {
			initSensor(this);
		}*/
		
		mButton.setMode(mMode);
		
		if (UdpHelper.getIp() != null) {
			//UdpHelper.send(Util.ORIENTATION_LANDSCAPE);
			Util.logd(getClass(), "UdpHelper.getIp() = " + UdpHelper.getIp());
			SendHelper.send(mMode);
		}
	}

	/*
    public SensorManager mSensorManager;
    public Sensor mSensor;
    
    private void initSensor(Context context) {
    	mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        try {
        	mSensorManager.registerListener(mButton, mSensor, SensorManager.SENSOR_DELAY_GAME); 
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        //expand();
    }
	*/
	
	public void onPause() {
	/*
		if (!Util.MODE_TOUCH.equals(mMode)) {
			mSensorManager.unregisterListener(mButton);
		}
		*/
		
		super.onPause();
		send(Util.MODE_KEYBOARD);
		mButton.onPause();
	}
	
	@Override
	public void refreshInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(final String string) {
		// TODO Auto-generated method stub
		SendHelper.send(string);        
	}
}