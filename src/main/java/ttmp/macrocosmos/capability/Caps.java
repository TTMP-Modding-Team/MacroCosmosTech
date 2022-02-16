package ttmp.macrocosmos.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Caps{
	@SuppressWarnings("ConstantConditions") @Nonnull private static <T> T definitelyNotNull(){
		return null;
	}

	@CapabilityInject(PokemonContainer.class)
	public static final Capability<PokemonContainer> POKEMON_CONTAINER = definitelyNotNull();
	@CapabilityInject(PokemonRecipeLogic.PokeRecipeIngredientCap.class)
	public static final Capability<PokemonRecipeLogic.PokeRecipeIngredientCap> POKE_RECIPE_INGREDIENT = definitelyNotNull();

	public static void register(){
		register(PokemonContainer.class);
		register(PokemonRecipeLogic.PokeRecipeIngredientCap.class);
	}

	private static <T> void register(Class<T> clazz){
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
