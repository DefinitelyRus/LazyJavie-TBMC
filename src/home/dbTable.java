package home;

import java.awt.Choice;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.P;

public class dbTable {
	
	/**Returns the contents of the table as a 2D object array.
	 * @param tableList The list of tables in the database.
	 * @param tableGrid The target JTable object to update.
	 * @return A 2D array containing the contents of the table.
	 */
	@SuppressWarnings("serial")
	public static Object[][] updateTableGridContents(Choice tableList, JTable tableGrid) {

		//Initialization
		String table = tableList.getSelectedItem();
		if (table.equals("- Select table -")) {LazyJavieUI.getEntryCounterLabel().setText("No table selected."); return null;}
		int xCount = 0, yCount = 0;
		String query = "select * from " +table;
		xCount = SQLconnector.getXY(table)[0];
		yCount = SQLconnector.getXY(table)[1];
		List<Object> tg_row = new LinkedList<Object>();
		List<Object[]> tg_2d = new ArrayList<Object[]>();
		SQLconnector.dbPass = SQLconnector.getPass();
		
		
		try {
			Connection connection = DriverManager.getConnection(SQLconnector.dbAddress, SQLconnector.dbID, SQLconnector.dbPass);
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			
			//Gets all the contents of an entire row, then adds it to tg_2d.
			//It will do this for every record in the table.
			while (results.next()) {
				for (int c = 1; c <= yCount; c++) tg_row.add(results.getString(c));
				tg_2d.add(tg_row.toArray());
			}
			
			//Result set reset. This avoids "IllegalStateException: SQLite JDBC: inconsistent internal state"
			//I'm assuming it has something to do with it being used to iterate prior to calling one of its functions.
			//I don't know how else to get around it without putting all this before the loop using resultset.next().
			results = statement.executeQuery(query);
			
			//Converts tg_2d into an object array then assigns it to a variable.
			Object[][] tableGridContents = tg_2d.toArray(new Object[0][0]);
			
			List<Boolean> columnEditablesList = new LinkedList<>();
			for (int c = 1; c <= yCount; c++) {columnEditablesList.add(false);}
			
			//Updates the table UI.
			tableGrid.setModel(new DefaultTableModel(tableGridContents, getColumnHeaders(yCount, results)) {
					boolean[] columnEditables = toBooleanArray(columnEditablesList);
					public boolean isCellEditable(int row, int column) {return columnEditables[column];}
				}
			);
			connection.close();
			String str;
			if (xCount == 1) str = " entry found."; else str = " entries found.";
			LazyJavieUI.getEntryCounterLabel().setText(xCount + str);
			
			return tableGridContents;
			
		} catch (SQLException e) {
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());
			return null;
		}
	}
	
	/**Updates the table choice list under the database tab.
	 * 
	 * @param tableList
	 */
	public static void updateTableList(Choice tableList) {
		try {
			ResultSet tableResultSet = SQLconnector.getResultSet("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
			tableList.removeAll();
			tableList.add("- Select table -");
			while (tableResultSet.next()) {tableList.add(tableResultSet.getString("name"));}
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
	}

	/**Returns the headers of the table as a string array.
	 * Made specifically for JTable.
	 * 
	 * @param columnCount
	 * @param results - ResultSet (uses JDBC)
	 * @return An array of column headers.
	 */
	public static String[] getColumnHeaders(int columnCount, ResultSet results) {
		
		List<String> headersList = new LinkedList<String>();
		for (int i = 1; i <= columnCount; i++) {
			try {headersList.add(results.getMetaData().getColumnName(i));}
			catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		}
		String[] columnHeaders = toStringArray(headersList);
		return columnHeaders;
	}
	
	//TODO Add these functions into a separate class.
	/**Converts a boolean list into primitive boolean array.
	 * @param booleanList
	 * @return A primite boolean array form of the list.
	 */
	public static boolean[] toBooleanArray(final List<Boolean> booleanList) {
	    final boolean[] primitives = new boolean[booleanList.size()];
	    int index = 0;
	    for (Boolean object : booleanList) {
	        primitives[index++] = object;
	    }
	    return primitives;
	}
	
	/**Converts a string list into a primitive string array.
	 * @param stringList
	 * @return A primite string array form of the list.
	 */
	public static String[] toStringArray(final List<String> stringList) {
	    final String[] primitives = new String[stringList.size()];
	    int index = 0;
	    for (String object : stringList) {
	        primitives[index++] = object;
	    }
	    return primitives;
	}
}
