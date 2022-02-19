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
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.capability.EmptyPokemonContainer;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.item.ModItems;
import ttmp.macrocosmos.recipe.poke.PokeRecipe;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMap;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;
import ttmp.macrocosmos.recipe.poke.PokeRecipeSkillBonus;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.ActivePokemonSkillBonuses;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMatch;
import ttmp.macrocosmos.util.PokemonWorkCache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
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
	protected final PokeRecipeMetadata defaultMetadata;

	protected final ActivePokemonSkillBonuses skillBonuses = new ActivePokemonSkillBonuses();
	protected final PokemonWorkCache workCache = new PokemonWorkCache(this);

	@Nullable protected PokeRecipeMetadata recipeMetadata;

	protected float partialProgressTime;
	protected boolean pokemonUpdated;

	protected float overclockPercentage = 1;

	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, PokeRecipeMap<?> recipeMap, PokemonContainer pokemonInput){
		this(metaTileEntity, recipeMap, () -> PRIMITIVE_ENERGY_CONTAINER, pokemonInput);
	}
	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, PokeRecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer, PokemonContainer pokemonInput){
		this(metaTileEntity, recipeMap, energyContainer, pokemonInput, recipeMap.getDefaultMetadata());
	}

	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, PokemonContainer pokemonInput, @Nullable PokeRecipeMetadata defaultMetadata){
		this(metaTileEntity, recipeMap, () -> PRIMITIVE_ENERGY_CONTAINER, pokemonInput, defaultMetadata);
	}
	public PokemonRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer, PokemonContainer pokemonInput, PokeRecipeMetadata defaultMetadata){
		super(metaTileEntity, recipeMap, energyContainer);
		this.pokemonInput = pokemonInput;
		this.defaultMetadata = defaultMetadata!=null ? defaultMetadata : PokeRecipeMetadata.defaultMetadata();
		if(pokemonInput instanceof PokemonContainer.Notifiable){
			((PokemonContainer.Notifiable)pokemonInput).addListener((index, container) -> markPokemonUpdated(true));
		}
	}

	public PokeRecipeMetadata getDefaultMetadata(){
		return defaultMetadata;
	}
	@Nullable public PokeRecipeMetadata getRecipeMetadata(){
		return recipeMetadata;
	}

	@Override public String getName(){
		return "pokemon_recipe_logic";
	}

	@Nullable private ItemStack ingredientWrapperItem;
	@Nullable private IItemHandlerModifiable inputInventory;

	@Override protected IItemHandlerModifiable getInputInventory(){
		if(inputInventory==null){
			ingredientWrapperItem = new ItemStack(ModItems.SHH);
			inputInventory = new CombinedInvWrapper(new SingleItemStackHandler(ingredientWrapperItem), getActualInputInventory());
		}
		if(ingredientWrapperItem!=null){
			ingredientWrapperItem.setCount(1);
			PokeRecipeIngredientCap cap = ingredientWrapperItem.getCapability(Caps.POKE_RECIPE_INGREDIENT, null);
			if(cap!=null) cap.setContainer(pokemonInput);
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
		return PokeRecipeMatch.test(this.defaultMetadata.getConditions(this.recipeMetadata), this.pokemonInput);
	}

	@Override public void update(){
		skillBonuses.tickActiveSkillBonus(pokemonInput);
		super.update();
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

		float progressModifier = workToProgress();
		if(progressModifier>0){
			partialProgressTime += progressModifier;
			progressTime = Math.max(1, (int)partialProgressTime);
		}
		return true;
	}

	protected float workToProgress(){
		PokemonValue progressValue = defaultMetadata.getProgress(recipeMetadata);
		Set<PokeRecipeSkillBonus> skillBonus = defaultMetadata.getSkillBonus(recipeMetadata);
		float progress = 0;
		for(int i = 0; i<pokemonInput.size(); i++){
			Pokemon pokemon = pokemonInput.getPokemon(i);
			if(pokemon!=null)
				progress += this.workCache.consumeWork(i, pokemon, progressValue.getValue(pokemon))
						*this.skillBonuses.getSkillBonus(i, pokemon, skillBonus);
		}
		return progress;
	}

	@Override protected void setupRecipe(Recipe recipe){
		super.setupRecipe(recipe);
		this.recipeMetadata = recipe instanceof PokeRecipe ? ((PokeRecipe)recipe).getMetadata() : null;
		this.partialProgressTime = 1;
		this.overclockPercentage = (float)recipe.getDuration()/this.getMaxProgress();
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
		tag.setFloat("Progress", partialProgressTime);
		if(recipeMetadata!=null) tag.setByteArray("recipeMetadata", recipeMetadata.writeToByteArray());
		tag.setTag("SkillBonuses", this.skillBonuses.write());
		tag.setTag("Works", workCache.write());
		tag.setFloat("OverclockPercentage", overclockPercentage);
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.partialProgressTime = tag.getFloat("Progress");
		this.recipeMetadata = tag.hasKey("recipeMetadata", Constants.NBT.TAG_BYTE_ARRAY) ?
				PokeRecipeMetadata.read(tag.getByteArray("recipeMetadata")) : null;
		this.skillBonuses.read(tag.getTagList("SkillBonuses", Constants.NBT.TAG_COMPOUND));
		this.workCache.read(tag.getTagList("Works", Constants.NBT.TAG_COMPOUND));
		this.overclockPercentage = tag.getFloat("OverclockPercentage");
	}

	public enum RecipeHaltBehavior{
		KEEP_PROGRESS,
		DECREMENT_PROGRESS,
		CLEAR_PROGRESS
	}

	/**
	 * wtf?
	 */
	@SuppressWarnings("NullableProblems") public static final class PokeRecipeIngredientCap implements ICapabilityProvider{
		private PokemonContainer container = EmptyPokemonContainer.EMPTY;

		public PokemonContainer getContainer(){
			return container;
		}
		public void setContainer(PokemonContainer container){
			this.container = container;
		}

		@Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
			return capability==Caps.POKE_RECIPE_INGREDIENT;
		}
		@Nullable @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
			return capability==Caps.POKE_RECIPE_INGREDIENT ? Caps.POKE_RECIPE_INGREDIENT.cast(this) : null;
		}
	}
}
