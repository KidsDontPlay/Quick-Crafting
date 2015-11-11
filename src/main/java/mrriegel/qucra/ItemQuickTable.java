package mrriegel.qucra;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemQuickTable extends Item {
	public ItemQuickTable() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setUnlocalizedName(QuickCrafting.MODID + ":" + "quickTableItem");
		this.setTextureName(QuickCrafting.MODID + ":" + "quickTableItem");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {
		if (!world.isRemote)
			player.openGui(QuickCrafting.instance, 0, world, 0, 0, 0);
		return stack;
	}

	public static final Item qt = new ItemQuickTable();

	public static void init() {
		GameRegistry.registerItem(qt, "quickTableItem");
	}
}
