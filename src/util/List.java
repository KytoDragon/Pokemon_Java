package util;

public class List<E> implements Iterable<E> {

	private java.util.ArrayList<E> list;

	public List() {
		list = new java.util.ArrayList<>();
	}

	public void add(E e) {
		list.add(e);
	}

	public void add(int index, E e) {
		list.add(index, e);
	}

	public boolean remove(E e) {
		return list.remove(e);
	}

	public int indexOf(E e) {
		return list.indexOf(e);
	}

	@Override
	public java.util.Iterator<E> iterator() {
		return list.iterator();
	}

	public void clear() {
		list.clear();
	}

	public boolean contains(E e) {
		return list.contains(e);
	}

	public E get(int index) {
		return list.get(index);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public E remove(int index) {
		return list.remove(index);
	}

	public E set(int index, E e) {
		if (index >= size()) {
			list.add(index, e);
			return null;
		} else {
			return list.set(index, e);
		}
	}

	public int size() {
		return list.size();
	}

	public E[] toArray(E[] array) {
		return list.toArray(array);
	}

	public java.util.Enumeration<E> enumeration() {
		return java.util.Collections.enumeration(list);
	}

}
