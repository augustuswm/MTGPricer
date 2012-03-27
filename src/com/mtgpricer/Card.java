package com.mtgpricer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Card {

	private CardDimensions dim;
	private Bitmap cardArt;
	private ImagePHash hasher;
	private String hash;
	
	public Card (Bitmap card) {
        
        dim = new CardDimensions(card, true);
        cardArt = Bitmap.createBitmap(card, dim.leftOffset(), dim.topOffset(), dim.artWidth(), dim.artHeight());
        hasher = new ImagePHash();
        
        try {
			hash = hasher.getHash(card);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
    
    public String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}