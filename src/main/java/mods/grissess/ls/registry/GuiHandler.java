package mods.grissess.ls.registry;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.container.LocksmithWorkbenchContainer;
import mods.grissess.ls.block.te.LocksmithWorkbenchTE;
import mods.grissess.ls.gui.GUIS;
import mods.grissess.ls.gui.GuiKeyring;
import mods.grissess.ls.gui.GuiLocksmithing;
import mods.grissess.ls.item.container.KeyringContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    // Called from mod class
    public static void registerGuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(
                Locksmithery.instance,
                new GuiHandler()
        );
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GUIS.LOCKSMITH_WORKBENCH.ordinal()) {
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);
            if(te == null) return null;

            if (!(te instanceof LocksmithWorkbenchTE)) return null;
            LocksmithWorkbenchTE lwte = (LocksmithWorkbenchTE) te;
            return new LocksmithWorkbenchContainer(player.inventory, lwte);
        }

        if(ID == GUIS.KEYRING.ordinal()) {
            return new KeyringContainer(player.inventory, x);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GUIS.LOCKSMITH_WORKBENCH.ordinal()) {
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);
            if(te == null) return null;

            if(!(te instanceof LocksmithWorkbenchTE)) return null;
            LocksmithWorkbenchTE lwte = (LocksmithWorkbenchTE) te;
            return new GuiLocksmithing(player.inventory, lwte);
        }

        if(ID == GUIS.KEYRING.ordinal()) {
            return new GuiKeyring(player.inventory, x);
        }

        return null;
    }
}
