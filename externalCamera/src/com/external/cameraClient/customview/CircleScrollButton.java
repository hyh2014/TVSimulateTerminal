package com.external.cameraClient.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.external.camera.utils.Util;

public class CircleScrollButton extends ScrollButton {

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public CircleScrollButton(Context context) {
        super(context);
    }
    
    public CircleScrollButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    
    public CircleScrollButton(Context context, AttributeSet attrs, int defStyle) {
       super(context, attrs, defStyle);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return super.onTouchEvent(event);
    }
    
    private void drawSmallCircle(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        if (true && !mHandler.hasMessages(MSG_REFRESH)) {
        }
    }
    int width;
    int height;
    public void onDraw(Canvas canvas) {
        if (mCircleCenter == null) {
            width = getWidth() / 2;
            height = getHeight() /2;
            mCircleCenter = new Point(width, height);
            if (width > height) {
                mRadius = height;
            } else {
                mRadius = width;
            }
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            mSmallCirclePoint.x = width;
            mSmallCirclePoint.y = height;
        } else {
            mSmallCirclePoint.x += TIME_INTERVAL * mXvelocity;
            mSmallCirclePoint.y += TIME_INTERVAL * mYvelocity;
        }
        canvas.drawCircle(width, height, mRadius, mPaint);
        drawSmallCircle(canvas);
        super.onDraw(canvas);
    }
}
