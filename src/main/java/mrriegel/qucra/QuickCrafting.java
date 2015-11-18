package mrriegel.qucra;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import appeng.api.recipes.IIngredient;
import appeng.recipes.game.ShapedRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@Mod(modid = QuickCrafting.MODID, version = QuickCrafting.VERSION)
public class QuickCrafting {
	public static final String MODID = "qucra";
	public static final String VERSION = "1.7.10-1.2";
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

//	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (Object o : CraftingManager.getInstance().getRecipeList()) {
			IRecipe r = (IRecipe) o;
			if (r instanceof ShapedRecipe) {
				if (!r.getRecipeOutput().getDisplayName().toLowerCase()
						.contains("q"))
					continue;
				System.out.println(" "+((ShapedRecipe) r).getIngredients().length+":"+r.getRecipeOutput().getDisplayName());
				for (Object oo : ((ShapedRecipe) r).getIngredients()) {
					try {
//						for (ItemStack s : ((IIngredient) oo).getItemStackSet())
//							System.out.println("   :" + s.getDisplayName());
						System.out.println("  :"+((IIngredient) oo).getItemStackSet());
					} catch (Exception e) {
						System.out.println(e.getClass());
					}
				}
			}
		}
	}
}