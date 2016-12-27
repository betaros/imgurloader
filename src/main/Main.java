package main;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gui.CredentialsDialog;
import gui.MainGui;

public class Main {

	public static void main(String[] args) {
		Debug debug = new Debug("main.Main");

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
			debug.DebugMessage("Exception:	" + "UnsupportedLookAndFeelException");
		}
		catch (ClassNotFoundException e) {
			// handle exception
			debug.DebugMessage("Exception:	" + "ClassNotFoundException");
		}
		catch (InstantiationException e) {
			// handle exception
			debug.DebugMessage("Exception:	" + "InstantiationException");
		}
		catch (IllegalAccessException e) {
			// handle exception
			debug.DebugMessage("Exception:	" + "IllegalAccessException");
		}

		try {
			String dash = "/";
			
			if(debug.getOS().indexOf("win") >= 0){
				dash = "\\";
			}
			
			StringBuilder sbPath = new StringBuilder();
			sbPath.append(System.getProperty("user.dir"));
			sbPath.append(dash);
			sbPath.append("credentials.xml");
			
			String path = sbPath.toString();
			debug.DebugMessage("XML Path:		" + path);
			File fXmlFile = new File(path);
			if(!fXmlFile.exists()){
				try {
					CredentialsDialog dialog = new CredentialsDialog(fXmlFile);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					debug.DebugMessage(e.getMessage());
				}
			} else {
				showMainGui(fXmlFile);
			}

		} catch (Exception e) {
			debug.DebugMessage("Exception:	" + "Function Exception");
		}

	}

	public static void showMainGui(File fXmlFile){
		Debug debug = new Debug("main.Main");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("imguruser");
			Node nNode = nList.item(0);

			debug.DebugMessage(String.valueOf(nList.getLength()));
			debug.DebugMessage("Current Element:	" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				Profile profile = new Profile(eElement.getElementsByTagName("id").item(0).getTextContent(), eElement.getElementsByTagName("secret").item(0).getTextContent());

				debug.DebugMessage("ID: 			" + profile.getId());
				debug.DebugMessage("Secret:			" + profile.getSecret());

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							MainGui window = new MainGui(profile);
							window.frmImgurloader.setVisible(true);
						} catch (Exception e) {
							debug.DebugMessage(e.getMessage());
						}
					}
				});
			}
		} catch (ParserConfigurationException e1) {
			debug.DebugMessage("Exception:	" + "ParserConfigurationException");
		} catch (SAXException e1) {
			debug.DebugMessage("Exception:	" + "SAXException");
		} catch (IOException e1) {
			debug.DebugMessage("Exception:	" + "IOException");
		}
		
	}
}


