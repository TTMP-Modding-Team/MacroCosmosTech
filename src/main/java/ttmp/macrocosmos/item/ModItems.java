package ttmp.macrocosmos.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@GameRegistry.ObjectHolder(MODID)
public class ModItems{
	@SuppressWarnings("ConstantConditions") @Nonnull private static <T> T definitelyNotNull(){
		return null;
	}

	public static final Item POCKETPOKEMONPC = definitelyNotNull();
	public static final Item POKE_RECIPE_INGREDIENT = definitelyNotNull();
	public static final Item FUCKING_DEBUGGER = definitelyNotNull();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e){
		IForgeRegistry<Item> registry = e.getRegistry();
		register(registry, "pocketpokemonpc", new PocketPokemonPC().setMaxStackSize(1));
		registry.register(new PokeRecipeIngredientItem().setRegistryName("poke_recipe_ingredient"));
		register(registry, "fucking_debugger", new FuckingDebuggerItem().setMaxStackSize(1));
	}

	private static void register(IForgeRegistry<Item> registry, String name, Item item){
		registry.register(item.setCreativeTab(CreativeTabs.REDSTONE) // TODO
				.setRegistryName(name)
				.setTranslationKey(MODID+"."+name));
	}
}
