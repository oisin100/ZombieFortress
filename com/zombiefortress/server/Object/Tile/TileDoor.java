package com.zombiefortress.server.Object.Tile;

import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class TileDoor extends TileObject{
	
	private int state;

	public TileDoor(int X, int Y) {
		super(X, Y, 4);
	}
	
	@Override
	public void update(){
		
		if(data.equalsIgnoreCase("interacted")){
			data = "";
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
		this.data = "interacted";
	}
	
	public boolean isOpen(){
		if(state == 1){
			return true;
		}
		return false;
	}
}
