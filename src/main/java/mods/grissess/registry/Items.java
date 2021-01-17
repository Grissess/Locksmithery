package mods.grissess.registry;

import mods.grissess.SecurityCraft;
import mods.grissess.item.*;
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
    public static final SecureBlockItem secure_block_item = new SecureBlockItem();
    public static final Keyring keyring = new Keyring();
    public static final LiddedSwitchItem lidded_button_item = new LiddedSwitchItem(Blocks.lidded_button);
    public static final LiddedSwitchItem lidded_lever_item = new LiddedSwitchItem(Blocks.lidded_lever);
    public static final CoreExtractor core_extractor = new CoreExtractor();
    public static final CaptiveKeyHolderItem captive_key_latch = new CaptiveKeyHolderItem(Blocks.captive_key_latch);
    public static final CaptiveKeyHolderItem captive_key_switch = new CaptiveKeyHolderItem(Blocks.captive_key_switch);

    public static final Item pin = new Item()
            .setRegistryName("pin")
            .setUnlocalizedName("Pin")
            .setCreativeTab(CreativeTab.tab);
    public static final Item cylinder = new ItemWithDescriptor()
            .setRegistryName("cylinder")
            .setUnlocalizedName("Cylinder")
            .setCreativeTab(CreativeTab.tab);
    public static final Item hardened_iron_nugget = new Item()
            .setRegistryName("hardened_iron_nugget")
            .setUnlocalizedName("Hardened Iron Nugget")
            .setCreativeTab(CreativeTab.tab);
    public static final Item hardened_iron_ingot = new Item()
            .setRegistryName("hardened_iron_ingot")
            .setUnlocalizedName("Hardened Iron Ingot")
            .setCreativeTab(CreativeTab.tab);
    public static final Item locking_cover = new Item()
            .setRegistryName("locking_cover")
            .setUnlocalizedName("Locking Cover")
            .setCreativeTab(CreativeTab.tab);

    public static final Item locksmithing_workbench_item = new ItemBlock(Blocks.locksmith_workbench)
            .setRegistryName(Blocks.locksmith_workbench.getRegistryName())
            .setUnlocalizedName(Blocks.locksmith_workbench.getUnlocalizedName())
            .setCreativeTab(CreativeTab.tab);

    public static final Item[] ITEMS = {
            key,
            lockset,
            secure_door_item,
            secure_block_item,
            keyring,
            lidded_button_item,
            lidded_lever_item,
            core_extractor,
            captive_key_latch,
            captive_key_switch,

            pin,
            cylinder,
            hardened_iron_nugget,
            hardened_iron_ingot,
            locking_cover,

            locksmithing_workbench_item,
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        SecurityCraft.logger.info("Registering items...");
        IForgeRegistry<Item> reg = event.getRegistry();
        for(Item item : ITEMS) {
            reg.register(item);
            if(item instanceof ICustomModelRegistration) {
                ((ICustomModelRegistration) item).registerCustomModels(
                        SecurityCraft.instance.proxy
                );
            } else {
                SecurityCraft.instance.proxy.setModelLocation(item, 0, new ModelResourceLocation(
                        item.getRegistryName(),
                        "inventory"
                ));
            }
        }
    }
}
