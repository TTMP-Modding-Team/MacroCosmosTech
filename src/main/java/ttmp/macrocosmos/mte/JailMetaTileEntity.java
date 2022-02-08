package ttmp.macrocosmos.mte;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ttmp.macrocosmos.gui.widget.PokemonSlotController;
import ttmp.macrocosmos.mte.trait.PokemonContainerTrait;

import java.util.UUID;

public class JailMetaTileEntity extends MetaTileEntity{
	private final PokemonContainerTrait pokemon;

	public JailMetaTileEntity(ResourceLocation metaTileEntityId){
		super(metaTileEntityId);
		this.pokemon = new PokemonContainerTrait(this, 1);
	}

	@Override public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder){
		return new JailMetaTileEntity(this.metaTileEntityId);
	}
	@Override protected ModularUI createUI(EntityPlayer player){
		PokemonSlotController slotController = new PokemonSlotController();

		ModularUI.Builder builder = ModularUI.defaultBuilder()
				.label(5, 5, this.getMetaFullName())
				.widget(slotController)
				.widget(slotController.newContainerSlot(5+18, 5+12+5, pokemon, 0, GuiTextures.SLOT));

		for(int i = 0; i<PlayerPartyStorage.MAX_PARTY; i++)
			builder.widget(slotController.newPartySlot(5+18+5+i*18, 5+12+5+18+5, player, i, GuiTextures.SLOT));

		return builder.build(getHolder(), player);
	}

	@Override public void onRemoval(){
		for(int i = 0; i<pokemon.size(); i++){
			Pokemon pokemon = this.pokemon.getPokemon(i);
			UUID uuid = this.pokemon.getOwnerId(i);
			if(pokemon!=null&&uuid!=null)
				Pixelmon.storageManager.getPCForPlayer(uuid).add(pokemon);
		}
	}
}
