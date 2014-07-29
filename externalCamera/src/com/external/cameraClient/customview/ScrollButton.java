package com.external.cameraClient.customview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.external.camera.utils.Util;
import com.external.cameraClient.inter.ScrollListener;
import com.external.cameraClient.udp.UdpHelper;

public class ScrollButton extends View {

    public ScrollListener mListener;
    public String mStartInfo;
    public float mDownX;
    public float mDownY;
    public Point mCircleCenter;
    public int mRadius;
    public String mInfo;
    final Paint mPaint = new Paint();

    public long mCurrentTime = 0;
    public float mXvelocity = 0;
    public float mYvelocity = 0;
    public Point mSmallCirclePoint = new Point(0, 0);
    public float mValues[] = {11.0f, 11.0f, 11.0f};
    
    public static final int TIME_INTERVAL = 100;
    public static final int MSG_REFRESH = 0;
    
    public int mTime = 0;
    public boolean isInTouchMode = false;
    public static final String TAG = "ScrollButton";
    		
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public ScrollButton(Context context) {
        super(context);
        //initSensor(context);  
    }
    
    public ScrollButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initSensor(context);
    }
    
    public ScrollButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //initSensor(context);
    }
    
    
    public void setListener(ScrollListener listener) {
        mListener = listener;
    }
    
    public String formatString(float x, float y) {
        return x + Util.END + y + Util.END;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        int r = (int)Math.sqrt(Math.pow(mCircleCenter.x - x, 2) + 
                        Math.pow(mCircleCenter.y - y, 2));
        if (r > mRadius) {
            mListener.send("滑出边界");
            return true;
        }
        float moveX = x - mDownX;
        float moveY = y - mDownY;
        char c = '\n';
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            	mInfo = Util.ACTION_DOWN + Util.END;
                moveX = 0;
                moveY = 0;
                //mListener.send(mInfo);
                //return true;
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
            	isInTouchMode = false;
                mInfo = Util.ACTION_CANCEL + Util.END;
                break;
            case MotionEvent.ACTION_UP:
            	isInTouchMode = false;
                mInfo = Util.ACTION_UP + Util.END;   
                break;
            default:
                break;
        }
        mDownX = x;
        mDownY = y;
        mInfo = mInfo + formatString(moveX, moveY);
        mListener.send(mInfo);
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
        	mInfo = Util.ACTION_DOWN + Util.END;
        	mInfo = mInfo + formatString(0, 0);
            mListener.send(mInfo);
        	isInTouchMode = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
        	mInfo = Util.ACTION_UP + Util.END;
        	mInfo = mInfo + formatString(0, 0);
            mListener.send(mInfo);
        	isInTouchMode = true;
        }
        return true;
    }

	/*
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    	//Log.e("client", "onSensorChanged");
        //disposSensor(event);
    	if (!isInTouchMode) {
    		sendSensorMessage(event.values[0], event.values[1]);
    	}
        //SensorEventByte sensorEventByte = new SensorEventByte(event);
        //sensorEventByte = new SensorEventByte(sensorEventByte.toString());
        //UdpHelper.send(Util.SENSOR_MODE + new SensorEventByte(event).toString());
    }
    */
    
    public void sendSensorMessage(float x, float y) {
    	mTime = mTime++;
    	mTime = mTime % 100;
    	if (mTime == 0) {
    		mInfo = Util.ACTION_MOVE + Util.END;
    	} else {
    		return;
    	}
    	mInfo = mInfo + formatString(x, y);
        mListener.send(mInfo);
    }
    
    public float getCurrentPositionX() {
    	return 0;
    }
    
    public float getCurrentPositionY() {
    	return 0;
    }
    
    public void changeState(boolean flag) {
    	if (flag) {
    		
    	}
    }
    public void onPause() {
    	mInfo = Util.ACTION_UP + Util.END;
        mListener.send(mInfo);
    	isInTouchMode = true;
    }

}
