package gameobject;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class PlayerCharacter extends GameObject {
	
	// dx/dy = direction
	
	public int requested_dx=0;
	public int requested_dy=0;
	public int requestCounter = 0;
	
	public int currentAnimationFrame;
	private int animationDelay = 0;
	
	public boolean changingDirection = false;
	
	String iconFileLocation;
	String iconFileName;
	String playerDirection;
	public String requestedDirection;
	
	public PlayerCharacter(int starting_x_position, int starting_y_position) {
		super(starting_x_position, starting_y_position);
		icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\Player_Right0.png");
		iconFileLocation = "C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\";
		playerDirection = "Player_Right";
		name = "Player";
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
	
	private boolean moving() {
		return x_direction != 0 || y_direction != 0;
	}
	
	public void stopMoving() {
		setX_direction(0);
		setY_direction(0);
		updateAnimation();
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
        
        updateIconFile(iconFileLocation, playerDirection, currentAnimationFrame);
        icon = new ImageIcon(iconFileName);
        loadImage();
	}
		
	//returns the hit box of the player after the next desired move. 
	//This is used to predict collisions and allow the caller to behave accordingly 
	public Rectangle getFutureHitbox(int requested_x_direction, int requested_y_direction) {
	    return new Rectangle(x_position+requested_x_direction, y_position + requested_y_direction, width, height);
	}
	
	@Override
	public Rectangle updateHitbox(int x_pos, int y_pos) {
		return hitbox = new Rectangle(x_pos, y_pos, width-6, height-3);
	}
	
	private void updateIconFile(String location, String direction, int frameNumber) {
		iconFileName =  location + "Player_" + direction + Integer.toString(frameNumber) + ".png";
	}
	
	private void requestDirectionChange(int requested_dx, int requested_dy, String requestedDirection) {
		this.requested_dx = requested_dx;
    	this.requested_dy = requested_dy;
        this.requestedDirection = requestedDirection;

    	requestCounter = 30;
    	changingDirection = true;
	}
	
	public void setPlayerDirection(String playerDirection) {
		this.playerDirection = playerDirection;
	}
	
	public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
        	requestDirectionChange(-2, 0, "Left");
        }

        if (key == KeyEvent.VK_D) {
        	requestDirectionChange(2, 0, "Right");
        }

        if (key == KeyEvent.VK_W) {
        	requestDirectionChange(0, -2, "Up");
        }

        if (key == KeyEvent.VK_S) {
        	requestDirectionChange(0, 2, "Down");
        }
    }
}
