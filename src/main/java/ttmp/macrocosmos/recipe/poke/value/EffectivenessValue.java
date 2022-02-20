package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.network.PacketBuffer;

public class EffectivenessValue implements PokemonValue{
	public static EffectivenessValue read(PacketBuffer buffer){
		return new EffectivenessValue(EnumType.parseType(buffer.readUnsignedByte()));
	}

	private final EnumType attackType;

	public EffectivenessValue(EnumType attackType){
		this.attackType = attackType;
	}

	@Override public float getValue(Pokemon pokemon){
		return EnumType.getTotalEffectiveness(pokemon.getBaseStats().getTypeList(), attackType);
	}

	@Override public byte type(){
		return Types.EFFECTIVENESS;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeByte(attackType.getIndex());
	}

	@Override public String toString(){
		return "Effectiveness vs. "+attackType;
	}
}
