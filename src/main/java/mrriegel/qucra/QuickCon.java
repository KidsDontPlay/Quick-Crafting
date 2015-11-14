package mrriegel.qucra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import mrriegel.qucra.inventory.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.input.Keyboard;

public class QuickCon extends Container {
	private class HandySlot extends Slot {

		public HandySlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
				int p_i1824_4_) {
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return false;
		}
	}

	private class UpdateSlot extends Slot {

		public UpdateSlot(IInventory p_i1824_1_, int p_i1824_2_,
				int p_i1824_3_, int p_i1824_4_) {
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		}

		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			updateContainer(player, inv);

		}

	}

	QuickInv inv;
	EntityPlayer player;
	ArrayList<ItemStack> craftInventory;
	String lastInv;
	int trueSize;
	int position;
	int maxPosition;
	String search;
	boolean shift;
	boolean control;

	public QuickCon(EntityPlayer player, InventoryPlayer playerInv, QuickInv inv) {
		this.player = player;
		this.inv = inv;
		this.position = 0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new HandySlot(inv, j + i * 9, 12 + j * 18,
						8 + i * 18));
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new UpdateSlot(playerInv, j + i * 9 + 9,
						12 + j * 18, 138 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new UpdateSlot(playerInv, i, 12 + i * 18, 196));
		}
		craftInventory = CraftingLogic.getCraftableStack(player);
		lastInv = playerInv(player);
		trueSize = craftInventory.size();
		maxPosition = trueSize < 64 ? 0 : ((trueSize - 1) / 9) - 6;
		search = "";
	}

	private String playerInv(EntityPlayer player) {
		String f = "";
		for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++)
			if (player.inventory.getStackInSlot(i) != null)
				f += player.inventory.getStackInSlot(i).toString();
		return f;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void arrange(int i) {
		if (i < 0 && position < maxPosition)
			position += 1;
		else if (i > 0 && position > 0)
			position -= 1;
		updateContainer(player, inv);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	private ItemStack click(int index, EntityPlayer player) {
		boolean done = false;
		if (insert(player.inventory, getSlot(index).getStack(), true, true)
				&& (player.inventory.getItemStack() == null || (InventoryHelper
						.areStacksEqual(player.inventory.getItemStack(),
								getSlot(index).getStack(), false) && getSlot(
						index).getStack().stackSize
						+ player.inventory.getItemStack().stackSize <= getSlot(
							index).getStack().getMaxStackSize()))) {
			done = consumeItems(getSlot(index).getStack(), player, false)
					&& insert(player.inventory, getSlot(index).getStack(),
							false, true);
		}
		return done ? super.slotClick(index, 0, 0, player) : null;
	}

	private ItemStack clickControl(int index, EntityPlayer player) {
		boolean done = false;
		if (player.inventory.getItemStack() != null)
			return null;
		if (insert(player.inventory, getSlot(index).getStack(), true, false)
				&& consumeItems(getSlot(index).getStack(), player, true)) {
			done = insert(player.inventory, getSlot(index).getStack(), false,
					false)
					&& consumeItems(getSlot(index).getStack(), player, false);
		}
		return done ? getSlot(index).getStack().copy() : null;
	}

	private ItemStack clickShift(int index, EntityPlayer player) {
		int done = 0;
		if (player.inventory.getItemStack() != null)
			return null;
		while (insert(player.inventory, getSlot(index).getStack(), true, false)
				&& consumeItems(getSlot(index).getStack(), player, true)) {
			insert(player.inventory, getSlot(index).getStack(), false, false);
			consumeItems(getSlot(index).getStack(), player, false);
			done++;
		}
		if (done == 0)
			return null;
		return done != 0 ? getSlot(index).getStack().copy() : null;
	}

	private boolean consumeItems(ItemStack stack, EntityPlayer player,
			boolean simulate) {
		for (IRecipe r : CraftingLogic.getRecipes(stack)) {
			boolean trueMatch = CraftingLogic.match(r, player, true);
			if (trueMatch && !simulate) {
				return CraftingLogic.match(r, player, false);
			} else if (trueMatch && simulate) {
				return true;
			}
		}
		return false;
	}

	private boolean insert(IInventory inv, ItemStack stacko, boolean simulate,
			boolean onlyBy) {
		for (IRecipe recipe : CraftingLogic.getRecipes(stacko)) {
			ArrayList<ItemStack> ss = new ArrayList<ItemStack>();
			if (!onlyBy)
				ss.add(stacko);
			ArrayList<Object> soll = null;
			if (recipe instanceof ShapelessRecipes) {
				soll = new ArrayList<Object>(
						((ShapelessRecipes) recipe).recipeItems);
			} else if (recipe instanceof ShapelessOreRecipe) {
				soll = new ArrayList<Object>(
						((ShapelessOreRecipe) recipe).getInput());
			} else if (recipe instanceof ShapedRecipes) {
				soll = new ArrayList<Object>(
						Arrays.asList((((ShapedRecipes) recipe).recipeItems)));
				soll.removeAll(Collections.singleton(null));
			} else if (recipe instanceof ShapedOreRecipe) {
				soll = new ArrayList<Object>(
						Arrays.asList(((ShapedOreRecipe) recipe).getInput()));
				soll.removeAll(Collections.singleton(null));
			}

			for (Object o : soll) {
				if (o instanceof ItemStack) {
					ItemStack stack = (ItemStack) o;
					if (!stack.getItem().hasContainerItem(stack))
						continue;
					ss.add(stack.getItem().getContainerItem(stack));
				} else if (o instanceof ArrayList) {
					ItemStack stack = (ItemStack) ((ArrayList) o).get(0);
					if (!stack.getItem().hasContainerItem(stack))
						continue;
					ss.add(stack.getItem().getContainerItem(stack));
				}
			}
			if (InventoryHelper.insert(inv, ss, simulate))
				return true;

		}
		return false;

	}

	@Override
	public ItemStack slotClick(int index, int key, int shift,
			EntityPlayer player) {
		if (player.worldObj.isRemote) {
			this.shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			this.control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
			PacketHandler.INSTANCE.sendToServer(new KeyMessage(this.shift,
					this.control));
		}

		ItemStack ss = null;
		if (index >= 0 && index < 63 && key == 0
				&& getSlot(index).getHasStack()) {
			if (this.shift) {
				ss = clickShift(index, player);
			} else if (this.control) {
				ss = clickControl(index, player);
			} else {
				ss = click(index, player);
			}
		} else if (((index >= 63 && index < inventorySlots.size()) || index == -999)
				&& shift != 1) {
			ss = super.slotClick(index, key, shift, player);
		}

		updateContainer(player, inv);
		return ss;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		throw new UnsupportedOperationException("Please make a bug report");
	}

	public void updateContainer(EntityPlayer player, QuickInv inv) {
		if (!lastInv.equals(playerInv(player))) {
			craftInventory = CraftingLogic.getCraftableStack(player);
			lastInv = playerInv(player);
		}
		ArrayList<ItemStack> tmp = search.equals("") ? new ArrayList<ItemStack>(
				craftInventory) : new ArrayList<ItemStack>();
		if (!search.equals("")) {
			for (ItemStack s : craftInventory)
				if (s.getDisplayName().toLowerCase()
						.contains(search.toLowerCase()))
					tmp.add(s);
		}
		trueSize = tmp.size();
		maxPosition = trueSize < 64 ? 0 : ((trueSize - 1) / 9) - 6;
		if (position > maxPosition)
			position = maxPosition;

		for (int i = 0; i < 63; i++) {
			if ((i + position * 9) < tmp.size()) {
				inv.setInventorySlotContents(i, tmp.get(i + position * 9)
						.copy());
			} else {
				inv.setInventorySlotContents(i, null);
			}
		}
	}
}