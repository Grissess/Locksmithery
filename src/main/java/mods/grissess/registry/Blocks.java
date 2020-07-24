package mods.grissess.registry;

import mods.grissess.block.LocksmithWorkbench;
import mods.grissess.block.SecureBlock;
import mods.grissess.block.SecureDoor;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Blocks {
    public static final SecureDoor secure_door = new SecureDoor();
    public static final SecureBlock secure_block = new SecureBlock();
    public static final LocksmithWorkbench locksmith_workbench = new LocksmithWorkbench();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                secure_door,
                secure_block,
                locksmith_workbench
        );
    }
}
