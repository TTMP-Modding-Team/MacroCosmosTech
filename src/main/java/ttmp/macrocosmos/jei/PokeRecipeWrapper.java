package ttmp.macrocosmos.jei;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.Recipe.ChanceEntry;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.util.GTUtility;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import ttmp.macrocosmos.recipe.poke.PokeRecipeConditionIngredient;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokeRecipeWrapper extends GTRecipeWrapper{
	private final RecipeMap<?> recipeMap;
	private final Int2ObjectMap<PokemonCondition> pokemonConditions = new Int2ObjectOpenHashMap<>();

	public PokeRecipeWrapper(RecipeMap<?> recipeMap, Recipe recipe){
		super(recipeMap, recipe);
		this.recipeMap = recipeMap;
	}

	public Int2ObjectMap<PokemonCondition> getPokemonConditions(){
		return pokemonConditions;
	}

	@Override public void getIngredients(@Nonnull IIngredients ingredients){
		super.getIngredients(ingredients);
		getNotConsumedItemInputs().clear();

		int currentItemSlot = 0;

		List<List<Pokemon>> pokemonInputs = new ArrayList<>();

		// Inputs
		if(!getRecipe().getInputs().isEmpty()){
			List<List<ItemStack>> matchingInputs = new ArrayList<>(getRecipe().getInputs().size());
			for(CountableIngredient ci : getRecipe().getInputs()){
				if(ci.getIngredient() instanceof PokeRecipeConditionIngredient){
					for(PokemonCondition c : ((PokeRecipeConditionIngredient)ci.getIngredient()).getConditions()){
						pokemonConditions.put(pokemonConditions.size(), c);
						pokemonInputs.add(PreviewPokemonFactory.filtered(c));
					}
				}else{
					matchingInputs.add(Arrays.stream(ci.getIngredient().getMatchingStacks())
							.sorted(OreDictUnifier.getItemStackComparator())
							.map(is -> GTUtility.copyAmount(ci.getCount()==0 ? 1 : ci.getCount(), is))
							.collect(Collectors.toList()));
					getNotConsumedItemInputs().put(currentItemSlot++, ci.getCount()==0);
				}
			}
			ingredients.setInputLists(VanillaTypes.ITEM, matchingInputs);
		}
		currentItemSlot = recipeMap.getMaxInputs()-1;

		// Outputs
		if(!getRecipe().getOutputs().isEmpty()||!getRecipe().getChancedOutputs().isEmpty()){
			List<ItemStack> recipeOutputs = getRecipe().getOutputs()
					.stream().map(ItemStack::copy).collect(Collectors.toList());
			currentItemSlot += recipeOutputs.size();

			List<ChanceEntry> chancedOutputs = getRecipe().getChancedOutputs();
			chancedOutputs.sort(Comparator.comparingInt(entry -> entry==null ? 0 : entry.getChance()));
			for(ChanceEntry chancedEntry : chancedOutputs){
				getChanceOutputMap().put(currentItemSlot++, chancedEntry);
				recipeOutputs.add(chancedEntry.getItemStack());
			}
			ingredients.setOutputs(VanillaTypes.ITEM, recipeOutputs);
		}

		ingredients.setInputLists(MacroCosmosJeiPlugin.POKEMON_INGREDIENT, pokemonInputs);
	}
}
