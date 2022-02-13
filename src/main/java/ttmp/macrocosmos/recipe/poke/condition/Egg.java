package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public final class Egg implements PokemonCondition{
	public static final Egg IS_EGG = new Egg();

	private Egg(){}

	@Override public boolean test(Pokemon pokemon){
		return pokemon.isEgg();
	}

	@Override public byte type(){
		return Types.EGG;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@Override public String toString(){
		return "Egg";
	}
}
