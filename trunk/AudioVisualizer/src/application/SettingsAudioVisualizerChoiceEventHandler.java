package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;


public class SettingsAudioVisualizerChoiceEventHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		TavApplicationManager.getInstance().playerSetting(((RadioMenuItem)arg0.getSource()).getText());
	}

}
