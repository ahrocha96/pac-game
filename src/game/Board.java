package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import gameobject.Door;
import gameobject.Enemy;
import gameobject.GameObject;
import gameobject.PlayerCharacter;
import gameobject.Point;
import gameobject.Wall;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener{
		
	boolean gameStarted = false;
	boolean gameOver = false;
	boolean levelComplete = false;
	
	public int height;
	public int width;
	
    Timer timer;
    PlayerCharacter player;
    Enemy ghost;
    Enemy Blinky;
    Enemy Inky;
    Enemy Pinky;
    Enemy Clyde;
    List<Point> points;
    List<Door> doors;
    HashMap<String, Wall> maze;
    Statistics  gameStats;
    
    private final int DELAY = 4;
	
    //These are used for placement of the GameObjects, and some collision detection
	Tile[][] tiles;
	
	public Board(int height, int width) {
		this.height = height-60;
		this.width = width-15;
        initializeBoard();
	}
		
	public void initializeBoard() {
		addKeyListener(new GameKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);

        gameStats = new Statistics();
        
        tiles = new Tile[height/20][width/20];
        points = new ArrayList<Point>();
        doors = new ArrayList<Door>();
        maze = new HashMap<String, Wall>();
        placeGameObjects();

        timer = new Timer(DELAY, this);
	}
	
	public void resetCharacters() {
		player.setX_position(player.getX_position_start());
		player.setY_position(player.getY_position_start());
		ghost.setX_position(ghost.getX_position_start());
		ghost.setY_position(ghost.getY_position_start());		
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!gameStarted) {
        	TransitionScreen.renderIntroScreen(g, width, height);
        }
        else {
            DrawGame.draw(g, this);  	
        }
        if(gameOver) {
        	TransitionScreen.renderGameOverScreen(g, width, height);
        }
        if (levelComplete) {
        	TransitionScreen.renderVictoryScreen(g, width, height);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    
	@Override
	public void actionPerformed(ActionEvent ev) {
		
		if(gameStats.livesLeft == 0) {
			gameOver = true;
			timer.stop();
		}
		else if (points.isEmpty()) {
			levelComplete = true;
			timer.stop();
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(!ghost.exitedBox) {
			determineGhostPath(doors.get(0).getTile_x(), doors.get(0).getTile_y()-1);
			CollisionDetector.checkGhostExit(this);
		}
		else {
			determineGhostPath(player.getTile_x(), player.getTile_y());
		}
		updateGameObjectTile(player);
		
		//OLD TILE POSITION (after the first loop)
		int gx = ghost.getTile_x();
		int gy = ghost.getTile_y();
		
		updateGameObjectTile(ghost);
		
		//NEW TILE POSITION
		int gx2 = ghost.getTile_x();
		int gy2 = ghost.getTile_y();
		
		if(gx != gx2 || gy != gy2) {
			ghost.canChangeDirection = true;
		}
		
		//always check collisions BEFORE moving the player
		CollisionDetector.checkCollisions(this);
		
		if(player.changingDirection) {
			processGameObjectDirectionChange(player);
		}

		if(ghost.changingDirection) {
			processGameObjectDirectionChange(ghost);
		}
		
		player.move();
    
    	ghost.move();
		repaint();
	}
	
	private void processGameObjectDirectionChange(GameObject obj){
		 boolean collisionInNewDirection = false;

		 obj.requestCounter--;
		 if(obj.requestCounter <= 0) {
			 obj.changingDirection = false;
		 }
		 
		 collisionInNewDirection = CollisionDetector.checkFutureGameObjectWallCollision(obj.requested_dx, obj.requested_dy, obj, this)
				 				|| CollisionDetector.checkFutureGameObjectDoorCollision(obj.requested_dx, obj.requested_dy, obj, this);
		 
		 if (collisionInNewDirection == false) {
			 obj.setX_direction(obj.requested_dx);
			 obj.setY_direction(obj.requested_dy);
			 obj.setObjectDirection(obj.requestedDirection); 
			 obj.changingDirection = false;
		 }
	}
	
    private void determineGhostPath(int targetX, int targetY) {
    	
    	int gx = ghost.getTile_x();
    	int gy = ghost.getTile_y();
    	    	
    	if(ghost.canChangeDirection || !ghost.moving()) {
    	    
    		if(targetX > gx && targetY < gy) {
    			checkGhostDirectionOrder("Up", "Right", "Down", "Left");
        	}
        	else if(targetX < gx && targetY < gy) {
    			checkGhostDirectionOrder("Up", "Left", "Down", "Right");	
    		}
        	else if(targetX > gx && targetY == gy) {
    			checkGhostDirectionOrder("Right", "Up", "Left", "Down");
    		}
        	else if(targetX < gx && targetY == gy) {
    			checkGhostDirectionOrder("Left", "Down", "Right", "Up");
    		}
        	else if(targetX < gx && targetY > gy) {
    			checkGhostDirectionOrder("Down", "Left", "Right", "Up");
    		}
        	else if(targetX > gx && targetY > gy) {
    			checkGhostDirectionOrder("Down", "Right", "Left", "Up");
    		}
        	else if(targetX == gx && targetY < gy) {
    			checkGhostDirectionOrder("Up", "Right", "Down", "Left");
    		}
        	else if(targetX == gx && targetY > gy) {
    			checkGhostDirectionOrder("Down", "Left", "Up", "Right");
    		}	
    		ghost.canChangeDirection = false;
    	}
    }
    
    public void checkGhostDirectionOrder(String one, String two, String three, String four) {
    	if (ghostCanMoveDirection(one)) {
			ghost.requestDirectionChange(one);

    	}
    	else if(ghostCanMoveDirection(two)) {
			ghost.requestDirectionChange(two);

    	}
    	else if(ghostCanMoveDirection(three)) {
			ghost.requestDirectionChange(three);

    	}
    	else if(ghostCanMoveDirection(four)) {
			ghost.requestDirectionChange(four);
    	}
    }
    
    public boolean ghostCanMoveDirection(String direction) {
    	int tilex = 0;
    	int tiley = 0;
    	String opposite = "";
    	
    	switch(direction) {
    	case "Up":
			tilex = ghost.getTile_x();
			tiley = ghost.getTile_y()-1;
			opposite = "Down";
			break;
		case "Down":
			tilex = ghost.getTile_x();
			tiley = ghost.getTile_y()+1;
			opposite = "Up";
			break;

		case "Right":
			tilex = ghost.getTile_x()+1;
			tiley = ghost.getTile_y();
			opposite = "Left";
			break;

		case "Left":
			tilex = ghost.getTile_x()-1;
			tiley = ghost.getTile_y();
			opposite = "Right";
			break;
		}
    	
    	String mazeKey = Integer.toString(tilex) + "-" + Integer.toString(tiley);
   
    	return (!maze.containsKey(mazeKey) && !ghost.objectDirection.equals(opposite))
    			|| !ghost.moving();
    }
    
    private void updateGameObjectTile(GameObject obj){
    	for(int i = 0; i < tiles.length; i++) {
    		for (int j = 0; j < tiles[i].length; j++) {
        		if (obj.getHitbox().intersects(tiles[i][j].getHitbox())) {
        			obj.setTile_x(j);
        			obj.setTile_y(i);
        		}    		
    		}
    	}
    }
  
	private void placeGameObjects() {
		
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				
				tiles[i][j] = new Tile(j, i);
				
				if(levelData[i][j] == 'w') {
					Wall w = new Wall(j*20, i*20);
					maze.put(Integer.toString(j) + "-" + Integer.toString(i), w);
				}
				if(levelData[i][j] == 'W') {
					Wall w = new Wall(j*20, i*20);
					w.setIcon(new ImageIcon(getClass().getResource("/assets/InvisibleWall.png")));
					maze.put(Integer.toString(j) + "-" + Integer.toString(i), w);
				}
				if(levelData[i][j] == 'd') {
					Door d = new Door(j*20, i*20);
					doors.add(d);
				}
				else if (levelData[i][j] == 'c'){
					player = new PlayerCharacter(j*20, i*20);
				}
				else if(levelData[i][j] == 'p') {
					Point p = new Point(j*20, i*20);
					points.add(p);
				}
				else if(levelData[i][j] == 'g') {
					ghost = new Enemy(j*20, i*20, "Clyde");
				}
				else if(levelData[i][j] == 'B') {
					Blinky = new Enemy(j*20, i*20, "Blinky");
				}	
				else if(levelData[i][j] == 'P') {
					Pinky = new Enemy(j*20, i*20, "Pinky");
				}	
				else if(levelData[i][j] == 'I') {
					Inky = new Enemy(j*20, i*20, "Inky");
				}	
				/*else if(levelData[i][j] == 'C') {
					Clyde = new Enemy(j*20, i*20, "Clyde");
				}*/
			}
		}
	}
	//w = wall
	//p = point
	//e = empty
	//B = Blinky, P = Pinky, I = Inky, C = Clyde
	//c = Player Character
	//d = door
	
	private char[][] levelData = {{'w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w'},
			{'w','p','p','p','p','p','p','p','p','p','p','p','p','w','w','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','p','w'},
			{'w','p','p','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','p','p','w'},
			{'w','w','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','e','e','e','e','e','e','e','e','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','d','d','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','W','W','e','e','W','W','w','e','w','w','p','w','w','w','w','w','w'},
			{'e','e','e','e','e','e','p','e','e','e','w','e','I','B','g','P','e','w','e','e','e','p','e','e','e','e','e','e'},
			{'w','w','w','w','w','w','p','w','w','e','w','W','W','W','W','W','W','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','e','e','e','e','e','e','e','e','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','p','p','p','p','p','p','p','p','p','p','p','p','w','w','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','p','p','w','w','p','p','p','p','p','c','p','p','p','p','p','p','p','p','p','p','w','w','p','p','p','w'},
			{'w','w','w','p','w','w','p','w','w','p','w','w','w','w','w','w','w','w','p','w','w','p','w','w','p','w','w','w'},
			{'w','p','p','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','w','w','p','w'},
			{'w','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w'}};

    class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(gameStarted) {
            	player.keyPressed(e); 
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            	gameStarted = true;
            	timer.start();
            }
        }
    }
}
