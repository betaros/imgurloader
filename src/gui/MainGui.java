package gui;

import javax.swing.JFrame;

import main.Debug;
import main.Functions;
import main.Profile;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGui {

	public JFrame frmImgurloader;
	private JProgressBar progressBar;
	private JTextField textFieldURL;
	private Profile profile;
	private Debug debug;
	
	private boolean pdfBool;
	private boolean cbzBool;

	private SwingWorker<Void, String> worker;
	
	/**
	 * Create the application.
	 */
	public MainGui(Profile _profile) {
		this.profile = _profile;
		this.debug = new Debug(this.getClass().getName());
		
		pdfBool = false;
		cbzBool = false;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmImgurloader = new JFrame();
		frmImgurloader.setTitle("Imgurloader");
		frmImgurloader.setBounds(100, 100, 450, 300);
		frmImgurloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{65, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmImgurloader.getContentPane().setLayout(gridBagLayout);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(null);
		GridBagConstraints gbc_panelCenter = new GridBagConstraints();
		gbc_panelCenter.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelCenter.anchor = GridBagConstraints.NORTH;
		gbc_panelCenter.insets = new Insets(0, 0, 5, 0);
		gbc_panelCenter.gridx = 0;
		gbc_panelCenter.gridy = 0;
		frmImgurloader.getContentPane().add(panelCenter, gbc_panelCenter);
		GridBagLayout gbl_panelCenter = new GridBagLayout();
		gbl_panelCenter.columnWidths = new int[]{0, 0};
		gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelCenter.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCenter.setLayout(gbl_panelCenter);
		
		JLabel lblImgurUrl = new JLabel("Imgur URL");
		GridBagConstraints gbc_lblImgurUrl = new GridBagConstraints();
		gbc_lblImgurUrl.anchor = GridBagConstraints.WEST;
		gbc_lblImgurUrl.insets = new Insets(0, 0, 5, 0);
		gbc_lblImgurUrl.gridx = 0;
		gbc_lblImgurUrl.gridy = 0;
		panelCenter.add(lblImgurUrl, gbc_lblImgurUrl);
		
		textFieldURL = new JTextField();
		GridBagConstraints gbc_textFieldURL = new GridBagConstraints();
		gbc_textFieldURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldURL.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldURL.gridx = 0;
		gbc_textFieldURL.gridy = 1;
		panelCenter.add(textFieldURL, gbc_textFieldURL);
		textFieldURL.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		panelCenter.add(panel, gbc_panel);
		
		JCheckBox chckbxPdf = new JCheckBox("PDF");
		panel.add(chckbxPdf);
		
		JCheckBox chckbxCbz = new JCheckBox("CBZ");
		panel.add(chckbxCbz);
		
		JButton btnDownload = new JButton("Download");
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 3;
		panelCenter.add(btnDownload, gbc_btnDownload);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelInfo = new GridBagConstraints();
		gbc_panelInfo.fill = GridBagConstraints.BOTH;
		gbc_panelInfo.insets = new Insets(0, 0, 5, 0);
		gbc_panelInfo.gridx = 0;
		gbc_panelInfo.gridy = 1;
		frmImgurloader.getContentPane().add(panelInfo, gbc_panelInfo);
		GridBagLayout gbl_panelInfo = new GridBagLayout();
		gbl_panelInfo.columnWidths = new int[]{0, 30, 0};
		gbl_panelInfo.rowHeights = new int[]{0, 0, 0};
		gbl_panelInfo.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelInfo.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelInfo.setLayout(gbl_panelInfo);
		
		JLabel lblTitle = new JLabel("Title");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		panelInfo.add(lblTitle, gbc_lblTitle);
		
		JLabel lblShowTitle = new JLabel("-");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		panelInfo.add(lblShowTitle, gbc_label);
		
		JLabel lblPictures = new JLabel("Pictures");
		GridBagConstraints gbc_lblPictures = new GridBagConstraints();
		gbc_lblPictures.insets = new Insets(0, 0, 0, 5);
		gbc_lblPictures.gridx = 0;
		gbc_lblPictures.gridy = 1;
		panelInfo.add(lblPictures, gbc_lblPictures);
		
		JLabel lblShowPictures = new JLabel("-");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 1;
		panelInfo.add(lblShowPictures, gbc_label_1);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		progressBar.setString("Ready");
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 2;
		frmImgurloader.getContentPane().add(progressBar, gbc_progressBar);
		
		/*----------------------------------------------------------------------*/
		/* Functions															*/
		/*----------------------------------------------------------------------*/
		Functions function = new Functions(profile);
		
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String url = textFieldURL.getText();
				if(url.isEmpty() || !url.contains("imgur.com")){
					debug.DebugMessage("URL:			Imgur URL wrong");
					return;
				}
				
				final String albumID = url.substring(url.lastIndexOf("/") + 1).trim();
				debug.DebugMessage("URL:			" + url);
				
				pdfBool = chckbxPdf.isSelected();
				cbzBool = chckbxCbz.isSelected();
								
				worker = new SwingWorker<Void, String>(){

					@Override
					protected Void doInBackground() throws Exception {
						lblShowTitle.setText(function.getTitle(albumID));
						lblShowPictures.setText(function.getPictureCount(albumID));
						
						progressBar.setString("Downloading files");
						progressBar.setValue(10);
						function.download(albumID);
						
						progressBar.setValue(30);
						progressBar.setString("Generating Documents");
								
						function.generator(albumID, pdfBool, cbzBool);
						
						if(pdfBool || cbzBool){
							progressBar.setValue(90);
							progressBar.setString("Removing Temp");
							
							function.removeTempFolder(albumID);
						}
						
						progressBar.setValue(100);
						progressBar.setString("Finished");
						return null;
					}
					
				};
				
				worker.execute();
			}
		});
	}
}
