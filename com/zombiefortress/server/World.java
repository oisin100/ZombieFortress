package com.zombiefortress.server;

import java.util.ArrayList;

import com.zombiefortress.server.Object.BaseObject;
import com.zombiefortress.server.Object.EntityZombie;
import com.zombiefortress.server.Object.Tile.TileConcreteFloor;
import com.zombiefortress.server.Object.Tile.TileConcreteWall;
import com.zombiefortress.server.Object.Tile.TileDoor;
import com.zombiefortress.server.Object.Tile.TileGrass;

public class World {
	
	public ArrayList<BaseObject> objs = new ArrayList<BaseObject>();
	private int zombiecount;
	
	public World(){
		zombiecount = 0;
		for(int i = -25; i <= 25; i++){
			for(int p = -10; p <= 10; p++){
				objs.add(new TileGrass(i,p));
			}
		}
		for(int x = -4; x <= 4; x++){
			for(int y = -3; y <= 3; y++){
				objs.add(new TileConcreteFloor(x,y));
			}
		}
		
		objs.add(new TileConcreteFloor(0, -4));
		objs.add(new TileConcreteFloor(-1, -4));
		objs.add(new TileConcreteFloor(1, -4));
		objs.add(new TileConcreteFloor(0, -5));
		objs.add(new TileConcreteFloor(-1, -5));
		objs.add(new TileConcreteFloor(1, -5));
		objs.add(new TileConcreteFloor(0, -6));
		objs.add(new TileConcreteFloor(-1, -6));
		objs.add(new TileConcreteFloor(1, -6));
		objs.add(new TileConcreteFloor(0, -7));
		objs.add(new TileConcreteFloor(-1, -7));
		objs.add(new TileConcreteFloor(1, -7));
		
		for(int x = -4; x <= 4; x++){
			for(int y = -13; y <= -8; y++){
				objs.add(new TileConcreteFloor(x,y));
				if(y == -13 || y == -8 && (x != 0 || y == -13)){
					objs.add(new TileConcreteWall(x,y));
				}
				if(x == -4 || x == 4){
					if(x != 0)
					objs.add(new TileConcreteWall(x,y));
				}
			}
		}
		
		for(int x = -4; x <= 4; x++){
			for(int y = -3; y <= 2; y++){
				objs.add(new TileConcreteFloor(x,y));
				if(y == -2 || y == 3 && (x != 0 || y == 2)){
					objs.add(new TileConcreteWall(x,y));
				}
				if(x == -4 || x == 4){
					if(x != 0)
					objs.add(new TileConcreteWall(x,y));
				}
			}
		}
		
	}
	
	public void update(){
		for(BaseObject obj : objs){
			obj.update();
		}
	}
	
	public ArrayList<BaseObject> getObjects(){
		return objs;
	}
	
	public void setTile(int x, int y, int ID){
		if(ID == Material.GRASS){
			objs.add(new TileGrass(x,y));
		}
		if(ID == Material.CONCRETE_FLOOR){
			objs.add(new TileConcreteFloor(x,y));
		}
		if(ID == Material.CONCRETE_WALL){
			objs.add(new TileConcreteWall(x,y));
		}
		if(ID == Material.DOOR){
			objs.add(new TileDoor(x,y));
		}
	}
	
	public BaseObject getTile(Location loc){
		return getTile(loc.getX(), loc.getY());
	}
	
	public BaseObject getTile(int x, int y){
		for(BaseObject obj : objs){
			if(obj.getID() <= 70){
				if(obj.getPosX() == x){
					if(obj.getPosY() == y){
						return obj;
					}
				}
			}
		}
		return null;
	}
	
	
	public int getZombieCount(){
		return zombiecount;
	}
	
	public void addZombieCount(){
		this.zombiecount+=1;
	}
}
