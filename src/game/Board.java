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

import javax.swing.JPanel;
import javax.swing.Timer;

import gameobject.Enemy;
import gameobject.GameObject;
import gameobject.PlayerCharacter;
import gameobject.Point;
import gameobject.Wall;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener{
		
	public int height;
	public int width;
	
    Timer timer;
    PlayerCharacter player;
    Enemy ghost;
    List<Point> points;
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
        maze = new HashMap<String, Wall>();
        placeGameObjects();

        timer = new Timer(DELAY, this);
        timer.start();
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

        DrawGame.draw(g, this);
        
        Toolkit.getDefaultToolkit().sync();
    }

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(gameStats.livesLeft == 0) {
			timer.stop();
		}
		
		updateGameObjectTile(player);
		
		int gx = ghost.getTile_x();
		int gy = ghost.getTile_y();
		
		updateGameObjectTile(ghost);
		
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
    	determineGhostPath();
    	ghost.move();
		repaint();
	}
	
	private void processGameObjectDirectionChange(GameObject obj){
		 boolean collisionInNewDirection = false;

		 obj.requestCounter--;
		 if(obj.requestCounter <= 0) {
			 obj.changingDirection = false;
		 }
		 
		 collisionInNewDirection = CollisionDetector.checkFutureGameObjectWallCollision(obj.requested_dx, obj.requested_dy, obj);
		 
		 if (collisionInNewDirection == false) {
			 obj.setX_direction(obj.requested_dx);
			 obj.setY_direction(obj.requested_dy);
			 obj.setObjectDirection(obj.requestedDirection); 
			 obj.changingDirection = false;
		 }
	}
	
    private void determineGhostPath() {
    	int px = player.getTile_x();
    	int py = player.getTile_y();
    	
    	int gx = ghost.getTile_x();
    	int gy = ghost.getTile_y();
    	    	
    	if(ghost.canChangeDirection || !ghost.moving()) {
    	    
    		if(px > gx && py < gy) {
    			checkGhostDirectionOrder("Up", "Right", "Down", "Left");
        	}
        	else if(px < gx && py < gy) {
    			checkGhostDirectionOrder("Up", "Left", "Down", "Right");	
    		}
        	else if(px > gx && py == gy) {
    			checkGhostDirectionOrder("Right", "Up", "Left", "Down");
    		}
        	else if(px < gx && py == gy) {
    			checkGhostDirectionOrder("Left", "Down", "Right", "Up");
    		}
        	else if(px < gx && py > gy) {
    			checkGhostDirectionOrder("Down", "Left", "Right", "Up");
    		}
        	else if(px > gx && py > gy) {
    			checkGhostDirectionOrder("Down", "Right", "Left", "Up");
    		}
        	else if(px == gx && py < gy) {
    			checkGhostDirectionOrder("Up", "Right", "Down", "Left");
    		}
        	else if(px == gx && py > gy) {
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
   
    	return !maze.containsKey(mazeKey) && !ghost.objectDirection.equals(opposite);
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
				else if (levelData[i][j] == 'c'){
					player = new PlayerCharacter(j*20, i*20);
				}
				else if(levelData[i][j] == 'p') {
					Point p = new Point(j*20, i*20);
					points.add(p);
				}
				else if(levelData[i][j] == 'g') {
					ghost = new Enemy(j*20, i*20);
				}
							
			}
		}
	}
	
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
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','e','e','e','e','e','e','w','e','w','w','p','w','w','w','w','w','w'},
			{'e','e','e','e','e','e','p','e','e','e','w','e','e','e','e','e','e','w','e','e','e','p','e','e','e','e','e','e'},
			{'w','w','w','w','w','w','p','w','w','e','w','e','e','e','e','e','e','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','e','e','e','e','e','e','e','e','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','w','w','w','w','w','p','w','w','e','w','w','w','w','w','w','w','w','e','w','w','p','w','w','w','w','w','w'},
			{'w','p','p','p','p','p','p','p','p','p','p','p','p','w','w','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','p','w','w','w','w','w','p','w','w','p','w','w','w','w','w','p','w','w','w','w','p','w'},
			{'w','p','p','p','w','w','p','p','p','p','p','p','p','p','c','p','p','p','p','p','p','p','w','w','p','p','p','w'},
			{'w','w','w','p','w','w','p','w','w','p','w','w','w','w','w','w','w','w','p','w','w','p','w','w','p','w','w','w'},
			{'w','p','p','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','w','w','p','p','p','p','p','p','w'},
			{'w','p','w','w','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','w','w','p','w'},
			{'w','p','w','w','w','w','w','w','w','w','w','w','p','w','w','p','w','w','w','w','w','w','w','w','w','w','p','w'},
			{'w','g','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','p','w'},
			{'w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w','w'}};

    class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e); 
        }
    }
}
