package mods.grissess.block.te;

import mods.grissess.block.SecureDoor;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.LocksetBitting;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecureDoorTE extends BaseBittedTE {
    public SecureDoorTE getProxyTE() {
        if(hasWorld() && getWorld().getBlockState(getPos()).getValue(SecureDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            TileEntity te = getWorld().getTileEntity(getPos().down());
            if(te instanceof SecureDoorTE) return (SecureDoorTE) te;
        }
        return null;
    }

    @Override
    public LocksetBitting getBitting() {
        SecureDoorTE proxy = getProxyTE();
        if(proxy != null) return proxy.getBitting();
        return super.getBitting();
    }

    @Override
    public void setBitting(LocksetBitting bitting) {
        SecureDoorTE proxy = getProxyTE();
        if(proxy != null) {
            proxy.setBitting(bitting);
        } else {
            super.setBitting(bitting);
        }
    }
}
