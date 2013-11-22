package application;

import java.io.IOException;

import application.mediaPlayer.interfacing.TavAudioSpectrumListener;
import application.visualizer.interfacing.VisualizerControlsEventListener;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import listeners.PlayerControlsEventListener;
import listeners.PlaylistReadyListener;

/**
 * The TavApplicationManager communicates with the media player and visualizer
 * managers as well as with the application stage (GUI w/o visualization window)
 */
public class TavApplicationManager implements TavAudioSpectrumListener, PlaylistReadyListener
{
	private final TavMediaPlayerManager mediaPlayerManager;
	private final TavVisualizerManager visualizerManager;

	// not being used functionaly.  Will remove soon
	public enum Status
	{
		STOPPED, PLAYING, PAUSED
	};

	private Status currentStatus = Status.STOPPED;
	private String usePlayerSetting = "Default";

	private TavApplicationManager()
	{
		this.mediaPlayerManager = new TavMediaPlayerManager();	
		this.visualizerManager = new TavVisualizerManager();
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
		TavApplicationStage.getInstance().initHandlers( t, a, v );

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
		if (TavApplicationStage.getInstance().getPlaylist().getItems().size() == 0)
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

	public TavMediaPlayerManager getMediaPlayerManager()
	{
		return this.mediaPlayerManager;
	}

	public TavVisualizerManager getVisualizerManager()
	{
		return this.visualizerManager;
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
		//Platform.runLater( new TavSpectrumDataUpdater(timestamp,duration,magnitudes,phases, timestamp - this.mediaPlayerManager.getCurrentTime()));
		for(double i = 0; i < 50; i++)
			magnitudes[(int)i] /= 10.0;
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
		Platform.runLater( new TavSpectrumDataUpdater(timestamp,duration,magnitudes,phases, timestamp - this.mediaPlayerManager.getCurrentTime()));
	}
	
	public ListView<String> getPlaylist(){
		return TavApplicationStage.getInstance()
				.getPlaylist();
	}

	public void widthSetting(double width)
	{
		this.visualizerManager.setWidth(width);
	}
	
	public void heightSetting(double height)
	{
		this.visualizerManager.setHeight(height);
	}

	public void playerSetting(String text)
	{
		// if there is a media player in action, stop it.
		if (this.mediaPlayerManager.getMediaPlayer() != null)
			this.mediaPlayerManager.stop();
		
		if (text.equals("Default")){
			this.usePlayerSetting = text;
		}
		else if (text.equals("Alternate")){
			this.usePlayerSetting = text;
		}
		// else if the text does not match, leave the current userPlayerSetting be
		
		this.mediaPlayerManager.updatePlayer();
	}

	public String getPlayerSetting()
	{
		// TODO Auto-generated method stub
		return usePlayerSetting;
	}
}
