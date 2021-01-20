package mods.grissess.ls.recipe;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.item.ILocksetItem;
import mods.grissess.ls.item.Lockset;
import mods.grissess.ls.registry.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ApplyLockset extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public ApplyLockset() {
        setRegistryName(
                Locksmithery.MODID,
                "apply_lockset"
        );
    }

    public ItemStack findApplicableItem(InventoryCrafting inv) {
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if(stack != null && (stack.getItem() instanceof ILocksetItem)) return stack;
        }
        return null;
    }

    public ItemStack findLockset(InventoryCrafting inv) {
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if(stack != null && stack.getItem() == Items.lockset) return stack;
        }
        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return findLockset(inv) != null && findApplicableItem(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack lockset = findLockset(inv);
        if(lockset == null) return null;
        ItemStack output = findApplicableItem(inv);
        if(output == null) return null;

        output = output.copy();
        output.setCount(1);
        ILocksetItem.setBitting(output, Lockset.getBitting(lockset));
        return output;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if(stack != null && stack.getItem() == Items.lockset) {
                stack = stack.copy();
                stack.setCount(1);
                list.set(slot, stack);
            }
        }
        return list;
    }
}
