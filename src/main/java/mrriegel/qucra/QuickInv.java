package mrriegel.qucra;

import java.util.ArrayList;

import mrriegel.qucra.inventory.CrunchItemInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class QuickInv extends CrunchItemInventory {

	public QuickInv(EntityPlayer player) {
		super(63, new ItemStack(Blocks.fire));
		ArrayList<ItemStack> lis = CraftingLogic.getCraftableStack(player);
		int trueSize = lis.size();
		for (int i = 0; i < (trueSize <= 63 ? trueSize : 63); i++) {
			setInventorySlotContents(i, lis.get(i).copy());
		}
		for (int i = trueSize; i < 63; i++) {
			setInventorySlotContents(i, null);
		}
	}

}
