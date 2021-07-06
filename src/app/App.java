package app;

import javax.swing.JFrame;

import game.Board;

public class App {
	
	public static final int height = 620;
	public static final int width = 575;
	
	JFrame frame;
	
    public App(JFrame frame) {
		this.frame = frame;
		initializeUI(this.frame);
    }
    
    private void initializeUI(JFrame frame) { 
    	frame.add(new Board(height, width));
        frame.setTitle("Pac");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
    	JFrame frame = new JFrame();
    	var app = new App(frame);
    	app.frame.setVisible(true);
    }

}
