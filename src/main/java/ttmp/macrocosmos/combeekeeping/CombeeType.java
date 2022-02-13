package ttmp.macrocosmos.combeekeeping;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public final class CombeeType{
	private final String name;
	private final double eggProductionRate;

	public CombeeType(String name, double eggProductionRate){
		if(eggProductionRate<=0) throw new IllegalArgumentException("eggProductionRate");
		this.name = Objects.requireNonNull(name);
		this.eggProductionRate = eggProductionRate;
	}

	public String getName(){
		return name;
	}

	public String translationKey(){
		return "name."+MODID+".combee_type."+name;
	}

	public double getEggProductionRate(){
		return eggProductionRate;
	}

	@Override public String toString(){
		return "CombeeType{"+name+"}";
	}

	@Override public boolean equals(Object o){
		if(this==o) return true;
		if(o==null||getClass()!=o.getClass()) return false;
		CombeeType that = (CombeeType)o;
		return getName().equals(that.getName());
	}
	@Override public int hashCode(){
		return Objects.hash(getName());
	}

	private static final String COMBEE_TYPE_KEY = MODID+".combeeType";

	public static CombeeType getCombeeType(Pokemon pokemon){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		return CombeeTypes.getCombeeTypes().getOrDefault(persistentData.getString(COMBEE_TYPE_KEY), CombeeTypes.NORMAL);
	}
	public static void setCombeeType(Pokemon pokemon, CombeeType type){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		persistentData.setString(COMBEE_TYPE_KEY, type.name);
	}
}
