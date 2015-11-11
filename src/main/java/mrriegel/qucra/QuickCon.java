package mrriegel.qucra;

import java.util.ArrayList;
import java.util.List;

import mrriegel.crunch.helper.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;

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

	int lastUpdate;

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
	}

	private String playerInv(EntityPlayer player) {
		String f = "";
		for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++)
			if (player.inventory.getStackInSlot(i) != null)
				f += player.inventory.getStackInSlot(i).toString();
		return f;
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
		if (insertByproduct(getSlot(index).getStack(), player, true)
				&& (player.inventory.getItemStack() == null || (InventoryHelper
						.areStacksEqual(player.inventory.getItemStack(),
								getSlot(index).getStack(), false) && getSlot(
						index).getStack().stackSize
						+ player.inventory.getItemStack().stackSize <= getSlot(
							index).getStack().getMaxStackSize()))) {
			done = consumeItems(getSlot(index).getStack(), player, false)
					&& insertByproduct(getSlot(index).getStack(), player, false);
		}
		return done ? super.slotClick(index, 0, 0, player) : null;
	}

	private ItemStack clickControl(int index, EntityPlayer player) {
		boolean done = false;
		if (player.inventory.getItemStack() != null)
			return null;
		if (InventoryHelper.insert(player.inventory, getSlot(index).getStack(),
				true)
				&& insertByproduct(getSlot(index).getStack(), player, true)
				&& consumeItems(getSlot(index).getStack(), player, true))
			done = InventoryHelper.insert(player.inventory, getSlot(index)
					.getStack(), false)
					&& consumeItems(getSlot(index).getStack(), player, false)
					&& insertByproduct(getSlot(index).getStack(), player, false);
		return done ? getSlot(index).getStack().copy() : null;
	}

	private ItemStack clickShift(int index, EntityPlayer player) {
		int done = 0;
		if (player.inventory.getItemStack() != null)
			return null;
		while (InventoryHelper.insert(player.inventory, getSlot(index)
				.getStack(), true)
				&& insertByproduct(getSlot(index).getStack(), player, true)
				&& consumeItems(getSlot(index).getStack(), player, true)) {
			InventoryHelper.insert(player.inventory, getSlot(index).getStack(),
					false);
			insertByproduct(getSlot(index).getStack(), player, false);
			consumeItems(getSlot(index).getStack(), player, false);
			done++;
		}
		if (done == 0)
			return null;
		return done != 0 ? getSlot(index).getStack().copy() : null;
	}

	public boolean consumeItems(ItemStack stack, EntityPlayer player,
			boolean simulate) {
		for (IRecipe r : CraftingLogic.getRecipes(stack)) {
			boolean trueMatch = CraftingLogic.match(r, player, true);
			if (trueMatch && !simulate)
				return CraftingLogic.match(r, player, false);
			else if (trueMatch && simulate)
				return true;
		}
		return false;
	}

	public boolean insertByproduct(ItemStack stack, EntityPlayer player,
			boolean simulate) {
		if (!stack.getItem().hasContainerItem(stack))
			return true;
		//FAAAALLLLSSSCCH
		for (IRecipe r : CraftingLogic.getRecipes(stack)) {
			boolean trueMatch = InventoryHelper.insert(player.inventory, stack
					.getItem().getContainerItem(stack), true);
			if (trueMatch && !simulate)
				return InventoryHelper.insert(player.inventory, stack.getItem()
						.getContainerItem(stack), false);
			else if (trueMatch && simulate)
				return true;
		}
		return false;
	}

	@Override
	public ItemStack slotClick(int index, int key, int shift,
			EntityPlayer player) {
		boolean changed = false;
		ItemStack ss = null;
		if (index >= 0 && index <= 62 && key == 0
				&& getSlot(index).getHasStack()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				ss = clickShift(index, player);
				if (ss != null)
					changed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				ss = clickControl(index, player);
				if (ss != null)
					changed = true;
			} else {
				ss = click(index, player);
				if (ss != null)
					changed = true;
			}
		} else if (((index >= 63 && index < inventorySlots.size()) || index == -999)
				&& shift != 1) {
			ss = super.slotClick(index, key, shift, player);
			if (ss != null)
				changed = true;
		}

		updateContainer(player, inv);
		return ss;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		throw new IllegalStateException(String.valueOf(slot));
	}

	public void updateContainer(EntityPlayer player, QuickInv inv) {
		if (!lastInv.equals(playerInv(player))) {
			craftInventory = CraftingLogic.getCraftableStack(player);
			lastInv = playerInv(player);
		}

		trueSize = craftInventory.size();
		maxPosition = trueSize < 64 ? 0 : ((trueSize - 1) / 9) - 6;
		if (position > maxPosition)
			position = maxPosition;

		for (int i = 0; i < 63; i++) {
			if ((i + position * 9) < craftInventory.size()) {
				inv.setInventorySlotContents(i,
						craftInventory.get(i + position * 9).copy());
			} else {
				inv.setInventorySlotContents(i, null);
			}
		}
	}
}
