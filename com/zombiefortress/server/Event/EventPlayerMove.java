package com.zombiefortress.server.Event;

import com.zombiefortress.server.Player;

public class EventPlayerMove extends Event{
	
	private Player player;
	private int x;
	private int y;

	public EventPlayerMove(Player player, int x, int y) {
		super("PlayerMove");
		this.player = player;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void activateEvent(){
		if(player != null){
			player.setX(x);
			player.setY(y);
		}
	}

}
