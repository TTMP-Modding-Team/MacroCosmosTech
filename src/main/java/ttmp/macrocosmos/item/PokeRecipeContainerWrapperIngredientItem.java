package ttmp.macrocosmos.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

import javax.annotation.Nullable;

// PRCWII for short
public class PokeRecipeContainerWrapperIngredientItem extends Item{
	@Nullable @Override public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
		return new PokemonRecipeLogic.Wtf();
	}
}
