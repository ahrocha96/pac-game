package gameobject;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemy extends GameObject {

	String iconFileLocation;
	String iconFileName;
	public String currentDirection;
	public boolean canChangeDirection = false;
		
	public int currentAnimationFrame;
	private int animationDelay = 0;
		
	public Enemy(int starting_x_position, int starting_y_position) {
		super(starting_x_position, starting_y_position);
		icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\Player_Left0.png");
		iconFileLocation = "C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\";
		currentDirection = "Player_Right";
		objectDirection = "Right";
		name = "Enemy";
		currentAnimationFrame = 0;
		
		loadImage();		
		hitbox = new Rectangle(x_position, y_position, width, height);

	}
	
	
	
	public void move() {	
		
		x_position += x_direction;
		y_position += y_direction;

		updateHitbox(x_position, y_position);
		        
		if(moving()) {
			updateAnimation();
		}	     
    }
	
	public void stopMoving() {
		setX_direction(0);
		setY_direction(0);
		updateAnimation();
	}
	
	
	@Override
	public Rectangle updateHitbox(int x_pos, int y_pos) {
		return hitbox = new Rectangle(x_pos, y_pos, width-6, height-3);
	}
	
	private  void updateAnimation(){
		 
		animationDelay++;

		 if (animationDelay == 3) {
			 if(currentAnimationFrame == 4) {
				 currentAnimationFrame = 0;
			 }
			 else {
				 currentAnimationFrame++;
			 }
			 animationDelay= 0;
		 }
        
        //updateIconFile(iconFileLocation, currentDirection, currentAnimationFrame);
        //icon = new ImageIcon(iconFileName);
        loadImage();
	}

}
