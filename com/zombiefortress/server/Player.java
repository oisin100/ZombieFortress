package com.zombiefortress.server;

import java.net.InetAddress;
import java.util.UUID;

import com.zombiefortress.server.Event.EventPlayerMove;

public class Player {

	private int x;
	private int y;
	
	private InetAddress address;
	private int port;
	private UUID uuid;
	private String name;
	private boolean isOnline;
	
	private int health;
	
	public Player(InetAddress address, int port, String name){
		this.address = address;
		this.port = port;
		this.name = name;
		this.isOnline = true;
		this.uuid = UUID.randomUUID();
		health = 1000;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getHealth(){
		return health;
	}

	public void setHealth(int health){
		this.health = health;
	}
	
	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public void sendMessage(String msg){
		Server.sendPacket("chat",msg,this);
	}
	
	public void teleport(int x2, int y2){
		this.x = x2;
		this.y = y2;
		Server.sendPacket("tp",x2 + "," + y2,this);
	}
	
	public int getPosX(){
		return this.x;
	}
	
	public int getPosY(){
		return this.y;
	}
	
	public void moveX(int x){
		new EventPlayerMove(this,  x, this.y).callEvent();
	}
	
	public void moveY(int y){
		new EventPlayerMove(this,  this.x, y).callEvent();
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isOnline(){
		return isOnline;
	}
	
	public void kickPlayer(String reason){
		Server.sendPacket("kick", reason, this);
		Server.sendMessage(name + " Was kicked for " + reason);
		this.isOnline = false;
	}
}
