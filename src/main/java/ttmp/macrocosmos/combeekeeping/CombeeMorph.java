package ttmp.macrocosmos.combeekeeping;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

public final class CombeeMorph{
	public static Builder eggOf(PokemonCondition queen, PokemonCondition worker){
		return new Builder(queen, worker, true);
	}
	public static Builder eggOfAny(PokemonCondition condition1, PokemonCondition condition2){
		return new Builder(condition1, condition2, false);
	}

	// TODO more challenge options? idk maybe?
	private final PokemonCondition queen;
	private final PokemonCondition worker;
	private final boolean checkForReverseCase;
	private final double chance;
	private final CombeeType morphType;

	public CombeeMorph(PokemonCondition queen, PokemonCondition worker, boolean checkForReverseCase, double chance, CombeeType morphType){
		this.queen = queen;
		this.worker = worker;
		this.checkForReverseCase = checkForReverseCase;
		this.chance = chance;
		this.morphType = morphType;
	}

	public boolean test(Pokemon queen, Pokemon worker){
		return this.queen.test(queen)&&this.worker.test(worker)||
				(checkForReverseCase&&this.worker.test(queen)&&this.queen.test(worker));
	}

	public PokemonCondition getQueenCondition(){
		return queen;
	}
	public PokemonCondition getWorkerCondition(){
		return worker;
	}
	public boolean checkForReverseCase(){
		return checkForReverseCase;
	}
	public double getChance(){
		return chance;
	}
	public CombeeType getMorphType(){
		return morphType;
	}

	public static final class Builder{
		private final PokemonCondition queen;
		private final PokemonCondition worker;
		private final boolean checkForReverseCase;
		private double chance = 1;

		public Builder(PokemonCondition queen, PokemonCondition worker, boolean checkForReverseCase){
			this.queen = queen;
			this.worker = worker;
			this.checkForReverseCase = checkForReverseCase;
		}

		public Builder chance(double chance){
			this.chance = chance;
			return this;
		}

		public CombeeMorph into(CombeeType result){
			return new CombeeMorph(queen, worker, checkForReverseCase, chance, result);
		}
	}
}
