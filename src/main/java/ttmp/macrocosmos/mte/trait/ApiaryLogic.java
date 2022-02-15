package ttmp.macrocosmos.mte.trait;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.BreedLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.recipe.ModRecipes;
import ttmp.macrocosmos.util.PokemonContainerUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class ApiaryLogic extends PokemonRecipeLogic{
	protected static final Random RNG = new Random();

	private final PokemonContainer queen;
	private final PokemonContainer workers;
	private final PokemonContainer eggs;

	@Nullable private CombeeType queenTypeCache;

	public ApiaryLogic(MetaTileEntity metaTileEntity, PokemonContainer queen, PokemonContainer workers, PokemonContainer eggs){
		super(metaTileEntity, ModRecipes.COMBEE, queen);
		this.queen = queen;
		this.workers = workers;
		this.eggs = eggs;
	}

	@Nullable private Pokemon queen(){
		return queen.getPokemon(0);
	}

	@Override public void update(){
		updateQueenType();
		super.update();
		Pokemon queen = queen();
		if(queen!=null){
			CombeeType t = CombeeTypes.getCombeeType(queen);
			double eggProgress = getEggProgress(queen);
			boolean sfaewf = false;
			while(eggProgress>=t.getMaxEggProduction()){
				if(produceEgg()){
					eggProgress -= t.getMaxEggProduction();
					sfaewf = true;
				}
			}
			if(sfaewf) setEggProgress(queen, eggProgress);
		}
	}

	protected boolean produceEgg(){
		int index = PokemonContainerUtil.firstEmptySlot(eggs);
		Pokemon egg = makeEgg();
		return egg!=null&&eggs.setPokemon(index, egg, (UUID)null).runIfSuccess();
	}

	@Nullable protected Pokemon makeEgg(){
		Pokemon queen = queen();
		if(queen==null||!isValidQueen(queen)) return null;
		List<Pokemon> validMaleWorkers = new ArrayList<>();
		for(int i = 0; i<workers.size(); i++){
			Pokemon worker = workers.getPokemon(i);
			if(worker!=null&&isValidWorker(worker)&&worker.getHealth()>0&&worker.getGender()==Gender.Male)
				validMaleWorkers.add(worker);
		}
		return validMaleWorkers.isEmpty() ? null : makeEgg(queen, validMaleWorkers.get(RNG.nextInt(validMaleWorkers.size())));
	}

	@Nullable protected Pokemon makeEgg(Pokemon queen, Pokemon worker){
		Pokemon egg = BreedLogic.makeEgg(queen, worker);
		if(egg!=null) CombeeTypes.setCombeeType(egg, CombeeTypes.generateType(queen, worker, RNG));
		return egg;
	}

	protected void updateQueenType(){
		Pokemon queen = queen();
		if(queen==null){
			this.queenTypeCache = null;
			cancelRecipe();
			return;
		}
		CombeeType type = CombeeTypes.getCombeeType(queen);
		if(queenTypeCache!=type){
			this.queenTypeCache = type;
			cancelRecipe();
		}
	}

	@Override protected boolean canProgressRecipe(){
		if(!super.canProgressRecipe()) return false;
		Pokemon queen = queen();
		if(queen!=null&&isValidQueen(queen)&&queen.getHealth()>0){
			for(int i = 0; i<workers.size(); i++){
				Pokemon worker = workers.getPokemon(i);
				if(worker!=null&&isValidWorker(worker)&&worker.getHealth()>0)
					return true;
			}
		}
		return false;
	}

	@Override protected RecipeHaltBehavior getRecipeHaltBehavior(){
		return RecipeHaltBehavior.KEEP_PROGRESS;
	}

	@Override protected boolean progress(){
		if(super.progress()){
			Pokemon queen = queen();
			if(queen!=null&&PokemonContainerUtil.hasEmptySlot(eggs))
				setEggProgress(queen, getEggProgress(queen)+getProgressModifier());
			return true;
		}else return false;
	}

	@Override protected float getProgressModifier(){
		Pokemon queen = queen();
		if(queen==null||queen.getHealth()<=0) return 0;

		float progress = pokeRecipeMap.getProgress(queen, recipeMetadata);
		for(int i = 0; i<this.workers.size(); i++){
			Pokemon worker = this.workers.getPokemon(i);
			if(worker!=null&&isValidWorker(worker)&&queen.getHealth()<=0)
				progress += pokeRecipeMap.getProgress(worker, recipeMetadata)/
						(CombeeTypes.getCombeeType(worker)==CombeeTypes.getCombeeType(queen) ? 2 : 3);
		}
		return progress;
	}

	public double getEggProgressPercentage(){
		Pokemon queen = queen();
		if(queen==null) return 0;
		CombeeType type = CombeeTypes.getCombeeType(queen);
		return getEggProgress(queen)/type.getMaxEggProduction();
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = super.serializeNBT();
		if(queenTypeCache!=null) tag.setString("QueenType", queenTypeCache.getName());
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.queenTypeCache = tag.hasKey("QueenType", Constants.NBT.TAG_STRING) ? CombeeTypes.withName(tag.getString("QueenType")) : null;
	}

	public static boolean isValidQueen(Pokemon pokemon){
		return !pokemon.isEgg()&&pokemon.getSpecies()==EnumSpecies.Vespiquen;
	}
	public static boolean isValidWorker(Pokemon pokemon){
		return !pokemon.isEgg()&&pokemon.getSpecies()==EnumSpecies.Combee;
	}

	private static final String EGG_PROGRESS_KEY = MODID+".egg";

	public static double getEggProgress(Pokemon pokemon){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		return persistentData.getDouble(EGG_PROGRESS_KEY);
	}
	public static void setEggProgress(Pokemon pokemon, double progress){
		NBTTagCompound persistentData = pokemon.getPersistentData();
		persistentData.setDouble(EGG_PROGRESS_KEY, progress);
	}
}
