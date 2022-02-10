package ttmp.macrocosmos.mte.trait;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.metatileentity.MetaTileEntity;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.recipe.ModRecipes;

import javax.annotation.Nullable;

public class ApiaryLogic extends PokemonRecipeLogic{
	private final PokemonContainer vespiquen;
	private final PokemonContainer combee;
	private final PokemonContainer eggs;

	@Nullable private CombeeType queenTypeCache;
	private long eggProgress;

	public ApiaryLogic(MetaTileEntity metaTileEntity, PokemonContainer vespiquen, PokemonContainer combee, PokemonContainer eggs){
		super(metaTileEntity, ModRecipes.COMBEE, () -> IEnergyContainer.DEFAULT, () -> vespiquen);
		this.vespiquen = vespiquen;
		this.combee = combee;
		this.eggs = eggs;
	}

	@Nullable private Pokemon queen(){
		return vespiquen.getPokemon(0);
	}

	@Override public void update(){
		updateQueenType();
		super.update();
	}

	private void updateQueenType(){
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
		for(int i = 0; i<combee.size(); i++){
			Pokemon p = combee.getPokemon(i);
			if(p!=null&&!p.isEgg()&&p.getSpecies()==EnumSpecies.Combee){
				return true;
			}
		}
		return false;
	}

	@Override protected void updateRecipeProgress(){
		if(this.canRecipeProgress&&this.drawEnergy(this.recipeEUt, true)){
			this.drawEnergy(this.recipeEUt, false);
			if(++this.progressTime>this.maxProgressTime)
				this.completeRecipe();

			if(this.hasNotEnoughEnergy&&this.getEnergyInputPerSecond()>19L*(long)this.recipeEUt)
				this.hasNotEnoughEnergy = false;
		}else if(this.recipeEUt>0) this.hasNotEnoughEnergy = true;
	}

	@Override protected void completeRecipe(){
		super.completeRecipe();
	}

	@Override protected long getEnergyInputPerSecond(){
		return Integer.MAX_VALUE;
	}
	@Override protected long getEnergyStored(){
		return Integer.MAX_VALUE;
	}
	@Override protected long getEnergyCapacity(){
		return Integer.MAX_VALUE;
	}
	@Override protected boolean drawEnergy(int recipeEUt, boolean simulate){
		return true;
	}

	@Override protected long getMaxVoltage(){
		return 1;
	}
}
