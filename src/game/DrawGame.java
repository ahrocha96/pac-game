package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import gameobject.Wall;

public class DrawGame {
	
	private static Color backgroundColor = Color.black;
	private static Color borderAndFontColor = new Color(233, 214, 115);
	
	public static void draw(Graphics g, Board board) {
		Graphics2D g2d = (Graphics2D) g;
        
        renderBoard(g2d, board);
		renderScore(g2d, board);
		renderLives(g2d, board);
		
		for (int i = 0; i < board.points.size(); i++) {
	        	g2d.drawImage(board.points.get(i).getImage(), board.points.get(i).getX_position(), 
	        			board.points.get(i).getY_position(), board);
		}
		for (int i = 0; i < board.doors.size(); i++) {
        	g2d.drawImage(board.doors.get(i).getImage(), board.doors.get(i).getX_position(), 
        			board.doors.get(i).getY_position(), board);
		}
		for (Wall values : board.maze.values() ) {
	        	g2d.drawImage(values.getImage(), values.getX_position(), 
	            		values.getY_position(), board);
		} 
        g2d.drawImage(board.player.getImage(), board.player.getX_position(), 
        		board.player.getY_position(), board);
        
        g2d.drawImage(board.ghost.getImage(), board.ghost.getX_position(), 
        		board.ghost.getY_position(), board); 
     /*   
        g2d.drawImage(board.Blinky.getImage(), board.Blinky.getX_position(), 
        		board.Blinky.getY_position(), board);
        g2d.drawImage(board.Inky.getImage(), board.Inky.getX_position(), 
        		board.Inky.getY_position(), board); 
        g2d.drawImage(board.Pinky.getImage(), board.Pinky.getX_position(), 
        		board.Pinky.getY_position(), board); 
        g2d.drawImage(board.Clyde.getImage(), board.Clyde.getX_position(), 
        		board.Clyde.getY_position(), board); */
	}
	
	public static void renderBoard(Graphics2D g2d, Board board) {	
		CreateBackground(g2d, board.width, board.height);
		CreateBorder(g2d, board.width, board.height);
	}
	
	private static void CreateBackground(Graphics2D g2d, int width, int height){
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, width, height);
	}
	private static void CreateBorder(Graphics2D g2d, int width, int height) {
		g2d.setColor(borderAndFontColor);
		g2d.drawRect(0, 0, width, height);
	}
	
	public static void renderScore(Graphics2D g, Board board) {
        String scoreMessage;
        
        scoreMessage = "Score: " + board.gameStats.score;        
        Font scoreFont = new Font("Futura", Font.BOLD, 16);

        g.setFont(scoreFont);
        g.setColor(new Color(62, 123, 99));
        g.drawString(scoreMessage, 5, board.height+17);
    }
	
	public static void renderLives(Graphics2D g, Board board) {
		Image image;
		ImageIcon icon = new ImageIcon("C:\\Users\\ahroc\\Desktop\\GameAssets\\Player\\Player_Right0.png");
		image = icon.getImage();
		
		String lives = "Lives:";
		Font scoreFont = new Font("Futura", Font.BOLD, 16);
		int xPosition = 150; 
		int livesLeft = board.gameStats.getLivesLeft();

        g.setFont(scoreFont);
        g.setColor(new Color(62, 123, 99));
        g.drawString(lives, 100, board.height+17);
       
        while(livesLeft>0) {
        	g.drawImage(image, xPosition, board.height+2, board);
        	xPosition+=20;
        	livesLeft--;
        }
		

	}

}
