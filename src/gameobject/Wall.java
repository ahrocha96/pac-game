package gameobject;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Wall extends GameObject {
	
	public Wall(int starting_x_pos, int starting_y_pos){
		super(starting_x_pos, starting_y_pos);
		icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Wall.png");
		name = "Wall";
		x_direction = 0;
		y_direction = 0;
		loadImage();
		hitbox = new Rectangle(starting_x_pos, starting_y_pos, width, height);
	}
	
}
