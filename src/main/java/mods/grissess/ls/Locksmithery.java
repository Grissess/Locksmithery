package mods.grissess.ls;

import mods.grissess.ls.proxy.Common;
import mods.grissess.ls.registry.GuiHandler;
import mods.grissess.ls.registry.TileEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Locksmithery.MODID,
        name = Locksmithery.NAME,
        version = Locksmithery.VERSION
)
public class Locksmithery {
    public static final String MODID = "locksmithery";
    public static final String NAME = "Locksmithery";
    public static final String VERSION = "0.3";

    @Mod.Instance(MODID)
    public static Locksmithery instance;

    public static Logger logger = null;
    // For the poor people coming from the "securitycraft" namespace
    // TODO: make a config for this
    public static boolean migrating = false;
    public static String OLD_MODID = "securitycraft";

    @SidedProxy(clientSide = "mods.grissess.ls.proxy.Client", serverSide = "mods.grissess.ls.proxy.Server")
    public static Common proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.registerModelLoaders(event);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        TileEntities.registerTileEntities(event);
        GuiHandler.registerGuiHandler();
        proxy.registerTESRs(event);
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
