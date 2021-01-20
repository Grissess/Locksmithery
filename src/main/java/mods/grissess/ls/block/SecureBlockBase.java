package mods.grissess.ls.block;

import mods.grissess.ls.block.te.IBittedTE;
import mods.grissess.ls.data.KeyBitting;
import mods.grissess.ls.data.LocksetBitting;
import mods.grissess.ls.item.Key;
import mods.grissess.ls.item.Keyring;
import mods.grissess.ls.registry.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecureBlockBase {
    public enum TryUnlock {
        INVALID, FAILED, SUCCEEDED
    }

    public static TryUnlock tryUnlock(World world, BlockPos pos, EntityPlayer player) {
        return tryUnlockWith(world, pos, player.getHeldItem(
                player.swingingHand == null ? EnumHand.MAIN_HAND : player.swingingHand
        ));
    }

    public static TryUnlock tryUnlock(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
        return tryUnlockWith(world, pos, player.getHeldItem(hand));
    }

    public static TryUnlock tryUnlockWith(World world, BlockPos pos, ItemStack handStack) {
        if(handStack == null || handStack.isEmpty()) return TryUnlock.INVALID;
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof IBittedTE)) return TryUnlock.INVALID;
        LocksetBitting lb = ((IBittedTE) te).getBitting();
        if(handStack.getItem() == Items.key) {
            KeyBitting kb = Key.getBitting(handStack);
            if (kb == null) return TryUnlock.INVALID;
            if (lb.fits(kb)) return TryUnlock.SUCCEEDED;
            return TryUnlock.FAILED;
        }
        if(handStack.getItem() == Items.keyring) {
            IInventory inv = Keyring.getInventory(handStack);
            for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
                ItemStack stack = inv.getStackInSlot(slot);
                if(stack.isEmpty()) continue;
                if(tryUnlockWith(world, pos, stack) == TryUnlock.SUCCEEDED) return TryUnlock.SUCCEEDED;
            }
            return TryUnlock.FAILED;
        }
        return TryUnlock.INVALID;
    }
}
