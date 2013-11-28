package stage.menuBar.event;

import stage.menuBar.listener.TavRemoveSelectedMenuItemListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TavRemoveSelectedMenuItemEventHandler implements
		EventHandler<ActionEvent>
{
	private TavRemoveSelectedMenuItemListener listener;

	public TavRemoveSelectedMenuItemEventHandler(TavRemoveSelectedMenuItemListener listener){
		this.listener = listener;
	}
	
	@Override
	public void handle(ActionEvent arg0)
	{
		this.listener.removeSelected ();
	}

}
