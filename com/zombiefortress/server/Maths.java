package com.zombiefortress.server;

public class Maths {
	
	public static float getDistance(int x1, int y1, int x2, int y2){
		
		float x = x2-x1;
		float y = y2-y1;
		
		return (float)Math.sqrt((x*x) + (y*y));
	}
}
