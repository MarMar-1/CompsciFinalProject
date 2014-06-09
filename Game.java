package CompsciFinalProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Game{
	private Font font;
	private int score;
	private BufferedImage backgroundImg;
	private BufferedImage boofpacImg;
	private BufferedImage tupacImg;
	private int circleMiddleDiameter;
	private boolean[][] tupacs;
	private int[] circlesX, circlesY;
	private int xOffset;
	private int boofpacX, boofpacY;
	private int initTupacs;
	private long lastTimeClicked;    
	private long timeBetweenClicks;
	private int[] moveX0, moveY0, moveX1, moveY1;
	private int[][] weights;

	public Game(int initTupacs)
	{
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
		this.initTupacs = initTupacs;

		Thread threadForInitGame = new Thread() {
			@Override
			public void run(){
                // Sets variables and objects for the game.
				Initialize();
                // Load game files (images, sounds, ...)
				LoadContent();

				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		threadForInitGame.start();
	}

	private void Initialize()
	{      
		font = new Font("monospaced", Font.BOLD, 18);

		tupacs = new boolean [11][11];

		circlesX = new int[]{60, 114, 168, 221, 275, 329, 383, 437, 491, 545, 598};
		xOffset = 27;
		circlesY = new int[]{74, 116, 157, 196, 239, 280, 321, 362, 403, 445, 486};

		moveX1 = new int[]{-1,1,0,0,1,1};
		moveY1 = new int[]{0,0,-1,1,1,-1};

		moveX0 = new int[]{-1,1,0,0,-1,-1};
		moveY0 = new int[]{0,0,-1,1,-1,1};

		initWeights();

		boofpacY = boofpacX = 5;

		lastTimeClicked = 0;
		timeBetweenClicks = Framework.secInNanosec / 3;

		initializeTupacs();

		score = 120 - initTupacs; 
	}

	private void initWeights(){
		weights = new int[11][11];
		for (int i = 0; i < 11; i ++){
			for (int j = 0; j < 11; j++){
				weights[i][j] = Math.min(i,j);
			}
		}
	}

	private void initializeTupacs(){
		int i = 0;
		int j = 0;
		int count=0;
		while (count < initTupacs){
			if(!tupacs[i][j]  && (i != boofpacX || j != boofpacY)){
				if(Math.random() < ((double) initTupacs)/120){
					count++;
					tupacs[i][j] = true;
					weights[i][j] = 200;
				}
				else
					tupacs[i][j] = false;

			}
			if(i<10)
				i++;
			else if(j<10){
				i = 0;
				j++;
			}
			else
				i = j = 0;


		}
	}

	public void RestartGame()
	{

		score = 120 - initTupacs;

		boofpacY = boofpacX = 5;

		lastTimeClicked = 0;

		tupacs = new boolean [11][11];

		initWeights();

		initializeTupacs();

	}


	private void LoadContent()
	{
		try
		{
			URL backgroundImgUrl = this.getClass().getResource("/CompsciFinalProject/resources/images/background.jpg");
			backgroundImg = ImageIO.read(backgroundImgUrl);

			URL boofpacImgUrl = this.getClass().getResource("/CompsciFinalProject/resources/images/boofpac.png");
			boofpacImg = ImageIO.read(boofpacImgUrl);

			URL tupacImgUrl = this.getClass().getResource("/CompsciFinalProject/resources/images/tupac.png");
			tupacImg = ImageIO.read(tupacImgUrl);

		}
		catch (IOException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
     * Draw the game to the screen.
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
	public void Draw(Graphics2D g2d, Point mousePosition){
		g2d.drawImage(backgroundImg, (Framework.frameWidth - 732)/2, (Framework.frameHeight - 603)/2, 732, 603, null);
		drawBoofpac(g2d);
		drawTupacs(g2d);

		g2d.setFont(font);
		g2d.setColor(Color.darkGray);

		g2d.drawString("Score: " + score, Framework.frameWidth / 2 - 180, Framework.frameHeight/2 - 250);
	}

	private void drawBoofpac(Graphics2D g2d){
		int x = (Framework.frameWidth - 732)/2, y = (Framework.frameHeight - 603)/2;
		x+=circlesX[boofpacX];
		if(boofpacY % 2 !=0)
			x+=xOffset;
		y+=circlesY[boofpacY];


		g2d.drawImage(boofpacImg, x, y, 47, 47, null);

	}

	private void drawTupacs(Graphics2D g2d){
		for (int i = 0; i < 11; i ++){
			for (int j = 0; j < 11; j++){
				if(tupacs[i][j]){
					int x = (Framework.frameWidth - 732)/2, y = (Framework.frameHeight - 603)/2;
					x+=circlesX[i];
					if(j % 2 !=0)
						x+=xOffset;
					y+=circlesY[j];


					g2d.drawImage(tupacImg, x, y, 47, 47, null);

				}
			}
		}
	}

    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition){
    	g2d.drawImage(backgroundImg, (Framework.frameWidth - 732)/2, (Framework.frameHeight - 603)/2, 732, 603, null);
    	g2d.setColor(Color.black);
    	g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, Framework.frameHeight/2 - 255);
    	g2d.drawString("Left Click to Restart.", Framework.frameWidth / 2 - 55, Framework.frameHeight/2 - 245);
    	g2d.setColor(Color.red);
    	g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, Framework.frameHeight/2 - 255);
    	g2d.drawString("Left Click to Restart.", Framework.frameWidth / 2 - 56, Framework.frameHeight/2 - 245);

    }

    /**
     * Draw the victory screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawVictory(Graphics2D g2d, Point mousePosition){
    	g2d.drawImage(backgroundImg, (Framework.frameWidth - 732)/2, (Framework.frameHeight - 603)/2, 732, 603, null);
    	g2d.setColor(Color.black);
    	g2d.drawString("Victory! Score: " + score, Framework.frameWidth / 2 - 39, Framework.frameHeight/2 - 255);
    	g2d.drawString("Left Click to Restart.", Framework.frameWidth / 2 - 55, Framework.frameHeight/2 - 245);
    	g2d.setColor(Color.red);
    	g2d.drawString("Victory! Score: " + score, Framework.frameWidth / 2 - 40, Framework.frameHeight/2 - 255);
    	g2d.drawString("Left Click to Restart.", Framework.frameWidth / 2 - 56, Framework.frameHeight/2 - 245);

    }

    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	if(boofpacX == 0 || boofpacX == 10 || boofpacY == 0 || boofpacY == 11){
    		Framework.gameState = Framework.GameState.GAMEOVER;
    	}
    	if(Canvas.mouseButtonState(MouseEvent.BUTTON1)){
    		if(System.nanoTime() - lastTimeClicked >= timeBetweenClicks){
    			for(int i = 0; i < 11; i++){
    				for (int j = 0; j < 11; j++){
    					if(!tupacs[i][j] && (i != boofpacX || j != boofpacY))
    					{
    						int x = (Framework.frameWidth - 732)/2, y = (Framework.frameHeight - 603)/2;
    						x+=circlesX[i];
    						if(j % 2 !=0)
    							x+=xOffset;
    						y+=circlesY[j];
    						if (new Rectangle(x, y, 47, 47).contains(mousePosition)){
    							score--;
    							tupacs[i][j] = true;
    							updateWeights(i,j);
    							moveBoofpac();
    							i=12;
    							break;
    						}
    					}
    				}
    			}
    			lastTimeClicked = System.nanoTime();
    		}
    	}

    }

    private void updateWeights(int x, int y){
    	weights[x][y] = 200;
    }

    private void moveBoofpac(){
    	weights[boofpacX][boofpacY]++;

    	if(boofpacX == 5 && boofpacY == 5){
    		double rand = Math.random();
    		if (rand < .25)
    			boofpacX--;
    		else if (rand < .5)
    			boofpacX++;
    		else if (rand < .75)
    			boofpacY--;
    		else
    			boofpacY++;
    		return;
    	}
    	else{
    		int bestMove = findBestMove(boofpacX,boofpacY);
    		if(boofpacY%2 == 0){
	    		int newX = boofpacX + moveX0[bestMove];
	    		int newY = boofpacY + moveY0[bestMove];
	    		if(tupacs[newX][newY]){
	    			for (int i = 0; i < 6; i ++){
	    				if(!tupacs[boofpacX + moveX0[i]][boofpacY + moveY0[i]]){
	    					boofpacX += moveX0[i];
	    					boofpacY += moveY0[i];
	    					return;
	    				}
	    			}
	    		}
	    		else{
	    			boofpacX = newX;
	    			boofpacY = newY;
	    			return;
	    		}
	    	}
	    	else{
	    		int newX = boofpacX + moveX1[bestMove];
	    		int newY = boofpacY + moveY1[bestMove];
	    		if(tupacs[newX][newY]){
	    			for (int i = 0; i < 6; i ++){
	    				if(!tupacs[boofpacX + moveX1[i]][boofpacY + moveY1[i]]){
	    					boofpacX += moveX1[i];
	    					boofpacY += moveY1[i];
	    					return;
	    				}
	    			}
	    		}
	    		else{
	    			boofpacX = newX;
	    			boofpacY = newY;
	    			return;
	    		}
	    	}
	    }
	    Framework.gameState = Framework.GameState.VICTORY;
	}

	private int findBestMove(int x, int y){
		if(y%2 == 0){
			int lowest = weights[x + moveX0[0]][y + moveY0[0]];
			ArrayList<Integer> indexes = new ArrayList<Integer>(Arrays.asList(0));
			for (int i = 1; i < 6; i ++){
				if(x + moveX0[i] >= 0 && x + moveX0[i] < 11 && y + moveY0[i] >= 0 && y + moveY0[i] < 11){
					int weight = weights[x + moveX0[i]][y + moveY0[i]];
					if(weight < lowest){
						lowest = weight;
						indexes = new ArrayList<Integer>(Arrays.asList(i));
					}
					else if(weight == lowest){
						indexes.add(i);
					}
				}
			}
			return indexes.get((int) (Math.random() * indexes.size()));
		}
		else{
			int lowest = weights[x + moveX1[0]][y + moveY1[0]];
			ArrayList<Integer> indexes = new ArrayList<Integer>(Arrays.asList(0));
			for (int i = 1; i < 6; i ++){
				if(x + moveX1[i] >= 0 && x + moveX1[i] < 11 && y + moveY1[i] >= 0 && y + moveY1[i] < 11){
					int weight = weights[x + moveX1[i]][y + moveY1[i]];
					if(weight < lowest){
						lowest = weight;
						indexes = new ArrayList<Integer>(Arrays.asList(i));
					}
					else if(weight == lowest){
						indexes.add(i);
					}
				}
			}
			return indexes.get((int) (Math.random() * indexes.size()));
		}
	}
}