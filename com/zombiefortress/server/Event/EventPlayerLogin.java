package com.zombiefortress.server.Event;

import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class EventPlayerLogin extends Event{
 
	private Player player;
	
	public EventPlayerLogin(Player player) {
		super("PlayerLogin");
		this.player = player;
	}

	@Override
	public void activateEvent() {
		Server.addPlayer(player);
		Server.sendMessage(("Player: " + player.getName() + " Logged in with the ip: " + player.getAddress()).trim());
		Server.sendWorld(player);
	}
	
	public void kickPlayer(String reason){
		setCanceled();
		Server.sendMessage("Kicked " + player.getName() + " For " + reason);
		Server.sendPacket("kick", reason, player);
	}
}
