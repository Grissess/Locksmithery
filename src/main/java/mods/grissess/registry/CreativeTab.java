package mods.grissess.registry;

import mods.grissess.data.BittingDescriptor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {
    public static final CreativeTabs tab = new CreativeTabs("tabSecurityCraft") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.key, 1, BittingDescriptor.GOLD.ordinal());
        }
    };
}
