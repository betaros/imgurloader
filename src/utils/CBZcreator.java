package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import main.Debug;

// https://www.mkyong.com/java/how-to-compress-files-in-zip-format/

public class CBZcreator implements Runnable
{
	private List<String> fileList;
	private String SOURCE_FOLDER;
	private String OUTPUT_ZIP_FILE;
	private Debug debug;
	
	/**
	 * 
	 * @param _source
	 * @param _output
	 */
	public CBZcreator(String _source, String _output){
		this.SOURCE_FOLDER = _source;
		this.OUTPUT_ZIP_FILE = _output;
		fileList = new ArrayList<String>();
		this.debug = new Debug(this.getClass().getName());
	}
	
	/**
	 * Zip it
	 * @param zipFile output ZIP file location
	 */
	private void zipIt(String zipFile){

		byte[] buffer = new byte[1024];

		try{
			String dash = "/";
			
			if(debug.getOS().indexOf("win") >= 0){
				dash = "\\";
			}
			
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			debug.DebugMessage("Output to Zip : " + zipFile);

			for(String file : this.fileList){

				debug.DebugMessage("File Added : " + file);
				ZipEntry ze= new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = 
						new FileInputStream(SOURCE_FOLDER + dash + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			//remember close it
			zos.close();

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
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
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
	private String generateZipEntry(String file){
		return file.substring(SOURCE_FOLDER.length()+1, file.length());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		generateFileList(new File(SOURCE_FOLDER));
		zipIt(OUTPUT_ZIP_FILE);
	}
}