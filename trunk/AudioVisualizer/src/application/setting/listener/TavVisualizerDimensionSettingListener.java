package application.setting.listener;

public interface TavVisualizerDimensionSettingListener
{
	/**
	 * Apply the initial visualization width setting.
	 * 
	 * @param width
	 *            is the initial visualization with.
	 */
	public void widthSetting(double width);

	/**
	 * Apply the initial visualization height setting.
	 * 
	 * @param height
	 *            is the initial visualization height.
	 */
	public void heightSetting(double height);
}
