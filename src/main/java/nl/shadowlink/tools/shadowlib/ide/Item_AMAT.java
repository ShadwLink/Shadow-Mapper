

package nl.shadowlink.tools.shadowlib.ide;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_AMAT {
	private int gameType;

	public Item_AMAT(int gameType) {
		this.gameType = gameType;
	}

	public void read(String line) {
		// TODO: Implement
	}

	public void save(BufferedWriter output) {
		try {
			String line = "";
			output.write(line + "\n");
			System.out.println("Line: " + line);
		} catch (IOException ex) {
			Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}