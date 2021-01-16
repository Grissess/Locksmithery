package mods.grissess.proxy;

import mods.grissess.net.Channel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Common {
    public SimpleNetworkWrapper channel;

    public void preInit(FMLPreInitializationEvent event) {}
    public void init(FMLInitializationEvent event) {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel("securitycraft");
        channel.registerMessage(
                Channel.HandlerSetCuts.class,
                Channel.PacketSetCuts.class,
                Channel.PacketKind.SET_CUTS.ordinal(),
                Side.SERVER
        );
        channel.registerMessage(
                Channel.HandlerTakeResult.class,
                Channel.PacketTakeResult.class,
                Channel.PacketKind.TAKE_RESULT.ordinal(),
                Side.SERVER
        );

        System.out.println("C: networking active");
    }
    public void postInit(FMLPostInitializationEvent event) {}

    public void setModelLocation(Item _item, int _metadata, ModelResourceLocation _loc) {}

    public void registerTESRs(FMLInitializationEvent init) {}
}
