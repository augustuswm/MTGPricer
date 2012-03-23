package com.mtgpricer;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class MTGPricerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Date timer = new Date();
    	
        super.onCreate(savedInstanceState);    
        
        setContentView(R.layout.main);
        
        ImagePHash hash = new ImagePHash();
		TextView hashView = (TextView) findViewById(R.id.Hash);
                
        ImageView image = (ImageView) findViewById(R.id.cardImage);
        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/download/Image.jpg");
        //bMap = hash.resize(bMap, 32, 32);
        //image.setImageBitmap(bMap);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        bMap.compress(CompressFormat.JPEG, 100, bos); 
        byte[] bitmapdata = bos.toByteArray();
        
        bMap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        
        //image.setImageBitmap(bMap);
        
        hashView.setText(Integer.toString(bMap.getHeight()));
        
        Mat canny = new Mat(320, 240, CvType.CV_8UC1);
        canny.put(0, 0, bitmapdata);
        
        try {
        	Utils.matToBitmap(canny, bMap);
            image.setImageBitmap(bMap);
        } catch (Exception e) {
			Log.d("MTGPricer", "Failed hash");
			e.printStackTrace();        	
        }

        /*Mat mRgba;
        Mat mGraySubmat;
        Mat mIntermediateMat;
        
        mGraySubmat = canny.submat(0, bMap.getHeight(), 0, bMap.getWidth());

        mRgba = new Mat();
        mIntermediateMat = new Mat();
        
        Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
        Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
        
        Bitmap bmp = Bitmap.createBitmap(bMap.getHeight(), bMap.getWidth(), Bitmap.Config.ARGB_8888);

        if (Utils.matToBitmap(canny, bmp))
            image.setImageBitmap(bmp);       
        
        try {
			String hashcode = hash.getHash("/sdcard/download/Image.jpg");
			//hashView.setText(hashcode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("MTGPricer", "Failed hash");
			e.printStackTrace();
		}*/
        
        //CameraPreview window = new CameraPreview(this);
        //setContentView(window);
    }

}