package com.zombiefortress.server.Object;

import com.zombiefortress.server.Maths;
import com.zombiefortress.server.Player;
import com.zombiefortress.server.Server;

public class Zombie extends BaseObject{
	
	private int health;
	private int strenght;

	public Zombie(int X, int Y) {
		super(X, Y, 100);
		health = 100;
		strenght = 5;
	}
	
	@Override
	public void update(){
		
		look();
		chase();
		attack();
	}

	private void attack() {
		// TODO
		
	}

	private void chase() {
		// TODO
		
	}

	private void look() {
		//TODO
		
	}
}
