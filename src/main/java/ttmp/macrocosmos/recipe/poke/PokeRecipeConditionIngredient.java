package ttmp.macrocosmos.recipe.poke;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.item.ModItems;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Yeah, don't even question
 */
public class PokeRecipeConditionIngredient extends Ingredient{
	private static final class ItemHolder{
		static final ItemStack item = new ItemStack(ModItems.POKE_RECIPE_INGREDIENT);
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

	@Nullable private List<PokemonCondition> conditions;

	public List<PokemonCondition> getConditions(){
		if(conditions==null) conditions = this.defaultMetadata.getConditions(this.overrideMetadata);
		return conditions;
	}

	@Override public boolean apply(@Nullable ItemStack input){
		if(input==null) return false;
		PokemonRecipeLogic.PokeRecipeIngredientCap cap = input.getCapability(Caps.POKE_RECIPE_INGREDIENT, null);
		return cap!=null&&PokeRecipeMatch.test(getConditions(), cap.getContainer());
	}

	@Override public boolean isSimple(){
		return false;
	}

	@Override public String toString(){
		return "PokeRecipeConditionIngredient{"+
				"defaultMetadata="+defaultMetadata+
				", overrideMetadata="+overrideMetadata+
				'}';
	}
}
