package ttmp.macrocosmos.mte;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.metatileentity.IDataInfoProvider;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonSlotController;
import ttmp.macrocosmos.mte.trait.PokemonContainerTrait;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;
import ttmp.macrocosmos.recipe.ModRecipes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static gregtech.api.util.GTUtility.formatNumbers;

public class PokeRecipeTestMTE extends MetaTileEntity implements IDataInfoProvider{
	private final PokemonContainer pokemon = new PokemonContainerTrait(this, 1);
	private final PokemonRecipeLogic recipeLogic = new PokemonRecipeLogic(this, ModRecipes.TEST, () -> pokemon);

	public PokeRecipeTestMTE(ResourceLocation metaTileEntityId){
		super(metaTileEntityId);
	}

	@Override public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult){
		if(!playerIn.world.isRemote){
			recipeLogic.debug();
		}
		return super.onRightClick(playerIn, hand, facing, hitResult);
	}

	@Override public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder){
		return new PokeRecipeTestMTE(metaTileEntityId);
	}
	@Override protected ModularUI createUI(EntityPlayer player){
		PokemonSlotController controller = new PokemonSlotController();

			ModularUI.Builder builder = recipeLogic.getRecipeMap().createUITemplate(recipeLogic::getProgressPercent, importItems, exportItems, importFluids, exportFluids, 0)
				.widget(new LabelWidget(5, 5, getMetaFullName()))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 0)
				.widget(controller)
				.widget(controller.newContainerSlot(5, 15, pokemon, 0, GuiTextures.SLOT));
		for(int i = 0; i<PlayerPartyStorage.MAX_PARTY; i++)
			builder.widget(controller.newPartySlot(-20, 5+i*18, player, i, GuiTextures.SLOT));
		return builder.build(getHolder(), player);
	}

	@Override protected IItemHandlerModifiable createImportItemHandler(){
		return new ItemStackHandler(1);
	}
	@Override protected IItemHandlerModifiable createExportItemHandler(){
		return new ItemStackHandler(1);
	}

	@Nonnull @Override public List<ITextComponent> getDataInfo(){
		List<ITextComponent> list = new ArrayList<>();

		if(recipeLogic!=null){
			list.add(new TextComponentTranslation("behavior.tricorder.workable_progress",
					new TextComponentTranslation(formatNumbers(recipeLogic.getProgress()/20)).setStyle(new Style().setColor(TextFormatting.GREEN)),
					new TextComponentTranslation(formatNumbers(recipeLogic.getMaxProgress()/20)).setStyle(new Style().setColor(TextFormatting.YELLOW))
			));

			if(recipeLogic.getRecipeEUt()>0){
				list.add(new TextComponentTranslation("behavior.tricorder.workable_consumption",
						new TextComponentTranslation(formatNumbers(recipeLogic.getRecipeEUt())).setStyle(new Style().setColor(TextFormatting.RED)),
						new TextComponentTranslation(formatNumbers(recipeLogic.getRecipeEUt()==0 ? 0 : 1)).setStyle(new Style().setColor(TextFormatting.RED))
				));
			}else{
				list.add(new TextComponentTranslation("behavior.tricorder.workable_production",
						new TextComponentTranslation(formatNumbers(recipeLogic.getRecipeEUt()*-1)).setStyle(new Style().setColor(TextFormatting.RED)),
						new TextComponentTranslation(formatNumbers(recipeLogic.getRecipeEUt()==0 ? 0 : 1)).setStyle(new Style().setColor(TextFormatting.RED))
				));
			}
		}

		return list;
	}
}
