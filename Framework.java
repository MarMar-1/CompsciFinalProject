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
		try
        {
            URL menuImgUrl = this.getClass().getResource("/CompsciFinalProject/resources/images/menu.jpg");
            menuImg = ImageIO.read(menuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition());
            break;
            case MAIN_MENU:
                g2d.drawImage(menuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawString("Use left mouse button place tupacs in order to trap boofpac.", frameWidth / 2 - 83, (int)(frameHeight * 0.65));   
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int)(frameHeight * 0.67));                
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int)(frameHeight * 0.70));
                g2d.setColor(Color.white);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }

    private void newGame()
    {
    	gameTime = 0;
    	lastTime = System.nanoTime();

    	game = new Game();
    }

    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
    	gameTime = 0;
    	lastTime = System.nanoTime();

    	game.RestartGame();

        // We change game status so that the game can start.
    	gameState = GameState.PLAYING;
    }

    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
    	try
    	{
    		Point mp = this.getMousePosition();

    		if(mp != null)
    			return this.getMousePosition();
    		else
    			return new Point(0, 0);
    	}
    	catch (Exception e)
    	{
    		return new Point(0, 0);
    	}
    }

    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
    	switch (gameState)
    	{
    		case GAMEOVER:
    		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
    			System.exit(0);
    		else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
    			restartGame();
    		break;
    		case PLAYING:
    		case MAIN_MENU:
    		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
    			System.exit(0);
    		break;
    	}
    }

    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
    	switch (gameState)
    	{
    		case MAIN_MENU:
    		if(e.getButton() == MouseEvent.BUTTON1)
    			newGame();
    		break;
    	}
    }
}