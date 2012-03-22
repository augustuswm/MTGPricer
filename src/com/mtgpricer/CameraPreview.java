package com.mtgpricer;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder cameraHolder;
	Camera camera;
	
	public CameraPreview(Context context) {
		super(context);
		
		cameraHolder = getHolder();
		cameraHolder.addCallback(this);
		cameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		List<Size> mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
		
		Log.d("MTGPricerCamera", Integer.toString(mSupportedPreviewSizes.size()));
		
		for (int i = 0; i < mSupportedPreviewSizes.size(); i++) {
			Log.d("MTGPricerCamera", Integer.toString(mSupportedPreviewSizes.get(i).width));
			Log.d("MTGPricerCamera", Integer.toString(mSupportedPreviewSizes.get(i).height));
		}
		
		Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        camera.setParameters(parameters);
		
		try {
			camera.startPreview();
			Log.d("MTGPricerCamera", "Started Preview");
		} catch (Exception e) {
			Log.e("MTGPricerCamera", e.toString());
		}	
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		
		try {
			camera.setPreviewDisplay(holder);
			Log.d("MTGPricerCamera", "Set PreviewDisplay");
		} catch (Exception e) {
			Log.e("MTGPricerCamera", "IOException caused by setPreviewDisplay()", e);
		}		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();		
	}

}
