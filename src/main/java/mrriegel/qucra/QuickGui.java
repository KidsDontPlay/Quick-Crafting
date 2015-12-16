package mrriegel.qucra;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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
	int guiRight = guiLeft + this.xSize;
	int guiBot = guiTop + this.ySize;
	private GuiButton up, down;
	private QuickCon con;

	public QuickGui(Container p_i1072_1_) {
		super(p_i1072_1_);
		con = (QuickCon) inventorySlots;
		xSize = 184;
		ySize = 220;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		searchBar = new GuiTextField(1, fontRendererObj, guiLeft + 86,
				guiTop - 6, 85, fontRendererObj.FONT_HEIGHT);
		searchBar.setMaxStringLength(14);
		searchBar.setEnableBackgroundDrawing(false);
		searchBar.setVisible(true);
		searchBar.setTextColor(16777215);
		searchBar.setCanLoseFocus(false);
		searchBar.setFocused(true);
		up = new GuiButton(1, guiLeft - 22, guiTop + 5, 20, 20,
				String.valueOf((char) 10224));
		buttonList.add(up);
		down = new GuiButton(-1, guiLeft - 22, guiTop + 30, 20, 20,
				String.valueOf((char) 10225));
		buttonList.add(down);
		ableButtons();
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		con.arrange(p_146284_1_.id);
		PacketHandler.INSTANCE.sendToServer(new ScrollMessage(p_146284_1_.id));
		ableButtons();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		guiRight = guiLeft + this.xSize;
		guiBot = guiTop + this.ySize;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft + 62, guiTop - 14, 0, 220, 122, 19);
		searchBar.drawTextBox();
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException {
		if (!this.checkHotbarKeys(p_73869_2_)) {
			if (this.searchBar.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
				PacketHandler.INSTANCE.sendToServer(new SearchMessage(searchBar
						.getText()));
				con.setSearch(searchBar.getText());
				con.updateContainer(con.player, con.inv);
				ableButtons();
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height
				/ this.mc.displayHeight - 1;
		if (ConfigurationHandler.scroll && i > (guiLeft) && i < (guiRight)
				&& j > (guiTop) && j < (guiBot)) {
			int mouse = Mouse.getEventDWheel();
			if (mouse == 0)
				return;
			con.arrange(mouse);
			PacketHandler.INSTANCE.sendToServer(new ScrollMessage(mouse));
			ableButtons();
		}
	}

	void ableButtons() {
		if (con.position == 0)
			up.enabled = false;
		else
			up.enabled = true;
		if (con.position == con.maxPosition)
			down.enabled = false;
		else
			down.enabled = true;
	}

}