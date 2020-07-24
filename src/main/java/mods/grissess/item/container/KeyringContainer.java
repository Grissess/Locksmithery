package mods.grissess.item.container;

import mods.grissess.item.Keyring;
import mods.grissess.registry.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class KeyringContainer extends Container {
    public IInventory keyringInv;
    public ItemStack keyring;
    public InventoryPlayer playerInv;
    public int keyringSlot;

    public static final int ROWS = (Keyring.MAX_KEYS + 8) / 9;

    public KeyringContainer(InventoryPlayer playerInv, int keyringSlot) {
        this.playerInv = playerInv;
        this.keyringSlot = keyringSlot;
        keyring = playerInv.getStackInSlot(keyringSlot);
        assert keyring.getItem() == Items.keyring;
        keyringInv = Keyring.getInventory(keyring);

        for (int j = 0; j < ROWS; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new KeyringContainerSlot(keyringInv, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        bindPlayerSlots(playerInv);
    }

    public void bindPlayerSlots(InventoryPlayer playerInventory) {
        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        ItemStack newRing = keyring.copy();
        Keyring.setInventory(newRing, keyringInv);
        playerInv.mainInventory.set(keyringSlot, newRing);
        super.onContainerClosed(playerIn);
    }
}
