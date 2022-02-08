package ttmp.macrocosmos.recipe;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.recipes.Recipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PokemonRecipe{
	@Nullable private final Predicate<Pokemon> pokemonPredicate;
	@Nullable private final WorkType workType;
	private final List<Modifier> modifiers = new ArrayList<>();
	private final Recipe recipe;

	public PokemonRecipe(@Nullable Predicate<Pokemon> pokemonPredicate, @Nullable WorkType workType, Recipe recipe){
		this.pokemonPredicate = pokemonPredicate;
		this.workType = workType;
		this.recipe = recipe;
	}

	@Nullable public Predicate<Pokemon> getPokemonPredicate(){
		return pokemonPredicate;
	}
	@Nullable public WorkType getWorkType(){
		return workType;
	}
	public List<Modifier> getModifiers(){
		return modifiers;
	}
	public Recipe getRecipe(){
		return recipe;
	}

	public enum WorkType{
		HP,
		ATTACK,
		DEFENCE,
		SPECIAL_ATTACK,
		SPECIAL_DEFENCE,
		SPEED
	}

	public static class Modifier{
		private final Predicate<Pokemon> predicate;
		private final double modifier;

		public Modifier(Predicate<Pokemon> predicate, double modifier){
			this.predicate = predicate;
			this.modifier = modifier;
		}

		public Predicate<Pokemon> getPredicate(){
			return predicate;
		}
		public double getModifier(){
			return modifier;
		}
	}
}
