package mrriegel.qucra;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SearchMessage implements IMessage,
		IMessageHandler<SearchMessage, IMessage> {
	String s;

	public SearchMessage() {
	}

	public SearchMessage(String s) {
		super();
		this.s = s;
	}

	@Override
	public IMessage onMessage(SearchMessage message, MessageContext ctx) {
		QuickCon con = (QuickCon) ctx.getServerHandler().playerEntity.openContainer;
		con.setSearch(message.s);
		con.updateContainer(con.player, con.inv);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.s = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.s);
	}

}
