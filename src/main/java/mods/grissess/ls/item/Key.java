package mods.grissess.ls.item;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.data.BittingDescriptor;
import mods.grissess.ls.data.KeyBitting;
import mods.grissess.ls.proxy.Common;
import mods.grissess.ls.registry.CreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Key extends ItemWithDescriptor {
    public Key() {
        super();
        setRegistryName("key");
        setUnlocalizedName("Key");
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public void registerCustomModels(Common proxy) {
        super.registerCustomModels(proxy);
        proxy.setModelLocation(this, BittingDescriptor.VALUES.length,
                new ModelResourceLocation(
                        new ResourceLocation(
                                Locksmithery.MODID,
                                "key_creative"
                        ),
                        "inventory"
                )
        );
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
        super.getSubItems(tab, items);
        if(isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this, 1, BittingDescriptor.VALUES.length);
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

    public static void setBitting(ItemStack stack, KeyBitting bitting) {
        stack.setTagCompound(bitting.toNBT());
    }
}
