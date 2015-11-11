package mrriegel.qucra;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(
			QuickCrafting.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(ScrollMessage.class, ScrollMessage.class,
				id++, Side.SERVER);
		INSTANCE.registerMessage(KeyMessage.class, KeyMessage.class,
				id++, Side.SERVER);
	}
}