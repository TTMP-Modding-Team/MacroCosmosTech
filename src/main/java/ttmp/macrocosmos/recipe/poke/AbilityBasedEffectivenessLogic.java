package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumType;

import java.util.List;

public enum AbilityBasedEffectivenessLogic implements EffectivenessLogic{
	DEFAULT,
	FILTER{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			float effectiveness = super.getEffectiveness(types, attackType);
			return effectiveness>=2 ? effectiveness*.75f : effectiveness;
		}
	},
	FLUFFY{ // lol
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire ?
					super.getEffectiveness(types, attackType)*2 :
					super.getEffectiveness(types, attackType);
		}
	},
	HEATPROOF{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire ?
					super.getEffectiveness(types, attackType)/2 :
					super.getEffectiveness(types, attackType);
		}
	},
	THICK_FAT{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire||attackType==EnumType.Ice ?
					super.getEffectiveness(types, attackType)/2 :
					super.getEffectiveness(types, attackType);
		}
	},
	WATER_BUBBLE{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire ?
					super.getEffectiveness(types, attackType)/2 :
					super.getEffectiveness(types, attackType);
		}
	},
	WATER_ABSORB{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Water ? -.25f : super.getEffectiveness(types, attackType);
		}
	},
	VOLT_ABSORB{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Electric ? -.25f : super.getEffectiveness(types, attackType);
		}
	},
	DRY_SKIN{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Water ? -.25f :
					attackType==EnumType.Fire ? super.getEffectiveness(types, attackType)*1.25f :
							super.getEffectiveness(types, attackType);
		}
	},
	FIRE_IMMUNITY{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire ? 0 : super.getEffectiveness(types, attackType);
		}
	},
	GROUND_IMMUNITY{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Ground ? 0 : super.getEffectiveness(types, attackType);
		}
	},
	ELECTRIC_IMMUNITY{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Fire ? 0 : super.getEffectiveness(types, attackType);
		}
	},
	WATER_IMMUNITY{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Water ? 0 : super.getEffectiveness(types, attackType);
		}
	},
	GRASS_IMMUNITY{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			return attackType==EnumType.Grass ? 0 : super.getEffectiveness(types, attackType);
		}
	},
	WONDER_GUARD{
		@Override public float getEffectiveness(List<EnumType> types, EnumType attackType){
			float effectiveness = super.getEffectiveness(types, attackType);
			return effectiveness>=2 ? effectiveness : 0;
		}
	};

	public float getEffectiveness(List<EnumType> types, EnumType attackType){
		return EnumType.getTotalEffectiveness(types, attackType);
	}

	public static AbilityBasedEffectivenessLogic get(Pokemon pokemon){
		switch(pokemon.getAbility().getName()){
			case "Filter":
				return FILTER;
			case "Fluffy":
				return FLUFFY;
			case "Heatproof":
				return HEATPROOF;
			case "ThickFat":
				return THICK_FAT;
			case "WaterBubble":
				return WATER_BUBBLE;
			case "WaterAbsorb":
				return WATER_ABSORB;
			case "VoltAbsorb":
				return VOLT_ABSORB;
			case "DrySkin":
				return DRY_SKIN;
			case "FlashFire":
				return FIRE_IMMUNITY;
			case "Levitate":
				return GROUND_IMMUNITY;
			case "MotorDrive":
			case "LightningRod":
				return ELECTRIC_IMMUNITY;
			case "StormDrain":
				return WATER_IMMUNITY;
			case "SapSipper":
				return GRASS_IMMUNITY;
			case "WonderGuard":
				return WONDER_GUARD;
			default:
				return DEFAULT;
		}
	}
}
