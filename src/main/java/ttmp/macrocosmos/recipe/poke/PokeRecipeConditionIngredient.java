package ttmp.macrocosmos.recipe.poke;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.item.ModItems;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

import javax.annotation.Nullable;

/**
 * Yeah, don't even question
 */
public class PokeRecipeConditionIngredient extends Ingredient{
	private static final class ItemHolder{
		static final ItemStack item = new ItemStack(ModItems.SHH);
	}
	private static ItemStack getItem(){
		return ItemHolder.item;
	}

	private final PokeRecipeMap<?> recipeMap;
	private final PokeRecipeMetadata metadata;

	public PokeRecipeConditionIngredient(PokeRecipeMap<?> recipeMap, PokeRecipeMetadata metadata){
		super(getItem());
		this.recipeMap = recipeMap;
		this.metadata = metadata;
	}

	@Override public boolean apply(@Nullable ItemStack input){
		if(input==null) return false;
		PokemonRecipeLogic.PokeRecipeIngredientCap cap = input.getCapability(Caps.POKE_RECIPE_INGREDIENT, null);
		return cap!=null&&this.recipeMap.test(cap.getContainer(), this.metadata);
	}

	@Override public String toString(){
		return "[Pokemon="+metadata.toString()+"]";
	}
}
