package com.external.cameraService;

import com.external.camera.utils.Util;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.initScreenRect(this);
        finish();
    }
}
