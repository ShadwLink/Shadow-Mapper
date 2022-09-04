

package nl.shadowlink.tools.io;

/**
 * @author Shadow-Link
 */
public class Message {
	// 0 = off
	// 1 = High only
	// 2 = All
	// 3 = Super
	private static int debug_mode = 0;

	public static void displayMsgHigh(String msg) {
		if (debug_mode == 1 || debug_mode == 2)
			System.out.println(msg);
	}

	public static void displayMsgHigh(int msg) {
		if (debug_mode == 1 || debug_mode == 2)
			System.out.println(msg);
	}

	public static void displayMsgLow(String msg) {
		if (debug_mode == 2)
			System.out.println(msg);
	}

	public static void displayMsgSuper(String msg) {
		if (debug_mode == 3)
			System.out.println(msg);
	}

	public static void displayMsgHigh(boolean msg) {
		if (debug_mode == 1 || debug_mode == 2)
			System.out.println(msg);
	}
}
