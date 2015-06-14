package com.zombiefortress.server;

public class ServerPlugin {
	
	private String name;
	private String author;
	private String version;
	
	public ServerPlugin(String name, String author, String version){
		this.name = name;
		this.author = author;
		this.version = version;
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
