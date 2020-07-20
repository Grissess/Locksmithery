package mods.grissess.item;

import net.minecraft.item.Item;

public class BaseItem extends Item {
    public void setName(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}
