package application.setting.listener;

public interface TavMediaPlayerChoiceSettingListener
{
	/**
	 * Apply the media player choice setting.
	 * 
	 * @param player
	 *            is the media player to be used with the system.
	 */
	public void mediaPlayerChoiceSetting(String player);

	/**
	 * Get the media player choice setting.
	 * 
	 * @return the value of the media player choice setting.
	 */
	public String getPlayerSetting();
}
