package mrriegel.qucra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
import appeng.api.exceptions.MissingIngredientError;
import appeng.api.exceptions.RegistrationError;
import appeng.api.recipes.IIngredient;
import appeng.recipes.game.ShapedRecipe;
import appeng.recipes.game.ShapelessRecipe;

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
		} else if (r instanceof ShapedRecipe) {
			ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
			for (Object o : ((ShapedRecipe) r).getIngredients()) {
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
		} else if (r instanceof ShapelessRecipe) {
			ArrayList<ArrayList> tmp = new ArrayList<ArrayList>();
			for (Object o : ((ShapelessRecipe) r).getInput()) {
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
			ArrayList<Object> tmp = findInvoke1(r, soll);
			if (tmp != null && tmp.size() > 0) {
				soll = new ArrayList<Object>(tmp);
				soll.removeAll(Collections.singleton(null));
			}
		}
		if (soll == null || soll.size() == 0)
			return false;
		IInventory alf = new InventoryCopy(player.inventory);
		for (Object o : soll) {
			if (!contains(o, simulate ? alf : player.inventory)) {
				return false;
			}
		}
		return true;

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
					((IRecipe) o).getRecipeOutput(), false)) {
				lis.add((IRecipe) o);
			}
		}
		return lis;
	}

	public static boolean contains(Object o, IInventory inv) {
		// for (int i = 0; i < inv.getSizeInventory(); i++)
		if (o instanceof ItemStack) {
			return InventoryHelper.consumeInventoryItem(inv, (ItemStack) o, 1);
		} else if (o instanceof ArrayList) {
			for (Object obj : (ArrayList) o) {
				try {
					ItemStack stack = (ItemStack) obj;
					if (InventoryHelper.consumeInventoryItem(inv, stack, 1)) {
						return true;
					}
				} catch (ClassCastException e) {
					continue;
				}
			}
		}
		return false;
	}
}
