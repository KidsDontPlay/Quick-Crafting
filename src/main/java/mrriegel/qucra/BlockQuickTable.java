package mrriegel.qucra;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockQuickTable extends Block {

	public BlockQuickTable() {
		super(Material.wood);
		this.setHarvestLevel("axe", 1);
		this.setHardness(2.0F);
		this.setCreativeTab(CreativeTab.tab1);
		this.setUnlocalizedName(QuickCrafting.MODID + ":" + "quickTableBlock");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote)
			playerIn.openGui(QuickCrafting.instance, 0, worldIn, 0, 0, 0);
		return true;
	}

	public static final Block qt = new BlockQuickTable();

	public static void init() {
		GameRegistry.registerBlock(qt, "quickTableBlock");
	}

}