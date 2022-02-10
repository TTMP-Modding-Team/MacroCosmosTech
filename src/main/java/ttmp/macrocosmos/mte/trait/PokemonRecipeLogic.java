package ttmp.macrocosmos.mte.trait;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.RecipeLogicEnergy;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.common.ConfigHolder;
import gregtech.common.inventory.handlers.SingleItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.capability.EmptyPokemonContainer;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.item.ModItems;
import ttmp.macrocosmos.recipe.poke.PokeRecipe;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PokemonRecipeLogic extends RecipeLogicEnergy{
	private final Supplier<PokemonContainer> pokemonInput;

	protected double partialProgressTime;

	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer, Supplier<PokemonContainer> pokemonInput){
		super(metaTileEntity, recipeMap, energyContainer);
		this.pokemonInput = pokemonInput;
	}

	@Override public String getName(){
		return "pokemon_recipe_logic";
	}

	@Nullable private ItemStack inputInventoryWrappedPokemonContainerInputItemStack;
	@Nullable private IItemHandlerModifiable inputInventory;

	@Override protected IItemHandlerModifiable getInputInventory(){
		if(inputInventory==null){
			inputInventoryWrappedPokemonContainerInputItemStack = new ItemStack(ModItems.SHH);
			inputInventory = new CombinedInvWrapper(new SingleItemStackHandler(inputInventoryWrappedPokemonContainerInputItemStack), getActualInputInventory());
		}
		if(inputInventoryWrappedPokemonContainerInputItemStack!=null){
			inputInventoryWrappedPokemonContainerInputItemStack.setCount(1);
			Wtf wtf = inputInventoryWrappedPokemonContainerInputItemStack.getCapability(Caps.WTF, null);
			if(wtf!=null) wtf.setContainer(pokemonInput.get());
		}
		return inputInventory;
	}

	protected IItemHandlerModifiable getActualInputInventory(){
		return super.getInputInventory();
	}

	@Nullable public PokeRecipeMetadata getPreviousRecipeMetadata(){
		Recipe previousRecipe = this.getPreviousRecipe();
		if(previousRecipe==null) return null;
		return previousRecipe instanceof PokeRecipe ? ((PokeRecipe)previousRecipe).getMetadata() : null;
	}

	@Override protected void updateRecipeProgress(){
		if(progress()){
			//as recipe starts with progress on 1 this has to be > only not => to compensate for it
			if(progressTime>=maxProgressTime+1) completeRecipe();
			if(this.hasNotEnoughEnergy&&getEnergyInputPerSecond()>19L*recipeEUt)
				this.hasNotEnoughEnergy = false;
		}else if(recipeEUt>0){
			//only set hasNotEnoughEnergy if this recipe is consuming recipe
			//generators always have enough energy
			this.hasNotEnoughEnergy = true;
			//if current progress value is greater than 2, decrement it by 2
			if(progressTime>=2){
				if(ConfigHolder.machines.recipeProgressLowEnergy) this.progressTime = 1;
				else this.progressTime = Math.max(1, progressTime-2);
			}
		}
	}

	protected boolean progress(){
		if(!canRecipeProgress||!drawEnergy(recipeEUt, true)) return false;
		drawEnergy(recipeEUt, false);

		double progressModifier = getProgressModifier();
		if(progressModifier>0){
			partialProgressTime += progressModifier;
			progressTime = Math.max(1, (int)partialProgressTime);
		}
		return true;
	}

	protected double getProgressModifier(){
		PokeRecipeMetadata metadata = getPreviousRecipeMetadata();
		if(metadata!=null){
			double progress = 0;
			PokemonContainer pokemonContainer = pokemonInput.get();
			for(int i = 0; i<pokemonContainer.size(); i++)
				progress += metadata.calculateProgress(pokemonContainer.getPokemon(i));
			return progress;
		}else return 1;
	}

	@Override protected boolean canProgressRecipe(){
		return super.canProgressRecipe();
	}
	@Override protected void setupRecipe(Recipe recipe){
		super.setupRecipe(recipe);
		this.partialProgressTime = 1;
	}
	@Override protected void completeRecipe(){
		super.completeRecipe();
		this.partialProgressTime = 0;
	}
	public void cancelRecipe(){
		this.progressTime = 0;
		setMaxProgress(0);
		this.recipeEUt = 0;
		this.fluidOutputs = null;
		this.itemOutputs = null;
		this.hasNotEnoughEnergy = false;
		this.wasActiveAndNeedsUpdate = true;
		this.parallelRecipesPerformed = 0;
		this.partialProgressTime = 0;
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = super.serializeNBT();
		tag.setDouble("Progress", partialProgressTime);
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.partialProgressTime = tag.getDouble("Progress");
	}

	/**
	 * wtf?
	 */
	public static final class Wtf implements ICapabilityProvider{
		private PokemonContainer container = EmptyPokemonContainer.EMPTY;

		public PokemonContainer getContainer(){
			return container;
		}
		public void setContainer(PokemonContainer container){
			this.container = container;
		}

		@Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
			return capability==Caps.WTF;
		}
		@Nullable @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
			return capability==Caps.WTF ? Caps.WTF.cast(this) : null;
		}
	}
}
