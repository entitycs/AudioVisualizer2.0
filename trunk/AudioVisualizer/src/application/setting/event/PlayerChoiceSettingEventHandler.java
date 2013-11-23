package application.setting.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;
import application.setting.listener.PlayerChoiceSettingListener;


public class PlayerChoiceSettingEventHandler implements EventHandler<ActionEvent> {

	private PlayerChoiceSettingListener listener;

	public PlayerChoiceSettingEventHandler(PlayerChoiceSettingListener listener){
		this.listener = listener;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		listener.playerSetting(((RadioMenuItem)arg0.getSource()).getText());
	}

}
