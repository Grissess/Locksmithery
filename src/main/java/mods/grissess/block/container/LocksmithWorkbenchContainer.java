package mods.grissess.block.container;

import mods.grissess.SecurityCraft;
import mods.grissess.block.te.LocksmithWorkbenchTE;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.KeyBitting;
import mods.grissess.data.LocksetBitting;
import mods.grissess.item.Key;
import mods.grissess.item.Lockset;
import mods.grissess.net.Channel;
import mods.grissess.registry.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LocksmithWorkbenchContainer extends Container {
    public BlockPos position;
    public World world;
    public LocksmithWorkbenchTE tileEntity;
    public EntityPlayer player;

    public Slot inputSlot;
    public Slot outputSlot;

    public LocksmithWorkbenchContainer(InventoryPlayer playerInv, LocksmithWorkbenchTE te) {
        world = te.getWorld();
        position = te.getPos();
        tileEntity = te;
        player = playerInv.player;
        assert tileEntity != null;

        inputSlot = new Slot(tileEntity, 0, 11, 8) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                if(stack == null) return false;
                Item item = stack.getItem();
                return (item == Items.key || item == Items.lockset) && !stack.hasTagCompound();
            }
        };
        addSlotToContainer(inputSlot);
        outputSlot = new Slot(tileEntity, 1, 12, 57) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;  // Don't allow items to be put in here
            }

            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
                /*if(world.isRemote) {
                    SecurityCraft.proxy.channel.sendToServer(
                            new Channel.PacketTakeResult(
                                    world,
                                    position
                            )
                    );
                }*/
                LocksmithWorkbenchContainer.this.inputSlot.putStack(ItemStack.EMPTY);
                return super.onTake(thePlayer, stack);
            }
        };
        addSlotToContainer(outputSlot);

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
        return playerIn.getDistanceSq(position.add(0.5, 0.5, 0.5)) <= 64d;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(IContainerListener listener: listeners) {
            listener.sendAllContents(this, tileEntity.getInventoryList());
            listener.sendAllWindowProperties(this, tileEntity);
        }
    }

    @Override
    public String toString() {
        return "LocksmithWorkbenchContainer{" +
                "position=" + position +
                ", world=" + world +
                ", tileEntity=" + tileEntity +
                ", player=" + player +
                ", inventoryItemStacks=" + inventoryItemStacks +
                ", inventorySlots=" + inventorySlots +
                ", windowId=" + windowId +
                ", listeners=" + listeners +
                '}';
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return (
                super.canMergeSlot(stack, slotIn) &&
                slotIn.inventory != tileEntity
        );
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack stack = slot.getStack();
        ItemStack newStack = stack.copy();

        if(index == 0) {  // The input slot
            if (!mergeItemStack(newStack, 2, 37, false))
                return ItemStack.EMPTY;
        } else if(index == 1) {  // The output slot
            if (!mergeItemStack(newStack, 2, 37, false))
                return ItemStack.EMPTY;
        } else {  // Anywhere in the player inventory
            if (!mergeItemStack(newStack, 0, 0, false))
                return ItemStack.EMPTY;
        }

        return newStack;
    }
}
