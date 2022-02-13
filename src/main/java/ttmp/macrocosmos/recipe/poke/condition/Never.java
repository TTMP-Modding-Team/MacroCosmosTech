package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public final class Never implements PokemonCondition{
	public static Never NEVER = new Never();

	private Never(){}

	@Override public boolean test(Pokemon pokemon){
		return false;
	}

	@Override public byte type(){
		return Types.NEVER;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@Override public String toString(){
		return "Never";
	}
}
