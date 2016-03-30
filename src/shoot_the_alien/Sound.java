package shoot_the_alien;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;


public class Sound implements Runnable {

	
	
	public static final String INTRO = "audios/into_warrior.mid";
	
	public static final String SHOOT = "audios/shoot-laser.mid";
	
	
	
	/**
	 * Load a file sound.
	 */
	private AudioClip audio;
	
	
	
	public Sound() {
		loadBackgroundMusic();
	}
	
	
	
	
	
	@Override
	public void run() {
		playBackgroundMusic();
	}
	
	
	
	
	/**
	 * Load the sound that will be executed in backgound.
	 */
	private void loadBackgroundMusic()
    {
		URL urlInitialSound = this.getClass().getResource("/shoot_the_alien/resources/audio/intro_warrior.mid");

    	audio = Applet.newAudioClip(urlInitialSound);
    }
	
	
	
	
	private void playBackgroundMusic()
    {
		this.audio.loop();
    }
	
	


}
