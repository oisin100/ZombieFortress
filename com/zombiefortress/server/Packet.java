package com.zombiefortress.server;

public class Packet {
	
	private String type;
	protected String[] data;
	

	public Packet(String type, String data) {
		this.type = type;
		this.data = data.trim().split(",");
	}
	
	public String getType(){
		return type;
	}
	
	public String[] getData(){
		return data;
	}

}
