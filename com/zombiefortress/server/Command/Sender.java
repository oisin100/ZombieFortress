package com.zombiefortress.server.Command;

import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class Sender {

	public Player player;
	
	public Sender(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		if(player != null){
			return player;
		}
		return null;
	}
	
	public void sendMessage(String msg){
		if(player != null){
			player.sendMessage(msg);
		}
		Server.sendMessage(msg);
	}
	
	public boolean isAdmin(){
		if(player != null){
			//TODO
			return false;
		}
		return true;
	}
	
}
