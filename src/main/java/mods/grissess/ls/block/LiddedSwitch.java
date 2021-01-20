package mods.grissess.ls.block;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.te.LiddedSwitchTE;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class LiddedSwitch extends Block {
    public static final PropertyDirection DIRECTION = PropertyDirection.create("direction");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool OPEN = PropertyBool.create("open");

    public static final AxisAlignedBB OPEN_BB_X = new AxisAlignedBB(0.125, 0.0, 0.25, 0.875, 0.75, 0.75);
    public static final AxisAlignedBB CLOSED_BB_X = new AxisAlignedBB(0.125, 0.0, 0.25, 0.875, 0.375, 0.75);
    public static final AxisAlignedBB OPEN_BB_Z = new AxisAlignedBB(0.25, 0.0, 0.125, 0.75, 0.75, 0.875);
    public static final AxisAlignedBB CLOSED_BB_Z = new AxisAlignedBB(0.25, 0.0, 0.125, 0.75, 0.375, 0.875);

    // TODO: distinct sound
    public static final SoundEvent LOCKED_SOUND = new SoundEvent(new ResourceLocation(Locksmithery.MODID, "door_locked"));

    public boolean toggle;

    public LiddedSwitch(boolean toggle) {
        super(Material.IRON);
        this.toggle = toggle;
        if(toggle) {
            setUnlocalizedName("Lidded Lever");
            setRegistryName("lidded_lever");
        } else {
            setUnlocalizedName("Lidded Button");
            setRegistryName("lidded_button");
        }
        setBlockUnbreakable();
        setResistance(6000000f);
        setDefaultState(
                blockState.getBaseState()
                .withProperty(DIRECTION, EnumFacing.NORTH)
                .withProperty(POWERED, false)
                .withProperty(OPEN, false)
        );
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = getDefaultState();
        if(placer != null) {
            state = state.withProperty(DIRECTION, placer.getHorizontalFacing());
        }
        return state;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(SecureBlockBase.tryUnlock(worldIn, pos, playerIn) == SecureBlockBase.TryUnlock.SUCCEEDED) {
            if(!worldIn.isRemote) {
                InventoryHelper.spawnItemStack(
                        worldIn,
                        pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(toggle ? Items.lidded_lever_item : Items.lidded_button_item)
                );
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        if(state.getValue(OPEN)) {
            ItemStack inHand = playerIn.getHeldItem(hand);
            Item handItem = inHand.getItem();
            if(handItem == Items.key || handItem == Items.keyring) {
                worldIn.setBlockState(pos, state.withProperty(OPEN, false));
            } else {
                if(toggle) {
                    worldIn.setBlockState(pos, state.withProperty(POWERED, !state.getValue(POWERED)));
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK,
                            SoundCategory.BLOCKS, 0.3f,
                            state.getValue(POWERED) ? 0.5f : 0.6f  // Recall that this was the _previous_ state
                    );
                } else {
                    if(!state.getValue(POWERED)) {
                        worldIn.setBlockState(pos, state.withProperty(POWERED, true));
                        worldIn.scheduleUpdate(pos, this, 20);
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.6f);
                    }
                }
            }
        } else {
            if (SecureBlockBase.tryUnlock(worldIn, pos, playerIn, hand) == SecureBlockBase.TryUnlock.SUCCEEDED) {
                worldIn.setBlockState(pos, state.withProperty(OPEN, true));
            } else {
                worldIn.playSound(null, pos, LOCKED_SOUND, SoundCategory.BLOCKS, 0.3f, 0.9f + 0.2f * worldIn.rand.nextFloat());
            }
        }
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        worldIn.setBlockState(pos, state.withProperty(POWERED, false));
        worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.5f);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(DIRECTION).getAxis() == EnumFacing.Axis.Z ?
                (state.getValue(OPEN) ? OPEN_BB_X : CLOSED_BB_X) :
                (state.getValue(OPEN) ? OPEN_BB_Z : CLOSED_BB_Z);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new LiddedSwitchTE();
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) && blockState.getValue(DIRECTION) == side ? 15 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, OPEN, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // There are too many states for 16 bits here, but it shouldn't matter in practice anyway, right?
        return
                state.getValue(DIRECTION).getIndex() |
                        ((state.getValue(OPEN) ? 1 : 0) << 3);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(DIRECTION, EnumFacing.getFront(meta & 0x7))
                .withProperty(OPEN, (meta & 0x8) != 0);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
