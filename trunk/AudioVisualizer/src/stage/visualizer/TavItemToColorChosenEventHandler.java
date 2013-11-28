package stage.visualizer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import application.visualizer.listener.TavVisualizerControlsListener;

public class TavItemToColorChosenEventHandler implements
		EventHandler<ActionEvent>
{

	private TavVisualizerControlsListener visualizerControlsListener;

	public TavItemToColorChosenEventHandler(
			TavVisualizerControlsListener visualizerControlsListener)
	{
		this.visualizerControlsListener = visualizerControlsListener;
	}

	@Override
	public void handle(ActionEvent event)
	{
		this.visualizerControlsListener
				.setColorItemColor ( ((ColorPicker) event.getSource())
						.getValue());

	}

}
