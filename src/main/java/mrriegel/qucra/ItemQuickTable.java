package mrriegel.qucra;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
		return false;
	}

	public static final Item qt = new ItemQuickTable();

	public static void init() {
		GameRegistry.registerItem(qt, "quickTableItem");
		qt.setContainerItem(Items.bucket);
	}
}
