package com.zombiefortress.server.Object;

import com.zombiefortress.server.Maths;
import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class EntityZombie extends BaseObject{
	
	private int health;
	private int strenght;
	private int speed;
	private int uid;
	
	private Player target;
	private int attackcooldown;
	private long lastattack;
	private boolean canAttack;

	public EntityZombie(int X, int Y) {
		super(X, Y, 100);
		isSolid = true;
		health = 100;
		speed = 1;
		strenght = 20;
		attackcooldown = 5;
		lastattack = System.currentTimeMillis();
		Server.getWorld().addZombieCount();
		uid = Server.getWorld().getZombieCount();
	}
	
	@Override
	public void update(){
		
		look();
		if(target != null){
			chase();
			attack();
		}
		
		if(System.currentTimeMillis() - lastattack >= attackcooldown*1000)
			canAttack = true;
	}

	private void attack() {
		if(canAttack){
			if(Maths.getDistance(this.posX, this.posY, target.getPosX(), target.getPosY()) <= 32){
				target.setHealth(target.getHealth() - strenght);
				canAttack = false;
				lastattack = System.currentTimeMillis();
				Server.sendPacket("health", String.valueOf(target.getHealth()), target);
			}
		}
		
	}

	private void chase() {
		
		int lastPosX = posX;
		int lastPosY = posY;

		int speedX = target.getPosX() - this.posX;
		int speedY = target.getPosY() - this.posY;
		
		if(speedX > speed)
			speedX = speed;
		if(speedX < -speed)
			speedX = -speed;
		
		if(speedY > speed)
			speedY = speed;
		if(speedY < -speed)
			speedY = -speed;
		
		posX = posX+speedX;
		posY = posY+speedY;
		
		if(lastPosX != posX || lastPosY != posY)
		new Thread(){
			public void run(){
				Server.sendPacket("mz" , uid  + "," + posX + "," + posY, target);
			}
		}.start();
	}

	private void look() {
		if(target == null){
			float distance = 1000;
			for(Player p : Server.getPlayers()){
				if(Maths.getDistance(this.posX, this.posY, p.getPosX(), p.getPosY()) <= distance){
					target = p;
				}
			}
		}
		else{
			for(Player p : Server.getPlayers()){
				if(Maths.getDistance(this.posX, this.posY, p.getPosX(), p.getPosY()) <= 256){
					target = p;
				}
			}
		}
	}
	
	public int getUID(){
		return uid;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getStrenght(){
		return strenght;
	}
	
	public void setStrength(int strength){
		this.strenght = strength;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
}
