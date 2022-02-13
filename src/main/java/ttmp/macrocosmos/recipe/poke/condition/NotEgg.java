package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public final class NotEgg implements PokemonCondition{
	public static final NotEgg NOT_EGG = new NotEgg();

	private NotEgg(){}

	@Override public boolean test(Pokemon pokemon){
		return !pokemon.isEgg();
	}

	@Override public byte type(){
		return Types.NOT_EGG;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@Override public String toString(){
		return "Not Egg";
	}
}
