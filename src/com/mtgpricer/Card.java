package com.mtgpricer;

import android.graphics.Bitmap;

public class Card {

	private CardDimensions dim;
	private Bitmap card, cardArt;
	private ImagePHash hasher;
	private String hash;
	
	public Card (Bitmap card) {
        
		this.card = card;
        dim = new CardDimensions(card, true);
        cardArt = Bitmap.createBitmap(card, dim.leftOffset(), dim.topOffset(), dim.artWidth(), dim.artHeight());
        hasher = new ImagePHash();
        
        try {
			hash = hasher.bmv_hash(cardArt);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	public Bitmap getCard() {
		return this.card;
	}
	
	public Bitmap getCardArt() {
		return this.cardArt;
	}
	
	public Bitmap getCardArtGray() {
		return this.hasher.toGrayscale(this.cardArt);
	}
	
	public String getHash() {
		return this.hash;
	}
	
	public CardDimensions getDims() {
		return this.dim;
	}
    
    public String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}