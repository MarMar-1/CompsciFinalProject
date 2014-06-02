package CompsciFinalProject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Framework extends Canvas{

	public static int frameWidth;
	public static int frameHeight;

	public static final long secInNanosec = 1000000000L;
	public static final long milisecInNanosec = 1000000L;

	private final int GAME_FPS = 60;
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
	public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
	public static GameState gameState;

	private long gameTime;
	private long lastTime;

	private Game game;

	private BufferedImage shootTheDuckMenuImg;

	public Framework ()
	{
		super();

		gameState = GameState.VISUALIZING;

		Thread gameThread = new Thread() {
			@Override
			public void run(){
				GameLoop();
			}
		};
		gameThread.start();
	}

	private void Initialize()
    {

    }

    private void LoadContent()
    {

    }

    private void GameLoop()
    {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;

                case GAMEOVER: break;

                case MAIN_MENU: break;

                case OPTIONS: break;

                case GAME_CONTENT_LOADING: break;

                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                break;

                case VISUALIZING:
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            repaint();
            
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            if (timeLeft < 10) 
                timeLeft = 10;
            try {
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }

}