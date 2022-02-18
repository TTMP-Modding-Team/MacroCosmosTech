package ttmp.macrocosmos.recipe.poke;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.item.ModItems;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;
import ttmp.macrocosmos.util.PokeRecipeMatch;

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

	private final PokeRecipeMetadata defaultMetadata;
	private final PokeRecipeMetadata overrideMetadata;

	public PokeRecipeConditionIngredient(PokeRecipeMetadata recipeMap, PokeRecipeMetadata overrideMetadata){
		super(getItem());
		this.defaultMetadata = recipeMap;
		this.overrideMetadata = overrideMetadata;
	}

	@Override public boolean apply(@Nullable ItemStack input){
		if(input==null) return false;
		PokemonRecipeLogic.PokeRecipeIngredientCap cap = input.getCapability(Caps.POKE_RECIPE_INGREDIENT, null);
		return cap!=null&&PokeRecipeMatch.test(this.defaultMetadata.getConditions(this.overrideMetadata), cap.getContainer());
	}

	@Override public String toString(){
		return "PokeRecipeConditionIngredient{"+
				"defaultMetadata="+defaultMetadata+
				", overrideMetadata="+overrideMetadata+
				'}';
	}
}
