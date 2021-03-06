package shoot_the_alien;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 * @author Luiz Eduardo da Costa
 * @category Game
 * @version 2.0
 */
public class Window extends JFrame{
        
	
	
	private static final long serialVersionUID = 1L;



	/**
	 * Sets the properties of the frame.
	 */
	private Window()
    {
        // Sets the title for this frame.
        this.setTitle("UNIVADERS - Shoot The Alien");
        
        // Disables decorations for this frame.
        this.setUndecorated(true);
        // Puts the frame to full screen.
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
     /* // Window mode - Size of the frame.
        this.setSize(900, 600);
        // Puts frame to center of the screen.
        this.setLocationRelativeTo(null);
     */ 
          
        // Exit the application when user close the frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add the panel.
        this.setContentPane(new Framework());
        
        this.setVisible(true);
        
        // Yeah! It's about two hours playng the sound of background =)
        PlayWAVFile pf = new PlayWAVFile(PlayWAVFile.INTRO_WARRIOR, 120);
        Thread t = new Thread(pf);
        t.start();
    }

    
    
    /**
     * Main method loads the start of the game.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
