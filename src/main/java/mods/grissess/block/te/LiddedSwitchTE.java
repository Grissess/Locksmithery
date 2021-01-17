package mods.grissess.block.te;

import mods.grissess.block.LiddedSwitch;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.LocksetBitting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LiddedSwitchTE extends TileEntity implements IBittedTE, ITickable {
    public LocksetBitting bitting;
    public float open = 0f;
    public float powered = 0f;
    public float prevOpen = 0f;
    public float prevPowered = 0f;

    public LiddedSwitchTE(BittingDescriptor desc) {
        bitting = new LocksetBitting(desc);
    }
    public LiddedSwitchTE(LocksetBitting bitting) {
        this.bitting = bitting;
    }
    public LiddedSwitchTE() {
        this(BittingDescriptor.WOOD);
    }

    @Override
    public LocksetBitting getBitting() {
        return bitting;
    }

    @Override
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
        return super.writeToNBT(compound);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        prevOpen = open;
        prevPowered = powered;
        IBlockState state = hasWorld() ? getWorld().getBlockState(getPos()) : null;
        open += state != null && state.getValue(LiddedSwitch.OPEN) ? 0.1f : -0.1f;
        powered += state != null && state.getValue(LiddedSwitch.POWERED) ? 0.25f : -0.25f;
        open = Math.max(0f, Math.min(1f, open));
        powered = Math.max(0f, Math.min(1f, powered));
    }
}
