package sequenze.menu;

import sequenze.GraphicHandler;
import sequenze.Text;
import sequenze.overlay.TextBox;
import util.Logger;

public class Cell implements Cloneable {

	public Cell[] children;
	public Cell parent;
	public int x;
	public int y;

	public int w;
	public int h;

	public int x_col;
	public int y_row;
	public int x_row;
	public int y_col;

	public int columns;
	public int rows;

	public int icon_type;
	public static final int ICON = 0;
	public static final int MULTIICON = 1; // TODO differenciate between Multi-pos vs Multi-id
	public static final int TEXTONLY = 2;
	public static final int TEXTBOX = 3;

	public Text text = null;

	public int arangement = -1;
	public static final int GRID = 0;
	public static final int ROW = 1;
	public static final int COLLUMN = 2;
	public static final int GRIDCR = 3;
	public static final int GRIDY = 4;
	public static final int GRIDX = 5;
	public static final int CHILDREN = 6;

	public Icon icon;
	public Icon[][] icons;
	public int[] starts;

	public boolean animationRunning;
	public int animationPos;
	public int animationIndex;
	public boolean animationSelected;

	public Cell get(int id) {
		return children[id];
	}

	public void setChildren(Cell... children) {
		this.children = children;
	}

	public Cell getChild(int id) {
		Cell cell = children[id].clone();
		cell.x += x;
		cell.y += y;
		return cell;
	}

	public Cell getChildMulti(int id, int pos) {
		Cell cell = children[id].clone();
		cell.x += getX(pos);
		cell.y += getY(pos);
		return cell;
	}

	public void addText(int x, int y, int style, int palette, int type) {
		text = new Text(x, y, style, palette, type);
	}

	public void addText(int x, int y, int style, int palette, int type, int lines, int line_off) {
		text = new Text(x, y, style, palette, type, lines, line_off);
	}

	public boolean click(int x, int y) {
		x -= this.x;
		y -= this.y;
		if (x < 0 || y < 0 || x >= w || y >= h) {
			return false;
		}
		return true;
	}

	public int clickMulti(int x, int y, int num) {
		x -= this.x;
		y -= this.y;
		int id;
		S:
		switch (arangement) {
			case GRID:
				int rows = (num + this.columns - 1) / this.columns;
				if (x < 0 || y < 0) {
					return -1;
				}
				if (x >= this.columns * x_col || y >= rows * y_row) {
					return -1;
				}
				if (x % x_col >= w || y % y_row >= h) {
					return -1;
				}
				id = x / x_col + y / y_row * this.columns;
				if (id >= num) {
					return -1;
				}
				break;
			case ROW:
				if (x < 0 || y < 0) {
					return -1;
				}
				if (x >= num * x_col) {
					return -1;
				}
				if (x % x_col >= w || y >= h) {
					return -1;
				}
				id = x / x_col;
				break;
			case COLLUMN:
				if (x < 0 || y < 0) {
					return -1;
				}
				if (y >= num * y_row) {
					return -1;
				}
				if (x >= w || y % y_row >= h) {
					return -1;
				}
				id = y / y_row;
				break;
			case GRIDCR:
				int columns = (num + this.rows - 1) / this.rows;
				if (x < 0 || y < 0) {
					return -1;
				}
				if (x >= columns * x_col || y >= this.rows * y_row) {
					return -1;
				}
				if (x % x_col >= w || y % y_row >= h) {
					return -1;
				}
				id = x / x_col + y / y_row * columns;
				if (id >= num) {
					return -1;
				}
				break;
			case GRIDY:
				rows = (num + this.columns - 1) / this.columns;
				int column_y = y - x / x_col * y_col;
				if (x < 0 || column_y < 0) {
					return -1;
				}
				if (x >= this.columns * x_col || column_y >= rows * y_row) {
					return -1;
				}
				if (x % x_col >= w || column_y % y_row >= h) {
					return -1;
				}
				id = x / x_col + column_y / y_row * this.columns;
				if (id >= num) {
					return -1;
				}
				break;
			case GRIDX:
				rows = (num + this.columns - 1) / this.columns;
				int row_x = x - y / y_row * x_row;
				if (row_x < 0 || y < 0) {
					return -1;
				}
				if (row_x >= this.columns * x_col || y >= rows * y_row) {
					return -1;
				}
				if (row_x % x_col >= w || y % y_row >= h) {
					return -1;
				}
				id = row_x / x_col + y / y_row * this.columns;
				if (id >= num) {
					return -1;
				}
				break;
			case CHILDREN:
				for (int i = 0; i < num; i++) {
					if (this.rows + i >= children.length) {
						return -1;
					}
					if (children[this.rows + i].click(x, y)) {
						id = i;
						break S;
					}
				}
				return -1;
			default:
				Logger.add(Logger.CELL, "unknown grid display type");
				return -1;
		}
		return id;
	}

	public boolean animationFinished() {
		if (animationIndex > 0) {
			animationIndex--;
		}
		if (animationRunning && animationIndex == 0) {
			animationRunning = false;
			return true;
		} else {
			return false;
		}
	}

	public void draw(int iconID) {
		draw(null, iconID, false);
	}

	public void draw(int iconID, boolean selected) {
		draw(null, iconID, selected);
	}

	public void draw(char[] text, int iconID, boolean selected) {
		draw(getIcon(0, iconID), text, selected);
	}

	public void draw() {
		draw(null, false);
	}

	public void draw(char[] text) {
		draw(text, false);
	}

	public void draw(char[] text, boolean selected) {
		draw(icon, text, selected);
	}

	public void draw(Icon icon, char[] text, boolean selected) {
		GraphicHandler.translate(x, y);
		if (icon_type == ICON || icon_type == MULTIICON) {
			if (animationRunning) {
				if (animationSelected) {
					icon.drawSelectedAnimation(animationIndex);
				} else {
					icon.drawAnimation(animationIndex);
				}
			} else {
				if (selected) {
					icon.drawSelectedTexture();
				} else {
					icon.drawTexture();
				}
			}
		} else if (icon_type == TEXTBOX) {
			TextBox.drawFrame(0, 0, (w - 40) / 8, (h - 32) / 16);
		}
		drawText(text);
		GraphicHandler.translate(-x, -y);
	}

	private void draw(char[] text, int iconID, boolean selected, boolean aniR, boolean aniS, int aniI) {
		GraphicHandler.translate(x, y);
		if (icon_type == ICON || icon_type == MULTIICON) {
			Icon i = getIcon(0, iconID);
			if (aniR) {
				if (aniS) {
					i.drawSelectedAnimation(aniI);
				} else {
					i.drawAnimation(aniI);
				}
			} else {
				if (selected) {
					i.drawSelectedTexture();
				} else {
					i.drawTexture();
				}
			}
		} else if (icon_type == TEXTBOX) {
			TextBox.drawFrame(0, 0, (w - 40) / 8, (h - 32) / 16);
		}
		drawText(text);
		GraphicHandler.translate(-x, -y);
	}

	public void drawMulti(int num, int selected) {
		for (int i = 0; i < num; i++) {
			drawMulti(null, i, selected == i);
		}
	}

	public void drawMulti(char[] text, int pos) {
		drawMulti(text, pos, 0, false);
	}

	public void drawMulti(char[][] text, int num, int selected) {
		for (int i = 0; i < num; i++) {
			drawMulti(text[i], i, selected == i);
		}
	}

	public void drawMulti(char[] text, int pos, boolean selected) {
		drawMulti(text, pos, 0, selected);
	}

	public void drawMulti(char[] text, int pos, int iconID, boolean selected) {
		GraphicHandler.translate(getX(pos), getY(pos));
		if (arangement == CHILDREN) {
			children[rows + pos].draw(text, iconID, selected, animationRunning && (animationPos == pos || animationPos == -1), animationSelected, animationIndex);
		} else {
			if (icon_type == ICON || icon_type == MULTIICON) {
				Icon i = getIcon(pos, iconID);
				if (animationRunning && (animationPos == pos || animationPos == -1)) {
					if (animationSelected) {
						i.drawSelectedAnimation(animationIndex);
					} else {
						i.drawAnimation(animationIndex);
					}
				} else {
					if (selected) {
						i.drawSelectedTexture();
					} else {
						i.drawTexture();
					}
				}
			} else if (icon_type == TEXTBOX) {
				TextBox.drawFrame(-16, -8, (w - 40) / 8, (h - 32) / 16);
			}
			drawText(text);
		}
		GraphicHandler.translate(-getX(pos), -getY(pos));
	}

	public void animate(boolean selected) {
		animationRunning = true;
		animationPos = -1;
		animationSelected = selected;
		animationIndex = getAnimationDuration(0, 0, selected);
	}

	public void animate(int pos, boolean selected) {
		animate(pos, 0, selected);
	}

	public void animate(int pos, int iconID, boolean selected) {
		animationRunning = true;
		animationPos = pos;
		animationSelected = selected;
		if (arangement == CHILDREN) {
			animationIndex = children[rows + pos].getAnimationDuration(0, iconID, selected);
		} else {
			animationIndex = getAnimationDuration(pos, iconID, selected);
		}
	}

	private int getAnimationDuration(int pos, int iconID, boolean selected) {
		if (icon_type == TEXTONLY || icon_type == TEXTBOX) {
			return 0;
		} else {
			return getIcon(pos, iconID).getAnimationDuration(selected);
		}
	}

	public void stopAnimation() {
		animationRunning = false;
		animationPos = -1;
		animationIndex = 0;
		animationSelected = false;
	}

	private Icon getIcon(int pos, int iconID) {
		if (icon_type == ICON) {
			return icon;
		} else if (icon_type == MULTIICON) {
			for (int i = 1; i < starts.length; i++) {
				if (pos < starts[i]) {
					return icons[i - 1][iconID];
				}
			}
			return icons[starts.length - 1][iconID];
		}
		return null;
	}

	public void generateBoxText(int style, int palette) {
		int lines = (h - 32) / 16 + 1;
		if (lines == 1) {
			text = new Text(16, 8, style, palette, Text.TEXT_LEFT);
		} else {
			text = new Text(16, 8, style, palette, Text.TEXT_LEFT, lines, 16);
		}
	}

	private void drawText(char[] text) {
		if (text == null) {
			return;
		}
		if (this.text == null) {
			Logger.add(Logger.CELL, "Tried to draw text to a cell without text fields");
			return;
		}
		this.text.draw(text);
	}

	private int getX(int pos) {
		switch (arangement) {
			case GRID:
			case GRIDY:
				return x + pos % columns * x_col;
			case ROW:
				return x + pos * x_col;
			case COLLUMN:
				return x;
			case GRIDCR:
				return x + pos / rows * x_col;
			case GRIDX:
				return x + pos % columns * x_col + pos / columns * x_row;
			case CHILDREN:
				return x;
			default:
				Logger.add(Logger.CELL, "unknown grid display type");
				return 0;
		}
	}

	private int getY(int pos) {
		switch (arangement) {
			case GRID:
			case GRIDX:
				return y + pos / columns * y_row;
			case ROW:
				return y;
			case COLLUMN:
				return y + pos * y_row;
			case GRIDCR:
				return y + pos % rows * y_row;
			case GRIDY:
				return y + pos / columns * y_row + pos % columns * y_col;
			case CHILDREN:
				return y;
			default:
				Logger.add(Logger.CELL, "unknown grid display type");
				return 0;
		}
	}

	@Override
	public Cell clone() {
		Cell result = new Cell();
		result.x = x;
		result.y = y;
		result.w = w;
		result.h = h;
		result.arangement = arangement;
		result.x_col = x_col;
		result.y_row = y_row;
		result.x_row = x_row;
		result.y_col = y_col;
		result.columns = columns;
		result.rows = rows;
		result.starts = starts;
		result.icons = icons;
		result.icon = icon;
		result.children = children;
		result.icon_type = icon_type;

		if (text != null) {
			result.text = text.clone();
		}

		result.animationIndex = animationIndex;
		result.animationPos = animationPos;
		result.animationRunning = animationRunning;
		result.animationSelected = animationSelected;
		return result;
	}
}
