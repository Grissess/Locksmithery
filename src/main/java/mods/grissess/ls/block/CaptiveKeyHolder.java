package mods.grissess.ls.block;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.te.CaptiveKeyHolderTE;
import mods.grissess.ls.data.KeyBitting;
import mods.grissess.ls.data.LocksetBitting;
import mods.grissess.ls.item.Key;
import mods.grissess.ls.registry.CreativeTab;
import mods.grissess.ls.registry.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CaptiveKeyHolder extends Block {
    public static final PropertyDirection DIRECTION = PropertyDirection.create("direction");
    public static final PropertyBool HAS_KEY = PropertyBool.create("has_key");
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    // TODO: distinct sound
    public static final SoundEvent LOCKED_SOUND = new SoundEvent(new ResourceLocation(Locksmithery.MODID, "door_locked"));

    public boolean output;

    public CaptiveKeyHolder(boolean output) {
        super(Material.IRON);
        this.output = output;

        if(output) {
            setUnlocalizedName("Captive Key Switch");
            setRegistryName("captive_key_switch");
        } else {
            setUnlocalizedName("Captive Key Latch");
            setRegistryName("captive_key_latch");
        }

        setBlockUnbreakable();
        setResistance(6000000f);
        setDefaultState(
                blockState.getBaseState()
                .withProperty(DIRECTION, EnumFacing.NORTH)
                .withProperty(POWERED, false)
                .withProperty(HAS_KEY, false)
        );
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new CaptiveKeyHolderTE();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, HAS_KEY, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DIRECTION).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DIRECTION, EnumFacing.getFront(meta));
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return output;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return output && blockState.getValue(HAS_KEY) ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return output && blockState.getValue(HAS_KEY) && blockState.getValue(DIRECTION) == side ? 15 : 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!output) {
            boolean powered = worldIn.isBlockPowered(pos);
            if(powered != state.getValue(POWERED)) {
                worldIn.setBlockState(pos, state.withProperty(POWERED, powered));
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DIRECTION, facing);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(SecureBlockBase.tryUnlock(worldIn, pos, playerIn) == SecureBlockBase.TryUnlock.SUCCEEDED) {
            if(!worldIn.isRemote) {
                InventoryHelper.spawnItemStack(
                        worldIn,
                        pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(output ? Items.captive_key_switch : Items.captive_key_latch)
                );
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        TileEntity te = worldIn.getTileEntity(pos);
        assert te != null && te instanceof CaptiveKeyHolderTE;
        CaptiveKeyHolderTE cte = (CaptiveKeyHolderTE) te;
        if(cte.hasKey()) {
            if(!output && !state.getValue(POWERED)) {
                worldIn.playSound(null, pos, LOCKED_SOUND, SoundCategory.BLOCKS, 0.3f, 0.9f + 0.2f * worldIn.rand.nextFloat());
            } else {
                ItemStack key = cte.takeKey();
                if(!playerIn.addItemStackToInventory(key)) {
                    InventoryHelper.spawnItemStack(
                            worldIn,
                            pos.getX(), pos.getY(), pos.getZ(),
                            key
                    );
                }
                IBlockState newState = state.withProperty(HAS_KEY, false);
                if(output) {
                    newState = newState.withProperty(POWERED, false);
                }
                worldIn.setBlockState(pos, newState);
            }
        } else {
            ItemStack handStack = playerIn.getHeldItem(hand);
            if(handStack != null && handStack.getItem() == Items.key) {
                KeyBitting kb = Key.getBitting(handStack);
                LocksetBitting lb = cte.getBitting();
                if(lb.fits(kb)) {
                    cte.putKey(handStack.splitStack(1));
                    IBlockState newState = state.withProperty(HAS_KEY, true);
                    if(output) {
                        newState = newState.withProperty(POWERED, true);
                    }
                    worldIn.setBlockState(pos, newState);
                } else {
                    worldIn.playSound(null, pos, LOCKED_SOUND, SoundCategory.BLOCKS, 0.3f, 0.9f + 0.2f * worldIn.rand.nextFloat());
                }
            }
        }
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(HAS_KEY) ? 15 : 0;
    }
}
