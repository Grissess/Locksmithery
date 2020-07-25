package mods.grissess.item;

import mods.grissess.data.BittingDescriptor;
import mods.grissess.proxy.Common;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWithDescriptor extends Item implements ICustomModelRegistration {
    public ItemWithDescriptor() {
        super();
        setHasSubtypes(true);
    }

    @Override
    public void registerCustomModels(Common proxy) {
        for(BittingDescriptor desc: BittingDescriptor.VALUES) {
            ResourceLocation rl = getRegistryName();
            rl = new ResourceLocation(rl.toString() + "_" + desc.name().toLowerCase());
            proxy.setModelLocation(this, desc.ordinal(),
                    new ModelResourceLocation(rl, "inventory")
            );
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) {
            for(BittingDescriptor desc: BittingDescriptor.VALUES) {
                items.add(new ItemStack(this, 1, desc.ordinal()));
            }
        }
    }

    public static BittingDescriptor getBittingDescriptor(ItemStack stack) {
        if(stack == null) return null;
        int meta = stack.getMetadata();
        if(meta < 0 || meta >= BittingDescriptor.VALUES.length) return null;
        return BittingDescriptor.VALUES[meta];
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        BittingDescriptor desc = getBittingDescriptor(stack);
        if(desc != null) {
            tooltip.add("Tier: " + desc.formattedName());
        }
    }
}
