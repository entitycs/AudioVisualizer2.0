package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import stage.TavApplicationStage;
import stage.mediaPlayer.listener.TavPlayerControlsListener;
import stage.playlist.listener.TavPlaylistReadyListener;
import application.exception.TavUnimplementedFunctionalityException;
import application.mediaPlayer.listener.TavAudioSpectrumListener;
import application.mediaPlayer.listener.TavEndOfMediaListener;
import application.setting.listener.TavMediaPlayerChoiceSettingListener;
import application.setting.listener.TavVisualizerDimensionSettingListener;
import application.visualizer.listener.TavVisualizerControlsListener;

/**
 * The TavApplicationManager communicates with the component management layer
 * which includes the media player, visualizer, and settings managers).
 */
public class TavApplicationManager implements TavAudioSpectrumListener,
		TavPlaylistReadyListener, TavEndOfMediaListener,
		EventHandler<WindowEvent>
{
	private TavMediaPlayerManager mediaPlayerManager;
	private TavVisualizerManager visualizerManager;
	private TavSettingsManager settingManager;

	private TavApplicationManager()
	{

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
	 * Do not attempt to initialize inside of constructor, as the managers
	 * in this function depend on this object having already been constructed.
	 */
	protected void initialize(){
		this.mediaPlayerManager = new TavMediaPlayerManager();
		this.visualizerManager = new TavVisualizerManager();
		if (this.settingManager == null)
		{
			this.settingManager = new TavSettingsManager();
			try
			{
				this.settingManager.loadFromSettingsFile();
			} catch (IOException e)
			{
				System.err.println ("Unable to load settings from file");
				e.printStackTrace();
			}
		}
		TavVisualizerDimensionSettingListener g = this.settingManager;
		TavMediaPlayerChoiceSettingListener o = this.settingManager;
		// instantiate the application stage and initialize its event handlers
		TavApplicationStage.getInstance()
				.setVisualizerDimensionSettingListener (g);
		TavApplicationStage.getInstance().setPlayerChoiceSettingListener (o);
		this.mediaPlayerManager.updatePlayer ();
	}
	
	/**
	 * @param primaryStage
	 *            is the JavaFX primary stage (visualizer window not included)
	 * @throws IOException
	 */
	protected void setStage(Stage primaryStage) throws IOException
	{
		
		TavEndOfMediaListener t = this;
		TavPlayerControlsListener a = mediaPlayerManager;
		TavVisualizerControlsListener v = visualizerManager;
		TavApplicationStage.getInstance().initHandlers (t, a, v);

		// pass the application scene to the primary stage
		primaryStage.setScene (TavApplicationStage.getAppScene());

		// show the primary stage
		primaryStage.show();
	}

	/**
	 * Playlist Ready
	 * 
	 * This method is called once the user has pressed play after building a
	 * playlist with one or more songs. It is also currently called when the
	 * media player instance in use implements the end of media event handler.
	 * 
	 */
	public void playlistReady()
	{
		// or, if the user has no songs in the playlist
		if (TavApplicationStage.getInstance().getPlaylist().size() == 0)
		{
			// do nothing (return)
			return;
		}

		// inform the sub management layer that the playlist is ready
		updateComponents();

		this.visualizerManager.playlistReady();
		this.mediaPlayerManager.playlistReady();
		this.visualizerManager.mediaPlayerReady();
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
	public TavSettingsManager getSettingManager()
	{
		if (this.settingManager == null)
		{
			this.settingManager = new TavSettingsManager();
			try
			{
				this.settingManager.loadFromSettingsFile();
			} catch (IOException e)
			{
				System.err.println ("Unable to load settings from file");
				e.printStackTrace();
			}
		}
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
		/*for (double i = 0; (i < 50 && i < magnitudes.length); i++)
			magnitudes[(int) i] /= (5.0 - i / 10) / 2;
		TavApplicationStage.getInstance().consoleFloatArray (magnitudes);*/
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
			Platform.runLater (new TavSpectrumDataUpdater (timestamp, duration,
					magnitudes, phases, timestamp
							- this.mediaPlayerManager.getCurrentTime()));

		} catch (TavUnimplementedFunctionalityException e)
		{
			System.err
					.println ("Error: The visualization listener will now be detached from the media player.");

			e.printStackTrace();
			mediaPlayerManager.getMediaPlayer()
					.setAudioSpectrumListener (null);
		}
	}

	@Override
	public void simulateStop()
	{
		TavApplicationStage.getInstance().getPlayerControls().stop();
	}

	@Override
	public void handle(WindowEvent arg0)
	{
		// stopping the media player runnables (not helping)
		if (this.mediaPlayerManager.getMediaPlayer() != null)
			this.mediaPlayerManager.getMediaPlayer().dispose();
		// still getting an error on exit which does not occur when
		// closing from eclipse.
	}

	@Override
	public void playlistReady(boolean b)
	{
		if ( !b)
			return;

		playlistReady();
		this.mediaPlayerManager.next();
	}
}
