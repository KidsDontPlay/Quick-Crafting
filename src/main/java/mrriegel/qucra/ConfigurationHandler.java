package mrriegel.qucra;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;

	public static boolean scroll;

	public static void refreshConfig() {
		scroll = config.get("Client", "mousewheel", true,
				"Enable scrolling with mousewheel. Could be buggy with NEI")
		.getBoolean();
		if (config.hasChanged()) {
			config.save();
		}

	}

}
