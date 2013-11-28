/**
 * 
 */
package stage.mediaPlayer.event;

import stage.mediaPlayer.listener.TavPlayerControlsListener;
import javafx.event.Event;
import javafx.event.EventHandler;

public class TavNextButtonEventHandler implements EventHandler<Event>
{
	private TavPlayerControlsListener playerControlsListener;

	public TavNextButtonEventHandler(
			final TavPlayerControlsListener pauseHandler)
	{
		this.playerControlsListener = pauseHandler;
	}

	@Override
	public void handle(Event event)
	{
		this.playerControlsListener.next();
	}

}
