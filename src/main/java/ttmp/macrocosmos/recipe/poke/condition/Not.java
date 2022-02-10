package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class Not implements PokemonCondition{
	private final PokemonCondition condition;
	public Not(PokemonCondition condition){
		this.condition = condition;
	}
	@Override public boolean test(Pokemon pokemon){
		return !condition.test(pokemon);
	}
}
