package stage.visualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import application.visualizer.listener.TavVisualizerControlsListener;

public class TavVisualizationChosenEventHandler implements
		ChangeListener<Number>
{

	private TavVisualizerControlsListener visualizerControlsListener;

	public TavVisualizationChosenEventHandler(
			TavVisualizerControlsListener visualizerControlsListener)
	{
		this.visualizerControlsListener = visualizerControlsListener;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable,
			Number oldValue, Number newValue)
	{
		this.visualizerControlsListener
		.setVisualizationItem (newValue.intValue ());		
	}

}
