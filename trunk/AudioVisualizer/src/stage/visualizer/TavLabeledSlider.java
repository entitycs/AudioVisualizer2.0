package stage.visualizer;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class TavLabeledSlider extends Slider
{
	private final Label sliderLabel;
	
	public TavLabeledSlider(final Label label){
		this.sliderLabel = label;
	}
	
	public final Label getLabel(){
		return sliderLabel;
	}
}
