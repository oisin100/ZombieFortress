package com.zombiefortress.server;

import java.util.ArrayList;

import com.zombiefortress.server.Object.BaseObject;
import com.zombiefortress.server.Object.Tile.TileConcreteFloor;
import com.zombiefortress.server.Object.Tile.TileConcreteWall;
import com.zombiefortress.server.Object.Tile.TileGrass;

public class World {
	
	public ArrayList<BaseObject> objs = new ArrayList<BaseObject>();
	
	public World(){
		for(int i = -25; i <= 25; i++){
			for(int p = -25; p <= 25; p++){
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
				if(y == -13 || y == -8){
					if(x == -4 || x == 4){
						objs.add(new TileConcreteWall(x,y));
					}
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
	}
}
