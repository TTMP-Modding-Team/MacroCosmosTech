package ttmp.macrocosmos.recipe.poke;

import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class PokeRecipe extends Recipe{
	private final PokeRecipeMetadata metadata;

	public PokeRecipe(Recipe recipe, PokeRecipeMetadata metadata){
		this(recipe.getInputs(),
				recipe.getOutputs(),
				recipe.getChancedOutputs(),
				recipe.getFluidInputs(),
				recipe.getFluidOutputs(),
				recipe.getDuration(),
				recipe.getEUt(),
				recipe.isHidden(),
				metadata);
	}
	public PokeRecipe(List<CountableIngredient> inputs,
	                  List<ItemStack> outputs,
	                  List<ChanceEntry> chancedOutputs,
	                  List<FluidStack> fluidInputs,
	                  List<FluidStack> fluidOutputs,
	                  int duration,
	                  int EUt,
	                  boolean hidden,
	                  PokeRecipeMetadata metadata){
		super(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs, duration, EUt, hidden);
		this.metadata = metadata;
	}

	public PokeRecipeMetadata getMetadata(){
		return metadata;
	}

	@Override public String toString(){
		return "PokeRecipe{"+
				"metadata="+metadata+
				"recipe="+super.toString()+
				'}';
	}
}
