package mods.grissess.ls.item;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.te.IBittedTE;
import mods.grissess.ls.data.LocksetBitting;
import mods.grissess.ls.item.render.CoreExtractorRender;
import mods.grissess.ls.proxy.Common;
import mods.grissess.ls.registry.CreativeTab;
import mods.grissess.ls.registry.Items;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CoreExtractor extends Item implements ICustomModelRegistration {
    public CoreExtractor() {
        setRegistryName("core_extractor");
        setUnlocalizedName("Core Extractor");
        setCreativeTab(CreativeTab.tab);
        setTileEntityItemStackRenderer(
                new CoreExtractorRender()
        );
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity te = world.getTileEntity(pos);
        if(te != null && (te instanceof IBittedTE)) {
            if(world.isRemote) return EnumActionResult.SUCCESS;
            LocksetBitting bitting = ((IBittedTE) te).getBitting();
            ItemStack stack = new ItemStack(Items.lockset, 1, bitting.descriptor.ordinal());
            Lockset.setBitting(stack, bitting);
            if(!player.addItemStackToInventory(stack)) {
                InventoryHelper.spawnItemStack(
                        world,
                        player.posX, player.posY, player.posZ,
                        stack
                );
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        TextFormatting tf = TextFormatting.LIGHT_PURPLE, r = TextFormatting.RESET;
        tooltip.add(tf + "Debug Tool" + r);
        tooltip.add("Right click or craft with a keyed block");
    }

    @Override
    public void registerCustomModels(Common proxy) {
        proxy.setModelLocation(this, 0,
                new ModelResourceLocation(
                        new ResourceLocation(
                                Locksmithery.MODID,
                                "teisr_core_extractor"),
                        "inventory"
                )
        );
        ModelBakery.registerItemVariants(this,
                new ModelResourceLocation(
                        new ResourceLocation(
                                Locksmithery.MODID,
                                "core_extractor_flipped"
                        ),
                        "inventory"
                )
        );
    }
}
