package ttmp.macrocosmos.mte.trait;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.BreedLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import ttmp.macrocosmos.MacroCosmosMod;
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

public class ApiaryLogic extends PokemonRecipeLogic{
	protected static final Random RNG = new Random();

	private final PokemonContainer queen;
	private final PokemonContainer workers;
	private final PokemonContainer eggs;

	@Nullable private CombeeType queenTypeCache;
	private double eggProgress;

	public ApiaryLogic(MetaTileEntity metaTileEntity, PokemonContainer queen, PokemonContainer workers, PokemonContainer eggs){
		super(metaTileEntity, ModRecipes.COMBEE, () -> queen);
		this.queen = queen;
		this.workers = workers;
		this.eggs = eggs;
	}

	@Override public void debug(){
		super.debug();
		MacroCosmosMod.LOGGER.info("{} {}", queenTypeCache, eggProgress);
	}

	@Nullable private Pokemon queen(){
		return queen.getPokemon(0);
	}

	@Override public void update(){
		updateQueenType();
		super.update();
		if(queenTypeCache!=null){
			while(eggProgress>=queenTypeCache.getEggProductionRate()){
				if(produceEgg())
					eggProgress -= queenTypeCache.getEggProductionRate();
			}
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
		CombeeType qt = CombeeType.getCombeeType(queen);
		CombeeType wt = CombeeType.getCombeeType(worker);
		// TODO do morph shit
		Pokemon egg = BreedLogic.makeEgg(queen, worker);
		if(egg!=null) CombeeType.setCombeeType(egg, RNG.nextBoolean() ? qt : wt);
		return egg;
	}

	protected void updateQueenType(){
		Pokemon queen = queen();
		if(queen==null){
			this.queenTypeCache = null;
			cancelRecipe();
			return;
		}
		CombeeType type = CombeeType.getCombeeType(queen);
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

	@Override protected boolean progress(){
		if(super.progress()){
			if(PokemonContainerUtil.hasEmptySlot(eggs))
				this.eggProgress += getProgressModifier();
			else this.eggProgress = 0;
			return true;
		}else return false;
	}

	@Override protected double getProgressModifier(){
		if(recipeMetadata==null) return 0;
		if(queenTypeCache==null) return 0;
		Pokemon queen = queen();
		if(queen==null||queen.getHealth()<=0) return 0;

		double progress = recipeMetadata.calculateProgress(queen);
		for(int i = 0; i<this.workers.size(); i++){
			Pokemon worker = this.workers.getPokemon(i);
			if(worker!=null&&isValidWorker(worker)&&queen.getHealth()<=0)
				progress += recipeMetadata.calculateProgress(worker)/(CombeeType.getCombeeType(worker)==queenTypeCache ? 2 : 3);
		}
		return progress;
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = super.serializeNBT();
		if(queenTypeCache!=null) tag.setString("QueenType", queenTypeCache.getName());
		if(eggProgress>0) tag.setDouble("Egg", eggProgress);
		return tag;
	}
	@Override public void deserializeNBT(@Nonnull NBTTagCompound tag){
		super.deserializeNBT(tag);
		this.queenTypeCache = tag.hasKey("QueenType", Constants.NBT.TAG_STRING) ? CombeeTypes.get(tag.getString("QueenType")) : null;
		this.eggProgress = tag.getDouble("Egg");
	}

	public static boolean isValidQueen(Pokemon pokemon){
		return !pokemon.isEgg()&&pokemon.getSpecies()==EnumSpecies.Vespiquen;
	}
	public static boolean isValidWorker(Pokemon pokemon){
		return !pokemon.isEgg()&&pokemon.getSpecies()==EnumSpecies.Combee;
	}
}
