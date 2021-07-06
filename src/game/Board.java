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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

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
    List<Point> points;
    List<Wall> maze;
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
        maze = new ArrayList<Wall>();
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


        g2d.drawImage(player.getImage(), player.getX_position(), 
            player.getY_position(), this);
        
        for (int i = 0; i < points.size(); i++) {
        	g2d.drawImage(points.get(i).getImage(), points.get(i).getX_position(), 
            		points.get(i).getY_position(), this);
        }
        for (int i = 0; i < maze.size(); i++) {
        	g2d.drawImage(maze.get(i).getImage(), maze.get(i).getX_position(), 
            		maze.get(i).getY_position(), this);
        }        
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
	public void actionPerformed(ActionEvent e) {
		//always check collisions before moving the player
		
		updatePlayerTile();
		
		checkCollisions();
		
		movePlayer();
		
		repaint();
	}
	
    private void movePlayer() {
        player.move();        
    }     
    
    private void checkCollisions() {

    	cd.checkBoardBounds();
    	cd.checkPlayerWallCollision();
    	cd.checkPlayerPointCollision();
    	
    }
    
   
  
    private void updatePlayerTile() {
    	for(int i = 0; i < tiles.length; i++) {
    		for (int j = 0; j < tiles[i].length; j++) {
        		if (player.getHitbox().intersects(tiles[i][j].getHitbox())) {
        			tiles[player.getTile_y()][player.getTile_x()].gameObject = null;
        			player.setTile_x(j);
        			player.setTile_y(i);
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
	
	public void placeGameObjects() {
		
		//rows
		for(int i = 0; i < tiles.length; i++) {
			//columns
			for(int j = 0; j < tiles[i].length; j++) {
				
				tiles[i][j] = new Tile(j, i);
				GameObject obj;
				
				//TODO: Explore best ways to store level data to read in and then convert to 2d array. Maybe JSON?
				if(levelData[i][j].equals("Wall")) {
					obj = new Wall(j*20, i*20);
					maze.add((Wall) obj);	
				}
				else if (levelData[i][j].equals("Player")){
					player = new PlayerCharacter(j*20, i*20);
					obj = player;
				}
				else {
					obj = new Point(j*20, i*20);
					points.add((Point) obj);
				}
				
				tiles[i][j].gameObject =  obj;
			
			}
		}
	}
	
	
	private String[][] levelData = {
			{"Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"}, 
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "null", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall"},
			{"Wall", "null", "Wall", "Wall", "Point", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Player", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "null", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Point", "Wall", "Point", "Point", "Point", "Point"},
			{"Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall","Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"}, 

		};
}
