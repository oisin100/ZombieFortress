package com.zombiefortress.server.Command;

import com.zombiefortress.server.Server;

public abstract class Command {
	
	private String name;
	protected String description = "No Description";
	protected boolean isDefault = true;
	private String[] alias;
	private String[] data;
	
	public Command(String name, String[] data){
		this.name = name;
		this.data = data;
	}
	
	public void checkAllowed(Sender sender, String[] data){
		if(Server.getConfig().isPermissionsEnabled()){
			//TODO
		}
		else{
			if(!isDefault){
				if(sender.isAdmin()){
					runCommand(sender, data);
				}
				else{
					sender.sendMessage("You do not have permission for this command");
				}
				return;
			}
			runCommand(sender, data);
		}
	}
	
	public abstract void runCommand(Sender sender, String[] data);
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String[] getAlias(){
		return alias;
	}
}
