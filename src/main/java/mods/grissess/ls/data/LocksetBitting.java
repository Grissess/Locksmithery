package mods.grissess.ls.data;

import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class LocksetBitting {
    public final BittingDescriptor descriptor;
    public List<Set<Integer>> pinSets;

    public static final LocksetBitting DEFAULT_BITTING = new LocksetBitting(BittingDescriptor.WOOD, new int[] {0}, new int[] {0}, new int[] {0}, new int[] {0});

    public LocksetBitting(BittingDescriptor descriptor, int[]... pinSetArrays) {
        this.descriptor = descriptor;
        assert pinSetArrays.length == descriptor.positions;
        pinSets = new ArrayList<>(descriptor.positions);
        for(int i = 0; i < descriptor.positions; i++) {
            Set<Integer> set = new HashSet<>();
            for(int j = 0; j < pinSetArrays[i].length; j++) {
                set.add(pinSetArrays[i][j]);
            }
            pinSets.add(set);
        }
    }

    public LocksetBitting(BittingDescriptor descriptor, List<Set<Integer>> pinSets) {
        this.descriptor = descriptor;
        this.pinSets = pinSets;
    }

    public LocksetBitting(BittingDescriptor descriptor) {
        List<Set<Integer>> ps = new ArrayList<>(descriptor.positions);
        for(int i = 0; i < descriptor.positions; i++) {
            Set<Integer> set = new HashSet<>();
            ps.add(set);
        }
        this.descriptor = descriptor;
        this.pinSets = ps;
    }

    public Set<Integer> getPinSets(int idx) {
        if(idx < 0 || idx >= descriptor.positions) return new HashSet<>();
        return pinSets.get(idx);
    }

    public int[] getPinSetsAsArray(int idx) {
        Set<Integer> ps = getPinSets(idx);
        int[] ints = new int[ps.size()];
        Iterator<Integer> iter = ps.iterator();
        int i = 0;
        while(iter.hasNext())
            ints[i++] = iter.next();
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
        getPinSets(idx).remove(pin);
    }

    public boolean fits(KeyBitting key) {
        if(key.overrides) return true;
        if(descriptor != key.descriptor) return false;
        for(int i = 0; i < descriptor.positions; i++) {
            if(!pinSets.get(i).contains(key.getPin(i))) return false;
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
            list.add(ps);
        }
        return new LocksetBitting(desc, list);
    }

    @Override
    public String toString() {
        return "LocksetBitting{" +
                "descriptor=" + descriptor +
                ", pinSets=" + pinSets +
                '}';
    }
}
