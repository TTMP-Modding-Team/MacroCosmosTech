package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;

public class Type implements PokemonCondition{
	public static PokemonCondition read(PacketBuffer buffer){
		return new Type(EnumType.parseType(buffer.readUnsignedByte()));
	}

	private final EnumType type;

	public Type(EnumType type){
		this.type = type;
	}

	@Override public boolean test(Pokemon pokemon){
		return pokemon.getBaseStats().getTypeList().contains(type);
	}

	@Override public byte type(){
		return Types.TYPE;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeByte(type.getIndex());
	}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocalFormatted("pokemon_condition.macrocosmos.type", type.getLocalizedName());
	}

	@Override public String toString(){
		return type+" Type";
	}
}
