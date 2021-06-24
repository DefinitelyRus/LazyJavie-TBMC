/*
 * This module's only purpose is to make printing text easier.
 * That's literally it.
 */

package commands;

import home.LazyJavieUI;

public class P {
	//This string holds the contents of the entire console.
	static String currentConsoleContents = "";
	
	//Standard console print shortcut (both to IDE and UI Console).
	public static void print (String args) {
		System.out.println(args);
		consoleOut(args);
	}
	
	private static void consoleOut(String args) {
		String returnString = null;
		try {
			currentConsoleContents = LazyJavieUI.consoleOutput.getText();
			try {
				if (currentConsoleContents.equals(null) || currentConsoleContents.equals("")) {
					returnString = args;
				} else {
					returnString = currentConsoleContents + "\n" + args;
				}
			}
			catch (Exception e) {print(e.toString() + "\n" + args); e.printStackTrace();}
			
		} catch (NullPointerException e) {
			returnString = args;
			}
		
		LazyJavieUI.getConsoleOutput().setText(returnString);
	}
}
