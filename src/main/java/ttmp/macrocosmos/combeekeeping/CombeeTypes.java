package ttmp.macrocosmos.combeekeeping;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;
import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.combeeType;

public class CombeeTypes{
	private static final Map<String, CombeeType> combeeTypes = new HashMap<>();
	private static final List<CombeeMorph> morphs = new ArrayList<>();

	public static final CombeeType NORMAL = new CombeeType("normal", 10000);
	public static final CombeeType MORPH_TEST = new CombeeType("morph_test", 10000);
	public static final CombeeType MORPH_TEST_2 = new CombeeType("morph_test_2", 10000);

	public static final CombeeMorph NORMAL_TO_MORPH_TEST = CombeeMorph.eggOf(combeeType(NORMAL), combeeType(MORPH_TEST)).chance(.5).into(MORPH_TEST);
	private static final String COMBEE_TYPE_KEY = MODID+".combeeType";

	public static void init(){
		register(NORMAL);
		register(MORPH_TEST);
		register(MORPH_TEST_2);

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

	public static CombeeType withName(String name){
		return combeeTypes.getOrDefault(name, NORMAL);
	}

	public static CombeeType getCombeeType(Pokemon pokemon){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		return getCombeeTypes().getOrDefault(persistentData.getString(COMBEE_TYPE_KEY), NORMAL);
	}
	public static void setCombeeType(Pokemon pokemon, CombeeType type){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		persistentData.setString(COMBEE_TYPE_KEY, type.getName());
	}

	public static CombeeType generateType(Pokemon queen, Pokemon worker, Random random){
		for(CombeeMorph morph : morphs){
			if(morph.test(queen, worker)&&random.nextDouble()<=morph.getChance()){
				return morph.getMorphType();
			}
		}
		return random.nextBoolean() ? getCombeeType(queen) : getCombeeType(worker);
	}
}
