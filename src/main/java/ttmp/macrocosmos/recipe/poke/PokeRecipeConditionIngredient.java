package ttmp.macrocosmos.recipe.poke;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

import javax.annotation.Nullable;

/**
 * Yeah, don't even question
 */
public class PokeRecipeConditionIngredient extends Ingredient{
	private final PokeRecipeCondition condition;
	public PokeRecipeConditionIngredient(PokeRecipeCondition condition){
		this.condition = condition;
	}

	@Override public boolean test(@Nullable ItemStack input){
		if(input==null) return false;
		PokemonRecipeLogic.Wtf wtf = input.getCapability(Caps.WTF, null);
		return wtf!=null&&this.condition.test(wtf.getContainer());
	}
}
