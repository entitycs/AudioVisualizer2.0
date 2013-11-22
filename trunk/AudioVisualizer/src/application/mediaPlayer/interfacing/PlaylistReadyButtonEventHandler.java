package application.mediaPlayer.interfacing;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import listeners.PlaylistReadyListener;

public class PlaylistReadyButtonEventHandler implements EventHandler<Event>
{
	private PlayButtonEventHandler resumeHandler;
	private PlaylistReadyListener playlistReadyListener;

	/**
	 * Constructor for 'PlaylistReady / Resume' button
	 */
	public PlaylistReadyButtonEventHandler(
			PlaylistReadyListener playlistReadyListener,
			PlayButtonEventHandler resumeHandler)
	{
		this.resumeHandler = resumeHandler;
		this.playlistReadyListener = playlistReadyListener;
	}

	@Override
	public void handle(Event event)
	{
		AnchorPane playButton;

		playButton = ((AnchorPane) event.getSource());

		playlistReadyListener.playlistReady();

		// for the first click, the event is 'playlist ready,' but
		// until the song is changed, the subsequent click events are 'resume'
		playButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);

		// set the resumeHandler for the play button
		playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
	}
}
