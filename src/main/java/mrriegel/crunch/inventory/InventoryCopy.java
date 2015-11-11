package mrriegel.crunch.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Creates a copy of an IInventory for extended simulation
 */

/**
 * Stolen from chicken bones
 */
public class InventoryCopy implements IInventory {
	public ItemStack[] items;
	public IInventory inv;

	public InventoryCopy(IInventory inv) {
		items = new ItemStack[inv.getSizeInventory()
				- ((inv instanceof InventoryPlayer) ? 4 : 0)];
		this.inv = inv;
		update();
	}

	public void update() {
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null)
				items[i] = stack.copy();
		}
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return inv.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inv.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		items[slot] = stack;
		markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return inv.isItemValidForSlot(i, itemstack);
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}
}