package com.zombiefortress.server.Event;

import com.zombiefortress.server.Packet;
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
		Server.getPackets().add(new Packet("login", player.getName() + "," + player.getAddress() + "," + player.getPort()));	
	}
	
	public void kickPlayer(String reason){
		setCanceled();
		Server.sendMessage("Kicked " + player.getName() + " For " + reason);
		Server.sendPacket("kick", reason, player);
	}
}
