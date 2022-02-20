package ttmp.macrocosmos.jei.combee_morph;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import ttmp.macrocosmos.combeekeeping.CombeeMorph;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.jei.MacroCosmosJeiPlugin;
import ttmp.macrocosmos.jei.ingredient.PreviewPokemonFactory;

import java.util.Arrays;

public class CombeeMorphRecipeWrapper implements IRecipeWrapper{
	private final CombeeMorph morph;

	public CombeeMorphRecipeWrapper(CombeeMorph morph){
		this.morph = morph;
	}

	public CombeeMorph getMorph(){
		return morph;
	}

	@Override public void getIngredients(IIngredients ingredients){
		if(morph.checkForReverseCase()){
			ingredients.setInputLists(MacroCosmosJeiPlugin.POKEMON_INGREDIENT, Arrays.asList(
					Arrays.asList(
							PreviewPokemonFactory.newFromSpecies(EnumSpecies.Vespiquen),
							PreviewPokemonFactory.newFromSpecies(EnumSpecies.Combee)
					), Arrays.asList(
							PreviewPokemonFactory.newFromSpecies(EnumSpecies.Combee),
							PreviewPokemonFactory.newFromSpecies(EnumSpecies.Vespiquen)
					)
			));
		}else{
			ingredients.setInputs(MacroCosmosJeiPlugin.POKEMON_INGREDIENT, Arrays.asList(
					PreviewPokemonFactory.newFromSpecies(EnumSpecies.Vespiquen),
					PreviewPokemonFactory.newFromSpecies(EnumSpecies.Combee)
			));
		}
		Pokemon out = PreviewPokemonFactory.newFromSpecies(EnumSpecies.Combee);
		CombeeTypes.setCombeeType(out, morph.getMorphType());
		ingredients.setOutput(MacroCosmosJeiPlugin.POKEMON_INGREDIENT, out);
	}
}
