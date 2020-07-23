package mods.grissess.block.te;

import mods.grissess.block.container.LocksmithWorkbenchContainer;
import mods.grissess.block.inv.LocksmithWorkbenchInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;

public class LocksmithWorkbenchTE extends TileEntity implements IInteractionObject {
    public IInventory inventory = new LocksmithWorkbenchInventory();

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new LocksmithWorkbenchContainer(world, pos, playerInventory);
    }

    @Override
    public String getGuiID() {
        return "securitycraft:locksmith_workbench";
    }

    @Override
    public String getName() {
        return "container.locksmithworkbench";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
