package audiovisualizer.gdx;

import javafx.application.Platform;
import application.mediaPlayer.TavMetaData;
import application.mediaPlayer.event.TavEndOfMediaEventHandler;
import application.mediaPlayer.interfacing.TavComboMediaPlayer;
import application.mediaPlayer.listener.TavAudioSpectrumListener;
import application.mediaPlayer.listener.TavMetaDataListener;
import application.visualizer.interfacing.TavVisualization;
import application.visualizer.interfacing.TavVisualizer;

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
public class AudioVisualizerPlayer implements TavComboMediaPlayer,
		LifecycleListener
{

	private String mediaLocation = null;
	private AudioVisualizer child;
	private AudioVisualizerPlayer childP;
	private boolean wrapperRunning = false;
	private LwjglApplication visApp;
	private int WIDTH = 1024;
	private int HEIGHT = 800;
	private LwjglApplicationConfiguration cfg;
	private TavAudioSpectrumListener audioSpectrumListener;
	private TavEndOfMediaEventHandler endOfMediaEventHandler;
	private TavMetaDataListener metaDataListener;
	private Number visualizationItem;
	private int visualizationIndex;

	public AudioVisualizerPlayer()
	{
		cfg = new LwjglApplicationConfiguration();
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		wrapperRunning = false;
		if (child != null)
		{
			child.stop();
			child.dispose();
		}
		if (visApp != null)
			visApp.exit();
		if (Gdx.app != null)
			Gdx.app.exit();

		endOfMediaEventHandler.playerClosed();
	}

	@Override
	public Object getAudioEqualizer()
	{
		return null; 
	}

	@Override
	public double getAudioSpectrumInterval()
	{
		return 0; 
	}

	@Override
	public int getAudioSpectrumNumBands()
	{
		return 0; 
	}

	@Override
	public int getAudioSpectrumThreshold()
	{
		return 0; 
	}

	@Override
	public Double getCurrentTime()
	{
		return null; 
	}

	@Override
	public String getMediaLocation()
	{
		return null; 
	}

	@Override
	public Object getPlayer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * LibGDX unused methods
	 * 
	 * pause and resume come from the interface we need to gracefully close a
	 * libGDX visualization window
	 */
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pauseTrack()
	{
		child.pause1();
	}

	@Override
	public void playlistReady()
	{
		if ( !wrapperRunning && visApp == null)
		{

			// main(new String[]
			// { this.mediaLocation });

			cfg.title = "AudioVisualizer";
			cfg.useGL20 = false;
			// do the following width and height settings
			// even get applied?
			cfg.width = WIDTH;
			cfg.height = HEIGHT;
			cfg.forceExit = false;
			childP = this;
			// Run the AudioVisualizer under the watch of the JavaFX thread
			// (i.e. put into thread queue)
			Platform.runLater (new Runnable()
			{
				@Override
				public void run()
				{

					child = new AudioVisualizer (mediaLocation);

					// do not close the application stage on exit
					visApp = new LwjglApplication (child, cfg);
					child.setApp (visApp);
					child.setAudioSpectrumListener (audioSpectrumListener);
					visApp.addLifecycleListener (childP);
					child.setonEndOfMedia (endOfMediaEventHandler);

				}
			});
			Thread.yield();

			wrapperRunning = true;
		} else
		{
			stopTrack();
			child.setApp (visApp);
			child.setAudioSpectrumListener (audioSpectrumListener);
			visApp.addLifecycleListener (childP);
			child.setonEndOfMedia (endOfMediaEventHandler);

			// Thread.sleep(400); // how to keep libGDX player from misreading
			// header?
			child.newTrack (this.mediaLocation);
		}
	}

	@Override
	public void playTrack()
	{
		child.resume1();
	}

	/**
	 * LibGDX unused methods
	 * 
	 * pause and resume come from the interface we need to gracefully close a
	 * libGDX visualization window
	 */
	@Override
	public void resume()
	{
	}

	@Override
	public boolean setAudioSpectrumInterval(double interval)
	{
		return false;
	}

	// libGDX LIFECYCLE LISTENERS

	@Override
	public boolean setAudioSpectrumNumBands(int numBands)
	{
		return false;
	}

	@Override
	public boolean setAudioSpectrumThreshold(int threshold)
	{
		return false;
	}

	/**
	 * Set AudioVisualizer
	 * 
	 * @param child
	 *            - the AudioVisualizer instance used as a GDX app
	 */
	public void setchild(AudioVisualizer child)
	{
		this.child = child;
	}

	@Override
	public void setMediaLocation(String mediaLocation)
	{
		this.mediaLocation = mediaLocation;
	}

	@Override
	public void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler)
	{
		this.endOfMediaEventHandler = tavEndOfMediaEventHandler;

	}

	@Override
	public void stopTrack()
	{
		if (child != null)
			child.stop();
	}

	/*********************************NOTE****************************/
	
	/*
	 * (non-Javadoc)
	 * Because of the way the libGDX app is built, I think the only way to
	 * achieve the functionality below is to call this parent from the
	 * child (AudioVisualizer.java) object.  Trying to call the child object
	 * before it is build will give a series of null pointer errors.  So
	 * if at all, access the data given here from inside of the child as
	 * there is not much chance it can be null at that point!
	 * 
	 */
	
	@Override
	public void setAudioSpectrumListener(
			TavAudioSpectrumListener audioSpectrumListener)
	{
		this.audioSpectrumListener = audioSpectrumListener;
	}

	@Override
	public TavMetaData getMetaData()
	{
		return null;
	}

	@Override
	public void setWidth(double width)
	{
		this.WIDTH = (int) width;
	}

	@Override
	public void setHeight(double height)
	{
		this.HEIGHT = (int) height;
	}
	
	/*********************************NOTE****************************/
	
	/*
	 * (non-Javadoc)
	 * Because of the way the libGDX app works, I think the only way to
	 * achieve the functionality below is to call this parent from the
	 * child (AudioVisualizer.java) object.  Trying to call the child object
	 * before it is build will give a series of null pointer errors.  So
	 * if at all, access the data given here from inside of the child as
	 * there is not much chance it can be null at that point!
	 * 
	 */

	@Override
	public void setMetaDataListener(TavMetaDataListener tavMetaDataListener)
	{
		this.metaDataListener = (tavMetaDataListener);
	}

	@Override
	public TavVisualization getVisualization()
	{
		// TODO Auto-generated method stub
		return child;
	}

	@Override
	public boolean isCustomizable()
	{
		return true;
	}

	@Override
	public void setVisualizationIndex(Number index)
	{
		this.visualizationIndex = index.intValue();
		
		child.changeVisualization(index.intValue());
	}
}
