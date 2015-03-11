package application.setting.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;
import application.setting.listener.TavMediaPlayerChoiceSettingListener;

public class TavMediaPlayerChoiceSettingEventHandler implements
		EventHandler<ActionEvent>
{

	private TavMediaPlayerChoiceSettingListener listener;

	public TavMediaPlayerChoiceSettingEventHandler(
			TavMediaPlayerChoiceSettingListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void handle(ActionEvent event)
	{
		listener.setPlayerSetting ( ((RadioMenuItem) event.getSource())
				.getId());
	}

}
