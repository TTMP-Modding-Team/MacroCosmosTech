package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;

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

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocal("pokemon_condition.macrocosmos.always");
	}

	@Override public String toString(){
		return "Any Pokemon";
	}
}
