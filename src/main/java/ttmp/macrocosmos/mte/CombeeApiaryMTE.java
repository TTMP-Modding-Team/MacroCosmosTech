package ttmp.macrocosmos.mte;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import ttmp.macrocosmos.capability.RangeWrappedPokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonSlotController;
import ttmp.macrocosmos.mte.trait.ApiaryLogic;
import ttmp.macrocosmos.mte.trait.PokemonContainerTrait;
import ttmp.macrocosmos.recipe.ModRecipes;

// TODO egg meter
public class CombeeApiaryMTE extends MetaTileEntity{
	private final PokemonContainerTrait pokemon = new PokemonContainerTrait(this, 10){
		@Override public boolean isValid(Pokemon pokemon, int index){
			if(index==0) return pokemon.getSpecies()==EnumSpecies.Vespiquen;
			if(index>=1&&index<=7) return pokemon.getSpecies()==EnumSpecies.Combee;
			if(index>=8&&index<=10) return pokemon.isEgg();
			return false;
		}
	};

	private final ApiaryLogic apiaryLogic = new ApiaryLogic(this,
			new RangeWrappedPokemonContainer(pokemon, 0, 1),
			new RangeWrappedPokemonContainer(pokemon, 1, 6),
			new RangeWrappedPokemonContainer(pokemon, 7, 3));

	public CombeeApiaryMTE(ResourceLocation metaTileEntityId){
		super(metaTileEntityId);
	}

	@Override public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult){
		if(!playerIn.world.isRemote) apiaryLogic.debug();
		return super.onRightClick(playerIn, hand, facing, hitResult);
	}

	@Override public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder){
		return new CombeeApiaryMTE(metaTileEntityId);
	}

	@Override protected IItemHandlerModifiable createImportItemHandler(){
		return new ItemStackHandler(6);
	}
	@Override protected IItemHandlerModifiable createExportItemHandler(){
		return new ItemStackHandler(6);
	}

	@Override protected ModularUI createUI(EntityPlayer player){
		PokemonSlotController controller = new PokemonSlotController();
		ModularUI.Builder builder = apiaryLogic.getRecipeMap().createUITemplate(apiaryLogic::getProgressPercent, importItems, exportItems, importFluids, exportFluids, 24)
				.widget(new LabelWidget(5, 5, getMetaFullName()))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 24)
				.widget(controller)
				.widget(controller.newContainerSlot(5, 15, pokemon, 0, GuiTextures.SLOT));
		for(int i = 0; i<PlayerPartyStorage.MAX_PARTY; i++)
			builder.widget(controller.newPartySlot(-20, 5+i*18, player, i, GuiTextures.SLOT));

		for(int i = 1; i<7; i++) builder.widget(controller.newContainerSlot(20+i*18, 16, pokemon, i, GuiTextures.SLOT));
		RangeWrappedPokemonContainer wrapped = new RangeWrappedPokemonContainer(pokemon, 7, 3){
			@Override public boolean isValid(Pokemon pokemon, int index){
				return false;
			}
		};
		for(int i = 0; i<3; i++) builder.widget(controller.newContainerSlot(100+i%3*18, 80, wrapped, i, GuiTextures.SLOT));

		return builder.build(getHolder(), player);
	}
}
