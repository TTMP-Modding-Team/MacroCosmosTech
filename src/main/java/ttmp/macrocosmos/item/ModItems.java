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
	public static final Item SHH = definitelyNotNull();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e){
		IForgeRegistry<Item> registry = e.getRegistry();
		register(registry, "pocketpokemonpc", new PocketPokemonPC().setMaxStackSize(1));
		registry.register(new PokeRecipeIngredientItem().setRegistryName("poke_recipe_ingredient"));
	}

	private static void register(IForgeRegistry<Item> registry, String name, Item item){
		registry.register(item.setCreativeTab(CreativeTabs.REDSTONE) // TODO
				.setRegistryName(name)
				.setTranslationKey(MODID+"."+name));
	}
}
