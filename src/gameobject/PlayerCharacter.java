package gameobject;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class PlayerCharacter extends GameObject {
	
	//dx = direction
	
	String iconFileLocation;
	String iconFileName;
	public String playerDirection;
	
	public int currentAnimationFrame;
	private int animationDelay = 0;
	
	
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
	
	private void updateIconFile(String location, String fileName, int frameNumber) {
		iconFileName =  location + fileName + Integer.toString(frameNumber) + ".png";
	}
	
	
	public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
        	
            x_direction = -2;
            y_direction = 0;
            playerDirection = "Player_Left";
        }

        if (key == KeyEvent.VK_D) {
        	
            x_direction = 2;
            y_direction = 0;
            playerDirection = "Player_Right";
        }

        if (key == KeyEvent.VK_W) {
        	
            y_direction = -2;
            x_direction = 0;
            playerDirection = "Player_Up";
        }

        if (key == KeyEvent.VK_S) {
        	
            y_direction = 2;
            x_direction = 0;
            playerDirection = "Player_Down";
        }
    }
}
