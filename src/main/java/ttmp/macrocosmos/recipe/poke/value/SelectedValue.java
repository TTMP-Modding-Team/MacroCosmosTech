package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

public class SelectedValue implements PokemonValue{
	public static SelectedValue read(PacketBuffer buffer){
		return new SelectedValue(PokemonCondition.readCondition(buffer), PokemonValue.readValue(buffer), PokemonValue.readValue(buffer));
	}

	private final PokemonCondition condition;
	private final PokemonValue onTrue, onFalse;

	public SelectedValue(PokemonCondition condition, PokemonValue onTrue, PokemonValue onFalse){
		this.condition = condition;
		this.onTrue = onTrue;
		this.onFalse = onFalse;
	}

	@Override public float getValue(Pokemon pokemon){
		return (condition.test(pokemon) ? onTrue : onFalse)
				.getValue(pokemon);
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		condition.write(buffer);
		onTrue.writeAdditional(buffer);
		onFalse.writeAdditional(buffer);
	}

	@Override public byte type(){
		return Types.SELECT;
	}

	@Override public String toString(){
		return "{["+condition+"] ? "+onTrue +" : "+onFalse+"}";
	}
}
