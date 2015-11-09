package mrriegel.qucra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class QuickGui extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(
			QuickCrafting.MODID + ":" + "textures/gui/quick.png");

	public QuickGui(Container p_i1072_1_) {
		super(p_i1072_1_);
		xSize = 184;
		ySize = 220;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		if (Mouse.getX() > ((width - xSize) + 12)
				&& Mouse.getX() < ((width + xSize) - 12)
				&& Mouse.getY() > ((height - ySize) + 170)
				&& Mouse.getY() < ((height + ySize) - 6)) {
			int mouse = Mouse.getEventDWheel();
			if (mouse == 0)
				return;
			QuickCon con = (QuickCon) ((QuickGui) Minecraft.getMinecraft().currentScreen).inventorySlots;
//			con.arrange(mouse);
			PacketHandler.INSTANCE.sendToServer(new ScrollMessage(mouse));
		}

	}

}
