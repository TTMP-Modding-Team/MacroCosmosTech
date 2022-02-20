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
import ttmp.macrocosmos.recipe.poke.PokeRecipeMatch;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;
import ttmp.macrocosmos.recipe.poke.PokeRecipeSkillBonus;
import ttmp.macrocosmos.recipe.poke.PokeRecipeWorkType;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.ActivePokemonSkillBonuses;
import ttmp.macrocosmos.util.PokemonRecipeWorkable;
import ttmp.macrocosmos.util.PokemonWorkCache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PokemonRecipeLogic extends RecipeLogicEnergy implements PokemonRecipeWorkable{
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

	protected final ActivePokemonSkillBonuses activeSkillBonuses = new ActivePokemonSkillBonuses();
	protected final PokemonWorkCache workCache = new PokemonWorkCache(this);

	@Nullable private PokeRecipeMetadata recipeMetadata;

	protected float partialProgressTime;
	protected boolean pokemonUpdated;

	protected float overclockPercentage = 1;

	private float progressPerTickCache;

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

	public PokemonContainer getPokemonInput(){
		return pokemonInput;
	}

	public PokeRecipeMetadata getDefaultMetadata(){
		return defaultMetadata;
	}
	@Nullable public final PokeRecipeMetadata getRecipeMetadata(){
		return recipeMetadata;
	}
	protected final void setRecipeMetadata(@Nullable PokeRecipeMetadata recipeMetadata){
		this.recipeMetadata = recipeMetadata;
		this.recipeConditions = null;
		this.recipeProgress = null;
		this.recipeWorkType = null;
		this.recipeHpToWorkConversionRate = null;
		this.recipeSkillBonus = null;
	}

	@Nullable private List<PokemonCondition> recipeConditions;
	@Nullable private PokemonValue recipeProgress;
	@Nullable private PokeRecipeWorkType recipeWorkType;
	@Nullable private PokemonValue recipeHpToWorkConversionRate;
	@Nullable private Set<PokeRecipeSkillBonus> recipeSkillBonus;

	public List<PokemonCondition> getRecipeConditions(){
		if(recipeConditions==null) recipeConditions = defaultMetadata.getConditions(recipeMetadata);
		return recipeConditions;
	}
	public PokemonValue getRecipeProgress(){
		if(recipeProgress==null) recipeProgress = defaultMetadata.getProgress(recipeMetadata);
		return recipeProgress;
	}
	public PokeRecipeWorkType getRecipeWorkType(){
		if(recipeWorkType==null) recipeWorkType = defaultMetadata.getWorkType(recipeMetadata);
		return recipeWorkType;
	}
	public PokemonValue getRecipeHpToWorkConversionRate(){
		if(recipeHpToWorkConversionRate==null) recipeHpToWorkConversionRate = defaultMetadata.getHpToWorkConversionRate(recipeMetadata);
		return recipeHpToWorkConversionRate;
	}
	public Set<PokeRecipeSkillBonus> getRecipeSkillBonus(){
		if(recipeSkillBonus==null) recipeSkillBonus = defaultMetadata.getSkillBonus(recipeMetadata);
		return recipeSkillBonus;
	}

	public ActivePokemonSkillBonuses getActiveSkillBonuses(){
		return activeSkillBonuses;
	}

	public float getOverclockPercentage(){
		return overclockPercentage;
	}

	@Override public String getName(){
		return "pokemon_recipe_logic";
	}

	@Nullable private ItemStack ingredientWrapperItem;
	@Nullable private IItemHandlerModifiable inputInventory;

	@Override protected IItemHandlerModifiable getInputInventory(){
		if(inputInventory==null){
			ingredientWrapperItem = new ItemStack(ModItems.POKE_RECIPE_INGREDIENT);
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

	@Override public float getProgressFloat(){
		return partialProgressTime;
	}
	@Override public float getMaxProgressFloat(){
		return getMaxProgress();
	}
	@Override public float getEstimatedProgressPerTick(){
		return getMaxProgressFloat()<=0 ? 0 : progressPerTickCache;
	}

	@Override public void update(){
		activeSkillBonuses.tickActiveSkillBonus(pokemonInput);
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
		if(!canRecipeProgress||
				!PokeRecipeMatch.test(getRecipeConditions(), this.pokemonInput)||
				!drawEnergy(recipeEUt, true)) return false;

		float progressModifier = workToProgress();
		this.progressPerTickCache = Math.max(0, progressModifier);
		if(progressModifier<=0) return false;

		drawEnergy(recipeEUt, false);
		this.partialProgressTime += progressModifier;
		this.progressTime = Math.max(1, (int)partialProgressTime);
		return true;
	}

	protected float workToProgress(){
		float progress = 0;
		for(int i = 0; i<pokemonInput.size(); i++){
			Pokemon pokemon = pokemonInput.getPokemon(i);
			if(pokemon!=null&&PokeRecipeMatch.matchesAny(pokemon, getRecipeConditions()))
				progress += this.workCache.consumeWork(i, pokemon, getRecipeProgress().getValue(pokemon)*overclockPercentage)/overclockPercentage
						*this.activeSkillBonuses.getSkillBonus(i, pokemon, getRecipeSkillBonus());
		}
		return progress;
	}

	@Override protected void setupRecipe(Recipe recipe){
		super.setupRecipe(recipe);
		setRecipeMetadata(recipe instanceof PokeRecipe ? ((PokeRecipe)recipe).getMetadata() : null);
		this.partialProgressTime = 1;
		this.overclockPercentage = (float)recipe.getDuration()/this.getMaxProgress();
	}
	@Override protected void completeRecipe(){
		super.completeRecipe();
		this.partialProgressTime = 0;
		setRecipeMetadata(null);
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
		setRecipeMetadata(null);
	}

	public void markPokemonUpdated(boolean pokemonUpdated){
		this.pokemonUpdated = pokemonUpdated;
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = super.serializeNBT();
		tag.setFloat("Progress", partialProgressTime);
		if(recipeMetadata!=null) tag.setByteArray("recipeMetadata", recipeMetadata.writeToByteArray());
		tag.setTag("SkillBonuses", this.activeSkillBonuses.write());
		tag.setTag("Works", workCache.write());
		tag.setFloat("OverclockPercentage", overclockPercentage);
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.partialProgressTime = tag.getFloat("Progress");
		setRecipeMetadata(tag.hasKey("recipeMetadata", Constants.NBT.TAG_BYTE_ARRAY) ?
				PokeRecipeMetadata.read(tag.getByteArray("recipeMetadata")) : null);
		this.activeSkillBonuses.read(tag.getTagList("SkillBonuses", Constants.NBT.TAG_COMPOUND));
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
