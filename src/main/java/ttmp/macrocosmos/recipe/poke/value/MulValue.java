package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.util.Join;

public class MulValue extends MultipleValue{
	public static MulValue read(PacketBuffer buffer){
		return new MulValue(buffer);
	}

	public MulValue(PokemonValue... works){
		super(works);
	}
	public MulValue(PacketBuffer buffer){
		super(buffer);
	}

	@Override public float getValue(Pokemon pokemon){
		float sum = 1;
		for(PokemonValue e : values)
			sum *= e.getValue(pokemon);
		return sum;
	}
	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(values.length);
		for(PokemonValue work : values)
			work.write(buffer);
	}

	@Override public byte type(){
		return Types.MUL;
	}

	@Override public String toString(){
		return "("+Join.join(" * ", values)+")";
	}
}
