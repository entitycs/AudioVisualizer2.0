/**
 * 
 */
package application.event;

import application.listener.PlayerControlsEventListener;
import javafx.event.Event;
import javafx.event.EventHandler;


public class NextButtonEventHandler implements EventHandler<Event>
{
	private PlayerControlsEventListener playerControlsListener;
	
	public NextButtonEventHandler(final PlayerControlsEventListener pauseHandler){
		this.playerControlsListener = pauseHandler;
	}
	
	@Override
	public void handle(Event event)
	{	
			this.playerControlsListener.next();
	}

}
