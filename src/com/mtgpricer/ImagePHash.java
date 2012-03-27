package com.mtgpricer;

import android.graphics.Bitmap;
import android.graphics.Color;
/*
 * pHash-like image hash. 
 * Author: Elliot Shepherd (elliot@jarofworms.com
 * Based On: http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
 */
public class ImagePHash {

	private int size = 32;
	private int smallerSize = 8;
	
	public ImagePHash() {
		initCoefficients();
	}
	
	public ImagePHash(int size, int smallerSize) {
		this.size = size;
		this.smallerSize = smallerSize;
		
		initCoefficients();
	}
	
	public int distance(String s1, String s2) {
		int counter = 0;
		for (int k = 0; k < s1.length();k++) {
			if(s1.charAt(k) != s2.charAt(k)) {
				counter++;
			}
		}
		return counter;
	}
	
	// Returns a 'binary string' (like. 001010111011100010) which is easy to do a hamming distance on. 
	public String getHash(Bitmap is) throws Exception {
		Bitmap img = is;
		
		/* 1. Reduce size. 
		 * Like Average Hash, pHash starts with a small image. 
		 * However, the image is larger than 8x8; 32x32 is a good size. 
		 * This is really done to simplify the DCT computation and not 
		 * because it is needed to reduce the high frequencies.
		 */
		img = resize(img, smallerSize, smallerSize);
		
		/* 2. Reduce color. 
		 * The image is reduced to a grayscale just to further simplify 
		 * the number of computations.
		 */
		img = toGrayscale(img);
		
		double[][] vals = new double[size][size];
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				vals[x][y] = getBlue(img, x, y);
			}
		}
		
		/* 3. Compute the DCT. 
		 * The DCT separates the image into a collection of frequencies 
		 * and scalars. While JPEG uses an 8x8 DCT, this algorithm uses 
		 * a 32x32 DCT.
		 */
		long start = System.currentTimeMillis();
		double[][] dctVals = applyDCT(vals);
		System.out.println("DCT: " + (System.currentTimeMillis() - start));
		
		/* 4. Reduce the DCT. 
		 * This is the magic step. While the DCT is 32x32, just keep the 
		 * top-left 8x8. Those represent the lowest frequencies in the 
		 * picture.
		 */
		/* 5. Compute the average value. 
		 * Like the Average Hash, compute the mean DCT value (using only 
		 * the 8x8 DCT low-frequency values and excluding the first term 
		 * since the DC coefficient can be significantly different from 
		 * the other values and will throw off the average).
		 */
		double total = 0;
		
		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++) {
				total += dctVals[x][y];
			}
		}
		total -= dctVals[0][0];
		
		double avg = total / ((smallerSize * smallerSize) - 1);
	
		/* 6. Further reduce the DCT. 
		 * This is the magic step. Set the 64 hash bits to 0 or 1 
		 * depending on whether each of the 64 DCT values is above or 
		 * below the average value. The result doesn't tell us the 
		 * actual low frequencies; it just tells us the very-rough 
		 * relative scale of the frequencies to the mean. The result 
		 * will not vary as long as the overall structure of the image 
		 * remains the same; this can survive gamma and color histogram 
		 * adjustments without a problem.
		 */
		String hash = "";
		
		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++) {
				if (x != 0 || y != 0) {
					hash += (dctVals[x][y] > avg?"1":"0");
				}
			}
		}
		
		return hash;
	}
	
	// Android
	public Bitmap resize(Bitmap image, int width, int height) {
		//Bitmap resizedImage = Bitmap.createBitmap(image, 0, 0, width, height);
		Bitmap resizedImage = Bitmap.createScaledBitmap(image, 32, 32, false);
		//Graphics2D g = resizedImage.createGraphics();
		//g.drawImage(image, 0, 0, width, height, null);
		//g.dispose();
		return resizedImage;
	}
	
	// Android
	private Bitmap toGrayscale(Bitmap img) {        
	    int width, height, pixel;
	    int A, R, G, B;
	    height = img.getHeight();
	    width = img.getWidth();    
	    
	    for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = img.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(0.299 * R + 0.587 * G + 0.114 * B);
                // set new pixel color to output bitmap
                img.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
	    
	    return img;
	}
	
	// Android
	private static int getBlue(Bitmap img, int x, int y) {
		return Color.blue(img.getPixel(x, y));
	}
	
	// DCT function stolen from http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java

	private double[] c;
	private void initCoefficients() {
		c = new double[size];
		
        for (int i=1;i<size;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }
	
	private double[][] applyDCT(double[][] f) {
		int N = size;
		
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
          for (int v=0;v<N;v++) {
            double sum = 0.0;
            for (int i=0;i<N;i++) {
              for (int j=0;j<N;j++) {
                sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*(f[i][j]);
              }
            }
            sum*=((c[u]*c[v])/4.0);
            F[u][v] = sum;
          }
        }
        return F;
    }

}