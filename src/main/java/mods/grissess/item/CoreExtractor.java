package mods.grissess.item;

import mods.grissess.block.te.IBittedTE;
import mods.grissess.data.LocksetBitting;
import mods.grissess.item.render.CoreExtractorRender;
import mods.grissess.proxy.Common;
import mods.grissess.registry.CreativeTab;
import mods.grissess.registry.Items;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void registerCustomModels(Common proxy) {
        proxy.setModelLocation(this, 0,
                new ModelResourceLocation(new ResourceLocation("securitycraft", "teisr_core_extractor"), "inventory")
        );
    }
}
