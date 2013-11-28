package stage.playlist.listener;

/**
 * Various parts of the application need to be notified when the playlist is
 * ready. Objects using this interface either dispatch or receive the event.
 */
public interface TavPlaylistReadyListener
{
	/**
	 * Called when the playlist is ready to supply the current media to the
	 * media player in use.
	 */
	public void playlistReady();

}
