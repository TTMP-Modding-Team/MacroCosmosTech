package ttmp.macrocosmos.jei.ingredient;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import mezz.jei.api.ingredients.IIngredientHelper;
import ttmp.macrocosmos.MacroCosmosMod;

import javax.annotation.Nullable;

public class PokemonIngredientHelper implements IIngredientHelper<Pokemon>{
	@Nullable @Override public Pokemon getMatch(Iterable<Pokemon> ingredients, Pokemon ingredientToMatch){
		for(Pokemon p : ingredients)
			if(p.getSpecies()==ingredientToMatch.getSpecies()) return p;
		return null;
	}
	@Override public String getDisplayName(Pokemon p){
		return p.getDisplayName();
	}
	@Override public String getUniqueId(Pokemon p){
		return "pokemon:"+p.getSpecies(); // TODO what?
	}
	@Override public String getWildcardId(Pokemon p){
		return getUniqueId(p);
	}
	@Override public String getModId(Pokemon p){
		return MacroCosmosMod.MODID;
	}
	@Override public String getResourceId(Pokemon p){
		return p.getSpecies().name;
	}
	@Override public Pokemon copyIngredient(Pokemon p){
		return PreviewPokemonFactory.fromSpecies(p.getSpecies()); // TODO maybe wrong?
	}
	@Override public String getErrorInfo(@Nullable Pokemon p){
		if(p==null) return "null";
		return "Pokemon "+p.getUUID()+" "+p.getSpecies()+" "+p.getGender()+" "+p.isEgg();
	}
}
