package com.mtgpricer;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	SurfaceHolder cameraHolder;
	VideoCapture camera;
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

        /*Log.i("MTGPricer", "surfaceCreated");
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
        }*/
		//Log.i(TAG, "surfaceCreated");
        synchronized (this) {
            if (camera != null && camera.isOpened()) {
                //Log.i(TAG, "before mCamera.getSupportedPreviewSizes()");
                List<Size> sizes = camera.getSupportedPreviewSizes();
                //Log.i(TAG, "after mCamera.getSupportedPreviewSizes()");
                int mFrameWidth = width;
                int mFrameHeight = height;

                // selecting optimal camera preview size
                {
                    double minDiff = Double.MAX_VALUE;
                    for (Size size : sizes) {
                        if (Math.abs(size.height - height) < minDiff) {
                            mFrameWidth = (int) size.width;
                            mFrameHeight = (int) size.height;
                            minDiff = Math.abs(size.height - height);
                        }
                    }
                }

                camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, mFrameWidth);
                camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, mFrameHeight);
            }
        }
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/*camera = Camera.open();
        camera.setPreviewCallback(new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                synchronized (CameraPreview.this) {
                    mFrame = data;
                    CameraPreview.this.notify();
                }
            }
        });
        (new Thread(this)).start();*/

        //Log.i(TAG, "surfaceCreated");
        camera = new VideoCapture(Highgui.CV_CAP_ANDROID);
        if (camera.isOpened()) {
            (new Thread(this)).start();
        } else {
        	camera.release();
        	camera = null;
            //Log.e(TAG, "Failed to open native camera");
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        /*Log.i("MTGPricer", "surfaceDestroyed");
        mThreadRun = false;
        if (camera != null) {
            synchronized (this) {
            	camera.stopPreview();
            	camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }
        }*/
        //Log.i(TAG, "surfaceDestroyed");
        if (camera != null) {
            synchronized (this) {
            	camera.release();
            	camera = null;
            }
        }
	}
    protected abstract Bitmap processFrame(VideoCapture capture);

    public void run() {
        /*mThreadRun = true;
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
        }*/
    	//Log.i(TAG, "Starting processing thread");
        while (true) {
            Bitmap bmp = null;

            synchronized (this) {
                if (camera == null)
                    break;

                if (!camera.grab()) {
                    //Log.e(TAG, "mCamera.grab() failed");
                    break;
                }

                bmp = processFrame(camera);
            }

            if (bmp != null) {
                Canvas canvas = cameraHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawBitmap(bmp, (canvas.getWidth() - bmp.getWidth()) / 2, (canvas.getHeight() - bmp.getHeight()) / 2, null);
                    cameraHolder.unlockCanvasAndPost(canvas);
                }
                bmp.recycle();
            }
        }

        //Log.i(TAG, "Finishing processing thread");
    }

}