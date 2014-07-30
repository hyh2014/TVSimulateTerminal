package com.external.cameraClient.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.external.camera.helper.SendHelper;
import com.external.camera.utils.Util;
import com.external.cameraClient.R;
import com.external.cameraClient.udp.UdpHelper;

public class SettingIpDialog extends Dialog implements TextWatcher, 
        OnClickListener {
    private EditText mEditText;
    private Toast mToast;
    
    public SettingIpDialog(Context context) {
        super(context, R.style.LauncherDialog);
        setContentView(R.layout.select_connectmode);
        initAdapter(context);
    }
    
    private void initAdapter(Context context) {
        
        findViewById(R.id.bluetoothmode).setOnClickListener(this);
        findViewById(R.id.wifimode).setOnClickListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ok:
                String ip = mEditText.getText().toString();
                if (ip == null || ip.isEmpty()) {
                    return;
                }
                UdpHelper.setIp(ip);
                showToast(ip);
                mEditText.setText(null);
                break;
            case R.id.cancel:
                break;
            case R.id.bluetoothmode:
            	SendHelper.sUseBluetooth = true;
            	Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	getContext().startActivity(intent);
				/*
            	SendHelper.send(Util.MODE_KEYBOARD);
		        SendHelper.send(Util.START_CONN + Util.SCREEN_SIZE.x);
				*/
            	break;
            case R.id.wifimode:
            	SendHelper.sUseBluetooth = false;
            	setContentView(R.layout.setting_ip);
            	mEditText = (EditText) findViewById(R.id.edit_ip);
                mEditText.addTextChangedListener(this);
                mEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                findViewById(R.id.ok).setOnClickListener(this);
                findViewById(R.id.cancel).setOnClickListener(this);
                return;
            default:
                break;
        }
        dismiss();
    }
    
    private void showToast(String ip) {
        String hint = getContext().getResources().getString(R.string.success_setting_ip)
                + ip;
        
        if (null == mToast) {
            mToast = Toast.makeText(getContext(), hint, 
                    Toast.LENGTH_SHORT);           
        } else {
            mToast.setText(hint);
        }
        mToast.show();
    }
}
