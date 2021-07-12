package game;

import java.awt.Rectangle;

public class Tile {
		
	private int width;
	private int height;
	private int x_position;
	private int y_position;
		
	private Rectangle hitbox;
		
	public Tile (int x_position, int y_position) {
		this.x_position = x_position;
		this.y_position = y_position;
		width = 20;
		height = 20;
		hitbox = new Rectangle(x_position*20, y_position*20, width, height);
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public Rectangle updateHitbox(int x, int y) {
		return hitbox = new Rectangle(x, y, width, height);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX_position() {
		return x_position;
	}

	public void setX_position(int x_position) {
		this.x_position = x_position;
	}

	public int getY_position() {
		return y_position;
	}

	public void setY_position(int y_position) {
		this.y_position = y_position;
	}
}
