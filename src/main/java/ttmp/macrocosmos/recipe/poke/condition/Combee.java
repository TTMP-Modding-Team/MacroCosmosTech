package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;

public class Combee implements PokemonCondition{
	public static Combee read(PacketBuffer buffer){
		return new Combee(CombeeTypes.get(buffer.readString(Short.MAX_VALUE)));
	}

	private final CombeeType type;
	public Combee(CombeeType type){
		this.type = type;
	}

	@Override public boolean test(Pokemon pokemon){
		return CombeeType.getCombeeType(pokemon)==type;
	}
	@Override public byte type(){
		return Types.COMBEE;
	}
	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeString(type.getName());
	}
}
