package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.mte.trait.ApiaryLogic;

import javax.annotation.Nullable;

public class Vespiquen implements PokemonCondition{
	public static PokemonCondition read(PacketBuffer buffer){
		return new Vespiquen(buffer.readBoolean() ? CombeeTypes.withName(buffer.readString(Short.MAX_VALUE)) : null);
	}

	@Nullable private final CombeeType combeeType;

	public Vespiquen(@Nullable CombeeType combeeType){
		this.combeeType = combeeType;
	}

	@Override public boolean test(Pokemon pokemon){
		return ApiaryLogic.isValidQueen(pokemon)&&
				(combeeType==null||combeeType==CombeeTypes.getCombeeType(pokemon));
	}

	@Override public byte type(){
		return Types.VESPIQUEN;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeBoolean(combeeType!=null);
		if(combeeType!=null) buffer.writeString(combeeType.getName());
	}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return combeeType!=null ?
				I18n.translateToLocalFormatted("pokemon_condition.macrocosmos.vespiquen.type", combeeType.getLocalizedName()) :
				I18n.translateToLocal("pokemon_condition.macrocosmos.vespiquen");
	}

	@Override public String toString(){
		return combeeType!=null ? combeeType.getName()+" Vespiquen" : "Vespiquen";
	}
}
