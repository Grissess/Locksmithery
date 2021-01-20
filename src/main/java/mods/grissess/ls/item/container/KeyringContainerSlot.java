package mods.grissess.ls.item.container;

import mods.grissess.ls.registry.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class KeyringContainerSlot extends Slot {
    public KeyringContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == Items.key && stack.hasTagCompound();
    }
}
