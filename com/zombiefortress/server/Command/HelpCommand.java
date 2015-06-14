package com.zombiefortress.server.Command;

import com.zombiefortress.server.Server;

public class HelpCommand extends Command{

	public HelpCommand() {
		super("help", null);
		this.description = "Lists all commands";
	}

	@Override
	public void runCommand(Sender sender, String[] data) {
		String str = "Command : Description \n";
		for(Command cmd : Server.getCommands()){
			str = str + cmd.getName() + " : " + cmd.getDescription() + "\n";
		}
		sender.sendMessage(str);
	}

}
