package ttmp.macrocosmos.jei;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

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

	public static Pokemon fromSpecies(EnumSpecies enumSpecies){
		Pokemon pokemon = map.get(enumSpecies);
		if(pokemon!=null) return pokemon;
		synchronized(map){
			pokemon = map.get(enumSpecies);
			if(pokemon!=null) return pokemon;
			map.put(enumSpecies, pokemon = Pixelmon.pokemonFactory.create(enumSpecies));
			return pokemon;
		}
	}
}
