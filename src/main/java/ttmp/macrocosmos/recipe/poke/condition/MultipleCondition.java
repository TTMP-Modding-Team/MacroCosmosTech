package ttmp.macrocosmos.recipe.poke.condition;

import net.minecraft.network.PacketBuffer;
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
