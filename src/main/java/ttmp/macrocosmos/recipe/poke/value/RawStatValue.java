package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.ArrayUtils;
import ttmp.macrocosmos.MacroCosmosMod;
import ttmp.macrocosmos.util.BitMask;
import ttmp.macrocosmos.util.Join;

import java.util.EnumSet;

public class RawStatValue implements PokemonValue{
	protected static final StatsType[] allowedValues = {
			StatsType.Attack,
			StatsType.Defence,
			StatsType.SpecialAttack,
			StatsType.SpecialDefence,
			StatsType.Speed
	};

	public static RawStatValue all(){
		return new RawStatValue(allowedValues);
	}

	public static RawStatValue read(PacketBuffer buffer){
		return new RawStatValue(buffer.readByte());
	}

	protected final EnumSet<StatsType> stats = EnumSet.noneOf(StatsType.class);

	public RawStatValue(StatsType... stat){
		for(StatsType s : stat){
			if(!ArrayUtils.contains(allowedValues, stat))
				MacroCosmosMod.LOGGER.info("StatBasedWork can only use [{}], not {}", Join.join(allowedValues), stat);
			else stats.add(s);
		}
	}
	protected RawStatValue(byte bitMask){
		for(StatsType s : allowedValues){
			if(BitMask.get(bitMask, s.getStatIndex())){
				this.stats.add(s);
			}
		}
	}

	@Override public float getValue(Pokemon pokemon){
		float sum = 0;
		for(StatsType s : stats){
			switch(s){
				case Attack:
				case Defence:
				case SpecialAttack:
				case SpecialDefence:
				case Speed:
					sum += pokemon.getStat(s);
			}
		}
		return sum;
	}

	@Override public byte type(){
		return Types.RAW_STAT;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		int bitMask = 0;
		for(StatsType s : stats){
			bitMask = BitMask.set(bitMask, s.getStatIndex(), true);
		}
		buffer.writeByte(bitMask);
	}

	@Override public String toString(){
		switch(stats.size()){
			case 0:
				return "0";
			case 1:
				return Join.join(stats);
			default:
				return "("+Join.join(" + ", stats)+")";
		}
	}
}
