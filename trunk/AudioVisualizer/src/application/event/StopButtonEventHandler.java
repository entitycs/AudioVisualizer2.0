/**
 * 
 */
package application.event;

import application.listener.PlayerControlsEventListener;
import javafx.event.Event;
import javafx.event.EventHandler;

public class StopButtonEventHandler implements EventHandler<Event>
{
	private PlayerControlsEventListener playerControlsListener;
	
	public StopButtonEventHandler(final PlayerControlsEventListener pauseHandler){
		this.playerControlsListener = pauseHandler;
	}
	
	@Override
	public void handle(Event event)
	{	
			this.playerControlsListener.stop();
	}

	public void simulateStop(){
		this.playerControlsListener.stop();
	}
	
}
