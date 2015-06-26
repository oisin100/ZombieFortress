package com.zombiefortress.server;

public class Location {

	private int locX;
	private int locY;
	
	public Location(int x, int y){
		this.locX = x;
		this.locY = y;
	}
	
	public int getX(){
		return locX;
	}
	
	public void setX(int x){
		this.locX = x;
	}
	
	public int getY(){
		return locY;
	}
	
	public void setY(int y){
		this.locY = y;
	}
}
