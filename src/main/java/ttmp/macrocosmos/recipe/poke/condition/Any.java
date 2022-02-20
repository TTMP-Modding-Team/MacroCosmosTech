package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public class Any extends MultipleCondition{
	public static PokemonCondition of(PokemonCondition... conditions){
		switch(conditions.length){
			case 0:
				return Never.NEVER;
			case 1:
				return conditions[0];
			default:
				return new Any(conditions);
		}
	}
	public static Any read(PacketBuffer buffer){
		return new Any(readConditions(buffer));
	}

	public Any(PokemonCondition... conditions){
		super(conditions);
	}

	@Override public boolean test(Pokemon pokemon){
		for(PokemonCondition c : conditions)
			if(c.test(pokemon)) return true;
		return false;
	}

	@Override public byte type(){
		return Types.ANY;
	}

	@Override protected String delimiterLocalizationKey(){
		return "pokemon_condition.macrocosmos.any.delimiter";
	}

	@Override protected String delimiter(){
		return " or ";
	}
}
