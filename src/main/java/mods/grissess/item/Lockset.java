package mods.grissess.item;

import mods.grissess.data.LocksetBitting;
import mods.grissess.registry.CreativeTab;
import mods.grissess.registry.Items;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Lockset extends ItemWithDescriptor {
    public Lockset() {
        super();
        setRegistryName("lockset");
        setUnlocalizedName("Lockset");
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.hasTagCompound()) {
            LocksetBitting bitting = LocksetBitting.fromNBT(stack.getTagCompound());
            tooltip.add("Cut: " + bitting.pinSets);
        } else {
            tooltip.add("Uncut");
        }
    }

    public static LocksetBitting getBitting(ItemStack stack) {
        if(stack == null) return null;
        if(stack.getItem() != Items.lockset) return null;
        if(!stack.hasTagCompound()) return null;
        return LocksetBitting.fromNBT(stack.getTagCompound());
    }

    public static void setBitting(ItemStack stack, LocksetBitting bitting) {
        stack.setTagCompound(bitting.toNBT());
    }
}
