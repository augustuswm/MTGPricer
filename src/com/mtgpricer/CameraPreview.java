package com.mtgpricer;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	SurfaceHolder cameraHolder;
	Camera camera;
    private int mFrameWidth;
    private int mFrameHeight;
    private byte[] mFrame;
    private boolean mThreadRun;
	
	public CameraPreview(Context context) {
		super(context);
		cameraHolder = getHolder();
		cameraHolder.addCallback(this);
		//cameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {

        Log.i("MTGPricer", "surfaceCreated");
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            mFrameWidth = width;
            mFrameHeight = height;

            // selecting optimal camera preview size
            {
                double minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - height) < minDiff) {
                        mFrameWidth = size.width;
                        mFrameHeight = size.height;
                        minDiff = Math.abs(size.height - height);
                    }
                }
            }

            params.setPreviewSize(getFrameWidth(), getFrameHeight());
            camera.setParameters(params);
            try {
            	camera.setPreviewDisplay(null);
			} catch (IOException e) {
				Log.e("MTGPricer", "camera.setPreviewDisplay fails: " + e);
			}
            camera.startPreview();
        }
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
        camera.setPreviewCallback(new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                synchronized (CameraPreview.this) {
                    mFrame = data;
                    CameraPreview.this.notify();
                }
            }
        });
        (new Thread(this)).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MTGPricer", "surfaceDestroyed");
        mThreadRun = false;
        if (camera != null) {
            synchronized (this) {
            	camera.stopPreview();
            	camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }
        }
	}
    protected abstract Bitmap processFrame(byte[] data);

    public void run() {
        mThreadRun = true;
        Log.i("MTGPricer", "Starting processing thread");
        while (mThreadRun) {
            Bitmap bmp = null;

            synchronized (this) {
                try {
                    this.wait();
                    bmp = processFrame(mFrame);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (bmp != null) {
                Canvas canvas = cameraHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawBitmap(bmp, (canvas.getWidth() - getFrameWidth()) / 2, (canvas.getHeight() - getFrameHeight()) / 2, null);
                    cameraHolder.unlockCanvasAndPost(canvas);
                }
                bmp.recycle();
            }
        }
    }

}