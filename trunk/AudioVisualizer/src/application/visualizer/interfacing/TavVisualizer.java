package application.visualizer.interfacing;

public interface TavVisualizer
{
	public TavVisualization getVisualization();

	//public abstract void setVisualization(int visualizationIndex);
	
	public TavVisualization setVisualizationIndex(int visualizationIndex);

	public void setWidth(double width);

	public void setHeight(double height);

	public boolean isShowing();

	public void show();

	public void build();

}
