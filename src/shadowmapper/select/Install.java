package shadowmapper.select;

import java.io.File;

import ch.ubique.inieditor.IniEditor;

/**
 * An object of an installation saved in the settings.ini
 * 
 * @author Kilian Steenman (Shadow-Link)
 * 
 */
public class Install {
	private String mPath;
	private String mName;
	private int mVersion;
	private int mType;

	/**
	 * Returns the path for the installation
	 * 
	 * @return
	 */
	public String getPath() {
		return mPath;
	}

	/**
	 * Sets the path for the installation
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		mPath = path;
	}

	/**
	 * Returns the name of the installation
	 * 
	 * @return
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the installation
	 * 
	 * @param name
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * Returns the version as a string
	 * 
	 * @return
	 */
	public String getVersionString() {
		return "";
	}

	/**
	 * Returns the version as an int
	 * 
	 * @return
	 */
	public int getVersion() {
		return mVersion;
	}

	/**
	 * Sets the version
	 * 
	 * @param version
	 *            The version number
	 */
	public void setVersion(int version) {
		mVersion = version;
	}

	/**
	 * Gets the type of this install
	 * 
	 * @return
	 */
	public int getType() {
		return mType;
	}

	/**
	 * Sets the type of this install
	 * 
	 * @param type
	 */
	public void setType(int type) {
		mType = type;
	}

	/**
	 * Checks if this installation is a valid version of GTA IV
	 * 
	 * @return
	 */
	public boolean isVersionValid() {
		if (mVersion != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param ini
	 */
	public void checkVersion(IniEditor ini) {
		// TODO: Add code to check install
	}

	/**
	 * Returns TRUE if the path is valid
	 * 
	 * @return
	 */
	public boolean isPathValid() {
		File file = new File(mPath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To String
	 */
	public String toString() {
		return "[" + mName + "] " + mPath + " (" + mVersion + ")";
	}
}
