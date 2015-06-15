package com.zombiefortress.server.Object.Tile;

import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class TileDoor extends TileObject{
	
	private boolean isInteractedWith;
	private int state;

	public TileDoor(int X, int Y) {
		super(X, Y, 4);
	}
	
	@Override
	public void update(){
		//MORETODO
		if(isInteractedWith){
			isInteractedWith = false;
			if(state == 1){
				state = 0;
			}else{
			state = 1;
			}
			
			for(Player p : Server.getPlayers()){
				Server.sendPacket("cd", "4," + this.posX + "," + this.posY + "," + state, p);
			}
		}
		
	}
	
	public void setOpen(boolean yes){
		this.isInteractedWith = yes;
	}
	
	public boolean isOpen(){
		if(state == 1){
			return true;
		}
		return false;
	}
}
