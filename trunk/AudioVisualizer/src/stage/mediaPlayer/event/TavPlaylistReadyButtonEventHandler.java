package stage.mediaPlayer.event;

import stage.playlist.listener.TavPlaylistReadyListener;
import javafx.event.Event;
import javafx.event.EventHandler;

public class TavPlaylistReadyButtonEventHandler implements EventHandler<Event>
{

	private TavPlaylistReadyListener playlistReadyListener;

	/**
	 * Constructor for 'PlaylistReady' button. The playlist ready button is the
	 * 'Play' button when the 'Play' button is not functioning as 'Resume'
	 */
	public TavPlaylistReadyButtonEventHandler(
			TavPlaylistReadyListener playlistReadyListener)
	{
		this.playlistReadyListener = playlistReadyListener;
	}

	@Override
	public void handle(Event event)
	{
		playlistReadyListener.playlistReady();
	}
}
