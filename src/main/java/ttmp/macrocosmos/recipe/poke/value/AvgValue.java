package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.util.Join;

public class AvgValue extends MultipleValue{
	public static AvgValue read(PacketBuffer buffer){
		return new AvgValue(buffer);
	}

	public AvgValue(PokemonValue... works){
		super(works);
	}
	public AvgValue(PacketBuffer buffer){
		super(buffer);
	}

	@Override public float getValue(Pokemon pokemon){
		if(values.length==0) return 0;
		float sum = 0;
		for(PokemonValue e : values)
			sum += e.getValue(pokemon);
		return sum/values.length;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(values.length);
		for(PokemonValue work : values)
			work.write(buffer);
	}

	@Override public byte type(){
		return Types.AVG;
	}

	@Override public String toString(){
		return "avg("+Join.join(", ", values)+")";
	}
}
