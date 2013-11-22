package application;

import java.io.File;
import java.net.MalformedURLException;

import application.mediaPlayer.interfacing.TavAudioSpectrumListener;
import application.mediaPlayer.interfacing.TavEndOfMediaEventHandler;
import application.mediaPlayer.interfacing.TavMediaPlayer;
import visualizer.visualizerFX.TavMediaPlayerFX;
import visualizer.visualizerGDX.AudioVisualizerPlayer;
import javafx.scene.control.ListView;
import listeners.PlayerControlsEventListener;
import listeners.PlaylistReadyListener;

// P should be a media player implementing this interface

/**
 * @author
 */
public class TavMediaPlayerManager implements PlaylistReadyListener, PlayerControlsEventListener {
	protected int currentTrack = -1;
	protected String mediaLocation = null;
	private TavMediaPlayer mediaPlayer;
	private ListView<String> mediaList;

	private AudioVisualizerPlayer comboPlayer;
	private TavMediaPlayerFX standAlonePlayer;

	private TavAudioSpectrumListener audioSpectrumListener;
	
	private boolean usingComboPlayer;

	public TavMediaPlayerManager() {

	}

	/**
	 * This is not being used PUBLICLY , but maybe later in case we have a
	 * drop-down option and need an object from which to fetch a name
	 * 
	 * @return
	 */
	public TavMediaPlayer comboVisualizerPlayer() {
		this.usingComboPlayer = true;
		if (this.comboPlayer != null)
			this.comboPlayer.dispose();
		this.comboPlayer = new AudioVisualizerPlayer();

		this.standAlonePlayer = null;

		return this.comboPlayer;
	}

	public Object getAudioEqualizer() {
		return this.mediaPlayer.getAudioEqualizer();
	}

	public double getCurrentTime() {
		return mediaPlayer.getCurrentTime();
	}

	public String getMediaLocation() {
		return mediaLocation;
	}

	/**
	 * @return the media player in use
	 */
	public TavMediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}

	private void initiateTrack() throws MalformedURLException {
		if (this.mediaPlayer == null)
			return;
		this.mediaPlayer.stopTrack();
		audioSpectrumListener = TavApplicationManager.getInstance();
		
		// as some click events have
		System.err.println("Initiating track " + currentTrack);
		final File file = new File(mediaList.getItems().get(currentTrack));

		// a string pointing to the next song
		this.mediaLocation = file.toURI().toURL().toExternalForm()
				.replace("%20", " ");

		// this.mediaLocation = file.to
		System.err.println("Initiate track " + this.mediaLocation);
		if (mediaPlayer != null) {
			this.mediaPlayer.setMediaLocation(this.mediaLocation);
			this.mediaPlayer.setAudioSpectrumListener(this.audioSpectrumListener);
			//this.mediaPlayer.setAudioSpectrumLlistener(TavApplicationManager.getInstance());
			this.mediaPlayer.playlistReady();
		} else
			TavApplicationManager.getInstance().playlistReady();

		if (!this.usingComboPlayer) {

			// we assume that each time a new song is played, the previous
			// application media player has been destroyed
			// we also assume the user could change the media player instance
			// type (e.g. from libGDX to javaFX)
			// therefore, we always set the following options when a new track
			// is being loaded.

			// per visualization

			// the media player and visualizer should be on the same layer and
			// we have some control over them in
			// this management layer
			this.mediaPlayer.setAudioSpectrumListener(this.audioSpectrumListener);
			this.mediaPlayer.setAudioSpectrumInterval(TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getInterval());
			this.mediaPlayer.setAudioSpectrumNumBands(TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getNumBands());
			this.mediaPlayer.setAudioSpectrumThreshold(TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getThreshold());
		}

	}

	/**
	 * sets up the next song for the 1-file per instance media player
	 */
	public void next() {

		if (!(++currentTrack < mediaList.getItems().size()))
			currentTrack = 0; // TODO playlist finished. Now what? Repeat?
								// Close?

		try {
			Thread.sleep(500); // did not fix GDX header 'hangup'
			initiateTrack();
		} catch (MalformedURLException | InterruptedException e) {
			System.err.println("Error initiating next track at index "
					+ currentTrack + " in playlist");
			e.printStackTrace();
		}

	}

	public void pause() {
		mediaPlayer.pauseTrack();

	}

	public void play() {
		mediaPlayer.setMediaLocation(mediaLocation);
		mediaPlayer.playTrack();

	}

	@Override
	public void playlistReady() {
		System.err.println(TavApplicationManager.getInstance()
				.getPlayerSetting());

		this.mediaList = TavApplicationManager.getInstance().getPlaylist();

		updatePlayer();
		next();
	}

	public void prev() {

			--currentTrack;

			if (currentTrack < 0)
				currentTrack = 0;
			
		try {
			Thread.sleep(500);
			initiateTrack();
		} catch (MalformedURLException | InterruptedException e) {
			System.err.println("Error initiating previous track at index "
					+ currentTrack + " in playlist");
			e.printStackTrace();
		}
	}

	public void setAudioSpectrumInterval(double interval) {
		this.mediaPlayer.setAudioSpectrumInterval(interval);
	}

	/**
	 * @param threshold
	 */
	public void setAudioSpectrumThreshold(int threshold) {
		this.mediaPlayer.setAudioSpectrumThreshold(threshold);
	}

	/**
	 * Set media player and type based on application manager setting
	 */
	private void setMediaPlayerAndType() {
		if (TavApplicationManager.getInstance().getPlayerSetting()
				.equals("Default"))

			this.mediaPlayer = comboVisualizerPlayer();

		else
			this.mediaPlayer = standAloneMediaPlayer();
	}

	/**
	 * @param mediaPlayer
	 *            is the media player to be used
	 * 
	 *            may become deprecated
	 */
	public void setPlayer(TavMediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	/**
	 * @param spectrum
	 */
	public void spectrumUpdate(float[] spectrum) {
		TavApplicationStage.getInstance().consoleFloatArray(spectrum);
	}

	public TavMediaPlayer standAloneMediaPlayer() {
		this.usingComboPlayer = false;
		if (this.standAlonePlayer == null)
			this.standAlonePlayer = new TavMediaPlayerFX();

		this.comboPlayer = null;

		return this.standAlonePlayer;
	}

	public void stop() {
		mediaPlayer.stopTrack();
		TavApplicationStage.getInstance().resetPlayHandler();
		// decrement the currentTrack so that pressing play
		// (which does a pre-increment) will result in replaying the
		// stopped track
		--currentTrack;

		if (currentTrack < -1)
			currentTrack = -1;
	}

	public void updatePlayer() {
		setMediaPlayerAndType();
		
		// cohesion and coupling update
		// note the heavier use of interfaces
		PlaylistReadyListener manager = TavApplicationManager.getInstance();
		this.mediaPlayer.setOnEndOfMedia(new TavEndOfMediaEventHandler(manager));
	}

	/**
	 * Using Combo Player
	 * 
	 * @return true if the visualizer is part of the media player. In such
	 *         cases, the native visualizer should not be loaded
	 */
	public boolean usingComboPlayer() {
		return this.usingComboPlayer;
	}
}
