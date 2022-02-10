package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum WorkType{
	ATTACK(StatsType.Attack),
	DEFENCE(StatsType.Defence),
	SPECIAL_ATTACK(StatsType.SpecialAttack),
	SPECIAL_DEFENCE(StatsType.SpecialDefence),
	SPEED(StatsType.Speed);

	public final StatsType pokemonStat;

	WorkType(StatsType pokemonStat){
		this.pokemonStat = pokemonStat;
	}
}
