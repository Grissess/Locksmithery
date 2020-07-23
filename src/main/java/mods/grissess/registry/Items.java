package mods.grissess.registry;

import mods.grissess.SecurityCraft;
import mods.grissess.item.Lockset;
import mods.grissess.item.Key;
import mods.grissess.item.SecureDoorItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class Items {
    public static final Key key = new Key();
    public static final Lockset lockset = new Lockset();
    public static final SecureDoorItem secure_door_item = new SecureDoorItem();

    public static final Item pin = new Item()
            .setRegistryName("pin")
            .setUnlocalizedName("Pin")
            .setCreativeTab(CreativeTab.tab);
    public static final Item cylinder = new Item()
            .setRegistryName("cylinder")
            .setUnlocalizedName("Cylinder")
            .setCreativeTab(CreativeTab.tab);

    public static final Item locksmithing_workbench_item = new ItemBlock(Blocks.locksmith_workbench)
            .setRegistryName(Blocks.locksmith_workbench.getRegistryName())
            .setUnlocalizedName(Blocks.locksmith_workbench.getUnlocalizedName())
            .setCreativeTab(CreativeTab.tab);

    public static final Item[] ITEMS = {
            key,
            lockset,
            secure_door_item,

            pin,
            cylinder,

            locksmithing_workbench_item,
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        SecurityCraft.logger.info("Registering items...");
        IForgeRegistry<Item> reg = event.getRegistry();
        for(Item item : ITEMS) {
            reg.register(item);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                    item.getRegistryName(),
                    "inventory"
            ));
        }
    }
}
