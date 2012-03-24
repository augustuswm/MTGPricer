package com.mtgpricer;

public class HammingDistance {

	static public int distance (byte[] x, byte[] y) {
		if (x.length != y.length)
			return -1;
		
		int distance = 0;
		
		for (int i = 0; i < x.length; i++)
			if (x[i] != y[i]) distance++;
		
		return distance;
	}

	static public int distance (String x, String y) {
		if (x.length() != y.length())
			return -1;
		
		int distance = 0;
		
		for (int i = 0; i < x.length(); i++)
			if (x.charAt(i) != y.charAt(i)) distance++;
		
		return distance;
	}

	public static int distance(CharSequence x, CharSequence y) {
		if (x.length() != y.length())
			return -1;
		
		int distance = 0;
		
		for (int i = 0; i < x.length(); i++)
			if (x.charAt(i) != y.charAt(i)) distance++;
		
		return distance;
	}
	
}
