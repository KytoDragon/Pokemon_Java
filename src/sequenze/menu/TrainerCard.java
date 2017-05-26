package sequenze.menu;

import sequenze.Button;
import sequenze.Direction;
import sequenze.Game;
import sequenze.GraphicHandler;
import sequenze.InstancePackage;

import static sequenze.InstancePackage.vfx;

import sequenze.Localisation;
import sequenze.MessageHandler;
import sequenze.SystemControll;
import sequenze.Text;
import util.ConV;
import util.Time;

public class TrainerCard extends GuiItem {

	/** Array of texture indices. */
	public int[] tex;
	public final static byte BG = 0; // screen background tile
	public final static byte CARD = 1; // trainer card front
	public final static byte COVER = 7; // cover for missing pokedex
	public final static byte STAR = 13; // trainer card star
	public final static byte BB = 14; // back button
	public final static byte BBA = 15; // back button activated
	private byte status = CLOSED;
	final static byte STARTUP = 0; // opening the trainer card with a fade in
	final static byte SHUTDOWNFRONT = 1; // closing the trainer card with a fade out
	final static byte SHUTDOWNBACK = 2; // closing the trainer card with a fade out
	final static byte CLOSED = 3; // closed
	final static byte FRONT = 4; // viewing the front
	final static byte BACK = 5; // viewing the back
	final static byte SINGING = 6; // signing the card
	//private TextBox textbox;
	private int bgAnimation;
	private boolean clickClose;

	private Cell background;
	private Cell cards;
	private Cell stars;
	private Cell idtitle;
	private Cell id;
	private Cell moneytitle;
	private Cell money;
	private Cell pokedextitle;
	private Cell pokedex;
	private Cell scoretitle;
	private Cell score;
	private Cell timetitle;
	private Cell time;
	private Cell startedtitle;
	private Cell started;
	private Cell nametitle;
	private Cell name;
	private Cell backbutton;

	public TrainerCard() {
		tex = GraphicHandler.giveTextures("TrainerCard");
		setupCells();
	}

	public void setupCells() {
		id = new Cell();
		id.x = 0;
		id.y = 0;
		id.icon_type = Cell.TEXTONLY;
		id.addText(96, 0, 0, 0x1, Text.TEXT_RIGHT);

		idtitle = new Cell();
		idtitle.x = 15;
		idtitle.y = 23;
		idtitle.icon_type = Cell.TEXTONLY;
		idtitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		idtitle.setChildren(id);

		money = new Cell();
		money.x = 0;
		money.y = 0;
		money.icon_type = Cell.TEXTONLY;
		money.addText(136, 0, 0, 0x1, Text.TEXT_RIGHT);

		moneytitle = new Cell();
		moneytitle.x = 15;
		moneytitle.y = 47;
		moneytitle.icon_type = Cell.TEXTONLY;
		moneytitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		moneytitle.setChildren(money);

		pokedex = new Cell();
		pokedex.x = 0;
		pokedex.y = 0;
		pokedex.icon_type = Cell.TEXTONLY;
		pokedex.addText(136, 0, 0, 0x1, Text.TEXT_RIGHT);

		pokedextitle = new Cell();
		pokedextitle.x = 15;
		pokedextitle.y = 71;
		pokedextitle.icon_type = Cell.TEXTONLY;
		pokedextitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		pokedextitle.setChildren(pokedex);

		score = new Cell();
		score.x = 0;
		score.y = 0;
		score.icon_type = Cell.TEXTONLY;
		score.addText(136, 0, 0, 0x1, Text.TEXT_RIGHT);

		scoretitle = new Cell();
		scoretitle.x = 15;
		scoretitle.y = 103;
		scoretitle.icon_type = Cell.TEXTONLY;
		scoretitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		scoretitle.setChildren(score);

		time = new Cell();
		time.x = 0;
		time.y = 0;
		time.icon_type = Cell.TEXTONLY;
		time.addText(224, 0, 0, 0x1, Text.TEXT_RIGHT);

		timetitle = new Cell();
		timetitle.x = 15;
		timetitle.y = 127;
		timetitle.icon_type = Cell.TEXTONLY;
		timetitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		timetitle.setChildren(time);

		started = new Cell();
		started.x = 0;
		started.y = 0;
		started.icon_type = Cell.TEXTONLY;
		started.addText(224, 0, 0, 0x1, Text.TEXT_RIGHT);

		startedtitle = new Cell();
		startedtitle.x = 15;
		startedtitle.y = 143;
		startedtitle.icon_type = Cell.TEXTONLY;
		startedtitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		startedtitle.setChildren(started);

		name = new Cell();
		name.x = 0;
		name.y = 0;
		name.icon_type = Cell.TEXTONLY;
		name.addText(104, 0, 0, 0x1, Text.TEXT_RIGHT);

		nametitle = new Cell();
		nametitle.x = 135;
		nametitle.y = 23;
		nametitle.icon_type = Cell.TEXTONLY;
		nametitle.addText(0, 0, 0, 0x1, Text.TEXT_LEFT);
		nametitle.setChildren(name);

		stars = new Cell();
		stars.x = 233;
		stars.y = 5;
		stars.arangement = Cell.ROW;
		stars.x_col = -16;
		stars.icon = new Icon(tex[STAR], 0, 0);

		cards = new Cell();
		cards.x = 2;
		cards.y = 1;
		cards.w = 165;
		cards.h = 252;
		cards.icon_type = Cell.MULTIICON;
		cards.starts = new int[]{0};
		cards.icons = new Icon[1][6];
		for (int i = 0; i < cards.icons[0].length; i++) {
			cards.icons[0][i] = new Icon(tex[CARD + i], 0, 0);
		}
		cards.setChildren(stars, idtitle, moneytitle, pokedextitle, scoretitle, timetitle, startedtitle, nametitle);

		backbutton = new Cell();
		backbutton.x = 193;
		backbutton.y = 227;
		backbutton.w = 60;
		backbutton.h = 24;
		backbutton.addText(30, 4, 2, 0xB, Text.TEXT_MIDDLE);
		backbutton.icon = new Icon(tex[BB], 0, tex[BBA]);
	}

	@Override
	public void update(Game g) {
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.CARD_OPEN)) {
					clickClose = false;
					vfx.addShutter(Direction.NORTH, true);
					status = STARTUP;
				}
				// TODO check for other messages
				break;
			case STARTUP:
				if (vfx.shutterFinished()) {
					status = FRONT;
				}
				break;
			case SHUTDOWNFRONT:
			case SHUTDOWNBACK:
				if (vfx.shutterFinished()) {
					status = CLOSED;
					MessageHandler.add(MessageHandler.CARD_CLOSE, clickClose);
				}
				break;
			case FRONT:
				if (backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWNFRONT;
					}
					break;
				}
				if (g.wasPressed(Button.B)) {
					backbutton.animate(false);
				} else {
					bgAnimation++;
					if (bgAnimation == 32) {
						bgAnimation = 0;
					}
				}
				frontclick(g);
				break;
		}
	}

	private void frontclick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		if (backbutton.click(g.getMouseX(), g.getMouseY())) {
			backbutton.animate(false);
		}
	}

	@Override
	public void drawL() {
		if (status != CLOSED) {
			drawBackground();
		}
	}

	@Override
	public void drawR() {
		switch (status) {
			case STARTUP:
				drawBackground();
				drawCardFront();
				/*drawTitle();
				 drawText();
				 drawButtons();
				 drawSelection();*/
				drawBackButton();
				break;
			case SHUTDOWNFRONT:
				drawBackground();
				drawCardFront();
				/*drawTitle();
				 drawText();
				 drawButtons();
				 drawSelection();*/
				drawBackButton();
				break;
			case SHUTDOWNBACK:
				drawBackground();
				/*drawTitle();
				 drawText();
				 drawButtons();
				 drawSelection();*/
				drawBackButton();
				break;
			case FRONT:
				drawBackground();
				drawCardFront();
				/*drawTitle();
				 drawText();
				 drawButtons();
				 drawSelection();*/
				drawBackButton();
				break;
			case BACK:
				drawBackground();
				/*drawTitle();
				 drawText();
				 drawButtons();
				 drawSelection();*/
				drawBackButton();
				break;
		}
	}

	private void drawCardFront() {
		int numstars = SystemControll.getVariable(SystemControll.TrainerCardStars);
		cards.draw(numstars, false);
		// TODO change to Cell.CHILDREN type for easier handling, figure out how to handle children in Cell.CHILDREN mode
		cards.getChild(0).drawMulti(numstars, -1);
		char[][] text = Localisation.getCardTitles();
		cards.getChild(1).draw(text[0]);
		cards.getChild(1).getChild(0).draw(ConV.toFixedCString(SystemControll.getVariable(SystemControll.TrainerID)));
		cards.getChild(2).draw(text[1]);
		cards.getChild(2).getChild(0).draw(ConV.sprintf(Localisation.getCardMoney(), SystemControll.getVariable(SystemControll.Money)));
		cards.getChild(3).draw(text[2]);
		cards.getChild(3).getChild(0).draw(ConV.toCString(InstancePackage.pokedex.caughtNum(0)));
		cards.getChild(4).draw(text[3]);
		cards.getChild(4).getChild(0).draw(ConV.toCString(SystemControll.getVariable(SystemControll.PlayerScore)));
		long minutes = SystemControll.getVariable(SystemControll.TimePlayed) / 60;
		cards.getChild(5).draw(text[4]);
		cards.getChild(5).getChild(0).draw(ConV.sprintf(Localisation.getCardTime(), (minutes / 60), (minutes % 60 / 10), (minutes % 10)));
		cards.getChild(6).draw(text[5]);
		int[] res = Time.timestamp_to_gmt(SystemControll.getVariable(SystemControll.TimeStart));
		cards.getChild(6).getChild(0).draw(ConV.sprintf(Localisation.getCardDate(), res[3], Localisation.getMonths()[res[4] - 1], res[5]));
		cards.getChild(7).draw(text[6]);
		cards.getChild(7).getChild(0).draw(SystemControll.getString(SystemControll.PlayerName));
	}

	private void drawBackButton() {
		backbutton.draw(Localisation.getCardBackButton());
	}

	private void drawBackground() {
		for (int i = -1; i < 8; i++) {
			for (int j = -1; j < 8; j++) {
				GraphicHandler.drawBox(j * 32 + bgAnimation, i * 32 + bgAnimation, tex[BG]);
			}
		}
	}

	@Override
	public boolean hasFocus() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesLeft() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED;
	}

}
