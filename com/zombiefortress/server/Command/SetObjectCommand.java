package com.zombiefortress.server.Command;

import com.zombiefortress.server.Server;

public class SetObjectCommand extends Command{

	public SetObjectCommand() {
		super("setobject", null);
		this.isDefault = false;
	}

	@Override
	public void runCommand(Sender sender, String[] data) {
		
	}

}
