package ttmp.macrocosmos.util;

public class BitMask{
	public static long fullMask(int size){
		return 1L<<size-1;
	}
	public static boolean get(long bitMask, int index){
		return (bitMask >> index&1)==1;
	}
	public static long set(long bitMask, int index, boolean value){
		return value ? bitMask|1L<<index : bitMask&~(1L<<index);
	}
}
