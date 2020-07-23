package mods.grissess.registry;

import mods.grissess.block.te.LocksmithWorkbenchTE;
import mods.grissess.block.te.SecureDoorTE;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {
    // Called from mod class
    public static void registerTileEntities(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(SecureDoorTE.class, new ResourceLocation("securitycraft:secure_door_te"));
        GameRegistry.registerTileEntity(LocksmithWorkbenchTE.class, new ResourceLocation("securitycraft:locksmith_workbench_te"));
    }
}
