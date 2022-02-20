package ttmp.macrocosmos.mte;

import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonSlotController;
import ttmp.macrocosmos.mte.trait.PokemonContainerTrait;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMap;

public class PokeRecipeMTE extends MetaTileEntity{
	private final PokeRecipeMap<?> recipeMap;
	private final PokemonContainer pokemon;
	private final PokemonRecipeLogic recipeLogic;

	public PokeRecipeMTE(ResourceLocation metaTileEntityId, PokeRecipeMap<?> recipeMap, int pokemons){
		super(metaTileEntityId);
		this.recipeMap = recipeMap;
		this.pokemon = new PokemonContainerTrait(this, pokemons);
		this.recipeLogic = new PokemonRecipeLogic(this, recipeMap, this.pokemon);
	}

	@Override public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder){
		return new PokeRecipeMTE(metaTileEntityId, recipeMap, pokemon.size());
	}

	@Override protected ModularUI createUI(EntityPlayer player){
		PokemonSlotController controller = new PokemonSlotController();

		ModularUI.Builder builder = recipeLogic.getRecipeMap().createUITemplate(recipeLogic::getProgressPercent, importItems, exportItems, importFluids, exportFluids, 0)
				.widget(new LabelWidget(5, 5, getMetaFullName()))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 0)
				.widget(controller);

		for(int i = 0; i<pokemon.size(); i++)
			builder.widget(controller.newContainerSlot(5, 15+18*i, pokemon, i, GuiTextures.SLOT));

		for(int i = 0; i<PlayerPartyStorage.MAX_PARTY; i++)
			builder.widget(controller.newPartySlot(-20, 5+i*18, player, i, GuiTextures.SLOT));

		return builder.build(getHolder(), player);
	}

	@Override protected IItemHandlerModifiable createImportItemHandler(){
		return new NotifiableItemStackHandler(1, this, false);
	}
	@Override protected IItemHandlerModifiable createExportItemHandler(){
		return new NotifiableItemStackHandler(1, this, true);
	}
}
