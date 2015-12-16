package mrriegel.qucra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import mrriegel.qucra.inventory.InventoryCopy;
import mrriegel.qucra.inventory.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftingLogic {
	public static boolean match(IRecipe r, EntityPlayer player, boolean simulate) {
		ArrayList<Object> soll = null;
		if (r instanceof ShapelessRecipes) {
			soll = new ArrayList<Object>(((ShapelessRecipes) r).recipeItems);
		} else if (r instanceof ShapelessOreRecipe) {
			soll = new ArrayList<Object>(((ShapelessOreRecipe) r).getInput());
		} else if (r instanceof ShapedRecipes) {
			soll = new ArrayList<Object>(
					Arrays.asList((((ShapedRecipes) r).recipeItems)));
			soll.removeAll(Collections.singleton(null));
		} else if (r instanceof ShapedOreRecipe) {
			soll = new ArrayList<Object>(Arrays.asList(((ShapedOreRecipe) r)
					.getInput()));
			soll.removeAll(Collections.singleton(null));
		} else
			return false;
		IInventory alf = new InventoryCopy(player.inventory);
		for (Object o : soll) {
			if (!contains(o, simulate ? alf : player.inventory)) {
				return false;
			}
		}
		return true;

	}

	public static ArrayList<ItemStack> getCraftableStack(EntityPlayer player) {
		ArrayList<ItemStack> lis = new ArrayList<ItemStack>();
		for (Object o : CraftingManager.getInstance().getRecipeList()) {
			if (match((IRecipe) o, player, true)
					&& checkIn(lis, ((IRecipe) o).getRecipeOutput())) {
				lis.add(((IRecipe) o).getRecipeOutput().copy());
			}
		}
		return lis;
	}

	private static boolean checkIn(ArrayList<ItemStack> lis, ItemStack stack) {
		for (ItemStack s : lis)
			if (InventoryHelper.areStacksEqual(stack, s, false))
				return false;
		return true;
	}

	public static ArrayList<IRecipe> getRecipes(ItemStack stack) {
		ArrayList<IRecipe> lis = new ArrayList<IRecipe>();
		for (Object o : CraftingManager.getInstance().getRecipeList()) {
			if (InventoryHelper.areStacksEqual(stack,
					((IRecipe) o).getRecipeOutput(), false)
					&& ((IRecipe) o).getRecipeOutput().stackSize == stack.stackSize) {
				lis.add((IRecipe) o);
			}
		}
		return lis;
	}

	public static boolean contains(Object o, IInventory inv) {
		if (o instanceof ItemStack) {
			ItemStack stack = (ItemStack) o;
			if (stack == null || stack.getItem() == null)
				return false;
			return InventoryHelper.consumeInventoryItem(inv, (ItemStack) o, 1);
		} else if (o instanceof Collection) {
			for (Object obj : (Collection) o) {
				ItemStack stack = (ItemStack) obj;
				if (stack == null || stack.getItem() == null)
					return false;
				if (InventoryHelper.consumeInventoryItem(inv, stack, 1)) {
					return true;
				}
			}
		}
		return false;
	}
}