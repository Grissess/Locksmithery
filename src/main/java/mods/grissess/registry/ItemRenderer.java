package mods.grissess.registry;

import mods.grissess.SecurityCraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ItemRenderer {
    @Mod.EventHandler
    public static void registerRenders(ModelRegistryEvent event) {
        SecurityCraft.logger.info("Registering custom models...");
        for(Item item : Items.ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation("minecraft", "bucket"), "inventory"));
        }
    }
}
