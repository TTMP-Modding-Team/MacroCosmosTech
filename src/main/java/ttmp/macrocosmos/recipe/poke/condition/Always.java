package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public final class Always implements PokemonCondition{
	public static Always ALWAYS = new Always();

	private Always(){}

	@Override public boolean test(Pokemon pokemon){
		return true;
	}

	@Override public byte type(){
		return Types.ALWAYS;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@Override public String toString(){
		return "Always";
	}
}
