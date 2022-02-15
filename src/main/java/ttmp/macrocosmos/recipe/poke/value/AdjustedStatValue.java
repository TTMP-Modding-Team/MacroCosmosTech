package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.network.PacketBuffer;

public class AdjustedStatValue extends RawStatValue{
	public static AdjustedStatValue read(PacketBuffer buffer){
		return new AdjustedStatValue(buffer.readByte());
	}

	public AdjustedStatValue(StatsType... stat){
		super(stat);
	}
	public AdjustedStatValue(byte bitMask){
		super(bitMask);
	}

	@Override public float getValue(Pokemon pokemon){
		return super.getValue(pokemon)/100f;
	}
	@Override public byte type(){
		return Types.ADJUSTED_STAT;
	}
	@Override public String toString(){
		return "adj("+super.toString()+")";
	}
}
