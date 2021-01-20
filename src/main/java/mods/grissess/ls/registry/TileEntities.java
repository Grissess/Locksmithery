package mods.grissess.ls.registry;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.render.LiddedSwitchTESR;
import mods.grissess.ls.block.te.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntities {
    public static Class<? extends TileEntity>[] TILE_ENTITIES = new Class[]{
            SecureDoorTE.class,
            SecureBlockTE.class,
            LocksmithWorkbenchTE.class,
            LiddedSwitchTE.class,
            CaptiveKeyHolderTE.class,
    };

    // Called from mod class
    public static void registerTileEntities(FMLInitializationEvent event) {
        if(Locksmithery.migrating)
            registerTileEntitiesInDomain(Locksmithery.OLD_MODID);
        else
            registerTileEntitiesInDomain(Locksmithery.MODID);
    }

    public static void registerTileEntitiesInDomain(String modid) {
        GameRegistry.registerTileEntity(SecureDoorTE.class, new ResourceLocation(modid, "secure_door_te"));
        GameRegistry.registerTileEntity(SecureBlockTE.class, new ResourceLocation(modid, "secure_block_te"));
        GameRegistry.registerTileEntity(LocksmithWorkbenchTE.class, new ResourceLocation(modid, "locksmith_workbench_te"));
        GameRegistry.registerTileEntity(LiddedSwitchTE.class, new ResourceLocation(modid, "lidded_switch_te"));
        GameRegistry.registerTileEntity(CaptiveKeyHolderTE.class, new ResourceLocation(modid, "captive_key_holder_te"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerTESRs(FMLInitializationEvent init) {
        ClientRegistry.bindTileEntitySpecialRenderer(
                LiddedSwitchTE.class,
                new LiddedSwitchTESR()
        );
    }
}
