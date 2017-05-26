package sequenze.battle;

import sequenze.*;

import static sequenze.InstancePackage.team;

import sequenze.menu.Cell;
import sequenze.menu.Icon;
import sequenze.menu.Icon.DTD;
import util.ConV;
import util.Logger;

public class BattleMenu {

	ExtPokemon enemy;
	ExtPokemon friendly;
	int status = CLOSED;
	private static final int CLOSED = 0;
	private static final int MENU = 1;
	private static final int ATTACKS = 2;
	int selection = 0;

	public int[] tex;
	public static final int BUTTONATTACK = 0;
	public static final int BUTTONBAG = 3;
	public static final int BUTTONRUN = 6;
	public static final int BUTTONTEAM = 9;
	public static final int BUTTONCANCEL = 12;
	public static final int BUTTONSEL = 15;
	public static final int BALL = 19;
	public static final int CAUGHT = 23;
	public static final int MALE = 24;
	public static final int FEMALE = 25;
	public static final int HPBAR = 26;
	public static final int HPGREEN = 27;
	public static final int HPYELLOW = 28;
	public static final int HPRED = 29;
	public static final int XPBAR = 30;
	public static final int XP = 31;
	public static final int INFOENEMY = 32;
	public static final int INFOFRIENDLY = 33;
	public static final int STATUS = 34;
	public static final int TYPE = 40;
	public static final int ATTACKBG = 60;
	public static final int ATTACKBGA = 80;
	public static final int ATTACKBGBASE = 100;
	public static final int ATTACKBGEMPTY = 101;

	private Cell menubuttons;
	private Cell buttonattack;
	private Cell buttonbag;
	private Cell buttonrun;
	private Cell buttonteam;
	private Cell buttoncancel;
	private Cell buttonattacks;
	private Cell attacktype;
	private Cell attackpptitle;
	private Cell attackpp;
	private Cell infofriendly;
	private Cell infoenemy;
	private Cell hpbar;
	private Cell hp;
	private Cell xpbar;
	private Cell xp;
	private Cell gender;
	private Cell level;
	private Cell caught;
	private Cell balls;
	private Cell statuscondition;

	public BattleMenu() {
		tex = GraphicHandler.giveTextures("BattleMenu");
		setupCells();
	}

	public void setupCells() {
		buttonattack = new Cell();
		buttonattack.x = 0;
		buttonattack.y = 24;
		buttonattack.w = 256;
		buttonattack.h = 120;
		buttonattack.addText(111, 51, 2, 0x9, Text.TEXT_LEFT);
		Icon bai = new Icon();
		bai.normal = new DTD[]{new DTD(tex[BUTTONATTACK], 24, 16)};
		bai.sel = new DTD[]{new DTD(tex[BUTTONATTACK], 24, 16), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 245, -1), new DTD(tex[BUTTONSEL + 2], -1, 109), new DTD(tex[BUTTONSEL + 3], 245, 109)};
		buttonattack.icon = bai;

		buttonbag = new Cell();
		buttonbag.x = 0;
		buttonbag.y = 144;
		buttonbag.w = 80;
		buttonbag.h = 48;
		buttonbag.addText(40, 17, 2, 0x9, Text.TEXT_MIDDLE);
		Icon bagi = new Icon();
		bagi.normal = new DTD[]{new DTD(tex[BUTTONBAG], 1, 1)};
		bagi.sel = new DTD[]{new DTD(tex[BUTTONBAG], 1, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 69, -1), new DTD(tex[BUTTONSEL + 2], -1, 37), new DTD(tex[BUTTONSEL + 3], 69, 37)};
		buttonbag.icon = bagi;

		buttonrun = new Cell();
		buttonrun.x = 88;
		buttonrun.y = 152;
		buttonrun.w = 80;
		buttonrun.h = 48;
		buttonrun.addText(40, 17, 2, 0x9, Text.TEXT_MIDDLE);
		Icon runi = new Icon();
		runi.normal = new DTD[]{new DTD(tex[BUTTONRUN], 1, 1)};
		runi.sel = new DTD[]{new DTD(tex[BUTTONRUN], 1, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 69, -1), new DTD(tex[BUTTONSEL + 2], -1, 37), new DTD(tex[BUTTONSEL + 3], 69, 37)};
		buttonrun.icon = runi;

		buttonteam = new Cell();
		buttonteam.x = 174;
		buttonteam.y = 144;
		buttonteam.w = 80;
		buttonteam.h = 48;
		buttonteam.addText(40, 17, 2, 0x9, Text.TEXT_MIDDLE);
		Icon teami = new Icon();
		teami.normal = new DTD[]{new DTD(tex[BUTTONTEAM], 1, 1)};
		teami.sel = new DTD[]{new DTD(tex[BUTTONTEAM], 1, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 69, -1), new DTD(tex[BUTTONSEL + 2], -1, 37), new DTD(tex[BUTTONSEL + 3], 69, 37)};
		buttonteam.icon = teami;

		menubuttons = new Cell();
		menubuttons.x = 0;
		menubuttons.y = 0;
		menubuttons.arangement = Cell.CHILDREN;
		menubuttons.setChildren(buttonattack, buttonteam, buttonbag, buttonrun);

		attacktype = new Cell();
		attacktype.x = 16;
		attacktype.y = 30;
		attacktype.w = 32;
		attacktype.h = 14;
		attacktype.icon_type = Cell.MULTIICON;
		attacktype.starts = new int[]{0};
		attacktype.icons = new Icon[1][20];
		for (int i = 0; i < attacktype.icons[0].length; i++) {
			attacktype.icons[0][i] = new Icon(tex[TYPE + i], 0, 0);
		}

		attackpptitle = new Cell();
		attackpptitle.x = 0;
		attackpptitle.y = 0;
		attackpptitle.icon_type = Cell.TEXTONLY;
		attackpptitle.addText(65, 29, 0, 0x1C, Text.TEXT_MIDDLE);

		attackpp = new Cell();
		attackpp.x = 0;
		attackpp.y = 0;
		attackpp.icon_type = Cell.TEXTONLY;
		attackpp.addText(89, 30, 0, 0x1C, Text.TEXT_SLASH);

		buttonattacks = new Cell();
		buttonattacks.x = 0;
		buttonattacks.y = 21;
		buttonattacks.w = 128;
		buttonattacks.h = 56;
		buttonattacks.arangement = Cell.GRID;
		buttonattacks.columns = 2;
		buttonattacks.x_col = 128;
		buttonattacks.y_row = 64;
		buttonattacks.icon_type = Cell.MULTIICON;
		buttonattacks.addText(64, 11, 2, 0x8, Text.TEXT_MIDDLE);
		buttonattacks.starts = new int[]{0};
		buttonattacks.icons = new Icon[1][21];
		Icon basei = new Icon();
		basei.normal = new DTD[]{new DTD(tex[ATTACKBGEMPTY], 1, 1)};
		basei.sel = new DTD[]{new DTD(tex[ATTACKBGEMPTY], 1, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 117, -1), new DTD(tex[BUTTONSEL + 2], -1, 45), new DTD(tex[BUTTONSEL + 3], 117, 45)};
		buttonattacks.icons[0][0] = basei;
		for (int i = 0; i < buttonattacks.icons[0].length - 1; i++) {
			Icon basi = new Icon();
			basi.normal = new DTD[]{new DTD(tex[ATTACKBG + i], 2, 1)};
			basi.sel = new DTD[]{new DTD(tex[ATTACKBG + i], 2, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 117, -1), new DTD(tex[BUTTONSEL + 2], -1, 45), new DTD(tex[BUTTONSEL + 3], 117, 45)};
			basi.sel_duration = 32;
			basi.frames_sa = new int[]{16};
			basi.sel_anim = new DTD[][]{basi.sel, {new DTD(tex[ATTACKBGA + i], 2, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 117, -1), new DTD(tex[BUTTONSEL + 2], -1, 45), new DTD(tex[BUTTONSEL + 3], 117, 45)}};
			buttonattacks.icons[0][i + 1] = basi;
		}
		buttonattacks.setChildren(attacktype, attackpptitle, attackpp);

		buttoncancel = new Cell();
		buttoncancel.x = 8;
		buttoncancel.y = 152;
		buttoncancel.w = 240;
		buttoncancel.h = 48;
		buttoncancel.addText(120, 17, 2, 0x9, Text.TEXT_MIDDLE);
		Icon canceli = new Icon();
		canceli.normal = new DTD[]{new DTD(tex[BUTTONCANCEL], 1, 1)};
		canceli.sel = new DTD[]{new DTD(tex[BUTTONCANCEL], 1, 1), new DTD(tex[BUTTONSEL], -1, -1), new DTD(tex[BUTTONSEL + 1], 229, -1), new DTD(tex[BUTTONSEL + 2], -1, 37), new DTD(tex[BUTTONSEL + 3], 229, 37)};
		buttoncancel.icon = canceli;

		gender = new Cell();
		gender.x = 66;
		gender.y = 9;
		gender.w = 6;
		gender.h = 9;
		gender.icon_type = Cell.MULTIICON;
		gender.starts = new int[]{0};
		gender.icons = new Icon[1][3];
		gender.icons[0][0] = new Icon(tex[MALE], 0, 0);
		gender.icons[0][1] = new Icon(tex[FEMALE], 0, 0);
		gender.icons[0][2] = new Icon();

		level = new Cell();
		level.x = 0;
		level.y = 0;
		level.icon_type = Cell.TEXTONLY;
		level.addText(73, 5, 4, 0x2, Text.TEXT_LEFT);

		statuscondition = new Cell();
		statuscondition.x = 11;
		statuscondition.y = 22;
		statuscondition.w = 22;
		statuscondition.h = 8;
		statuscondition.icon_type = Cell.MULTIICON;
		statuscondition.starts = new int[]{0};
		statuscondition.icons = new Icon[1][StatusCondition.length()];
		statuscondition.icons[0][0] = new Icon();
		for (int i = 1; i < statuscondition.icons[0].length; i++) {
			statuscondition.icons[0][i] = new Icon(tex[STATUS + i - 1], 0, 0);
		}

		hp = new Cell();
		hp.x = 16;
		hp.y = 2;
		hp.w = 1;
		hp.h = 3;
		hp.arangement = Cell.ROW;
		// TODO store this differently, will be lost
		hp.columns = 48;
		hp.x_col = 1;
		hp.icon_type = Cell.MULTIICON;
		hp.starts = new int[]{0};
		hp.icons = new Icon[1][3];
		hp.icons[0][0] = new Icon(tex[HPGREEN], 0, 0);
		hp.icons[0][1] = new Icon(tex[HPYELLOW], 0, 0);
		hp.icons[0][2] = new Icon(tex[HPRED], 0, 0);

		hpbar = new Cell();
		hpbar.x = 34;
		hpbar.y = 22;
		hpbar.w = 66;
		hpbar.h = 8;
		hpbar.addText(32, 3, 4, 0x2, Text.TEXT_SLASH);
		hpbar.icon = new Icon(tex[HPBAR], 0, 0);
		hpbar.setChildren(hp);

		xp = new Cell();
		xp.x = 3;
		xp.y = 2;
		xp.w = 1;
		xp.h = 2;
		xp.arangement = Cell.ROW;
		// TODO store this differently, will be lost
		xp.columns = 96;
		xp.x_col = 1;
		xp.icon = new Icon(tex[XP], 0, 0);

		xpbar = new Cell();
		xpbar.x = -1;
		xpbar.y = 39;
		xpbar.w = 102;
		xpbar.h = 7;
		xpbar.icon = new Icon(tex[XPBAR], 0, 0);
		xpbar.setChildren(xp);

		infofriendly = new Cell();
		infofriendly.x = 150;
		infofriendly.y = 93;
		infofriendly.w = 128;
		infofriendly.h = 43;
		infofriendly.addText(2, 6, 0, 0xC, Text.TEXT_LEFT);
		Icon ifi = new Icon();
		ifi.normal = new DTD[]{new DTD(tex[INFOFRIENDLY], -22, -1)};
		infofriendly.icon = ifi;
		infofriendly.setChildren(gender, level, statuscondition, hpbar, xpbar);

		caught = new Cell();
		caught.x = 2;
		caught.y = 22;
		caught.w = 7;
		caught.h = 7;
		caught.icon = new Icon(tex[CAUGHT], 0, 0);

		infoenemy = new Cell();
		infoenemy.x = 0;
		infoenemy.y = 18;
		infoenemy.w = 122;
		infoenemy.h = 35;
		infoenemy.addText(2, 6, 0, 0xC, Text.TEXT_LEFT);
		infoenemy.icon = new Icon(tex[INFOENEMY], 0, 0);
		infoenemy.setChildren(gender, level, statuscondition, hpbar, caught);

		balls = new Cell();
		balls.x = 4;
		balls.y = 5;
		balls.w = 16;
		balls.h = 16;
		balls.arangement = Cell.ROW;
		balls.x_col = 19;
		balls.icon_type = Cell.MULTIICON;
		balls.starts = new int[]{0};
		balls.icons = new Icon[1][4];
		for (int i = 0; i < balls.icons[0].length; i++) {
			balls.icons[0][i] = new Icon(tex[BALL + i], 0, 0);
		}
	}

	public void start(ExtPokemon enemy, ExtPokemon team) {
		this.enemy = enemy;
		this.friendly = team;
		status = MENU;
		selection = -1;
	}

	public boolean isFinished() {
		return status == CLOSED;
	}

	public void drawL() {
		switch (status) {
			case MENU:
			case ATTACKS:
				infofriendly.draw(friendly.getName());
				if (friendly.gender != Gender.IRRELEVANT) {
					infofriendly.getChild(0).draw(friendly.gender.getID());
				}
				infofriendly.getChild(1).draw(ConV.sprintf("[Lv]\\s0000".toCharArray(), friendly.level));
				infofriendly.getChild(2).draw(friendly.status.getID());
				int max_hp = friendly.stats[Statistic.HEALTH.getID()];
				assert (max_hp <= 999);
				infofriendly.getChild(3).draw(ConV.sprintf("\\s0000/\\s0001".toCharArray(), friendly.hp, max_hp));
				Cell hp_bar = infofriendly.getChild(3).getChild(0);
				int t_hp;
				if (friendly.hp * 2 > max_hp) {
					t_hp = 0;
				} else if (friendly.hp * 5 >= max_hp) {
					t_hp = 1;
				} else {
					t_hp = 2;
				}
				int width = friendly.hp * hp_bar.columns / max_hp;
				if (width == 0 && friendly.hp != 0) {
					width = 1;
				}
				for (int j = 0; j < width; j++) {
					hp_bar.drawMulti(null, j, t_hp, false);
				}
				int diff = 0;
				if (friendly.level != Pokemon.LEVELMAX) {
					diff = friendly.xptype.getXPDifference(friendly.level);
				}
				width = xp.columns;
				if (friendly.level != Pokemon.LEVELMAX) {
					width = width * (diff + friendly.currentep - friendly.nextlevelep) / diff;
				}
				Cell experiencebar = infofriendly.getChild(4);
				experiencebar.draw();
				experiencebar.getChild(0).drawMulti(width, -1);

				infoenemy.draw(enemy.getName());
				if (enemy.gender != Gender.IRRELEVANT) {
					infoenemy.getChild(0).draw(enemy.gender.getID());
				}
				infoenemy.getChild(1).draw(ConV.sprintf("[Lv]\\s0000".toCharArray(), enemy.level));
				infoenemy.getChild(2).draw(enemy.status.getID());
				max_hp = enemy.stats[Statistic.HEALTH.getID()];
				assert (max_hp <= 999);
				infoenemy.getChild(3).draw();
				hp_bar = infoenemy.getChild(3).getChild(0);
				if (enemy.hp * 2 > max_hp) {
					t_hp = 0;
				} else if (enemy.hp * 5 >= max_hp) {
					t_hp = 1;
				} else {
					t_hp = 2;
				}
				width = enemy.hp * hp_bar.columns / max_hp;
				if (width == 0 && enemy.hp != 0) {
					width = 1;
				}
				for (int j = 0; j < width; j++) {
					hp_bar.drawMulti(null, j, t_hp, false);
				}
				infoenemy.getChild(4).draw();
				break;
		}
	}

	public void drawR() {
		switch (status) {
			case MENU:
				char[][] text = {"FIGHT".toCharArray(), "POKéMON".toCharArray(), "BAG".toCharArray(), "RUN".toCharArray()};
				menubuttons.drawMulti(text, 4, selection);
				drawBalls();
				break;
			case ATTACKS:
				for (int i = 0; i < Pokemon.ATTACKS; i++) {
					int attack = friendly.attackids[i];
					if (attack == 0) {
						buttonattacks.drawMulti(null, i, 0, selection == i);
						continue;
					}
					buttonattacks.drawMulti(Localisation.getAttackName(attack), i, friendly.attacktypes[i].getID() + 1, selection == i);
					buttonattacks.getChildMulti(0, i).draw(friendly.attacktypes[i].getID());

					int pp = friendly.pps[i];
					int maxpp = friendly.maxpps[i];

					char[][] titles = new char[][]{"PP".toCharArray(), "\\c1DPP".toCharArray(), "\\c1EPP".toCharArray(), "\\c1FPP".toCharArray()};
					int titlei = 0;
					if (pp == 0) {
						titlei = 3;
					} else if (maxpp / 4 >= pp) {
						titlei = 2;
					} else if (maxpp / 2 >= pp) {
						titlei = 1;
					}
					buttonattacks.getChildMulti(1, i).draw(titles[titlei]);

					assert (maxpp <= 99);
					//TODO string formatting
					char[] s1 = new char[2];
					s1[0] = pp >= 10 ? (char) ('０' + pp / 10) : ' ';
					s1[1] = (char) ('０' + pp % 10);
					char[] s2 = new char[2];
					s2[0] = maxpp >= 10 ? (char) ('０' + maxpp / 10) : ' ';
					s2[1] = (char) ('０' + maxpp % 10);
					char[][] pps = new char[][]{"\\s0000／\\s0001".toCharArray(), "\\c1D\\s0000／\\s0001".toCharArray(), "\\c1E\\s0000／\\s0001".toCharArray(), "\\c1F\\s0000／\\s0001".toCharArray()};
					buttonattacks.getChildMulti(2, i).draw(ConV.sprintf(pps[titlei], s1, s2));
				}
				buttoncancel.draw("CANCEL".toCharArray(), selection == -1);
				drawBalls();
				break;
		}
	}

	private void drawBalls() {
		for (int i = 0; i < team.team.length; i++) {
			int t = 0;
			if (team.team[i] == null) {
				t = 3;
			} else if (team.team[i].status == StatusCondition.KO) {
				t = 2;
			} else if (team.team[i].status != StatusCondition.OK) {
				t = 1;
			}
			balls.drawMulti(null, i, t, false);
		}
	}

	public void update(Game g, ExtPokemon[] t) {
		switch (status) {
			case MENU:
				if (menubuttons.animationRunning) {
					if (menubuttons.animationFinished()) {
						if (selection == 0) {
							selection = -2;
							status = ATTACKS;
						} else if (selection == 3) {
							status = CLOSED;
						}
					}
					break;
				}
				if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					if (selection == -1) {
						selection = 0;
					} else {
						menubuttons.animate(selection, true);
					}
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					selection = 0;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					selection = 2;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					selection = 3;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					selection = 1;
				}
				break;
			case ATTACKS:
				if (buttoncancel.animationRunning || buttonattacks.animationRunning) {
					if (buttoncancel.animationFinished()) {
						selection = -1;
						status = MENU;
					} else if (buttonattacks.animationFinished()) {
						int id = friendly.attackids[buttonattacks.animationPos];
						int power = InfoLoader.getPower(id) / 10;
						if (enemy.hp < power) {
							power = enemy.hp;
						}
						enemy.hp -= power;
						if (enemy.hp == 0) {
							enemy.status = StatusCondition.KO;
							status = CLOSED;
							SystemControll.setFlag(SystemControll.BattleWon, true);
							Logger.debug("won");
						}else{
							id = enemy.attackids[buttonattacks.animationPos];
							power = InfoLoader.getPower(id) / 10;
							if (friendly.hp < power) {
								power = friendly.hp;
							}
							friendly.hp -= power;
							if (friendly.hp == 0) {
								friendly.status = StatusCondition.KO;
								status = CLOSED;
								SystemControll.setFlag(SystemControll.BattleWon, false);
								Logger.debug("lost");
							}
						}
					}
					break;
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					buttoncancel.animate(true);
				}
				if (selection <= -1) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						buttoncancel.animate(true);
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						selection = Pokemon.ATTACKS - buttonattacks.columns;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						selection = -1;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						selection = -1;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						selection = -1;
					}
				} else {
					int columns = buttonattacks.columns;
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						buttonattacks.animate(selection, friendly.attacktypes[selection].getID(), true);
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (selection >= columns) {
							selection -= columns;
						}
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						if (selection % columns != 0) {
							selection--;
						}
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (selection < Pokemon.ATTACKS - columns) {
							selection += columns;
						} else {
							selection = -1;
						}
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						if (selection % columns != columns - 1) {
							selection++;
						}
					}
				}
				break;
		}
	}
}
