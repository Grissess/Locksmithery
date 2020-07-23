package mods.grissess.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {
    public static final CreativeTabs tab = new CreativeTabs("tabSecurityCraft") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.key);
        }
    };
}
