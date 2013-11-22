package visualizer.visualizerFX;

import application.visualizer.interfacing.TavVisualization;


public interface TavVisualizer
{

	public TavVisualization getVisualization();

	public void setVisualization(TavVisualization listener);

	public void setWidth(double width);

	public void setHeight(double height);

}
