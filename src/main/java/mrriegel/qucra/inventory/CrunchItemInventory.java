package mrriegel.qucra.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CrunchItemInventory implements IInventory {
	protected final int INVSIZE;
	protected ItemStack[] inv;
	protected final int stackLimit;
	public ItemStack storedInv = null;

	public CrunchItemInventory(int size, int stackLimit, ItemStack stack) {
		this.INVSIZE = size;
		inv = new ItemStack[size];
		this.stackLimit = stackLimit;
		this.storedInv = stack;
		if (!storedInv.hasTagCompound()) {
			storedInv.setTagCompound(new NBTTagCompound());
		}
	}

	public CrunchItemInventory(int size, ItemStack stack) {
		this(size, 64, stack);
	}

	public ItemStack[] getInv() {
		return inv;
	}

	public void clear() {
		inv = new ItemStack[INVSIZE];
	}

	@Override
	public int getSizeInventory() {
		return INVSIZE;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if (this.inv[slot] != null) {
			ItemStack itemstack;
			if (this.inv[slot].stackSize <= size) {
				itemstack = this.inv[slot];
				this.inv[slot] = null;
				return itemstack;
			} else {
				itemstack = this.inv[slot].splitStack(size);

				if (this.inv[slot].stackSize == 0) {
					this.inv[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.inv[slot] != null) {
			ItemStack itemstack = this.inv[slot];
			this.inv[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return storedInv.getDisplayName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return this.stackLimit;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return InventoryHelper.areStacksEqual(player.getHeldItem(), storedInv,
				false);
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public void markDirty() {
	}
}
