package sequenze.menu;

public final class DataLoader {

	private DataLoader() {
	}

	public static int[] getGadgetBackButton() {
		// TODO Auto-generated method stub
		int[] tmp = {240, 0, 16, 16}; // (x, y, w, h) of the back (menu) button for the gadget
		return tmp;
	}

	public static int[] getGadgetReturnButton() {
		// TODO Auto-generated method stub
		int[] tmp = {240, 240, 16, 16}; // (x, y, w, h) of the return (close app) button for the gadget
		return tmp;
	}

	public static int[] getGadgetRedCover() {
		// TODO Auto-generated method stub
		int[] tmp = {0, 80, 256, 96}; // (x, y, w, h) of the cover for incoming calls
		return tmp;
	}

	public static int[][] getGadgetPositions() {
		// TODO Auto-generated method stub
		int[][] tmp = {{80, 80, 32, 32}}; // (number of gadgets) (x, y, w, h) of the individual gadgets
		return tmp;
	}

	public static int[] getTeamInfoStatsOrder() {
		// TODO Auto-generated method stub
		int[] tmp = {0, 1, 3, 4, 2};
		return tmp;
	}

}
