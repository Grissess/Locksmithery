package mods.grissess.item;

import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.KeyBitting;
import mods.grissess.registry.CreativeTab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Key extends BaseItem {
    public Key() {
        super();
        setRegistryName("key");
        setUnlocalizedName("Key");
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        KeyBitting bitting = getBitting(stack);
        if(bitting == null) {
            tooltip.add("Uncut");
        } else {
            if(bitting.overrides) {
                tooltip.add(TextFormatting.LIGHT_PURPLE + "Creative Key");
            } else {
                tooltip.add("Bitting: " + Arrays.toString(bitting.pins));
            }
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(this);
            stack.setTagCompound(KeyBitting.OVERRIDE_BITTING.toNBT());
            items.add(stack);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        KeyBitting bitting = getBitting(stack);
        if(bitting != null && bitting.overrides) return true;
        return super.hasEffect(stack);
    }

    public static KeyBitting getBitting(ItemStack stack) {
        if(stack == null) return null;
        if(!(stack.getItem() instanceof Key)) return null;
        if(!stack.hasTagCompound()) return null;
        return KeyBitting.fromNBT(stack.getTagCompound());
    }
}
