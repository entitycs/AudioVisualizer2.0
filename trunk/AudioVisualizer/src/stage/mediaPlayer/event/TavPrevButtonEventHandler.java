/**
 * 
 */
package stage.mediaPlayer.event;

import stage.mediaPlayer.listener.TavPlayerControlsListener;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Handles 'previous' (<<) button events.
 * 
 */
public class TavPrevButtonEventHandler implements EventHandler<Event>
{
	private TavPlayerControlsListener playerControlsListener;

	public TavPrevButtonEventHandler(
			final TavPlayerControlsListener pauseHandler)
	{
		this.playerControlsListener = pauseHandler;
	}

	@Override
	public void handle(Event event)
	{
		this.playerControlsListener.prev();
	}

}
