package application.visualizer.interfacing;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public interface TavVisualizationCustomizable extends TavVisualization
{
	/**
	 * Change color at the given index.
	 * 
	 * @param index is the index of the color to change.
	 * @param value is the new color to replace the color at the given index.
	 */
	public void changeColor(int index, Color value);

	/**
	 * Get the color at the given index.
	 * 
	 * @param index is the index of the color to return.
	 * 
	 * @return the customizable color at the given index.
	 */
	public Color getCustomColor(int index);

	/**
	 * Set the customization level controls. Above every user interfacing
	 * control, there should be a label (defined by the interfacer).
	 * In terms of layout, the labels are located in the same column, 
	 * one row above the level control.
	 * 
	 * Values of sliders, tick marks, step scales, etc. can also be
	 * defined by the interfacer.
	 * 
	 * @param levelControls
	 */
	public void setCustomizeLevels(GridPane levelControls);
}
