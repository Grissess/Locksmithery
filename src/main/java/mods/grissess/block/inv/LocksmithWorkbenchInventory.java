package mods.grissess.block.inv;

import mods.grissess.data.BittingDescriptor;
import mods.grissess.registry.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class LocksmithWorkbenchInventory extends InventoryBasic {
    protected int[] cuts = new int[BittingDescriptor.MAX_POSITIONS];

    public LocksmithWorkbenchInventory() {
        super("LocksmithWorkbench", true, 2);
    }

    public ItemStack getInputStack() { return getStackInSlot(0); }
    public ItemStack getOutputStack() { return getStackInSlot(1); }

    @Override
    public int getFieldCount() {
        return BittingDescriptor.MAX_POSITIONS;
    }

    @Override
    public int getField(int id) {
        return cuts[id];
    }

    @Override
    public void setField(int id, int value) {
        cuts[id] = value;
        ItemStack is = getInputStack();
        if(is != null && is.getItem() == Items.key) {
            cuts[id] = ensureAtMostOneBit(cuts[id]);
            fieldChanged(id);
        }
    }

    public void fieldChanged(int index) {}

    public static int ensureAtMostOneBit(int input) {
        for(int bit = BittingDescriptor.MAX_SETTINGS - 1; bit >= 0; bit--) {
            int mask = 1 << bit;
            if((mask & input) != 0) return mask;
        }
        return 0;
    }
}
