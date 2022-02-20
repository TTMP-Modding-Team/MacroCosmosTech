package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;

public class CombeeTypeCondition implements PokemonCondition{
	public static CombeeTypeCondition read(PacketBuffer buffer){
		return new CombeeTypeCondition(CombeeTypes.withName(buffer.readString(Short.MAX_VALUE)));
	}

	private final CombeeType type;
	public CombeeTypeCondition(CombeeType type){
		this.type = type;
	}

	@Override public boolean test(Pokemon pokemon){
		return CombeeTypes.getCombeeType(pokemon)==type;
	}
	@Override public byte type(){
		return Types.COMBEE_TYPE;
	}
	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeString(type.getName());
	}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocalFormatted("pokemon_condition.macrocosmos.combee_type", type.getLocalizedName());
	}

	@Override public String toString(){
		return type+" Combee";
	}
}
