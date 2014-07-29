package com.external.cameraClient.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import android.net.wifi.WifiManager;
import android.util.Log;

import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;
import com.external.cameraClient.MainActivity;
import com.external.cameraClient.inter.ScrollListener;

public class UdpHelper {
    public Boolean IsThreadDisable = false;//指示监听线程是否终止
    private static WifiManager.MulticastLock lock;
    InetAddress mInetAddress;
    private ScrollListener mListener;
    private static final int BYTE_SIZE = 1000;
    private static final Integer sPort = 8800;
    private static String sIP = "10.100.138.49";
    
    private DatagramSocket mDatagramSocket;
    private DatagramPacket mDatagramPacket;
    
    public UdpHelper(WifiManager manager, ScrollListener listener) {
    	if (lock == null) {
            lock= manager.createMulticastLock("UDPwifi");
        } else {
            try {
                lock.release();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
         mListener = listener;
    }
    
    public static String getIp() {
    	if (sIP == null || sIP.isEmpty()) {
            return null;
        }
    	return sIP;
    }
    
    public void createSocket() {
        byte[] message = new byte[BYTE_SIZE];
        try {
            mDatagramSocket = new DatagramSocket(sPort);
            mDatagramSocket.setBroadcast(true);
            mDatagramPacket = new DatagramPacket(message, message.length);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    public void destroySocket() {
        mDatagramSocket.close();
        mDatagramSocket = null;
        mDatagramPacket = null;
    }
    
    public void StartListen()  {
        try {
            while (!IsThreadDisable) {
                lock.acquire();
                mDatagramSocket.receive(mDatagramPacket);

                String string = new String(mDatagramPacket.getData()).trim();
                string = string.substring(0, mDatagramPacket.getLength());
                String strMsg = URLDecoder.decode(string, "utf-8");
                
                mListener.refreshInfo(strMsg);
                lock.release();
            }
        } catch (Exception e) {//IOException
            e.printStackTrace();
        }
    }

    public static void setIp(String ip) {
        sIP = ip;
        
        SendHelper.send(Util.MODE_KEYBOARD);
        SendHelper.send(Util.START_CONN + Util.SCREEN_SIZE.x);
    }      
   
    public static String sendMessage(String message) {
        try {
            message = (message == null ? "hello" : 
                URLEncoder.encode(message, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Util.logd(UdpHelper.class, "message = " + message);
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            if (sIP == null || sIP.isEmpty()) {
                return null;
            } else {
                local = InetAddress.getByName(sIP);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int msg_length = message.length();
        byte[] messageByte = message.getBytes();
        DatagramPacket p = new DatagramPacket(messageByte, msg_length, local,
                sPort);
        try {
            s.send(p);
            s.close();           
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sIP;
    }
    
    public static String GetHostIp() {
        try { 
            for (Enumeration<NetworkInterface> en = NetworkInterface 
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); 
                        ipAddr.hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.e("UdpHelper", "" + inetAddress.getHostAddress().toString() +
                                "\n" + 
                                 "\n");
                        return inetAddress.getHostAddress();
                     }
                 }
             }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) { 
            e.printStackTrace();
        }
         return null;
     }
    
    public static String getIpv4() {
        String networkIp = "";  
        try {  
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());  
            for(NetworkInterface iface : interfaces){  
                if(iface.getDisplayName().equals("wlan0")){  
                    List<InetAddress> addresses = Collections.list(iface.getInetAddresses());  
                    for(InetAddress address : addresses){  
                        if(address instanceof Inet4Address){  
                            networkIp = address.getHostAddress(); 
                            Log.e("UdpHelper", "networkIp = " + networkIp);
                            return networkIp;
                        }  
                    }  
                }  
            }  
        } catch (SocketException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        return null;
    }

}
