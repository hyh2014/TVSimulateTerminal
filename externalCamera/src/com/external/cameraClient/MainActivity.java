package com.external.cameraClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;
import com.external.cameraClient.customview.CircleScrollButton;
import com.external.cameraClient.customview.ModeChangeDialog;
import com.external.cameraClient.customview.ScrollButton;
import com.external.cameraClient.customview.SettingIpDialog;
import com.external.cameraClient.inter.ScrollListener;
import com.external.cameraClient.udp.UdpHelper;

public class MainActivity extends Activity implements OnClickListener,
        ScrollListener {

    private CircleScrollButton mScrollButton;
    private Thread mThread;
    private ScrollListener mListener = this;
    private UdpHelper mHelper;
    private Dialog mDialog;
    private Toast mToast;
    private String mOrientation = Util.ORIENTATION_PORTRAIT;
    private TextView mChangeOrientation;
    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mScrollButton = (CircleScrollButton) findViewById(R.id.scroll);
        setClickListener();
        Util.initScreenRect(this);

        //ScrollButton.disposSensor(null);
        SendHelper.sBluetoothhelper.findBluetoothDevice();
    }
    
    private void setClickListener() {
        findViewById(R.id.up).setOnClickListener(this);
        findViewById(R.id.down).setOnClickListener(this);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.hint).setOnClickListener(this);
        findViewById(R.id.bind).setOnClickListener(this);
        mChangeOrientation = (TextView) findViewById(R.id.orientation);
        mChangeOrientation.setOnClickListener(this);
        mScrollButton.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    	if (UdpHelper.getIp() == null) {
    		showToast();
    	}
        int id = v.getId();
        String info;
        switch (id) {
        case R.id.up:
            info = Util.UP;           
            break;
        case R.id.down:
            info = Util.DOWN;           
            break;
        case R.id.left:
            info = Util.LEFT;
            break;
        case R.id.right:
            info = Util.RIGHT;           
            break;
        case R.id.ok:
            info = Util.OK;
            break;
        case R.id.cancel:
            info = Util.BACK;           
            break;
        case R.id.orientation:
        	if (Util.ORIENTATION_PORTRAIT.equals(mOrientation)) {
        		mChangeOrientation.setText(R.string.landscape);
        		info = mOrientation = Util.ORIENTATION_LANDSCAPE;       		
        	} else {
        		mChangeOrientation.setText(R.string.portrait);
        		info = mOrientation = Util.ORIENTATION_PORTRAIT;
        	}
        	Util.setOrientation(mContext, mOrientation);
        	break;
        case R.id.hint:
        	
        	showModeChangeDialog();
        	return;
        case R.id.bind:
            showBindDialog();
            return;
        default:
            info = null;
            break;
        }
        send(info);
    }
    
    public void showBindDialog() {
    	mDialog = null;
        mDialog = new SettingIpDialog(this);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showModeChangeDialog() {
    	mDialog = null;
        mDialog = new ModeChangeDialog(this);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshInfo(final String info) {
        
    }
    
    @Override
    public void send(final String message) {
        if (null == UdpHelper.getIp()) {
            runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    showToast();
                }
            });
        } else {
        	SendHelper.send(message);
        }
    }
    
    private void showToast() {
        if (null == mToast) {
            mToast = Toast.makeText(this, R.string.error_ip, 
                    Toast.LENGTH_SHORT);           
        }
        mToast.show();
        showBindDialog();
    }
    
    public void onResume() {
        super.onResume();
        if (true) {
            mThread = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                    mHelper = new UdpHelper(wifiManager, mListener);
                    mHelper.createSocket();
                    mHelper.StartListen();
                }
            });
            mThread.start();
        }
    }
    
    public void onPause() {
    	//mSensorManager.unregisterListener(mScrollButton);
        if (mHelper != null) {
            mHelper.IsThreadDisable = true;
            try {
                mHelper.destroySocket();
            } catch (Exception e) {
            }
        }
        mScrollButton.onPause();
        //mThread = null;
        super.onPause();
    }
    
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }
    
    public void onDestroy() {
    	super.onDestroy();
    }

	
    /*
    public SensorManager mSensorManager;
    public Sensor mSensor;
    
    private void initSensor(Context context) {
    	mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        try {
        	mSensorManager.registerListener(mScrollButton, mSensor, SensorManager.SENSOR_DELAY_GAME); 
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        //expand();
    }
    */
}
