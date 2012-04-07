package com.mtgpricer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

public class MTGPricerActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	String[][] cardHashes = new String[233][2];
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        try{
			//InputStream img = new FileInputStream("Image.jpg");
			
			
			// Open the file that is the first 
			// command line parameter
			InputStream fstream = getResources().openRawResource(R.raw.shardsofalarahash);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			int i = 0;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				String[] a = strLine.split(":");
				cardHashes[i][0] = a[0].trim();
				cardHashes[i][1] = a[1].trim();
				i++;
			}

			//Close the input stream
			in.close();
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

        setContentView(new Preview(this, cardHashes));
        /*Card c = new Card(BitmapFactory.decodeFile("/sdcard/download/Image.jpg"));
        Log.d("Hash", c.getHash());

        setContentView(R.layout.main);

        ImageView i = (ImageView) findViewById(R.id.cardImage);
        
        i.setImageBitmap(c.getCardArt());*/
        /*
        
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
        
        String x = "0001111001111100100111010011000001100000110000000";
        String y = "0000111001111100101110110011000001100000110000011";
        String z = "0111110001111100111110000001000000000000000000000";
        
        Log.d("Distance",Integer.toString(HammingDistance.distance(x,y)));
        Log.d("Distance",Integer.toString(HammingDistance.distance(x,z)));
        Log.d("Distance",Integer.toString(HammingDistance.distance(y,z)));
        
        */
        
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