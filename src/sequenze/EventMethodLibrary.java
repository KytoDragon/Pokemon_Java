package sequenze;

import script.*;

import static sequenze.InstancePackage.*;

import sequenze.overlay.ItemSelector;
import sequenze.overlay.MCQuestion;
import sequenze.overlay.MenuObjectHandler;
import sequenze.overlay.PokemonSelector;
import sequenze.overlay.TextBox;
import sequenze.overlay.TextInput;
import sequenze.overlay.YNQuestion;
import sequenze.world.World;
import util.*;

public class EventMethodLibrary implements MethodLibrary {

	private static EventMethodLibrary pml;
	private static int MAX_STRING_LENGTH = 64;
	private static StringMap<Module> modules;
	private static StringMap<ModuleMethod>[] methods;
	private static Map<Class, StringMap<ObjectMethod>> obmethods;

	private static enum Module implements util.Enum {

		Text,
		Input,
		Sound,
		Controll,
		World,
		Team,
		Fight,
		Warp,
		Bag,
		Localisation,
		VFX;

		@Override
		public int getID() {
			return this.ordinal();
		}

		public static int length() {
			return values().length;
		}
	}

	public static void init() {
		pml = new EventMethodLibrary();
		modules = new StringMap<>();
		for (Module module : Module.values()) {
			modules.put(module.name().toCharArray(), module);
		}

		methods = new StringMap[modules.size()];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = new StringMap<>();
		}
		pml.initMethods();

		obmethods = new Map<>();
		pml.initObMethods();
		obmethods.get(ExtPokemon.class).putAll(obmethods.get(Pokemon.class));
		obmethods.get(Player.class).putAll(obmethods.get(Entity.class));
	}

	private void initMethods() {
		methods[Module.Text.getID()].put("say", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			boolean left = false;
			if (paras.length >= 2) {
				left = paras[1].getBool();
			}
			MenuObjectHandler.current.setText(new TextBox(paras[0].getCString(), left));
			return null;
		});
		methods[Module.Text.getID()].put("sayLocal", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			MenuObjectHandler.current.setText(new TextBox(Localisation.getEventText(paras[0].getInt()), true));
			return null;
		});
		methods[Module.Text.getID()].put("getLocal", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(Localisation.getEventText(paras[0].getInt()));
		});
		methods[Module.Input.getID()].put("ask", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			// TODO reset needed?
			SystemControll.setFlag(SystemControll.QuestionAnswer, false);
			MenuObjectHandler.current.setObject(new YNQuestion(paras[0].getCString()));
			return null;
		});
		methods[Module.Input.getID()].put("askMultiple", (paras, s) -> {
			if (check(paras, 3, s) || checkNum(paras[1], paras[2].getInt() + 1, s)) {
				return null;
			}
			SystemControll.setVariable(SystemControll.MCQuestionAnswer, 0);
			VariableArray va = cast(paras[2], VariableArray.class, s);
			if (s.isInitialized()) {
				return null;
			}
			char[][] answers = new char[paras[1].getInt()][];
			for (int i = 0; i < answers.length; i++) {
				Variable var = va.getElement(ConV.toCString(i), s);
				if (s.isInitialized()) {
					return null;
				}
				answers[i] = var.getCString();
			}
			MenuObjectHandler.current.setObject(new MCQuestion(paras[0].getCString(), answers));
			return null;
		});
		methods[Module.Input.getID()].put("getName", (paras, s) -> {
			TextInput input = new TextInput(16, false, Gender.IRRELEVANT);
			MenuObjectHandler.current.setObject(input);
			return null;
		});
		methods[Module.Input.getID()].put("selectPokemon", (paras, s) -> {
			PokemonSelector pkmnsel = new PokemonSelector();
			MenuObjectHandler.current.setObject(pkmnsel);
			return null;
		});
		methods[Module.Input.getID()].put("selectItem", (paras, s) -> {
			ItemSelector itemsel = new ItemSelector();
			MenuObjectHandler.current.setObject(itemsel);
			return null;
		});
		methods[Module.Input.getID()].put("clear", (paras, s) -> {
			MenuObjectHandler.current.closeObject();
			return null;
		});
		methods[Module.Sound.getID()].put("startBGM", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			SoundManager.startBGM(paras[0].getString());
			return null;
		});
		methods[Module.Sound.getID()].put("stopBGM", (paras, s) -> {
			SoundManager.stopBGM();
			return null;
		});
		methods[Module.Sound.getID()].put("pauseBGM", (paras, s) -> {
			SoundManager.pauseBGM();
			return null;
		});
		methods[Module.Sound.getID()].put("unpauseBGM", (paras, s) -> {
			SoundManager.unpauseBGM();
			return null;
		});
		methods[Module.Sound.getID()].put("fadeOutBGM", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			int frames = paras[0].getInt();
			if (frames < 0) {
				s.initMessage("Negative fade out time: ", frames);
				return null;
			}
			SoundManager.fadeOutBGM(frames);
			return null;
		});
		methods[Module.Sound.getID()].put("playSE", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			SoundManager.playSE(paras[0].getString(), false);
			return null;
		});
		methods[Module.Sound.getID()].put("playAndWait", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			SoundManager.playSE(paras[0].getString(), true);
			SystemControll.setFlag(SystemControll.WaitForSound, true);
			return null;
		});
		methods[Module.Sound.getID()].put("cry", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			// TODO id bounds check
			// TODO store pokemon cry differently ?
			String sound_name = "cry" + paras[1].getString();
			SoundManager.playSE(sound_name, true);
			SystemControll.setFlag(SystemControll.WaitForSound, true);
			return null;
		});
		// TODO check bounds
		methods[Module.Controll.getID()].put("getVar", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableInt(EventControll.getVariable(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setVar", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			EventControll.setVariable(paras[0].getInt(), paras[1].getInt());
			return null;
		});
		methods[Module.Controll.getID()].put("getFlag", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableBool(EventControll.getFlag(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setFlag", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			EventControll.setFlag(paras[0].getInt(), paras[1].getBool());
			return null;
		});
		methods[Module.Controll.getID()].put("getPokemon", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableObject(EventControll.getPokemon(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setPokemon", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			Pokemon p = cast(paras[1], Pokemon.class, s);
			EventControll.setPokemon(paras[0].getInt(), p);
			return null;
		});
		methods[Module.Controll.getID()].put("getString", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(EventControll.getString(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setString", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			char[] str = paras[1].getCString();
			if (str.length >= MAX_STRING_LENGTH) {
				s.initMessage("String is too large for storage: ", str, ", maximum is ", MAX_STRING_LENGTH);
				return null;
			}
			EventControll.setString(paras[0].getInt(), str);
			return null;
		});
		methods[Module.Controll.getID()].put("getSysString", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(SystemControll.getString(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setSysString", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			char[] str = paras[1].getCString();
			if (str.length >= MAX_STRING_LENGTH) {
				s.initMessage("String is too large for storage: ", str, ", maximum is ", MAX_STRING_LENGTH);
				return null;
			}
			SystemControll.setString(paras[0].getInt(), str);
			return null;
		});
		methods[Module.Controll.getID()].put("getSysVar", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableInt(SystemControll.getVariable(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setSysVar", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			SystemControll.setVariable(paras[0].getInt(), paras[1].getInt());
			return null;
		});
		methods[Module.Controll.getID()].put("getSysFlag", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableBool(SystemControll.getFlag(paras[0].getInt()));
		});
		methods[Module.Controll.getID()].put("setSysFlag", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			SystemControll.setFlag(paras[0].getInt(), paras[1].getBool());
			return null;
		});
		methods[Module.Controll.getID()].put("throw", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			if (!MessageHandler.tryadd(paras[0].getCString(), paras.length >= 2 ? paras[1].getObject() : null)) {
				s.initMessage("Could not trow message");
			}
			return null;
		});
		methods[Module.World.getID()].put("getEvents", (paras, s) -> {
			Event[] events = world.getEvents(player.x, player.y);
			return new VariableArray(events);
		});
		methods[Module.World.getID()].put("getPlayer", (paras, s) -> {
			Player player = InstancePackage.player;
			return new VariableObject(player);
		});
		methods[Module.World.getID()].put("getEvent", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			Event[] events = world.getEvents(player.x, player.y);
			for (Event event : events) {
				if (ConV.equals(paras[0].getCString(), event.name)) {
					return new VariableObject(event);
				}
			}
			s.initMessage("Could not find event with name ", paras[0].getCString());
			return null;

		});
		methods[Module.Team.getID()].put("getTeam", (paras, s) -> {
			ExtPokemon[] pokemon = team.team;
			return new VariableArray(pokemon);
		});
		methods[Module.Team.getID()].put("getPokemon", (paras, s) -> {
			if (check(paras, 1, s) || checkNum(paras[1], SystemControll.getVariable(SystemControll.TeamSize), s)) {
				return null;
			}
			ExtPokemon[] pokemon = team.team;
			return new VariableObject(pokemon[paras[0].getInt()]);
		});
		methods[Module.Team.getID()].put("setTeam", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			VariableArray pokemon = cast(paras[0], VariableArray.class, s);
			if (s.isInitialized()) {
				return null;
			}
			for (int i = 0; i < SystemControll.getVariable(SystemControll.TeamSize); i++) {
				Pokemon p = cast(pokemon.getElement(ConV.toCString(i), s), Pokemon.class, s);
				if (s.isInitialized()) {
					return null;
				}
				team.team[i] = p.extend();
			}
			return null;
		});
		methods[Module.Team.getID()].put("setPokemon", (paras, s) -> {
			if (check(paras, 2, s) || checkNum(paras[1], SystemControll.getVariable(SystemControll.TeamSize), s)) {
				return null;
			}
			Pokemon p = cast(paras[0], Pokemon.class, s);
			if (s.isInitialized()) {
				return null;
			}
			int id = paras[1].getInt();
			team.team[id] = p.extend();
			return null;
		});
		methods[Module.Team.getID()].put("newPokemon", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			// TODO id bounds check
			Pokemon p = new Pokemon();
			p.id = paras[0].getInt();
			p.generatePersonality();
			return new VariableObject(p);
		});
		methods[Module.Fight.getID()].put("start", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			VariableArray arr = cast(paras[0], VariableArray.class, s);
			if (s.isInitialized()) {
				return null;
			}
			StringMap<Variable> map = arr.getObject();
			ExtPokemon[] enemy_team = new ExtPokemon[map.size()];
			int i = 0;
			for (StringMap.Entry<Variable> entry : map) {
				Pokemon pokemon = cast(entry.getValue(), Pokemon.class, s);
				if (s.isInitialized()) {
					return null;
				}
				enemy_team[i++] = pokemon.extend();
			}
			bm.start(enemy_team[0], team.team[0]);
			if (!MessageHandler.tryadd(MessageHandler.FIGHT, enemy_team)) {
				s.initMessage("Could not start fight, another action already in progress.");
				return null;
			}
			return null;
		});
		methods[Module.Warp.getID()].put("teleport", (paras, s) -> {
			if (check(paras, 3, s)) {
				return null;
			}
			char[] world_name = paras[0].getCString();
			World new_world = World.getWorld(world_name);
			if (new_world == null) {
				s.initMessage("Could not load world: ", world_name);
				return null;
			}
			int x = paras[1].getInt();
			int y = paras[2].getInt();
			if (world.isOutsideMatrix(x, y)) {
				s.initMessage("Invalid coordibates in world: ", world_name, " x:", x, " y:", y);
				return null;
			}
			world.unloadAll();
			world = new_world;
			player.x = x;
			player.y = y;
			return null;
		});
		methods[Module.Bag.getID()].put("add", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			bag.addItem(paras[0].getInt(), paras[1].getInt());
			return null;
		});
		methods[Module.Bag.getID()].put("has", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			boolean had = bag.containsItem(paras[0].getInt(), paras[1].getInt());
			return new VariableBool(had);
		});
		methods[Module.Bag.getID()].put("remove", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			boolean success = bag.removeItem(paras[0].getInt(), paras[1].getInt());
			return new VariableBool(success);
		});
		methods[Module.Localisation.getID()].put("getItem", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(Localisation.getItemName(paras[0].getInt()));
		});
		methods[Module.Localisation.getID()].put("getAbility", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(Localisation.getAbilityName(paras[0].getInt()));
		});
		methods[Module.Localisation.getID()].put("getAttack", (paras, s) -> {
			if (check(paras, 1, s)) {
				return null;
			}
			return new VariableString(Localisation.getAttackName(paras[0].getInt()));
		});
		methods[Module.Localisation.getID()].put("getPokemon", (paras, s) -> {
			if (check(paras, 2, s)) {
				return null;
			}
			return new VariableString(Localisation.getPokemonName(paras[0].getInt(), paras[1].getInt()));
		});
		methods[Module.VFX.getID()].put("fadescreen", (paras, s) -> {
			if (check(paras, 1, s) || checkNum(paras[0], 4, s)) {
				return null;
			}
			// TODO
			return null;
		});
	}

	@Override
	public int getModuleId(char[] name) {
		if (!modules.containsKey(name)) {
			return -1;
		}
		return modules.get(name).getID();
	}

	@Override
	public boolean hasMethod(int module, char[] name) {
		return methods[module].containsKey(name);
	}

	private static boolean check(Variable[] paras, int num, ScriptException s) {
		if (paras.length < num) {
			s.initMessage("Not enough parameters for method : ", paras.length, " < ", num);
			return true;
		}
		return false;
	}

	@Override
	public Variable callMethod(int module_id, char[] method, Variable[] paras, ScriptException s) {
		if (module_id < 0 || module_id >= Module.length()) {
			s.initMessage("Unknown module id: ", module_id);
			return null;
		}
		ModuleMethod exc = methods[module_id].get(method);
		if (exc == null) {
			s.initMessage("Unknown method ", method, " in module ", Module.values()[module_id].name());
			return null;
		}
		Variable result = exc.run(paras, s);
		if (s.isInitialized()) {
			s.addStackElement("Method: ", method, ", Module: ", Module.values()[module_id].name());
			return null;
		}
		return result;
	}

	@Override
	public Variable callObject(Variable object, char[] method, Variable[] paras, ScriptException s) {
		if (object.getObject() == null) {
			s.initMessage("Null pointer on method ", method);
			return null;
		} else if(!obmethods.containsKey(object.getObject().getClass())) {
			s.initMessage("Unknown object type: ", object.getObject().getClass().getName());
			return null;
		}
		ObjectMethod exc = obmethods.get(object.getObject().getClass()).get(method);
		if (exc == null) {
			s.initMessage("Unknown method ", method, " for object ", object.getCString());
			return null;
		}
		Variable result = exc.run(object.getObject(), paras, s);
		if (s.isInitialized()) {
			s.addStackElement("Method: ", method, ", Object: ", object.getCString());
			return null;
		}
		return result;
	}

	private static boolean checkNum(Variable v, int max, ScriptException s) {
		if (v.getInt() < 0) {
			s.initMessage("Index for method is negative: ", v.getInt());
			return true;
		} else if (v.getInt() >= max) {
			s.initMessage("Index for method is too big: ", v.getInt(), " >= ", max);
			return true;
		}
		return false;
	}

	private void initObMethods() {
		obmethods.put(Player.class, new StringMap<>());
		obmethods.get(Player.class).put("setStatus", (object, paras, s) -> {
			Player player = (Player) object;
			if (check(paras, 1, s)) {
				return null;
			}
			String name = paras[0].getString();
			EntityStatus pls;
			try {
				pls = EntityStatus.valueOf(name);
			} catch (IllegalArgumentException e) {
				s.initMessage("No player status with the name: ", name);
				return null;
			}
			player.setStatus(pls);
			return null;
		});
		obmethods.get(Player.class).put("move", (object, paras, s) -> {
			Player player = (Player) object;
			player.calcXY();
			return null;
		});
		obmethods.get(Player.class).put("bike", (object, paras, s) -> {
			Player player = (Player) object;
			game.bike = !game.bike;
			return null;
		});
		obmethods.put(Entity.class, new StringMap<>());
		obmethods.get(Entity.class).put("lookAt", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 2, s)) {
				return null;
			}
			entity.orientation = Direction.getDirection(entity.x, entity.y, paras[0].getInt(), paras[1].getInt());
			return null;
		});
		obmethods.get(Entity.class).put("lookAtPlayer", (object, paras, s) -> {
			Entity entity = (Entity) object;
			entity.orientation = Direction.getDirection(entity.x, entity.y, player.x, player.y);
			return null;
		});
		obmethods.get(Entity.class).put("facePlayer", (object, paras, s) -> {
			Entity entity = (Entity) object;
			entity.orientation = Direction.getSimpleDirection(entity.x, entity.y, player.x, player.y);
			return null;
		});
		obmethods.get(Entity.class).put("setMoveRoute", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (paras.length > 0) {
				int i = 0;
				try {
					String[] list = new String[paras.length];
					for (; i < list.length; i++) {
						list[i] = Moveroute.getMoveroute(paras[i].getString());
					}
					entity.setMoveRoute(list);
				} catch (IllegalArgumentException iae) {
					s.initMessage("Unknown move route command: \"", paras[i].getCString(), "\"");
					return null;
				}
			}
			return null;
		});
		obmethods.get(Entity.class).put("approachPlayer", (object, paras, s) -> {
			Entity entity = (Entity) object;
			entity.orientation = Direction.getSimpleDirection(entity.x, entity.y, player.x, player.y);
			int dx = ConV.abs(entity.x - player.x);
			int dy = ConV.abs(entity.y - player.y);
			if (dx + dy - 1 > 0) {
				String[] list = new String[dx + dy - 1];
				for (int i = 0; i < list.length; i++) {
					list[i] = Moveroute.FORWARD;
				}
				entity.setMoveRoute(list);
			}
			return null;
		});
		obmethods.get(Entity.class).put("getX", (object, paras, s) -> {
			Entity entity = (Entity) object;
			return new VariableInt(entity.x);
		});
		obmethods.get(Entity.class).put("setX", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s)) {
				return null;
			}
			entity.x = paras[0].getInt();
			return null;
		});
		obmethods.get(Entity.class).put("getY", (object, paras, s) -> {
			Entity entity = (Entity) object;
			return new VariableInt(entity.y);
		});
		obmethods.get(Entity.class).put("setY", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s)) {
				return null;
			}
			entity.y = paras[0].getInt();
			return null;
		});
		obmethods.get(Entity.class).put("getZ", (object, paras, s) -> {
			Entity entity = (Entity) object;
			return new VariableInt(entity.z);
		});
		obmethods.get(Entity.class).put("setZ", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s)) {
				return null;
			}
			entity.z = paras[0].getInt();
			return null;
		});
		obmethods.get(Entity.class).put("getOrientation", (object, paras, s) -> {
			Entity entity = (Entity) object;
			return new VariableObject(entity.orientation);
		});
		obmethods.get(Entity.class).put("setOrientation", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s)) {
				return null;
			}
			Direction direction = cast(paras[0], Direction.class, s);
			if (s.isInitialized()) {
				return null;
			}
			entity.orientation = direction;
			return null;
		});
		obmethods.get(Entity.class).put("getOrientationId", (object, paras, s) -> {
			Entity entity = (Entity) object;
			return new VariableInt(entity.orientation.getID());
		});
		obmethods.get(Entity.class).put("setOrientationId", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s) || checkNum(paras[0], 4, s)) {
				return null;
			}
			try {
				entity.orientation = Direction.getByID((byte) paras[0].getInt());
			} catch (IllegalArgumentException iae) {
				s.initMessage(iae.getMessage());
				return null;
			}
			return null;
		});
		obmethods.get(Entity.class).put("getSwitch", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 1, s) || checkNum(paras[0], entity.switches.length, s)) {
				return null;
			}
			return new VariableBool(entity.switches[paras[0].getInt()]);
		});
		obmethods.get(Entity.class).put("setSwitch", (object, paras, s) -> {
			Entity entity = (Entity) object;
			if (check(paras, 2, s) || checkNum(paras[0], entity.switches.length, s)) {
				return null;
			}
			entity.switches[paras[0].getInt()] = paras[1].getBool();
			return null;
		});
		obmethods.put(ExtPokemon.class, new StringMap<>());
		obmethods.get(ExtPokemon.class).put("addHP", (object, paras, s) -> {
			ExtPokemon epk = (ExtPokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			int hp = paras[0].getInt();
			if (epk.hp + hp > epk.stats[Statistic.HEALTH.getID()]) {
				epk.hp = epk.stats[Statistic.HEALTH.getID()];
			} else {
				epk.hp += hp;
			}
			if (epk.status == StatusCondition.KO && epk.hp != 0) {
				epk.status = StatusCondition.OK;
			}
			return null;
		});
		obmethods.put(Pokemon.class, new StringMap<>());
		obmethods.get(Pokemon.class).put("getId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.id);
		});
		obmethods.get(Pokemon.class).put("getForm", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.form);
		});
		obmethods.get(Pokemon.class).put("isShiny", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableBool(pokemon.shiny);
		});
		obmethods.get(Pokemon.class).put("hasGender", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableBool(InfoLoader.getGenderThreshold(pokemon.id, pokemon.form) != 0xFF);
		});
		obmethods.get(Pokemon.class).put("getGender", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableObject(pokemon.gender);
		});
		obmethods.get(Pokemon.class).put("getLevel", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.level);
		});
		obmethods.get(Pokemon.class).put("getCurrentXP", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.currentep);
		});
		obmethods.get(Pokemon.class).put("hasPokerus", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableBool(pokemon.pokerus.isActive());
		});
		obmethods.get(Pokemon.class).put("getTrainerId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.trainerid);
		});
		obmethods.get(Pokemon.class).put("getSecretTrainerId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.strainerid);
		});
		obmethods.get(Pokemon.class).put("hasItem", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableBool(pokemon.itemid != 0);
		});
		obmethods.get(Pokemon.class).put("getItemId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.itemid);
		});
		obmethods.get(Pokemon.class).put("getItem", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableString(InfoLoader.getInternalItemName(pokemon.itemid));
		});
		obmethods.get(Pokemon.class).put("getAbilityId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.ability);
		});
		obmethods.get(Pokemon.class).put("getAbility", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableString(InfoLoader.getInternalAbilityName(pokemon.ability));
		});
		obmethods.get(Pokemon.class).put("getNatureId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.nature.getID());
		});
		obmethods.get(Pokemon.class).put("getNature", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableObject(pokemon.nature);
		});
		obmethods.get(Pokemon.class).put("getAttackId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.ATTACKS, s)) {
				return null;
			}
			return new VariableInt(pokemon.attackids[paras[0].getInt()]);
		});
		obmethods.get(Pokemon.class).put("getAttack", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.ATTACKS, s)) {
				return null;
			}
			return new VariableString(InfoLoader.getInternalAttackName(pokemon.attackids[paras[0].getInt()]));
		});
		obmethods.get(Pokemon.class).put("getName", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableString(pokemon.getName());
		});
		obmethods.get(Pokemon.class).put("getPersonality", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.personality);
		});
		obmethods.get(Pokemon.class).put("getCaughtLevel", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.caught_level);
		});
		obmethods.get(Pokemon.class).put("getCaughtRegionId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableInt(pokemon.caught_region);
		});
		obmethods.get(Pokemon.class).put("getCaughtRegion", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			return new VariableString(InfoLoader.getInternalRegionName(pokemon.caught_region));
		});
		obmethods.get(Pokemon.class).put("getPPUP", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.ATTACKS, s)) {
				return null;
			}
			return new VariableInt(pokemon.ppups[paras[0].getInt()]);
		});
		obmethods.get(Pokemon.class).put("getEV", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			Statistic st = cast(paras[0], Statistic.class, s);
			if (s.isInitialized()) {
				return null;
			}
			return new VariableInt(pokemon.evs[st.getID()]);
		});
		obmethods.get(Pokemon.class).put("getIV", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			Statistic st = cast(paras[0], Statistic.class, s);
			if (s.isInitialized()) {
				return null;
			}
			return new VariableInt(pokemon.ivs[st.getID()]);
		});
		obmethods.get(Pokemon.class).put("getEVId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Statistic.length(), s)) {
				return null;
			}
			return new VariableInt(pokemon.evs[paras[0].getInt()]);
		});
		obmethods.get(Pokemon.class).put("getIVId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Statistic.length(), s)) {
				return null;
			}
			return new VariableInt(pokemon.ivs[paras[0].getInt()]);
		});
		obmethods.get(Pokemon.class).put("getRibbons", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			VariableArray va = new VariableArray();
			for (int i = 0; i < pokemon.ribbons.length; i++) {
				va.setElement(ConV.toCString(i), new VariableInt(pokemon.ribbons[i]));
			}
			return va;
		});
		obmethods.get(Pokemon.class).put("getRibbon", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], pokemon.ribbons.length, s)) {
				return null;
			}
			return new VariableInt(pokemon.ribbons[paras[0].getInt()]);
		});
		obmethods.get(Pokemon.class).put("setId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.id = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setForm", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.form = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("switchShiny", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			pokemon.shiny = !pokemon.shiny;
			return null;
		});
		obmethods.get(Pokemon.class).put("setGender", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			Gender gender = cast(paras[0], Gender.class, s);
			if (s.isInitialized()) {
				return null;
			}
			pokemon.gender = gender;
			return null;
		});
		obmethods.get(Pokemon.class).put("setLevel", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.LEVELMAX + 1, s)) {
				return null;
			}
			pokemon.level = paras[0].getInt();
			pokemon.currentep = InfoLoader.getXpType(pokemon.id, pokemon.form).getXpForLevel(pokemon.level);
			return null;
		});
		obmethods.get(Pokemon.class).put("triggerPokerus", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			pokemon.pokerus = Pokerus.DAY4;
			return null;
		});
		obmethods.get(Pokemon.class).put("advancePokerus", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			pokemon.pokerus = pokemon.pokerus.next();
			return null;
		});
		obmethods.get(Pokemon.class).put("setTrainerId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.TRAINERIDMAX, s)) {
				return null;
			}
			pokemon.trainerid = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setSecretTrainerId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.TRAINERIDMAX, s)) {
				return null;
			}
			pokemon.strainerid = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setItemId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.itemid = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setItem", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			try {
				pokemon.itemid = InfoLoader.getItemId(paras[0].getCString());
				return null;
			} catch (IllegalArgumentException iae) {
				s.initMessage(iae.getMessage());
				return null;
			}
		});
		obmethods.get(Pokemon.class).put("setAbilityId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.ability = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setAbility", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			try {
				pokemon.ability = InfoLoader.getAbilityId(paras[0].getCString());
				return null;
			} catch (IllegalArgumentException iae) {
				s.initMessage(iae.getMessage());
				return null;
			}
		});
		obmethods.get(Pokemon.class).put("setNatureId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Nature.length(), s)) {
				return null;
			}
			int id = paras[0].getInt();
			if (id < 0 && id >= Nature.length()) {
				s.initMessage("Unknown nature id: ", paras[0].getInt());
				return null;
			}
			pokemon.nature = Nature.getById(paras[0].getInt());
			return null;
		});
		obmethods.get(Pokemon.class).put("setNature", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			Nature nature = cast(paras[0], Nature.class, s);
			if (s.isInitialized()) {
				return null;
			}
			pokemon.nature = nature;
			return null;
		});
		obmethods.get(Pokemon.class).put("setAttackId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s) || checkNum(paras[0], Pokemon.ATTACKS, s)) {
				return null;
			}
			pokemon.attackids[paras[0].getInt()] = paras[1].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setAttack", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s) || checkNum(paras[0], Pokemon.ATTACKS, s)) {
				return null;
			}
			pokemon.attackids[paras[0].getInt()] = InfoLoader.getAttackId(paras[1].getCString());
			return null;
		});
		obmethods.get(Pokemon.class).put("setName", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.name = paras[0].getCString();
			return null;
		});
		obmethods.get(Pokemon.class).put("setPersonality", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.personality = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setCaughtLevel", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s) || checkNum(paras[0], Pokemon.LEVELMAX + 1, s)) {
				return null;
			}
			pokemon.caught_level = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setCaughtRegionId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.caught_region = paras[0].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setCaughtRegion", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			pokemon.caught_region = InfoLoader.getRegionId(paras[0].getCString());
			return null;
		});
		obmethods.get(Pokemon.class).put("setPPUp", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s) || checkNum(paras[0], Pokemon.ATTACKS, s) || checkNum(paras[1], Pokemon.PPUPMAX, s)) {
				return null;
			}
			pokemon.ppups[paras[0].getInt()] = paras[1].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setEV", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s) || checkNum(paras[0], Statistic.length(), s)) {
				return null;
			}
			int evMax = 0;
			for (int i = 0; i < Statistic.length(); i++) {
				if (i != paras[0].getInt()) {
					evMax += pokemon.evs[i];
				}
			}
			if (checkNum(paras[1], ConV.min(Pokemon.EVMAX, Pokemon.EVOVERALLMAX - evMax), s)) {
				return null;
			}
			pokemon.evs[paras[0].getInt()] = paras[1].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setIV", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s)) {
				return null;
			}
			Statistic st = cast(paras[0], Statistic.class, s);
			if (s.isInitialized()) {
				return null;
			}
			if (checkNum(paras[1], Pokemon.IVMAX, s)) {
				return null;
			}
			pokemon.ivs[st.getID()] = paras[1].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("setIVId", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 2, s) || checkNum(paras[0], Statistic.length(), s) || checkNum(paras[1], Pokemon.IVMAX, s)) {
				return null;
			}
			pokemon.ivs[paras[0].getInt()] = paras[1].getInt();
			return null;
		});
		obmethods.get(Pokemon.class).put("addRibbon", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			for (int i = 0; i < pokemon.ribbons.length; i++) {
				if (pokemon.ribbons[i] == paras[0].getInt()) {
					return null;
				}
			}
			int[] ribbons = new int[pokemon.ribbons.length + 1];
			ConV.arrayCopy(pokemon.ribbons, ribbons);
			ribbons[pokemon.ribbons.length] = paras[0].getInt();
			pokemon.ribbons = ribbons;
			return null;
		});
		obmethods.get(Pokemon.class).put("removeRibbon", (object, paras, s) -> {
			Pokemon pokemon = (Pokemon) object;
			if (check(paras, 1, s)) {
				return null;
			}
			int index = -1;
			for (int i = 0; i < pokemon.ribbons.length; i++) {
				if (pokemon.ribbons[i] == paras[0].getInt()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				int[] ribbons = new int[pokemon.ribbons.length - 1];
				ConV.arrayCopy(pokemon.ribbons, 0, ribbons, 0, index);
				ConV.arrayCopy(pokemon.ribbons, index + 1, ribbons, index, pokemon.ribbons.length - index - 1);
				pokemon.ribbons = ribbons;
			}
			return null;
		});
	}

	<t> t cast(Object o, Class<t> clazz, ScriptException s) {
		if (o.getClass() == VariableObject.class) {
			o = ((VariableObject) o).getObject();
		}
		if (o == null) {
			s.initMessage("Object is null, expected ", clazz.getName());
			return null;
		}
		if (clazz.isInstance(o)) {
			return clazz.cast(o);
		} else {
			s.initMessage("Failed to cast object of type ", o.getClass().getName(), " to type ", clazz.getName());
			return null;
		}
	}

	private final static String SCRIPT_PATH = "lib/scripts/";

	public static Code callScript(String name) {
		ScriptException sc = new ScriptException();
		Code code = current().callScript(name, sc);
		if (sc.isInitialized()) {
			Logger.add(sc);
		}
		return code;
	}

	@Override
	public Code callScript(String name, ScriptException s) {
		FileReader fr = new FileReader(SCRIPT_PATH + name + ".txt");
		if (!fr.exists()) {
			s.initMessage("Could not find script: ", SCRIPT_PATH, name, ".txt");
			return null;
		}
		if (!fr.canRead()) {
			s.initMessage("Could not access script: ", SCRIPT_PATH, name, ".txt");
			return null;
		}
		char[][] lines;
		try {
			char[] line = fr.readCLine();
			if (!ConV.isInteger(line)) {
				s.initMessage("Non-numeric line number in file ", SCRIPT_PATH, name, ".txt");
				return null;
			}
			int lineNum = ConV.parseInteger(line);
			if (lineNum < 0) {
				s.initMessage("Negativ line number in file ", SCRIPT_PATH, name, ".txt");
				return null;
			}
			lines = new char[lineNum][];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = fr.readCLine();
				if (lines[i] == null) {
					s.initMessage("Script is shorter than specified: ", SCRIPT_PATH, name, ".txt");
					return null;
				}
			}
			if (fr.readCLine() != null) {
				Logger.add(Logger.FILE, "Script is longer than specified: ", SCRIPT_PATH, name, ".txt");
			}
		} finally {
			fr.close();
		}
		if (!fr.canRead()) {
			s.initMessage("Could not access script: ", SCRIPT_PATH, name, ".txt");
			return null;
		}
		return new RawCode(lines);
	}

	public static MethodLibrary current() {
		return pml;
	}

	private interface ModuleMethod {

		Variable run(Variable[] parameters, ScriptException s);
	}

	private interface ObjectMethod {

		Variable run(Object object, Variable[] parameters, ScriptException s);
	}

}
