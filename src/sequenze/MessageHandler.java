package sequenze;

import util.ConV;
import util.Logger;

public class MessageHandler {

	public static char[] EVENT = "event".toCharArray();
	public static char[] BAG_OPEN = "Bag open".toCharArray();
	public static char[] BAG_CLOSE = "Bag close".toCharArray();
	public static char[] TEAM_OPEN = "Team open".toCharArray();
	public static char[] TEAM_CLOSE = "Team close".toCharArray();
	public static char[] GADGET_OPEN = "Gadget open".toCharArray();
	public static char[] GADGET_CLOSE = "Gadget close".toCharArray();
	public static char[] POKEDEX_OPEN = "Pokedex open".toCharArray();
	public static char[] POKEDEX_CLOSE = "Pokedex close".toCharArray();
	public static char[] CARD_OPEN = "Card open".toCharArray();
	public static char[] CARD_CLOSE = "Card close".toCharArray();
	public static char[] OPTIONS_OPEN = "Options open".toCharArray();
	public static char[] OPTIONS_CLOSE = "Options close".toCharArray();
	public static char[] SUMMARY_OPEN = "Summary open".toCharArray();
	public static char[] FIGHT = "fight".toCharArray();

	private static Message lastMessage;
	private static boolean locked;

	private MessageHandler() {
	}

	public static boolean hasMessage() {
		return !locked && lastMessage != null;
	}

	private static void newMessage(Message message) {
		if (locked) {
			Logger.add(Logger.GAME, "Double message: ", lastMessage.command, ", ", message.command);
			return;
		}
		lastMessage = message;
		locked = true;
	}

	public static boolean hasMessage(char[] command) {
		if (locked || lastMessage == null || !ConV.equals(command, lastMessage.command)) {
			return false;
		}
		if (lastMessage.extra == null) {
			lastMessage = null;
		}
		return true;
	}

	public static boolean tryadd(char[] command, Object extra) {
		if (locked) {
			return false;
		}
		newMessage(new Message(command, extra));
		return true;
	}

	public static boolean tryadd(char[] command, int extra) {
		return tryadd(command, new Integer(extra));
	}

	public static void add(char[] command, Object extra) {
		newMessage(new Message(command, extra));
	}

	public static void add(char[] command, int extra) {
		add(command, new Integer(extra));
	}

	public static void add(char[] command, boolean extra) {
		add(command, Boolean.valueOf(extra));
	}

	public static boolean getBool() {
		Object extra = getObject();
		if (extra == null) {
			return false;
		}
		return ((Boolean) extra).booleanValue();
	}

	public static int getInt() {
		Object extra = getObject();
		if (extra == null) {
			return 0;
		}
		return ((Integer) extra).intValue();
	}

	public static Object getObject() {
		if (locked) {
			return null;
		}
		Object extra = lastMessage.extra;
		lastMessage = null;
		return extra;
	}

	static void free() {
		locked = false;
	}

	private static class Message {

		char[] command;

		Object extra;

		Message(char[] command, Object extra) {
			this.command = command;
			this.extra = extra;
		}

	}
}