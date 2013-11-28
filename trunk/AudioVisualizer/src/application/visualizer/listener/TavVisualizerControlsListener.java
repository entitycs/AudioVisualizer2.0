package application.visualizer.listener;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public interface TavVisualizerControlsListener extends ChangeListener<Number>
{
	public void setHeight(double height);

	public void setWidth(double width);

	public void setColorItem(Number index);

	public void setVisualizationItem(Number index);

	public void setColorItemColor(Color value);

	public Color getColorAtIndex(Number arg2);

	public void setVisualizationControls(GridPane customizeVidSliders);
}
