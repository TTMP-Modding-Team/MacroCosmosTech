package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

public class ConditionedValue implements PokemonValue{
	public static ConditionedValue read(PacketBuffer buffer){
		return new ConditionedValue(PokemonCondition.readCondition(buffer), PokemonValue.readValue(buffer));
	}

	private final PokemonCondition condition;
	private final PokemonValue work;

	public ConditionedValue(PokemonCondition condition, PokemonValue work){
		this.condition = condition;
		this.work = work;
	}

	@Override public float getValue(Pokemon pokemon){
		return condition.test(pokemon) ? work.getValue(pokemon) : 0;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		condition.write(buffer);
		work.writeAdditional(buffer);
	}

	@Override public byte type(){
		return Types.CONDITION;
	}

	@Override public String toString(){
		return "{["+condition+"] => "+work+"}";
	}
}
