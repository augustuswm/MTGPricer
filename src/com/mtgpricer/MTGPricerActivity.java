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
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class MTGPricerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    
        
        setContentView(R.layout.main);
        
        ImagePHash hash = new ImagePHash();
		TextView hashView = (TextView) findViewById(R.id.Hash);
		TextView hashView2 = (TextView) findViewById(R.id.Hash2);
		TextView distance = (TextView) findViewById(R.id.distance);
        ImageView image = (ImageView) findViewById(R.id.cardImage);
        ImageView image2 = (ImageView) findViewById(R.id.rotated);
        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/download/Image.jpg");
        
        double cardHeight = 9.525; // cm, equiv to 3.75 inches
        double cardWidth = 6.0325; // cm, equiv to 2.375 inches
        
        double sizeRatio = bMap.getHeight() / cardHeight;
        
        int topOffset = (int) Math.ceil(1.11125 * sizeRatio);
        int leftOffset = (int) Math.ceil(0.47625 * sizeRatio);
        int artHeight = (int) Math.ceil(3.81 * sizeRatio);
        int artWidth = (int) Math.ceil(5.23875 * sizeRatio);
        
        bMap = Bitmap.createBitmap(bMap, leftOffset, topOffset, artWidth, artHeight);
        
        image.setImageBitmap(bMap);
        
        try {
			hashView.setText(hash.getHash(bMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Matrix m = new Matrix();
        m.postScale(32, 32);
        m.postRotate(45);
        
        Bitmap art = Bitmap.createScaledBitmap(bMap, 32, 32, false);
        image2.setImageBitmap(art);
        
        try {
			hashView2.setText(hash.getHash(art));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
        distance.setText(Integer.toString(HammingDistance.distance(hashView.getText(), hashView2.getText())));
        
    }

}