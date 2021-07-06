package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class IntroScreen {
	
	private static Color backgroundColor = new Color(125, 78, 102);
	private static Color borderAndFontColor = new Color(233, 214, 115);
	
	public static void renderIntroScreen(Graphics2D g2d, int width, int height) {		
		CreateBackground(g2d, width, height);
		CreateBorder(g2d, width, height);
		CreateInstructions(g2d, width, height);
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
		String instructions = "Press 'Spacebar' To Start.";
		Font font = new Font("Futura", Font.BOLD, 18);

		g2d.setFont(font);
		g2d.drawString(instructions, (width / 2)-120, height / 2);
	}
}
