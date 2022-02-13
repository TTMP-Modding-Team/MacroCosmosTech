package ttmp.macrocosmos.combeekeeping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.combeeType;

public class CombeeTypes{
	private static final Map<String, CombeeType> combeeTypes = new HashMap<>();
	private static final List<CombeeMorph> morphs = new ArrayList<>();

	public static final CombeeType NORMAL = new CombeeType("normal", 10000);
	public static final CombeeType MORPH_TEST = new CombeeType("morph_test", 10000);

	public static final CombeeMorph NORMAL_TO_MORPH_TEST = CombeeMorph.eggOf(combeeType(NORMAL), combeeType(MORPH_TEST)).chance(.5).into(MORPH_TEST);

	public static void init(){
		register(NORMAL);
		register(MORPH_TEST);

		register(NORMAL_TO_MORPH_TEST);
	}

	public static Map<String, CombeeType> getCombeeTypes(){
		return Collections.unmodifiableMap(combeeTypes);
	}
	public static List<CombeeMorph> getMorphs(){
		return Collections.unmodifiableList(morphs);
	}

	public static void register(CombeeType type){
		if(combeeTypes.put(type.getName(), type)!=null)
			throw new IllegalStateException("Duplicated combee type "+type.getName());
	}

	public static void register(CombeeMorph morph){
		morphs.add(morph);
	}

	public static CombeeType get(String name){
		return combeeTypes.getOrDefault(name, NORMAL);
	}
}
