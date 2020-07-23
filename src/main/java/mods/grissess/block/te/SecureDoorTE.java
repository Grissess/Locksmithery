package mods.grissess.block.te;

import mods.grissess.block.SecureDoor;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.LocksetBitting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SecureDoorTE extends TileEntity {
    public LocksetBitting bitting;

    public SecureDoorTE() {
        this(BittingDescriptor.WOOD);
    }

    public SecureDoorTE(BittingDescriptor desc) {
        bitting = new LocksetBitting(desc);
    }

    public SecureDoorTE(LocksetBitting bitting) {
        this.bitting = bitting;
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
        return super.writeToNBT(compound);
    }
}
