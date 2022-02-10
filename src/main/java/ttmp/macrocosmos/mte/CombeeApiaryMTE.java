package ttmp.macrocosmos.mte;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import ttmp.macrocosmos.capability.RangeWrappedPokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonSlotController;
import ttmp.macrocosmos.mte.trait.ApiaryLogic;
import ttmp.macrocosmos.mte.trait.PokemonContainerTrait;

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
		ModularUI.Builder builder = ModularUI.extendedBuilder()
				.label(5, 5, getMetaFullName());

		PokemonSlotController slotController = new PokemonSlotController();
		builder.widget(slotController)
				.widget(slotController.newContainerSlot(5, 16, pokemon, 0, GuiTextures.SLOT));

		for(int i = 0; i<PlayerPartyStorage.MAX_PARTY; i++) builder.widget(slotController.newPartySlot(-7-18, 5+i*18, player, i, GuiTextures.SLOT));
		for(int i = 1; i<7; i++) builder.widget(slotController.newContainerSlot(20+i*18, 16, pokemon, i, GuiTextures.SLOT));
		for(int i = 0; i<6; i++) builder.slot(importItems, i, i%3*18, 16+18+10+i/3*18, GuiTextures.SLOT);
		for(int i = 0; i<6; i++) builder.slot(exportItems, i, 100+i%3*18, 16+18+10+i/3*18, GuiTextures.SLOT);
		RangeWrappedPokemonContainer wrapped = new RangeWrappedPokemonContainer(pokemon, 7, 3){
			@Override public boolean isValid(Pokemon pokemon, int index){
				return false;
			}
		};
		for(int i = 0; i<3; i++) builder.widget(slotController.newContainerSlot(100+i%3*18, 16+18+10+18*2+5, wrapped, i, GuiTextures.SLOT));

		return builder.bindPlayerInventory(player.inventory, 216-72).build(getHolder(), player);
	}
}
