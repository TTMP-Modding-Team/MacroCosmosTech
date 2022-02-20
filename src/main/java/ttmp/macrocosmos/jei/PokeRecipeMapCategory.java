package ttmp.macrocosmos.jei;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.gui.BlankUIHolder;
import gregtech.api.gui.ModularUI;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import gregtech.integration.jei.recipe.RecipeMapCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;
import ttmp.macrocosmos.MacroCosmosMod;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokeRecipeMapCategory extends RecipeMapCategory{
	public PokeRecipeMapCategory(RecipeMap<?> recipeMap, IGuiHelper guiHelper){
		super(recipeMap, guiHelper);
		try{
			ModularUI modularUI = recipeMap.createJeiUITemplate(
					setField("importItems", new ItemStackHandler(recipeMap.getMaxInputs()-1)),
					getField("exportItems"),
					getField("importFluids"),
					getField("exportFluids"), 18
			).build(new BlankUIHolder(), Minecraft.getMinecraft().player);
			setField("modularUI", modularUI);
			modularUI.initWidgets();
			setField("backgroundDrawable",
					guiHelper.createBlankDrawable(modularUI.getWidth(), modularUI.getHeight()*2/3+getPropertyShiftAmount2(recipeMap)));
		}catch(Exception ex){
			MacroCosmosMod.LOGGER.info("Cannot hack into RecipeMapCategory", ex);
		}finally{
			cachedFields = null;
		}
	}

	/////////////////////////////////////////////
	// ********************
	// Close your eyes if you value your sanity
	// ********************
	/////////////////////////////////////////////

	@SuppressWarnings({"UnusedAssignment", "GrazieInspection"}) // You're fucking stupid IntelliJ
	private Map<String, Field> cachedFields = new HashMap<>();

	private Field searchField(String field) throws NoSuchFieldException{
		Field f = cachedFields.get(field);
		if(f!=null) return f;
		f = RecipeMapCategory.class.getDeclaredField(field);
		cachedFields.put(field, f);
		return f;
	}

	private <T> T setField(String fieldName, T val) throws NoSuchFieldException, IllegalAccessException{
		Field field = searchField(fieldName);
		field.setAccessible(true);
		field.set(this, val);
		return val;
	}
	@SuppressWarnings("unchecked")
	private <T> T getField(String fieldName) throws NoSuchFieldException, IllegalAccessException{
		Field field = searchField(fieldName);
		field.setAccessible(true);
		return (T)field.get(this);
	}

	/////////////////////////////////////////////
	// ********************
	// Ok you can open your eyes now
	// ********************
	/////////////////////////////////////////////

	@Override public void setRecipe(IRecipeLayout recipeLayout, @Nonnull GTRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients){
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);

		if(!(recipeWrapper instanceof PokeRecipeWrapper)) return;

		PokeRecipeWrapper prw = (PokeRecipeWrapper)recipeWrapper;
		IGuiIngredientGroup<Pokemon> pokemonInputGroup = recipeLayout.getIngredientsGroup(MacroCosmosJeiPlugin.POKEMON_INGREDIENT);

		List<List<Pokemon>> pokemonInputs = ingredients.getInputs(MacroCosmosJeiPlugin.POKEMON_INGREDIENT);
		for(int i = 0; i<pokemonInputs.size(); i++){
			pokemonInputGroup.init(i, true, 18*i, 0);
		}

		pokemonInputGroup.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			PokemonCondition pokemonCondition = prw.getPokemonConditions().get(slotIndex);
			tooltip.add(TextFormatting.YELLOW+"Condition: "+(pokemonCondition!=null ? pokemonCondition.toString() : "??? ???? ??"));
		});
		pokemonInputGroup.set(ingredients);
	}

	// Fuck my life
	private static boolean shouldShiftWidgets2(@Nonnull RecipeMap<?> recipeMap){
		return recipeMap.getMaxOutputs()>=6||recipeMap.getMaxInputs()>=7||
				recipeMap.getMaxFluidOutputs()>=6||recipeMap.getMaxFluidInputs()>=6;
	}

	private static int getPropertyShiftAmount2(@Nonnull RecipeMap<?> recipeMap){
		int maxPropertyCount = 0;
		if(shouldShiftWidgets2(recipeMap)){
			for(Recipe recipe : recipeMap.getRecipeList()){
				if(recipe.getPropertyCount()>maxPropertyCount)
					maxPropertyCount = recipe.getPropertyCount();
			}
		}
		return maxPropertyCount*9;
	}
}
