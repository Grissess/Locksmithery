package mods.grissess.item;

import mods.grissess.data.LocksetBitting;
import mods.grissess.registry.CreativeTab;
import mods.grissess.registry.Items;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Lockset extends BaseItem {
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
            tooltip.add("Cut: " + Arrays.toString(bitting.pinSets));
        } else {
            tooltip.add("Uncut");
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        LocksetBitting bitting = getBitting(stack);
        if(bitting != null) return true;
        return super.hasEffect(stack);
    }

    public static LocksetBitting getBitting(ItemStack stack) {
        if(stack == null) return null;
        if(stack.getItem() != Items.lockset) return null;
        if(!stack.hasTagCompound()) return null;
        return LocksetBitting.fromNBT(stack.getTagCompound());
    }
}
