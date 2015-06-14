package com.zombiefortress.server.Command;

import com.zombiefortress.server.Server;

public class StopCommand extends Command{

	public StopCommand() {
		super("stop", null);
		this.description = "Stops the server";
		this.isDefault = false;
	}

	@Override
	public void runCommand(Sender sender, String[] data) {
		Server.stopServer();
	}

}
