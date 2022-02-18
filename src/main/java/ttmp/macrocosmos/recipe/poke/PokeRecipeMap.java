package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import javax.annotation.Nullable;

import static ttmp.macrocosmos.recipe.poke.value.PokemonValue.*;

public class PokeRecipeMap<R extends RecipeBuilder<R>> extends RecipeMap<R>{
	public static PokemonValue defaultProgress(){
		// HP is omitted because they already contribute to operation and it uses different formula for calculation
		// defence stats were also omitted because they already contribute to operation
		return mul(
				div(
						constant(600),
						sum(constant(500), stat(StatsType.Attack, StatsType.SpecialAttack, StatsType.Speed))
				),
				sum(
						degradation(),
						ability()
				)
		);
	}

	public static PokemonValue defaultHpToWorkConversionRate(){
		return div(
				constant(4000),
				sum(
						constant(200),
						stat(StatsType.Defence, StatsType.SpecialDefence)
				)
		);
	}

	@Nullable private final PokeRecipeMetadata defaultMetadata;

	public PokeRecipeMap(String unlocalizedName,
	                     int minInputs,
	                     int maxInputs,
	                     int minOutputs,
	                     int maxOutputs,
	                     int minFluidInputs,
	                     int maxFluidInputs,
	                     int minFluidOutputs,
	                     int maxFluidOutputs,
	                     R defaultRecipe,
	                     boolean isHidden){
		this(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden,
				PokeRecipeMetadata.builder()
						.progress(defaultProgress())
						.hpToWorkConversionRate(defaultHpToWorkConversionRate()));
	}
	public PokeRecipeMap(String unlocalizedName,
	                     int minInputs,
	                     int maxInputs,
	                     int minOutputs,
	                     int maxOutputs,
	                     int minFluidInputs,
	                     int maxFluidInputs,
	                     int minFluidOutputs,
	                     int maxFluidOutputs,
	                     R defaultRecipe,
	                     boolean isHidden,
	                     @Nullable PokeRecipeMetadataBuilder defaultMetadata){
		// Need one more item input because pokemon container is transferred as an item
		super(unlocalizedName, minInputs, maxInputs+1, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden);
		this.defaultMetadata = defaultMetadata!=null ? defaultMetadata.build() : null;
	}

	@Nullable public PokeRecipeMetadata getDefaultMetadata(){
		return this.defaultMetadata;
	}

	// TODO jei shit?
}
