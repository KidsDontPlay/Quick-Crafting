package mrriegel.qucra;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.google.common.eventbus.Subscribe;

import mrriegel.crunch.helper.InventoryHelper;
import mrriegel.crunch.inventory.InventoryCopy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = QuickCrafting.MODID, version = QuickCrafting.VERSION)
public class QuickCrafting {
	public static final String MODID = "qucra";
	public static final String VERSION = "1.7.10-1.0";
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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

}