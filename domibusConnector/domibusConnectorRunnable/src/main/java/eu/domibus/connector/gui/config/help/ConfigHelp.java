package eu.domibus.connector.gui.config.help;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class ConfigHelp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371190214145718475L;

	/** The contentpane */
	  protected Container cp;

	  /** The editorpane */
	JEditorPane help;
	
	public ConfigHelp(String windowName, String helpFileName){
		super(windowName);
		cp = getContentPane();
		try {
		      URL url = this.getClass().getResource(helpFileName);
		      // Only create the window once.
		      if (help == null) {
		        // System.out.println("Creat-ing help window for " + url);
		        help = new JEditorPane(url);
		        // System.out.println("Done!");
		        help.setEditable(false);
		        JScrollPane scroller = new JScrollPane();
//		        scroller.setBorder(BorderFactory.createTitledBorder("Database Configuration Help"));
		        scroller.getViewport().add(help);
		        cp.add(BorderLayout.CENTER, scroller);
		        addWindowListener(new WindowListener() {
					
					@Override
					public void windowOpened(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowIconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowDeiconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowDeactivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowClosing(WindowEvent e) {
						ConfigHelp.this.setVisible(false);
						ConfigHelp.this.dispose();
						
					}
					
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowActivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
		       
		        setSize(500, 400);
		      } else {
		        System.out.println("Re-using help window!");
		      }
		    } catch (MalformedURLException e) {
		      System.out.println("Malformed URL: " + e);
		    } catch (IOException e) {
		      System.out.println("IOException: " + e);
		    }
	}
}
