package mrriegel.qucra;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockQuickTable extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon icon;

	public BlockQuickTable() {
		super(Material.wood);
		this.setHarvestLevel("axe", 1);
		this.setHardness(2.0F);
		this.setCreativeTab(CreativeTab.tab1);
		this.setBlockName(QuickCrafting.MODID + ":" + "quickTableBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		icon = reg.registerIcon(QuickCrafting.MODID + ":" + "quickTableBlock");

	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float p_149727_7_,
			float p_149727_8_, float p_149727_9_) {
		if (!world.isRemote)
			player.openGui(QuickCrafting.instance, 0, world, 0, 0, 0);
		return true;
	}

	public static final Block qt = new BlockQuickTable();

	public static void init() {
		GameRegistry.registerBlock(qt, "quickTableBlock");
	}

}
