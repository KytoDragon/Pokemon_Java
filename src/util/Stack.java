package util;

public class Stack<E> implements Iterable<E> {

	private java.util.Stack<E> stack;

	public Stack() {
		stack = new java.util.Stack<>();
	}

	public void push(E e) {
		stack.push(e);
	}

	public E pop() {
		if (stack.isEmpty()) {
			return null;
		}
		return stack.pop();
	}

	public E peek() {
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public int size() {
		return stack.size();
	}

	public void clear() {
		stack.clear();
	}

	@Override
	public java.util.Iterator<E> iterator() {
		return stack.iterator();
	}
}
