package ttmp.macrocosmos.jei;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.GTValues;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import ttmp.macrocosmos.recipe.ModRecipes;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JEIPlugin
public class MacroCosmosJeiPlugin implements IModPlugin{
	public static final IIngredientType<Pokemon> POKEMON_INGREDIENT = () -> Pokemon.class;

	@Override public void registerIngredients(IModIngredientRegistration registry){
		registry.register(POKEMON_INGREDIENT, PreviewPokemonFactory.all(), new PokemonIngredientHelper(), new PokemonIngredientRenderer());
	}

	@Override public void registerCategories(IRecipeCategoryRegistration registry){
		registerRecipeCategories(registry, ModRecipes.COMBEE);
		registerRecipeCategories(registry, ModRecipes.TEST_1);
	}
	@Override public void register(IModRegistry registry){
		registerRecipes(registry, ModRecipes.COMBEE);
		registerRecipes(registry, ModRecipes.TEST_1);
	}

	private void registerRecipeCategories(IRecipeCategoryRegistration registry, RecipeMap<?> recipeMap){
		registry.addRecipeCategories(new PokeRecipeMapCategory(recipeMap, registry.getJeiHelpers().getGuiHelper()));
	}

	private void registerRecipes(IModRegistry registry, RecipeMap<?> recipeMap){
		Stream<Recipe> recipeStream = recipeMap.getRecipeList().stream()
				.filter(recipe -> !recipe.isHidden()&&recipe.hasValidInputsForDisplay());

		if(recipeMap.getSmallRecipeMap()!=null){
			Collection<Recipe> smallRecipes = recipeMap.getSmallRecipeMap().getRecipeList();
			recipeStream = recipeStream.filter(recipe -> !smallRecipes.contains(recipe));
		}

		registry.addRecipes(
				recipeStream.map(r -> new PokeRecipeWrapper(recipeMap, r))
						.collect(Collectors.toList()),
				GTValues.MODID+":"+recipeMap.unlocalizedName);
	}
}
