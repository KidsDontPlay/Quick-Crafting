package mrriegel.qucra;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(modid = QuickCrafting.MODID, version = QuickCrafting.VERSION)
public class QuickCrafting {
	public static final String MODID = "qucra";
	public static final String VERSION = "1.8-1.6";
	public static final String MODNAME = "QuickCrafting";

	@Instance(QuickCrafting.MODID)
	public static QuickCrafting instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configFile = event.getSuggestedConfigurationFile();
		ConfigurationHandler.config = new Configuration(configFile);
		ConfigurationHandler.config.load();
		ConfigurationHandler.refreshConfig();
		PacketHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		BlockQuickTable.init();
		ItemQuickTable.init();
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickTable.qt),
				ItemQuickTable.qt);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemQuickTable.qt),
				BlockQuickTable.qt);
		GameRegistry.addShapedRecipe(new ItemStack(BlockQuickTable.qt), "cic",
				"iki", "cic", 'c', Blocks.crafting_table, 'i',
				Items.iron_ingot, 'k', Blocks.coal_block);
		if (event.getSide() == Side.CLIENT) {
			Minecraft
					.getMinecraft()
					.getRenderItem()
					.getItemModelMesher()
					.register(
							ItemQuickTable.qt,
							0,
							new ModelResourceLocation(QuickCrafting.MODID + ":"
									+ "quickTableItem", "inventory"));
			Minecraft
					.getMinecraft()
					.getRenderItem()
					.getItemModelMesher()
					.register(
							Item.getItemFromBlock(BlockQuickTable.qt),
							0,
							new ModelResourceLocation(QuickCrafting.MODID + ":"
									+ "quickTableBlock", "inventory"));
		}
	}

}