package stage.visualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Rectangle;
import application.visualizer.listener.TavVisualizerControlsListener;

public class TavItemToColorColorSetEventHandler implements
		ChangeListener<Number>
{

	TavVisualizerControlsListener listener;
	private Rectangle rectangle;

	public TavItemToColorColorSetEventHandler(
			TavVisualizerControlsListener listener, Rectangle rectangle)
	{
		this.listener = listener;
		this.rectangle = rectangle;
	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0,
			Number oldValue, Number newValue)
	{
		listener.setColorItem (newValue);
		rectangle.setFill (listener.getColorAtIndex (newValue.intValue()));
	}
}
