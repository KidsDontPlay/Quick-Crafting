package mrriegel.qucra;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = QuickCrafting.MODID, version = QuickCrafting.VERSION)
public class QuickCrafting {
	public static final String MODID = "qucra";
	public static final String VERSION = "1.7.10-1.1";
	public static final String MODNAME = "QuickCrafting";

	@Instance(QuickCrafting.MODID)
	public static QuickCrafting instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// File configFile = event.getSuggestedConfigurationFile();
		// ConfigurationHandler.config = new Configuration(configFile);
		// ConfigurationHandler.config.load();
		// ConfigurationHandler.refreshConfig();
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
	}

}