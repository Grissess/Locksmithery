package mods.grissess.registry;

import mods.grissess.recipe.ApplyLockset;
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

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().registerAll(
                apply_lockset
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
