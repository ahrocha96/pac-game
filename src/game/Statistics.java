package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Statistics {
	
	int score;
	int livesLeft;
	
	public Statistics(){
		score = 0;
		livesLeft = 3;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getLivesLeft() {
		return livesLeft;
	}

	public void setLivesLeft(int livesLeft) {
		this.livesLeft = livesLeft;
	}
	public void renderScore(Graphics2D g, int height) {

        String scoreMessage;
        
        scoreMessage = "Score: " + score;        
        Font scoreFont = new Font("Futura", Font.BOLD, 16);

        g.setFont(scoreFont);
        g.setColor(new Color(62, 123, 99));
        g.drawString(scoreMessage, 5, height+17);
    }
}
