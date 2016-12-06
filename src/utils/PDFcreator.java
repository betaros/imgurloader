package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import main.Debug;

public class PDFcreator implements Runnable
{
	
	private List<String> fileList;
	private String SOURCE_FOLDER;
	private String OUTPUT_PDF_FILE;
	private Debug debug;
	
	public PDFcreator(String _source, String _output){
		this.SOURCE_FOLDER = _source;
		this.OUTPUT_PDF_FILE = _output;
		fileList = new ArrayList<String>();
		this.debug = new Debug(this.getClass().getName());
	}
	
	//public void start(){
	//	generateFileList(new File(SOURCE_FOLDER));
	//	generatePDF(OUTPUT_PDF_FILE);
	//}
	
	private void generatePDF(String outputfile){
		try{
			debug.DebugMessage("Output to PDF : " + outputfile);
			
			PDDocument document = new PDDocument();
			for(String file : this.fileList){
				String fullpath = SOURCE_FOLDER + "\\" + file;
				
				debug.DebugMessage("File Added : " + fullpath);
				InputStream in = new FileInputStream(fullpath);
				BufferedImage bimg = ImageIO.read(in);
				float width = bimg.getWidth();
				float height = bimg.getHeight();
				PDPage page = new PDPage(new PDRectangle(width, height));
				document.addPage(page);
				
				PDImageXObject img = PDImageXObject.createFromFile(fullpath, document);
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
				contentStream.drawImage(img, 0, 0);
				contentStream.close();
				in.close();
			}

			document.save(outputfile);
			document.close();
			
			debug.DebugMessage("Done");
		}catch(IOException ex){
			ex.printStackTrace();   
		}
	}
	
	/**
	 * Traverse a directory and get all files,
	 * and add the file into fileList  
	 * @param node file or directory
	 */
	private void generateFileList(File node){

		//add file only
		if(node.isFile()){
			fileList.add(generatePDFEntry(node.getAbsoluteFile().toString()));
		}

		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
	}
	
	/**
	 * Format the file path for zip
	 * @param file file path
	 * @return Formatted file path
	 */
	private String generatePDFEntry(String file){
		return file.substring(SOURCE_FOLDER.length()+1, file.length());
	}

	@Override
	public void run() {
		generateFileList(new File(SOURCE_FOLDER));
		generatePDF(OUTPUT_PDF_FILE);
	}
}
