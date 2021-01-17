package mods.grissess.registry;

import mods.grissess.recipe.ApplyLockset;
import mods.grissess.recipe.ExtractCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class Recipes {
    public static final ApplyLockset apply_lockset = new ApplyLockset();
    public static final ExtractCore extract_core = new ExtractCore();

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().registerAll(
                apply_lockset,
                extract_core
        );

        // You could argue this is a good time to do these, since I consider them
        // to be "recipes" as well
        GameRegistry.addSmelting(
                new ItemStack(Item.getByNameOrId("minecraft:iron_nugget")),
                new ItemStack(Items.hardened_iron_nugget),
                0f
        );
    }
}
