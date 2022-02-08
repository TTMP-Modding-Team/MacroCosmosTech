package ttmp.macrocosmos.combeekeeping;

import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import net.minecraft.init.Blocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CombeeTypes{
	private static final Map<String, CombeeType> combeeTypes = new HashMap<>();

	public static final CombeeType NORMAL = new CombeeType("normal", new SimpleRecipeBuilder()
			.input(Blocks.DIRT)
			.output(Blocks.BEDROCK)
			.build().getResult());

	public static void init(){
		register(NORMAL);
	}

	public static Map<String, CombeeType> getCombeeTypes(){
		return Collections.unmodifiableMap(combeeTypes);
	}

	public static void register(CombeeType type){
		if(combeeTypes.put(type.getName(), type)!=null)
			throw new IllegalStateException("Duplicated combee type "+type.getName());
	}
}
