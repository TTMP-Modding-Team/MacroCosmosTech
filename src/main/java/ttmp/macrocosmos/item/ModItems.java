package ttmp.macrocosmos.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import ttmp.macrocosmos.MacroCosmosMod;

@GameRegistry.ObjectHolder(MacroCosmosMod.MODID)
@Mod.EventBusSubscriber(modid = MacroCosmosMod.MODID)
public class ModItems{

	public static final Item POCKETPOKEMONPC = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e){
		IForgeRegistry<Item> registry = e.getRegistry();
		registry.register(new PocketPokemonPC());
	}
}
