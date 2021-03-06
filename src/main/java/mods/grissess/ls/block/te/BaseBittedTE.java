package mods.grissess.ls.block.te;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.data.BittingDescriptor;
import mods.grissess.ls.data.LocksetBitting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseBittedTE extends TileEntity implements IBittedTE {
    public LocksetBitting bitting;

    public BaseBittedTE() {
        this(BittingDescriptor.WOOD);
    }
    public BaseBittedTE(BittingDescriptor desc) {
        bitting = new LocksetBitting(desc);
    }
    public BaseBittedTE(LocksetBitting bit) {
        bitting = bit;
    }

    public LocksetBitting getBitting() {
        return bitting;
    }
    public void setBitting(LocksetBitting bitting) {
        this.bitting = bitting;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("bitting")) {
            bitting = LocksetBitting.fromNBT(compound.getCompoundTag("bitting"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("bitting", bitting.toNBT());
        NBTTagCompound result = super.writeToNBT(compound);
        if(Locksmithery.migrating) {
            ResourceLocation rl = TileEntity.getKey(getClass());
            result.setString("id", (new ResourceLocation(Locksmithery.MODID, rl.getResourcePath())).toString());
        }
        return result;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
