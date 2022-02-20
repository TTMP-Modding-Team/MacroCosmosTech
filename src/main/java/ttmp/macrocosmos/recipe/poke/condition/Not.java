package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;

public class Not implements PokemonCondition{
	public static Not read(PacketBuffer buffer){
		return new Not(PokemonCondition.readCondition(buffer));
	}

	private final PokemonCondition condition;

	public Not(PokemonCondition condition){
		this.condition = condition;
	}

	@Override public boolean test(Pokemon pokemon){
		return !condition.test(pokemon);
	}

	@Override public byte type(){
		return Types.NOT;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		condition.write(buffer);
	}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocalFormatted("pokemon_condition.macrocosmos.not", condition.localize());
	}

	@Override public String toString(){
		return "Not "+condition;
	}
}
