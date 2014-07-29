package com.external.cameraClient.customview;

import com.external.camera.utils.Util;
import com.external.cameraClient.R;
import com.external.cameraClient.MoveScrollActivity;
import com.external.cameraClient.SensorScrollActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;;

public class ModeChangeDialog extends Dialog implements OnClickListener{
    
	public ModeChangeDialog(Context context) {
        super(context, R.style.LauncherDialog);
        setContentView(R.layout.select_mode);
        initAdapter(context);
    }
	
    private void initAdapter(Context context) {
        findViewById(R.id.touch_mode).setOnClickListener(this);
        findViewById(R.id.sensor_mode).setOnClickListener(this);
        findViewById(R.id.sensortotouch_mode).setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		String mode = Util.MODE_KEYBOARD;
		switch (id) {
			case R.id.touch_mode:
				mode = Util.MODE_TOUCH;
				break;
			case R.id.sensor_mode:	
				mode = Util.MODE_SENSOR;
				break;
			case R.id.sensortotouch_mode:	
				mode = Util.MODE_SENSORTOTOUCH;
				break;
			default:
				break;
		}
		Util.setMode(getContext(), mode);

		if(mode == Util.MODE_SENSORTOTOUCH || mode == Util.MODE_SENSOR)
		{
			startSensorScrollActivity();
		}
		else if(mode == Util.MODE_TOUCH)
		{
			startMoveScrollActivity();
		}
		
		dismiss();
	}
	
	private void startMoveScrollActivity() {
		Context context = getContext();
        Intent intent = new Intent(getContext(), MoveScrollActivity.class);
        intent.putExtra(Util.ORIENTATION, Util.getOrientation(context));
        context.startActivity(intent);
        return;
        
	}

	private void startSensorScrollActivity() {
		Context context = getContext();
        Intent intent = new Intent(getContext(), SensorScrollActivity.class);
        intent.putExtra(Util.ORIENTATION, Util.getOrientation(context));
        context.startActivity(intent);
        return;
	}
}
