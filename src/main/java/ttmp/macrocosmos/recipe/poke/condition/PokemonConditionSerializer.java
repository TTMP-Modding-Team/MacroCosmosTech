package ttmp.macrocosmos.recipe.poke.condition;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.Types;

public class PokemonConditionSerializer{
	public static NBTTagByteArray writeToNBT(PokemonCondition condition){
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		buffer.writeByte(condition.type());
		condition.writeAdditional(buffer);
		buffer.readerIndex(0);
		byte[] bytes = new byte[buffer.writerIndex()];
		buffer.getBytes(0, bytes);
		return new NBTTagByteArray(bytes);
	}

	public static PokemonCondition readCondition(byte[] bytes){
		return readCondition(new PacketBuffer(Unpooled.wrappedBuffer(bytes)));
	}
	public static PokemonCondition readCondition(PacketBuffer buffer){
		switch(buffer.readByte()){
			case Types.ALL:
				return All.read(buffer);
			case Types.ANY:
				return Any.read(buffer);
			case Types.NOT:
				return Not.read(buffer);
			case Types.EGG:
				return Egg.IS_EGG;
			case Types.NOT_EGG:
				return NotEgg.NOT_EGG;
			case Types.SPECIES:
				return Species.read(buffer);
			case Types.TYPE:
				return Type.read(buffer);
			case Types.VESPIQUEN:
				return Vespiquen.read(buffer);
			case Types.ALWAYS:
				return Always.ALWAYS;
			case Types.NEVER:
				return Never.NEVER;
			case Types.COMBEE:
				return Combee.read(buffer);
		}
		return null;
	}
}
