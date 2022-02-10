package ttmp.macrocosmos.recipe.poke;

import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

public class PokeRecipeModifier{
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
}
