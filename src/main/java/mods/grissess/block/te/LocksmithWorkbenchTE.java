package mods.grissess.block.te;

import mods.grissess.block.container.LocksmithWorkbenchContainer;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.data.KeyBitting;
import mods.grissess.data.LocksetBitting;
import mods.grissess.item.Key;
import mods.grissess.item.Lockset;
import mods.grissess.registry.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.*;

public class LocksmithWorkbenchTE extends TileEntity implements IInventory {
    protected NonNullList<Integer> cuts = NonNullList.withSize(
            BittingDescriptor.MAX_POSITIONS, 0
    );
    protected NonNullList<ItemStack> slots = NonNullList.withSize(
            2, ItemStack.EMPTY
    );

    public ItemStack getInputStack() { return getStackInSlot(0); }
    public ItemStack getOutputStack() { return getStackInSlot(1); }
    public Set<Integer> getAllCuts(int pos) {
        Set<Integer> ret = new HashSet<>();
        int cutMask = cuts.get(pos);
        for(int bit = 0; bit < BittingDescriptor.MAX_SETTINGS; bit++) {
            if((cutMask & (1 << bit)) != 0) ret.add(bit);
        }
        return ret;
    }
    public Integer getSingleCut(int pos) {
        Set<Integer> cut = getAllCuts(pos);
        Iterator<Integer> iter = cut.iterator();
        if(!iter.hasNext()) return null;
        return iter.next();
    }
    public boolean hasAllNecessaryCuts() {
        ItemStack input = getInputStack();
        if(input == null) return false;
        BittingDescriptor desc = null;
        if(input.getItem() == Items.key) {
            desc = Key.getBittingDescriptor(input);
        }
        if(input.getItem() == Items.lockset) {
            desc = Lockset.getBittingDescriptor(input);
        }
        if(desc == null) return false;
        for(int pos = 0; pos < desc.positions; pos++) {
            if(getAllCuts(pos).isEmpty()) return false;
        }
        return true;
    }
    public int[] getCutsAsArray() {
        int[] array = new int[cuts.size()];
        for(int i = 0; i < cuts.size(); i++) {
            array[i] = cuts.get(i);
        }
        return array;
    }
    public void setAllCuts(int[] cutsIn) {
        for(int i = 0; i < cutsIn.length && i < BittingDescriptor.MAX_POSITIONS; i++) {
            cuts.set(i, cutsIn[i]);
        }
        updateOutput();
    }
    public NonNullList<ItemStack> getInventoryList() { return slots; }

    public static int ensureAtMostOneBit(int input) {
        for(int bit = BittingDescriptor.MAX_SETTINGS - 1; bit >= 0; bit--) {
            int mask = 1 << bit;
            if((mask & input) != 0) return mask;
        }
        return 0;
    }

    public void updateOutput() {
        ItemStack oldOutput = getOutputStack();
        slots.set(1, ItemStack.EMPTY);
        ItemStack inputStack = getInputStack();
        if(inputStack == null || !hasAllNecessaryCuts()) {
            if(!oldOutput.isEmpty()) markDirty();
            return;
        }

        if(inputStack.getItem() == Items.key) {
            ItemStack outputStack = new ItemStack(Items.key, inputStack.getCount(), inputStack.getMetadata());
            KeyBitting bitting = new KeyBitting(Key.getBittingDescriptor(inputStack));
            for(int pos = 0; pos < bitting.descriptor.positions; pos++) {
                bitting.setPin(pos, getSingleCut(pos));
            }
            Key.setBitting(outputStack, bitting);
            slots.set(1, outputStack);
        }
        if(inputStack.getItem() == Items.lockset) {
            ItemStack outputStack = new ItemStack(Items.lockset, inputStack.getCount(), inputStack.getMetadata());
            LocksetBitting bitting = new LocksetBitting(Lockset.getBittingDescriptor(inputStack));
            for(int pos = 0; pos < bitting.descriptor.positions; pos++) {
                bitting.getPinSets(pos).addAll(getAllCuts(pos));
            }
            Lockset.setBitting(outputStack, bitting);
            slots.set(1, outputStack);
        }
        if(!ItemStack.areItemStacksEqual(oldOutput, getOutputStack())) markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setIntArray("cuts", getCutsAsArray());
        ItemStackHelper.saveAllItems(compound, slots);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("cuts")) {
            int[] array = compound.getIntArray("cuts");
            for(int cut = 0; cut < cuts.size() && cut < array.length; cut++) {
                cuts.set(cut, array[cut]);
            }
        }
        ItemStackHelper.loadAllItems(compound, slots);
    }

    @Override
    public int getSizeInventory() {
        return slots.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack: slots) {
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(slots, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(slots, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        slots.set(index, stack);
        updateOutput();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64d;
    }

    @Override
    public void openInventory(EntityPlayer player) { }

    @Override
    public void closeInventory(EntityPlayer player) { }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        switch(index) {
            case 0:
                return stack != null && (
                        stack.getItem() == Items.key ||
                        stack.getItem() == Items.cylinder
                );
        }
        return false;
    }

    @Override
    public int getField(int id) {
        return cuts.get(id);
    }

    @Override
    public void setField(int id, int value) {
        ItemStack stack = getInputStack();
        boolean modified = false;
        if(stack != null && stack.getItem() == Items.key) {
            int newValue = ensureAtMostOneBit(value);
            modified = (value != newValue);
            value = newValue;
        }
        cuts.set(id, value);
        if(modified) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 2);
        }
    }

    @Override
    public int getFieldCount() {
        return cuts.size();
    }

    @Override
    public void clear() {
        cuts = NonNullList.withSize(
                BittingDescriptor.MAX_POSITIONS, 0
        );
        slots = NonNullList.withSize(
                2, ItemStack.EMPTY
        );
    }

    @Override
    public String getName() {
        return "container.locksmith_workbench";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    protected IItemHandler itemHandler = null;

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) (itemHandler == null ? itemHandler = new InvWrapper(this) : itemHandler);
        return super.getCapability(capability, facing);
    }
}
