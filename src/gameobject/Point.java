package gameobject;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Point extends GameObject {
	
	public Point(int starting_x_pos, int starting_y_pos){
		super(starting_x_pos, starting_y_pos);
		icon = new ImageIcon(getClass().getResource("/assets/Point.png"));
		name = "Point";
		x_direction = 0;
		y_direction = 0;
		loadImage();
		hitbox = new Rectangle(starting_x_pos, starting_y_pos, width-11, height-11);
	}
}
