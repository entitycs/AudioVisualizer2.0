package visualizer.visualizerGDX;

import javafx.application.Platform;
import application.event.TavEndOfMediaEventHandler;
import application.listener.TavAudioSpectrumListener;
import application.mediaPlayer.interfacing.TavMediaPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * AudioVisualizer is the main class for the whole system. It facilitates the
 * complete process of the Visualizer. This process is as follows:
 * <ul>
 * <li>Load the mp3 file to visualize and play
 * <li>Go through the song, decoding each frame to raw data samples
 * <li>Write samples to AudioDevice
 * <li>Transform samples data into spectrum data
 * <li>Visualize spectrum data and draw visualization to screen
 * </ul>
 * 
 * @author Eric
 */
public class AudioVisualizerPlayer implements TavMediaPlayer, LifecycleListener {

	private String mediaLocation = null;
	private AudioVisualizer AV;
	private AudioVisualizerPlayer AVP;
	private boolean wrapperRunning = false;
	private LwjglApplication visApp;
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 800;
	private LwjglApplicationConfiguration cfg;
	private TavEndOfMediaEventHandler endOfMediaHandler;
	private TavAudioSpectrumListener audioSpectrumListener;
	private TavEndOfMediaEventHandler endOfMediaEventHandler;

	public AudioVisualizerPlayer() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		wrapperRunning = false;
		if (AV != null){
			AV.stop();
			//AV.dispose();
		}
		if (visApp != null)
		visApp.exit();
		if (Gdx.app != null)
		Gdx.app.exit();
	}

	@Override
	public Object getAudioEqualizer() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public double getAudioSpectrumInterval() {
		return 0; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	@Override
	public int getAudioSpectrumNumBands() {
		return 0; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	@Override
	public int getAudioSpectrumThreshold() {
		return 0; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	@Override
	public Double getCurrentTime() {
		return null; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	@Override
	public String getMediaLocation() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Object getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  LibGDX unused methods
	 *  
	 *  pause and resume come from the interface we need to gracefully close
	 *  a libGDX visualization window
	 */
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseTrack() {
		AV.pause1();
	}

	@Override
	public void playlistReady() {
		if (!wrapperRunning && visApp == null) {

			// main(new String[]
			// { this.mediaLocation });

			cfg = new LwjglApplicationConfiguration();
			cfg.title = "AudioVisualizer";
			cfg.useGL20 = false;
			cfg.width = WIDTH;
			cfg.height = HEIGHT;
			cfg.forceExit = false;
			System.out.println("hello");
			AVP = this;
			// Run the AudioVisualizer under the watch of the JavaFX thread
			// (i.e. put into thread queue)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					AV = new AudioVisualizer(mediaLocation);

					// do not close the application stage on exit
					visApp = new LwjglApplication(AV, cfg);
					AV.setApp(visApp);
					AV.setAudioSpectrumListener(audioSpectrumListener);
					System.out.println("visapp");
					visApp.addLifecycleListener(AVP);
					AV.setonEndOfMedia(endOfMediaEventHandler);

				}
			});
			Thread.yield();

			wrapperRunning = true;
		} else {
			stopTrack();
			AV.setApp(visApp);
			AV.setAudioSpectrumListener(audioSpectrumListener);
			System.out.println("visapp");
			visApp.addLifecycleListener(AVP);
			AV.setonEndOfMedia(endOfMediaEventHandler);

			// Thread.sleep(400); // how to keep libGDX player from misreading
			// header?
			AV.newTrack(this.mediaLocation);
		}
	}

	@Override
	public void playTrack() {
		AV.resume1();
	}

	/**
	 *  LibGDX unused methods
	 *  
	 *  pause and resume come from the interface we need to gracefully close
	 *  a libGDX visualization window
	 */
	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setAudioSpectrumInterval(double interval) {
		return false;
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	// libGDX LIFECYCLE LISTENERS

	
	@Override
	public boolean setAudioSpectrumNumBands(int numBands) {
		return false;
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public boolean setAudioSpectrumThreshold(int threshold) {
		return false;
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
	

	/**
	 * Set AudioVisualizer
	 * 
	 * @param AV
	 *            - the AudioVisualizer instance used as a GDX app
	 */
	public void setAV(AudioVisualizer AV) {

		this.AV = AV;

	}

	@Override
	public void setMediaLocation(String mediaLocation) {
		this.mediaLocation = mediaLocation;
	}

	@Override
	public void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler) {
		// TODO Auto-generated method stub
		this.endOfMediaEventHandler = tavEndOfMediaEventHandler;
		
	}

	@Override
	public void stopTrack() {
		// TODO Auto-generated method stub
		if (AV != null)
			AV.stop();
	}

	@Override
	public void setAudioSpectrumListener(
			TavAudioSpectrumListener audioSpectrumListener)
	{
		this.audioSpectrumListener = audioSpectrumListener;
		
	}
}
