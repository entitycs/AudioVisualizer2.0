package application;

import java.net.MalformedURLException;

import stage.TavApplicationStage;
import stage.mediaPlayer.listener.TavPlayerControlsListener;
import stage.playlist.listener.TavPlaylistReadyListener;
import application.exception.TavUnimplementedFunctionalityException;
import application.mediaPlayer.event.TavEndOfMediaEventHandler;
import application.mediaPlayer.interfacing.TavMediaPlayer;
import application.mediaPlayer.listener.TavMetaDataListener;
import audiovisualizer.fx.mediaPlayer.TavMediaPlayerFX;
import audiovisualizer.gdx.AudioVisualizerPlayer;

/**
 * Think of the media player manager as a mechanical 'arm' in an advanced
 * jukebox, which itself has a manager - the application manager, and a playlist
 * (along with plenty of other things not mentioned here).
 * 
 * What follows are system requirements, then functionalities of the media
 * player manager are listed.
 * 
 * Requirements:
 * 
 * 1) You can put any working media player inside of the jukebox so long as it
 * can i.) play the given media, ii.) pause, and iii.) stop.
 * 
 * 2) The media player in place should not need to know the contents of the
 * playlist other than what it is currently being asked to play.
 * 
 * 3) The application manager needs to know the type of media player being used
 * (either stand-alone or combination--visualizer-included).
 * 
 * 4) The application manager needs to know what advanced functionality the
 * media player in place can or cannot provide.
 * 
 * 5) The media player can be swapped out but treated equally in the context of
 * the application manager.
 * 
 * Requirements 3 and 4 are required before and each time any media is provided
 * to the media player, as requirement 5 means the media player may have
 * changed.
 * 
 * Functions:
 * 
 * The media player manager (i.e. the mechanical jukebox arm ), can ...
 * 
 * 1) grab (or be handed) the next song to be played. It then feeds the selected
 * song to the media player. Additionally...
 * 
 * 2) The mechanical arm can read the type of media player which has been put in
 * place and report this information to the system managing this arm.
 * 
 * Since the media player in use may change in between fetching the next song,
 * this information is provided to the application manager before the media is
 * played.
 * 
 * 3) The system managing this arm can direct the arm to put a known media
 * player type into place.
 * 
 * 4) The arm attempts to interface advanced functionality with the media player
 * in place (functionality beyond the three requirements listed above) and it
 * throws exceptions if an attempt is made to use advanced interfaced methods
 * that the currently attached media player does not support.
 * 
 *                       _______
 *                  _.-'\       /'-._
 *              _.-'    _\ .-. /_    '-._
 *           .-'    _.-' |/.-.\| '-._    '-.
 *         .'    .-'    _||   ||_    '-.    '.
 *        /    .'    .-' ||___|| '-.    '.    \
 *       /   .'   .-' _.-'-----'-._ '-.   '.   \
 *      /   /   .' .-'  ___ARM____ '-. '-. '.   \
 *     /   /   / .' ~  |=|-------.|   '-.\   \   \
 *    /   /   /.'...   |.|       ||     '.\   \   \
 *    |  /   //....    |.|    |~SONG~|    \\   \  |
 *    |  |  |/....     |.|                 \|  |  |
 *   .--.|__||___      | |'--------------'__ _||.--.
 *   .'   '----. .-----| || Media Player | ---'   '.
 *   (_________________| |__________________________)
 *   |______________________________________________|
 *  '.________' |~SONG~|    V    |~|==|~| '________.'
 *  .'--------. |~|==|~|    V    |~SONG~| .--------'.
 *  '.________' |~SONG~|PLAY:LIST|~|==|~| '________.'
 *  .'--------. |~|==|~|    V    |~SONG~| .--------'.
 *  '.________' |~SONG~|    V    |~|==|~| '________.'
 *   (______________________:______________________)
 *    |===========================================|
 *    |  |  ||  ____ | | | |:| | | | ____  ||  |  |
 *    |  |  || |    || | | |:| | | ||    | ||  |  |
 *    |  |  || |____||AUDIO : DATA ||____| ||  |  |
 *    |  |  ||  |   /| | | |V| | | |\   |  ||  |  |
 *    |  |  ||  |_.` | | | |V| | | | `._|  ||  |  |
 *    |  |  || .---.-'-'-'-'V'-'-'-'-.---. ||  |  |
 *    |  |  || |   |\  /\  /V\  /\  /|   | ||  |  |
 *    |  |  || |   |~\/  \/ V \/  \/ |   | ||  |  |
 *    |  |  || |   | /\ ~/\ V /\ ~/\ |   | ||  |  |
 *    |  |  || |   |/  \/  \V/  \/ ~\|   | ||  |  |
 *    |  |  || |   |\~ /\~ /V\~ /\  /|   | ||  |  |
 *    |  |  || |   | \/  \/ V \/  \/ |   | ||  |  |
 *    |  |  || |   | /\~ /\ V /\ ~/\ |   | ||  |  |
 *    |  |  || |===|/  \/  .V.  \/  \|===| ||  |  |
 *    |  |  || |   | ~ /\ ( * ) /\ ~ |   | ||  |  |
 *    |  |  || |    \|AUDIO : DATA |/    | ||  |  |
 *    /._|__||  \ V  \ ~ /\ ~ /\~  /  R /  ||__|_.-\
 *    |._/__||   \ I  './  .-.  \.'  E /   |\__\_.-|
 *    | | | ||    '._S   '-| |-'   Z_.'    ||  | | |
 *    | | | ||      '._U   | |  I _.'      ||  | | |
 *    | | | ||         '-.A| |L.-'         ||  | | |
 *    | | | ||             | |             ||  | | |
 *    | | | ||             |_|             ||  | | |
 *    '.|_|_||_____ _______________________||__|_|.'
 *    |  |   |-----------------------------|   |  |
 *    |  |   [_____________________________]   |  |
 *    |  |   |/                           \|   |  |
 *    '._|__.'                             '.__|_.'
 *
 */
public class TavMediaPlayerManager implements TavPlaylistReadyListener,
		TavPlayerControlsListener
{

	private TavMediaPlayer mediaPlayer;

	private AudioVisualizerPlayer comboPlayer;

	private TavMediaPlayerFX standAlonePlayer;

	private boolean usingComboPlayer;

	public TavMediaPlayerManager()
	{

	}

	/**
	 * Combo visualizer player
	 * 
	 * By default, the system runs on a libGDX application which encompasses
	 * both visualizations and audio.
	 * 
	 * @return the primary visualizer + media player combination.
	 */
	private TavMediaPlayer comboVisualizerPlayer()
	{
		this.usingComboPlayer = true;
//		if (this.comboPlayer != null)
//			this.comboPlayer = this.comboPlayer;
//		// this.comboPlayer.dispose();
//		else
			this.comboPlayer = new AudioVisualizerPlayer();

		this.standAlonePlayer = null;

		return this.comboPlayer;
	}

	/**
	 * Audio Equalizer
	 * 
	 * Return the system media player's audio equalizer if one exists.
	 * 
	 * @return the audio equalizer being used by the system.
	 * 
	 * @throws NullPointerException
	 *             if no audio equalizer is currently in use
	 */
	public Object getAudioEqualizer()
			throws TavUnimplementedFunctionalityException
	{
		if (this.mediaPlayer.getAudioEqualizer() == null)

			throw new TavUnimplementedFunctionalityException (
					"The media player in use did not provide an audio equalizer.");
		else
			return this.mediaPlayer.getAudioEqualizer();
	}

	/**
	 * Current Time
	 * 
	 * Return the current time as reported by the media player if the media
	 * player has implemented this ability
	 * 
	 * @return the current media time in seconds, or 0.0 if the media is not
	 *         playing or if the media player in use does not provide this
	 *         function.
	 */
	public double getCurrentTime()
			throws TavUnimplementedFunctionalityException
	{
		if (this.mediaPlayer.getCurrentTime() == null)

			throw new TavUnimplementedFunctionalityException (
					"The media player in use did not provide a current time.");

		return mediaPlayer.getCurrentTime().doubleValue();
	}

	public String getMediaLocation()
	{
		return TavApplicationStage.getInstance().getPlaylist().current();
	}

	/**
	 * Get the media player in use
	 * 
	 * @return the media player in use
	 */
	public TavMediaPlayer getMediaPlayer()
	{
		return this.mediaPlayer;
	}

	/**
	 * Play the loaded track from the beginning
	 * 
	 * @throws MalformedURLException
	 */
	private void playLoadedFromBeginning(final String mediaLocation)
			throws MalformedURLException, IllegalStateException
	{
		// return if we have loaded null
		if (mediaLocation == null)
			return;

		if (this.mediaPlayer == null)
			throw new IllegalStateException (
					"An attempt to play media was made when no media player was set");

		// stop whatever track is playing, if any
		// this.mediaPlayer.stopTrack();

		if (mediaPlayer != null)
		{
			this.mediaPlayer.stopTrack();
			this.mediaPlayer.setMediaLocation (TavApplicationStage
					.getInstance().getPlaylist().current());
			this.mediaPlayer.setOnEndOfMedia (new TavEndOfMediaEventHandler (
					TavApplicationManager.getInstance()));
			TavMetaDataListener metaDataListener = TavApplicationStage.getInstance ();
			this.mediaPlayer.setMetaDataListener (metaDataListener);
			this.mediaPlayer.setAudioSpectrumListener (TavApplicationManager
					.getInstance());

			this.mediaPlayer.playlistReady();
		} else{ System.err.println("Tried to play media when no media player was built.  Reverting...");
			TavApplicationManager.getInstance().playlistReady();
		}

		if ( !this.usingComboPlayer)
		{

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
			this.mediaPlayer.setAudioSpectrumInterval (TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getInterval());
			this.mediaPlayer.setAudioSpectrumNumBands (TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getNumBands());
			this.mediaPlayer.setAudioSpectrumThreshold (TavApplicationManager
					.getInstance().getVisualizerManager().getVisualizer()
					.getVisualization().getThreshold());
		}

	}
	
	/**
	 * sets up the next song for the 1-file per instance media player
	 */
	public boolean next()
	{
		try
		{
			String mediaLocation = TavApplicationStage.getInstance()
					.getPlaylist().next();
			if (mediaLocation == null)
				return false;
			playLoadedFromBeginning (mediaLocation);

		} catch (MalformedURLException e)
		{
			System.err.println ("Error initiating next track in playlist");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void pause()
	{
		if (mediaPlayer != null)
			mediaPlayer.pauseTrack();
	}

	public boolean play()
	{
		String mediaLocation = TavApplicationStage.getInstance()
				.getPlaylist().current();

		if (mediaLocation == null)
			return false;

		mediaPlayer.setMediaLocation (mediaLocation);

		mediaPlayer.playTrack();

		return true;
	}

	@Override
	public void playlistReady()
	{
		String mediaLocation = TavApplicationStage.getInstance()
				.getPlaylist().current();

		if (mediaLocation == null)
			return;
		// Because a song which is currently playing may not be located
		// at the same index it was when playback began - for instance if
		// a song is deleted from the playlist and was located at an index
		// lower the the index of the currently playing song.
		updatePlayer();

		try
		{
			playLoadedFromBeginning (mediaLocation);
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean prev()
	{
		try
		{
			playLoadedFromBeginning (TavApplicationStage.getInstance()
					.getPlaylist().prev());

		} catch (MalformedURLException e)
		{
			System.err
					.println ("Error initiating previous track at index in playlist");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setAudioSpectrumInterval(double interval)
	{
		this.mediaPlayer.setAudioSpectrumInterval (interval);
	}

	/**
	 * @param threshold
	 */
	public void setAudioSpectrumThreshold(int threshold)
	{
		this.mediaPlayer.setAudioSpectrumThreshold (threshold);
	}

	/**
	 * Set media player and type based on application manager setting
	 */
	private void setMediaPlayerAndType()
	{
		if (TavApplicationManager.getInstance().getSettingManager()
				.getPlayerSetting().equals ("Default"))

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
	public void setPlayer(TavMediaPlayer mediaPlayer)
	{
		this.mediaPlayer = mediaPlayer;
	}

	/**
	 * @param spectrum
	 */
	public void spectrumUpdate(float[] spectrum)
	{
		TavApplicationStage.getInstance().consoleFloatArray (spectrum);
	}

	/**
	 * Use the standalone media player
	 * 
	 * @return the standAlone media player to be used
	 */
	private TavMediaPlayer standAloneMediaPlayer()
	{
		this.usingComboPlayer = false;
		if (this.standAlonePlayer == null)
			this.standAlonePlayer = new TavMediaPlayerFX();

		this.comboPlayer = null;

		return this.standAlonePlayer;
	}

	public void stop()
	{
		if (mediaPlayer != null)
			mediaPlayer.stopTrack();

		// TavApplicationStage.getInstance().getPlaylist().prev();
	}

	public void updatePlayer()
	{
		setMediaPlayerAndType();
	}

	/**
	 * Using Combo Player
	 * 
	 * @return true if the visualizer is part of the media player. In such
	 *         cases, the native visualizer should not be loaded
	 */
	public boolean usingComboPlayer()
	{
		return this.usingComboPlayer;
	}
}
