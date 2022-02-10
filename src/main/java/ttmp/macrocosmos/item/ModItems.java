package ttmp.macrocosmos.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@GameRegistry.ObjectHolder(MODID)
public class ModItems{

	public static final Item POCKETPOKEMONPC = null;
	public static final Item SHH = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e){
		IForgeRegistry<Item> registry = e.getRegistry();
		register(registry, "pocketpokemonpc", new PocketPokemonPC().setMaxStackSize(1));
		registry.register(new PokeRecipeContainerWrapperIngredientItem().setRegistryName("shh"));
	}

	private static void register(IForgeRegistry<Item> registry, String name, Item item){
		registry.register(item.setCreativeTab(CreativeTabs.REDSTONE) // TODO
				.setRegistryName(name)
				.setTranslationKey(MODID+"."+name));
	}
}
