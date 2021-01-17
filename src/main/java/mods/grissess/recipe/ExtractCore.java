package mods.grissess.recipe;

import mods.grissess.data.LocksetBitting;
import mods.grissess.item.ILocksetItem;
import mods.grissess.registry.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ExtractCore extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public ExtractCore() {
        setRegistryName("securitycraft:extract_core");
    }

    public static class SearchResult {
        public ItemStack stack;
        public int slot;

        public SearchResult(ItemStack stack, int slot) {
            this.stack = stack;
            this.slot = slot;
        }

        public boolean wasFound() {
            return slot != -1;
        }

        public static SearchResult NOT_FOUND = new SearchResult(ItemStack.EMPTY, -1);
    }

    public SearchResult findCoreExtractor(InventoryCrafting inv) {
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if(stack != null && stack.getItem() == Items.core_extractor)
                return new SearchResult(stack, slot);
        }
        return SearchResult.NOT_FOUND;
    }

    public SearchResult findLockable(InventoryCrafting inv) {
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if(stack != null && (stack.getItem() instanceof ILocksetItem))
                return new SearchResult(stack, slot);
        }
        return SearchResult.NOT_FOUND;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        SearchResult lockable = findLockable(inv);
        if(!lockable.wasFound()) return false;
        return findCoreExtractor(inv).wasFound() && ILocksetItem.getBitting(lockable.stack) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        SearchResult lockable = findLockable(inv);
        if(!lockable.wasFound()) return null;
        LocksetBitting bitting = ILocksetItem.getBitting(lockable.stack);
        if(bitting == null) return null;

        ItemStack output = new ItemStack(Items.lockset, 1, bitting.descriptor.ordinal());
        ILocksetItem.setBitting(output, bitting);
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
        SearchResult core = findCoreExtractor(inv);
        SearchResult lockable = findLockable(inv);
        if(core.wasFound())
            list.set(core.slot, core.stack.copy());
        if(lockable.wasFound())
            list.set(lockable.slot, lockable.stack.copy());
        return list;
    }
}
