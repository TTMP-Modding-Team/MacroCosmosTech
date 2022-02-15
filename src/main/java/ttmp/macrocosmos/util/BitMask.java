package ttmp.macrocosmos.util;

public class BitMask{
	public static boolean get(long bitMask, int index){
		return (bitMask >> index&1)==1;
	}

	public static long set(long bitMask, int index, boolean value){
		return value ? bitMask|1L<<index : bitMask&~(1L<<index);
	}

	public static long removeAll(long bitMask, long removeBit){
		return bitMask&~removeBit;
	}

	public static boolean get(int bitMask, int index){
		return (bitMask >> index&1)==1;
	}

	public static int set(int bitMask, int index, boolean value){
		return value ? bitMask|1<<index : bitMask&~(1<<index);
	}

	public static int removeAll(int bitMask, int removeBit){
		return bitMask&~removeBit;
	}

	public static int toByteMask(boolean... values){
		if(values.length>Byte.SIZE)
			throw new IllegalArgumentException("Too many bits to fit in one byte");
		int m = 0;
		for(int i = 0; i<values.length; i++)
			if(values[i])
				m = set(m, i, true);
		return m;
	}
}
