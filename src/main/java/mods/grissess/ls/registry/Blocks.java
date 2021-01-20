package mods.grissess.ls.registry;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber
public class Blocks {
    public static final SecureDoor secure_door = new SecureDoor();
    public static final SecureBlock secure_block = new SecureBlock();
    public static final LocksmithWorkbench locksmith_workbench = new LocksmithWorkbench();
    public static final LiddedSwitch lidded_button = new LiddedSwitch(false);
    public static final LiddedSwitch lidded_lever = new LiddedSwitch(true);
    public static final CaptiveKeyHolder captive_key_latch = new CaptiveKeyHolder(false);
    public static final CaptiveKeyHolder captive_key_switch = new CaptiveKeyHolder(true);

    public static final Block[] BLOCKS = {
            secure_door,
            secure_block,
            locksmith_workbench,
            lidded_button,
            lidded_lever,
            captive_key_latch,
            captive_key_switch
    };

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCKS);
        Locksmithery.logger.info("B.rB: registered blocks");
    }

    @SubscribeEvent
    public static void missingMappings(RegistryEvent.MissingMappings<Block> event) {
        Migration.fixMappings(event, BLOCKS);
    }
}
