package gameobject;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Wall extends GameObject {
	
	public Wall(int starting_x_pos, int starting_y_pos){
		super(starting_x_pos, starting_y_pos);
		icon = new ImageIcon(getClass().getResource("/assets/Wall.png"));
		name = "Wall";
		loadImage();
		hitbox = new Rectangle(starting_x_pos, starting_y_pos, width, height);
	}
	
}
