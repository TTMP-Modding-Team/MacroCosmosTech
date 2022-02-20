package ttmp.macrocosmos.jei.ingredient;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class PreviewPokemonFactory{
	private static final EnumMap<EnumSpecies, Pokemon> map = new EnumMap<>(EnumSpecies.class);

	public static List<Pokemon> filtered(Predicate<Pokemon> filter){
		EnumSpecies[] values = EnumSpecies.values();
		List<Pokemon> list = new ArrayList<>(values.length-1);
		for(int i = 1; i<values.length; i++){
			Pokemon p = fromSpecies(values[i]);
			if(filter.test(p)) list.add(p);
		}
		return list;
	}
	public static List<Pokemon> all(){
		EnumSpecies[] values = EnumSpecies.values();
		List<Pokemon> list = new ArrayList<>(values.length-1);
		for(int i = 1; i<values.length; i++)
			list.add(fromSpecies(values[i]));
		return list;
	}

	public static Pokemon fromSpecies(EnumSpecies species){
		Pokemon pokemon = map.get(species);
		if(pokemon!=null) return pokemon;
		synchronized(map){
			pokemon = map.get(species);
			if(pokemon!=null) return pokemon;
			map.put(species, pokemon = newFromSpecies(species));
			return pokemon;
		}
	}

	public static Pokemon newFromSpecies(EnumSpecies species){
		Pokemon pokemon = Pixelmon.pokemonFactory.create(species);
		setPreviewPokemon(pokemon, true);
		return pokemon;
	}

	public static boolean isPreviewPokemon(Pokemon pokemon){
		return pokemon.getPersistentData().getBoolean(MODID+".preview");
	}

	public static void setPreviewPokemon(Pokemon pokemon, boolean preview){
		pokemon.getPersistentData().setBoolean(MODID+".preview", preview);
	}
}
