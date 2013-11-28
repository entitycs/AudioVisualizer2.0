package application.mediaPlayer.event;

import application.mediaPlayer.listener.TavEndOfMediaListener;

/**
 * The end of media handler for the application.
 * 
 * The media player instance should use the TavMediaPlayer interface which will
 * provide the method and this event handler. The media player instance shall
 * run this object when it has finished playing the current media.
 * 
 * PlaylistReadyListener suffices as the 'EndOfMediaListener' for now, so there
 * is no need for an 'EndOfMediaListener' at the moment.
 * 
 */
public class TavEndOfMediaEventHandler implements Runnable // extends
{
	private TavEndOfMediaListener listener;

	/**
	 * Constructor
	 * 
	 * The application manager or any other 'PlaylistReady' interfacing object
	 * is to be provided in the constructor.
	 * 
	 * @param listener
	 *            is the PlaylistReadyListener which will be notified of the end
	 *            of media event. The playlistReady() function of this object
	 *            will be called upon notification.
	 */
	public TavEndOfMediaEventHandler(TavEndOfMediaListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void run()
	{
		this.listener.playlistReady (true);
	}

	/**
	 * A convenience method used to call the simulateStop method of this
	 * listener.
	 */
	public void playerClosed()
	{
		listener.simulateStop();
	}
}
