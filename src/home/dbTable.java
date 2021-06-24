package home;

import java.util.ArrayList;
import java.util.List;

import commands.P;

public class dbTable {
	
	//public static int recordCount = 0;
	//public static int columnCount = 0;
	public static Object[] r_tableGrid_rows = {null, null, null, null, null, null, null, null};
	public static Object[] r_tableGrid_columns;
	
	public static void main() {
		//dbTable myObj = new dbTable();
	}
	
	//Returns the contents of the table as a 2D object array.
	public static Object[][] getTableGridContents(int rowCount, int columnCount) {
		/*
		 * A row refers to one record, while a column refers to a category (eg. ID, name, quantity)
		 */
		//TODO Make the contents modifiable. (Use a list, but return as an array.)
		Object[][] tableGrid = new Object[][] {
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null},
		};
		
		List<Object> tableGrid_asList = new ArrayList<Object>();
		List<Object> tableGrid_perRow = new ArrayList<Object>();
		
		for (int r = 0; r < rowCount; r++) {
			tableGrid_perRow.clear();
			//r stands for row, while c stands for column.
			for (int c = 0; c < columnCount; c++) {
				tableGrid_perRow.add(r_tableGrid_rows[c]);
			}
			tableGrid_asList.add(tableGrid_perRow.toArray());
		}
		tableGrid = (Object[][]) tableGrid_asList.toArray();
		P.print(tableGrid.toString());
		return tableGrid;
	}
	
	//Returns the headers of the table as a string array.
	public static String[] getColumnHeaders() {
		//TODO Make the contents modifiable. (Use a list, but return as an array.)
		String[] columnHeaders = {
			"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
		};
		return columnHeaders;
	}
}
