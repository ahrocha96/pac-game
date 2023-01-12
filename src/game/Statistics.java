package game;

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
}
