package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Type implements PokemonCondition{
	private final EnumType type;
	public Type(EnumType type){
		this.type = type;
	}
	@Override public boolean test(Pokemon pokemon){
		return pokemon.getBaseStats().getTypeList().contains(type);
	}
}
