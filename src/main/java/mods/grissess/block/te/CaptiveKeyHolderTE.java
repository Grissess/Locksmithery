package mods.grissess.block.te;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class CaptiveKeyHolderTE extends BaseBittedTE {
    public ItemStack key = ItemStack.EMPTY;

    public boolean hasKey() {
        return !key.isEmpty();
    }

    public ItemStack takeKey() {
        ItemStack k = key;
        key = ItemStack.EMPTY;
        return k;
    }

    public void putKey(ItemStack stack) {
        key = stack.copy();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound,
                NonNullList.withSize(1, key)
        );
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, list);
        key = list.get(0);
    }
}
