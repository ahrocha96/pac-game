package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import gameobject.Wall;

public class DrawGame {
	
	private static Color backgroundColor = Color.black;
	private static Color borderAndFontColor = new Color(233, 214, 115);
	
	public static void draw(Graphics g, Board board) {
		Graphics2D g2d = (Graphics2D) g;
        
        renderBoard(g2d, board);
		board.gameStats.renderScore(g2d, board.height);
		
		for (int i = 0; i < board.points.size(); i++) {
	        	g2d.drawImage(board.points.get(i).getImage(), board.points.get(i).getX_position(), 
	        			board.points.get(i).getY_position(), board);
		}
		for (Wall values : board.maze.values() ) {
	        	g2d.drawImage(values.getImage(), values.getX_position(), 
	            		values.getY_position(), board);
		} 
        g2d.drawImage(board.player.getImage(), board.player.getX_position(), 
        		board.player.getY_position(), board);
        
        g2d.drawImage(board.ghost.getImage(), board.ghost.getX_position(), 
        		board.ghost.getY_position(), board);    
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

}
