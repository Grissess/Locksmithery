package mods.grissess.proxy;

import mods.grissess.registry.TileEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.function.Predicate;

public class Client extends Common {
    public void setModelLocation(Item item, int metadata, ModelResourceLocation loc) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, loc);
    }

    public void registerTESRs(FMLInitializationEvent init) {
        TileEntities.registerTESRs(init);
    }
}
