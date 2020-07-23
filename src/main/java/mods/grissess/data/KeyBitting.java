package mods.grissess.data;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class KeyBitting {
    public final BittingDescriptor descriptor;
    public int[] pins;
    public boolean overrides = false;

    public static final KeyBitting OVERRIDE_BITTING = new KeyBitting(true);

    private KeyBitting(boolean unused) {
        descriptor = BittingDescriptor.WOOD;
        pins = new int[descriptor.positions];
        overrides = true;
    }

    public KeyBitting(BittingDescriptor descriptor, int[] pins) {
        this.descriptor = descriptor;
        this.pins = pins;
    }

    public KeyBitting(BittingDescriptor descriptor) {
        this(descriptor, new int[descriptor.positions]);
    }

    public int getPin(int idx) {
        if(idx < 0 || idx >= descriptor.positions) return -1;
        return pins[idx];
    }

    public void setPin(int idx, int setting) {
        if(idx < 0 || idx >= descriptor.positions) return;
        if(setting < 0) setting = 0;
        if(setting >= descriptor.settings) setting = descriptor.settings - 1;
        pins[idx] = setting;
    }

    public boolean fits(LocksetBitting lock) {
        return lock.fits(this);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("desc", descriptor.toNBT());
        tag.setIntArray("pins", pins);
        if(overrides) tag.setBoolean("overrides", true);
        return tag;
    }

    public static KeyBitting fromNBT(NBTTagCompound tag) {
        BittingDescriptor desc = BittingDescriptor.fromNBT(tag.getCompoundTag("desc"));
        int[] pins = tag.getIntArray("pins");
        KeyBitting instance = new KeyBitting(desc, pins);
        if(tag.hasKey("overrides") && tag.getBoolean("overrides")) instance.overrides = true;
        return instance;
    }

    @Override
    public String toString() {
        return "KeyBitting{" +
                "descriptor=" + descriptor +
                ", pins=" + Arrays.toString(pins) +
                ", overrides=" + overrides +
                '}';
    }
}
