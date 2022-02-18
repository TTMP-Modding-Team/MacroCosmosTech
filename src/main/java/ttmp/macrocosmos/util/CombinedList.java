package ttmp.macrocosmos.util;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public class CombinedList<T> extends AbstractList<T> implements RandomAccess{
	public static <T> List<T> of(List<T> first, List<T> second){
		if(first.isEmpty()) return second;
		if(second.isEmpty()) return first;
		return new CombinedList<>(first, second);
	}

	private final List<T> first, second;

	public CombinedList(List<T> first, List<T> second){
		this.first = first;
		this.second = second;
	}

	@Override public T get(int index){
		return index>=first.size() ? second.get(index-first.size()) : first.get(index);
	}
	@Override public int size(){
		return first.size()+second.size();
	}
}
