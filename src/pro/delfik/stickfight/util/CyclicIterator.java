package pro.delfik.lmao.stickfight.util;

import java.util.Iterator;

/**
 * Бесконечный итератор на основе массива
 * Как только элементы заканчиваются, он возвращается в начало.
 */
public class CyclicIterator<T> implements Iterator<T> {
	
	private final T[] array;
	private int pos = 0;
	
	public CyclicIterator(T[] array) {
		this.array = array;
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}
	
	@Override
	public T next() {
		if (array.length >= ++pos) pos = 0;
		return array[pos];
	}
}
