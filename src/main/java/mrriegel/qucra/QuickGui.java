package mrriegel.qucra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class QuickGui extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(
			QuickCrafting.MODID + ":" + "textures/gui/quick.png");
	private GuiTextField searchBar;

	public QuickGui(Container p_i1072_1_) {
		super(p_i1072_1_);
		xSize = 184;
		ySize = 220;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		searchBar = new GuiTextField(fontRendererObj, guiLeft + 86, guiTop -6,
				85, fontRendererObj.FONT_HEIGHT);
		searchBar.setMaxStringLength(14);
		searchBar.setEnableBackgroundDrawing(false);
		searchBar.setVisible(true);
		searchBar.setTextColor(16777215);
		searchBar.setCanLoseFocus(false);
		searchBar.setFocused(true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawTexturedModalRect(k+62, l-14, 0, 220, 122, 19);
		searchBar.drawTextBox();
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {

		if (!this.checkHotbarKeys(p_73869_2_)) {
			if (this.searchBar.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
				PacketHandler.INSTANCE.sendToServer(new SearchMessage(searchBar
						.getText()));
				QuickCon con = (QuickCon) ((QuickGui) Minecraft.getMinecraft().currentScreen).inventorySlots;
				con.setSearch(searchBar.getText());
				con.updateContainer(con.player, con.inv);
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		if (Mouse.getX() > ((width - xSize) + 12)
				&& Mouse.getX() < ((width + xSize) - 12)
				&& Mouse.getY() > ((height - ySize) + 169)
				&& Mouse.getY() < ((height + ySize) - 8)) {
			int mouse = Mouse.getEventDWheel();
			if (mouse == 0)
				return;
			QuickCon con = (QuickCon) ((QuickGui) Minecraft.getMinecraft().currentScreen).inventorySlots;
			con.arrange(mouse);
			PacketHandler.INSTANCE.sendToServer(new ScrollMessage(mouse));
		}

	}

}
