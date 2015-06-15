package com.zombiefortress.server.Object.Tile;

import com.zombiefortress.server.Object.BaseObject;

public class TileObject extends BaseObject{
	
	private int health = -1;
	private int strength = -1;
	
	public TileObject(int X, int Y, int ID) {
		super(X, Y, ID);	
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getStrenght(){
		return strength;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void addHealth(int health){
		this.health =+ health;
	}
}
