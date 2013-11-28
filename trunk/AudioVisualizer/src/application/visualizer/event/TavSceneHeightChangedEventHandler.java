package application.visualizer.event;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import application.visualizer.interfacing.TavVisualizationHeight;

public class TavSceneHeightChangedEventHandler implements
		ChangeListener<Number>
{
	private final DoubleProperty canvasHeight;
	private TavVisualizationHeight visualizationHeight;

	public TavSceneHeightChangedEventHandler(
			final DoubleProperty heightProperty,
			TavVisualizationHeight tavVisualizationHeight)
	{
		this.canvasHeight = heightProperty;
		this.visualizationHeight = tavVisualizationHeight;
	}

	@Override
	public void changed(
			final ObservableValue<? extends Number> observableValue,
			final Number oldSceneHeight, final Number newSceneHeight)
	{
		this.canvasHeight.setValue (newSceneHeight);
		this.visualizationHeight.setValue (newSceneHeight);
	}
}