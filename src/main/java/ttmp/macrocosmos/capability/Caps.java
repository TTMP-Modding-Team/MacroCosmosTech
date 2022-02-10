package ttmp.macrocosmos.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

import javax.annotation.Nullable;

public class Caps{
	@CapabilityInject(PokemonContainer.class)
	public static final Capability<PokemonContainer> POKEMON_CONTAINER = null;
	@CapabilityInject(PokemonRecipeLogic.Wtf.class)
	public static final Capability<PokemonRecipeLogic.Wtf> WTF = null;

	public static void register(){
		registerShit(PokemonContainer.class);
		registerShit(PokemonRecipeLogic.Wtf.class);
	}

	private static <T> void registerShit(Class<T> clazz){
		CapabilityManager.INSTANCE.register(clazz, new Capability.IStorage<T>(){
			@Nullable @Override public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side){
				return null;
			}
			@Override public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt){}
		}, () -> {
			throw new UnsupportedOperationException();
		});
	}
}
