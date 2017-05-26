package sequenze.overlay;

import sequenze.Button;
import sequenze.Game;
import sequenze.GraphicHandler;
import sequenze.SystemControll;

public class MCQuestion extends MenuObject {

	boolean isAtQuestion = false;
	boolean end = false;
	int xq = 3;
	int yq = 72;
	int choice = 0;
	char[][] answers;
	TextBox tb;

	public MCQuestion(char[] text, char[][] answers) {
		tb = new TextBox(text, true);
		this.answers = answers;
	}

	@Override
	public void update(Game g, MenuObjectHandler m) {
		if (!isAtQuestion) {
			tb.update(g, null);
			if (tb.isFinished()) {
				end = false;
				isAtQuestion = true;
			}
		} else if (!end) {
			// TODO clicking
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				end = true;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				choice = (choice + 1) % answers.length;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				choice = (choice - 1 + answers.length) % answers.length;
			} else if (g.wasPressed(Button.B)) {
				g.process(Button.B);
			}
		} else {
			SystemControll.setVariable(SystemControll.MCQuestionAnswer, choice);
			m.deleteObject();
			return;
		}
	}

	@Override
	public void drawR() {
		if (isAtQuestion) {
			drawYNQuestion(MenuObjectHandler.current.tex);
		}
	}

	void drawYNQuestion(int[] tex) {
		// TODO convert
		int w = 234;
		int g = 56;
		GraphicHandler.translate(xq, yq);
		GraphicHandler.drawBox(0, 0, tex[MenuObjectHandler.YNL]);
		GraphicHandler.drawBox(8 + w, 0, tex[MenuObjectHandler.YNM]);
		GraphicHandler.drawBox(8, 0, tex[MenuObjectHandler.YNR]);

		GraphicHandler.drawBox(0, g, tex[MenuObjectHandler.YNL]);
		GraphicHandler.drawBox(8 + w, g, tex[MenuObjectHandler.YNM]);
		GraphicHandler.drawBox(8, g, tex[MenuObjectHandler.YNR]);

		if (choice == 0) {
			GraphicHandler.drawBox(-1, -1, tex[MenuObjectHandler.YNLS]);
			GraphicHandler.drawBox(8 + w, -1, tex[MenuObjectHandler.YNMS]);
			GraphicHandler.drawBox(8, -1, tex[MenuObjectHandler.YNRS]);
		} else {
			GraphicHandler.drawBox(-1, g - 1, tex[MenuObjectHandler.YNLS]);
			GraphicHandler.drawBox(8 + w, g - 1, tex[MenuObjectHandler.YNMS]);
			GraphicHandler.drawBox(8, g - 1, tex[MenuObjectHandler.YNRS]);
		}
		GraphicHandler.translate(-xq, -yq);
	}

	@Override
	void drawL() {
		tb.drawL();
	}

	@Override
	boolean getsInput() {
		return true;
	}

	@Override
	void close(MenuObjectHandler m) {
		m.deleteObject();
	}

}
