package sequenze.menu;

import util.ConV;

public class ItemList implements BagList {

	int len = 0;// Length of the list
	int selectedItem = 0;// currently selected item position
	Item[] list;

	public ItemList() {
		list = new Item[64];
	}

	@Override
	public int getNumber(int n) {
		if (n >= len) {
			return 0;
		}
		return list[n].id;
	}

	@Override
	public void moveItem(int from, int to) {
		Item current = list[selectedItem];
		if (to > from) {
			for (int i = from; i < to; i++) {
				list[i] = list[i + 1];
			}
		} else {

		}
		list[to] = current;
	}

	@Override
	public int amountOfItem(int id) {// Returns the amount of the given item in the list, 0 if not in the list
		for (int i = 0; i < len; i++) {
			if (list[i].id == id) {
				return list[i].amount;
			}
		}
		return 0;
	}

	@Override
	public int amountOfItemP(int pos) {// Returns the amount of the given item in the list, 0 if not in the list
		if (pos >= len) {
			return 0;
		}
		return list[pos].amount;
	}

	@Override
	public boolean hasItem(int id, int amount) {// Checks whether list does contain the given item in the given amount
		return amountOfItem(id) >= amount;
	}

	@Override
	public void addItem(int id, int amount) {// Add item(s) to the end of the list, add amounts if already included
		for (int i = 0; i < len; i++) {
			if (list[i].id == id) {
				list[i].amount += amount;
				return;
			}
		}
		initAdd(id, amount);
	}

	@Override
	public boolean removeItem(int id, int amount) {// Subtract amount from specific item, fails if not included, removes all if amount = -1
		for (int i = 0; i < len; i++) {
			if (list[i].id == id) {
				if (list[i].amount == amount || amount == -1) {
					if (i == len - 1) {
						list[i] = null;
					} else {
						ConV.arrayCopy(list, i + 1, list, i, len - i - 1);
					}
					len--;
					return true;
				} else if (list[i].amount > amount) {
					list[i].amount -= amount;
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeItemP(int pos, int amount) {// Subtract amount from specific item, fails if not included, removes all if amount = -1
		if (pos >= len) {
			return false;
		}
		if (list[pos].amount == amount || amount == -1) {
			if (pos == len - 1) {
				list[pos] = null;
			} else {
				ConV.arrayCopy(list, pos + 1, list, pos, len - pos - 1);
			}
			len--;
			return true;
		} else if (list[pos].amount > amount) {
			list[pos].amount -= amount;
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void initAdd(int id, int amount) {
		if (len == list.length) {
			list = ConV.arrayCopy(list, list.length + 16);
		}
		list[len] = new Item(id, amount);
		len++;
	}

	private class Item {

		/** ID of this item. */
		int id;
		/** Amount of this item. */
		int amount;

		/** Sets the id and amount for this item. */
		Item(int i, int a) {// Set attributes
			id = i;
			amount = a;
		}
	}

	@Override
	public int[][] getNumbers(int pagesize, int pos) {
		int[][] res = new int[2][pagesize];
		if (len == 0) {
			return res;
		}
		int startpos = pos - pos % pagesize;
		for (int i = 0; i < pagesize && startpos + i < len; i++) {
			res[0][i] = list[startpos + i].id;
			res[1][i] = list[startpos + i].amount;
		}
		return res;
	}

	@Override
	public int getPosition(int pagesize) {
		return selectedItem % pagesize;
	}

	@Override
	public void setPosition(int pagesize, int pos) {
		selectedItem = selectedItem - selectedItem % pagesize + pos;
		if (pos < 0) {
			selectedItem += pagesize;
		}
	}

	@Override
	public void addPosition(int pos) {
		selectedItem += pos;
	}

	@Override
	public int getSelectedPosition() {
		return selectedItem;
	}

	@Override
	public int getLength() {
		return len;
	}

	@Override
	public int[] getCurrent() {
		if (selectedItem < len) {
			int[] res = {list[selectedItem].id, list[selectedItem].amount};
			return res;
		} else {
			int[] res = {0, 0};
			return res;
		}
	}
}
