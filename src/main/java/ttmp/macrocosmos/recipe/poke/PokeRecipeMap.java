package ttmp.macrocosmos.recipe.poke;

import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;

import javax.annotation.Nullable;

public class PokeRecipeMap<R extends RecipeBuilder<R>> extends RecipeMap<R>{
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
	                     R defaultRecipe){
		this(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe,
				PokeRecipeMetadata.defaultMetadata());
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
	                     PokeRecipeMetadataBuilder defaultMetadata){
		this(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, defaultMetadata.build());
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
	                     PokeRecipeMetadata defaultMetadata){
		// Need one more item input because pokemon container is transferred as an item
		super(unlocalizedName, minInputs, maxInputs+1, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, true);
		this.defaultMetadata = defaultMetadata;
	}

	@Nullable public PokeRecipeMetadata getDefaultMetadata(){
		return this.defaultMetadata;
	}
}
