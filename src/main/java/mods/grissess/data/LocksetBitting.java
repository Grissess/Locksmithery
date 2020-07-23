package mods.grissess.data;

import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class LocksetBitting {
    public final BittingDescriptor descriptor;
    public Set<Integer>[] pinSets;

    public static final LocksetBitting DEFAULT_BITTING = new LocksetBitting(BittingDescriptor.WOOD, new int[] {0}, new int[] {0}, new int[] {0}, new int[] {0});

    public LocksetBitting(BittingDescriptor descriptor, int[]... pinSetArrays) {
        this.descriptor = descriptor;
        assert pinSetArrays.length == descriptor.positions;
        pinSets = new HashSet[descriptor.positions];
        for(int i = 0; i < descriptor.positions; i++) {
            pinSets[i] = new HashSet<>();
            for(int j = 0; j < pinSetArrays[i].length; j++) {
                pinSets[i].add(pinSetArrays[i][j]);
            }
        }
    }

    public LocksetBitting(BittingDescriptor descriptor, Set<Integer>[] pinSets) {
        this.descriptor = descriptor;
        this.pinSets = pinSets;
    }

    public LocksetBitting(BittingDescriptor descriptor) {
        Set<Integer>[] ps = new HashSet[descriptor.positions];
        for(int i = 0; i < descriptor.positions; i++) {
            ps[i] = new HashSet<>();
        }
        this.descriptor = descriptor;
        this.pinSets = ps;
    }

    public Set<Integer> getPinSets(int idx) {
        if(idx < 0 || idx >= descriptor.positions) return new HashSet<>();
        return pinSets[idx];
    }

    public int[] getPinSetsAsArray(int idx) {
        Integer[] ps = getPinSets(idx).toArray(new Integer[0]);
        int[] ints = new int[ps.length];
        for(int i = 0; i < ps.length; i++) {
            ints[i] = ps[i];
        }
        return ints;
    }

    public void addPin(int idx, int pin) {
        if(idx < 0 || idx >= descriptor.positions) return;
        if(pin < 0 || pin >= descriptor.settings) return;
        getPinSets(idx).add(pin);
    }

    public void removePin(int idx, int pin) {
        if(idx < 0 || idx >= descriptor.positions) return;
        if(pin < 0 || idx >= descriptor.settings) return;
        // This might be overcautious, but we definitely want integer equality
        getPinSets(idx).removeIf(value -> value == pin);
    }

    public boolean fits(KeyBitting key) {
        if(key.overrides) return true;
        for(int i = 0; i < descriptor.positions; i++) {
            if(!pinSets[i].contains(key.getPin(i))) return false;
        }
        return true;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("desc", descriptor.toNBT());
        for(int i = 0; i < descriptor.positions; i++) {
            tag.setIntArray("pin" + i, getPinSetsAsArray(i));
        }
        return tag;
    }

    public static LocksetBitting fromNBT(NBTTagCompound tag) {
        BittingDescriptor desc = BittingDescriptor.fromNBT(tag.getCompoundTag("desc"));
        List<Set<Integer>> list = new ArrayList<>(desc.positions);
        for (int i = 0; i < desc.positions; i++) {
            Set<Integer> ps = new HashSet<>();
            int[] psi = tag.getIntArray("pin" + i);
            for (int j = 0; j < psi.length; j++) {
                ps.add(psi[j]);
            }
        }
        return new LocksetBitting(desc, list.toArray(new Set[0]));
    }

    @Override
    public String toString() {
        return "LocksetBitting{" +
                "descriptor=" + descriptor +
                ", pinSets=" + Arrays.toString(pinSets) +
                '}';
    }
}
