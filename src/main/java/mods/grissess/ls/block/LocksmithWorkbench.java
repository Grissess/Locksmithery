package mods.grissess.ls.block;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.te.LocksmithWorkbenchTE;
import mods.grissess.ls.gui.GUIS;
import mods.grissess.ls.registry.CreativeTab;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LocksmithWorkbench extends BlockContainer {
    public LocksmithWorkbench() {
        super(Material.WOOD);
        setRegistryName("locksmith_workbench");
        setUnlocalizedName("Locksmith Workbench");
        setCreativeTab(CreativeTab.tab);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new LocksmithWorkbenchTE();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        TileEntity te = worldIn.getTileEntity(pos);
        if(te != null && te instanceof LocksmithWorkbenchTE) {
            playerIn.openGui(Locksmithery.instance, GUIS.LOCKSMITH_WORKBENCH.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te != null && te instanceof LocksmithWorkbenchTE) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (LocksmithWorkbenchTE) te);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
