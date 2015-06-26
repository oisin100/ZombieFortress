package com.zombiefortress.server.Object.Item;

import com.zombiefortress.server.Maths;
import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;
import com.zombiefortress.server.Object.BaseObject;

public class Item extends BaseObject{
	
	private boolean inInventory;
	private int stacksize;
	private int maxstacksize = 6;
	
	public Item(int x, int y, int ID, int stacksize){
		super(x, y, ID);
		this.stacksize = stacksize;
	}
	
	public Item(int x, int y, int ID){
		super(x, y, ID);
		this.stacksize = 1;
	}
	
	@Override
	public void update(){
		if(!inInventory){
			for(Player p : Server.getPlayers()){
				if(Maths.getDistance(p.getPosX(), p.getPosY(), posX, posY) <= 5f){
					//pickUp()
				}
			}
		}
	}
}
