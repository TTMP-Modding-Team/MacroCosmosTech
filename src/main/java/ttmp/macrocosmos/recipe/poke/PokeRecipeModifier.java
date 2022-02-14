package ttmp.macrocosmos.recipe.poke;

import net.minecraft.nbt.NBTTagCompound;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

public class PokeRecipeModifier{
	public static PokeRecipeModifier read(NBTTagCompound tag){
		return new PokeRecipeModifier(PokemonCondition.readCondition(tag.getByteArray("c")), tag.getDouble("operationSpeed"));
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
		tag.setByteArray("c", condition.writeToByteArray());
		tag.setDouble("s", operationSpeed);
		return tag;
	}
}
