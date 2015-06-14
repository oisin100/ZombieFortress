package com.zombiefortress.server.Object;

public class BaseObject {

	protected int ID;
	protected String name;
	protected boolean isSolid;
	
	protected int posX;
	protected int posY;
	
	public BaseObject(int X, int Y, int ID){
		this.posX = X;
		this.posY = Y;
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public void setLocation(int x, int y){
		this.posX = x;
		this.posY = y;
	}
	
	public boolean isSoliid(){
		return this.isSolid;
	}
	
	public void update(){};
}
