package gameobject;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class PlayerCharacter extends GameCharacter {
		
	public int currentAnimationFrame;
	private int animationDelay = 0;
	
	String iconFileLocation;
	String iconFileName;
	
	public PlayerCharacter(int starting_x_position, int starting_y_position) {
		super(starting_x_position, starting_y_position);
		icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\Player_Right0.png");
		iconFileLocation = "C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\";
		objectDirection = "Right";
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
	
	public void stopMoving() {
		setX_direction(0);
		setY_direction(0);
		updateAnimation();
	}
	
	private void updateAnimation(){
		 
		animationDelay++;

		 if (animationDelay == 5) {
			 if(currentAnimationFrame == 4) {
				 currentAnimationFrame = 0;
			 }
			 else {
				 currentAnimationFrame++;
			 }
			 animationDelay= 0;
		 }
        
        updateIconFile(iconFileLocation, objectDirection, currentAnimationFrame);
        icon = new ImageIcon(iconFileName);
        loadImage();
	}
		

	
	@Override
	public Rectangle updateHitbox(int x_pos, int y_pos) {
		return hitbox = new Rectangle(x_pos, y_pos, width-6, height-3);
	}
	
	private void updateIconFile(String location, String direction, int frameNumber) {
		iconFileName =  location + "Player_" + direction + Integer.toString(frameNumber) + ".png";
	}
	

	public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
        	this.requestDirectionChange("Left");
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
        	this.requestDirectionChange("Right");
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
        	this.requestDirectionChange("Up");
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
        	this.requestDirectionChange("Down");
        }
    }
}
