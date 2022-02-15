package ttmp.macrocosmos.util;

import net.minecraft.network.PacketBuffer;

public interface TypedSerializable extends ByteSerializable{
	byte type();
	void writeAdditional(PacketBuffer buffer);

	@Override default void write(PacketBuffer buffer){
		buffer.writeByte(type());
		writeAdditional(buffer);
	}
}
