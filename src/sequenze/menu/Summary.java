package sequenze.menu;

import sequenze.Button;
import sequenze.Direction;
import sequenze.EncounterType;
import sequenze.ExtPokemon;
import sequenze.Game;
import sequenze.Gender;
import sequenze.GraphicHandler;
import sequenze.InfoLoader;
import sequenze.InstancePackage;

import static sequenze.InstancePackage.vfx;

import sequenze.Localisation;
import sequenze.MessageHandler;
import sequenze.Pokemon;
import sequenze.Pokerus;
import sequenze.Statistic;
import sequenze.StatusCondition;
import sequenze.SystemControll;
import sequenze.Text;
import sequenze.Type;
import sequenze.menu.Icon.DTD;
import util.ConV;
import util.Time;

public class Summary extends GuiItem {

	public ExtPokemon[/* max Number of Pokemon */] team;
	int selection = 0; // 0-2 info pages, 0 - ? attack move selection
	int currentPokemon = 0; // 0 - ?
	int moveFrom = 0; // 0 - ? in MOVEATTACK, 0 - ? in POKEMONINFO clicking
	public int[/* number of textures */] tex;
	public final static byte LIS1 = 0; // left info screen 1
	public final static byte RIS1 = 1; // right info screen 1
	public final static byte LIS2 = 2; // left info screen 2
	public final static byte RIS2 = 3; // right info screen 2
	public final static byte LIS3 = 4; // left info screen 3
	public final static byte RIS3 = 5; // right info screen 3
	public final static byte IBB = 6; // info back button
	public final static byte IBBA = 7; // info back button activated
	public final static byte ISB = 8; // info selection box
	public final static byte ISBS = 9; // info selection box selected
	public final static byte SW = 10; // move swap button
	public final static byte SWA = 11; // move swap button activated
	public final static byte RIBS = 13; // ribbon selection box
	public final static byte SWMS = 15; // move swap selection
	public final static byte SWMA = 16; // move swap activated
	public final static byte MIB = 17; // move info box
	public final static byte RIBIB = 18; // ribbon info box
	public final static byte RIBBB = 19; // ribbon back button
	public final static byte RIBBBA = 20; // ribbon back button activated
	public final static byte IIB = 21; // item info box
	public final static byte GIB = 22; // general info box
	public final static byte ISBA = 23; // info selection box activated
	public final static byte IN1 = 24; // info navigation 1
	public final static byte IN1A = 25; // info navigation 1 activated
	public final static byte IN2 = 26; // info navigation 2
	public final static byte IN2A = 27; // info navigation 2 activated
	public final static byte IN3 = 28; // info navigation 3
	public final static byte IN3A = 29; // info navigation 3 activated
	public final static byte RIS1EGG = 30; // right info screen 1 for eggs
	public final static int SHINYSTAR = 31; // star indicating a shiny pokemon
	public final static byte XP = 32; // xp bar
	public final static byte MALE = 33; // male sign
	public final static byte FEMALE = 34; // female sign
	public final static byte MARK = 35; // marking 1 - 6
	public final static byte MARKA = 36; // marking 1 - 6 activated
	public final static byte LEAF = 50; // shiny leaf 1-5
	public final static byte CROWN = 51; // shiny leaf crown 1-5
	public final static byte TYPE = 60; // type icon 1 - 20

	public final static byte STAR = 80; // performance star
	public final static byte STARBIG = 81; // big performance star
	public final static byte STARSMALL = 82; // small performance star
	public final static byte STARNONE = 83; // missing performance star
	// TODO performance arrows

	public final static byte HPGREEN2 = 84; // green hp bar
	public final static byte HPYELLOW2 = 85; // yellow hp bar
	public final static byte HPRED2 = 86; // red hp bar
	public final static byte PHYSICAL = 87; // physical attack type
	public final static byte SPECIAL = 88; // special attack type
	public final static byte STATUS = 89; // status attack type
	public final static byte XPBAR = 90; // xp bar
	public final static byte HPBAR = 91; // hp bar
	public final static byte BALL = 92; // the pokeball used 1-25
	public final static byte STATUSCD = 117; // status condition icon 1-6
	public final static byte POKERUS = 123; // active pokerus icon
	public final static byte POKERUSCURED = 124; // cured pokerus icon
	public final static byte PERF = 125; // performance label
	public final static byte PERFB = 126; // performance label bridge
	public final static byte RIBBONBG = 127; // ribbon background
	public final static int RIBBONTEXT = 128; // ribbon text label
	public final static int RIBBON = 129; // ribbon 1 - 80
	public final static int RIBBONICON = 209; // ribbon 1 - 7
	public final static int RIBARROWUP = 216; // ribbon arrow up
	public final static int RIBARROWUPA = 217; // ribbon arrow up activated
	public final static int RIBARROWDOWN = 218; // ribbon arrow down activated
	public final static int RIBARROWDOWNA = 219; // ribbon arrow down activated
	public final static int ATTACK = 220; // attack label
	public final static int ATTACKB = 221; // attack label bridge
	public final static int ATTACKPOS = 222; // attack label bridge
	public final static int ATTACKPOSA = 223; // attack label bridge
	public final static int ABILITY = 224; // attack label bridge

	private byte status = CLOSED; // current status of the team
	final static byte STARTUP = 0; // starting up
	final static byte SHUTDOWN = 1; // shutting down
	final static byte POKEMONINFO = 2; // viewing the info for a pokemon
	final static byte ATTACKINFO = 3; // viewing the info for an attack
	final static byte RIBBONINFO = 4; // viewing the info for a ribbon
	final static byte MOVEATTACK = 5; // moving an attack
	final static byte CLOSED = 6; // closed

	private Cell backgroundl;
	private Cell backgroundr;
	private Cell backbutton;
	private Cell markings;
	private Cell itembox;
	private Cell itemicon;
	private Cell navigation;
	private Cell generelinfo;
	private Cell pokeball;
	private Cell level;
	private Cell gender;
	private Cell condition;
	private Cell pokerus;
	private Cell pokeruscured;
	private Cell pokemonicon;
	private Cell pokemonnavigation;
	private Cell navigationicon;
	private Cell swapattack;
	private Cell performance;
	private Cell shinycrown;
	private Cell shinyleaf;
	private Cell ribbonbg;
	private Cell ribbons;
	private Cell ribbontext;
	private Cell ribboninfo;
	private Cell ribbonlore;
	private Cell ribbonbackbutton;
	private Cell ribbonicon;
	private Cell ribbonarrows;
	private Cell attacks;
	private Cell attacktype;
	private Cell attackpp;
	private Cell attackpptitle;
	private Cell attackselection;
	private Cell ability;
	private Cell abilityname;
	private Cell abilitydesc;
	private Cell healthbar;
	private Cell health;
	private Cell hpnumbers;
	private Cell stats;
	private Cell statgood;
	private Cell statbad;
	private Cell statnumber;
	private Cell memo;
	private Cell infoitems;
	private Cell infotext;
	private Cell infotextshiny;
	private Cell infotrainermale;
	private Cell infotrainerfemale;
	private Cell infotypesingle;
	private Cell infotypedual;
	private Cell xpbar;
	private Cell experience;
	private Cell xpinfo;
	private Cell xpnumbers;
	private Cell infobox;
	private Cell infoicon;
	private Cell infotype;
	private Cell infostats;
	private Cell infokind;
	private Cell infonumbers;
	private Cell shinystar;

	public Summary() {
		tex = GraphicHandler.giveTextures("Summary");

		setupCells();
	}

	public void setupCells() {
		backgroundl = new Cell();
		backgroundl.x = 0;
		backgroundl.y = 0;
		backgroundl.w = 256;
		backgroundl.h = 256;
		backgroundl.icon_type = Cell.MULTIICON;
		backgroundl.addText(160, 8, 0, 0x2, Text.TEXT_LEFT);
		backgroundl.starts = new int[]{0};
		backgroundl.icons = new Icon[1][3];
		backgroundl.icons[0][0] = new Icon(tex[LIS1], 0, 0);
		backgroundl.icons[0][1] = new Icon(tex[LIS2], 0, 0);
		backgroundl.icons[0][2] = new Icon(tex[LIS3], 0, 0);

		backgroundr = new Cell();
		backgroundr.x = 0;
		backgroundr.y = 0;
		backgroundr.w = 256;
		backgroundr.h = 256;
		backgroundr.icon_type = Cell.MULTIICON;
		backgroundr.addText(160, 8, 0, 0x2, Text.TEXT_LEFT);
		backgroundr.starts = new int[]{0};
		backgroundr.icons = new Icon[1][4];
		backgroundr.icons[0][0] = new Icon(tex[RIS1], 0, 0);
		backgroundr.icons[0][1] = new Icon(tex[RIS2], 0, 0);
		backgroundr.icons[0][2] = new Icon(tex[RIS3], 0, 0);
		backgroundr.icons[0][3] = new Icon(tex[RIS1EGG], 0, 0);

		backbutton = new Cell();
		backbutton.x = 190;
		backbutton.y = 229;
		backbutton.w = 60;
		backbutton.h = 24;
		backbutton.addText(30, 3, 2, 0x1, Text.TEXT_MIDDLE);
		backbutton.icon = new Icon(tex[IBB], 0, tex[IBBA]);

		markings = new Cell();
		markings.x = 197;
		markings.y = 211;
		markings.w = 8;
		markings.h = 8;
		markings.arangement = Cell.ROW;
		markings.x_col = 8;
		markings.icon_type = Cell.MULTIICON;
		markings.starts = new int[]{0, 1, 2, 3, 4, 5};
		markings.icons = new Icon[Pokemon.MARKINGS_NUM][1];
		for (int i = 0; i < markings.icons.length; i++) {
			markings.icons[i][0] = new Icon(tex[MARK + i * 2], tex[MARKA + i * 2], 0);
		}

		itemicon = new Cell();
		itemicon.x = 68;
		itemicon.y = 4;
		itemicon.w = 24;
		itemicon.h = 24;
		itemicon.addText(-64, 16, 0, 0x1, Text.TEXT_LEFT);

		itembox = new Cell();
		itembox.x = 156;
		itembox.y = 220;
		itembox.w = 100;
		itembox.h = 36;
		itembox.addText(4, 4, 0, 0x2, Text.TEXT_LEFT);
		itembox.icon = new Icon(tex[IIB], 0, 0);
		itembox.setChildren(itemicon);

		navigation = new Cell();
		navigation.x = 3;
		navigation.y = 221;
		navigation.w = 42;
		navigation.h = 30;
		navigation.arangement = Cell.ROW;
		navigation.x_col = 48;
		navigation.icon_type = Cell.MULTIICON;
		navigation.starts = new int[]{0, 1, 2};
		navigation.icons = new Icon[3][1];
		for (int i = 0; i < navigation.icons.length; i++) {
			navigation.icons[i][0] = new Icon(tex[IN1 + i * 2], tex[IN1A + i * 2], 0);
		}

		pokeball = new Cell();
		pokeball.x = 3;
		pokeball.y = 2;
		pokeball.w = 16;
		pokeball.h = 16;
		pokeball.icon_type = Cell.MULTIICON;
		pokeball.starts = new int[]{0};
		pokeball.icons = new Icon[1][25];
		for (int i = 0; i < pokeball.icons[0].length; i++) {
			pokeball.icons[0][i] = new Icon(tex[BALL + i], 0, 0);
		}

		level = new Cell();
		level.x = 0;
		level.y = 0;
		level.icon_type = Cell.TEXTONLY;
		level.addText(9, 19, 0, 0x1, Text.TEXT_LEFT);

		gender = new Cell();
		gender.x = 86;
		gender.y = 6;
		gender.w = 6;
		gender.h = 10;
		gender.icon_type = Cell.MULTIICON;
		gender.starts = new int[]{0};
		gender.icons = new Icon[1][2];
		gender.icons[0][0] = new Icon(tex[MALE], 0, 0);
		gender.icons[0][1] = new Icon(tex[FEMALE], 0, 0);

		condition = new Cell();
		condition.x = 60;
		condition.y = 21;
		condition.w = 20;
		condition.h = 8;
		condition.icon_type = Cell.MULTIICON;
		condition.starts = new int[]{0};
		condition.icons = new Icon[1][6];
		for (int i = 0; i < condition.icons[0].length; i++) {
			condition.icons[0][i] = new Icon(tex[STATUSCD + i], 0, 0);
		}

		pokerus = new Cell();
		pokerus.x = 52;
		pokerus.y = 18;
		pokerus.w = 40;
		pokerus.h = 14;
		pokerus.icon = new Icon(tex[POKERUS], 0, 0);

		generelinfo = new Cell();
		generelinfo.x = 156;
		generelinfo.y = 45;
		generelinfo.w = 100;
		generelinfo.h = 34;
		generelinfo.addText(20, 3, 0, 0x2, Text.TEXT_LEFT);
		generelinfo.icon = new Icon(tex[GIB], 0, 0);
		generelinfo.setChildren(pokeball, level, gender, condition, pokerus);

		pokeruscured = new Cell();
		pokeruscured.x = 248;
		pokeruscured.y = 176;
		pokeruscured.w = 8;
		pokeruscured.h = 8;
		pokeruscured.icon = new Icon(tex[POKERUSCURED], 0, 0);

		pokemonicon = new Cell();
		pokemonicon.x = 168;
		pokemonicon.y = 100;
		pokemonicon.w = 64;
		pokemonicon.h = 64;

		navigationicon = new Cell();
		navigationicon.x = 5;
		navigationicon.y = -5;
		navigationicon.w = 24;
		navigationicon.h = 24;

		pokemonnavigation = new Cell();
		pokemonnavigation.x = 160;
		pokemonnavigation.y = 112;
		pokemonnavigation.w = 40;
		pokemonnavigation.h = 32;
		pokemonnavigation.arangement = Cell.GRIDY;
		pokemonnavigation.columns = 2;
		pokemonnavigation.x_col = 40;
		pokemonnavigation.y_row = 32;
		pokemonnavigation.y_col = 8;
		pokemonnavigation.icon_type = Cell.MULTIICON;
		pokemonnavigation.starts = new int[]{70};
		pokemonnavigation.icons = new Icon[1][2];
		pokemonnavigation.icons[0][0] = new Icon(tex[ISB], tex[ISBS], 0);
		pokemonnavigation.icons[0][1] = new Icon(tex[ISB], tex[ISBA], 0);
		pokemonnavigation.setChildren(navigationicon);

		swapattack = new Cell();
		swapattack.x = 8;
		swapattack.y = 162;
		swapattack.w = 80;
		swapattack.h = 18;
		swapattack.addText(40, 1, 2, 0x1, Text.TEXT_MIDDLE);
		swapattack.icon = new Icon(tex[SW], 0, tex[SWA]);

		performance = new Cell();
		performance.x = 8;
		performance.y = 32;
		performance.w = 189;
		performance.h = 37;
		performance.arangement = Cell.COLLUMN;
		performance.y_row = 37;
		performance.icon_type = Cell.MULTIICON;
		performance.starts = new int[]{0, 1};
		performance.icons = new Icon[2][1];
		Icon pi1 = new Icon();
		pi1.normal = new DTD[]{new DTD(tex[PERF], -2, -2)};
		performance.icons[0][0] = pi1;
		Icon pi2 = new Icon();
		pi2.normal = new DTD[]{new DTD(tex[PERF], -2, -2), new DTD(tex[PERFB], -2, -2)};
		performance.icons[1][0] = pi2;

		shinyleaf = new Cell();
		shinyleaf.x = -12;
		shinyleaf.y = 2;
		shinyleaf.w = 16;
		shinyleaf.h = 18;
		shinyleaf.arangement = Cell.ROW;
		shinyleaf.x_col = 10;
		Icon sli = new Icon();
		sli.normal = new DTD[]{new DTD(tex[LEAF])};
		sli.duration = 25;
		sli.frames_a = new int[]{4, 7, 10, 13, 16, 19, 22};
		sli.anim = new DTD[][]{{new DTD(tex[LEAF])}, {new DTD(tex[LEAF + 2])}, {new DTD(tex[LEAF + 4])}, {new DTD(tex[LEAF + 2])}, {new DTD(tex[LEAF])}, {new DTD(tex[LEAF + 6])}, {new DTD(tex[LEAF + 8])}, {new DTD(tex[LEAF + 6])}};
		shinyleaf.icon = sli;

		shinycrown = new Cell();
		shinycrown.x = 96;
		shinycrown.y = 224;
		shinycrown.w = 31;
		shinycrown.h = 20;
		shinycrown.addText(-88, 6, 0, 0x1, Text.TEXT_LEFT);
		shinycrown.icon_type = Cell.MULTIICON;
		shinycrown.starts = new int[]{0};
		shinycrown.icons = new Icon[1][2];
		Icon sci = new Icon();
		sci.normal = new DTD[]{new DTD(tex[CROWN])};
		sci.duration = 25;
		sci.frames_a = new int[]{4, 7, 10, 13, 16, 19, 22};
		sci.anim = new DTD[][]{{new DTD(tex[CROWN])}, {new DTD(tex[CROWN + 2])}, {new DTD(tex[CROWN + 4])}, {new DTD(tex[CROWN + 2])}, {new DTD(tex[CROWN])}, {new DTD(tex[CROWN + 6])}, {new DTD(tex[CROWN + 8])}, {new DTD(tex[CROWN + 6])}};
		shinycrown.icons[0][0] = sci;
		shinycrown.icons[0][1] = new Icon();
		shinycrown.setChildren(shinyleaf);

		ribbons = new Cell();
		ribbons.x = -2;
		ribbons.y = -2;
		ribbons.w = 32;
		ribbons.h = 32;
		ribbons.icon_type = Cell.MULTIICON;
		ribbons.starts = new int[]{0};
		ribbons.icons = new Icon[1][80];
		for (int i = 0; i < ribbons.icons[0].length; i++) {
			ribbons.icons[0][i] = new Icon(tex[RIBBON + i], 0, 0);
		}

		ribbonbg = new Cell();
		ribbonbg.x = 18;
		ribbonbg.y = 26;
		ribbonbg.w = 28;
		ribbonbg.h = 28;
		ribbonbg.arangement = Cell.GRID;
		ribbonbg.columns = 3;
		ribbonbg.x_col = 32;
		ribbonbg.y_row = 40;
		ribbonbg.icon = new Icon(tex[RIBBONBG], tex[RIBS], 0).moveSel(-2, -2);
		ribbonbg.setChildren(ribbons);

		ribbontext = new Cell();
		ribbontext.x = 0;
		ribbontext.y = 188;
		ribbontext.w = 150;
		ribbontext.h = 20;
		ribbontext.addText(8, 2, 0, 0x1, Text.TEXT_LEFT);
		ribbontext.icon = new Icon(tex[RIBBONTEXT], 0, 0);

		ribbonlore = new Cell();
		ribbonlore.x = 0;
		ribbonlore.y = 0;
		ribbonlore.icon_type = Cell.TEXTONLY;
		ribbonlore.addText(8, 18, 0, 0x1, Text.TEXT_LEFT, 2, 16);

		ribbonbackbutton = new Cell();
		ribbonbackbutton.x = 210;
		ribbonbackbutton.y = 51;
		ribbonbackbutton.w = 44;
		ribbonbackbutton.h = 16;
		ribbonbackbutton.icon = new Icon(tex[RIBBB], 0, tex[RIBBBA]);

		ribboninfo = new Cell();
		ribboninfo.x = 0;
		ribboninfo.y = 188;
		ribboninfo.w = 256;
		ribboninfo.h = 67;
		ribboninfo.addText(8, 2, 0, 0x2, Text.TEXT_LEFT);
		ribboninfo.icon = new Icon(tex[RIBIB], 0, 0);
		ribboninfo.setChildren(ribbonlore, ribbonbackbutton);

		ribbonicon = new Cell();
		ribbonicon.x = 160;
		ribbonicon.y = 205;
		ribbonicon.w = 16;
		ribbonicon.h = 16;
		Icon rii = new Icon();
		rii.normal = new DTD[]{new DTD(tex[RIBBONICON])};
		rii.duration = 37;
		rii.frames_a = new int[]{4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34};
		rii.anim = new DTD[][]{{new DTD(tex[RIBBONICON])}, {new DTD(tex[RIBBONICON + 1])}, {new DTD(tex[RIBBONICON + 2])}, {new DTD(tex[RIBBONICON + 3])}, {new DTD(tex[RIBBONICON + 2])}, {new DTD(tex[RIBBONICON + 1])}, {new DTD(tex[RIBBONICON])}, {new DTD(tex[RIBBONICON + 4])}, {new DTD(tex[RIBBONICON + 5])}, {new DTD(tex[RIBBONICON + 6])}, {new DTD(tex[RIBBONICON + 5])}, {new DTD(tex[RIBBONICON + 4])}};
		ribbonicon.icon = rii;

		ribbonarrows = new Cell();
		ribbonarrows.x = 116;
		ribbonarrows.y = 40;
		ribbonarrows.w = 24;
		ribbonarrows.h = 40;
		ribbonarrows.arangement = Cell.COLLUMN;
		ribbonarrows.y_row = 80;
		ribbonarrows.icon_type = Cell.MULTIICON;
		ribbonarrows.starts = new int[]{0, 1};
		ribbonarrows.icons = new Icon[2][1];
		ribbonarrows.icons[0][0] = new Icon(tex[RIBARROWUP], 0, tex[RIBARROWUPA]);
		ribbonarrows.icons[1][0] = new Icon(tex[RIBARROWDOWN], 0, tex[RIBARROWDOWNA]);

		attackselection = new Cell();
		attackselection.x = -2;
		attackselection.y = -2;
		attackselection.icon_type = Cell.MULTIICON;
		attackselection.starts = new int[]{0};
		attackselection.icons = new Icon[1][3];
		attackselection.icons[0][0] = new Icon(tex[SWMS], 0, 0);
		attackselection.icons[0][1] = new Icon(tex[SWMA], 0, 0);
		Icon asi = new Icon();
		asi.normal = new DTD[]{new DTD(tex[SWMS]), new DTD(tex[SWMA])};
		attackselection.icons[0][2] = asi;

		attackpptitle = new Cell();
		attackpptitle.x = 0;
		attackpptitle.y = 0;
		attackpptitle.icon_type = Cell.TEXTONLY;
		attackpptitle.addText(49, 18, 0, 0x1, Text.TEXT_LEFT);

		attackpp = new Cell();
		attackpp.x = 0;
		attackpp.y = 0;
		attackpp.icon_type = Cell.TEXTONLY;
		attackpp.addText(90, 18, 0, 0x1, Text.TEXT_SLASH);

		attacktype = new Cell();
		attacktype.x = 1;
		attacktype.y = 2;
		attacktype.w = 32;
		attacktype.h = 14;
		attacktype.icon_type = Cell.MULTIICON;
		attacktype.starts = new int[]{0};
		attacktype.icons = new Icon[1][20];
		for (int i = 0; i < attacktype.icons[0].length; i++) {
			attacktype.icons[0][i] = new Icon(tex[TYPE + i], 0, 0);
		}

		attacks = new Cell();
		attacks.x = 7;
		attacks.y = 23;
		attacks.w = 127;
		attacks.h = 35;
		attacks.arangement = Cell.COLLUMN;
		attacks.y_row = 35;
		attacks.icon_type = Cell.MULTIICON;
		attacks.addText(34, 3, 0, 0x1, Text.TEXT_LEFT);
		attacks.starts = new int[]{0, 1, 2, 3};
		attacks.icons = new Icon[4][1];
		Icon ai1 = new Icon();
		ai1.normal = new DTD[]{new DTD(tex[ATTACK], -2, -2), new DTD(tex[ATTACKPOS], 2, 19), new DTD(tex[ATTACKPOSA], 2, 19)};
		attacks.icons[0][0] = ai1;
		Icon ai2 = new Icon();
		ai2.normal = new DTD[]{new DTD(tex[ATTACK], -2, -2), new DTD(tex[ATTACKB], -2, -2), new DTD(tex[ATTACKPOS], 2, 19), new DTD(tex[ATTACKPOSA], 18, 19)};
		attacks.icons[1][0] = ai2;
		Icon ai3 = new Icon();
		ai3.normal = new DTD[]{new DTD(tex[ATTACK], -2, -2), new DTD(tex[ATTACKB], -2, -2), new DTD(tex[ATTACKPOS], 2, 19), new DTD(tex[ATTACKPOSA], 2, 27)};
		attacks.icons[2][0] = ai3;
		Icon ai4 = new Icon();
		ai4.normal = new DTD[]{new DTD(tex[ATTACK], -2, -2), new DTD(tex[ATTACKB], -2, -2), new DTD(tex[ATTACKPOS], 2, 19), new DTD(tex[ATTACKPOSA], 18, 27)};
		attacks.icons[3][0] = ai4;
		attacks.setChildren(attacktype, attackpptitle, attackpp, attackselection);

		abilityname = new Cell();
		abilityname.x = 0;
		abilityname.y = 0;
		abilityname.icon_type = Cell.TEXTONLY;
		abilityname.addText(72, 3, 0, 0x1, Text.TEXT_LEFT);

		abilitydesc = new Cell();
		abilitydesc.x = 0;
		abilitydesc.y = 0;
		abilitydesc.icon_type = Cell.TEXTONLY;
		abilitydesc.addText(3, 21, 0, 0x1, Text.TEXT_LEFT, 2, 18);

		ability = new Cell();
		ability.x = 0;
		ability.y = 186;
		ability.w = 149;
		ability.h = 58;
		ability.addText(3, 3, 0, 0x2, Text.TEXT_LEFT);
		ability.icon = new Icon(tex[ABILITY], 0, 0);
		ability.setChildren(abilityname, abilitydesc);

		health = new Cell();
		health.x = 16;
		health.y = 2;
		health.w = 1;
		health.h = 3;
		health.arangement = Cell.ROW;
		health.x_col = 1;
		// TODO store differently, will be deleted
		health.columns = 48;
		health.icon_type = Cell.MULTIICON;
		health.starts = new int[]{0};
		health.icons = new Icon[1][3];
		health.icons[0][0] = new Icon(tex[HPGREEN2], 0, 0);
		health.icons[0][1] = new Icon(tex[HPYELLOW2], 0, 0);
		health.icons[0][2] = new Icon(tex[HPRED2], 0, 0);

		hpnumbers = new Cell();
		hpnumbers.x = 0;
		hpnumbers.y = 0;
		hpnumbers.icon_type = Cell.TEXTONLY;
		hpnumbers.addText(49, -16, 0, 0x1, Text.TEXT_SLASH);

		healthbar = new Cell();
		healthbar.x = 64;
		healthbar.y = 58;
		healthbar.w = 67;
		healthbar.h = 7;
		healthbar.addText(-18, -16, 0, 0x2, Text.TEXT_MIDDLE);
		healthbar.icon = new Icon(tex[HPBAR], 0, 0);
		healthbar.setChildren(health, hpnumbers);

		statgood = new Cell();
		statgood.x = 0;
		statgood.y = 0;
		statgood.icon_type = Cell.TEXTONLY;
		statgood.addText(0, 0, 0, 0x17, Text.TEXT_LEFT);

		statbad = new Cell();
		statbad.x = 0;
		statbad.y = 0;
		statbad.icon_type = Cell.TEXTONLY;
		statbad.addText(0, 0, 0, 0x16, Text.TEXT_LEFT);

		statnumber = new Cell();
		statnumber.x = 0;
		statnumber.y = 0;
		statnumber.icon_type = Cell.TEXTONLY;
		statnumber.addText(103, 0, 0, 0x1, Text.TEXT_RIGHT);

		stats = new Cell();
		stats.x = 24;
		stats.y = 67;
		stats.arangement = Cell.COLLUMN;
		stats.y_row = 18;
		stats.icon_type = Cell.TEXTONLY;
		stats.addText(0, 0, 0, 0x2, Text.TEXT_LEFT);
		stats.setChildren(statgood, statbad, statnumber);

		memo = new Cell();
		memo.x = 0;
		memo.y = 0;
		memo.icon_type = Cell.TEXTONLY;
		memo.addText(6, 42, 0, 0x1, Text.TEXT_LEFT, 9, 18);

		infotext = new Cell();
		infotext.x = 0;
		infotext.y = 0;
		infotext.icon_type = Cell.TEXTONLY;
		infotext.addText(100, 0, 0, 0x1, Text.TEXT_MIDDLE);

		infotextshiny = new Cell();
		infotextshiny.x = 0;
		infotextshiny.y = 0;
		infotextshiny.icon_type = Cell.TEXTONLY;
		infotextshiny.addText(100, 0, 0, 0x4, Text.TEXT_MIDDLE);

		infotypesingle = new Cell();
		infotypesingle.x = 84;
		infotypesingle.y = 1;
		infotypesingle.w = 32;
		infotypesingle.h = 14;
		infotypesingle.icon_type = Cell.MULTIICON;
		infotypesingle.starts = new int[]{0};
		infotypesingle.icons = new Icon[1][20];
		for (int i = 0; i < infotypesingle.icons[0].length; i++) {
			infotypesingle.icons[0][i] = new Icon(tex[TYPE + i], 0, 0);
		}

		infotypedual = new Cell();
		infotypedual.x = 67;
		infotypedual.y = 1;
		infotypedual.w = 32;
		infotypedual.h = 14;
		infotypedual.arangement = Cell.ROW;
		infotypedual.x_col = 34;
		infotypedual.icon_type = Cell.MULTIICON;
		infotypedual.starts = new int[]{0};
		infotypedual.icons = new Icon[1][20];
		for (int i = 0; i < infotypedual.icons[0].length; i++) {
			infotypedual.icons[0][i] = new Icon(tex[TYPE + i], 0, 0);
		}

		infotrainermale = new Cell();
		infotrainermale.x = 0;
		infotrainermale.y = 0;
		infotrainermale.icon_type = Cell.TEXTONLY;
		infotrainermale.addText(100, 0, 0, 0x3, Text.TEXT_MIDDLE);

		infotrainerfemale = new Cell();
		infotrainerfemale.x = 0;
		infotrainerfemale.y = 0;
		infotrainerfemale.icon_type = Cell.TEXTONLY;
		infotrainerfemale.addText(100, 0, 0, 0x4, Text.TEXT_MIDDLE);

		infoitems = new Cell();
		infoitems.x = 8;
		infoitems.y = 25;
		infoitems.arangement = Cell.COLLUMN;
		infoitems.y_row = 18;
		infoitems.icon_type = Cell.TEXTONLY;
		infoitems.addText(0, 0, 0, 0x2, Text.TEXT_LEFT);
		infoitems.setChildren(infotext, infotypesingle, infotypedual, infotrainermale, infotrainerfemale, infotextshiny);

		experience = new Cell();
		experience.x = 22;
		experience.y = 2;
		experience.w = 1;
		experience.h = 3;
		experience.arangement = Cell.ROW;
		experience.x_col = 1;
		experience.columns = 64;
		experience.icon = new Icon(tex[XP], 0, 0);

		xpbar = new Cell();
		xpbar.x = 42;
		xpbar.y = 71;
		xpbar.w = 89;
		xpbar.h = 7;
		xpbar.icon = new Icon(tex[XPBAR], 0, 0);
		xpbar.setChildren(experience);

		xpnumbers = new Cell();
		xpnumbers.x = 0;
		xpnumbers.y = 0;
		xpnumbers.icon_type = Cell.TEXTONLY;
		xpnumbers.addText(128, 18, 0, 0x1, Text.TEXT_RIGHT);

		xpinfo = new Cell();
		xpinfo.x = 8;
		xpinfo.y = 115;
		xpinfo.arangement = Cell.COLLUMN;
		xpinfo.y_row = 36;
		xpinfo.icon_type = Cell.TEXTONLY;
		xpinfo.addText(0, 0, 0, 0x2, Text.TEXT_LEFT);
		xpinfo.setChildren(xpnumbers, xpbar);

		infokind = new Cell();
		infokind.x = 72;
		infokind.y = 1;
		infokind.w = 32;
		infokind.h = 14;
		infokind.icon_type = Cell.MULTIICON;
		infokind.starts = new int[]{0};
		infokind.icons = new Icon[1][3];
		for (int i = 0; i < infokind.icons[0].length; i++) {
			infokind.icons[0][i] = new Icon(tex[PHYSICAL + i], 0, 0);
		}

		infonumbers = new Cell();
		infonumbers.x = 0;
		infonumbers.y = 0;
		infonumbers.icon_type = Cell.TEXTONLY;
		infonumbers.addText(96, 0, 0, 0x1, Text.TEXT_RIGHT);

		infostats = new Cell();
		infostats.x = 16;
		infostats.y = 28;
		infostats.arangement = Cell.COLLUMN;
		infostats.y_row = 18;
		infostats.icon_type = Cell.TEXTONLY;
		infostats.addText(0, 0, 0, 0x2, Text.TEXT_LEFT);
		infostats.setChildren(infonumbers, infokind);

		infotype = new Cell();
		infotype.x = 56;
		infotype.y = 11;
		infotype.w = 32;
		infotype.h = 14;
		infotype.arangement = Cell.ROW;
		infotype.x_col = 34;
		infotype.icon_type = Cell.MULTIICON;
		infotype.starts = new int[]{0};
		infotype.icons = new Icon[1][20];
		for (int i = 0; i < infotype.icons[0].length; i++) {
			infotype.icons[0][i] = new Icon(tex[TYPE + i], 0, 0);
		}

		infoicon = new Cell();
		infoicon.x = 16;
		infoicon.y = 4;
		infoicon.w = 24;
		infoicon.h = 24;

		infobox = new Cell();
		infobox.x = 128;
		infobox.y = 21;
		infobox.w = 128;
		infobox.h = 174;
		infobox.addText(9, 82, 0, 0x1, Text.TEXT_LEFT, 5, 18);
		infobox.icon = new Icon(tex[MIB], 0, 0);
		infobox.setChildren(infoicon, infotype, infostats);

		shinystar = new Cell();
		shinystar.x = 248;
		shinystar.y = 84;
		shinystar.w = 8;
		shinystar.h = 8;
		shinystar.icon = new Icon(tex[SHINYSTAR], 0, 0);

		shinycrown.animate(false);
		shinyleaf.animate(false);
	}

	@Override
	public void update(Game g) {
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.SUMMARY_OPEN)) {
					vfx.addShutter(Direction.NORTH, true);
					selection = 0;
					status = STARTUP;
				}
				// TODO check for other messages
				break;
			case STARTUP:
				if (vfx.shutterFinished()) {
					status = POKEMONINFO;
				}
				break;
			case SHUTDOWN:
				if (vfx.shutterFinished()) {
					status = CLOSED;
					// TODO make independent of team/pc
					InstancePackage.team.currentPokemon = currentPokemon;
					MessageHandler.add(MessageHandler.TEAM_OPEN, null);
				}
				break;
			case POKEMONINFO:
				if (attacks.animationRunning || backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWN;
					} else if (attacks.animationFinished()) {
						selection = moveFrom;
						moveFrom = 0;
						status = ATTACKINFO;
					}
					break;
				}
				if (shinycrown.animationFinished()) {
					shinycrown.animate(false);
				}
				if (shinyleaf.animationFinished()) {
					shinyleaf.animate(false);
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					backbutton.animate(false);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					if (selection == 1) {
						attacks.animate(0, false);
					} else if (selection == 2 && team[currentPokemon].ribbons.length > 0) {
						selection = 0;
						ribbonicon.animate(false);
						status = RIBBONINFO;
					}
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					if (selection > 0 && selection <= 2) {
						selection--;
					}
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					if (selection < 2 && !team[currentPokemon].isEgg()) {
						selection++;
					}
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					int i;
					for (i = currentPokemon - 1; i >= 0; i--) {
						if (team[i] != null && (selection == 0 || !team[i].isEgg())) {
							break;
						}
					}
					if (i >= 0) {
						currentPokemon = i;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					int i;
					for (i = currentPokemon + 1; i < team.length; i++) {
						if (team[i] != null && (selection == 0 || !team[i].isEgg())) {
							break;
						}
					}
					if (i < team.length) {
						currentPokemon = i;
					}
					break;
				}
				infoClick(g);
				break;
			case MOVEATTACK:
				if (attacks.animationRunning || backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						moveFrom = 0;
						status = ATTACKINFO;
					} else if (attacks.animationFinished()) {
						team[currentPokemon].switchAttacks(moveFrom, selection);
						moveFrom = 0;
						status = ATTACKINFO;
					}
					break;
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					backbutton.animate(false);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					attacks.animate(selection, true);
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection > 0) {
						selection--;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection + 1 < Pokemon.ATTACKS && team[currentPokemon].attackids[selection + 1] != 0) {
						selection++;
					}
					break;
				}
				moveAttackClick(g);
				break;
			case ATTACKINFO:
				if (swapattack.animationRunning || backbutton.animationRunning) {
					if (backbutton.animationFinished()) {
						selection = 1;
						status = POKEMONINFO;
					} else if (swapattack.animationFinished()) {
						moveFrom = selection;
						status = MOVEATTACK;
					}
					break;
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					backbutton.animate(false);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					swapattack.animate(false);
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection > 0) {
						selection--;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection + 1 < Pokemon.ATTACKS && team[currentPokemon].attackids[selection + 1] != 0) {
						selection++;
					}
					break;
				}
				attackInfoClick(g);
				break;
			case RIBBONINFO:
				if (ribbonbackbutton.animationRunning || ribbonarrows.animationRunning) {
					if (ribbonbackbutton.animationFinished()) {
						selection = 2;
						status = POKEMONINFO;
						ribbonicon.stopAnimation();
					} else if (ribbonarrows.animationFinished()) {
						if (ribbonarrows.animationPos == 0) {
							if (selection >= 12) {
								selection -= 12;
							}
						} else {
							if (selection / 12 + 1 <= (team[currentPokemon].ribbons.length - 1) / 12) {
								selection += 12;
							}
						}
					}
					break;
				}
				if (ribbonicon.animationFinished()) {
					ribbonicon.animate(false);
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					ribbonbackbutton.animate(false);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					ribbonbackbutton.animate(false);
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					if (selection % ribbonbg.columns != 0) {
						selection--;
					}
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					if ((selection + 1) % ribbonbg.columns != 0) {
						selection++;
					}
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection >= ribbonbg.columns) {
						selection -= ribbonbg.columns;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if ((selection + ribbonbg.columns) / 12 <= (team[currentPokemon].ribbons.length - 1) / 12) {
						selection += ribbonbg.columns;
					}
					break;
				}
				ribbonInfoClick(g);
				break;
		}
	}

	/**
	 * Processes clicking while moving a attack.
	 */
	private void ribbonInfoClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}

		int sel = ribbonbg.clickMulti(g.getMouseX(), g.getMouseY(), 12);
		if (sel != -1) {
			g.processMouse();
			selection = selection / 12 * 12 + sel;
			return;
		}

		sel = ribbonarrows.clickMulti(g.getMouseX(), g.getMouseY(), 2);
		if (sel != -1) {
			g.processMouse();
			ribbonarrows.animate(sel, false);
			return;
		}

		if (ribboninfo.getChild(1).click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			ribbonbackbutton.animate(false);
		}
	}

	/**
	 * Processes clicking while moving a attack.
	 */
	private void moveAttackClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}

		int sel = attacks.clickMulti(g.getMouseX(), g.getMouseY(), Pokemon.ATTACKS);
		if (sel != -1 && team[currentPokemon].attackids[sel] != 0) {
			g.processMouse();
			selection = sel;
			attacks.animate(sel, true);
			return;
		}

		if (backbutton.click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			backbutton.animate(false);
		}
	}

	/**
	 * Processes clicking while viewing the pokemon info.
	 */
	private void infoClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}

		int sel = pokemonnavigation.clickMulti(g.getMouseX(), g.getMouseY(), team.length);
		if (sel != -1) {
			g.processMouse();
			if (team[sel] == null) {
				return;
			}
			if (team[sel].isEgg() && selection != 0) {
				return;
			}
			if (currentPokemon == sel) {
				return;
			}
			currentPokemon = sel;
			return;
		}

		sel = navigation.clickMulti(g.getMouseX(), g.getMouseY(), 3);
		if (sel != -1) {
			g.processMouse();
			if (selection == sel || team[currentPokemon].isEgg()) {
				return;
			}
			selection = sel;
			return;
		}

		if (selection == 1) {
			sel = attacks.clickMulti(g.getMouseX(), g.getMouseY(), Pokemon.ATTACKS);
			if (sel != -1 && team[currentPokemon].attackids[sel] != 0) {
				g.processMouse();
				moveFrom = sel;
				attacks.animate(sel, true);
				return;
			}
		}

		if (selection == 2 && team[currentPokemon].ribbons.length > 0) {
			sel = ribbonbg.clickMulti(g.getMouseX(), g.getMouseY(), 12);
			if (sel != -1) {
				g.processMouse();
				selection = sel;
				ribbonicon.animate(false);
				status = RIBBONINFO;
				return;
			}
		}

		if (backbutton.click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			backbutton.animate(false);
		}
	}

	/**
	 * Draws the left section of the selection.
	 */
	@Override
	public void drawL() {
		switch (status) {
			case STARTUP:
			case SHUTDOWN:
			case POKEMONINFO:
				drawInfoBackgroundL();
				drawGeneralInfo();
				drawInfoPagesL();
				break;
			case MOVEATTACK:
				drawInfoBackgroundL();
				drawGeneralInfo();
				drawSkillsPage();

				break;
			case ATTACKINFO:
				drawInfoBackgroundL();
				drawGeneralInfo();
				drawSkillsPage();

				break;
			case RIBBONINFO:
				drawInfoBackgroundL();
				drawGeneralInfo();
				drawPerformacePage();
				break;
		}
	}

	/**
	 * Draws the right section of the selection.
	 */
	@Override
	public void drawR() {
		switch (status) {
			case STARTUP:
			case SHUTDOWN:
			case POKEMONINFO:
				drawInfoBackground();
				drawPokemonNavigation();
				drawInfoPages();
				drawInfoPageNavigation();
				drawInfoBackButton();
				break;
			case MOVEATTACK:
				drawInfoBackground();
				drawMovePage();
				drawMoveInfo();
				drawMoveSelection();
				drawInfoPageNavigation();
				drawInfoBackButton();
				break;
			case ATTACKINFO:
				drawInfoBackground();
				drawMovePage();
				drawMoveInfo();
				drawMoveAccept();
				drawMoveSelection();
				drawInfoPageNavigation();
				drawInfoBackButton();
				break;
			case RIBBONINFO:
				drawInfoBackground();
				drawPokemonNavigation();
				drawRibbonPage();
				break;
		}
	}

	private void drawInfoBackButton() {
		backbutton.draw(Localisation.getTeamInfoBackButton());
	}

	/**
	 * Draws the info pages.
	 */
	private void drawInfoPages() {
		ExtPokemon epk = team[currentPokemon];
		if (epk.isEgg()) {
			return;
		}
		if (selection == 0) {
			char[][] names = Localisation.getTeamInfoNames();

			infoitems.drawMulti(names[0], 0);
			// TODO display local pokedex number when national pokedex is not yet unlocked
			if (epk.shiny) {
				infoitems.getChildMulti(5, 0).draw(ConV.toCString(epk.id));
			} else {
				infoitems.getChildMulti(0, 0).draw(ConV.toCString(epk.id));
			}
			infoitems.drawMulti(names[1], 1);
			infoitems.getChildMulti(0, 1).draw(epk.getName());
			infoitems.drawMulti(names[2], 2);
			if (epk.type2 == Type.NONE) {
				infoitems.getChildMulti(1, 2).draw(epk.type1.getID(), false);
			} else {
				infoitems.getChildMulti(2, 2).drawMulti(null, 0, epk.type1.getID(), false);
				infoitems.getChildMulti(2, 2).drawMulti(null, 1, epk.type2.getID(), false);
			}
			infoitems.drawMulti(names[3], 3);
			if (epk.trainer_male) {
				infoitems.getChildMulti(3, 3).draw(epk.trainer_name);
			} else {
				infoitems.getChildMulti(4, 3).draw(epk.trainer_name);
			}
			infoitems.drawMulti(names[4], 4);
			infoitems.getChildMulti(0, 4).draw(ConV.toFixedCString(epk.trainerid));

			xpinfo.drawMulti(names[5], 0);
			xpinfo.getChildMulti(0, 0).draw(ConV.toCString(epk.currentep));
			xpinfo.drawMulti(names[6], 1);
			int diff = 0;
			if (epk.level != Pokemon.LEVELMAX) {
				diff = epk.xptype.getXPDifference(epk.level);
				xpinfo.getChildMulti(0, 1).draw(ConV.toCString(epk.nextlevelep - epk.currentep));
			} else {
				xpinfo.getChildMulti(0, 1).draw(Localisation.getSummaryMaxLevelEP());
			}

			int width = experience.columns;
			if (epk.level != Pokemon.LEVELMAX) {
				width = width * (diff + epk.currentep - epk.nextlevelep) / diff;
			}
			Cell experiencebar = xpinfo.getChild(1);
			experiencebar.draw();
			experiencebar.getChild(0).drawMulti(width, -1);

		} else if (selection == 1) {
			drawMovePage();
		} else {
			ribbonbg.drawMulti(12, -1);
			for (int i = 0; i < 12 && i < epk.ribbons.length; i++) {
				ribbonbg.getChildMulti(0, i).draw(epk.ribbons[i], false);
			}
			ribbontext.draw(ConV.sprintf(Localisation.getSummaryRibbonNumber(), epk.ribbons.length));
		}
	}

	private void drawMovePage() {
		ExtPokemon epk = team[currentPokemon];
		for (int i = 0; i < epk.attackids.length; i++) {
			if (epk.attackids[i] == 0) {
				attacks.drawMulti(Localisation.getSummaryNoAttack(), i, false);
				attacks.getChildMulti(2, i).draw(Localisation.getSummaryEmptyMove());
				continue;
			}

			attacks.drawMulti(Localisation.getAttackName(epk.attackids[i]), i, false);
			attacks.getChildMulti(0, i).draw(epk.attacktypes[i].getID(), false);
			attacks.getChildMulti(1, i).draw(Localisation.getSummaryPPTitle());
			assert (epk.maxpps[i] <= 999);
			attacks.getChildMulti(2, i).draw(ConV.sprintf(Localisation.getSummaryPP(), epk.pps[i], epk.maxpps[i]));
		}
	}

	private void drawRibbonPage() {
		ExtPokemon epk = team[currentPokemon];
		ribbonbg.drawMulti(12, selection % 12);
		for (int i = 0; i < 12; i++) {
			if (selection / 12 * 12 + i >= epk.ribbons.length) {
				break;
			}
			ribbonbg.getChildMulti(0, i).draw(epk.ribbons[selection / 12 * 12 + i], false);
		}
		ribbontext.draw(ConV.sprintf(Localisation.getSummaryRibbonNumber(), epk.ribbons.length));
		if (selection < team[currentPokemon].ribbons.length) {
			ribboninfo.draw(Localisation.getRibbonName(epk.ribbons[selection]));
			ribboninfo.getChild(0).draw(Localisation.getRibbonText(epk.ribbons[selection]));
		} else {
			ribboninfo.draw(null);
		}
		ribboninfo.getChild(1).draw();
		if (selection / 12 != 0) {
			ribbonarrows.drawMulti(null, 0);
		}
		if (selection / 12 < (epk.ribbons.length - 1) / 12) {
			ribbonarrows.drawMulti(null, 1);
		}
	}

	/**
	 * Draws the info pages.
	 */
	private void drawInfoPagesL() {
		ExtPokemon epk = team[currentPokemon];
		if (selection == 0) {
			int[] res = Time.timestamp_to_gmt(epk.caught_time);
			int[] egg_res = Time.timestamp_to_gmt(epk.hatch_time);
			char[] location;
			char[] egg_location = null;
			if (epk.caught_region != SystemControll.getVariable(SystemControll.RegionID)) {
				location = Localisation.getRegionName(epk.caught_region);
				assert (epk.encounter_type != EncounterType.Egg);
				// TODO what about traded pokemon coming from eggs?
			} else {
				location = Localisation.getLocationName(epk.caught_location);
				if (epk.encounter_type == EncounterType.Egg) {
					egg_location = Localisation.getLocationName(epk.egg_location);
				}
			}
			char[] format;
			if (epk.isEgg()) {
				format = Localisation.getInfoPageEggText()[epk.getEggQoute()];
			} else if (epk.trainerid != SystemControll.getVariable(SystemControll.TrainerID) || epk.strainerid != SystemControll.getVariable(SystemControll.STrainerID)) {
				format = Localisation.getInfoPageTradedText(epk.encounter_type);
			} else {
				format = Localisation.getInfoPageText(epk.encounter_type);
			}
			Object[] paras = new Object[11];
			paras[0] = Localisation.getNatureName(epk.nature);
			paras[1] = res[3];
			paras[2] = Localisation.getMonths()[res[4] - 1];
			paras[3] = res[5];
			paras[4] = location;
			paras[5] = epk.caught_level;
			paras[6] = Localisation.getCharacteristics()[epk.characteristic];
			paras[7] = egg_res[3];
			paras[8] = Localisation.getMonths()[egg_res[4] - 1];
			paras[9] = egg_res[5];
			paras[10] = egg_location;

			memo.draw(ConV.sprintf(format, paras));

			if (epk.shiny_leaf > 0) {
				if (epk.shiny_leaf == 6) {
					shinycrown.draw(Localisation.getSummaryShinyLeafs(), 0, false);
				} else {
					shinycrown.draw(Localisation.getSummaryShinyLeafs(), 1, false);
					shinycrown.getChild(0).drawMulti(epk.shiny_leaf, -1);
				}
			}
		} else if (selection == 1) {
			drawSkillsPage();
		} else {
			drawPerformacePage();
		}
	}

	private void drawPerformacePage() {
		performance.drawMulti(5, -1);
		// TODO
		if (status == RIBBONINFO) {
			ribbonicon.draw();
		}
	}

	private void drawSkillsPage() {
		ExtPokemon epk = team[currentPokemon];

		healthbar.draw(Localisation.getSummaryHPTitle());
		int max_hp = epk.stats[Statistic.HEALTH.getID()];

		int t;
		if (epk.hp * 2 > epk.stats[Statistic.HEALTH.getID()]) {
			t = 0;
		} else if (epk.hp * 5 >= epk.stats[Statistic.HEALTH.getID()]) {
			t = 1;
		} else {
			t = 2;
		}
		int width = epk.hp * health.columns / epk.stats[Statistic.HEALTH.getID()];
		Cell ch = healthbar.getChild(0);
		for (int i = 0; i < width; i++) {
			ch.drawMulti(null, i, t, false);
		}

		assert (max_hp <= 999);
		healthbar.getChild(1).draw(ConV.sprintf(Localisation.getSummaryHP(), epk.hp, max_hp));

		int[] statOrder = DataLoader.getTeamInfoStatsOrder();
		char[][] statNames = Localisation.getTeamInfoStatNames();
		for (int i = 0; i < epk.stats.length - 1; i++) {
			int stat = statOrder[i];
			if (epk.nature.getID() / 5 == stat && epk.nature.getID() % 5 != stat) {
				stats.getChildMulti(0, i).draw(statNames[stat]);
			} else if (epk.nature.getID() % 5 == stat && epk.nature.getID() / 5 != stat) {
				stats.getChildMulti(1, i).draw(statNames[stat]);
			} else {
				stats.drawMulti(statNames[stat], i);
			}
			stats.getChildMulti(2, i).draw(ConV.toCString(epk.stats[stat]));
		}

		ability.draw(Localisation.getSummaryAbilityTitle());
		ability.getChild(0).draw(Localisation.getAbilityName(epk.ability));
		ability.getChild(1).draw(Localisation.getAbilityText(epk.ability));
	}

	private void drawGeneralInfo() {
		ExtPokemon epk = team[currentPokemon];

		generelinfo.draw(epk.getName());
		generelinfo.getChild(0).draw(epk.pokeball);
		if (!epk.isEgg()) {
			generelinfo.getChild(1).draw(ConV.sprintf(Localisation.getSummaryLevel(), epk.level));
			if (epk.gender == Gender.MALE) {
				generelinfo.getChild(2).draw(0);
			} else if (epk.gender == Gender.FEMALE) {
				generelinfo.getChild(2).draw(1);
			}
		}
		if (epk.status != StatusCondition.OK) {
			generelinfo.getChild(3).draw(epk.status.getID() - 1, false);
		} else if (epk.pokerus.isActive()) {
			generelinfo.getChild(4).draw();
		}

		pokemonicon.draw(new Icon(GraphicHandler.getPokemonIcon(epk, 1), 0, 0), null, false);
		if (epk.shiny) {
			shinystar.draw();
		}
		if (epk.pokerus == Pokerus.HEALED) {
			pokeruscured.draw();
		}

		for (int i = 0; i < epk.markings.length; i++) {
			markings.drawMulti(null, i, epk.markings[i]);
		}

		itembox.draw(Localisation.getSummaryItemTitle(), false);
		if (epk.itemid != 0) {
			itembox.getChild(0).draw(new Icon(GraphicHandler.getItemIcon(epk.itemid), 0, 0), Localisation.getItemName(epk.itemid), false);
		} else {
			itembox.getChild(0).draw(new Icon(), Localisation.getItemName(epk.itemid), false);
		}
	}

	/**
	 * Draws the info page for an attack.
	 */
	private void drawMoveInfo() {
		ExtPokemon epk = team[currentPokemon];
		int attack = epk.attackids[selection];
		infobox.draw(Localisation.getAttackText(attack));
		// TODO mirror pokemon icon ?
		infobox.getChild(0).draw(new Icon(GraphicHandler.getPokemonIcon(epk, 0), 0, 0), null, false);
		Cell itc = infobox.getChild(1);
		itc.drawMulti(null, 0, epk.type1.getID(), false);
		if (epk.type2 != Type.NONE) {
			itc.drawMulti(null, 1, epk.type2.getID(), false);
		}

		Cell isc = infobox.getChild(2);
		isc.drawMulti(Localisation.getSummaryMoveTitles(), 3, -1);
		isc.getChildMulti(1, 0).draw(InfoLoader.getContactType(attack).getID(), false);
		int power3 = InfoLoader.getPower(attack);
		if (power3 == 0) {
			isc.getChildMulti(0, 1).draw(Localisation.getSummaryEmptyMoveStat());
		} else {
			isc.getChildMulti(0, 1).draw(ConV.toCString(power3));
		}
		int accuracy3 = InfoLoader.getAccuracy(attack);
		if (accuracy3 == 0) {
			isc.getChildMulti(0, 2).draw(Localisation.getSummaryEmptyMoveStat());
		} else {
			isc.getChildMulti(0, 2).draw(ConV.toCString(accuracy3));
		}
	}

	/**
	 * Draws the info page for an attack.
	 */
	private void drawMoveAccept() {
		swapattack.draw(Localisation.getSummarySwitchMoves());
	}

	/**
	 * Draws the pokemon selection.
	 */
	private void drawPokemonNavigation() {
		for (int i = 0; i < team.length; i++) {
			if (status != RIBBONINFO) {
				pokemonnavigation.drawMulti(null, i, 0, currentPokemon == i);
			} else {
				pokemonnavigation.drawMulti(null, i, 1, currentPokemon == i);
			}
			if (team[i] != null) {
				pokemonnavigation.getChildMulti(0, i).draw(new Icon(GraphicHandler.getPokemonIcon(team[i], 0), 0, 0), null, false);
			}
		}
	}

	/**
	 * Draws the pokemon selection.
	 */
	private void drawInfoPageNavigation() {
		int sel = selection;
		if (status == MOVEATTACK || status == ATTACKINFO) {
			sel = 1;
		}
		if (team[currentPokemon].isEgg()) {
			navigation.drawMulti(1, sel);
		} else {
			navigation.drawMulti(3, sel);
		}
	}

	/**
	 * Draws the backgrounds for the info pages.
	 */
	private void drawInfoBackground() {
		int t;
		if (status == MOVEATTACK || status == ATTACKINFO) {
			t = 1;
		} else if (team[currentPokemon].isEgg()) {
			t = 3;
		} else if (status == RIBBONINFO) {
			t = 2;
		} else {
			t = selection;
		}
		char[] text = Localisation.getSummaryTitleR()[t];
		backgroundr.draw(text, t, false);
	}

	private void drawInfoBackgroundL() {
		int t;
		if (status == MOVEATTACK || status == ATTACKINFO) {
			t = 1;
		} else if (status == RIBBONINFO) {
			t = 2;
		} else {
			t = selection;
		}
		char[] text = null;
		if (selection != 2 || !SystemControll.getFlag(SystemControll.PerformanceHidden)) {
			text = Localisation.getSummaryTitleL()[t];
		}
		backgroundl.draw(text, t, false);
	}

	private void drawMoveSelection() {
		if (status == MOVEATTACK) {
			if (selection == moveFrom) {
				attacks.getChildMulti(3, selection).draw(2, false);
			} else {
				attacks.getChildMulti(3, selection).draw(0, false);
				attacks.getChildMulti(3, moveFrom).draw(1, false);
			}
		} else {
			attacks.getChildMulti(3, selection).draw(0, false);
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

	private void attackInfoClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}

		int sel = attacks.clickMulti(g.getMouseX(), g.getMouseY(), Pokemon.ATTACKS);
		if (sel != -1 && team[currentPokemon].attackids[sel] != 0) {
			g.processMouse();
			selection = sel;
			return;
		}

		if (swapattack.click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			swapattack.animate(false);
			return;
		}

		if (backbutton.click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			backbutton.animate(false);
		}
	}
}
