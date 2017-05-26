package sequenze;

import sequenze.overlay.MenuObjectHandler;
import sequenze.overlay.TextInput;
import sequenze.menu.Menu;
import sequenze.battle.BattleMenu;
import util.Logger;
import script.Interpreter;
import script.ScriptException;

import static sequenze.InstancePackage.*;

import sequenze.awt.FilterAWT;
import sequenze.awt.GameAWT;
import sequenze.awt.GraphicDisplayAWT;
import sequenze.awt.SoundHandlerAWT;
import sequenze.opengl.FilterGL;
import sequenze.opengl.GameGL;
import sequenze.opengl.GraphicDisplayGL;
import sequenze.opengl.SoundHandlerGL;
import util.ConV;
import util.Graph;

public class Sequenze {

	/** A Reference to the currently running interpreter */
	Interpreter current;

	ScriptException sc;

	StartMenu start;

	/** Switch about whether to user the OpenGL library or stay with pure java */
	private final boolean UseGL = false;
	/** Switch about whether to capture the time needed per frame-update and print them at the end */
	private final boolean DisplaySPF = true;
	private final boolean DisplaySPFGraph = false;

	private final int FPS = 40;

	public static void main(String[] args) {
		new Sequenze().run();
	}

	private Sequenze() {
	}

	private void run() {
		int[] spf = new int[200];
		Graph graph;
		if (DisplaySPFGraph) {
			graph = new Graph(1000, spf.length);
		}
		long upd = System.nanoTime() / 1000000;
		try {
			initialize();
			int frames = 0;
			while (!GraphicHandler.isCloseRequested() && !game.close) {
				long start_time = System.nanoTime() / 1000000;
				if (start_time - upd < (1000 / FPS)) {
					continue;
				}
				game.input();
				if (game.screenshot) {
					GraphicHandler.takeScreenshot();
					game.screenshot = false;
					game.paused = true;
				}
				if (game.paused) {
					updatePause();
					draw();
				} else {
					update();
					draw();
				}
				frames++;
				if (frames == FPS) {
					frames = 0;
					SystemControll.setVariable(SystemControll.TimePlayed, SystemControll.getVariable(SystemControll.TimePlayed) + 1);
				}
				if (DisplaySPF) {
					int delta_time = (int) (System.nanoTime() / 1000000 - start_time);
					if (delta_time < spf.length - 1) {
						spf[delta_time]++;
					} else {
						spf[spf.length - 1]++;
					}
					if (DisplaySPFGraph) {
						graph.draw(delta_time);
					}
				}
				upd = upd + (1000 / FPS);
			}
		} catch (Throwable t) {
			Logger.add(t);
		} finally {
			close();
			if (DisplaySPFGraph) {
				graph.close();
			}
		}
		if (DisplaySPF) {
			Logger.add(Logger.SYSTEM, "Time needed to update and draw a frame:");
			if (spf[0] > 0) {
				Logger.add(Logger.SYSTEM, "  <1 ms/f: ", spf[0], "x");
			}
			for (int i = 1; i < spf.length - 1; i++) {
				if (spf[i] != 0) {
					Logger.add(Logger.SYSTEM, (i < 100 ? "  " : " "), (i < 10 ? " " : ""), i, " ms/f: ", spf[i], "x");
				}
			}
			if (spf[spf.length - 1] > 0) {
				Logger.add(Logger.SYSTEM, ">200 ms/f: ", spf[spf.length - 1], "x");
			}
			Logger.flush();
		}
	}

	private static void close() {
		if (world != null) {
			world.unloadAll();
		}
		GraphicHandler.close();
		SoundManager.close();
		Logger.flush();
	}

	private void drawPause() {
		GraphicHandler.drawRectangleMax(127, 127, 127, 127);
		Font.drawStringMiddle(Localisation.getPauseText(), 0, 0, 256, 256, 0, 0x1);
	}

	/** Draws the game to the screen */
	private void draw() {
		player.calcPosition();
		int xf = vfx.getCameraX(player.xf);
		int yf = vfx.getCameraY(player.yf);
		GraphicHandler.clear();

		// TODO skip drawing of menu items when a menu object covers the screen
		GraphicHandler.changeToLeft();
		if (!game.paused || !SystemControll.getFlag(SystemControll.HideWhilePause)) {
			if (!start.isActive()) {
				if (bm.isFinished()) {
					if (!menu.occupiesLeft()) {
						GraphicHandler.translate(16 * 8 - xf, 16 * 8 - yf);
						world.drawLDown(xf, yf);
						// shud.drawL(xf, yf);
						player.drawLDown();
						world.drawLUp(xf, yf);
						player.drawLUp();
						GraphicHandler.translate(-16 * 8 + xf, -16 * 8 + yf);
					}
					menu.drawL();
				} else {
					bm.drawL();
				}
			}
			gui.drawL();
			vfx.drawL();
			start.drawL();
			filter.applyLeft();
		}
		if (game.paused) {
			drawPause();
		}
		GraphicHandler.changeToRight();

		if (!game.paused || !SystemControll.getFlag(SystemControll.HideWhilePause)) {
			if (!start.isActive()) {
				if (bm.isFinished()) {
					player.drawR();
					menu.drawR();
				} else {
					bm.drawR();
				}
			}
			gui.drawR();
			vfx.drawR();
			start.drawR();
			filter.applyRight();
		}
		if (game.paused) {
			drawPause();
		}
		GraphicHandler.drawToScreen();
	}

	/** Initializes everything the game needs, to start */
	private void initialize() {
		if (UseGL) {
			GraphicHandler.init(new GraphicDisplayGL());
			game = new GameGL();
			filter = new FilterGL();
		} else {
			GraphicDisplayAWT gd = new GraphicDisplayAWT();
			GraphicHandler.init(gd);
			game = new GameAWT(gd.getScreen());
			filter = new FilterAWT(gd);
		}
		sc = new ScriptException();
		bm = new BattleMenu();
		SystemControll.init();
		EventMethodLibrary.init();
		EventControll.init();
		if (UseGL) {
			SoundManager.set(new SoundHandlerGL());
		} else {
			SoundManager.set(new SoundHandlerAWT());
		}
		vfx = new VisualEffects();
		player = new Player();
		gui = new MenuObjectHandler();
		// shud = new SemiHUD();
		XPType.initLoockUp();
		TextInput.init();
		InfoLoader.init();
		start = new StartMenu();
		menu = new Menu();
	}

	/** Updates all parts of the game that are currently active */
	private void update() {
		if (game.wasPressed(Button.PAUSE) && SystemControll.getFlag(SystemControll.Pausable)) {
			game.process(Button.PAUSE);
			game.paused = true;
			reloadGraphics();
		}
		SoundManager.update();
		MessageHandler.free();
		if (MessageHandler.hasMessage(MessageHandler.EVENT)) {
			Interpreter i = (Interpreter) MessageHandler.getObject();
			if (doScript(i)) {
				current = i;
				player.stopMovement();
				return;
			}
		}
		vfx.update();
		if (!vfx.active()) {
			return;
		}
		gui.update(game);
		if (gui.isActive()) {
			menu.clickABox(game);
			return;
		}
		start.update(game);
		if (start.isActive()) {
			return;
		}
		Event[] list = world.getEvents(player.x, player.y);
		for (Event e : list) {
			e.checkActive();
		}
		if (SystemControll.getFlag(SystemControll.WaitForSound)) {
			if (SoundManager.isActive()) {
				return;
			}
			SystemControll.setFlag(SystemControll.WaitForSound, false);
		}
		if (SystemControll.getFlag(SystemControll.CompleteMoveRoutes)) {
			boolean hasMoved = false;
			for (Event e : list) {
				if (e.getType() != Script.PARALLEL && e.hasToMove()) {
					e.update(game, world, list);
					hasMoved = true;
				}
			}
			if (player.hasToMove()) {
				player.move(game, world);
				hasMoved = true;
			}
			if (hasMoved) {
				return;
			} else {
				SystemControll.setFlag(SystemControll.CompleteMoveRoutes, false);
			}
		}
		if (current != null) {
			if (doScript(current)) {
				return;
			}
			current = null;
		}
		doMessageScripts(list);
		if (current != null) {
			return;
		}
		bm.update(game, team.team);
		if (!bm.isFinished()) {
			return;
		}
		menu.update(game);
		if (menu.hasFocus()) {
			return;
		}
		doAutoScripts(list);
		if (current != null) {
			return;
		}
		checkParallelScripts(list);
		for (Event e : list) {
			e.update(game, world, list);
		}
		player.update(game, world);
	}

	private void reloadGraphics() {
		GraphicHandler.unloadTextures(team.tex);
		team.tex = GraphicHandler.giveTextures("Team");
		team.setupCells();
		GraphicHandler.unloadTextures(bag.tex);
		bag.tex = GraphicHandler.giveTextures("Bag");
		bag.setupCells();
		GraphicHandler.unloadTextures(gadget.tex);
		gadget.tex = GraphicHandler.giveTextures("Gadget");
		GraphicHandler.unloadTextures(menu.tex);
		menu.tex = GraphicHandler.giveTextures("IGMenu");
		menu.setupCells();
		GraphicHandler.unloadTextures(gui.tex);
		gui.tex = GraphicHandler.giveTextures("Menu");
		GraphicHandler.unloadTextures(options.tex);
		options.tex = GraphicHandler.giveTextures("Options");
		options.setupCells();
		GraphicHandler.unloadTextures(player.tex);
		player.tex = GraphicHandler.giveTextures("Hero");
		GraphicHandler.unloadTextures(card.tex);
		card.tex = GraphicHandler.giveTextures("TrainerCard");
		card.setupCells();
		GraphicHandler.unloadTextures(summary.tex);
		summary.tex = GraphicHandler.giveTextures("Summary");
		summary.setupCells();
		GraphicHandler.unloadTextures(start.tex);
		start.tex = GraphicHandler.giveTextures("StartMenu");
		start.setupCells();
		GraphicHandler.unloadTextures(bm.tex);
		bm.tex = GraphicHandler.giveTextures("BattleMenu");
		bm.setupCells();
		GraphicHandler.unloadTextures(TextInput.tex);
		TextInput.init();
		GraphicHandler.unloadIndexedTextures(Font.textindtex);
		GraphicHandler.unloadPalettes(Font.palettes);
		Font.initText();

		FlagHandler fh = FlagHandler.current();
		fh.initTeamLoader();
		team.team = new ExtPokemon[SystemControll.getVariable(SystemControll.TeamSize)];
		for (int i = 0; i < team.team.length; i++) {
			team.team[i] = fh.nextTeamPokemon();
		}
	}

	private void updatePause() {
		if (game.wasPressed(Button.PAUSE)) {
			game.process(Button.PAUSE);
			game.paused = false;
		}
	}

	private boolean doScript(Interpreter ev) {
		boolean finished = ev.doStep(sc);
		if (!sc.isInitialized()) {
			return finished;
		}
		game.paused = true;
		sc.addStackElement("Update Handler");
		Logger.add(sc);
		sc = new ScriptException();
		return false;

	}

	// TODO also check for menu objects after each script to prevent double message
	private void checkParallelScripts(Event[] list) {
		for (Event e : list) {
			if (e.getType() == Script.PARALLEL) {
				doScript(e.startScript());
			}
		}
	}

	private void doAutoScripts(Event[] list) {
		for (Event e : list) {
			if (e.getType() != Script.AUTOSCRIPT) {
				continue;
			}
			Interpreter i = e.startScript();
			if (doScript(i)) {
				current = i;
				break;
			}
		}
	}

	private void doMessageScripts(Event[] list) {
		for (Event e : list) {
			if (e.getType() != Script.MESSAGE) {
				continue;
			}
			if (!MessageHandler.hasMessage(e.getMessage())) {
				continue;
			}
			Interpreter i = e.startScript();
			if (doScript(i)) {
				current = i;
				break;
			}
		}
	}

}
