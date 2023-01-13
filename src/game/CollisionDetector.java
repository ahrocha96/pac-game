package game;

import gameobject.GameCharacter;
import gameobject.GameObject;
import gameobject.Wall;

public class CollisionDetector {
	
	private static Board board;
	
	public static void checkCollisions(Board b) {
    	board = b;
		
		checkBoardBounds();
    	checkGameObjectWallCollision(board.player);
    	checkGameObjectWallCollision(board.ghost);
    	checkPlayerGhostCollision();
    	checkPlayerPointCollision();	
    }
	
    public static void checkBoardBounds() {
    	if (board.player.getX_position() > 540) {
    		board.player.setX_position(0);
    	}
    	else if (board.player.getX_position() < 0) {
    		board.player.setX_position(540);
    	}
    	else if (board.player.getY_position() > 580) {
    		board.player.setY_position(0);
    	}
    	else if (board.player.getY_position() < 0) {
    		board.player.setY_position(580);
    	}
    }
	
	public static void checkPlayerPointCollision() {
		for(int i = 0; i < board.points.size(); i++) {
			if (board.player.getHitbox().intersects(board.points.get(i).getHitbox())) {
				board.points.remove(i);
				board.gameStats.score++;
			}
		}
	}
	
	public static void checkPlayerGhostCollision() {
		if(board.player.getHitbox().intersects(board.ghost.getHitbox())) {
			board.player.stopMoving();
			board.ghost.stopMoving();
			board.gameStats.livesLeft--;
			board.resetCharacters();
		}
	}
	    
	public static void checkGameObjectWallCollision(GameCharacter obj) {
		for(Wall values : board.maze.values()) {
			if (obj.getFutureHitbox(obj.getX_Direction(), obj.getY_Direction()).intersects(values.getHitbox())) {
				obj.stopMoving();
			}
		}	
	}
	
	/*This is distinct from checkGameObjectWallCollision to fulfill requested direction changes by the player. Don't use for movement in current direction*/
	public static boolean checkFutureGameObjectWallCollision(int x_directionToCheck, int y_directionToCheck, GameObject obj) {
		boolean collision = false;
		
		for(Wall values : board.maze.values()) {
			if (obj.getFutureHitbox(x_directionToCheck, y_directionToCheck).intersects(values.getHitbox())) {
				collision = true;
			}
		}
		return collision;
	}
}
