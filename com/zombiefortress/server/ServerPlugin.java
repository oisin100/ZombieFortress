package com.zombiefortress.server;

public class ServerPlugin {
	
	private String name;
	private String author;
	private String version;
	
	private boolean isEnabled = true;
	
	public ServerPlugin(String name, String author, String version){
		this.name = name;
		this.author = author;
		this.version = version;
	}
	
	public boolean isEnabled(){
		return isEnabled;
	}
	
	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getVersion(){
		return version;
	}
	
	public void onEnable(){};
	public void onDisable(){};
}
