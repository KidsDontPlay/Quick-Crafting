package mrriegel.qucra;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	public IMessage onMessage(final ScrollMessage message,
			final MessageContext ctx) {
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				QuickCon con = (QuickCon) ctx.getServerHandler().playerEntity.openContainer;
				con.arrange(message.mouse);
			}
		});

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