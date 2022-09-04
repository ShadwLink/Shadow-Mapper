package nl.shadowlink.tools.shadowmapper.gui.select;

import javax.swing.table.AbstractTableModel;

/**
 * Installs table model
 * 
 * @author Shadow-Link 
 * 
 */
public class InstallsTableModel extends AbstractTableModel {
	private static String[] sTableColumnNames = { "Type", "Name", "Path", "Version", "Valid" };
	private Install[] mInstalls;

	@Override
	public int getColumnCount() {
		return sTableColumnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return sTableColumnNames[column];
	}

	@Override
	public int getRowCount() {
		if (mInstalls != null) {
			return mInstalls.length;
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0:
			return mInstalls[row].getType();
		case 1:
			return mInstalls[row].getName();
		case 2:
			return mInstalls[row].getPath();
		case 3:
			return mInstalls[row].getVersionString();
		case 4:
			return mInstalls[row].isPathValid();
		default:
			return "-";
		}
	}

	/**
	 * Sets the installs
	 * 
	 * @param installs
	 *            The installs to set
	 */
	public void setInstalls(Install[] installs) {
		mInstalls = installs;
		fireTableStructureChanged();
	}

}
