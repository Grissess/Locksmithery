package mods.grissess.ls.item;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.gui.GUIS;
import mods.grissess.ls.registry.CreativeTab;
import mods.grissess.ls.registry.Items;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class Keyring extends Item {
    public static final int MAX_KEYS = 27;

    public Keyring() {
        super();
        setRegistryName("keyring");
        setUnlocalizedName("Keyring");
        setCreativeTab(CreativeTab.tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.openGui(
                Locksmithery.instance,
                GUIS.KEYRING.ordinal(),
                worldIn,
                playerIn.inventory.currentItem, 0, 0
        );
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        IInventory inv = getInventory(stack);
        if(inv != null) {
            int keys = 0;
            for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
                if(!inv.getStackInSlot(slot).isEmpty()) keys++;
            }
            tooltip.add(keys + " key" + (keys == 1 ? "" : "s"));
        }
    }

    public static IInventory getInventory(ItemStack stack) {
        if(stack == null) return null;
        if(stack.getItem() != Items.keyring) return null;
        InventoryBasic inv = new InventoryBasic("Keyring", false, MAX_KEYS);
        if(stack.hasTagCompound()) {
            NonNullList<ItemStack> stacks = NonNullList.withSize(MAX_KEYS, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(stack.getTagCompound(), stacks);
            for(int i = 0; i < MAX_KEYS; i++) {
                inv.setInventorySlotContents(i, stacks.get(i));
            }
        }
        return inv;
    }

    public static void setInventory(ItemStack stack, IInventory inv) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(MAX_KEYS, ItemStack.EMPTY);
        for(int slot = 0; slot < MAX_KEYS && slot < inv.getSizeInventory(); slot++) {
            stacks.set(slot, inv.getStackInSlot(slot));
        }
        NBTTagCompound nbt = new NBTTagCompound();
        ItemStackHelper.saveAllItems(nbt, stacks);
        stack.setTagCompound(nbt);
    }

    public static void setInventory(ItemStack stack, IItemHandler inv) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(MAX_KEYS, ItemStack.EMPTY);
        for(int slot = 0; slot < MAX_KEYS && slot < inv.getSlots(); slot++) {
            stacks.set(slot, inv.getStackInSlot(slot));
        }
        NBTTagCompound nbt = new NBTTagCompound();
        ItemStackHelper.saveAllItems(nbt, stacks);
        stack.setTagCompound(nbt);
    }
}
