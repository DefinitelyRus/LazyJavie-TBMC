package home;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JFrame;
import javax.swing.UIManager;

//I don't understand this enough to modify it to fit my program.
//TODO Add HIDE TO SYSTEM TRAY option.

/**
*
* @author Mohammad Faisal
* ermohammadfaisal.blogspot.com
* facebook.com/m.faisal6621
*/
@SuppressWarnings("serial")
public class HideToSystemTray extends JFrame{
   TrayIcon trayIcon;
   SystemTray tray;
   HideToSystemTray() {
	   
	   //???
       super("SystemTray test");
       
       System.out.println("creating instance");
       
       //Setting look & feel
       try{
           System.out.println("setting look and feel");
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       } catch(Exception e) {
           System.out.println("Unable to set LookAndFeel");
       }
       
       
       if (SystemTray.isSupported()) {
    	   //Gets the system tray as object
           System.out.println("system tray supported");
           tray = SystemTray.getSystemTray();
           
           //Assigns icon to object
           Image image=Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
           
           //???
           ActionListener exitListener=new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Exiting....");
                   System.exit(0);
               }
           };
           
           //Creates a pop-up menu when system tray icon is right-clicked.
           PopupMenu popup=new PopupMenu();
           MenuItem defaultItem=new MenuItem("Exit");
           defaultItem.addActionListener(exitListener);
           popup.add(defaultItem);
           defaultItem=new MenuItem("Open");
           defaultItem.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   setVisible(true);
                   setExtendedState(JFrame.NORMAL);
               }
           });
           
           //???
           popup.add(defaultItem);
           
           //Initializes tray icon details
           trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
           trayIcon.setImageAutoSize(true);
           
       } else {
           System.out.println("system tray not supported");
       }
       
       //???
       addWindowStateListener(new WindowStateListener() {
    	   
    	   //???
           public void windowStateChanged(WindowEvent e) {
        	   
        	   //???
               if(e.getNewState()==ICONIFIED){
                   try {
                       tray.add(trayIcon);
                       setVisible(false);
                       System.out.println("added to SystemTray");
                   } catch (AWTException ex) {
                       System.out.println("unable to add to tray");
                   }
               }
               
		       //???
		       if(e.getNewState()==7){
		           try {
			           tray.add(trayIcon);
			           setVisible(false);
			           System.out.println("added to SystemTray");
			           
		           } catch (AWTException ex) {
			           System.out.println("unable to add to system tray");
		           }
	           }
		       
		       //???
		       if (e.getNewState() == MAXIMIZED_BOTH) {
		           tray.remove(trayIcon);
		           setVisible(true);
		           System.out.println("Tray icon removed");
		       }
		       if(e.getNewState()==NORMAL){
		           tray.remove(trayIcon);
		           setVisible(true);
		           System.out.println("Tray icon removed");
		       }
           }
       });
       
       setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));
       setVisible(true);
       setSize(300, 200);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   public static void main(String[] args){
       new HideToSystemTray();
   }
}