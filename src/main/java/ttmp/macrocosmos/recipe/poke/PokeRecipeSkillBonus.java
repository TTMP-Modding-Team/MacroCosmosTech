package ttmp.macrocosmos.recipe.poke;

import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import java.util.Objects;

public final class PokeRecipeSkillBonus{
	public static PokeRecipeSkillBonus read(PacketBuffer buffer){
		return new PokeRecipeSkillBonus(buffer.readString(Short.MAX_VALUE), PokemonValue.readValue(buffer), buffer.readInt());
	}

	private final String skillName;
	private final PokemonValue additionalModifier;
	private final int effectDuration;

	public PokeRecipeSkillBonus(String skillName, PokemonValue additionalModifier, int effectDuration){
		if(effectDuration<=0) throw new IllegalArgumentException("effectDuration");
		this.skillName = Objects.requireNonNull(skillName);
		this.additionalModifier = Objects.requireNonNull(additionalModifier);
		this.effectDuration = effectDuration;
	}

	public String getSkillName(){
		return skillName;
	}
	public PokemonValue getAdditionalModifier(){
		return additionalModifier;
	}
	public int getEffectDuration(){
		return effectDuration;
	}

	public void write(PacketBuffer buffer){
		buffer.writeString(skillName);
		additionalModifier.write(buffer);
		buffer.writeInt(effectDuration);
	}

	@Override public boolean equals(Object o){
		if(this==o) return true;
		if(o==null||getClass()!=o.getClass()) return false;
		PokeRecipeSkillBonus that = (PokeRecipeSkillBonus)o;
		return getSkillName().equals(that.getSkillName());
	}
	@Override public int hashCode(){
		return Objects.hash(getSkillName());
	}

	@Override public String toString(){
		return "PokeRecipeSkillBonus{"+
				"skillName='"+skillName+'\''+
				", additionalModifier="+additionalModifier+
				", effectDuration="+effectDuration+
				'}';
	}
}
