package application.mediaPlayer.listener;

import stage.playlist.listener.TavPlaylistReadyListener;

/**
 * An object implementing this interface shall handle end of media events for
 * any attached media player or combination player instance.
 */
public interface TavEndOfMediaListener extends TavPlaylistReadyListener
{
	/**
	 * Simulate stopping the media. This is useful for visualizer + media player
	 * combination instances. When such a window is closed, the application
	 * shall proceed as if the stop button was pressed.
	 */
	public void simulateStop();

	/**
	 * playlistReady after an end of media event should require the next song to
	 * play. If this is not intended, set the parameter to false.
	 * 
	 * @param keepPlaying
	 *            when set to false keeps the media player from continuing on to
	 *            the next track, but allows all other playlistReady events
	 *            preceding to occur.
	 */
	public void playlistReady(boolean keepPlaying);
}