package com.external.cameraClient.customview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.external.camera.data.SensorEventByte;
import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;
import com.external.cameraClient.udp.UdpHelper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MoveScrollButton extends ScrollButton {

	private String mMode = Util.MODE_TOUCH;
    public MoveScrollButton(Context context) {
        super(context);       
    }
    
    public MoveScrollButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MoveScrollButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (!Util.MODE_TOUCH.equals(mMode)) {
    		return true;
    	}
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            	isInTouchMode = true;
            	mInfo = Util.ACTION_DOWN + Util.END;
                break;
            case MotionEvent.ACTION_MOVE:
            	mTime = mTime++;
            	mTime = mTime % 3;
            	if (mTime == 0) {
            		mInfo = Util.ACTION_MOVE + Util.END;
            	} else {
            		return true;
            	}
                break;
            case MotionEvent.ACTION_CANCEL:
                mInfo = Util.ACTION_CANCEL + Util.END;
                break;
            case MotionEvent.ACTION_UP:
                mInfo = Util.ACTION_UP + Util.END;   
                break;
            default:
                break;
        }
        mDownX = x;
        mDownY = y;

		Log.e("MoveScrollButton", "x=" + x + "y=" + y);
		
        mInfo = mInfo + formatString(x, y);
        mListener.send(mInfo);
        if (Util.MODE_SENSORTOTOUCH.equals(mMode)) {
        	if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            	mInfo = Util.ACTION_DOWN + Util.END;
            	mInfo = mInfo + formatString(x, y);
                mListener.send(mInfo);
            	isInTouchMode = false;
            } else if (action == MotionEvent.ACTION_DOWN) {
            	mInfo = Util.ACTION_UP + Util.END;
            	mInfo = mInfo + formatString(x, y);
                mListener.send(mInfo);
            	isInTouchMode = true;
            }
        }
        
        return true;
    }

	/*
    @Override
    public void onSensorChanged(SensorEvent event) {
    	if (Util.MODE_SENSORTOTOUCH.equals(mMode)) {
	    	mDownX = mDownX + event.values[0];
	    	mDownY = mDownY + event.values[1];
	    	sendSensorMessage(mDownX, mDownY);
    	} else if (Util.MODE_SENSOR.equals(mMode)) {
            SendHelper.send(Util.SENSOR_MODE + new SensorEventByte(event).toString());
    	}
    }
    */
    
    public void setMode(String mode) {
    	mMode = mode;
    }
}
