package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import main.Debug;
import main.Main;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CredentialsDialog extends JDialog {

	private static final long serialVersionUID = -4690911906934268511L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtFdId;
	private JTextField txtFdSecret;

	private Debug debug;

	/**
	 * Create the dialog.
	 */
	public CredentialsDialog(File fXmlFile) {
		this.debug = new Debug(this.getClass().getName());

		setTitle("Imgurloader - Credentials");
		setBounds(100, 100, 450, 146);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblID = new JLabel("Id");
			GridBagConstraints gbc_lblID = new GridBagConstraints();
			gbc_lblID.anchor = GridBagConstraints.EAST;
			gbc_lblID.insets = new Insets(0, 0, 5, 5);
			gbc_lblID.gridx = 0;
			gbc_lblID.gridy = 0;
			contentPanel.add(lblID, gbc_lblID);
		}
		{
			txtFdId = new JTextField();
			GridBagConstraints gbc_txtFdId = new GridBagConstraints();
			gbc_txtFdId.insets = new Insets(0, 0, 5, 0);
			gbc_txtFdId.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFdId.gridx = 1;
			gbc_txtFdId.gridy = 0;
			contentPanel.add(txtFdId, gbc_txtFdId);
			txtFdId.setColumns(10);
		}
		{
			JLabel lblSecret = new JLabel("Secret");
			GridBagConstraints gbc_lblSecret = new GridBagConstraints();
			gbc_lblSecret.anchor = GridBagConstraints.EAST;
			gbc_lblSecret.insets = new Insets(0, 0, 5, 5);
			gbc_lblSecret.gridx = 0;
			gbc_lblSecret.gridy = 1;
			contentPanel.add(lblSecret, gbc_lblSecret);
		}
		{
			txtFdSecret = new JTextField();
			GridBagConstraints gbc_txtFdSecret = new GridBagConstraints();
			gbc_txtFdSecret.insets = new Insets(0, 0, 5, 0);
			gbc_txtFdSecret.fill = GridBagConstraints.BOTH;
			gbc_txtFdSecret.gridx = 1;
			gbc_txtFdSecret.gridy = 1;
			contentPanel.add(txtFdSecret, gbc_txtFdSecret);
			txtFdSecret.setColumns(10);
		}
		{
			JLabel lblInfo = new JLabel("What is this?");
			lblInfo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					URL url;
					try {
						url = new URL("https://api.imgur.com/oauth2/addclient");
						URI uri = url.toURI();
						Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
						if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
							try {
								desktop.browse(uri);
							} catch (Exception e) {
								debug.DebugMessage(e.getMessage());
							}
						}
					} catch (MalformedURLException e1) {
						debug.DebugMessage(e1.getMessage());
					} catch (URISyntaxException e2) {
						debug.DebugMessage(e2.getMessage());
					}

				}
			});
			lblInfo.setForeground(Color.BLUE);
			GridBagConstraints gbc_lblInfo = new GridBagConstraints();
			gbc_lblInfo.anchor = GridBagConstraints.EAST;
			gbc_lblInfo.gridx = 1;
			gbc_lblInfo.gridy = 2;
			contentPanel.add(lblInfo, gbc_lblInfo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						generateCredentials(txtFdId.getText(), txtFdSecret.getText());
						Main.showMainGui(fXmlFile);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void generateCredentials(String id, String secret){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("imguruser");
			doc.appendChild(rootElement);

			// id element
			Element element_id = doc.createElement("id");
			element_id.appendChild(doc.createTextNode(id));
			rootElement.appendChild(element_id);

			// secret element
			Element element_secret = doc.createElement("secret");
			element_secret.appendChild(doc.createTextNode(secret));
			rootElement.appendChild(element_secret);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			String dash = "/";
			
			if(debug.getOS().indexOf("win") >= 0){
				dash = "\\";
			}
			
			String path = System.getProperty("user.dir") + dash + "credentials.xml";
			
			StreamResult result = new StreamResult(new File(path));
			debug.DebugMessage("Credentialpath:			" + path);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			debug.DebugMessage("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}
}
