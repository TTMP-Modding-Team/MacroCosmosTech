package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.util.Join;

public class SumValue extends MultipleValue{
	public static SumValue read(PacketBuffer buffer){
		return new SumValue(buffer);
	}

	public SumValue(PokemonValue... works){
		super(works);
	}
	public SumValue(PacketBuffer buffer){
		super(buffer);
	}

	@Override public float getValue(Pokemon pokemon){
		float sum = 0;
		for(PokemonValue e : values)
			sum += e.getValue(pokemon);
		return sum;
	}
	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(values.length);
		for(PokemonValue work : values)
			work.write(buffer);
	}

	@Override public byte type(){
		return Types.SUM;
	}

	@Override public String toString(){
		return "sum("+Join.join(", ", values)+")";
	}
}
