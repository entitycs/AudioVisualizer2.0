package application.setting.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import application.setting.listener.TavVisualizerDimensionSettingListener;

public class TavVisualizerDimensionSettingEventHandler implements
		EventHandler<ActionEvent>
{

	private TavVisualizerDimensionSettingListener listener;
	private TextField width;
	private TextField height;
	private Label label;

	public TavVisualizerDimensionSettingEventHandler(
			TavVisualizerDimensionSettingListener listener, TextField width,
			TextField height, Label label)
	{
		this.listener = listener;
		this.width = width;
		this.height = height;
		this.label = label;
	}

	public void handle(ActionEvent e)
	{
		if ( (width.getText() != null && !width.getText().isEmpty())
				&& (height.getText() != null && !height.getText().isEmpty()))
		{
			listener.widthSetting (Double.parseDouble (width.getText()));
			listener.heightSetting (Double.parseDouble (height.getText()));

			label.setText (width.getText() + " " + height.getText() + ", "
					+ "Visualizer size was set successfully");
		} else
		{
			label.setText ("Unable to parse visualizer size.");
		}

	}
}
