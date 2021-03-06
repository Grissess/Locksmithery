package mods.grissess.ls.block;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.te.SecureDoorTE;
import mods.grissess.ls.registry.CreativeTab;
import mods.grissess.ls.registry.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SecureDoor extends BlockDoor {
    public ResourceLocation DOOR_LOCKED_RESOURCE = new ResourceLocation(Locksmithery.MODID, "door_locked");
    public SoundEvent DOOR_LOCKED_SOUND = new SoundEvent(DOOR_LOCKED_RESOURCE);

    public SecureDoor() {
        super(Material.IRON);
        setUnlocalizedName("Secure Door");
        setRegistryName("secure_door");
        setBlockUnbreakable();
        setResistance(6000000f);
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        // This usually gets reassigned in SecureDoorItem anyway
        return new SecureDoorTE();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Items.secure_door_item);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        if(state.getValue(HALF) == EnumDoorHalf.UPPER) pos = pos.down();
        if(isOpen(worldIn, pos)) {
            toggleDoor(worldIn, pos, false);
            return true;
        }
        if(SecureBlockBase.tryUnlock(worldIn, pos, playerIn, hand) == SecureBlockBase.TryUnlock.SUCCEEDED) {
            toggleDoor(worldIn, pos, true);
        } else {
            worldIn.playSound(null, pos, DOOR_LOCKED_SOUND, SoundCategory.BLOCKS, 0.3f, 0.9f + 0.2f * worldIn.rand.nextFloat());
        }
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(Items.secure_door_item));
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (worldIn.isRemote) return;
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) pos = pos.down();
        if (SecureBlockBase.tryUnlock(worldIn, pos, playerIn) == SecureBlockBase.TryUnlock.SUCCEEDED) {
            InventoryHelper.spawnItemStack(
                    worldIn,
                    pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(Items.secure_door_item)
            );
            worldIn.setBlockToAir(pos);
            worldIn.setBlockToAir(pos.up());
        }
    }

        // Sorry kinda need to change this
        @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
            else if (blockIn != this)
            {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        }
        else
        {
            boolean flag1 = false;
            BlockPos blockpos1 = pos.up();
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

            if (iblockstate1.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn,  pos.down(), EnumFacing.UP))
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;

                if (iblockstate1.getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos1);
                }
            }

            if (flag1)
            {
                if (!worldIn.isRemote)
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else
            {
                boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1);

                if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && flag != ((Boolean)iblockstate1.getValue(POWERED)).booleanValue())
                {
                    worldIn.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

                    if (state.getValue(OPEN) && !flag)
                    {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, false), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playEvent(null, 1011, pos, 0);
                    }
                }
            }
        }
    }
}
