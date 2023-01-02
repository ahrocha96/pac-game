package gameobject;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public abstract class GameObject {

	protected String name;

	protected int x_position;
	protected int y_position;
	protected int x_direction;
	protected int y_direction;
	public int requested_dx = 0;
	public int requested_dy = 0;
	public int requestCounter = 0;
	public boolean changingDirection = false;
	public String requestedDirection = "";
	public String objectDirection;

	protected int tile_x;
	protected int tile_y;

	protected int width;
	protected int height;

	protected Rectangle hitbox;

	protected Image image;
	protected ImageIcon icon;

	public GameObject(int x_position, int y_position) {
		this.x_position = x_position;
		this.y_position = y_position;
		tile_x = x_position / 20;
		tile_y = y_position / 20;
	}

	protected void loadImage() {
		image = icon.getImage();

		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	public int getX_Direction() {
		return x_direction;
	}

	public int getY_Direction() {
		return y_direction;
	}

	public void setX_direction(int x_direction) {
		this.x_direction = x_direction;
	}

	public void setY_direction(int y_direction) {
		this.y_direction = y_direction;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

	public Image getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public int getTile_x() {
		return tile_x;
	}

	public void setTile_x(int tile_x) {
		this.tile_x = tile_x;
	}

	public int getTile_y() {
		return tile_y;
	}

	public void setTile_y(int tile_y) {
		this.tile_y = tile_y;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public Rectangle updateHitbox(int x, int y) {
		return hitbox = new Rectangle(x, y, width, height);
	}

	public abstract void stopMoving();
	
	public boolean moving() {
		return x_direction != 0 || y_direction != 0;
	}
	
	public void setObjectDirection(String objectDirection) {
		this.objectDirection = objectDirection;
	}
	
	//returns the hit box of the player after the next desired move. 
	//This is used to predict collisions and allow the caller to behave accordingly 

	public Rectangle getFutureHitbox(int requested_x_direction, int requested_y_direction) {
	    return new Rectangle(x_position+requested_x_direction, y_position + requested_y_direction, width, height);
	}
	
	public void requestDirectionChange(String requestedDirection) {

		int requested_dx = 0;
		int requested_dy = 0;

		switch (requestedDirection) {
		case "Up":
			requested_dx = 0;
			requested_dy = -2;
			break;
		case "Down":
			requested_dx = 0;
			requested_dy = 2;
			break;

		case "Right":
			requested_dx = 2;
			requested_dy = 0;
			break;

		case "Left":
			requested_dx = -2;
			requested_dy = 0;
			break;
		}

		this.requested_dx = requested_dx;
		this.requested_dy = requested_dy;
		this.requestedDirection = requestedDirection;

		requestCounter = 30;
		changingDirection = true;
	}

}
