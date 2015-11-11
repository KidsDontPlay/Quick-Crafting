package mrriegel.qucra;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyMessage implements IMessage,
		IMessageHandler<KeyMessage, IMessage> {
	boolean shift, control;

	public KeyMessage() {
	}

	public KeyMessage(boolean shift, boolean control) {
		super();
		this.shift = shift;
		this.control = control;
	}

	@Override
	public IMessage onMessage(KeyMessage message, MessageContext ctx) {
		QuickCon con = (QuickCon) ctx.getServerHandler().playerEntity.openContainer;
		con.shift = message.shift;
		con.control = message.control;
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.shift = buf.readBoolean();
		this.control = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.shift);
		buf.writeBoolean(this.control);
	}

}