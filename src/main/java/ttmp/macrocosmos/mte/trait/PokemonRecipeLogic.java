package ttmp.macrocosmos.mte.trait;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.GTValues;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import ttmp.macrocosmos.MacroCosmosMod;
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
	public static final IEnergyContainer PRIMITIVE_ENERGY_CONTAINER = new IEnergyContainer(){
		@Override public long acceptEnergyFromNetwork(EnumFacing side, long voltage, long amperage){
			return 0;
		}
		@Override public boolean inputsEnergy(EnumFacing side){
			return false;
		}
		@Override public long changeEnergy(long differenceAmount){
			return 0;
		}
		@Override public long getEnergyStored(){
			return Integer.MAX_VALUE;
		}
		@Override public long getEnergyCapacity(){
			return Integer.MAX_VALUE;
		}
		@Override public long getInputAmperage(){
			return 1L;
		}
		@Override public long getInputVoltage(){
			return GTValues.LV;
		}
		@Override public long getInputPerSec(){
			return Integer.MAX_VALUE;
		}
	};

	private final PokemonContainer pokemonInput;
	@Nullable protected PokeRecipeMetadata recipeMetadata;

	protected double partialProgressTime;
	protected boolean pokemonUpdated;

	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, PokemonContainer pokemonInput){
		this(metaTileEntity, recipeMap, () -> PRIMITIVE_ENERGY_CONTAINER, pokemonInput);
	}
	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer, PokemonContainer pokemonInput){
		super(metaTileEntity, recipeMap, energyContainer);
		this.pokemonInput = pokemonInput;
		if(pokemonInput instanceof PokemonContainer.Notifiable){
			((PokemonContainer.Notifiable)pokemonInput).addListener((index, container) -> markPokemonUpdated(true));
		}
	}

	public void debug(){ // lol
		MacroCosmosMod.LOGGER.info("Metadata: {}", recipeMetadata);
		MacroCosmosMod.LOGGER.info("Fuck: {}| {} / {}", getProgressModifier(), this.partialProgressTime, this.maxProgressTime);
		IItemHandlerModifiable inv = getInputInventory();
		for(int i = 0; i<inv.getSlots(); i++){
			ItemStack stackInSlot = inv.getStackInSlot(i);
			MacroCosmosMod.LOGGER.info(stackInSlot);
		}
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
			if(wtf!=null) wtf.setContainer(pokemonInput);
		}
		return inputInventory;
	}

	protected IItemHandlerModifiable getActualInputInventory(){
		return super.getInputInventory();
	}

	@Override protected void trySearchNewRecipe(){
		super.trySearchNewRecipe();
		markPokemonUpdated(false);
	}

	@Override protected boolean hasNotifiedInputs(){
		return pokemonUpdated||super.hasNotifiedInputs();
	}

	@Override protected boolean canProgressRecipe(){
		return recipeMetadata==null||recipeMetadata.test(pokemonInput);
	}

	@Override protected void updateRecipeProgress(){
		if(progress()){
			//as recipe starts with progress on 1 this has to be > only not => to compensate for it
			if(partialProgressTime>=maxProgressTime+1) completeRecipe();
			if(this.hasNotEnoughEnergy&&getEnergyInputPerSecond()>19L*recipeEUt)
				this.hasNotEnoughEnergy = false;
		}else if(recipeEUt>0){
			//only set hasNotEnoughEnergy if this recipe is consuming recipe
			//generators always have enough energy
			this.hasNotEnoughEnergy = true;
			//if current progress value is greater than 2, decrement it by 2
			if(partialProgressTime>1){
				switch(getRecipeHaltBehavior()){
					case CLEAR_PROGRESS:
						this.partialProgressTime = 1;
						break;
					case DECREMENT_PROGRESS:
						this.partialProgressTime = Math.max(1, partialProgressTime-2);
						break;
				}
				progressTime = Math.max(1, (int)partialProgressTime);
			}
		}
	}

	protected RecipeHaltBehavior getRecipeHaltBehavior(){
		return ConfigHolder.machines.recipeProgressLowEnergy ? RecipeHaltBehavior.CLEAR_PROGRESS : RecipeHaltBehavior.DECREMENT_PROGRESS;
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
		if(recipeMetadata==null) return 1;
		double progress = 0;
		for(int i = 0; i<pokemonInput.size(); i++){
			Pokemon pokemon = pokemonInput.getPokemon(i);
			if(pokemon!=null) progress += recipeMetadata.calculateProgress(pokemon);
		}
		return progress;
	}

	@Override protected void setupRecipe(Recipe recipe){
		super.setupRecipe(recipe);
		this.recipeMetadata = recipe instanceof PokeRecipe ? ((PokeRecipe)recipe).getMetadata() : null;
		this.partialProgressTime = 1;
	}
	@Override protected void completeRecipe(){
		super.completeRecipe();
		this.partialProgressTime = 0;
		this.recipeMetadata = null;
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
		this.recipeMetadata = null;
	}

	public void markPokemonUpdated(boolean pokemonUpdated){
		this.pokemonUpdated = pokemonUpdated;
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = super.serializeNBT();
		tag.setDouble("Progress", partialProgressTime);
		if(recipeMetadata!=null) tag.setTag("recipeMetadata", recipeMetadata.write());
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.partialProgressTime = tag.getDouble("Progress");
		this.recipeMetadata = tag.hasKey("recipeMetadata", Constants.NBT.TAG_COMPOUND) ?
				PokeRecipeMetadata.read(tag.getCompoundTag("recipeMetadata")) : null;
	}

	public enum RecipeHaltBehavior{
		KEEP_PROGRESS,
		DECREMENT_PROGRESS,
		CLEAR_PROGRESS
	}

	/**
	 * wtf?
	 */
	@SuppressWarnings("NullableProblems") public static final class Wtf implements ICapabilityProvider{
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
