package mrriegel.qucra;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ScrollMessage implements IMessage,
		IMessageHandler<ScrollMessage, IMessage> {
	int mouse = 0;

	public ScrollMessage() {
	}

	public ScrollMessage(int mouse) {
		super();
		this.mouse = mouse;
	}

	@Override
	public IMessage onMessage(ScrollMessage message, MessageContext ctx) {
		QuickCon con = (QuickCon) ((QuickGui) Minecraft.getMinecraft().currentScreen).inventorySlots;
		con.arrange(message.mouse);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mouse = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.mouse);
	}

}
