package mods.grissess.ls.block.te;

import mods.grissess.ls.block.SecureDoor;
import mods.grissess.ls.data.LocksetBitting;
import net.minecraft.block.BlockDoor;
import net.minecraft.tileentity.TileEntity;

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
