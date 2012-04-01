package com.mtgpricer;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;

public class Preview extends CameraPreview {
    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mIntermediateMat;
    private String[][] cardHashes;

    public Preview(Context context, String[][] hash) {
        super(context);
        this.cardHashes = hash;
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            // initialize Mats before usage
            mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
            mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

            mRgba = new Mat();
            mIntermediateMat = new Mat();
        }
    }

    @Override
    protected Bitmap processFrame(byte[] data) {
        mYuv.put(0, 0, data);
        
        Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        
        Bitmap bmp = Bitmap.createBitmap(getFrameWidth(), getFrameHeight(), Bitmap.Config.ARGB_8888);

        if (Utils.matToBitmap(mRgba, bmp)) {
            Card c = new Card(bmp);
            CardDimensions cd = c.getDims();
            String h = c.getHash();
            int bestHashIndex = 0, bestHashScore = 99;
        	
        	Log.d("Distance", "-------------------------------------");
            
            for (int i = 0; i < this.cardHashes.length; i++) {
            	int j = HammingDistance.distance(this.cardHashes[i][1], h);
            	
            	Log.d("Distance", Integer.toString(j));
            	
            	if (j < bestHashScore) {
            		bestHashScore = j;
            		bestHashIndex = i;
            	}
            }
            
            Core.putText(mRgba, this.cardHashes[bestHashIndex][0], new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 0.75, new Scalar(255, 0, 0, 255), 3);
            Core.rectangle(mRgba, new Point(cd.leftOffset(), cd.topOffset()), new Point(cd.leftOffset() + cd.artWidth(), cd.topOffset() + cd.artHeight()), new Scalar(255, 0, 0, 255));
            //Log.d("Distance",Integer.toString(HammingDistance.distance("0000111001111100101110110011000001100000110000011",c.getHash())));
            
            Utils.matToBitmap(mRgba, bmp);
            Log.i("MTGPricer", c.getHash());
            return bmp;
        }

        bmp.recycle();
        return null;
    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (mYuv != null)
                mYuv.release();
            if (mRgba != null)
                mRgba.release();
            if (mGraySubmat != null)
                mGraySubmat.release();
            if (mIntermediateMat != null)
                mIntermediateMat.release();

            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mIntermediateMat = null;
        }
    }
}
