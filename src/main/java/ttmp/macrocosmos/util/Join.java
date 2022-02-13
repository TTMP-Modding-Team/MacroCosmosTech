package ttmp.macrocosmos.util;

import java.util.Collection;

public class Join{
	public static String join(Collection<?> objs){
		return join(", ", objs);
	}
	public static String join(String delimiter, Collection<?> objs){
		StringBuilder stb = new StringBuilder();
		boolean first = true;
		for(Object obj : objs){
			if(first) first = false;
			else stb.append(delimiter);
			stb.append(obj);
		}
		return stb.toString();
	}

	@SafeVarargs public static <T> String join(T... objs){
		return join(", ", objs);
	}
	@SafeVarargs public static <T> String join(String delimiter, T... objs){
		StringBuilder stb = new StringBuilder();
		boolean first = true;
		for(T obj : objs){
			if(first) first = false;
			else stb.append(delimiter);
			stb.append(obj);
		}
		return stb.toString();
	}
}
