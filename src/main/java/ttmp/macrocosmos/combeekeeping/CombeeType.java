package ttmp.macrocosmos.combeekeeping;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import gregtech.api.recipes.Recipe;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class CombeeType{
	private final String name;
	private final Recipe production;
	private final List<Morph> morphs = new ArrayList<>();

	public CombeeType(String name, Recipe production){
		this.name = name;
		this.production = production;
	}

	public String getName(){
		return name;
	}

	public String translationKey(){
		return "name."+MODID+".combee_type."+name;
	}

	public static class Morph{
		// TODO predicate?
		private final double probability;
		private final String combeeType;

		public Morph(double probability, String combeeType){
			this.probability = probability;
			this.combeeType = combeeType;
		}

		public double getProbability(){
			return probability;
		}
		public String getCombeeType(){
			return combeeType;
		}
	}

	private static final String COMBEE_TYPE_KEY = MODID+".combeeType";

	@Nullable public static CombeeType getCombeeType(Pokemon pokemon){
		if(pokemon.getSpecies()==EnumSpecies.Combee||pokemon.getSpecies()==EnumSpecies.Vespiquen){
			NBTTagCompound persistentData = pokemon.getPersistentData();
			String type = persistentData.getString(COMBEE_TYPE_KEY);
			return CombeeTypes.getCombeeTypes().getOrDefault(type, CombeeTypes.NORMAL);
		}
		return null;
	}
	public static void setCombeeType(Pokemon pokemon, CombeeType type){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		persistentData.setString(COMBEE_TYPE_KEY, type.name);
	}
}
