package sequenze.menu;

public interface BagList {

	/** Returns the number of items with the given id. */
	public int getNumber(int n);

	/** Moves the item with the given id to the given position. */
	public void moveItem(int id, int pos);
	
	/** Returns whether or not the item with the given id exists with the given quantity. */
	public boolean hasItem(int id, int amount);
	
	/** Returns the quantity of the item with the given id. */
	public int amountOfItem(int id);
	
	/** Returns the quantity of the item with the given position. */
	public int amountOfItemP(int pos);
	
	/** Adds the item with the given id in the given quantity to the list. */
	public void addItem(int id, int amount);
	
	/** Removes the item with the given id in the given quantity from the list. */
	public boolean removeItem(int id, int amount);
	
	/** Removes the item with the given position in the given quantity from the list. */
	public boolean removeItemP(int pos, int amount);
	
	/** Adds the given item id and quantity without checking whether it already exists. */
	public void initAdd(int id, int amount);
	
	/** Returns id and quantity of the items on the page that contains the given position relative to the given page size. */
	public int[][] getNumbers(int pagesize, int pos);
	
	/** Returns the selection position relative to the given page size. */
	public int getPosition(int pagesize);
	
	/** Sets the selection position to the given position relative to the given page size. */
	public void setPosition(int pagesize, int pos);
	
	/** Adds the given value to the selection position. */
	public void addPosition(int pos);
	
	/** Return the id of the selection position. */
	public int getSelectedPosition();
	
	/** Return a array of length 2 containing the id and quantity of the currently selected item. */
	public int[] getCurrent();
	
	/** Returns the amount of items in the list. */
	public int getLength();
	
}
