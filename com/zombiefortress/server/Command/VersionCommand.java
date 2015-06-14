package com.zombiefortress.server.Command;

import com.zombiefortress.server.Server;

public class VersionCommand extends Command{

	public VersionCommand() {
		super("version", null);
		this.description = "Gets the server Version And build number";
	}

	@Override
	public void runCommand(Sender sender, String[] data) {
		sender.sendMessage("Server Version: " + Server.getVersion() + " Running build number: " + Server.getBuildNumber());
	}

}
