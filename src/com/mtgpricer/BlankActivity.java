package com.mtgpricer;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class BlankActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	String value = "";
        
        Bundle extras = getIntent().getExtras(); 
        if (extras !=null) {
        	value= extras.getString("filepath");
        }
        
        Card c = new Card(BitmapFactory.decodeFile(value));
        
        TextView t = (TextView) findViewById(R.id.Hash2);
        
        t.setText(c.getHash());
        Log.d("MTGPricer", t.getText().toString());
        
        ImageView i = (ImageView) findViewById(R.id.cardImage);
        
        i.setImageBitmap(c.getCardArtGray());
    }

}
