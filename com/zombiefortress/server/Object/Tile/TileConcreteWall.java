package com.zombiefortress.server.Object.Tile;

public class TileConcreteWall extends TileObject{

	public TileConcreteWall(int X, int Y) {
		super(X, Y, 3);
		this.isSolid = true;
	}

}
