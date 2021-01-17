package mods.grissess.registry;

import mods.grissess.block.render.LiddedSwitchTESR;
import mods.grissess.block.te.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntities {
    // Called from mod class
    public static void registerTileEntities(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(SecureDoorTE.class, new ResourceLocation("securitycraft:secure_door_te"));
        GameRegistry.registerTileEntity(SecureBlockTE.class, new ResourceLocation("securitycraft:secure_block_te"));
        GameRegistry.registerTileEntity(LocksmithWorkbenchTE.class, new ResourceLocation("securitycraft:locksmith_workbench_te"));
        GameRegistry.registerTileEntity(LiddedSwitchTE.class, new ResourceLocation("securitycraft:lidded_switch_te"));
        GameRegistry.registerTileEntity(CaptiveKeyHolderTE.class, new ResourceLocation("securitycraft:captive_key_holder_te"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerTESRs(FMLInitializationEvent init) {
        ClientRegistry.bindTileEntitySpecialRenderer(
                LiddedSwitchTE.class,
                new LiddedSwitchTESR()
        );
    }
}
