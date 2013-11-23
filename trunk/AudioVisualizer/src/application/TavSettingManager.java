package application;

import application.setting.MediaPlayerSetting;
import application.setting.VisualizerDimensionSetting;
import application.setting.interfacing.ApplicationSetting;
import application.setting.listener.PlayerChoiceSettingListener;
import application.setting.listener.VisualizerDimensionSettingListener;

public class TavSettingManager implements ApplicationSetting, PlayerChoiceSettingListener, VisualizerDimensionSettingListener
{
	// the following may be better suited in its own object which can load
	// settings from a file

	private final MediaPlayerSetting mediaPlayerSetting;
	private final VisualizerDimensionSetting visualizerSetting;
	
	public TavSettingManager(){
		mediaPlayerSetting = new MediaPlayerSetting();
		visualizerSetting = new VisualizerDimensionSetting();
	}
	
	public void widthSetting(double width)
	{
		visualizerSetting.setWidth(width);
		TavApplicationManager.getInstance().getVisualizerManager()
		.setWidth(width);
	}

	public void heightSetting(double height)
	{
		visualizerSetting.setHeight(height);
		TavApplicationManager.getInstance().getVisualizerManager()
		.setHeight(height);
	}

	public void playerSetting(String text)
	{
		// if there is a media player in action, stop it.
		if (TavApplicationManager.getInstance().getMediaPlayerManager()
				.getMediaPlayer() != null)
			TavApplicationManager.getInstance().getMediaPlayerManager().stop();

		mediaPlayerSetting.setPlayer(text);

		TavApplicationManager.getInstance().getMediaPlayerManager()
				.updatePlayer();
	}

	public String getPlayerSetting()
	{
		return mediaPlayerSetting.getPlayerSetting();
	}
}
