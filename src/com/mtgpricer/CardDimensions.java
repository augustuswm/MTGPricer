package com.mtgpricer;

import android.graphics.Bitmap;

public class CardDimensions {
	
	private boolean newFrame;
	
	private double cardHeight = 9.525; // cm, equiv to 3.75 inches
    private double cardWidth = 6.0325; // cm, equiv to 2.375 inches
    
    private double ratio;
    
    // Card artwork for new frame
    private double newTopOffset = 1.1075;
    private double newLeftOffset = 0.59625;
    private double newArtHeight = 4.1275;
    private double newArtWidth = 5.63875;
    
    // Card artwork for old frame
    private double oldTopOffset = 0.9525;
    private double oldLeftOffset = 0.84625;
    private double oldArtHeight = 3.81;
    private double oldArtWidth = 5.23875;
	
	public CardDimensions (Bitmap card, boolean newFrame) {
		this.newFrame = newFrame;
		this.ratio = card.getHeight() / cardHeight;
	}
	
	public int leftOffset() {
		return (int) Math.ceil((this.newFrame ? newLeftOffset : oldLeftOffset) * ratio);
	}
	
	public int topOffset() {
		return (int) Math.ceil((this.newFrame ? newTopOffset : oldTopOffset) * ratio);
	}
	
	public int artWidth() {
		return (int) Math.ceil((this.newFrame ? newArtWidth : oldArtWidth) * ratio);
	}
	
	public int artHeight() {
		return (int) Math.ceil((this.newFrame ? newArtHeight : oldArtHeight) * ratio);
	}
	
}
