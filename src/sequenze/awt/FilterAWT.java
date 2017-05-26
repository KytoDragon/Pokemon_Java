package sequenze.awt;

import sequenze.Filter;

public class FilterAWT implements Filter{

	GraphicDisplayAWT gd;
	int r = 0;
	
	public FilterAWT(GraphicDisplayAWT gd){
		this.gd = gd;
	}
	
	@Override
	public void applyRight() {
		// TODO
		return;
	}

	@Override
	public void applyLeft() {
		r++;
		int[][] data = gd.getCurrent().getData();
		int[][] result = new int[data.length][data[0].length];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				int newx = x;// + ConV.abs(10 - (r+x) % 20) - 5;
				int newy = y;// + ConV.abs(10 - (r+y) % 20) - 5;
				if(newx >= data[0].length || newx < 0 || newy >= data.length || newy < 0 ){
					result[y][x] = 0x00000000;
				}else{
					result[y][x] = data[newy][newx];
				}
			}
		}
		gd.getCurrent().setData(result);
	}
	
}
