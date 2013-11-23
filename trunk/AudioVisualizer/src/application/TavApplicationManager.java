package application;

import java.io.IOException;

import exception.TavUnimplementedFunctionalityException;
import application.listener.PlayerControlsEventListener;
import application.listener.PlaylistReadyListener;
import application.listener.TavAudioSpectrumListener;
import application.visualizer.interfacing.VisualizerControlsEventListener;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The TavApplicationManager communicates with the media player and visualizer
 * managers as well as with the application stage (GUI w/o visualization window)
 */
public class TavApplicationManager implements TavAudioSpectrumListener,
		PlaylistReadyListener
{
	private final TavMediaPlayerManager mediaPlayerManager;
	private final TavVisualizerManager visualizerManager;
	private final TavSettingManager settingManager;

	// not being used functionaly. Will remove soon
	public enum Status
	{
		STOPPED, PLAYING, PAUSED
	};

	private Status currentStatus = Status.STOPPED;

	private TavApplicationManager()
	{
		this.mediaPlayerManager = new TavMediaPlayerManager();
		this.visualizerManager = new TavVisualizerManager();
		this.settingManager = new TavSettingManager();
	}

	/**
	 * A static, randomly named singleton inner class implementation
	 */
	private static class x2jl98j_r$$3
	{
		private static final TavApplicationManager INSTANCE = new TavApplicationManager();
	}

	/**
	 * @return the singleton instance of the TavApplicationManager object
	 */
	protected static TavApplicationManager getInstance()
	{
		return x2jl98j_r$$3.INSTANCE;
	}

	/**
	 * @param primaryStage
	 *            is the JavaFX primary stage (visualizer window not included)
	 * @throws IOException
	 */
	public void setStage(Stage primaryStage) throws IOException
	{
		PlaylistReadyListener t = this;
		PlayerControlsEventListener a = mediaPlayerManager;
		VisualizerControlsEventListener v = visualizerManager;

		// instantiate the application stage and initialize its event handlers
		TavApplicationStage.getInstance().initHandlers(t, a, v);

		// pass the application scene to the primary stage
		primaryStage.setScene(TavApplicationStage.getAppScene());

		// show the primary stage
		primaryStage.show();
	}

	/**
	 * Playlist Ready
	 * 
	 * This method is called once the user has pressed play after building a
	 * playlist with one or more songs.
	 * 
	 * @param playlist
	 */
	public void playlistReady()
	{
		// or, if the user has no songs in the playlist
		if (TavApplicationStage.getInstance().getPlaylist().size() == 0)
		{
			// do nothing (return)
			return;
		}
		System.out.println("PlaylistReady (app manager)");
		// inform the sub management layer that the playlist is ready

		updateComponents();

		this.visualizerManager.playlistReady();

		this.mediaPlayerManager.playlistReady();
	}

	/**
	 * Update Components
	 * 
	 * Any time PlaylistReady is called, we need to update the
	 */
	private void updateComponents()
	{

		// let the mediaPlayerManager decide on whether or not to update
		// the visualizer for now

		// updatePlayer
		this.mediaPlayerManager.updatePlayer();
	}

	/**
	 * Set Status
	 * 
	 * The sub management layer should set the status but this is not being
	 * enforced as of now
	 * 
	 * @param statusUpdate
	 *            is the status the application will be changed to
	 */
	public void setStatus(Status statusUpdate)
	{
		this.currentStatus = statusUpdate;
	}

	/**
	 * Get Status
	 * 
	 * @return the current application status
	 */
	public Status getStatus()
	{
		return this.currentStatus;
	}

	/**
	 * Get Media Player Manager
	 * 
	 * The application manager should be aware of any time the media player
	 * manager needs the visualizer manager and vice versa.
	 * 
	 * @return the system media player manager
	 */
	public TavMediaPlayerManager getMediaPlayerManager()
	{
		return this.mediaPlayerManager;
	}
	
	/**
	 * Get Visualizer Manager
	 * 
	 * The application manager should be aware of any time the visualizer
	 * manager needs the media player manager and vice versa.
	 * 
	 * @return the system visualizer manager
	 */
	public TavVisualizerManager getVisualizerManager()
	{
		return this.visualizerManager;
	}

	/**
	 * Get Setting Manager
	 * 
	 * The application manager should be aware of any time the visualizer
	 * manager needs the media player manager and vice versa.
	 * 
	 * @return the system setting manager
	 */
	public TavSettingManager getSettingManager()
	{
		return this.settingManager;
	}
	
	/**
	 * Spectrum data update given short[] magnitudes
	 * 
	 * (CAN BE USED TO ALTER LIBGDX SPECTRUM VALUES)
	 * 
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param phases
	 *            - note: these are really samples but how to use them?
	 */
	@Override
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, short[] phases)
	{
		// Platform.runLater( new
		// TavSpectrumDataUpdater(timestamp,duration,magnitudes,phases,
		// timestamp - this.mediaPlayerManager.getCurrentTime()));
		for (double i = 0; i < 50; i++)
			magnitudes[(int) i] /= 10.0;
		TavApplicationStage.getInstance().consoleFloatArray(magnitudes);
	}

	/**
	 * Spectrum data update given float[] magnitudes
	 * 
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param phases
	 */
	@Override
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases)
	{
		try
		{
			Platform.runLater(new TavSpectrumDataUpdater(timestamp, duration,
					magnitudes, phases, timestamp
							- this.mediaPlayerManager.getCurrentTime()));
			
		} catch (TavUnimplementedFunctionalityException e)
		{
			System.err
					.println("Error: The visualization listener will now be detached from the media player.");

			e.printStackTrace();
			mediaPlayerManager.getMediaPlayer().setAudioSpectrumListener(null);
		}
	}
}
