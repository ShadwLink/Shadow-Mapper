package nl.shadowlink.tools.shadowlib.utils;

/**
 * Contains utilities for hash generation/reversal
 *
 * @author Shadow-Link 
 */
public class HashUtils {

	/**
	 * Generate hash for key
	 *
	 * @param key
	 *        The key to generate the hash for
	 * @return The hash
	 */
	public static long genHash(final String key) {
		int hash = 0;

		for (int i = 0; i < key.length(); i++) {
			hash += (key.charAt(i) & 0xFF);
			hash += (hash << 10);
			hash ^= (hash >>> 6);
		}
		hash += (hash << 3);
		hash ^= (hash >>> 11);
		hash += (hash << 15);
		return hash & 0xffffffffL;
	}
}
