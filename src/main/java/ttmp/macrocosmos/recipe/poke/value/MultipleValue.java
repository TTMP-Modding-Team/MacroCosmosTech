package ttmp.macrocosmos.recipe.poke.value;

import net.minecraft.network.PacketBuffer;

public abstract class MultipleValue implements PokemonValue{
	protected final PokemonValue[] values;

	public MultipleValue(PokemonValue... values){
		this.values = values;
	}

	public MultipleValue(PacketBuffer buffer){
		this.values = new PokemonValue[buffer.readVarInt()];
		for(int i = 0; i<this.values.length; i++)
			this.values[i] = PokemonValue.readValue(buffer);
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(values.length);
		for(PokemonValue work : values)
			work.write(buffer);
	}
}
