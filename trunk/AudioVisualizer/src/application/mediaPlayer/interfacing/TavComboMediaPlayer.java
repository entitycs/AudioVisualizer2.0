package application.mediaPlayer.interfacing;

import application.visualizer.interfacing.TavVisualization;

/**
 * A combo media player needs to accept certain events from the visualization
 * 'side' of the application.
 * 
 * This should represent the minimum coupling allowed by the system for a
 * visualization.
 */
public interface TavComboMediaPlayer extends TavMediaPlayer
{
	/**
	 * A combo media player shall have a width which can be set.
	 * 
	 * @param width
	 *            is the value of the preferred visualization width.
	 */
	public void setWidth(double width);

	/**
	 * A combo media player shall have a width which can be set.
	 * 
	 * @param height
	 *            is the value of the preferred visualization height.
	 */
	public void setHeight(double height);

	/**
	 * Set visualization index.
	 * 
	 * @param index
	 */
	public void setVisualizationIndex(Number index);

	/**
	 * Get the current visualization
	 * 
	 * @return
	 */
	public TavVisualization getVisualization();
	
//	/**
//	 * Set the current visualization belonging to the visualizer
//	 * having the given index.
//	 * 
//	 * @param visualizationIndex is the index of the visualization
//	 * to set.
//	 */
//	void setVisualization(int visualizationIndex);
	
	/**
	 * Return true if the visualization in place is customizable.
	 * 
	 * @return true if the underlying visualization in use is customizable.
	 */
	public boolean isCustomizable();
}
