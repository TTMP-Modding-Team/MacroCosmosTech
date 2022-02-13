package ttmp.macrocosmos.recipe.poke;

import net.minecraft.nbt.NBTTagCompound;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.condition.PokemonConditionSerializer;

public class PokeRecipeModifier{
	public static PokeRecipeModifier read(NBTTagCompound tag){
		return new PokeRecipeModifier(PokemonConditionSerializer.readCondition(tag.getByteArray("c")), tag.getDouble("operationSpeed"));
	}

	private final PokemonCondition condition;
	private final double operationSpeed;

	public PokeRecipeModifier(PokemonCondition condition, double operationSpeed){
		this.condition = condition;
		this.operationSpeed = operationSpeed;
	}

	public PokemonCondition getCondition(){
		return condition;
	}
	public double getOperationSpeed(){
		return operationSpeed;
	}

	@Override public String toString(){
		return "PokeRecipeModifier{"+
				"condition="+condition+
				", operationSpeed="+operationSpeed+
				'}';
	}

	public NBTTagCompound write(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("c", PokemonConditionSerializer.writeToNBT(condition));
		tag.setDouble("s", operationSpeed);
		return tag;
	}
}
