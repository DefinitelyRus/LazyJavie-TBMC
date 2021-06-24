package commands;

import org.apache.commons.lang3.exception.ExceptionUtils;
import home.Bot;
import home.LazyJavieUI;
import home.SQLconnector;

/**
 * This class is dedicated to shortening the process of printing to console.
 * Having to type "System.out.println()" every time is annoying and this also lets us customize what happens when printing to console.
 * @author DefinitelyRus
 */
public class P {
	//This string holds the contents of the entire console.
	static String currentConsoleContents = "";
	private static final String expectedNullPointerError = "java.lang.NullPointerException: Cannot invoke \"javax.swing.JTextArea.setText(String)\" because the return value of \"home.LazyJavieUI.getConsoleOutput()\" is null";
	
	/**
	 * Standard console print shortcut (both to IDE and UI Console).
	 * @param args - A string value to be printed to the console.
	 */
	public static void print (String args) {
		System.out.println(args);
		consoleOut(args);
	}
	
	/**
	 * Simply prints the inserted argument into the IDE console. This is primarily used for testing purposes only.
	 * @param args - A string value to be printed to the console.
	 */
	public static void devprint (String args) {
		System.out.println(args);
	}
 	
	private static void consoleOut(String args) {
		String returnString = null;
		try {
			//Gets the current text in the UI console.
			currentConsoleContents = LazyJavieUI.consoleOutput.getText();
			try {
				/* Checks if the console UI is empty.
				 * If it is, this will set the console to display only the new text.
				 * If not, this will set the console to display the old text then appends the new text at the end.
				 */
				if (currentConsoleContents.equals(null) || currentConsoleContents.equals("")) {returnString = args;}
				else {returnString = currentConsoleContents + "\n" + args;}
			}
			catch (Exception e) {print(e.toString() + "\n" + args); e.printStackTrace();}
		} catch (NullPointerException e) {returnString = args;}
		
		try {LazyJavieUI.getConsoleOutput().setText(returnString);}
		catch (Exception e) {
			try {if (!e.toString().equals(expectedNullPointerError)) {SQLconnector.errorLog(e.toString(), ExceptionUtils.getStackTrace(e), Bot.VERSION); devprint(e.toString());}
			} catch (Exception e1) {
				devprint(e1.toString());
			}
		}
	}
}
