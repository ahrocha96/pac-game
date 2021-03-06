package game;

public class CollisionDetector {
	
	Board board;
	
	public CollisionDetector(Board b) {
		board = b;
	}
	
    public void checkBoardBounds() {
    	if (board.player.getX_position() > 540) {
    		board.player.setX_position(540);
    	}
    	else if (board.player.getX_position() < 0) {
    		board.player.setX_position(0);
    	}
    	else if (board.player.getY_position() > 540) {
    		board.player.setY_position(540);
    	}
    	else if (board.player.getY_position() < 0) {
    		board.player.setY_position(0);
    	}
    }
	
	public void checkPlayerPointCollision() {
		for(int i = 0; i < board.points.size(); i++) {
			if (board.player.getHitbox().intersects(board.points.get(i).getHitbox())) {
				board.points.remove(i);
				board.gameStats.score++;
			}
		}
	}
	    
	public void checkPlayerWallCollision() {
		for(int i = 0; i < board.maze.size(); i++) {
			if (board.player.getFutureHitbox(board.player.getX_Direction(), board.player.getY_Direction()).intersects(board.maze.get(i).getHitbox())) {
				board.stopPlayerMovement();
			}
		}
	}
	
	/*This is distinct from checkPlayerWallCollision to fulfill requested direction changes by the player. Don't use for movement in current direction*/
	public boolean  checkFuturePlayerWallCollision(int x_directionToCheck, int y_directionToCheck) {
		boolean collision = false;
		for(int i = 0; i < board.maze.size(); i++) {
			if (board.player.getFutureHitbox(x_directionToCheck, y_directionToCheck).intersects(board.maze.get(i).getHitbox())) {
				collision = true;
			}
		}
		return collision;
	}

}
