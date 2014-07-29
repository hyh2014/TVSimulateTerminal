package com.external.cameraClient.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;
import android.util.Log;

public class Bluetoothhelper {
	
	private static final String sTag = "BusniessBluetooth";  
    private BluetoothAdapter mBluetoothAdapter; 

    private InputStream mInputStream;  
    private OutputStream mOutputStream;  
    private int mState;  
    private int mStateConnected = 0;  
    private int mStateDisConnect = 1;  
    private boolean mIsNormalClose = false;  
    private Message mMessage = new Message();  
    private PortListenThread mPortListenThread;  
    private OnPortListener mOnPortListener;  
    
    public interface OnPortListener {     	  
        public abstract void OnReceiverData(String p_Message);  
    }
    
    public Bluetoothhelper() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  
    }
    
    private static final String SNAME = "BluetoothChat";  
    private static final UUID SUUID = UUID  
            .fromString("00001101-0000-1000-8000-00805F9B34FB");  
    
	public void CreatePortListen() {  
        try {    
            if (mBluetoothAdapter != null || mBluetoothAdapter.isEnabled()) {  
                BluetoothServerSocket BluetoothServerSocket = mBluetoothAdapter  
                        .listenUsingRfcommWithServiceRecord(SNAME, SUUID);  
  
                if (mPortListenThread == null) {  
                    mPortListenThread = new PortListenThread(  
                            BluetoothServerSocket);  
                    mPortListenThread.start();  
                }  
            } else {  
                //mHandler.sendMessage(mMessage);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            CreatePortListen();  
        }  
    }  
  
    public class PortListenThread extends Thread {  
  
        private BluetoothServerSocket mBluetoothServerSocket;  
  
        public PortListenThread(BluetoothServerSocket pBluetoothServerSocket) {  
            mBluetoothServerSocket = pBluetoothServerSocket;  
        }  
  
        @Override  
        public void run() {  
            try {  
                BluetoothSocket bluetoothSocket = mBluetoothServerSocket  
                        .accept();  
                mState = mStateConnected;  
                while (mState == mStateConnected) {  
                    mInputStream = bluetoothSocket.getInputStream();  
                    ReceiverData();  
                }  
            } catch (Exception e) {  
                Log.i(sTag, e.getMessage());  
                if (!mBluetoothAdapter.isEnabled()) {  
                    //mHandler.sendMessage(mMessage);  
                }  
            }  
        }  
  
        public void Close() {  
            try {  
                mBluetoothServerSocket.close();  
                if (mInputStream != null) {  
                    mInputStream.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();
            }  
        }  
    }  
  
    public void ReceiverData() {  
        try {
            byte[] Byte = new byte[1024];  
            int len = 0;  
            int ttemp = 0;  
            while((ttemp = mInputStream.read()) != -1){  
                Byte[len++] = (byte)ttemp;  
            }  
            String Msg = new String(Byte,0,Byte.length);  
            mOnPortListener.OnReceiverData(Msg);  
  
        } catch (Exception e) {  
            Log.i(sTag, e.getMessage());  
            if (!mIsNormalClose) {  
                Close(false);  
                CreatePortListen();  
            }  
        }  
  
    }  
    
    public void SendData(String pData) {
        try {
        	Util.logd(getClass(), "pData = " + pData);
            mOutputStream.write(pData.getBytes());  
        } catch (Exception e) {  
            //Log.i(sTag, e.getMessage()); 
        	e.printStackTrace();
        }  
    }  
  
    public void Close(boolean pIsNormalClose) {  
    	
    	//BluetoothDevice.createRfcommSocketToServiceRecord();
    	
        mIsNormalClose = pIsNormalClose;  
        mState = mStateDisConnect;
        try {
			mOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (mPortListenThread != null) {  
            mPortListenThread.Close();  
            mPortListenThread = null;  
        }  
    }
    
    public void findBluetoothDevice() {
	    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	    Log.e(sTag, "pairedDevices.size = " + pairedDevices.size());
	    if (pairedDevices.size() > 1) {  
	    	return;
	    } else {
	    	for (BluetoothDevice device : pairedDevices) {
	    		try {
	    			mBluetoothAdapter.cancelDiscovery();
					BluetoothSocket socket = device.createRfcommSocketToServiceRecord(SUUID);
					socket.connect();
					mOutputStream = socket.getOutputStream();
					SendHelper.send(Util.MODE_KEYBOARD);
			        SendHelper.send(Util.START_CONN + Util.SCREEN_SIZE.x);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	} 

	    }  
    }
}
