package mods.grissess.ls.proxy;

import mods.grissess.ls.registry.TileEntities;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class Client extends Common {
    public void setModelLocation(Item item, int metadata, ModelResourceLocation loc) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, loc);
    }

    public void registerTESRs(FMLInitializationEvent init) {
        TileEntities.registerTESRs(init);
    }
}
