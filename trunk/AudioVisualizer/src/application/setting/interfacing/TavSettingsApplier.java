package application.setting.interfacing;

import application.setting.listener.TavMediaPlayerChoiceSettingListener;
import application.setting.listener.TavVisualizerDimensionSettingListener;

/**
 * Defines the interface for the object which is to manage the settings. Such an
 * object should extend all settings listeners. This interface is used to assure
 * all settings are handled by the implementing object.
 */
public interface TavSettingsApplier extends
		TavMediaPlayerChoiceSettingListener,
		TavVisualizerDimensionSettingListener
{
	// TODO any additional methods needed here for the object which
	// will be used apply settings?

	// The two listeners are separate as they apply to different
	// 'sides' of the sytem.
}
