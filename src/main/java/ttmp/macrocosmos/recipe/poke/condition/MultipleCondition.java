package ttmp.macrocosmos.recipe.poke.condition;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;
import ttmp.macrocosmos.util.Join;

public abstract class MultipleCondition implements PokemonCondition{
	protected final PokemonCondition[] conditions;

	public MultipleCondition(PokemonCondition... conditions){
		this.conditions = conditions;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(conditions.length);
		for(PokemonCondition c : conditions) c.write(buffer);
	}

	@Override public String localize(){
		switch(conditions.length){
			case 0: return Always.ALWAYS.localize();
			case 1: return conditions[0].localize();
			default:{
				String s = conditions[0].localize();
				for(int i=1; i<conditions.length; i++){
					//noinspection deprecation
					s = I18n.translateToLocalFormatted(delimiterLocalizationKey(), s, conditions[i].localize());
				}
				return s;
			}
		}
	}

	protected abstract String delimiterLocalizationKey();

	@Override public String toString(){
		return "("+Join.join(delimiter(), conditions)+")";
	}

	protected abstract String delimiter();

	protected static PokemonCondition[] readConditions(PacketBuffer buffer){
		PokemonCondition[] conditions = new PokemonCondition[buffer.readVarInt()];
		for(int i = 0; i<conditions.length; i++)
			conditions[i] = PokemonCondition.readCondition(buffer);
		return conditions;
	}
}
