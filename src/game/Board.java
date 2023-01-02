package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

//TODO: Clean up this file as follows:
//1.   I think I want to move the drawing to a different class

public class Board extends JPanel implements ActionListener{
	
	//TODO: Learn what this is for
	private static final long serialVersionUID = 1L;
	
	private int height;
	private int width;
	
	private static Color backgroundColor = Color.black;
	private static Color borderAndFontColor = new Color(233, 214, 115);
	
    private Timer timer;
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
	
	private CollisionDetector cd;
	
	public void initializeBoard() {
		
		addKeyListener(new GameKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);
        cd = new CollisionDetector(this);

        gameStats = new Statistics();
        
        tiles = new Tile[height/20][width/20];
        points = new ArrayList<Point>();
        maze = new HashMap<String, Wall>();
        placeGameObjects();

        timer = new Timer(DELAY, this);
        timer.start();
        
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBoard(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawBoard(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        renderBoard(g2d);
		gameStats.renderScore(g2d, height);
		 for (int i = 0; i < points.size(); i++) {
	        	g2d.drawImage(points.get(i).getImage(), points.get(i).getX_position(), 
	            		points.get(i).getY_position(), this);
	        }
		 for (Wall values : maze.values() ) {
	        	g2d.drawImage(values.getImage(), values.getX_position(), 
	            		values.getY_position(), this);
		 } 

        g2d.drawImage(player.getImage(), player.getX_position(), 
            player.getY_position(), this);
        
        g2d.drawImage(ghost.getImage(), ghost.getX_position(), 
        		ghost.getY_position(), this);
        
              
    }
	
	public void renderBoard(Graphics2D g2d) {	
		CreateBackground(g2d, width, height);
		CreateBorder(g2d, width, height);
	}
	
	private void CreateBackground(Graphics2D g2d, int width, int height){
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, width, height);
	}
	private void CreateBorder(Graphics2D g2d, int width, int height) {
		g2d.setColor(borderAndFontColor);
		g2d.drawRect(0, 0, width, height);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		//always check collisions before moving the player
		
		updateGameObjectTile(player);
		updateGameObjectTile(ghost);
		
		checkCollisions();
		
		if(player.changingDirection) {
			processGameObjectDirectionChange(player);
		}

		if(ghost.changingDirection) {
			processGameObjectDirectionChange(ghost);
		}
		
		movePlayer();
		moveGhosts();
		
		repaint();
	}
	
	private void processGameObjectDirectionChange(GameObject obj){
		 boolean collisionInNewDirection = false;

		 obj.requestCounter--;
			if(obj.requestCounter <= 0) {
				obj.changingDirection = false;
			}
			collisionInNewDirection = cd.checkFutureGameObjectWallCollision(obj.requested_dx, obj.requested_dy, obj);
	    	if (collisionInNewDirection == false) {
	    		obj.setX_direction(obj.requested_dx);
	    		obj.setY_direction(obj.requested_dy);
				obj.setObjectDirection(obj.requestedDirection); 
				obj.changingDirection = false;
	    	}
	}
	


	
    private void movePlayer() {
        player.move();        
    }     
    
    private void moveGhosts() {
    	determineGhostPath();
    	ghost.move();
    }
    
    private void determineGhostPath() {
    	int px = player.getTile_x();
    	int py = player.getTile_y();
    	
    	int gx = ghost.getTile_x();
    	int gy = ghost.getTile_y();
    	
    	if(!ghost.moving()) {
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
    			checkGhostDirectionOrder("Down", "Left", "Up", "Right");
    		}
        	else if(px > gx && py > gy) {
    			checkGhostDirectionOrder("Down", "Right", "Up", "Left");
    	
    		}
        	else if(px == gx && py < gy) {
    			checkGhostDirectionOrder("Up", "Right", "Down", "Left");
    	
    		}
        	else if(px == gx && py > gy) {
    			checkGhostDirectionOrder("Down", "Left", "Up", "Right");

    		}	
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
    	
    	switch(direction) {
    	case "Up":
			tilex = ghost.getTile_x();
			tiley = ghost.getTile_y()-1;
			break;
		case "Down":
			tilex = ghost.getTile_x();
			tiley = ghost.getTile_y()+1;
			break;

		case "Right":
			tilex = ghost.getTile_x()+1;
			tiley = ghost.getTile_y();
			break;

		case "Left":
			tilex = ghost.getTile_x()-1;
			tiley = ghost.getTile_y();
			break;
		}
    	
    	String mazeKey = Integer.toString(tilex) + "-" + Integer.toString(tiley);
   
    	return !maze.containsKey(mazeKey) && !ghost.objectDirection.equals(direction);
    }
    
    public void stopGameObjectMovement(GameObject obj) {
    	obj.stopMoving();
    }
    
    private void checkCollisions() {
    	cd.checkBoardBounds();
    	cd.checkPlayerWallCollision();
    	cd.checkEnemyWallCollision();
    	cd.checkPlayerPointCollision();	
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
  
    
    class GameKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
	
	private void placeGameObjects() {
		
		//rows
		for(int i = 0; i < tiles.length; i++) {
			//columns
			for(int j = 0; j < tiles[i].length; j++) {
				
				tiles[i][j] = new Tile(j, i);
				
				//TODO: Explore best ways to store level data to read in and then convert to 2d array. Maybe JSON?
				if(levelData[i][j].equals("Wall")) {
					Wall w = new Wall(j*20, i*20);
					maze.put(Integer.toString(j) + "-" + Integer.toString(i), w);
				}
				else if (levelData[i][j].equals("Player")){
					player = new PlayerCharacter(j*20, i*20);
				}
				else if(levelData[i][j].equals("Point")) {
					Point p = new Point(j*20, i*20);
					points.add(p);
				}
				else if(levelData[i][j].equals("Enemy")) {
					ghost = new Enemy(j*20, i*20);
				}
							
			}
		}
	}
	
	private String[][] levelData = {
			{"Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"}, 
			{"Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "empty", "empty", "empty", "empty", "empty", "empty", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"empty", "empty", "empty", "empty", "empty", "empty", "Point", "empty", "empty", "empty", "Wall", "empty", "empty", "empty", "empty", "empty", "empty", "Wall", "empty", "empty", "empty", "Point", "empty", "empty", "empty", "empty", "empty", "empty"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "empty", "empty", "empty", "empty", "empty", "empty", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "empty", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "empty", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
			{"Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Player", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Wall"},
			{"Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall"},
			{"Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall"},
			{"Wall", "Enemy", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"}
		};
}
