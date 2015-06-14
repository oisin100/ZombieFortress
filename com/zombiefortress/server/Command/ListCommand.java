package com.zombiefortress.server.Command;

import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class ListCommand extends Command{

	public ListCommand() {
		super("list", null);
		this.description = "Lists online players";
	}

	@Override
	public void runCommand(Sender sender, String[] data) {
		for(Player p : Server.getPlayers()){
			sender.sendMessage(p.getName() + " Port: " + p.getPort() + " Address: " + p.getAddress());
		}
		int i= 0;
		String players = "";
		for(Player p : Server.getPlayers()){
			i++;
			players = players + "," + p.getName();
		}
		if(i == 0){
			sender.sendMessage("Nobody online BooHoo D:");
			return;
		}
		sender.sendMessage("Players online " + i + ": \n" + players);
	}


}
