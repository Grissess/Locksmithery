package mods.grissess.ls.registry;

import mods.grissess.ls.Locksmithery;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Migration {
    // Note that this takes the array of our instances explicitly; for some reason,
    // the registry comes down to us without our mappings (even though they were registered)
    // in init back when the game started).
    public static <T extends IForgeRegistryEntry<T>> void fixMappings(RegistryEvent.MissingMappings<T> event, T... ours) {
        if(!Locksmithery.migrating) {
            Locksmithery.logger.warn("M.fM: There were missing mappings but we're not configured to look at them");
            return;
        }

        for (RegistryEvent.MissingMappings.Mapping<T> mapping : event.getAllMappings()) {
            if(mapping.key.getResourceDomain().equals(Locksmithery.OLD_MODID)) {
                ResourceLocation loc = new ResourceLocation(
                        Locksmithery.MODID,
                        mapping.key.getResourcePath()
                );
                T newInstance = null;
                // Yes, it's a linear search. This path is expected to be very cold.
                for(T inst: ours) {
                    if(inst.getRegistryName().equals(loc)) {
                        newInstance = inst;
                        break;
                    }
                }
                if(newInstance != null) {
                    Locksmithery.logger.warn(String.format("M.fM: remapped %s to %s (block %s) due to migration", mapping.key, loc, newInstance));
                    mapping.remap(newInstance);
                } else {
                    Locksmithery.logger.warn(String.format("M.fM: mapping missing for both %s and %s; nothing could be done", mapping.key, loc));
                }
            }
        }
    }
}
