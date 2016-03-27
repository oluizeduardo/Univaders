package shoot_the_alien;

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
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The actual game.
 * 
 * @author www.gametutorial.net
 * @author Luiz Eduardo da Costa
 * @category Game
 * @version 2.0
 */
public class Game {
    
	
	/**
	 * Maximum number of fugitives.
	 */
	private static final int MAX_ALIENS_RUNAWAY = 20;
    /**
     * We use this object to generate a random number.
     */
    private Random random;
    
    /**
     * Font that we will use to write statistic on the screen.
     */
    private Font font;
    
    /**
     * Array list of the aliens.
     */
    private ArrayList<Alien> aliens;
    
    /**
     * How many aliens leave the screen alive?
     */
    private int runawayAliens;
    
   /**
     * How many aliens the player killed?
     */
    private int killedAliens;
    
    /**
     * For each killed alien, the player gets points.
     */
    private int score; 
    /**
     * How many times a player is shot?
     */
    private int shoots;
    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;    
    /**
     * The time which must elapse between shots.
     */
    private long timeBetweenShots;
    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;  
    /**
     * Univás logo bottom.
     */
    private BufferedImage univas_rodape_Img;  
    /**
     * Alien image.
     */
    private BufferedImage alienImg, alienDead;   
    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;
    /**
     * Red border to game over.
     */
    private BufferedImage red_borderImg;
    /**
     * There is any alien dead?
     */
    private boolean hasAlienDead = false;
    /**Position X of the alien dead.*/
    private int alienDead_x = 0;
    /**Position Y of the alien dead.*/
    private int alienDead_y = 0;
    
    

    
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
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();        
        font = new Font("monospaced", Font.BOLD, 25);
        
        aliens = new ArrayList<Alien>();
        
        runawayAliens = 0;
        killedAliens = 0;
        score = 0;
        shoots = 0;
        
        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 4;
    }
    
    
    
    
    /**
     * Load the files of the game (images, sounds...)
     */
    private void LoadContent()
    {
    	// URL common to all files.
    	String url = "/shoot_the_alien/resources/images/";
        try
        {
            URL backgroundImgUrl = this.getClass().getResource(url+"background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL univas_rodape_ImgUrl = this.getClass().getResource(url+"logo_base.png");
            univas_rodape_Img = ImageIO.read(univas_rodape_ImgUrl);
            
            URL alienImgUrl = this.getClass().getResource(url+"alien.png");
            alienImg = ImageIO.read(alienImgUrl);

            URL alienDeadUrl = this.getClass().getResource(url+"blood.png");//
            alienDead = ImageIO.read(alienDeadUrl);
            
            URL sightImgUrl = this.getClass().getResource(url+"sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            
            URL red_borderImgUrl = this.getClass().getResource(url+"red_border.png");
            red_borderImg = ImageIO.read(red_borderImgUrl);
            
        }
        catch (IOException ex) {
        	
        	Logger.getLogger(Game.class.getName());
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        // Removes all the aliens from this list.
        aliens.clear();
        
        // We set last alien time to zero.
        Alien.lastAlienTime = 0;
        
        runawayAliens = 0;
        killedAliens = 0;
        score = 0;
        shoots = 0;
        alienDead = null;
        lastTimeShoot = 0;
    }
    
    
    
    /**
     * Update game logic.
     * 
     * Very important game's method.
     * 
     * @param gameTime The game time.
     * @param mousePosition Current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        // Creates a new alien, if it's the time, and add it to the array list.
        if(System.nanoTime() - Alien.lastAlienTime >= Alien.timeBetweenAliens)
        {
        	
        	int x 	  = Alien.alienLines[Alien.nextAlienLines][0] + random.nextInt(200);// demora um pouco mais para aparecer.
        	int y 	  = Alien.alienLines[Alien.nextAlienLines][1];
        	int speed = Alien.alienLines[Alien.nextAlienLines][2];
        	int score = Alien.alienLines[Alien.nextAlienLines][3];
        	
        	// Here we create new duck and add it to the array list.
        	Alien newAlien = new Alien(x, y, speed, score, alienImg);
        	aliens.add(newAlien);
        	
            
            // Here we increase nextAlienLines so that next alien will be created in next line.
            Alien.nextAlienLines++;
            
            // Used to don't exceed the bounds of the array.
            if(Alien.nextAlienLines >= Alien.alienLines.length)
                Alien.nextAlienLines = 0;
            
            Alien.lastAlienTime = System.nanoTime();
        }
        
        // Update all of the aliens.
        for(int i = 0; i < aliens.size(); i++)
        {
            // Move the alien.
            aliens.get(i).Update();
            
            // Checks if the duck leaves the screen and remove it if it does.
            if(aliens.get(i).x < 0 - alienImg.getWidth())
            {
                aliens.remove(i);
                runawayAliens++;
            }
        }
        
        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;
                
                // We go over all the aliens and we look if any of them was shoot.
                for(int i = 0; i < aliens.size(); i++)
                {
                	
                	int x = aliens.get(i).x;
                	int y = aliens.get(i).y;
                	int larg = 128;
                	int altu = 128;
                	Rectangle rec = new Rectangle(x, y, larg, altu);
                	
                	// We check, if the mouse was over the aliens body, when player has shot.
                	if(rec.contains(mousePosition)){

                        killedAliens++;
                        score += aliens.get(i).score;
                        
                        this.hasAlienDead = true;
                        
                        // Get the alien position.
                        this.alienDead_x= aliens.get(i).x;
                        this.alienDead_y = aliens.get(i).y;
                        
                        // Remove the aliens from the array list.
                        aliens.remove(i);
                        
                        // We found the alien that player shoot, so we can leave the for loop.
                        break;
                    }
                }
                
                lastTimeShoot = System.nanoTime();
            }
        }
        
        // When 'MAX_ALIENS_RUNAWAY' aliens runaway, the game ends.
        if(runawayAliens >= MAX_ALIENS_RUNAWAY)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }
    
        
    
    
 
    
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
    	// Draw the background image.
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        // Here we draw all the aliens.
        for(int i = 0; i < aliens.size(); i++)
        {
            aliens.get(i).Draw(g2d);
        }
        
        // Draw the Univás logo in the lower left corner.
        g2d.drawImage(univas_rodape_Img, 0, Framework.frameHeight - (univas_rodape_Img.getHeight() + 10), 250, 70, null);
        // Draw the sight.
        g2d.drawImage(sightImg, mousePosition.x , mousePosition.y , null);
        
        g2d.setFont(font);
        g2d.setColor(Color.MAGENTA);
        
        // Draw the scoreboard in the higher left corner.
        g2d.drawString("FUGITIVOS..: " + runawayAliens, 10, 20);
        g2d.drawString("CAPTURADOS.: " + killedAliens, 10, 45);
        g2d.drawString("TIROS......: " + shoots, 10, 70);
        g2d.drawString("PONTOS.....: " + score, 10, 95);
        
        
        if(hasAlienDead){
        	// Draw the blood of the alien dead.
            g2d.drawImage(alienDead, alienDead_x, alienDead_y, 70, 70, null);            
        }
        
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);
        
        // Draw the Game Over image.
        g2d.drawImage(red_borderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        g2d.setColor(Color.red);
        g2d.drawString("Game Over!!!", Framework.frameWidth / 2 - 50, (int)(Framework.frameHeight / 2)-15);
        g2d.drawString("Pressione ESPAÇO ou ENTER para continuar", Framework.frameWidth / 2 - 300, (int)(Framework.frameHeight /2) + 25);
    }
    
    
    
    
    
}
