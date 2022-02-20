package ttmp.macrocosmos.combeekeeping;

import net.minecraft.util.text.translation.I18n;

import java.util.Objects;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public final class CombeeType{
	private final String name;
	private final double maxEggProduction;

	public CombeeType(String name, double maxEggProduction){
		if(maxEggProduction<=0) throw new IllegalArgumentException("eggProductionRate");
		this.name = Objects.requireNonNull(name);
		this.maxEggProduction = maxEggProduction;
	}

	public String getName(){
		return name;
	}

	public String translationKey(){
		return "name."+MODID+".combee_type."+name;
	}

	@SuppressWarnings("deprecation") public String getLocalizedName(){
		return I18n.translateToLocal(translationKey());
	}

	public double getMaxEggProduction(){
		return maxEggProduction;
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

}
