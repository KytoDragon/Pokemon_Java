package sequenze.menu;

import script.Interpreter;
import sequenze.Button;
import sequenze.Direction;
import sequenze.EventControll;
import sequenze.EventMethodLibrary;
import sequenze.ExtPokemon;
import sequenze.FlagHandler;
import sequenze.Game;
import sequenze.Gender;
import sequenze.GraphicHandler;

import static sequenze.InstancePackage.summary;
import static sequenze.InstancePackage.vfx;

import sequenze.Localisation;
import sequenze.MessageHandler;
import sequenze.Pokemon;
import sequenze.Statistic;
import sequenze.StatusCondition;
import sequenze.SystemControll;
import sequenze.Text;
import sequenze.menu.Icon.DTD;
import util.ConV;
import util.Logger;

public class Team extends GuiItem {

	public ExtPokemon[/* max Number of Pokemon */] team;
	int selection = 0; // 0 pokemon selection, 1 back button, 2-12 options, 13-15 info pages, 16-19 attack move selection
	int currentPokemon = 0; // 0 - number of pokemon -1
	int moveFrom = 0; // 0 - 3 in MOVEATTACK, 0 - number of pokemon -1 in POKEMONSELECTION
	public int[/* number of textures */] tex;
	public final static byte RBG = 0; // background of the right side
	public final static byte LBG = 1; // background of the left side
	public final static byte IB = 2; // info box
	public final static byte BB = 3; // back button
	public final static byte BBA = 4; // back button activated
	public final static byte MBB = 5; // menu back button
	public final static byte MBBS = 7; // menu back button selected
	public final static byte MO = 8; // menu option
	public final static byte MOS = 9; // menu option selected
	public final static byte PIC = 10; // pokeball icon
	public final static byte PICS = 11; // pokeball icon selected
	public final static byte ML = 12; // member label
	public final static byte MLF = 13; // member label first
	public final static byte MLKO = 14; // member label KO
	public final static byte MLKOF = 15; // member label KO first
	public final static byte MLS = 16; // member label selected
	public final static byte MLSF = 17; // member label first selected
	public final static byte MLSKO = 18; // member label KO selected
	public final static byte MLSKOF = 19; // member label KO first selected
	public final static byte MLM = 20; // member label move
	public final static byte MLMF = 21; // member label move first
	public final static byte SB = 22; // selection box
	public final static byte SBF = 23; // selection box first
	public final static byte MB = 24; // move box
	public final static byte MBF = 25; // move box first
	public final static byte AB = 26; // activation box
	public final static byte ABF = 27; // activation box first
	public final static byte MALE = 28; // male sign
	public final static byte FEMALE = 29; // female sign
	public final static byte HPGREEN = 30; // green hp bar
	public final static byte HPYELLOW = 31; // yellow hp bar
	public final static byte HPRED = 32; // red hp bar
	public final static byte HPBAR = 33; // hp bar background
	public final static byte HPBARKO = 34; // hp bar background KO
	public final static byte HPBARMOVE = 35; // hp bar background moving
	public final static byte STATUSCD = 36; // status condition icon 1-6
	public final static int ITEM = 42; // item icon
	public final static int LETTER = 43; // letter icon
	public final static int CAPSULE = 44; // ball capsule icon

	private byte status = CLOSED; // current status of the team
	private final static byte STARTUP = 0; // starting up
	private final static byte SHUTDOWN = 1; // shutting down
	private final static byte POKEMONSELECTION = 2; // selecting a pokemon
	private final static byte POKEMONOPTIONS = 3; // choosing what to do with the selected pokemon
	private final static byte MOVEPOKEMON = 4; // moving a pokemon
	private final static byte USEITEM = 5; // using an item on a pokemon
	private final static byte CLOSED = 7; // closed
	private final static byte SUMMARY = 8; // viewing the stats of a pokemon

	private boolean clickClose;

	private Cell whatdo;
	private Cell choose;
	private Cell moveto;
	private Cell backgroudR;
	private Cell backgroudL;
	private Cell items;
	private Cell itemicon;
	private Cell itemhpbar;
	private Cell itemstatus;
	private Cell itemgender;
	private Cell itemextra;
	private Cell hpbar;
	private Cell backbutton;
	private Cell menubackbutton;
	private Cell menuitems;
	private Cell menuattacks;
	private Cell infobox;
	private Cell infoicon;
	private Cell infostatus;
	private Cell infoitemtitle;
	private Cell infoitem;

	public Team() {
		FlagHandler fh = FlagHandler.current();
		fh.initTeamLoader();
		team = new ExtPokemon[SystemControll.getVariable(SystemControll.TeamSize)];
		for (int i = 0; i < team.length; i++) {
			team[i] = fh.nextTeamPokemon();
		}
		tex = GraphicHandler.giveTextures("Team");

		setupCells();
	}

	public void setupCells() {
		backgroudR = new Cell();
		backgroudR.x = 0;
		backgroudR.y = 0;
		backgroudR.w = 256;
		backgroudR.h = 256;
		backgroudR.icon = new Icon(tex[RBG], 0, 0);

		backgroudL = new Cell();
		backgroudL.x = 0;
		backgroudL.y = 0;
		backgroudL.w = 256;
		backgroudL.h = 256;
		backgroudL.icon = new Icon(tex[LBG], 0, 0);

		itemicon = new Cell();
		itemicon.x = 14;
		itemicon.y = -3;
		itemicon.w = 24;
		itemicon.h = 24;

		hpbar = new Cell();
		hpbar.x = 16;
		hpbar.y = 1;
		hpbar.w = 1;
		hpbar.h = 4;
		hpbar.arangement = Cell.ROW;
		hpbar.x_col = 1;
		hpbar.columns = 48;
		hpbar.icon_type = Cell.MULTIICON;
		hpbar.starts = new int[]{0};
		hpbar.icons = new Icon[1][3];
		hpbar.icons[0][0] = new Icon(tex[HPGREEN], 0, 0);
		hpbar.icons[0][1] = new Icon(tex[HPYELLOW], 0, 0);
		hpbar.icons[0][2] = new Icon(tex[HPRED], 0, 0);

		itemhpbar = new Cell();
		itemhpbar.x = 48;
		itemhpbar.y = 22;
		itemhpbar.w = 69;
		itemhpbar.h = 7;
		itemhpbar.icon_type = Cell.MULTIICON;
		itemhpbar.addText(37, 4, 4, 0x1, Text.TEXT_SLASH);
		itemhpbar.starts = new int[]{0};
		itemhpbar.icons = new Icon[1][3];
		itemhpbar.icons[0][0] = new Icon(tex[HPBAR], 0, 0);
		itemhpbar.icons[0][1] = new Icon(tex[HPBARKO], 0, 0);
		itemhpbar.icons[0][2] = new Icon(tex[HPBARMOVE], 0, 0);
		itemhpbar.setChildren(hpbar);

		itemstatus = new Cell();
		itemstatus.x = 20;
		itemstatus.y = 33;
		itemstatus.w = 20;
		itemstatus.h = 8;
		itemstatus.icon_type = Cell.MULTIICON;
		itemstatus.addText(-10, -7, 4, 0x1, Text.TEXT_LEFT);
		itemstatus.starts = new int[]{0};
		itemstatus.icons = new Icon[1][7];
		itemstatus.icons[0][0] = new Icon();
		for (int i = 0; i < itemstatus.icons[0].length - 1; i++) {
			itemstatus.icons[0][1 + i] = new Icon(tex[STATUSCD + i], 0, 0);
		}

		itemgender = new Cell();
		itemgender.x = 112;
		itemgender.y = 8;
		itemgender.w = 6;
		itemgender.h = 10;
		itemgender.icon_type = Cell.MULTIICON;
		itemgender.starts = new int[]{0};
		itemgender.icons = new Icon[1][2];
		itemgender.icons[0][0] = new Icon(tex[MALE], 0, 0);
		itemgender.icons[0][1] = new Icon(tex[FEMALE], 0, 0);

		itemextra = new Cell();
		itemextra.x = 30;
		itemextra.y = 21;
		itemextra.w = 8;
		itemextra.h = 8;
		itemextra.icon_type = Cell.MULTIICON;
		itemextra.starts = new int[]{0};
		itemextra.icons = new Icon[1][3];
		itemextra.icons[0][0] = new Icon(tex[ITEM], 0, 0);
		itemextra.icons[0][1] = new Icon(tex[LETTER], 0, 0);
		itemextra.icons[0][1] = new Icon(tex[CAPSULE], 0, 0);

		// animation for moving pokemon imposible
		items = new Cell();
		items.x = 0;
		items.y = 23;
		items.w = 128;
		items.h = 46;
		items.arangement = Cell.GRIDY;
		items.x_col = 128;
		items.y_row = 60;
		items.y_col = 22;
		items.columns = 2;
		items.icon_type = Cell.MULTIICON;
		items.addText(48, 5, 0, 0x15, Text.TEXT_LEFT);
		items.starts = new int[]{0, 1};
		items.icons = new Icon[2][6];
		Icon ic1 = new Icon();
		ic1.normal = new DTD[]{new DTD(tex[MLF]), new DTD(tex[PIC], 5, -3)};
		ic1.sel = new DTD[]{new DTD(tex[MLSF]), new DTD(tex[SBF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][0] = ic1;
		Icon ic3 = new Icon();
		ic3.sel = new DTD[]{new DTD(tex[MLSF]), new DTD(tex[ABF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][1] = ic3;
		Icon ic2 = new Icon();
		ic2.normal = new DTD[]{new DTD(tex[MLKOF]), new DTD(tex[PIC], 5, -3)};
		ic2.sel = new DTD[]{new DTD(tex[MLSKOF]), new DTD(tex[SBF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][2] = ic2;
		Icon ic4 = new Icon();
		ic4.sel = new DTD[]{new DTD(tex[MLSKOF]), new DTD(tex[ABF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][3] = ic4;
		Icon ic5 = new Icon();
		ic5.normal = new DTD[]{new DTD(tex[MLMF]), new DTD(tex[MBF]), new DTD(tex[PIC], 5, -3)};
		ic5.sel = new DTD[]{new DTD(tex[MLMF]), new DTD(tex[SBF]), new DTD(tex[MBF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][4] = ic5;
		Icon ic6 = new Icon();
		ic6.sel = new DTD[]{new DTD(tex[MLMF]), new DTD(tex[SBF]), new DTD(tex[PICS], 5, -3)};
		items.icons[0][5] = ic6;

		Icon ic12 = new Icon();
		ic12.normal = new DTD[]{new DTD(tex[ML]), new DTD(tex[PIC], 5, -3)};
		ic12.sel = new DTD[]{new DTD(tex[MLS]), new DTD(tex[SB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][0] = ic12;
		Icon ic32 = new Icon();
		ic32.sel = new DTD[]{new DTD(tex[MLS]), new DTD(tex[AB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][1] = ic32;
		Icon ic22 = new Icon();
		ic22.normal = new DTD[]{new DTD(tex[MLKO]), new DTD(tex[PIC], 5, -3)};
		ic22.sel = new DTD[]{new DTD(tex[MLSKO]), new DTD(tex[SB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][2] = ic22;
		Icon ic42 = new Icon();
		ic42.sel = new DTD[]{new DTD(tex[MLSKO]), new DTD(tex[AB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][3] = ic42;
		Icon ic52 = new Icon();
		ic52.normal = new DTD[]{new DTD(tex[MLM]), new DTD(tex[MB]), new DTD(tex[PIC], 5, -3)};
		ic52.sel = new DTD[]{new DTD(tex[MLM]), new DTD(tex[SB]), new DTD(tex[MB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][4] = ic52;
		Icon ic62 = new Icon();
		ic62.sel = new DTD[]{new DTD(tex[MLM]), new DTD(tex[SB]), new DTD(tex[PICS], 5, -3)};
		items.icons[1][5] = ic62;
		items.setChildren(itemicon, itemhpbar, itemstatus, itemgender, itemextra);

		backbutton = new Cell();
		backbutton.x = 200;
		backbutton.y = 228;
		backbutton.w = 56;
		backbutton.h = 24;
		Icon bbi = new Icon();
		bbi.normal = new DTD[]{new DTD(tex[BB])};
		bbi.sel = new DTD[]{new DTD(tex[BBA])};
		bbi.anim = new DTD[][]{{new DTD(tex[BB])}, {new DTD(tex[BBA])}, {new DTD(tex[BB])}};
		bbi.duration = 16;
		bbi.frames_a = new int[]{5, 12};
		bbi.sel_anim = new DTD[][]{{new DTD(tex[BBA])}, {new DTD(tex[BB])}, {new DTD(tex[BBA])}};
		bbi.sel_duration = 16;
		bbi.frames_sa = new int[]{5, 12};
		backbutton.icon = bbi;
		backbutton.addText(28, 4, 0, 0x15, Text.TEXT_MIDDLE);

		menubackbutton = new Cell();
		menubackbutton.x = 201;
		menubackbutton.y = 219;
		menubackbutton.w = 54;
		menubackbutton.h = 37;
		Icon mbbi = new Icon();
		mbbi.normal = new DTD[]{new DTD(tex[MBB])};
		mbbi.sel = new DTD[]{new DTD(tex[MBBS])};
		mbbi.anim = new DTD[][]{{new DTD(tex[MBB])}, {new DTD(tex[MBBS])}, {new DTD(tex[MBB])}};
		mbbi.duration = 16;
		mbbi.frames_a = new int[]{5, 12};
		mbbi.sel_anim = new DTD[][]{{new DTD(tex[MBBS])}, {new DTD(tex[MBB])}, {new DTD(tex[MBBS])}};
		mbbi.sel_duration = 16;
		mbbi.frames_sa = new int[]{5, 12};
		menubackbutton.icon = mbbi;
		menubackbutton.addText(27, 10, 2, 0xF, Text.TEXT_MIDDLE);

		menuitems = new Cell();
		menuitems.x = 129;
		menuitems.y = 64;
		menuitems.w = 126;
		menuitems.h = 69;
		menuitems.arangement = Cell.COLLUMN;
		menuitems.y_row = 32;
		Icon mii = new Icon();
		mii.normal = new DTD[]{new DTD(tex[MO])};
		mii.sel = new DTD[]{new DTD(tex[MOS])};
		mii.anim = new DTD[][]{{new DTD(tex[MO])}, {new DTD(tex[MOS])}, {new DTD(tex[MO])}};
		mii.duration = 16;
		mii.frames_a = new int[]{5, 12};
		mii.sel_anim = new DTD[][]{{new DTD(tex[MOS])}, {new DTD(tex[MO])}, {new DTD(tex[MOS])}};
		mii.sel_duration = 16;
		mii.frames_sa = new int[]{5, 12};
		menuitems.icon = mii;
		menuitems.addText(7, 5, 2, 0xF, Text.TEXT_LEFT);

		menuattacks = new Cell();
		menuattacks.x = 1;
		menuattacks.y = 64;
		menuattacks.w = 126;
		menuattacks.h = 69;
		menuattacks.arangement = Cell.COLLUMN;
		menuattacks.y_row = 32;
		Icon mai = new Icon();
		mai.normal = new DTD[]{new DTD(tex[MO])};
		mai.sel = new DTD[]{new DTD(tex[MOS])};
		mai.anim = new DTD[][]{{new DTD(tex[MO])}, {new DTD(tex[MOS])}, {new DTD(tex[MO])}};
		mai.duration = 16;
		mai.frames_a = new int[]{5, 12};
		mai.sel_anim = new DTD[][]{{new DTD(tex[MOS])}, {new DTD(tex[MO])}, {new DTD(tex[MOS])}};
		mai.sel_duration = 16;
		mai.frames_sa = new int[]{5, 12};
		menuattacks.icon = mai;
		menuattacks.addText(7, 5, 2, 0xF, Text.TEXT_LEFT);

		infoicon = new Cell();
		infoicon.x = 20;
		infoicon.y = 10;
		infoicon.w = 24;
		infoicon.h = 24;

		infostatus = new Cell();
		infostatus.x = 34;
		infostatus.y = 29;
		infostatus.w = 20;
		infostatus.h = 8;
		infostatus.icon_type = Cell.MULTIICON;
		infostatus.starts = new int[]{0};
		infostatus.icons = new Icon[1][6];
		for (int i = 0; i < infostatus.icons[0].length; i++) {
			infostatus.icons[0][i] = new Icon(tex[STATUSCD + i], 0, 0);
		}

		infoitemtitle = new Cell();
		infoitemtitle.x = 0;
		infoitemtitle.y = 0;
		infoitemtitle.icon_type = Cell.TEXTONLY;
		infoitemtitle.addText(139, 9, 0, 0x1A, Text.TEXT_LEFT);

		infoitem = new Cell();
		infoitem.x = 0;
		infoitem.y = 0;
		infoitem.icon_type = Cell.TEXTONLY;
		infoitem.addText(139, 29, 0, 0x1A, Text.TEXT_LEFT);

		// TODO needs animation dependent text and child positions
		infobox = new Cell();
		infobox.x = 0;
		infobox.y = 207;
		infobox.w = 256;
		infobox.h = 49;
		infobox.arangement = Cell.COLLUMN;
		infobox.y_row = 40;
		infobox.icon = new Icon(tex[IB], 0, 0);
		infobox.addText(56, 9, 0, 0x1A, Text.TEXT_LEFT);
		infobox.setChildren(infoicon, infostatus, infoitemtitle, infoitem);

		whatdo = new Cell();
		whatdo.x = 0;
		whatdo.y = 208;
		whatdo.w = 144;
		whatdo.h = 48;
		whatdo.icon_type = Cell.TEXTBOX;
		whatdo.generateBoxText(1, 1);

		choose = new Cell();
		choose.x = 0;
		choose.y = 224;
		choose.w = 200;
		choose.h = 32;
		choose.icon_type = Cell.TEXTBOX;
		choose.generateBoxText(1, 1);

		moveto = new Cell();
		moveto.x = 0;
		moveto.y = 224;
		moveto.w = 200;
		moveto.h = 32;
		moveto.icon_type = Cell.TEXTBOX;
		moveto.generateBoxText(1, 1);
	}

	/** Adds the given pokemon to the team and returns whether it was successful. */
	public boolean addPokemon(Pokemon p) {
		for (int i = 0; i < team.length; i++) {
			if (team[i] == null) {
				team[i] = p.extend();
				return true;
			}
		}
		return false;
	}

	/** Removes the pokemon at the given position and returns it. */
	public ExtPokemon removePokemon(int pos) {
		ExtPokemon p = team[pos];
		team[pos] = null;
		return p;
	}

	/** Returns the position the given pokemon was found, -1 if not found. */
	public int containsPokemon(Pokemon p) {
		for (int i = 0; i < team.length; i++) {
			if (containsPokemonAt(p, i)) {
				return i;
			}
		}
		return -1;
	}

	/** Checks the given position and compares the given pokemon with the found one, ignoring all fields that are set to standard values. */
	public boolean containsPokemonAt(Pokemon p, int pos) {
		if (team[pos] == p) {
			return true;
		}
		if (team[pos] != null) {
			// TODO check all fields of p, skip if set to default
		}
		return false;
	}

	/** Returns whether the team has a free slot for an extra pokemon. */
	public boolean hasFreeSlot() {
		for (ExtPokemon team1 : team) {
			if (team1 == null) {
				return true;
			}
		}
		return false;
	}

	/** Updates the team and processes events and key-presses. */
	@Override
	public void update(Game g) {
		int teamSize = SystemControll.getVariable(SystemControll.TeamSize);
		if (teamSize != team.length) {
			ExtPokemon[] tmp = new ExtPokemon[teamSize];
			int i;
			for (i = 0; i < team.length & i < teamSize; i++) {
				tmp[i] = team[i];
			}
			for (; i < tmp.length; i++) {
				if (team[i] != null) {
					Logger.add(Logger.TEAM, "New team size is too small, deleting Pokemon: \"", team[i].name, "\" !");
				}
			}
			team = tmp;
			// TODO evtl. change size related graphics
		}
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.TEAM_OPEN)) {
					clickClose = false;
					vfx.addShutter(Direction.NORTH, true);
					selection = 0;
					currentPokemon = 0;
					status = STARTUP;
				}
				// TODO check for other messages
				break;
			case STARTUP:
				if (vfx.shutterFinished()) {
					status = POKEMONSELECTION;
				}
				break;
			case SHUTDOWN:
				if (vfx.shutterFinished()) {
					status = CLOSED;
					MessageHandler.add(MessageHandler.TEAM_CLOSE, clickClose);
				}
				break;
			case POKEMONSELECTION:
				if (items.animationRunning || backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWN;
					} else if (items.animationFinished()) {
						selection = 2;
						status = POKEMONOPTIONS;
					}
					break;
				}
				int columns = items.columns;
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					backbutton.animate(selection == 1);
					selection = 1;
					break;
				}
				if (selection == 0) {
					if (team.length == 0) {
						selection = 1;
						break;
					}
					int row = currentPokemon / columns;
					int column = currentPokemon % columns;
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						items.animate(currentPokemon, true);
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						if (column != 0) {
							currentPokemon--;
						}
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						if (column != columns - 1 && currentPokemon + 1 < team.length && team[currentPokemon + 1] != null) {
							currentPokemon++;
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (row != 0) {
							currentPokemon -= columns;
						} else {
							selection = 1;
						}
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (currentPokemon + columns < team.length && team[currentPokemon + columns] != null) {
							currentPokemon += columns;
						} else {
							selection = 1;
						}
						break;
					}
				} else if (selection == 1) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						backbutton.animate(true);
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (team.length != 0) {
							selection = 0;
							// TODO find last pokemon
							currentPokemon = team.length - 1;
						}
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (team.length != 0) {
							selection = 0;
							currentPokemon = 0;
						}
						break;
					}
				}
				selectionClick(g);
				break;
			case POKEMONOPTIONS:
				if (menubackbutton.animationRunning || menuitems.animationRunning || menuattacks.animationRunning) {
					if (menubackbutton.animationFinished()) {
						if (selection == 12) {
							selection = 4;
						} else {
							selection = 0;
							status = POKEMONSELECTION;
						}
					} else if (menuitems.animationFinished() || menuattacks.animationFinished()) {
						// TODO rearrange selection to allow more than 4 attacks
						if (selection == 2) {
							selection = 0;
							vfx.addShutter(Direction.NORTH, false);
							status = SUMMARY;
						} else if (selection == 3) {
							selection = 0;
							moveFrom = currentPokemon;
							status = MOVEPOKEMON;
						} else if (selection == 4) {
							selection = 9;
						} else if (selection >= 5 && selection <= 8) {
							int attack = team[currentPokemon].getFieldAttackNr(selection - 5);
							//MessageHandler.add("Fieldattack", attack);
						} else if (selection == 9) {
							selection = 0;
							status = POKEMONSELECTION;
							MessageHandler.add(MessageHandler.EVENT, Interpreter.getNewInterpreter(EventMethodLibrary.callScript("team/item_give"), EventMethodLibrary.current(), team[currentPokemon]));
						} else if (selection == 10) {
							selection = 0;
							status = POKEMONSELECTION;
							MessageHandler.add(MessageHandler.EVENT, Interpreter.getNewInterpreter(EventMethodLibrary.callScript("team/item_take"), EventMethodLibrary.current(), team[currentPokemon]));
						} else if (selection == 11) {
							//MessageHandler.add("Use item", null);
							selection = 0;
							status = USEITEM;
						}
					}
					break;
				}
				int max = team[currentPokemon].getFieldAttackCount();
				if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					if (selection == 1) {
						menubackbutton.animate(true);
					} else if (selection >= 5 && selection <= 8) {
						menuattacks.animate(selection - 5, true);
					} else if (selection >= 2 && selection <= 4) {
						menuitems.animate(selection - 2, true);
					} else if (selection >= 9 && selection <= 11) {
						menuitems.animate(selection - 9, true);
					} else {
						menubackbutton.animate(true);
					}
					break;
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					menubackbutton.animate(true);
					if (selection >= 9) {
						selection = 12;
					} else {
						selection = 1;
					}
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					if (selection >= 2 && selection <= 4 && max != 0) {
						selection = Math.min(selection + 3, max + 4);
					}
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					if (selection >= 5 && selection <= 8) {
						selection = Math.min(selection - 3, 4);
					}
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection >= 3 && selection <= 4 || selection >= 6 && selection <= 8 || selection >= 10 && selection <= 12) {
						selection--;
					} else if (selection == 1) {
						selection = 4;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection >= 2 && selection <= 3 || selection >= 5 && selection < max + 4 || selection >= 9 && selection <= 11) {
						selection++;
					} else if (selection == 4 || selection == max + 4) {
						selection = 1;
					}
					break;
				}
				optionClick(g);
				break;
			case MOVEPOKEMON:
				if (items.animationRunning || backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						status = POKEMONSELECTION;
					} else if (items.animationFinished()) {
						ExtPokemon tmp = team[moveFrom];
						team[moveFrom] = team[currentPokemon];
						team[currentPokemon] = tmp;
						status = POKEMONSELECTION;
					}
					break;
				}
				columns = items.columns;
				int row = currentPokemon / columns;
				int column = currentPokemon % columns;
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					backbutton.animate(selection == 1);
					break;
				} else if (selection == 0) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						items.animate(currentPokemon, true);
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						if (column != 0) {
							currentPokemon--;
						}
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						if (column != columns - 1 && currentPokemon + 1 < team.length && team[currentPokemon + 1] != null) {
							currentPokemon++;
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (row != 0) {
							currentPokemon -= columns;
						} else {
							selection = 1;
						}
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (currentPokemon + columns < team.length && team[currentPokemon + columns] != null) {
							currentPokemon += columns;
						} else {
							selection = 1;
						}
						break;
					}
				} else if (selection == 1) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						backbutton.animate(true);
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						selection = 0;
						// TODO find last pokemon
						currentPokemon = team.length - 1;
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						selection = 0;
						currentPokemon = 0;
						break;
					}
				}
				moveClick(g);
				break;
			case USEITEM:
				// TODO
				break;
			case SUMMARY:
				if (vfx.shutterFinished()) {
					summary.team = team;
					summary.currentPokemon = currentPokemon;
					MessageHandler.add(MessageHandler.SUMMARY_OPEN, null);
					status = CLOSED;
				}
				break;
		}
	}

	/** Processes clicking while choosing the options for a pokemon. */
	private void optionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		if (selection < 9) {
			int sel = menuitems.clickMulti(g.getMouseX(), g.getMouseY(), 3);
			if (sel != -1 && !(sel == 2 && team[currentPokemon].isEgg())) {
				g.processMouse();
				menuitems.animate(sel, true);
				selection = sel + 2;
				return;
			}
			int max = team[currentPokemon].getFieldAttackCount();
			sel = menuattacks.clickMulti(g.getMouseX(), g.getMouseY(), max);
			if (sel != -1) {
				g.processMouse();
				menuattacks.animate(sel, true);
				selection = sel + 5;
				return;
			}
		} else {
			int sel = menuitems.clickMulti(g.getMouseX(), g.getMouseY(), 3);
			if (sel != -1) {
				g.processMouse();
				menuitems.animate(sel, true);
				selection = sel + 9;
				return;
			}
		}
		if (menubackbutton.click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			menubackbutton.animate(true);
			if (selection >= 9) {
				selection = 12;
			} else {
				selection = 1;
			}
		}
	}

	/** Processes clicking while moving a pokemon. */
	private void moveClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}

		int x = g.getMouseX();
		int y = g.getMouseY();
		int sel = items.clickMulti(x, y, team.length);
		if (sel != -1 && team[sel] != null) {
			g.processMouse();
			items.animate(sel, true);
			currentPokemon = sel;
			selection = 0;
			return;
		}
		if (backbutton.click(x, y)) {
			g.processMouse();
			selection = 1;
			backbutton.animate(true);
		}
	}

	/** Processes clicking while selecting a pokemon. */
	private void selectionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		int x = g.getMouseX();
		int y = g.getMouseY();
		int sel = items.clickMulti(x, y, team.length);
		if (sel != -1 && team[sel] != null) {
			g.processMouse();
			items.animate(sel, true);
			currentPokemon = sel;
			selection = 0;
			return;
		}
		if (backbutton.click(x, y)) {
			g.processMouse();
			selection = 1;
			backbutton.animate(true);
			clickClose = true;
		}
	}

	/** Draws the left section of the selection. */
	@Override
	public void drawL() {
		switch (status) {
			case STARTUP:
			case SHUTDOWN:
			case POKEMONSELECTION:
			case POKEMONOPTIONS:
			case MOVEPOKEMON:
			case SUMMARY:
				drawSelectionBackgroundL();
				drawInfoBox();

				break;
			case USEITEM:

				break;
		}
	}

	/** Draws the right section of the selection. */
	@Override
	public void drawR() {
		switch (status) {
			case STARTUP:
			case SHUTDOWN:
			case SUMMARY:
			case POKEMONSELECTION:
				drawSelectionBackground();
				drawSelection();
				drawWhatToChoose();
				drawBackButton();
				break;
			case POKEMONOPTIONS:
				drawSelectionBackground();
				drawSelection();
				drawOptions();
				drawOptionDesc();
				drawOptionsBackButton();
				break;
			case MOVEPOKEMON:
				drawSelectionBackground();
				drawSelection();
				drawMoveToWhere();
				drawBackButton();
				break;
			case USEITEM:

				break;
		}
	}

	private void drawOptionsBackButton() {
		menubackbutton.draw(Localisation.getTeamOptionsBackButton(), selection == 1 || selection == 12);
	}

	private void drawMoveToWhere() {
		moveto.draw(Localisation.getTeamMove());
	}

	private void drawWhatToChoose() {
		choose.draw(Localisation.getTeamChoose());
	}

	private void drawOptionDesc() {
		whatdo.draw(ConV.sprintf(Localisation.getTeamOptionInfo(), team[currentPokemon].getName()));
	}

	/** Draws the option menu. */
	private void drawOptions() {
		char[][] names = Localisation.getTeamOptionNames();
		if (selection < 9) {
			for (int i = 0; i < 3; i++) {
				if (i == 2 && team[currentPokemon].isEgg()) {
					return;
				}
				menuitems.drawMulti(names[i], i, selection == i + 2);
			}
			int max = team[currentPokemon].getFieldAttackCount();
			for (int i = 0; i < max; i++) {
				char[] name = Localisation.getAttackName(team[currentPokemon].getFieldAttackNr(i));
				menuattacks.drawMulti(name, i, selection == i + 5);
			}
		} else {
			for (int i = 0; i < 3; i++) {
				menuitems.drawMulti(names[i + 3], i, selection == i + 9);
			}
		}
	}

	/** Draws the back button. */
	private void drawBackButton() {
		backbutton.draw(Localisation.getTeamBackButton(), selection == 1);
	}

	/** Draws the pokemon selection. */
	private void drawSelection() {
		boolean showsel = status != SUMMARY && status != SHUTDOWN && status != STARTUP && (selection == 0 || status == POKEMONOPTIONS);
		for (int i = 0; i < team.length; i++) {
			ExtPokemon epk = team[i];
			if (epk == null) {
				continue;
			}
			boolean selected = currentPokemon == i && showsel;
			int t_base;
			if (moveFrom == i && status == MOVEPOKEMON) {
				t_base = 4;
			} else if (currentPokemon == i && selection == 0 && status == MOVEPOKEMON) {
				t_base = 5;
			} else {
				if (epk.status == StatusCondition.KO) {
					t_base = 2;
				} else {
					t_base = 0;
				}
				if (selected && status == POKEMONOPTIONS) {
					t_base++;
				}
			}
			items.drawMulti(epk.getName(), i, t_base, selected);
			int t_icon = GraphicHandler.getPokemonIcon(epk, 0);
			// TODO different animation depending on status
			// KO: not moving
			// low HP: moving slowly
			// selected: additional movement
			// status condition: different (sideways) movement
			// egg stages?
			items.getChildMulti(0, i).draw(new Icon(t_icon, 0, 0), null, false);

			if (epk.isEgg()) {
				continue;
			}

			int max_hp = epk.stats[Statistic.HEALTH.getID()];

			assert (max_hp <= 999);

			items.getChildMulti(1, i).draw(ConV.sprintf(Localisation.getTeamHP(), epk.hp, max_hp), t_base / 2, false);

			Cell hp_bar = items.getChildMulti(1, i).getChild(0);
			int t_hp;
			if (epk.hp * 2 > max_hp) {
				t_hp = 0;
			} else if (epk.hp * 5 >= max_hp) {
				t_hp = 1;
			} else {
				t_hp = 2;
			}
			int width = epk.hp * hp_bar.columns / max_hp;
			if (width == 0 && epk.hp != 0) {
				width = 1;
			}
			for (int j = 0; j < width; j++) {
				hp_bar.drawMulti(null, j, t_hp, false);
			}

			if (epk.status != StatusCondition.OK) {
				items.getChildMulti(2, i).draw(epk.status.getID(), false);
			} else {
				items.getChildMulti(2, i).draw(ConV.sprintf(Localisation.getTeamLevel(), epk.level), 0, false);
			}

			if (epk.gender == Gender.MALE) {
				items.getChildMulti(3, i).draw(0, false);
			} else if (epk.gender == Gender.FEMALE) {
				items.getChildMulti(3, i).draw(1, false);
			}

			if (epk.itemid != 0) {
				items.getChildMulti(4, i).draw(0, false);
			}
			// TODO check for ball capsules and letters

		}
	}

	/** Draws the background for the pokemon selection. */
	private void drawSelectionBackground() {
		backgroudR.draw();
	}

	private void drawSelectionBackgroundL() {
		backgroudL.draw();
	}

	private void drawInfoBox() {
		ExtPokemon epk;
		if (status == MOVEPOKEMON) {
			epk = team[moveFrom];
		} else if (status == POKEMONOPTIONS) {
			epk = team[currentPokemon];
		} else {
			infobox.drawMulti(null, 1, false);
			return;
		}
		char[] name = epk.getName();
		infobox.drawMulti(name, 0, false);
		if (epk.status != StatusCondition.OK) {
			infobox.getChildMulti(1, 0).draw(epk.status.getID() - 1, false);
		}

		infobox.getChildMulti(0, 0).draw(new Icon(GraphicHandler.getPokemonIcon(epk, 0), 0, 0), null, false);
		infobox.getChildMulti(2, 0).draw(Localisation.getTeamItemTitle());
		infobox.getChildMulti(3, 0).draw(Localisation.getItemName(epk.itemid));

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

	public void initSelection() {
		selection = 0;
		currentPokemon = 0;
	}

	public void drawRSelection() {
		// TODO some way to switch between normal and filtered (UNABLE, ABLE, LEARNED)
		// TODO filter how? (HM, TM, evolution stones)
		// TODO needs a per pokemon list of lernable attacks
		// TODO put list of pokemon in evolution stones script
		drawSelectionBackground();
		drawSelection();
		drawWhatToChoose();
		drawBackButton();
	}

	public void drawLSelection() {
		drawSelectionBackgroundL();
		drawInfoBox();
	}

	public boolean updateSelection(Game g) {
		if (items.animationRunning || backbutton.animationRunning) {
			if (backbutton.animationFinished()) {
				SystemControll.setFlag(SystemControll.QuestionAnswer, false);
				return true;
			} else if (items.animationFinished()) {
				SystemControll.setFlag(SystemControll.QuestionAnswer, true);
				// TODO should be SystemControll
				EventControll.setPokemon(0, team[currentPokemon]);
				return true;
			}
			return false;
		}
		int columns = items.columns;
		if (g.wasPressed(Button.B)) {
			g.process(Button.B);
			backbutton.animate(selection == 1);
			selection = 1;
			return false;
		}
		if (selection == 0) {
			if (team.length == 0) {
				selection = 1;
				return false;
			}
			int row = currentPokemon / columns;
			int column = currentPokemon % columns;
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				items.animate(currentPokemon, true);
				return false;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				if (column != 0) {
					currentPokemon--;
				}
				return false;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				if (column != columns - 1 && currentPokemon + 1 < team.length && team[currentPokemon + 1] != null) {
					currentPokemon++;
				}
				return false;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				if (row != 0) {
					currentPokemon -= columns;
				} else {
					selection = 1;
				}
				return false;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				if (currentPokemon + columns < team.length && team[currentPokemon + columns] != null) {
					currentPokemon += columns;
				} else {
					selection = 1;
				}
				return false;
			}
		} else if (selection == 1) {
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				backbutton.animate(true);
				return false;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				return false;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				return false;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				if (team.length != 0) {
					selection = 0;
					// TODO find last pokemon
					currentPokemon = team.length - 1;
				}
				return false;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				if (team.length != 0) {
					selection = 0;
					currentPokemon = 0;
				}
				return false;
			}
		}
		selectionClick(g);
		return false;
	}
}
