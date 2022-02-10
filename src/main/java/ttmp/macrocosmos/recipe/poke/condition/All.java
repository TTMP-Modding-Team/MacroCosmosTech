package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class All implements PokemonCondition{
	private final PokemonCondition[] conditions;
	public All(PokemonCondition... conditions){
		this.conditions = conditions;
	}
	@Override public boolean test(Pokemon pokemon){
		for(PokemonCondition c : conditions)
			if(!c.test(pokemon)) return false;
		return true;
	}
}
