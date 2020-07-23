package mods.grissess.block.container;

import mods.grissess.block.inv.LocksmithWorkbenchInventory;
import mods.grissess.block.te.LocksmithWorkbenchTE;
import mods.grissess.registry.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
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

    public LocksmithWorkbenchContainer(World w, BlockPos pos, InventoryPlayer playerInv) {
        world = w;
        position = pos;
        tileEntity = (LocksmithWorkbenchTE) w.getTileEntity(pos);
        player = playerInv.player;
        assert tileEntity != null;

        LocksmithWorkbenchInventory inv = (LocksmithWorkbenchInventory) tileEntity.inventory;
        addSlotToContainer(new Slot(inv, 0, 10, 7) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                if(stack == null) return false;
                Item item = stack.getItem();
                return (item == Items.key || item == Items.lockset) && !stack.hasTagCompound();
            }
        });
        addSlotToContainer(new Slot(inv, 1, 10, 55) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;  // Don't allow items to be put in here
            }
        });

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
}
