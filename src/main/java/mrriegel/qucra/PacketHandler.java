package mrriegel.qucra;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(
			QuickCrafting.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(ScrollMessage.class, ScrollMessage.class,
				id++, Side.SERVER);
	}
}
