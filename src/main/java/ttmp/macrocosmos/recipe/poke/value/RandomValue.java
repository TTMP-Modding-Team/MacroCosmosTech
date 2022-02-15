package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.util.Join;

import java.util.Random;

public class RandomValue extends MultipleValue{
	public static final Random random = new Random();

	public static PokemonValue read(PacketBuffer buffer){
		return new RandomValue(buffer);
	}

	public RandomValue(PokemonValue... values){
		super(values);
	}
	public RandomValue(PacketBuffer buffer){
		super(buffer);
	}

	@Override public float getValue(Pokemon pokemon){
		return values.length==0 ? 0 : values[random.nextInt(values.length)].getValue(pokemon);
	}

	@Override public byte type(){
		return Types.RANDOM;
	}

	@Override public String toString(){
		return "("+Join.join(" or ", values)+")";
	}
}
