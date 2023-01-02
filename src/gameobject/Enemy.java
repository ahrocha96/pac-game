package gameobject;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemy extends GameObject {

	String iconFileLocation;
	String iconFileName;
	String currentDirection;
	public String requestedDirection;
	
	public int requested_dx=0;
	public int requested_dy=0;
	public int requestCounter = 0;
	
	public int currentAnimationFrame;
	private int animationDelay = 0;
	
	public boolean changingDirection = false;
	
	public void requestDirectionChange(int requested_dx, int requested_dy, String requestedDirection) {
		this.requested_dx = requested_dx;
    	this.requested_dy = requested_dy;
        this.requestedDirection = requestedDirection;

    	requestCounter = 30;
    	changingDirection = true;
	}
	
	
	public Enemy(int starting_x_position, int starting_y_position) {
		super(starting_x_position, starting_y_position);
		icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\Player_Left0.png");
		iconFileLocation = "C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\";
		currentDirection = "Player_Right";
		name = "Enemy";
		currentAnimationFrame = 0;
		
		loadImage();		
		hitbox = new Rectangle(x_position, y_position, width, height);

	}
	
	public Rectangle getFutureHitbox(int requested_x_direction, int requested_y_direction) {
	    return new Rectangle(x_position+requested_x_direction, y_position + requested_y_direction, width, height);
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
	
	public boolean moving() {
		return x_direction != 0 || y_direction != 0;
	}
	
	@Override
	public Rectangle updateHitbox(int x_pos, int y_pos) {
		return hitbox = new Rectangle(x_pos, y_pos, width-6, height-3);
	}
	
	String enemyDirection = "";
	
	public void setEnemyDirection(String enemyDirection) {
		this.enemyDirection = enemyDirection;
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
