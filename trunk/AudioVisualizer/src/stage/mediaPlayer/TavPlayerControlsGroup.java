package stage.mediaPlayer;

import stage.mediaPlayer.event.TavNextButtonEventHandler;
import stage.mediaPlayer.event.TavPauseButtonEventHandler;
import stage.mediaPlayer.event.TavPlayButtonEventHandler;
import stage.mediaPlayer.event.TavPlaylistReadyButtonEventHandler;
import stage.mediaPlayer.event.TavPrevButtonEventHandler;
import stage.mediaPlayer.event.TavStopButtonEventHandler;
import stage.mediaPlayer.listener.TavPlayerControlsListener;
import stage.playlist.listener.TavPlaylistReadyListener;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This object defines the behavior of the media player controls located on the
 * application stage.
 * 
 * @author entitycs
 * 
 */
public class TavPlayerControlsGroup implements TavPlayerControlsListener,
		TavPlaylistReadyListener
{

	private Button prevButton;
	private Button playButton;
	private Button pauseButton;
	private Button nextButton;
	private Button stopButton;

	private TavStopButtonEventHandler stopHandler;
	private TavPlaylistReadyButtonEventHandler playlistReadyHandler;
	private TavPlayButtonEventHandler resumeHandler;
	private TavPlayerControlsListener playerControlsListener;
	private TavPlaylistReadyListener playlistReadyListener;

	public TavPlayerControlsGroup(Button Button, Button Button2,
			Button Button3, Button Button4, Button Button5)
	{
		this.prevButton = Button;
		this.playButton = Button2;
		this.nextButton = Button3;
		this.pauseButton = Button4;
		this.stopButton = Button5;

		// Because the stage is being built, requesting the focus
		// will not work yet. Use Platform.runLater to request
		// focus "later."
		Platform.runLater (new Runnable()
		{
			@Override
			public void run()
			{
				stopButton.requestFocus();
			}
		});

	}

	/**
	 * Initialize Handlers
	 * 
	 * Any events coming from the primary application stage have handlers which
	 * are set in this function. This method should be called after the first
	 * initialization of the object.
	 * 
	 * This function is necessary in order to let the event handlers refer
	 * statically to the application manager.
	 * 
	 * post-revision: 623 - moved setting of event handlers outside of
	 * constructor.
	 * 
	 * @param playlistReadyListener
	 *            is the object which decides what to do in the event that the
	 *            playlist is ready (i.e. new media is about to be played).
	 * 
	 * @param playerControlsListener
	 *            is the object which decides what to do with player control
	 *            events in application.event (next, play, pause, etc.).
	 */
	public void initHandlers(
			final TavPlaylistReadyListener playlistReadyListener,
			final TavPlayerControlsListener playerControlsListener)
	{
		this.playerControlsListener = playerControlsListener;
		this.playlistReadyListener = playlistReadyListener;

		this.resumeHandler = new TavPlayButtonEventHandler (this);

		this.playlistReadyHandler = new TavPlaylistReadyButtonEventHandler (
				this);

		this.stopHandler = new TavStopButtonEventHandler (this);

		this.playButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				this.playlistReadyHandler);

		this.stopButton.addEventHandler (MouseEvent.MOUSE_CLICKED, stopHandler);

		this.prevButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				new TavPrevButtonEventHandler (this));

		this.nextButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				new TavNextButtonEventHandler (this));

		this.pauseButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				new TavPauseButtonEventHandler (this));

	}

	private void discourageQuickClicking()
	{
		try
		{
			Thread.sleep (200);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reset Play Handler
	 * 
	 * This method is called when the play button is to function not as 'resume'
	 * but as 'load media and play from beginning after updating from settings'
	 */
	public void resetPlayHandler()
	{

	}

	@Override
	public boolean prev()
	{
		resetStopHandler();
		if ( !this.playerControlsListener.prev())
		{
			return false;
		}
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		discourageQuickClicking();
		return true;
	}

	@Override
	public boolean play()
	{
		if ( !this.playerControlsListener.play())
		{
			return false;
		}
		resetStopHandler();
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		discourageQuickClicking();
		return true;
	}

	@Override
	public boolean next()
	{
		if ( !this.playerControlsListener.next())
		{
			return false;
		}
		resetStopHandler();
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		discourageQuickClicking();
		return true;
	}

	@Override
	public void pause()
	{
		this.playerControlsListener.pause();
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		discourageQuickClicking();
	}

	@Override
	public void stop()
	{
		this.playerControlsListener.stop();
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.addEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		// remove the stop button event handler (until another button resets it)
		stopButton.removeEventHandler (MouseEvent.MOUSE_CLICKED, stopHandler);
		discourageQuickClicking();
	}

	@Override
	public void playlistReady()
	{

		// re-apply the stop event handler in the case the user pressed stop
		// previously, thereby removing the stop handler from the stop button.
		resetStopHandler();

		this.playlistReadyListener.playlistReady();
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				resumeHandler);
		this.playButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				playlistReadyHandler);
		discourageQuickClicking();
	}

	private void resetStopHandler()
	{
		stopButton.removeEventHandler (MouseEvent.MOUSE_CLICKED,
				this.stopHandler);
		stopButton.addEventHandler (MouseEvent.MOUSE_CLICKED, this.stopHandler);
	}

}
