package ttmp.macrocosmos.util;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

public interface TypedSerializable{
	byte type();
	void writeAdditional(PacketBuffer buffer);

	default void write(PacketBuffer buffer){
		buffer.writeByte(type());
		writeAdditional(buffer);
	}
	default byte[] writeToByteArray(){
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		buffer.writeByte(type());
		writeAdditional(buffer);
		buffer.readerIndex(0);
		byte[] bytes = new byte[buffer.writerIndex()];
		buffer.getBytes(0, bytes);
		return bytes;
	}
}
