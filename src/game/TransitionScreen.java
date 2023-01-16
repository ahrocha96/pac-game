package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TransitionScreen {
	
	private static Color backgroundColor = new Color(125, 78, 102);
	private static Color borderAndFontColor = new Color(233, 214, 115);
	
	public static void renderIntroScreen(Graphics g, int width, int height) {		
		Graphics2D g2d = (Graphics2D) g;
		
		CreateBackground(g2d, width, height);
		CreateBorder(g2d, width, height);
		CreateInstructions(g2d, width, height);
	}
	public static void renderGameOverScreen(Graphics g, int width, int height) {		
		Graphics2D g2d = (Graphics2D) g;
		
		CreateBackground(g2d, width, height);
		CreateBorder(g2d, width, height);
		CreateGameOver(g2d, width, height);
	}
	private static void CreateBackground(Graphics2D g2d, int width, int height){
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, width, height);
	}
	private static void CreateBorder(Graphics2D g2d, int width, int height) {
		g2d.setColor(borderAndFontColor);
		g2d.drawRect(0, 0, width, height);	
	}
	private static void CreateInstructions(Graphics2D g2d, int width, int height) {
		String instructions = "Press 'Spacebar' To Start ";
		Font font = new Font("Futura", Font.BOLD, 28);

		g2d.setFont(font);
		g2d.drawString(instructions, (width / 2)-165, height / 2);
	}
	private static void CreateGameOver(Graphics2D g2d, int width, int height) {
		String gameOver = "Game Over";
		Font font = new Font("Futura", Font.BOLD, 28);

		g2d.setFont(font);
		g2d.drawString(gameOver, (width / 2)-80, height / 2);
	}
}
