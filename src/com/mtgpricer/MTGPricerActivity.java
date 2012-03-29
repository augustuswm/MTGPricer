package com.mtgpricer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MTGPricerActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, BlankActivity.class).putExtra("filepath", "/sdcard/download/Image.jpg");

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Snap").setIndicator("Snap",
                          res.getDrawable(R.drawable.tabs))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, BlankActivity.class).putExtra("filepath", "/sdcard/download/Image2.jpg");
        spec = tabHost.newTabSpec("Crackle").setIndicator("Crackle",
                          res.getDrawable(R.drawable.tabs))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, BlankActivity.class).putExtra("filepath", "/sdcard/download/Image3.jpg");
        spec = tabHost.newTabSpec("Pop").setIndicator("Pop",
                          res.getDrawable(R.drawable.tabs))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        /*String x = "101010110011000100010100010100111110010011001101000100100101001";
        String y = "101010011011110100010010010100111110010011101100000100000000001";
        String z = "111010010000100100010010010100110011010011101101100110000100001";
        
        Log.d("Distance",Integer.toString(HammingDistance.distance(x,y)));
        Log.d("Distance",Integer.toString(HammingDistance.distance(x,z)));
        Log.d("Distance",Integer.toString(HammingDistance.distance(y,z)));*/
        
        /*
        //Get the text file
        File file = new File("/sdcard/download/file.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();
        
        String[] k = new String[30000];
        int c = 0;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String strLine;

            while ((strLine = br.readLine()) != null) {
				String[] a = strLine.split(":");
				
				for (int i = 1; i < a.length; i++) {
					k[c++] = a[i];
				}
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }*/
        
        
    }

}