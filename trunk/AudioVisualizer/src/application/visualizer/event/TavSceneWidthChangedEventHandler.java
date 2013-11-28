package application.visualizer.event;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import application.visualizer.interfacing.TavVisualizationWidth;

public class TavSceneWidthChangedEventHandler implements ChangeListener<Number>
{
	private final DoubleProperty canvasWidth;
	private TavVisualizationWidth visualizationWidth;

	public TavSceneWidthChangedEventHandler(final DoubleProperty widthProperty,
			TavVisualizationWidth tavVisualizationWidth)
	{
		this.canvasWidth = widthProperty;
		this.visualizationWidth = tavVisualizationWidth;
	}

	@Override
	public void changed(
			final ObservableValue<? extends Number> observableValue,
			final Number oldSceneWidth, final Number newSceneWidth)
	{
		this.canvasWidth.setValue (newSceneWidth);
		this.visualizationWidth.setValue (newSceneWidth);
	}
}