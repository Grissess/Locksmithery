package mods.grissess.item;

import mods.grissess.block.te.SecureBlockTE;
import mods.grissess.data.LocksetBitting;
import mods.grissess.registry.Blocks;
import mods.grissess.registry.CreativeTab;
import mods.grissess.registry.Items;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SecureBlockItem extends ItemBlock {
    public SecureBlockItem() {
        super(Blocks.secure_block);
        setRegistryName(getBlock().getRegistryName());
        setUnlocalizedName(getBlock().getUnlocalizedName());
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        LocksetBitting bitting = getBitting(player.getHeldItem(hand));
        if(bitting == null) return EnumActionResult.PASS;
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if(!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
            return false;
        SecureBlockTE te = (SecureBlockTE) world.getTileEntity(pos);
        te.setBitting(getBitting(stack));
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.hasTagCompound()) {
            tooltip.add("Keyed");
        } else {
            tooltip.add("Unkeyed");
        }
    }

    public static LocksetBitting getBitting(ItemStack stack) {
        if(stack == null) return null;
        if(stack.getItem() != Items.secure_block_item) return null;
        if(!stack.hasTagCompound()) return null;
        return LocksetBitting.fromNBT(stack.getTagCompound());
    }

    public static void setBitting(ItemStack stack, LocksetBitting bitting) {
        stack.setTagCompound(bitting.toNBT());
    }
}
