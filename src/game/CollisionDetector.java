package game;

import gameobject.GameCharacter;
import gameobject.GameObject;
import gameobject.Wall;

public class CollisionDetector {
	
	public static void checkCollisions(Board board) {
    	
		checkBoardBounds(board.player);
		checkBoardBounds(board.ghost);
    	checkGameObjectWallCollision(board, board.player);
    	checkGameObjectWallCollision(board, board.ghost);
    	checkGameObjectDoorCollision(board, board.player);
    	checkPlayerGhostCollision(board);
    	checkPlayerPointCollision(board);	
    }
    
    public static void checkBoardBounds(GameCharacter c) {
    	if (c.getX_position() > 540) {
    		c.setX_position(0);
    	}
    	else if (c.getX_position() < 0) {
    		c.setX_position(540);
    	}
    	else if (c.getY_position() > 580) {
    		c.setY_position(0);
    	}
    	else if (c.getY_position() < 0) {
    		c.setY_position(580);
    	}
    }
	
	public static void checkPlayerPointCollision(Board board) {
		for(int i = 0; i < board.points.size(); i++) {
			if (board.player.getHitbox().intersects(board.points.get(i).getHitbox())) {
				board.points.remove(i);
				board.gameStats.score++;
			}
		}
	}
	public static void checkGhostExit(Board board) {
		if (board.ghost.getHitbox().intersects(board.tiles[board.doors.get(0).getTile_y()-1][board.doors.get(0).getTile_x()].getHitbox())) {
				board.ghost.exitedBox = true;
		}
	}
	
	public static void checkPlayerGhostCollision(Board board) {
		if(board.player.getHitbox().intersects(board.ghost.getHitbox())) {
			board.player.stopMoving();
			board.ghost.stopMoving();
			board.ghost.exitedBox = false;
			board.gameStats.livesLeft--;
			board.resetCharacters();
		}
	}
	    
	public static void checkGameObjectWallCollision(Board board, GameCharacter obj) {
		for(Wall values : board.maze.values()) {
			if (obj.getFutureHitbox(obj.getX_Direction(), obj.getY_Direction()).intersects(values.getHitbox())) {
				obj.stopMoving();
			}
		}	
	}
	
	public static void checkGameObjectDoorCollision(Board board, GameCharacter obj) {
		for(int i = 0; i < board.doors.size(); i++) {
			if (obj.getY_position() < board.doors.get(i).getY_position() - 20
			&&  obj.getFutureHitbox(obj.getX_Direction(), obj.getY_Direction()).intersects(board.doors.get(i).getHitbox())) {
				obj.stopMoving();
			}
		}
	}
	
	/*This is distinct from checkGameObjectWallCollision to fulfill requested direction changes by the player. Don't use for movement in current direction*/
	public static boolean checkFutureGameObjectWallCollision(int x_directionToCheck, int y_directionToCheck, GameObject obj, Board board) {
		for(Wall values : board.maze.values()) {
			if (obj.getFutureHitbox(x_directionToCheck, y_directionToCheck).intersects(values.getHitbox())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkFutureGameObjectDoorCollision(int x_directionToCheck, int y_directionToCheck, GameObject obj, Board board) {
		for(int i = 0; i < board.doors.size(); i++) {
			if (obj.getFutureHitbox(x_directionToCheck, y_directionToCheck).intersects(board.doors.get(i).getHitbox())) {
				return true;
			}
		}
		return false;
	}
	
}
