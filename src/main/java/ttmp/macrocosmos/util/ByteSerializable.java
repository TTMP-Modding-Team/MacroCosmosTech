package ttmp.macrocosmos.util;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

public interface ByteSerializable{
	void write(PacketBuffer buffer);
	default byte[] writeToByteArray(){
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		write(buffer);
		buffer.readerIndex(0);
		byte[] bytes = new byte[buffer.writerIndex()];
		buffer.getBytes(0, bytes);
		return bytes;
	}

	static PacketBuffer createBufferFromBytes(byte[] bytes){
		return new PacketBuffer(Unpooled.wrappedBuffer(bytes));
	}
}
