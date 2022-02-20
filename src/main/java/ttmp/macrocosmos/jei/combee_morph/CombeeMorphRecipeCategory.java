package ttmp.macrocosmos.jei.combee_morph;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.client.resources.I18n;
import ttmp.macrocosmos.MacroCosmosMod;
import ttmp.macrocosmos.combeekeeping.CombeeMorph;
import ttmp.macrocosmos.jei.MacroCosmosJeiPlugin;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class CombeeMorphRecipeCategory implements IRecipeCategory<CombeeMorphRecipeWrapper>{
	public static final String UID = MODID+":combee_morph";

	private final IDrawable background;

	public CombeeMorphRecipeCategory(IGuiHelper helper){
		this.background = helper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 168, 125, 18)
				.addPadding(0, 20, 0, 0)
				.build();
	}

	@Override public String getUid(){
		return UID;
	}
	@Override public String getTitle(){
		return I18n.format("recipe_category.macrocosmos.combee_morph");
	}
	@Override public String getModName(){
		return MacroCosmosMod.NAME;
	}
	@Override public IDrawable getBackground(){
		return background;
	}
	@Override public void setRecipe(IRecipeLayout recipeLayout, CombeeMorphRecipeWrapper recipeWrapper, IIngredients ingredients){
		IGuiIngredientGroup<Pokemon> pokemonInputs = recipeLayout.getIngredientsGroup(MacroCosmosJeiPlugin.POKEMON_INGREDIENT);
		pokemonInputs.init(0, true, 1, 1);
		pokemonInputs.init(1, true, 1+18, 1);
		pokemonInputs.init(2, false, 1+18*2, 1);

		CombeeMorph morph = recipeWrapper.getMorph();
		pokemonInputs.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if(input){
				switch(slotIndex){
					case 0:
						tooltip.add("Condition: "+morph.getQueenCondition().localize());
						break;
					case 1:
						tooltip.add("Condition: "+morph.getWorkerCondition().localize());
				}
			}else{
				tooltip.add("Chance: "+(morph.getChance()*100)+"%"); // TODO localize
			}
		});
		pokemonInputs.set(ingredients);
	}
}
