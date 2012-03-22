package com.mtgpricer;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MTGPricerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Date timer = new Date();
    	
        super.onCreate(savedInstanceState);    
        
        setContentView(R.layout.main);
        
        ImagePHash hash = new ImagePHash();
                
        ImageView image = (ImageView) findViewById(R.id.cardImage);
        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/download/Image.jpg");
        //bMap = hash.resize(bMap, 32, 32);
        image.setImageBitmap(bMap);
                
        //ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        //bMap.compress(CompressFormat.JPEG, 0, bos); 
        //byte[] bitmapdata = bos.toByteArray();
                
        try {
			String hashcode = hash.getHash("/sdcard/download/Image.jpeg");
			TextView hashView = (TextView) findViewById(R.id.Hash);
			hashView.setText(hashcode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("MTGPricer", "Failed hash");
			e.printStackTrace();
		}
      
        
        //CameraPreview window = new CameraPreview(this);
        //setContentView(window);
    }

}