package mrriegel.qucra;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(QuickCrafting.MODID) {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(BlockQuickTable.qt);
		}

		@Override
		public String getTranslatedTabLabel() {
			return QuickCrafting.MODNAME;
		}
	};
}
