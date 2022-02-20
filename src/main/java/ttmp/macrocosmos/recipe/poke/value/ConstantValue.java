package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public class ConstantValue implements PokemonValue{
	public static ConstantValue read(PacketBuffer buffer){
		return new ConstantValue(buffer.readFloat());
	}

	private final float value;

	public ConstantValue(float value){
		this.value = value;
	}

	@Override public float getValue(Pokemon pokemon){
		return value;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@Override public byte type(){
		return Types.CONST;
	}

	@Override public String toString(){
		return decimalFormat.format(value);
	}
}
