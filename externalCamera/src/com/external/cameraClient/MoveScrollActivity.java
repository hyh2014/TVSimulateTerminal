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
		mButton.setMode(mMode);
		
		if (UdpHelper.getIp() != null) {
			Util.logd(getClass(), "UdpHelper.getIp() = " + UdpHelper.getIp());
			send(mMode);
		}
	}
	
	public void onPause() {
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
		SendHelper.send(string);        
	}
}
