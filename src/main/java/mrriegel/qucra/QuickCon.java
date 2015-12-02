package mrriegel.qucra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import appeng.api.exceptions.MissingIngredientError;
import appeng.api.exceptions.RegistrationError;
import appeng.api.recipes.IIngredient;
import appeng.recipes.game.ShapedRecipe;
import appeng.recipes.game.ShapelessRecipe;

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
	IRecipe current;
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
		if (consumeItems(getSlot(index).getStack().copy(), player, true)
				&& insert(player.inventory, getSlot(index).getStack().copy(),
						true, true)
				&& (player.inventory.getItemStack() == null || (InventoryHelper
						.areStacksEqual(player.inventory.getItemStack(),
								getSlot(index).getStack().copy(), false) && getSlot(
						index).getStack().copy().stackSize
						+ player.inventory.getItemStack().stackSize <= getSlot(
							index).getStack().copy().getMaxStackSize()))) {
			done = consumeItems(getSlot(index).getStack().copy(), player, false)
					&& insert(player.inventory, getSlot(index).getStack()
							.copy(), false, true);
		}
		return done ? super.slotClick(index, 0, 0, player) : null;
	}

	private ItemStack clickControl(int index, EntityPlayer player) {
		boolean done = false;
		if (player.inventory.getItemStack() != null)
			return null;
		if (consumeItems(getSlot(index).getStack().copy(), player, true)
				&& insert(player.inventory, getSlot(index).getStack().copy(),
						true, false)) {
			done = consumeItems(getSlot(index).getStack().copy(), player, false)
					&& insert(player.inventory, getSlot(index).getStack()
							.copy(), false, false);
		}
		return done ? getSlot(index).getStack().copy() : null;
	}

	private ItemStack clickShift(int index, EntityPlayer player) {
		int done = 0;
		if (player.inventory.getItemStack() != null)
			return null;
		while (consumeItems(getSlot(index).getStack().copy(), player, true)
				&& insert(player.inventory, getSlot(index).getStack().copy(),
						true, false)) {
			consumeItems(getSlot(index).getStack().copy(), player, false);
			insert(player.inventory, getSlot(index).getStack().copy(), false,
					false);
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
				current = r;
				return CraftingLogic.match(r, player, false);
			} else if (trueMatch && simulate) {
				current = r;
				return true;
			}
		}
		return false;
	}

	private boolean insert(InventoryPlayer inv, ItemStack stacko,
			boolean simulate, boolean onlyBy) {
		IRecipe recipe = current;
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
		} else if (recipe instanceof ShapedRecipe) {
			ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
			for (Object o : ((ShapedRecipe) recipe).getIngredients()) {
				if (o != null) {
					try {
						tmp.add(new ArrayList(Arrays.asList(((IIngredient) o)
								.getItemStackSet())));
					} catch (RegistrationError e) {
					} catch (MissingIngredientError e) {
					}
				}
			}
			soll = new ArrayList<Object>(tmp);
			soll.removeAll(Collections.singleton(null));
		} else if (recipe instanceof ShapelessRecipe) {
			ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
			for (Object o : ((ShapelessRecipe) recipe).getInput()) {
				if (o != null) {
					try {
						tmp.add(new ArrayList(Arrays.asList(((IIngredient) o)
								.getItemStackSet())));
					} catch (RegistrationError e) {
					} catch (MissingIngredientError e) {
					}
				}
			}
			soll = new ArrayList<Object>(tmp);
			soll.removeAll(Collections.singleton(null));
		} else {
			ArrayList<Object> tmp = findInvoke1(recipe, soll);
			if (tmp != null && tmp.size() > 0) {
				soll = new ArrayList<Object>(tmp);
				soll.removeAll(Collections.singleton(null));
			}
		}
		if (soll == null || soll.size() == 0)
			return false;
		for (Object o : soll) {
			if (o instanceof ItemStack) {
				ItemStack stack = ((ItemStack) o).copy();
				if (!stack.getItem().hasContainerItem(stack))
					continue;
				ss.add(stack.getItem().getContainerItem(stack));
			} else if (o instanceof ArrayList /* && !((ArrayList) o).isEmpty() */) {
				ItemStack stack = ((ItemStack) ((ArrayList) o).get(0)).copy();
				if (!stack.getItem().hasContainerItem(stack))
					continue;
				ss.add(stack.getItem().getContainerItem(stack));
			}
		}
		if (InventoryHelper.insert(inv, ss, simulate))
			return true;

		return false;

	}

	private static ArrayList<Object> findInvoke1(IRecipe r,
			ArrayList<Object> soll) {
		Method m = null;
		Object[] fin = null;
		try {
			m = r.getClass().getMethod("getInput", (Class<?>[]) null);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
		if (m == null)
			return null;

		try {
			fin = (Object[]) m.invoke(r, (Object[]) null);
		} catch (ClassCastException e) {
		} catch (IllegalAccessException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
		if (fin == null)
			return null;
		ArrayList<Object> res = new ArrayList<Object>();
		for (Object o : fin) {
			if (o instanceof ItemStack) {
				res.add(o);
			} else if (o instanceof ArrayList && !((ArrayList) o).isEmpty()
					&& ((ArrayList) o).get(0) instanceof ItemStack) {
				res.add(o);
			}
		}
		return res;
	}

	@Override
	public ItemStack slotClick(int index, int key, int shift,
			EntityPlayer player) {
		// System.out.println("index; " + index + ", key: " + key + ", shift: "
		// + shift);
		// for (StackTraceElement s : Thread.currentThread().getStackTrace())
		// System.out.println(s);
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
