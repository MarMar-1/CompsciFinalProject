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

	public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
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
        random = new Random();        
        font = new Font("monospaced", Font.BOLD, 18);
        
        int[][] circles = new int[11][11];
        
        score = 0; 
    }

    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL grassImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);
            
            URL duckImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);
            
            URL sightImgUrl = this.getClass().getResource("/shoot_the_duck/resources/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}