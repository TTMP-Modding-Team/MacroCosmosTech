package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class Egg implements PokemonCondition{
	public static final Egg IS_EGG = new Egg();
	@Override public boolean test(Pokemon pokemon){
		return pokemon.isEgg();
	}
}
