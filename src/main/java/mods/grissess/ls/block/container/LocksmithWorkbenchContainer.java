package mods.grissess.ls.block.container;

import mods.grissess.ls.block.te.LocksmithWorkbenchTE;
import mods.grissess.ls.registry.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
                slotIn.slotNumber != 1
        );
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack stack = slot.getStack();
        ItemStack oldStack = stack.copy();

        if(index == 0) {  // The input slot
            if (!mergeItemStack(stack, 2, 38, false))
                return ItemStack.EMPTY;
        } else if(index == 1) {  // The output slot
            if (!mergeItemStack(stack, 2, 38, false))
                return ItemStack.EMPTY;

            // In the particular case of the output, also remove an equivalent
            // amount of inputs
            Slot inputSlot = inventorySlots.get(0);
            if(stack.isEmpty()) {
                inventorySlots.get(0).putStack(ItemStack.EMPTY);
            } else if(inputSlot.getHasStack()) {
                ItemStack inputStack = inputSlot.getStack();
                if(inputStack != null) {
                    inputStack.shrink(oldStack.getCount() - stack.getCount());
                }
            }
        } else {  // Anywhere in the player inventory
            // For simplicity, only support moving to the input slot
            if (!mergeItemStack(stack, 0, 1, false))
                return ItemStack.EMPTY;
        }

        if(stack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        return oldStack;
    }
}
