package mods.grissess;

import mods.grissess.proxy.Common;
import mods.grissess.registry.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = SecurityCraft.MODID,
        name = SecurityCraft.NAME,
        version = SecurityCraft.VERSION
)
public class SecurityCraft {
    public static final String MODID = "securitycraft";
    public static final String NAME = "SecurityCraft";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @SidedProxy(clientSide = "mods.grissess.proxy.Client", serverSide = "mods.grissess.proxy.Server")
    private static Common proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
