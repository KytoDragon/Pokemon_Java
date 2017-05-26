package sequenze.menu;

import util.ConV;

public class BaseItemList implements BagList {

	/** The length of the list. */
	int len = 0;
	/** The currently selected position. */
	int selectedItem = 0;

	BaseItem[] list;

	/** creates a new base item list. */
	public BaseItemList() {
		list = new BaseItem[64];
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
		BaseItem current = list[selectedItem];
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
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int amountOfItemP(int pos) {// Returns the amount of the given item in the list, 0 if not in the list
		if (pos >= len) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean hasItem(int id, int amount) {// Checks whether list does contain the given item in the given amount
		return amountOfItem(id) >= amount;
	}

	@Override
	public void addItem(int id, int amount) {// Add item(s) to the end of the list, add amounts if already included
		for (int i = 0; i < len; i++) {
			if (list[i].id == id) {
				return;
			}
		}
		initAdd(id, amount);
	}

	@Override
	public boolean removeItem(int id, int amount) {// Subtract amount from specific item, fails if not included, removes all if amount = -1
		if (amount < -1 || amount > 1) {
			return false;
		}
		if (amount == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if (list[i].id == id) {
				if (i == len - 1) {
					list[i] = null;
				} else {
					ConV.arrayCopy(list, i + 1, list, i, len - i - 1);
				}
				len--;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeItemP(int pos, int amount) {// Subtract amount from specific item, fails if not included, removes all if amount = -1
		if (pos >= len) {
			return false;
		}

		if (pos == len - 1) {
			list[pos] = null;
		} else {
			ConV.arrayCopy(list, pos + 1, list, pos, len - pos - 1);
		}
		len--;
		return true;

	}

	@Override
	public void initAdd(int id, int amount) {
		if (len == list.length) {
			list = ConV.arrayCopy(list, list.length + 16);
		}
		list[len] = new BaseItem(id);
		len++;
	}

	private class BaseItem {

		/** ID of this item. */
		int id;

		/** Sets the id for this item. */
		BaseItem(int i) {// Set attributes
			id = i;
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
			res[1][i] = 1;
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
			int[] res = {list[selectedItem].id, 1};
			return res;
		} else {
			int[] res = {0, 0};
			return res;
		}
	}
}
