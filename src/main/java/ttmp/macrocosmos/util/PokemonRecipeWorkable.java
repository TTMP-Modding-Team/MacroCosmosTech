package ttmp.macrocosmos.util;

public interface PokemonRecipeWorkable{
	/**
	 * @return current progress of machine
	 */
	float getProgressFloat();

	/**
	 * @return progress machine need to complete its stuff
	 */
	float getMaxProgressFloat();

	/**
	 * @return Estimated progress per tick
	 */
	float getEstimatedProgressPerTick();

	/**
	 * @return true is machine is active
	 */
	boolean isActive();
}
