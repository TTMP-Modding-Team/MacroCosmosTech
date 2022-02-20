package ttmp.macrocosmos.item;

import com.mojang.realmsclient.util.Pair;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.mte.trait.ApiaryLogic;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMatch;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.Join;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class FuckingDebuggerItem extends Item{
	@Override public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand){
		if(!world.isRemote){
			TileEntity te = world.getTileEntity(pos);
			if(te!=null){
				List<Pair<String, Object>> list = collectDebugInfo(te);
				if(!list.isEmpty()){
					player.sendMessage(new TextComponentString("============================================="));
					for(Pair<String, Object> pair : list){
						String s = pair.first();
						Object o = pair.second();
						player.sendMessage(new TextComponentString(s+": "+(
								o==Boolean.TRUE ? TextFormatting.GREEN :
										o==Boolean.FALSE ? TextFormatting.RED :
												o==null ? TextFormatting.GRAY :
														TextFormatting.YELLOW)+o));
					}
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	private static List<Pair<String, Object>> collectDebugInfo(TileEntity te){
		List<Pair<String, Object>> list = new ArrayList<>();
		BiConsumer<String, Object> c = (s, o) -> list.add(Pair.of(s, o));

		AbstractRecipeLogic recipeLogic = te.getCapability(GregtechTileCapabilities.CAPABILITY_RECIPE_LOGIC, null);
		if(recipeLogic instanceof PokemonRecipeLogic){
			debugPokemonRecipeLogic((PokemonRecipeLogic)recipeLogic, c);
			if(recipeLogic instanceof ApiaryLogic) debugApiary(((ApiaryLogic)recipeLogic), c);
		}
		return list;
	}

	private static void debugPokemonRecipeLogic(PokemonRecipeLogic prl, BiConsumer<String, Object> consumer){
		consumer.accept("RecipeMap", prl.getRecipeMap());
		if(prl.getMaxProgress()>0){
			consumer.accept("Conditions", Join.join(prl.getRecipeConditions()));
			consumer.accept("Progress", prl.getRecipeProgress());
			consumer.accept("HP => Work Conversion Ratio", prl.getRecipeHpToWorkConversionRate());
			consumer.accept("Test Result", PokeRecipeMatch.test(prl.getRecipeConditions(), prl.getPokemonInput()));

			consumer.accept("Current Recipe Meta", prl.getRecipeMetadata());
			consumer.accept("Overclock", PokemonValue.decimalFormat.format(prl.getOverclockPercentage()*100)+"%");
			consumer.accept("Progress", prl.getProgressFloat()+" / "+prl.getMaxProgressFloat());
			consumer.accept("Progress (Int)", prl.getProgress()+" / "+prl.getMaxProgress());
			consumer.accept("Est. progress/t", prl.getEstimatedProgressPerTick());
			if(prl.getMaxProgressFloat()>0)
				consumer.accept("Est. recipe completion", "in "+
						PokemonValue.decimalFormat.format(prl.getMaxProgressFloat()+1-prl.getProgressFloat()/prl.getEstimatedProgressPerTick()/20)+"s");
		}
	}
	private static void debugApiary(ApiaryLogic al, BiConsumer<String, Object> consumer){
		Pokemon queen = al.queen();
		consumer.accept("Queen", queen);
		if(queen!=null){
			consumer.accept("Queen Type", CombeeTypes.getCombeeType(queen));
			consumer.accept("Egg", al.getEggProgressPercentage());
		}
	}
}
