package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public class DivValue implements PokemonValue{
	public static DivValue read(PacketBuffer buffer){
		return new DivValue(PokemonValue.readValue(buffer), PokemonValue.readValue(buffer));
	}

	private final PokemonValue num, denom;

	public DivValue(PokemonValue num, PokemonValue denom){
		this.num = num;
		this.denom = denom;
	}

	@Override public float getValue(Pokemon pokemon){
		float value = denom.getValue(pokemon);
		return value==0 ? 0 : num.getValue(pokemon)/value;
	}
	@Override public byte type(){
		return Types.DIV;
	}
	@Override public void writeAdditional(PacketBuffer buffer){
		num.write(buffer);
		denom.write(buffer);
	}

	@Override public String toString(){
		return "("+num+" / "+denom+")";
	}
}
